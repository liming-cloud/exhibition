<template>
  <div class="max-w-7xl mx-auto mt-12 px-4">
    <h1 class="text-3xl font-bold mb-8">新闻资讯</h1>
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div
          v-for="news in newsList"
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
            <button class="text-blue-600 hover:text-blue-700">阅读更多</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import newsData from '~/content/news.json'

const newsList = ref(newsData)
</script>