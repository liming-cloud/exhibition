<template>
  <div class="news-container">
    <div class="header">
      <el-button type="primary" @click="handleAdd">
        添加新闻
      </el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="content" label="内容" show-overflow-tooltip />
      <el-table-column label="封面图" width="180">
        <template #default="{ row }">
          <el-image
            style="width: 100px; height: 60px"
            :src="row.image"
            fit="cover"
          />
        </template>
      </el-table-column>
      <el-table-column prop="publishTime" label="发布时间" width="120" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status ? 'success' : 'info'">
            {{ row.status ? '已发布' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button
            type="primary"
            link
            @click="handleEdit(row)"
          >
            编辑
          </el-button>
          <el-button
            type="danger"
            link
            @click="handleDelete(row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
    >
      <el-form :model="currentItem" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="currentItem.title" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input
            v-model="currentItem.content"
            type="textarea"
            :rows="4"
          />
        </el-form-item>
        <el-form-item label="封面图">
          <el-upload
            class="avatar-uploader"
            :show-file-list="false"
            :before-upload="handleUpload"
          >
            <img
              v-if="currentItem.image"
              :src="currentItem.image"
              class="avatar"
            >
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="发布时间">
          <el-date-picker
            v-model="currentItem.publishTime"
            type="date"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch
            v-model="currentItem.status"
            :active-text="'已发布'"
            :inactive-text="'草稿'"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { UploadProps } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import type { NewsItem } from '@/types'

const tableData = ref<NewsItem[]>([
  {
    id: 1,
    title: '示例新闻',
    content: '这是一条示例新闻内容',
    image: 'https://example.com/news1.jpg',
    publishTime: '2024-01-01',
    status: true
  }
])

const dialogVisible = ref(false)
const dialogTitle = ref('添加新闻')
const currentItem = ref<NewsItem>({
  id: 0,
  title: '',
  content: '',
  image: '',
  publishTime: new Date().toISOString().split('T')[0],
  status: true
})

const handleAdd = () => {
  dialogTitle.value = '添加新闻'
  currentItem.value = {
    id: 0,
    title: '',
    content: '',
    image: '',
    publishTime: new Date().toISOString().split('T')[0],
    status: true
  }
  dialogVisible.value = true
}

const handleEdit = (row: NewsItem) => {
  dialogTitle.value = '编辑新闻'
  currentItem.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = (row: NewsItem) => {
  ElMessageBox.confirm('确认删除该新闻吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = tableData.value.findIndex(item => item.id === row.id)
    if (index > -1) {
      tableData.value.splice(index, 1)
      ElMessage.success('删除成功')
    }
  })
}

const handleUpload: UploadProps['beforeUpload'] = (file) => {
  // 这里应该调用上传接口，这里仅做演示
  const reader = new FileReader()
  reader.readAsDataURL(file)
  reader.onload = () => {
    currentItem.value.image = reader.result as string
  }
  return false
}

const handleSubmit = () => {
  if (!currentItem.value.title || !currentItem.value.content) {
    ElMessage.error('请填写完整信息')
    return
  }

  if (currentItem.value.id === 0) {
    // 新增
    currentItem.value.id = tableData.value.length + 1
    tableData.value.push({ ...currentItem.value })
  } else {
    // 编辑
    const index = tableData.value.findIndex(item => item.id === currentItem.value.id)
    if (index > -1) {
      tableData.value[index] = { ...currentItem.value }
    }
  }
  
  dialogVisible.value = false
  ElMessage.success('保存成功')
}
</script>

<style scoped>
.avatar-uploader {
  width: 178px;
  height: 178px;
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader:hover {
  border-color: var(--el-color-primary);
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
  line-height: 178px;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style> 