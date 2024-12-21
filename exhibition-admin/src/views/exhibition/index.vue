<template>
  <div class="app-container">
    <el-card class="box-card">
      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryRef" :inline="true">
        <el-form-item label="展会名称" prop="name">
          <el-input v-model="queryParams.name" placeholder="请输入展会名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="国家" prop="country">
          <el-row :gutter="10">
            <el-col :span="12">
              <el-select v-model="searchContinent" placeholder="请选择大洲" @change="handleSearchContinentChange" style="width: 120px">
                <el-option
                  v-for="continent in continentList"
                  :key="continent.code"
                  :label="continent.name"
                  :value="continent.code"
                />
              </el-select>
            </el-col>
            <el-col :span="12">
              <el-select v-model="queryParams.country" placeholder="请选择国家" @change="handleSearchCountryChange" :disabled="!searchContinent" style="width: 120px">
                <el-option
                  v-for="country in searchCountryList"
                  :key="country.id"
                  :label="country.countryName"
                  :value="country.id"
                />
              </el-select>
            </el-col>
          </el-row>
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
      <el-table v-loading="loading" :data="exhibitionList">
        <el-table-column type="index" label="序号" width="50" />
        <el-table-column prop="name" label="展会名称" />
        <el-table-column prop="time" label="时间" width="160" />
        <el-table-column prop="location" label="地址" show-overflow-tooltip />
        <el-table-column prop="countryName" label="国家" width="120" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column label="操作" width="180" align="center">
          <template #default="scope">
            <el-button type="primary" link icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :small="false"
          :disabled="false"
          :background="true"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>

      <!-- 添加或修改展会对话框 -->
      <el-dialog :title="title" v-model="open" width="800px" append-to-body>
        <el-form ref="exhibitionRef" :model="form" :rules="rules" label-width="100px">
          <el-form-item label="展会名称" prop="name">
            <el-input v-model="form.name" placeholder="请输入展会名称" />
          </el-form-item>
          <el-form-item label="举办地址" prop="location">
            <el-input v-model="form.location" placeholder="请输入举办地址" />
          </el-form-item>
          <el-form-item label="文件" prop="fileUrl">
            <el-upload
              class="file-uploader"
              :action="uploadUrl"
              name="file"
              :show-file-list="true"
              :limit="1"
              :on-success="handleUploadSuccess"
              :before-upload="beforeUpload"
              :file-list="fileList"
              :on-remove="handleRemove"
            >
              <el-button type="primary">点击上传</el-button>
              <template #tip>
                <div class="el-upload__tip">
                  只能上传doc、docx、pdf格式文件
                </div>
              </template>
            </el-upload>
          </el-form-item>
          <el-form-item label="时间" prop="time">
            <el-date-picker
              v-model="form.time"
              type="date"
              placeholder="请选择时间"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              @change="handleTimeChange"
            />
          </el-form-item>
          <el-form-item label="年月" prop="year">
            <el-row :gutter="10">
              <el-col :span="12">
                <el-input v-model="form.year" placeholder="年份" disabled />
              </el-col>
              <el-col :span="12">
                <el-input v-model="form.month" placeholder="月份" disabled />
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item label="国家" prop="country">
            <el-row :gutter="10">
              <el-col :span="12">
                <el-select v-model="selectedContinent" placeholder="请选择大洲" @change="handleContinentChange" class="area-select">
                  <el-option
                    v-for="continent in continentList"
                    :key="continent.code"
                    :label="continent.name"
                    :value="continent.code"
                  />
                </el-select>
              </el-col>
              <el-col :span="12">
                <el-select v-model="form.country" placeholder="请选择国家" @change="handleCountryChange" :disabled="!selectedContinent" class="area-select">
                  <el-option
                    v-for="country in countryList"
                    :key="country.id"
                    :label="country.countryName"
                    :value="country.id"
                  />
                </el-select>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item label="排序" prop="sort">
            <el-input-number v-model="form.sort" :min="0" :max="999" controls-position="right" />
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
import { ref, reactive, onMounted, computed, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, UploadProps } from 'element-plus'
import { listExhibition, getExhibition, delExhibition, saveExhibition, findGlobalArea } from '@/api/exhibition'
import type { Exhibition, ExhibitionQuery, GlobalArea } from '@/types/exhibition'
import { getImageUrl } from '@/utils/request'

const loading = ref(false)
const total = ref(0)
const open = ref(false)
const title = ref('')
const exhibitionList = ref<Exhibition[]>([])
const uploadUrl = '/api/file/upload'
const areaOptions = ref<GlobalArea[]>([])

// 查询参数
const queryParams = reactive<ExhibitionQuery>({
  pageNum: 1,
  pageSize: 10,
  name: undefined,
  country: undefined
})

// 表单参数
const form = reactive<Exhibition>({
  name: '',
  location: '',
  time: '',
  year: '',
  month: '',
  sort: 0,
  fileUrl: '',
  country: 0,
  countryName: ''
})

// 表单校验规则
const rules = reactive({
  name: [{ required: true, message: '展会名称不能为空', trigger: 'blur' }],
  fileUrl: [{ required: true, message: '请上传文件', trigger: 'blur' }],
  time: [{ required: true, message: '请选择时间', trigger: 'change' }],
  country: [{ required: true, message: '请选择国家', trigger: 'change' }],
  location: [{ required: true, message: '举办地址不能为空', trigger: 'blur' }],
  sort: [{ required: true, message: '排序不能空', trigger: 'blur' }]
})

const queryRef = ref<FormInstance>()
const exhibitionRef = ref<FormInstance>()

const fileList = ref<any[]>([])

const selectedContinent = ref<string>('')

const continentList = computed(() => {
  const continents = new Set<string>()
  const list: { code: string; name: string }[] = []
  areaOptions.value.forEach(area => {
    if (!continents.has(area.continentCode)) {
      continents.add(area.continentCode)
      list.push({
        code: area.continentCode,
        name: area.continentName
      })
    }
  })
  return list
})

const countryList = computed(() => {
  if (!selectedContinent.value) return []
  return areaOptions.value.filter(area => area.continentCode === selectedContinent.value)
})

// 搜索用的大洲选择
const searchContinent = ref<string>('')
// 搜索用的国家列表
const searchCountryList = computed(() => {
  if (!searchContinent.value) return []
  return areaOptions.value.filter(area => area.continentCode === searchContinent.value)
})

/** 获取国家列表 */
const getAreaOptions = async () => {
  try {
    const res = await findGlobalArea()
    areaOptions.value = res.data
  } catch (error) {
    console.error(error)
  }
}

/** 处理时间选择变化 */
const handleTimeChange = (val: string) => {
  if (val) {
    const date = new Date(val)
    form.year = date.getFullYear().toString()
    form.month = (date.getMonth() + 1).toString().padStart(2, '0')
  } else {
    form.year = ''
    form.month = ''
  }
}

/** 处理大洲选择变化 */
const handleContinentChange = (val: string) => {
  form.country = 0
  form.countryName = ''
}

/** 处理国家选择变化 */
const handleCountryChange = (value: number) => {
  if (!value) {
    form.countryName = ''
    return
  }
  const country = areaOptions.value.find(item => item.id === value)
  if (country) {
    form.countryName = country.countryName
  }
}

/** 查询展会列表 */
const getList = async () => {
  loading.value = true
  try {
    const res = await listExhibition(queryParams)
    exhibitionList.value = res.rows
    total.value = res.total
  } catch (error) {
    console.error(error)
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
  searchContinent.value = ''
  queryRef.value?.resetFields()
  handleQuery()
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset()
  open.value = true
  title.value = '添加展会'
}

/** 修改按钮操作 */
const handleUpdate = async (row: Exhibition) => {
  reset()
  try {
    // 先加载区域数据
    if (areaOptions.value.length === 0) {
      await getAreaOptions()
    }

    const res = await getExhibition(row.id!)
    
    // 设置选中的大洲和国家
    if (res.data.country) {
      const country = areaOptions.value.find(item => item.id === res.data.country)
      if (country) {
        selectedContinent.value = country.continentCode
        // 等待下一个 tick，确保 countryList 已更新
        await nextTick()
        res.data.countryName = country.countryName
      }
    }
    
    // 设置表单数据
    Object.assign(form, res.data)
    
    if (form.fileUrl) {
      fileList.value = [{
        name: form.fileUrl.substring(form.fileUrl.lastIndexOf('/') + 1),
        url: getImageUrl(form.fileUrl)
      }]
    }
    
    title.value = '修改展会'
    open.value = true
  } catch (error) {
    console.error(error)
  }
}

/** 提交按钮 */
const submitForm = async () => {
  exhibitionRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        await saveExhibition(form)
        ElMessage.success('保存成功')
        open.value = false
        getList()
      } catch (error) {
        console.error(error)
      }
    }
  })
}

/** 删除按钮操作 */
const handleDelete = async (row: Exhibition) => {
  try {
    await ElMessageBox.confirm('是否确认删除该展会？', '提示', {
      type: 'warning'
    })
    await delExhibition(row.id!)
    ElMessage.success('删除成功')
    getList()
  } catch (error) {
    console.error(error)
  }
}

/** 表单重置 */
const reset = () => {
  form.id = undefined
  form.name = ''
  form.location = ''
  form.time = ''
  form.year = ''
  form.month = ''
  form.sort = 0
  form.fileUrl = ''
  form.country = 0
  form.countryName = ''
  selectedContinent.value = ''
  fileList.value = []
  exhibitionRef.value?.resetFields()
}

/** 上传成功回调 */
const handleUploadSuccess: UploadProps['onSuccess'] = (response) => {
  if (response.code === 200) {
    form.fileUrl = response.data
    fileList.value = [{
      name: response.data.substring(response.data.lastIndexOf('/') + 1),
      url: getImageUrl(response.data)
    }]
    ElMessage.success('上传成功')
  } else {
    ElMessage.error(response.msg || '上传失败')
  }
}

/** 移除文件 */
const handleRemove = () => {
  form.fileUrl = ''
  fileList.value = []
}

/** 上传前的校验 */
const beforeUpload: UploadProps['beforeUpload'] = (file) => {
  const allowedTypes = [
    'application/msword',                                                    // .doc
    'application/vnd.openxmlformats-officedocument.wordprocessingml.document', // .docx
    'application/pdf'                                                        // .pdf
  ]
  const isAllowed = allowedTypes.includes(file.type)
  if (!isAllowed) {
    ElMessage.error('只能上传doc、docx、pdf格式文件!')
    return false
  }
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('上传文件大小不能超过 10MB!')
    return false
  }
  return true
}

/** 取消按钮操作 */
const cancel = () => {
  open.value = false
  reset()
}

/** 分页大小改变 */
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  getList()
}

/** 页码改变 */
const handleCurrentChange = (val: number) => {
  queryParams.pageNum = val
  getList()
}

/** 处理搜索大洲选择变化 */
const handleSearchContinentChange = (val: string) => {
  queryParams.country = undefined
}

/** 处理搜索国家选择变化 */
const handleSearchCountryChange = (value: number) => {
  // 可以在这里添加额外的处理逻辑
}

onMounted(() => {
  getList()
  getAreaOptions()
})
</script>

<style scoped>
.file-uploader {
  width: 360px;
}

.file-uploader .el-upload {
  width: 100%;
}

.file-uploader .el-upload-list {
  margin-top: 10px;
}

.el-upload__tip {
  margin-top: 8px;
  color: var(--el-text-color-secondary);
  font-size: 12px;
}

.area-select {
  width: 100%;
  min-width: 200px;
}
</style> 