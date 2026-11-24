<template>
  <div class="flex flex-wrap items-end justify-between gap-4 mb-6">
    <div class="min-w-0">
      <p v-if="eyebrow" class="section-eyebrow">{{ eyebrow }}</p>
      <h2 class="section-title">{{ title }}</h2>
      <p v-if="desc" class="section-desc">{{ desc }}</p>
    </div>
    <!-- 右侧：「查看全部」链接或统计数字 -->
    <slot name="action" />
  </div>
</template>

<script setup lang="ts">
defineProps<{ title: string; eyebrow?: string; desc?: string }>()
</script>
