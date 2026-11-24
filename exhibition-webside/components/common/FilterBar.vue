<template>
  <!-- 筛选栏：关键词搜索 + 筛选项（默认插槽）+ 已选条件。手机端筛选项收进底部抽屉，已选条件留在列表上方 -->
  <div class="space-y-3">
    <div class="flex flex-wrap gap-2 md:gap-3">
      <form class="relative flex-1 min-w-0 md:flex-none md:w-72" @submit.prevent="emit('search')">
        <i
          class="fas fa-search absolute left-3 top-1/2 -translate-y-1/2 text-sm text-neutral-400"
        ></i>
        <input
          v-model="keyword"
          type="search"
          enterkeyhint="search"
          :placeholder="placeholder"
          class="h-10 w-full rounded-md border-neutral-300 pl-9 text-sm focus:border-primary-500 focus:ring-primary-500"
        />
      </form>
      <div v-if="$slots.default" class="hidden md:flex flex-wrap items-center gap-3">
        <slot />
      </div>
      <button
        v-if="$slots.default"
        type="button"
        class="md:hidden btn btn-secondary shrink-0"
        @click="drawer = true"
      >
        <i class="fas fa-sliders-h"></i>筛选<span v-if="tags.length">（{{ tags.length }}）</span>
      </button>
    </div>

    <div v-if="tags.length" class="flex flex-wrap items-center gap-2 text-sm">
      <span class="text-neutral-500">已选：</span>
      <button
        v-for="tag in tags"
        :key="tag.key"
        type="button"
        class="chip chip-active h-7"
        :aria-label="`移除 ${tag.label}`"
        @click="emit('remove', tag.key)"
      >
        {{ tag.label }}<i class="fas fa-times text-[10px] opacity-70"></i>
      </button>
      <button type="button" class="text-primary-700 hover:underline" @click="emit('clear')">
        清空
      </button>
    </div>

    <ClientOnly>
      <el-drawer v-model="drawer" direction="btt" size="auto" title="筛选" class="!rounded-t-lg">
        <div class="space-y-4">
          <slot />
        </div>
        <template #footer>
          <button type="button" class="btn btn-primary w-full" @click="drawer = false">
            查看结果
          </button>
        </template>
      </el-drawer>
    </ClientOnly>
  </div>
</template>

<script setup lang="ts">
export interface FilterTag {
  key: string
  label: string
}

withDefaults(defineProps<{ tags?: FilterTag[]; placeholder?: string }>(), {
  tags: () => [],
  placeholder: '搜索'
})
const keyword = defineModel<string>('keyword', { default: '' })
const emit = defineEmits<{ search: []; remove: [key: string]; clear: [] }>()

const drawer = ref(false)
</script>
