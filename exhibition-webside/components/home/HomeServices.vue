<template>
  <!-- 紧凑的一行服务（R3），放在首页最后 -->
  <div
    class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-px bg-neutral-200 border border-neutral-200 rounded-lg overflow-hidden"
  >
    <component
      :is="service.link ? NuxtLink : 'div'"
      v-for="service in services"
      :key="service.title"
      :to="service.link"
      class="bg-white p-5 text-center"
      :class="{ 'hover:bg-primary-50 transition-colors': service.link }"
    >
      <i :class="service.icon" class="text-2xl text-primary-600"></i>
      <p class="mt-3 font-semibold text-neutral-900">{{ service.title }}</p>
      <p class="mt-1 text-xs text-neutral-500 leading-relaxed">{{ service.description }}</p>
    </component>
  </div>
</template>

<script setup lang="ts">
import { NuxtLink } from '#components'

const services = [
  { icon: 'fas fa-globe-asia', title: '展会咨询', description: '按产品与目标市场推荐展会' },
  { icon: 'fas fa-store', title: '展位预订', description: '标摊、光地、特装展位预订' },
  { icon: 'fas fa-drafting-compass', title: '展台设计', description: '展台设计与海外搭建' },
  { icon: 'fas fa-plane-departure', title: '商旅服务', description: '机票、酒店与行程安排' },
  { icon: 'fas fa-passport', title: '签证办理', description: '商务签证与邀请函' },
  { icon: 'fas fa-coins', title: '补贴申请', description: '协助申请展会补贴', link: '/subsidies' }
]
</script>
