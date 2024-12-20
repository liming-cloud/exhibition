<template>
  <nav class="fixed top-0 left-0 w-full bg-white shadow-md z-50">
    <div class="max-w-7xl mx-auto px-4">
      <div class="flex justify-between items-center h-16">
        <span class="text-2xl font-pacifico text-blue-600">logo</span>
        <div class="flex items-center space-x-8">
          <NuxtLink to="/" class="text-gray-700 hover:text-blue-600">首页</NuxtLink>
          <a class="text-gray-700 hover:text-blue-600 cursor-pointer" @click="scrollToNews">新闻资讯</a>
          <div class="relative group">
            <button class="text-gray-700 hover:text-blue-600">展会信息</button>
            <div class="hidden group-hover:block absolute top-full left-0 w-32 bg-white shadow-lg rounded-lg py-2">
<!--              <NuxtLink
                  v-for="continent in continents"
                  :key="continent"
                  :to="`/exhibitions?continent=${continent}`"
                  class="px-4 py-2 hover:bg-gray-100 block"
              >
                {{ continent }}
              </NuxtLink>-->
              <NuxtLink class="px-4 py-2 hover:bg-gray-100 block" to="/exhibitions/asia">亚洲</NuxtLink>
              <NuxtLink class="px-4 py-2 hover:bg-gray-100 block" to="/exhibitions/europe">欧美</NuxtLink>
              <NuxtLink class="px-4 py-2 hover:bg-gray-100 block" to="/exhibitions/africa">非洲</NuxtLink>
              <NuxtLink class="px-4 py-2 hover:bg-gray-100 block" to="/exhibitions/australia">澳洲</NuxtLink>
              <NuxtLink class="px-4 py-2 hover:bg-gray-100 block" to="/exhibitions/middleeast">中东</NuxtLink>
              <NuxtLink class="px-4 py-2 hover:bg-gray-100 block" to="/exhibitions/intersolar">intersolar系列</NuxtLink>
              <NuxtLink class="px-4 py-2 hover:bg-gray-100 block" to="/exhibitions/setex">setex系列</NuxtLink>
              <NuxtLink class="px-4 py-2 hover:bg-gray-100 block" to="/exhibitions/battery">the battery show系列</NuxtLink>
              <NuxtLink class="px-4 py-2 hover:bg-gray-100 block" to="/exhibitions/solarshow">the solar show系列</NuxtLink>


            </div>
          </div>
          <NuxtLink to="/about" class="text-gray-700 hover:text-blue-600">关于我们</NuxtLink>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { useRouter, useRoute } from '#app'
const router = useRouter()
const route = useRoute()
const continents = ['澳洲','亚洲','欧美','非洲','中东','intersoLar','setex','the battery show','the solar show']

function scrollToNews() {
  if (route.path === '/') {
    // 已在首页
    const section = document.getElementById('news-section')
    if (section) {
      section.scrollIntoView({ behavior: 'smooth' })
    }
  } else {
    // 不在首页，先跳转首页再滚动
    router.push('/').then(() => {
      nextTick(() => {
        const section = document.getElementById('news-section')
        section && section.scrollIntoView({ behavior: 'smooth' })
      })
    })
  }
}
</script>