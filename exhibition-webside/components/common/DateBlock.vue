<template>
  <!-- 展会行最前面的日期块：月.日 大字，年份小字 -->
  <div
    class="w-16 shrink-0 rounded-md flex flex-col items-center justify-center py-2"
    :class="muted ? 'bg-neutral-100 text-neutral-400' : 'bg-primary-50 text-primary-700'"
  >
    <span class="text-lg font-bold leading-none">{{ date.slice(5).replace('-', '.') }}</span>
    <span class="mt-1 text-xs opacity-80">{{ date.slice(0, 4) }}</span>
  </div>
</template>

<script setup lang="ts">
// date 形如 2026-06-23
defineProps<{ date: string; muted?: boolean }>()
</script>
