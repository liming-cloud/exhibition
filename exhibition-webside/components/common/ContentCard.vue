<template>
  <!-- 图文卡片：成功案例、照片墙等。封面固定 4:3 裁切，图片缺失或加载失败时显示占位底色 -->
  <NuxtLink
    :to="to"
    class="group block rounded-lg border border-neutral-200 bg-white overflow-hidden hover:border-primary-300 hover:shadow-sm transition"
  >
    <div class="aspect-[4/3] bg-neutral-100 overflow-hidden flex items-center justify-center">
      <img
        v-if="cover && !failed"
        :src="cover"
        :alt="title"
        loading="lazy"
        class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
        @error="failed = true"
      />
      <i v-else class="far fa-image text-3xl text-neutral-300"></i>
    </div>
    <div class="p-4">
      <h3 class="text-base font-medium text-neutral-900 line-clamp-2 group-hover:text-primary-700">
        {{ title }}
      </h3>
      <p v-if="$slots.default" class="mt-2 text-sm text-neutral-500">
        <slot />
      </p>
    </div>
  </NuxtLink>
</template>

<script setup lang="ts">
defineProps<{ to: string; title: string; cover?: string }>()

const failed = ref(false)
</script>
