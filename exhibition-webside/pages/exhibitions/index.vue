<template>
  <div class="container mx-auto px-4 py-8">
    <!-- 搜索筛选区 -->
    <div class="bg-white rounded-lg shadow-soft p-6 mb-8">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-4">
        <el-input v-model="query.name" placeholder="搜索展会名称" clearable>
          <template #prefix>
            <i class="fas fa-search text-neutral-400"></i>
          </template>
        </el-input>

        <!-- 新增展会类型筛选 -->
        <el-select v-model="query.exhibitionType" placeholder="展会类型" clearable>
          <el-option label="全部类型" value="" />
          <el-option
            v-for="(label, value) in ExhibitionTypeMap"
            :key="value"
            :label="label"
            :value="value"
          />
        </el-select>

        <el-select
          v-model="selectedContinent"
          placeholder="选择大洲"
          @change="handleContinentChange"
        >
          <el-option label="全部大洲" value="" />
          <el-option
            v-for="continent in continents"
            :key="continent"
            :label="continent"
            :value="continent"
          />
        </el-select>

        <el-select v-model="query.country" placeholder="选择国家" :disabled="!filteredAreas.length">
          <el-option label="全部国家" value="" />
          <el-option
            v-for="area in filteredAreas"
            :key="area.id"
            :label="area.countryName"
            :value="area.id"
          />
        </el-select>
      </div>

      <!-- 搜索按钮改为橙色 -->
      <div class="text-center">
        <el-button
          type="primary"
          @click="handleSearch"
          class="!bg-primary-500 !border-primary-500 hover:!bg-primary-600 px-8 py-2"
        >
          <i class="fas fa-search mr-2"></i>
          搜索展会
        </el-button>
      </div>
    </div>

    <!-- 展会卡片列表 - 增加背景颜色 -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
      <div
        v-for="exhibition in exhibitions"
        :key="exhibition.id"
        class="bg-gradient-to-br from-white to-neutral-50 rounded-xl shadow-elegant hover:shadow-modern overflow-hidden transition-all duration-500 border border-neutral-100"
      >
        <div class="p-6 relative">
          <!-- 展会类型标签 -->
          <div class="absolute top-4 right-4">
            <span
              class="px-3 py-1 bg-gradient-to-r from-primary-500 to-primary-600 text-white text-sm rounded-full"
            >
              {{ ExhibitionTypeMap[exhibition.exhibitionType] || '能源展' }}
            </span>
          </div>

          <h3 class="text-xl font-bold mb-4 text-neutral-800 pr-20">{{ exhibition.name }}</h3>

          <div class="space-y-3 text-neutral-600 mb-6">
            <p class="flex items-center">
              <i class="fas fa-map-marker-alt w-6 text-primary-500"></i>
              {{ exhibition.countryName }} - {{ exhibition.location }}
            </p>
            <p class="flex items-center">
              <i class="fas fa-calendar-alt w-6 text-primary-500"></i>
              {{ exhibition.time }}
            </p>
          </div>

          <!-- 操作按钮区域 -->
          <div class="flex justify-between items-center">
            <NuxtLink
              :to="`/exhibitions/${exhibition.id}`"
              class="inline-flex items-center px-6 py-2 bg-gradient-to-r from-neutral-600 to-neutral-700 text-white rounded-lg hover:shadow-lg transition-all duration-300"
            >
              查看详情
            </NuxtLink>

            <!-- 新增报名按钮 -->
            <el-button
              type="primary"
              @click="openRegistrationModal(exhibition)"
              class="!bg-primary-500 !border-primary-500 hover:!bg-primary-600"
            >
              <i class="fas fa-user-plus mr-2"></i>
              立即报名
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 报名弹窗 -->
    <RegistrationModal
      v-model="showRegistrationModal"
      :exhibition="selectedExhibition"
      @submit="handleRegistration"
    />

    <!-- 分页 -->
    <div class="mt-8 flex justify-center">
      <el-pagination
        v-model:current-page="query.pageNum"
        v-model:page-size="query.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        :pager-count="5"
        locale="zh-cn"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { Exhibition, ExhibitionType } from '~/types/exhibition'
import { ExhibitionTypeMap } from '~/types/exhibition'
import RegistrationModal from '~/components/common/RegistrationModal.vue'
import { ElMessage } from 'element-plus'

interface QueryState {
  year: string
  continent: string
  country: number | ''
  pageNum: number
  pageSize: number
  name?: string
  exhibitionType: ExhibitionType | ''
}

const api = useApi()

// 查询参数
const query = ref<QueryState>({
  pageNum: 1,
  pageSize: 10,
  year: '',
  continent: '',
  country: '',
  name: '',
  exhibitionType: ''
})

// 报名功能相关
const showRegistrationModal = ref(false)
const selectedExhibition = ref<Exhibition | null>(null)

// 展会与全球区域数据（服务端取数，筛选分页在前端完成）
const { data } = await useAsyncData('exhibition-list', async () => {
  const [areas, list] = await Promise.all([
    api.exhibition.getGlobalArea(),
    api.exhibition.getList({ pageSize: 999, pageNum: 1 })
  ])
  return { areas, rows: list.rows }
})
const allExhibitions = computed(() => data.value?.rows ?? [])
const exhibitions = ref<Exhibition[]>([])
const total = ref(0)

// 全球区域数据
const globalAreas = computed(() => data.value?.areas ?? [])
const selectedContinent = ref('')

// 年份选项（2024-2030）
const years = Array.from({ length: 7 }, (_, i) => (2024 + i).toString())

// 获取所有大洲
const continents = computed(() => {
  const continentSet = new Set<string>()
  globalAreas.value.forEach(area => {
    if (
      area &&
      area.continentName &&
      typeof area.continentName === 'string' &&
      area.continentName.trim() !== ''
    ) {
      continentSet.add(area.continentName.trim())
    }
  })
  return Array.from(continentSet).sort()
})

// 根据选中的大洲筛选国家
const filteredAreas = computed(() => {
  if (!selectedContinent.value) return globalAreas.value
  return globalAreas.value.filter(area => area.continentName === selectedContinent.value)
})

// 筛选展会数据
const filterExhibitions = () => {
  let filtered = [...allExhibitions.value]

  // 按名称筛选
  if (query.value.name && query.value.name.trim() !== '') {
    filtered = filtered.filter(item =>
      item.name.toLowerCase().includes(query.value.name!.toLowerCase())
    )
  }

  // 按展会类型筛选
  if (query.value.exhibitionType) {
    filtered = filtered.filter(item => item.exhibitionType === query.value.exhibitionType)
  }

  // 按年份筛选
  if (query.value.year && query.value.year.trim() !== '') {
    filtered = filtered.filter(item => item.year === query.value.year)
  }

  // 按国家筛选
  if (query.value.country) {
    filtered = filtered.filter(item => item.country === query.value.country)
  }

  // 按大洲筛选
  if (selectedContinent.value) {
    const countriesInContinent = globalAreas.value
      .filter(area => area.continentName === selectedContinent.value)
      .map(area => area.id)
    filtered = filtered.filter(item => countriesInContinent.includes(item.country))
  }

  // 计算总数
  total.value = filtered.length

  // 分页处理
  const pageNum = query.value.pageNum || 1
  const pageSize = query.value.pageSize || 10
  const start = (pageNum - 1) * pageSize
  const end = start + pageSize
  exhibitions.value = filtered.slice(start, end)
}

// 处理大洲变化
const handleContinentChange = () => {
  query.value.country = ''
  filterExhibitions()
}

// 处理搜索
const handleSearch = () => {
  query.value.pageNum = 1
  filterExhibitions()
}

// 处理重置
const handleReset = () => {
  query.value = {
    pageNum: 1,
    pageSize: 10,
    year: '',
    continent: '',
    country: '',
    name: '',
    exhibitionType: ''
  }
  selectedContinent.value = ''
  filterExhibitions()
}

// 打开报名弹窗
const openRegistrationModal = (exhibition: Exhibition) => {
  selectedExhibition.value = exhibition
  showRegistrationModal.value = true
}

// 处理报名提交
const handleRegistration = async (data: any) => {
  try {
    // 这里调用报名接口
    await $fetch('/api/exhibition/registration/submit', {
      method: 'POST',
      body: data
    })
    ElMessage.success('报名成功')
    showRegistrationModal.value = false
  } catch (error) {
    console.error('报名失败:', error)
    ElMessage.error('报名失败')
  }
}

// 处理每页数量变化
const handleSizeChange = (val: number) => {
  query.value.pageSize = val
  filterExhibitions()
}

// 处理页码变化
const handleCurrentChange = (val: number) => {
  query.value.pageNum = val
  filterExhibitions()
}

// 处理路由参数（如首页地图跳转带 country）
const route = useRoute()
const countryId = Number(route.query.country)
const country = globalAreas.value.find(area => area.id === countryId)
if (country) {
  selectedContinent.value = country.continentName
  query.value.country = countryId
}
filterExhibitions()

useSeoMeta({
  title: '展会列表',
  description: '全球能源、电梯、医疗等行业国际展会目录，按大洲、国家、年份筛选'
})
</script>

<style scoped>
.el-select {
  width: 100%;
}

.el-date-picker {
  width: 100%;
}
</style>