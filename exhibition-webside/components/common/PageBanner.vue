<template>
  <!-- 列表页标题区：面包屑 + 英文小字 + 标题 + 一句话；右侧插槽放统计数字或按钮 -->
  <section class="bg-neutral-50 border-b border-neutral-200">
    <div class="container py-6 md:py-10">
      <AppBreadcrumb v-if="crumbs" :items="crumbs" />
      <div class="mt-4 md:mt-6 flex flex-wrap items-end justify-between gap-4">
        <div class="min-w-0">
          <p v-if="en" class="section-eyebrow">{{ en }}</p>
          <h1 class="mt-1 text-2xl md:text-4xl font-bold text-neutral-900">{{ title }}</h1>
          <p v-if="desc" class="mt-2 text-neutral-500">{{ desc }}</p>
        </div>
        <slot />
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import AppBreadcrumb, { type Crumb } from './AppBreadcrumb.vue'

defineProps<{ title: string; en?: string; desc?: string; crumbs?: Crumb[] }>()
</script>
