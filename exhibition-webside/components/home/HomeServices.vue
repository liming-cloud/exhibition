<template>
  <div class="grid-cols-auto-fit">
    <div v-for="service in services" :key="service.title" class="feature-card group">
      <div class="feature-icon group-hover:scale-110">
        <i :class="service.icon" class="text-xl"></i>
      </div>
      <div class="flex-1">
        <h3 class="text-lg font-semibold text-neutral-900 mb-2">{{ service.title }}</h3>
        <p class="text-neutral-600">{{ service.description }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const services = [
  {
    icon: 'fas fa-globe',
    title: '展会咨询',
    description: '提供全球展会信息咨询，帮助企业选择最适合的展会平台'
  },
  {
    icon: 'fas fa-handshake',
    title: '展位预订',
    description: '协助企业预订展位，提供优质展位资源，确保最佳展示效果'
  },
  {
    icon: 'fas fa-paint-brush',
    title: '展台设计',
    description: '专业的展台设计和搭建服务，打造独特的品牌形象'
  },
  {
    icon: 'fas fa-plane',
    title: '商旅服务',
    description: '提供机票、酒店预订等一站式商旅服务，让您安心参展'
  },
  {
    icon: 'fas fa-file-alt',
    title: '签证办理',
    description: '专业的签证申请服务，确保顺利出行参展'
  },
  {
    icon: 'fas fa-coins',
    title: '补贴申请',
    description: '协助企业申请展会补贴，降低参展成本'
  }
]
</script> 