<template>
  <div class="max-w-3xl mx-auto mt-12 px-4">
    <h1 class="text-3xl font-bold mb-4">{{ newsItem.title }}</h1>
    <p class="text-gray-500 text-sm mb-4">{{ newsItem.date }}</p>
    <img
        v-if="newsItem.image"
        :src="newsItem.image"
        alt="news image"
        class="w-full h-auto mb-4"
    />
    <p class="text-gray-700 whitespace-pre-wrap">{{ newsItem.content }}</p>
  </div>
</template>

<script setup>
import { useRoute } from '#app'
import newsData from '~/content/news.json'

const route = useRoute()
const newsId = route.params.id
const newsItem = newsData.find(n => n.id === newsId) || {}
</script>