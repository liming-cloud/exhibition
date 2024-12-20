<template>
  <div id="news-section" class="max-w-7xl mx-auto mt-12 px-4">
    <h2 class="text-2xl font-bold mb-6">最新资讯</h2>
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div
          v-for="(news, index) in newsList"
          :key="news.id"
          class="bg-white rounded-lg shadow-lg overflow-hidden"
      >
        <img
            :src="news.image"
            :alt="news.title"
            class="w-full h-48 object-cover object-top"
        />
        <div class="p-4">
          <h3 class="text-lg font-semibold mb-2">{{ news.title }}</h3>
          <p class="text-gray-600 text-sm line-clamp-2">
            {{ news.description }}
          </p>
          <div class="mt-4 flex justify-between items-center">
            <span class="text-gray-500 text-sm">{{ news.date }}</span>
            <NuxtLink :to="`/news/${news.id}`" class="text-blue-600 hover:text-blue-700">阅读更多</NuxtLink>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import newsData from '~/content/news.json'
import { ref } from 'vue'
const newsList = ref(newsData)
</script>