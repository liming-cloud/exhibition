<template>
  <div class="container mx-auto px-4 py-8">
    <!-- 筛选区域 -->
    <div class="bg-white rounded-lg shadow-md p-6 mb-8">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <!-- 年份选择 -->
        <div>
          <label class="block text-sm font-medium text-neutral-700 mb-2">年份</label>
          <el-select v-model="query.year" placeholder="请选择年份" clearable @change="handleSearch" class="w-full">
            <el-option v-for="year in years" :key="year" :label="year" :value="year" />
          </el-select>
        </div>
        
        <!-- 大洲选择 -->
        <div>
          <label class="block text-sm font-medium text-neutral-700 mb-2">大洲</label>
          <el-select v-model="selectedContinent" placeholder="请选择大洲" clearable @change="handleContinentChange" class="w-full">
            <el-option v-for="continent in continents" :key="continent" :label="continent" :value="continent" />
          </el-select>
        </div>
        
        <!-- 国家选择 -->
        <div>
          <label class="block text-sm font-medium text-neutral-700 mb-2">国家</label>
          <el-select v-model="query.country" placeholder="请选择国家" clearable @change="handleSearch" class="w-full">
            <el-option 
              v-for="area in filteredAreas" 
              :key="area.id" 
              :label="area.countryName" 
              :value="area.id" 
            />
          </el-select>
        </div>
      </div>
      
      <!-- 搜索按钮 -->
      <div class="flex justify-end mt-6">
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset" class="ml-4">重置</el-button>
      </div>
    </div>

    <!-- 展会列表 -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
      <div v-for="exhibition in exhibitions" :key="exhibition.id" 
           class="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300">
        <div class="p-6">
          <h3 class="text-xl font-bold mb-4 text-neutral-800">{{ exhibition.name }}</h3>
          <div class="space-y-2 text-neutral-600">
            <p class="flex items-center">
              <i class="fas fa-map-marker-alt w-6"></i>
              {{ exhibition.countryName }} - {{ exhibition.location }}
            </p>
            <p class="flex items-center">
              <i class="fas fa-calendar-alt w-6"></i>
              {{ exhibition.time }}
            </p>
          </div>
          <NuxtLink :to="`/exhibitions/${exhibition.id}`" 
                   class="inline-block mt-6 px-6 py-2 bg-gradient-to-r from-primary-500 to-primary-600 text-white rounded-lg hover:shadow-lg transition-all duration-300">
            查看详情
          </NuxtLink>
        </div>
      </div>
    </div>

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
import type { Exhibition, ExhibitionQuery, GlobalArea } from '~/types/exhibition'

interface QueryState extends ExhibitionQuery {
  year: string
  continent: string
  country?: number
  pageNum: number
  pageSize: number
}

const api = useApi()

// 查询参数
const query = ref<QueryState>({
  pageNum: 1,
  pageSize: 10,
  year: '',
  continent: '',
  country: undefined
})

// 展会列表数据
const allExhibitions = ref<Exhibition[]>([]) // 存储所有展会数据
const exhibitions = ref<Exhibition[]>([])
const total = ref(0)

// 全球区域数据
const globalAreas = ref<GlobalArea[]>([])
const selectedContinent = ref('')

// 年份选项（2024-2030）
const years = Array.from({ length: 7 }, (_, i) => (2024 + i).toString())

// 获取所有大洲
const continents = computed(() => {
  console.log('Computing continents from global areas:', globalAreas.value)
  const continentSet = new Set<string>()
  globalAreas.value.forEach(area => {
    console.log('Processing area:', area)
    if (area && area.continentName && typeof area.continentName === 'string' && area.continentName.trim() !== '') {
      console.log('Adding continent:', area.continentName.trim())
      continentSet.add(area.continentName.trim())
    }
  })
  const result = Array.from(continentSet).sort()
  console.log('Final continents list:', result)
  return result
})

// 根据选中的大洲筛选国家
const filteredAreas = computed(() => {
  if (!selectedContinent.value) return globalAreas.value
  return globalAreas.value.filter(area => area.continentName === selectedContinent.value)
})

// 筛选展会数据
const filterExhibitions = () => {
  let filtered = [...allExhibitions.value]

  // 按年份筛选
  if (query.value.year && query.value.year.trim() !== '') {
    filtered = filtered.filter(item => item.year === query.value.year)
  }

  // 按国家筛选
  if (query.value.country !== undefined) {
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

// 获取展会列表
const fetchExhibitions = async () => {
  try {
    const res = await api.exhibition.getList({
      pageSize: 999, // 获取所有数据
      pageNum: 1
    })
    allExhibitions.value = res.rows
    filterExhibitions()
  } catch (error) {
    console.error('Failed to fetch exhibitions:', error)
  }
}

// 获取全球区域列表
const fetchGlobalAreas = async () => {
  try {
    const res = await api.exhibition.getGlobalArea()
    globalAreas.value = res
  } catch (error) {
    console.error('Failed to fetch global areas:', error)
    globalAreas.value = []
  }
}

// 处理大洲变化
const handleContinentChange = () => {
  query.value.country = undefined
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
    country: undefined
  }
  selectedContinent.value = ''
  filterExhibitions()
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

// 页面加载时获取数据
onMounted(async () => {
  await fetchGlobalAreas()
  await fetchExhibitions()

  // 处理路由参数
  const route = useRoute()
  if (route.query.country) {
    const countryId = Number(route.query.country)
    const country = globalAreas.value.find(area => area.id === countryId)
    if (country) {
      selectedContinent.value = country.continentName
      query.value.country = countryId
      filterExhibitions()
    }
  }
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