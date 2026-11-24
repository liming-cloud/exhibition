<template>
  <!-- 大图背景：照片 + 一层主色叠加 + 深色渐变（左深右浅），不同风格的照片都统一到品牌色调，文字放在上层 -->
  <div class="absolute inset-0 overflow-hidden" aria-hidden="true">
    <img
      :src="src"
      alt=""
      class="w-full h-full object-cover"
      :class="imgClass"
      :loading="eager ? 'eager' : 'lazy'"
      :fetchpriority="eager ? 'high' : 'auto'"
    />
    <div class="absolute inset-0 bg-primary-800/40 mix-blend-multiply"></div>
    <div
      class="absolute inset-0 bg-gradient-to-r from-neutral-900/85 via-neutral-900/55 to-neutral-900/20"
    ></div>
  </div>
</template>

<script setup lang="ts">
// eager：首屏大图（轮播第一张、页面标题区）立即加载并提高优先级，其余懒加载
defineProps<{ src: string; eager?: boolean; imgClass?: string }>()
</script>
