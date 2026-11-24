<template>
  <!-- 列表行：展会、简讯、政策共用。左侧插槽放日期块或缩略图，右侧插槽放按钮；muted 用于已结束/已截止 -->
  <div
    class="group relative flex gap-4 p-4 rounded-lg border hover:border-primary-300 hover:shadow-sm transition"
    :class="muted ? 'bg-neutral-50 border-neutral-200' : 'bg-white border-neutral-200'"
  >
    <slot name="leading" />
    <div class="min-w-0 flex-1 sm:flex sm:items-center sm:gap-4">
      <div class="min-w-0 flex-1">
        <h3
          class="text-base font-medium line-clamp-2"
          :class="muted ? 'text-neutral-500' : 'text-neutral-900 group-hover:text-primary-700'"
        >
          <slot name="badge" />
          <!-- 整行可点：链接的点击区域铺满整行，按钮插槽叠在它上层 -->
          <NuxtLink :to="to" class="after:absolute after:inset-0">{{ title }}</NuxtLink>
        </h3>
        <div class="mt-1.5 text-sm text-neutral-500 space-y-1">
          <slot />
        </div>
      </div>
      <div v-if="$slots.actions" class="relative mt-3 sm:mt-0 flex shrink-0 gap-2">
        <slot name="actions" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{ to: string; title: string; muted?: boolean }>()
</script>
