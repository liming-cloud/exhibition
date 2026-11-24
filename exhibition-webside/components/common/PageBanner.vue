<template>
  <!-- 列表页标题区：面包屑 + 英文小字 + 标题 + 一句话；右侧插槽放统计数字或按钮。
       传 image 时用大图背景 + 品牌色遮罩（白字），不传时为浅灰底 -->
  <section
    class="relative overflow-hidden"
    :class="image ? 'bg-neutral-900' : 'bg-neutral-50 border-b border-neutral-200'"
  >
    <MediaBackground v-if="image" :src="image" eager />
    <div class="relative container" :class="image ? 'py-10 md:py-20' : 'py-6 md:py-10'">
      <AppBreadcrumb v-if="crumbs" :items="crumbs" :inverse="!!image" />
      <div class="mt-4 md:mt-6 flex flex-wrap items-end justify-between gap-4">
        <div class="min-w-0">
          <p v-if="en" class="section-eyebrow" :class="{ '!text-white/70': image }">{{ en }}</p>
          <h1
            class="mt-1 text-2xl md:text-4xl font-bold"
            :class="image ? 'text-white' : 'text-neutral-900'"
          >
            {{ title }}
          </h1>
          <p v-if="desc" class="mt-2" :class="image ? 'text-white/80' : 'text-neutral-500'">
            {{ desc }}
          </p>
        </div>
        <slot />
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import AppBreadcrumb, { type Crumb } from './AppBreadcrumb.vue'
import MediaBackground from './MediaBackground.vue'

defineProps<{ title: string; en?: string; desc?: string; crumbs?: Crumb[]; image?: string }>()
</script>
