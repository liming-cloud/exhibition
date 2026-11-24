<template>
  <nav aria-label="面包屑" class="text-sm text-neutral-500">
    <ol class="flex flex-wrap items-center gap-x-2 gap-y-1">
      <li>
        <NuxtLink to="/" class="hover:text-primary-700">首页</NuxtLink>
      </li>
      <li v-for="(item, i) in items" :key="i" class="flex items-center gap-2 min-w-0">
        <i class="fas fa-chevron-right text-[10px] text-neutral-300"></i>
        <NuxtLink v-if="item.to" :to="item.to" class="hover:text-primary-700">{{
          item.name
        }}</NuxtLink>
        <!-- 最后一级通常是内容标题，过长时截断 -->
        <span v-else class="text-neutral-700 truncate max-w-[16rem] md:max-w-md">{{
          item.name
        }}</span>
      </li>
    </ol>
  </nav>
</template>

<script setup lang="ts">
export interface Crumb {
  name: string
  to?: string
}

defineProps<{ items: Crumb[] }>()
</script>
