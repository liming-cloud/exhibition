<template>
  <div class="bg-white mt-12 py-16">
    <div class="max-w-7xl mx-auto px-4">
      <h2 class="text-2xl font-bold mb-8 text-center">关于我们</h2>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
        <div v-for="(item, index) in aboutUsList" :key="index" class="text-center">
          <div class="w-20 h-20 bg-blue-100 rounded-full mx-auto flex items-center justify-center mb-4">
            <i :class="item.icon" class="text-4xl text-blue-600"></i>
          </div>
          <h3 class="text-lg font-semibold mb-2">{{ item.title }}</h3>
          <p class="text-gray-600">{{ item.description }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
const aboutUsList = [
  {
    icon: 'fas fa-medal',
    title: '专业服务',
    description: '十年展会服务经验，为企业提供专业的展会解决方案'
  },
  {
    icon: 'fas fa-globe',
    title: '全球网络',
    description: '覆盖全球主要展会市场，拥有完善的合作伙伴网络'
  },
  {
    icon: 'fas fa-headset',
    title: '一站式服务',
    description: '提供展位设计、搭建、物流等全方位展会服务'
  }
]
</script>