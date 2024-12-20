<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-card class="search-wrapper">
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="标题" prop="title">
          <el-input
            v-model="queryParams.title"
            placeholder="请输入标题"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮区域 -->
    <el-card class="table-wrapper">
      <template #header>
        <el-button type="primary" @click="handleAdd">新增轮播图</el-button>
      </template>

      <!-- 表格区域 -->
      <el-table v-loading="loading" :data="carouselList">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="imageUrl" label="图片" width="120">
          <template #default="{ row }">
            <el-image
              :src="row.imageUrl"
              :preview-src-list="[row.imageUrl]"
              fit="cover"
              class="table-image"
            />
          </template>
        </el-table-column>
        <el-table-column prop="link" label="链接" show-overflow-tooltip />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
      append-to-body
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="图片" prop="imageUrl">
          <el-upload
            class="avatar-uploader"
            :action="import.meta.env.VITE_APP_UPLOAD_URL"
            :show-file-list="false"
            :before-upload="beforeUpload"
            :on-success="handleUploadSuccess"
          >
            <img v-if="form.imageUrl" :src="form.imageUrl" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="链接" prop="link">
          <el-input v-model="form.link" placeholder="请输入链接" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, FormInstance } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import type { Carousel, CarouselQuery } from '@/types/carousel'
import {
  getCarouselList,
  addCarousel,
  updateCarousel,
  deleteCarousel,
  updateCarouselStatus
} from '@/api/carousel'

// 查询参数
const queryParams = reactive<CarouselQuery>({
  pageNum: 1,
  pageSize: 10,
  title: '',
  status: undefined
})

// 数据列表
const carouselList = ref<Carousel[]>([])
const total = ref(0)
const loading = ref(false)

// 表单相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const form = reactive<Carousel>({
  title: '',
  imageUrl: '',
  link: '',
  sort: 0,
  status: 1
})

// 表单校验规则
const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  imageUrl: [{ required: true, message: '请上传图片', trigger: 'change' }],
  link: [{ required: true, message: '请输入链接', trigger: 'blur' }],
  sort: [{ required: true, message: '请输入排序', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

// 获取列表数据
const getList = async () => {
  try {
    loading.value = true
    const { rows, total: totalCount } = await getCarouselList(queryParams)
    carouselList.value = rows
    total.value = totalCount
  } catch (error) {
    console.error('获取轮播图列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 查询操作
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

// 重置查询
const resetQuery = () => {
  queryParams.title = ''
  queryParams.status = undefined
  handleQuery()
}

// 分页操作
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  getList()
}

const handleCurrentChange = (val: number) => {
  queryParams.pageNum = val
  getList()
}

// 新增操作
const handleAdd = () => {
  dialogTitle.value = '新增轮播图'
  dialogVisible.value = true
  Object.assign(form, {
    title: '',
    imageUrl: '',
    link: '',
    sort: 0,
    status: 1
  })
}

// 编辑操作
const handleEdit = (row: Carousel) => {
  dialogTitle.value = '编辑轮播图'
  dialogVisible.value = true
  Object.assign(form, row)
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (form.id) {
          await updateCarousel(form)
          ElMessage.success('修改成功')
        } else {
          await addCarousel(form)
          ElMessage.success('新增成功')
        }
        dialogVisible.value = false
        getList()
      } catch (error) {
        console.error('保存失败:', error)
      }
    }
  })
}

// 删除操作
const handleDelete = (row: Carousel) => {
  ElMessageBox.confirm('确认要删除该轮播图吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCarousel(row.id!)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除失败:', error)
    }
  })
}

// 状态更新
const handleStatusChange = async (row: Carousel) => {
  try {
    await updateCarouselStatus(row.id!, row.status)
    ElMessage.success('状态更新成功')
  } catch (error) {
    console.error('状态更新失败:', error)
    // 更新失败时恢复状态
    row.status = row.status === 1 ? 0 : 1
  }
}

// 上传相关
const beforeUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

const handleUploadSuccess = (response: any) => {
  form.imageUrl = response.data
}

// 初始化
onMounted(() => {
  getList()
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.search-wrapper {
  margin-bottom: 20px;
}

.table-wrapper {
  margin-bottom: 20px;
}

.table-image {
  width: 80px;
  height: 80px;
  border-radius: 4px;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.avatar-uploader {
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
  width: 120px;
  height: 120px;
  text-align: center;
  line-height: 120px;
}

.avatar {
  width: 120px;
  height: 120px;
  display: block;
}
</style> 