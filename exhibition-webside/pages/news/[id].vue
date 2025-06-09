<template>
  <div class="container mx-auto px-4 py-8">
    <div v-if="newsDetail" class="max-w-4xl mx-auto bg-white rounded-lg shadow-md overflow-hidden">
      <!-- 封面图 -->
      <div class="aspect-w-16 aspect-h-9">
        <img
          :src="`/api/file/preview?path=${encodeURIComponent(newsDetail.coverUrl || '')}`"
          :alt="newsDetail.title"
          class="w-full h-full object-cover"
        />
      </div>
      <!-- 内容区域 -->
      <div class="p-8">
        <h1 class="text-3xl font-bold mb-4">{{ newsDetail.title }}</h1>
        <div class="text-gray-500 text-sm mb-8">
          发布时间：{{ formatDate(newsDetail.createTime) }}
        </div>
        <div class="prose max-w-none" v-html="newsDetail.content"></div>
      </div>
      <!-- 返回按钮 -->
      <div class="p-8 border-t">
        <router-link
          to="/news"
          class="inline-flex items-center px-6 py-3 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-colors"
        >
          <i class="fas fa-arrow-left mr-2"></i>
          返回资讯列表
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import type { Information } from '~/types/information'

const route = useRoute()
const api = useApi()
const newsDetail = ref<Information>()

const getNewsDetail = async () => {
  try {
    const id = Number(route.params.id)
    const res = await api.information.getDetail(id)
    if (res) {
      newsDetail.value = res
    }
  } catch (error) {
    console.error('获取资讯详情失败:', error)
  }
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

onMounted(() => {
  getNewsDetail()
})
</script>