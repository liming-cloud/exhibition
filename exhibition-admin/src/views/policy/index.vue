<template>
  <div class="policy-container">
    <el-card class="box-card">
      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryRef" :inline="true">
        <el-form-item label="标题" prop="title">
          <el-input
            v-model="queryParams.title"
            placeholder="请输入标题"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮区域 -->
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" icon="Plus" @click="handleAdd">新增</el-button>
        </el-col>
      </el-row>

      <!-- 表格区域 -->
      <el-table v-loading="loading" :data="list" style="width: 100%" border>
        <el-table-column type="index" label="序号" width="50" />
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="price" label="补贴金额" width="120">
          <template #default="{ row }">
            {{ formatPrice(row.price) }}
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="180" />
        <el-table-column prop="endTime" label="结束时间" width="180" />
        <el-table-column label="操作" width="180" align="center">
          <template #default="{ row }">
            <el-button type="primary" link icon="Edit" @click="handleUpdate(row)">修改</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <Pagination
          v-if="total > 0"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          :total="total"
          @pagination="getList"
        />
      </div>

      <!-- 添加或修改对话框 -->
      <el-dialog
        :title="dialog.title"
        v-model="dialog.visible"
        width="800px"
        append-to-body
        destroy-on-close
      >
        <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
          <el-form-item label="标题" prop="title">
            <el-input v-model="form.title" placeholder="请输入标题" />
          </el-form-item>
          <el-form-item label="补贴金额" prop="price">
            <el-input-number
              v-model="form.price"
              :min="0"
              :precision="2"
              :step="0.01"
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="时间范围" prop="dateRange">
            <el-date-picker
              v-model="dateRange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              value-format="YYYY-MM-DD HH:mm:ss"
              :default-time="[
                new Date(2000, 1, 1, 0, 0, 0),
                new Date(2000, 1, 1, 23, 59, 59)
              ]"
            />
          </el-form-item>
          <el-form-item label="内容" prop="content">
            <Editor v-model="form.content" />
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref} from 'vue'
import {ElMessage, ElMessageBox, type FormInstance} from 'element-plus'
import type {Subsidies, SubsidiesQuery} from '@/types/subsidies'
import {deleteSubsidies, getSubsidies, listSubsidies, saveCarousel} from '@/api/subsidies'
import Editor from '@/components/Editor/index.vue'
import Pagination from '@/components/Pagination/index.vue'

const loading = ref(false)
const total = ref(0)
const list = ref<Subsidies[]>([])
const formRef = ref<FormInstance>()
const queryRef = ref<FormInstance>()
const dateRange = ref<[string, string]>(['', ''])

const queryParams = reactive<SubsidiesQuery>({
  pageNum: 1,
  pageSize: 10,
  title: ''
})

const dialog = reactive({
  title: '',
  visible: false
})

const form = reactive<Subsidies>({
  id: undefined,
  title: '',
  content: '',
  price: 0,
  startTime: '',
  endTime: ''
})

const rules = reactive({
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
  price: [{ required: true, message: '请输入补贴金额', trigger: 'blur' }]
})

/** 格式化金额 */
const formatPrice = (price: number) => {
  return `¥ ${price}`
}

/** 查询列表 */
const getList = async () => {
  try {
    loading.value = true
    const res = await listSubsidies(queryParams)
    list.value = res.rows
    total.value = res.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryRef.value?.resetFields()
  handleQuery()
}

/** 重置表单 */
const resetForm = () => {
  form.id = undefined
  form.title = ''
  form.content = ''
  form.price = 0
  form.startTime = ''
  form.endTime = ''
  dateRange.value = ['', '']
}

/** 取消按钮 */
const cancel = () => {
  resetForm()
  dialog.visible = false
}

/** 新增按钮操作 */
const handleAdd = () => {
  resetForm()
  dialog.title = '新增补贴信息'
  dialog.visible = true
}

/** 编辑按钮操作 */
const handleUpdate = async (row: Subsidies) => {
  try {
    const res = await getSubsidies(row.id!)
    Object.assign(form, res.data)
    dateRange.value = [form.startTime, form.endTime]
    dialog.title = '编辑补贴信息'
    dialog.visible = true
  } catch (error) {
    console.error('获取详情失败:', error)
  }
}

/** 提交按钮 */
const submitForm = async () => {
  const formEl = formRef.value
  if (!formEl) return

  if (!dateRange.value?.[0] || !dateRange.value?.[1]) {
    ElMessage.error('请选择时间范围')
    return
  }

  form.startTime = dateRange.value[0]
  form.endTime = dateRange.value[1]

  await formEl.validate(async (valid) => {
    if (valid) {
      try {
        const res = await saveCarousel(form)
        if (res.code === 200) {
          ElMessage.success(form.id ? '修改成功' : '新增成功')
          dialog.visible = false
          getList()
        }
      } catch (error) {
        console.error('提交失败:', error)
      }
    }
  })
}

/** 删除按钮操作 */
const handleDelete = (row: Subsidies) => {
  ElMessageBox.confirm('是否确认删除该补贴信息?', '警告', {
    type: 'warning'
  }).then(async () => {
    const res = await deleteSubsidies(row.id!)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      getList()
    }
  })
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.policy-container {
  padding: 20px;
  background: white;
  border-radius: 4px;
}

.mb8 {
  margin-bottom: 8px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 