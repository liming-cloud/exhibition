<template>
  <!-- inverse：放在深色大图背景上时用浅色文字 -->
  <nav aria-label="面包屑" class="text-sm" :class="inverse ? 'text-white/70' : 'text-neutral-500'">
    <ol class="flex flex-wrap items-center gap-x-2 gap-y-1">
      <li>
        <NuxtLink to="/" :class="hoverClass">首页</NuxtLink>
      </li>
      <li v-for="(item, i) in items" :key="i" class="flex items-center gap-2 min-w-0">
        <i
          class="fas fa-chevron-right text-[10px]"
          :class="inverse ? 'text-white/40' : 'text-neutral-300'"
        ></i>
        <NuxtLink v-if="item.to" :to="item.to" :class="hoverClass">{{ item.name }}</NuxtLink>
        <!-- 最后一级通常是内容标题，过长时截断 -->
        <span
          v-else
          class="truncate max-w-[16rem] md:max-w-md"
          :class="inverse ? 'text-white' : 'text-neutral-700'"
          >{{ item.name }}</span
        >
      </li>
    </ol>
  </nav>
</template>

<script setup lang="ts">
export interface Crumb {
  name: string
  to?: string
}

const props = defineProps<{ items: Crumb[]; inverse?: boolean }>()
const hoverClass = computed(() => (props.inverse ? 'hover:text-white' : 'hover:text-primary-700'))
</script>
