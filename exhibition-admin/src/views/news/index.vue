<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">资讯管理</h2>
      <el-button type="primary" class="primary-button" @click="handleAdd">
        <el-icon><Plus /></el-icon>添加资讯
      </el-button>
    </div>

    <div class="table-container">
      <el-table 
        :data="tableData" 
        style="width: 100%" 
        v-loading="loading"
        border
        stripe
        highlight-current-row
      >
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column label="封面图" width="180">
          <template #default="scope">
            <el-image 
              style="width: 120px; height: 80px" 
              :src="scope.row.imageUrl"
              fit="cover"
              :preview-src-list="[scope.row.imageUrl]"
              class="table-image"
            >
              <template #error>
                <div class="image-error">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status ? 'success' : 'info'" effect="dark">
              {{ scope.row.status ? '发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button-group>
              <el-button type="primary" link @click="handleEdit(scope.row)">
                <el-icon><Edit /></el-icon>编辑
              </el-button>
              <el-button type="danger" link @click="handleDelete(scope.row)">
                <el-icon><Delete /></el-icon>删除
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="800px"
      destroy-on-close
      :close-on-click-modal="false"
    >
      <el-form 
        :model="form" 
        :rules="rules" 
        ref="formRef" 
        label-width="80px"
        class="form-container"
      >
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="封面图" prop="imageUrl">
          <el-upload
            class="upload-container"
            action="/api/upload"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
          >
            <img v-if="form.imageUrl" :src="form.imageUrl" class="upload-image" />
            <div v-else class="upload-icon">
              <el-icon><Plus /></el-icon>
              <div class="upload-text">点击上传</div>
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <div class="editor-container">
            <Toolbar
              class="editor-toolbar"
              :editor="editorRef"
              :defaultConfig="toolbarConfig"
              mode="default"
            />
            <Editor
              class="editor-content"
              v-model="form.content"
              :defaultConfig="editorConfig"
              mode="default"
              @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch
            v-model="form.status"
            active-text="发布"
            inactive-text="草稿"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="loading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, shallowRef } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Picture } from '@element-plus/icons-vue'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import type { FormInstance, FormRules } from 'element-plus'
import type { News } from '@/types'
import type { IDomEditor } from '@wangeditor/editor'

defineOptions({
  name: 'NewsPage'
})

const loading = ref<boolean>(false)
const dialogVisible = ref<boolean>(false)
const dialogTitle = ref<string>('')
const formRef = ref<FormInstance>()
const editorRef = shallowRef<IDomEditor>()

// 编辑器配置
const toolbarConfig = {}
const editorConfig = {
  placeholder: '请输入内容...'
}

interface NewsForm extends Omit<News, 'id'> {
  id: number | null;
}

const tableData = ref<News[]>([
  {
    id: 1,
    title: '示例资讯',
    imageUrl: 'https://placeholder.co/800x400',
    content: '示例内容',
    createTime: '2023-12-18 12:00:00',
    status: true
  }
])

const form = reactive<NewsForm>({
  id: null,
  title: '',
  imageUrl: '',
  content: '',
  status: true,
  createTime: ''
})

const rules: FormRules = {
  title: [
    { required: true, message: '请输入标题', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  imageUrl: [
    { required: true, message: '请上传封面图', trigger: 'change' }
  ],
  content: [
    { required: true, message: '请输入内容', trigger: 'blur' }
  ]
}

const resetForm = (): void => {
  form.id = null
  form.title = ''
  form.imageUrl = ''
  form.content = ''
  form.status = true
  form.createTime = new Date().toLocaleString()
}

const handleCreated = (editor: IDomEditor): void => {
  editorRef.value = editor
}

const handleAdd = (): void => {
  resetForm()
  dialogTitle.value = '添加资讯'
  dialogVisible.value = true
}

const handleEdit = (row: News): void => {
  Object.assign(form, row)
  dialogTitle.value = '编辑资讯'
  dialogVisible.value = true
}

const handleDelete = (row: News): void => {
  ElMessageBox.confirm(
    '确定要删除这条资讯吗？此操作不可恢复',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    const index = tableData.value.findIndex(item => item.id === row.id)
    if (index > -1) {
      tableData.value.splice(index, 1)
      ElMessage.success('删除成功')
    }
  }).catch(() => {})
}

const handleUploadSuccess = (response: { url: string }): void => {
  form.imageUrl = response.url
  ElMessage.success('上传成功')
}

const handleSubmit = async (): Promise<void> => {
  if (!formRef.value) return
  
  const valid = await formRef.value.validate()
  if (valid) {
    loading.value = true
    try {
      if (form.id) {
        // 编辑
        const index = tableData.value.findIndex(item => item.id === form.id)
        if (index > -1) {
          tableData.value[index] = { ...form as News }
        }
      } else {
        // 新增
        const newId = tableData.value.length + 1
        tableData.value.push({
          ...form,
          id: newId,
          createTime: new Date().toLocaleString()
        } as News)
      }
      dialogVisible.value = false
      ElMessage.success(form.id ? '编辑成功' : '添加成功')
    } finally {
      loading.value = false
    }
  }
}
</script>

<style scoped>
.table-image {
  border-radius: 4px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.image-error {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 24px;
}

.upload-text {
  font-size: 14px;
  color: #8c939d;
  margin-top: 8px;
}

:deep(.el-upload) {
  width: 178px;
  height: 178px;
}
</style> 