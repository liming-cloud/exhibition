<template>
  <div class="cases-container">
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
        <el-table-column label="封面图" width="100">
          <template #default="{ row }">
            <el-image
              v-if="row.coverUrl"
              :src="getImageUrl(row.coverUrl)"
              :preview-src-list="[getImageUrl(row.coverUrl)]"
              preview-teleported
              fit="cover"
              style="width: 50px; height: 50px; cursor: pointer"
            >
              <template #error>
                <div class="image-slot">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
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
          <el-form-item label="封面图" prop="coverUrl">
            <el-upload
              class="avatar-uploader"
              :action="uploadApi"
              :show-file-list="false"
              :on-success="handleUploadSuccess"
              :before-upload="beforeUpload"
            >
              <img
                v-if="form.coverUrl"
                :src="getImageUrl(form.coverUrl)"
                class="avatar"
              />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
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
import {Picture, Plus} from '@element-plus/icons-vue'
import {getImageUrl} from '@/utils/request'
import {prepareImage} from '@/utils/image'
import type {Cases, CasesQuery} from '@/types/cases'
import {deleteCases, getCases, listCases, saveCases} from '@/api/cases'
import Editor from '@/components/Editor/index.vue'
import Pagination from '@/components/Pagination/index.vue'

const uploadApi = '/api/file/upload'

const loading = ref(false)
const total = ref(0)
const list = ref<Cases[]>([])
const formRef = ref<FormInstance>()
const queryRef = ref<FormInstance>()

const queryParams = reactive<CasesQuery>({
  pageNum: 1,
  pageSize: 10,
  title: ''
})

const dialog = reactive({
  title: '',
  visible: false
})

const form = reactive<Cases>({
  id: undefined,
  title: '',
  content: '',
  coverUrl: ''
})

const rules = reactive({
  title: [{ required: true, message: '请输输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
  coverUrl: [{ required: true, message: '请上传封面图', trigger: 'change' }]
})

/** 查询列表 */
const getList = async () => {
  try {
    loading.value = true
    const res = await listCases(queryParams)
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
  form.coverUrl = ''
}

/** 取消按钮 */
const cancel = () => {
  resetForm()
  dialog.visible = false
}

/** 新增按钮操作 */
const handleAdd = () => {
  resetForm()
  dialog.title = '新增案例'
  dialog.visible = true
}

/** 编辑按钮操作 */
const handleUpdate = async (row: Cases) => {
  try {
    const res = await getCases(row.id!)
    Object.assign(form, res.data)
    dialog.title = '编辑案例'
    dialog.visible = true
  } catch (error) {
    console.error('获取详情失败:', error)
  }
}

/** 提交按钮 */
const submitForm = async () => {
  const formEl = formRef.value
  if (!formEl) return
  await formEl.validate(async (valid) => {
    if (valid) {
      try {
        const res = await saveCases(form)
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
const handleDelete = (row: Cases) => {
  ElMessageBox.confirm('是否确认删除该案例?', '警告', {
    type: 'warning'
  }).then(async () => {
    const res = await deleteCases(row.id!)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      getList()
    }
  })
}

/** 上传成功回调 */
const handleUploadSuccess = (res: any) => {
  if (res.code === 200) {
    form.coverUrl = res.data
  } else {
    ElMessage.error('上传失败')
  }
}

/** 上传前的校验与压缩 */
const beforeUpload = (file: File) => prepareImage(file, 5)

onMounted(() => {
  getList()
})
</script>

<style scoped>
.cases-container {
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

.avatar-uploader {
  width: 178px;
  height: 178px;
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
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

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: var(--el-fill-color-light);
  color: var(--el-text-color-secondary);
}
</style> 