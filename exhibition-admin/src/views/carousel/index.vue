<template>
  <div class="carousel-container">
    <el-card class="box-card">
      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryRef" :inline="true">
        <el-form-item label="标题" prop="title">
          <el-input v-model="queryParams.title" placeholder="请输入标题" clearable style="width: 200px" @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="轮播图状态" clearable style="width: 200px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
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
      <el-table v-loading="loading" :data="carouselList" style="width: 100%" border>
        <el-table-column type="index" label="序号" width="50" />
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="imageUrl" label="图片" width="100">
          <template #default="scope">
            <el-image 
              :src="getImageUrl(scope.row.imageUrl)" 
              :preview-src-list="[getImageUrl(scope.row.imageUrl)]" 
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
        <el-table-column prop="detail" label="详情" show-overflow-tooltip>
          <template #default="scope">
            <div v-html="scope.row.detail"></div>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
              {{ scope.row.status === 1 ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template #default="scope">
            <el-button type="primary" link icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <Pagination
          v-if="total > 0"
          :total="total"
          :page="queryParams.pageNum"
          :limit="queryParams.pageSize"
          @pagination="handlePagination"
        />
      </div>

      <!-- 添加或修改轮播图对话框 -->
      <el-dialog :title="title" v-model="open" width="500px" append-to-body>
        <el-form ref="carouselRef" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="标题" prop="title">
            <el-input v-model="form.title" placeholder="请输入标题" />
          </el-form-item>
          <el-form-item label="图片" prop="imageUrl">
            <el-upload
              class="avatar-uploader"
              :action="uploadUrl"
              name="file"
              :show-file-list="false"
              :on-success="handleUploadSuccess"
              :before-upload="beforeUpload"
            >
              <el-image 
                v-if="form.imageUrl" 
                :src="getImageUrl(form.imageUrl)" 
                :preview-src-list="[getImageUrl(form.imageUrl)]"
                preview-teleported
                class="avatar" 
              />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>
          <el-form-item label="链接" prop="link">
            <el-input v-model="form.link" placeholder="请输入链接" />
          </el-form-item>
          <el-form-item label="排序" prop="sort">
            <el-input-number v-model="form.sort" :min="0" :max="999" controls-position="right" />
          </el-form-item>
          <el-form-item label="状态">
            <el-radio-group v-model="form.status">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="0">禁用</el-radio>
            </el-radio-group>
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
import type {FormInstance, UploadProps} from 'element-plus'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Picture, Plus} from '@element-plus/icons-vue'
import {delCarousel, getCarousel, listCarousel, saveCarousel} from '@/api/carousel'
import type {Carousel, CarouselQuery} from '@/types/carousel'
import {getImageUrl} from '@/utils/request'
import Pagination from '@/components/Pagination/index.vue'

const loading = ref(false)
const total = ref(0)
const open = ref(false)
const title = ref('')
const carouselList = ref<Carousel[]>([])
const uploadUrl = '/api/file/upload'

// 查询参数
const queryParams = reactive<CarouselQuery>({
  pageNum: 1,
  pageSize: 10,
  title: undefined,
  status: undefined
})

// 表单参数
const form = reactive<Carousel>({
  title: '',
  imageUrl: '',
  link: '',
  sort: 0,
  status: 1
})

// 表单校验规则
const rules = reactive({
  title: [{ required: true, message: '标题不能为空', trigger: 'blur' }],
  imageUrl: [{ required: true, message: '请上传图片', trigger: 'blur' }],
  link: [{ required: true, message: '链接不能为空', trigger: 'blur' }],
  sort: [{ required: true, message: '排序不能为空', trigger: 'blur' }]
})

const queryRef = ref<FormInstance>()
const carouselRef = ref<FormInstance>()

/** 查询轮播图列表 */
const getList = async () => {
  loading.value = true
  try {
    const res = await listCarousel(queryParams)
    carouselList.value = res.rows
    total.value = res.total
  } catch (error) {
    console.error(error)
  }
  loading.value = false
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

/** 新增按钮操作 */
const handleAdd = () => {
  reset()
  open.value = true
  title.value = '添加轮播图'
}

/** 修改按钮操作 */
const handleUpdate = async (row: Carousel) => {
  reset()
  const id = row.id || 0
  const res = await getCarousel(id)
  Object.assign(form, res.data)
  open.value = true
  title.value = '修改轮播图'
}

/** 提交按钮 */
const submitForm = async () => {
  carouselRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        await saveCarousel(form)
        ElMessage.success('操作成功')
        open.value = false
        getList()
      } catch (error) {
        console.error(error)
      }
    }
  })
}

/** 删除按钮操作 */
const handleDelete = (row: Carousel) => {
  const id = row.id
  if (!id) return
  ElMessageBox.confirm('是否确认删除该轮播图?', '警告', {
    type: 'warning'
  }).then(async () => {
    await delCarousel(id)
    ElMessage.success('删除成功')
    getList()
  })
}

/** 表单重置 */
const reset = () => {
  form.id = undefined
  form.title = ''
  form.imageUrl = ''
  form.link = ''
  form.sort = 0
  form.status = 1
  carouselRef.value?.resetFields()
}

/** 上传成功回调 */
const handleUploadSuccess: UploadProps['onSuccess'] = (response) => {
  if (response.code === 200) {
    form.imageUrl = response.data
    ElMessage.success('上传成功')
  } else {
    ElMessage.error(response.msg || '上传失败')
  }
}

/** 上传前的校验 */
const beforeUpload: UploadProps['beforeUpload'] = (file) => {
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  const isLt2M = file.size / 1024 / 1024 < 10
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 10MB!')
    return false
  }
  return true
}

/** 取消按钮操作 */
const cancel = () => {
  open.value = false
  reset()
}

/** 处理分页变化 */
const handlePagination = ({ page, limit }: { page: number; limit: number }) => {
  queryParams.pageNum = page
  queryParams.pageSize = limit
  getList()
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.carousel-container {
  padding: 20px;
  background: white;
  border-radius: 4px;
}

.header {
  margin-bottom: 20px;
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
</style> 