<template>
  <!-- 空状态：说明 + 至少一个出口（插槽放「清空筛选」「联系顾问」等按钮），不留空白 -->
  <div class="rounded-lg border border-dashed border-neutral-300 bg-white px-6 py-12 text-center">
    <i class="fas text-3xl text-neutral-300" :class="icon"></i>
    <p class="mt-4 font-medium text-neutral-700">{{ title }}</p>
    <p v-if="desc" class="mt-1 text-sm text-neutral-500">{{ desc }}</p>
    <div v-if="$slots.default" class="mt-6 flex flex-wrap justify-center gap-3">
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
withDefaults(defineProps<{ title: string; desc?: string; icon?: string }>(), {
  icon: 'fa-inbox'
})
</script>
