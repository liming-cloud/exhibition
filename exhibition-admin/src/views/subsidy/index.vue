<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">补贴政策管理</h2>
      <el-button type="primary" class="primary-button" @click="handleAdd">
        <el-icon><Plus /></el-icon>添加政策
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
        <el-table-column prop="title" label="政策标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="amount" label="补贴金额" width="120" align="center">
          <template #default="scope">
            <span class="amount">{{ scope.row.amount }}元</span>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="180" />
        <el-table-column prop="endTime" label="结束时间" width="180" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status ? 'success' : 'info'" effect="dark">
              {{ scope.row.status ? '生效中' : '已结束' }}
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
        label-width="100px"
        class="form-container"
      >
        <el-form-item label="政策标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入政策标题" />
        </el-form-item>
        <el-form-item label="补贴金额" prop="amount">
          <el-input-number 
            v-model="form.amount" 
            :min="0" 
            :precision="2"
            :step="100"
            style="width: 200px"
          />
          <span class="unit">元</span>
        </el-form-item>
        <el-form-item label="有效期" required>
          <el-col :span="11">
            <el-form-item prop="startTime">
              <el-date-picker
                v-model="form.startTime"
                type="datetime"
                placeholder="开始时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="2" class="text-center">
            <span class="text-gray-500">-</span>
          </el-col>
          <el-col :span="11">
            <el-form-item prop="endTime">
              <el-date-picker
                v-model="form.endTime"
                type="datetime"
                placeholder="结束时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item label="申请条件" prop="conditions">
          <el-input 
            v-model="form.conditions" 
            type="textarea" 
            rows="3"
            placeholder="请输入申请条件"
          />
        </el-form-item>
        <el-form-item label="政策详情" prop="content">
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
            active-text="生效"
            inactive-text="结束"
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
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import type { FormInstance, FormRules } from 'element-plus'
import type { Subsidy, EditorConfig } from '@/types'
import type { IDomEditor } from '@wangeditor/editor'

defineOptions({
  name: 'SubsidyPage'
})

const loading = ref<boolean>(false)
const dialogVisible = ref<boolean>(false)
const dialogTitle = ref<string>('')
const formRef = ref<FormInstance>()
const editorRef = shallowRef<IDomEditor>()

// 编辑器配置
const toolbarConfig = {}
const editorConfig: EditorConfig = {
  placeholder: '请输入政策详情...'
}

const tableData = ref<Subsidy[]>([
  {
    id: 1,
    title: '2024年度展会补贴政策',
    amount: 5000.00,
    content: '示例内容',
    conditions: '1. 注册满一年的企业\n2. 具有有效营业执照\n3. 参展材料完整',
    startTime: '2024-01-01 00:00:00',
    endTime: '2024-12-31 23:59:59',
    status: true
  }
])

interface SubsidyForm extends Omit<Subsidy, 'id'> {
  id: number | null;
}

const form = reactive<SubsidyForm>({
  id: null,
  title: '',
  amount: 0,
  content: '',
  conditions: '',
  startTime: '',
  endTime: '',
  status: true
})

const rules: FormRules = {
  title: [
    { required: true, message: '请输入政策标题', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  amount: [
    { required: true, message: '请输入补贴金额', trigger: 'blur' }
  ],
  conditions: [
    { required: true, message: '请输入申请条件', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入政策详情', trigger: 'blur' }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ]
}

const resetForm = (): void => {
  form.id = null
  form.title = ''
  form.amount = 0
  form.content = ''
  form.conditions = ''
  form.startTime = ''
  form.endTime = ''
  form.status = true
}

const handleCreated = (editor: IDomEditor): void => {
  editorRef.value = editor
}

const handleAdd = (): void => {
  resetForm()
  dialogTitle.value = '添加政策'
  dialogVisible.value = true
}

const handleEdit = (row: Subsidy): void => {
  Object.assign(form, row)
  dialogTitle.value = '编辑政策'
  dialogVisible.value = true
}

const handleDelete = (row: Subsidy): void => {
  ElMessageBox.confirm(
    '确定要删除这条政策吗？此操作不可恢复',
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
          tableData.value[index] = { ...form as Subsidy }
        }
      } else {
        // 新增
        const newId = tableData.value.length + 1
        tableData.value.push({
          ...form,
          id: newId
        } as Subsidy)
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
.amount {
  color: #f56c6c;
  font-weight: 500;
}

.unit {
  margin-left: 8px;
  color: #606266;
}

.text-center {
  text-align: center;
}

:deep(.el-form-item__content) {
  flex-wrap: nowrap;
}
</style> 