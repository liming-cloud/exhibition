<template>
  <!-- 顾问卡片：展会详情、关于我们、联系我们共用。电话可拨，微信号可复制 -->
  <div class="flex items-start gap-4 p-5 rounded-lg border border-neutral-200 bg-white">
    <div
      class="w-12 h-12 shrink-0 rounded-full bg-primary-50 text-primary-700 flex items-center justify-center text-lg font-semibold"
    >
      {{ consultant.name.slice(0, 1) }}
    </div>
    <div class="min-w-0 flex-1">
      <p class="font-semibold text-neutral-900">{{ consultant.name }}</p>
      <p class="text-sm text-neutral-500 truncate">{{ consultant.title }}</p>
      <div class="mt-3 space-y-1.5 text-sm">
        <a
          :href="telHref(consultant.phone)"
          class="flex items-center gap-2 text-neutral-700 hover:text-primary-700"
        >
          <i class="fas fa-phone-alt w-4 text-primary-500"></i>{{ consultant.phone }}
        </a>
        <button
          v-if="consultant.wechat"
          type="button"
          class="flex items-center gap-2 max-w-full text-neutral-700 hover:text-primary-700"
          @click="copyWechat"
        >
          <i class="fab fa-weixin w-4 text-primary-500"></i>
          <span class="truncate">{{ consultant.wechat }}</span>
          <span class="shrink-0 text-xs text-primary-600">复制</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus'
import type { Consultant } from '~/types/contact'

const props = defineProps<{ consultant: Consultant }>()

const copyWechat = async () => {
  try {
    await navigator.clipboard.writeText(props.consultant.wechat ?? '')
    ElMessage.success('微信号已复制')
  } catch {
    ElMessage.info(`微信号：${props.consultant.wechat}`)
  }
}
</script>
