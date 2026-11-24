<template>
  <div class="container mx-auto px-4 py-8">
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div
        v-for="item in newsList"
        :key="item.id"
        class="bg-white rounded-lg shadow-md overflow-hidden"
      >
        <div class="aspect-w-16 aspect-h-9">
          <img
            :src="`/api/file/preview?path=${encodeURIComponent(item.coverUrl || '')}`"
            :alt="item.title"
            class="w-full h-full object-cover"
          />
        </div>
        <div class="p-6">
          <h2 class="text-xl font-semibold mb-2">{{ item.title }}</h2>
          <p class="text-gray-600 mb-4 line-clamp-3" v-html="item.content"></p>
          <div class="flex justify-between items-center">
            <span class="text-gray-500 text-sm">{{ formatDate(item.createTime) }}</span>
            <router-link :to="`/news/${item.id}`" class="text-blue-600 hover:text-blue-700">
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
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive } from 'vue'
import type { Information } from '~/types/information'

const api = useApi()

const queryParams = reactive({
  pageNum: 1,
  pageSize: 12
})

const { data } = await useAsyncData('news-list', () => api.information.getList(queryParams), {
  watch: [queryParams]
})
const newsList = computed(() => data.value?.rows ?? [])
const total = computed(() => data.value?.total ?? 0)

useSeoMeta({
  title: '市场简讯',
  description: '海外展会与外贸行业最新资讯'
})

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
}

const handleCurrentChange = (val: number) => {
  queryParams.pageNum = val
}
</script>

<style scoped>
.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>