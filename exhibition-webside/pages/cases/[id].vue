<template>
  <div class="container mx-auto px-4 py-8">
    <div v-if="caseDetail" class="max-w-4xl mx-auto bg-white rounded-lg shadow-md overflow-hidden">
      <!-- 封面图 -->
      <div class="aspect-w-16 aspect-h-9">
        <img
          :src="`/api/file/preview?path=${encodeURIComponent(caseDetail.coverUrl)}`"
          :alt="caseDetail.title"
          class="w-full h-full object-cover"
        />
      </div>
      <!-- 内容区域 -->
      <div class="p-8">
        <h1 class="text-3xl font-bold mb-6">{{ caseDetail.title }}</h1>
        <div class="prose max-w-none" v-html="caseDetail.content"></div>
      </div>
      <!-- 返回按钮 -->
      <div class="p-8 border-t">
        <router-link
          to="/cases"
          class="inline-flex items-center px-6 py-3 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-colors"
        >
          <i class="fas fa-arrow-left mr-2"></i>
          返回案例列表
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'

const route = useRoute()
const api = useApi()
const { data: caseDetail, error } = await useAsyncData(`case-${route.params.id}`, () =>
  api.cases.getDetail(Number(route.params.id))
)
if (error.value) {
  throw createError({ statusCode: 500, statusMessage: '获取案例详情失败', fatal: true })
}
if (!caseDetail.value) {
  throw createError({ statusCode: 404, statusMessage: '案例不存在', fatal: true })
}

useSeoMeta({
  title: () => caseDetail.value?.title,
  description: () => toSummary(caseDetail.value?.content)
})
</script>