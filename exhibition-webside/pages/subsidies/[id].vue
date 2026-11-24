<template>
  <div class="container mx-auto px-4 py-8">
    <div
      v-if="subsidyDetail"
      class="max-w-4xl mx-auto bg-white rounded-lg shadow-md overflow-hidden"
    >
      <!-- 内容区域 -->
      <div class="p-8">
        <div class="flex justify-between items-start mb-6">
          <h1 class="text-3xl font-bold">{{ subsidyDetail.title }}</h1>
          <span class="text-red-600 font-bold text-xl whitespace-nowrap ml-4">
            补贴金额：{{ formatPrice(subsidyDetail.price) }}
          </span>
        </div>
        <div class="text-gray-500 text-sm mb-8">
          <i class="far fa-calendar-alt mr-2"></i>
          <span
            >申请时间：{{ formatDate(subsidyDetail.startTime) }} -
            {{ formatDate(subsidyDetail.endTime) }}</span
          >
        </div>
        <div class="prose max-w-none" v-html="subsidyDetail.content"></div>
      </div>
      <!-- 返回按钮 -->
      <div class="p-8 border-t">
        <router-link
          to="/subsidies"
          class="inline-flex items-center px-6 py-3 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-colors"
        >
          <i class="fas fa-arrow-left mr-2"></i>
          返回补贴列表
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'

const route = useRoute()
const api = useApi()
const { data: subsidyDetail, error } = await useAsyncData(`subsidy-${route.params.id}`, () =>
  api.subsidies.getDetail(Number(route.params.id))
)
if (error.value) {
  throw createError({ statusCode: 500, statusMessage: '获取补贴详情失败', fatal: true })
}
if (!subsidyDetail.value) {
  throw createError({ statusCode: 404, statusMessage: '政策不存在', fatal: true })
}

useSeoMeta({
  title: () => subsidyDetail.value?.title,
  description: () => toSummary(subsidyDetail.value?.content)
})

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
</script>