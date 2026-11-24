<template>
  <!-- 咨询转化条：每页底部（页脚之前）放一条，文案按页面场景写；默认按钮是电话 + 联系顾问 -->
  <div
    class="rounded-lg bg-primary-700 px-6 py-6 md:px-10 md:py-8 flex flex-col md:flex-row md:items-center gap-5 md:gap-8"
  >
    <div class="flex-1 min-w-0">
      <p class="text-lg md:text-xl font-semibold text-white">{{ title }}</p>
      <p v-if="desc" class="mt-1 text-sm text-primary-100">{{ desc }}</p>
    </div>
    <div class="flex flex-wrap gap-3">
      <a
        v-if="contact.hotline"
        :href="telHref(contact.hotline)"
        class="btn btn-lg bg-white text-primary-700 hover:bg-primary-50"
      >
        <i class="fas fa-phone-alt"></i>{{ contact.hotline }}
      </a>
      <slot>
        <NuxtLink
          to="/contact"
          class="btn btn-lg border border-white/60 text-white hover:bg-white/10"
        >
          联系顾问
        </NuxtLink>
      </slot>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{ title: string; desc?: string }>()

const contact = useSiteContact()
</script>
