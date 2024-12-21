<template>
  <div class="policy-container">
    <div class="header">
      <el-button type="primary" @click="handleAdd">
        添加补贴信息
      </el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="content" label="内容" show-overflow-tooltip />
      <el-table-column prop="startTime" label="开始时间" width="120" />
      <el-table-column prop="endTime" label="结束时间" width="120" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status ? 'success' : 'info'">
            {{ row.status ? '生效中' : '已过期' }}
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
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="currentItem.startTime"
            type="date"
            placeholder="开始时间"
            value-format="YYYY-MM-DD"
            style="width: 220px"
          />
          <span class="mx-2">至</span>
          <el-date-picker
            v-model="currentItem.endTime"
            type="date"
            placeholder="结束时间"
            value-format="YYYY-MM-DD"
            style="width: 220px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch
            v-model="currentItem.status"
            :active-text="'生效中'"
            :inactive-text="'已过期'"
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
import type { PolicyItem } from '@/types'

const tableData = ref<PolicyItem[]>([
  {
    id: 1,
    title: '示例补贴信息',
    content: '这是一条示例补贴信息内容',
    startTime: '2024-01-01',
    endTime: '2024-12-31',
    status: true
  }
])

const dialogVisible = ref(false)
const dialogTitle = ref('添加补贴信息')
const currentItem = ref<PolicyItem>({
  id: 0,
  title: '',
  content: '',
  startTime: '',
  endTime: '',
  status: true
})

const handleAdd = () => {
  dialogTitle.value = '添加补贴信息'
  currentItem.value = {
    id: 0,
    title: '',
    content: '',
    startTime: '',
    endTime: '',
    status: true
  }
  dialogVisible.value = true
}

const handleEdit = (row: PolicyItem) => {
  dialogTitle.value = '编辑补贴信息'
  currentItem.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = (row: PolicyItem) => {
  ElMessageBox.confirm('确认删除该补贴信息吗？', '提示', {
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

const handleSubmit = () => {
  if (!currentItem.value.title || !currentItem.value.content || !currentItem.value.startTime || !currentItem.value.endTime) {
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
.policy-container {
  padding: 20px;
  background: white;
  border-radius: 4px;
}

.header {
  margin-bottom: 20px;
}

.mx-2 {
  margin: 0 8px;
}
</style> 