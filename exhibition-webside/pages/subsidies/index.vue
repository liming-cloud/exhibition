<template>
  <div class="container mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold mb-8">补贴政策</h1>
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div v-for="item in subsidiesList" :key="item.id" 
           class="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300">
        <div class="p-6">
          <div class="mb-4">
            <h2 class="text-xl font-semibold mb-2 line-clamp-2">{{ item.title }}</h2>
            <div class="text-red-600 font-semibold text-lg mb-2">
              补贴金额：{{ formatPrice(item.price) }}
            </div>
          </div>
          <p class="text-gray-600 mb-4 line-clamp-3" v-html="item.content"></p>
          <div class="flex flex-col space-y-3">
            <div class="text-gray-500 text-sm">
              <i class="far fa-calendar-alt mr-2"></i>
              <span>{{ formatDate(item.startTime) }} - {{ formatDate(item.endTime) }}</span>
            </div>
            <router-link :to="`/subsidies/${item.id}`" 
                        class="text-blue-600 hover:text-blue-700 inline-flex items-center">
              查看详情 <i class="fas fa-arrow-right ml-1"></i>
            </router-link>
          </div>
        </div>
      </div>
    </div>
    <!-- 分页 -->
    <div class="mt-8 flex justify-center">
      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[12, 24, 36]"
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
import { ref, reactive, onMounted } from 'vue'
import type { Subsidies } from '~/types/subsidies'

const api = useApi()
const subsidiesList = ref<Subsidies[]>([])
const total = ref(0)

const queryParams = reactive({
  pageNum: 1,
  pageSize: 12
})

const getSubsidiesList = async () => {
  try {
    const res = await api.subsidies.getList(queryParams)
    if (res.code === 200) {
      subsidiesList.value = res.rows
      total.value = res.total
    }
  } catch (error) {
    console.error('获取补贴政策列表失败:', error)
  }
}

const formatPrice = (price: number) => {
  return `¥ ${(price / 10000).toFixed(2)}万`
}

const formatDate = (dateStr?: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  getSubsidiesList()
}

const handleCurrentChange = (val: number) => {
  queryParams.pageNum = val
  getSubsidiesList()
}

onMounted(() => {
  getSubsidiesList()
})
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style> 