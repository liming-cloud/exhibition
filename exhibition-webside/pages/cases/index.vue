<template>
  <div class="container mx-auto px-4 py-8">
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div v-for="item in casesList" :key="item.id" 
           class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="aspect-w-16 aspect-h-9">
          <img :src="`/api/file/preview?path=${encodeURIComponent(item.coverUrl)}`" 
               :alt="item.title" 
               class="w-full h-full object-cover">
        </div>
        <div class="p-6">
          <h2 class="text-xl font-semibold mb-4">{{ item.title }}</h2>
          <p class="text-neutral-600 mb-4 line-clamp-3" v-html="item.content"></p>
          <router-link :to="`/cases/${item.id}`" 
                      class="inline-flex items-center text-primary-500 hover:text-primary-600">
            查看详情 <i class="fas fa-arrow-right ml-1"></i>
          </router-link>
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
import type { Cases } from '~/types/cases'

const api = useApi()
const casesList = ref<Cases[]>([])
const total = ref(0)

const queryParams = reactive({
  pageNum: 1,
  pageSize: 12
})

const getCasesList = async () => {
  try {
    const res = await api.cases.getList(queryParams)
    if (res.code === 200) {
      casesList.value = res.rows
      total.value = res.total
    }
  } catch (error) {
    console.error('获取案例列表失败:', error)
  }
}

const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  getCasesList()
}

const handleCurrentChange = (val: number) => {
  queryParams.pageNum = val
  getCasesList()
}

onMounted(() => {
  getCasesList()
})
</script>

<style scoped>
.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style> 