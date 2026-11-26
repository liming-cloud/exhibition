<template>
  <div v-if="detail" class="container py-8 md:py-12">
    <article class="mx-auto max-w-3xl">
      <AppBreadcrumb :items="[{ name: '市场简讯', to: '/news' }, { name: detail.title }]" />
      <h1 class="mt-4 text-2xl md:text-4xl font-bold text-neutral-900 break-words">{{ detail.title }}</h1>
      <p class="mt-3 text-sm text-neutral-500">
        <i class="far fa-calendar-alt mr-1.5"></i>发布于 {{ formatDate(detail.createTime) }}
      </p>
      <img
        v-if="detail.coverUrl"
        :src="fileUrl(detail.coverUrl)"
        :alt="detail.title"
        class="mt-6 max-h-[420px] w-full rounded-lg object-cover bg-neutral-100"
      />
      <!-- 正文：超宽图片/表格在容器内横向滚动，不撑破手机端；统一渲染组件见 #30 -->
      <div class="mt-8 overflow-x-auto">
        <div class="prose prose-neutral max-w-none" v-html="detail.content"></div>
      </div>
      <NuxtLink to="/news" class="btn btn-ghost mt-10 px-0">
        <i class="fas fa-arrow-left text-xs"></i>返回市场简讯
      </NuxtLink>
    </article>

    <ConsultBar class="mt-12" title="想了解这个市场的展会？" desc="顾问为您推荐适合的海外展会和参展方案">
      <NuxtLink to="/exhibitions" class="btn btn-lg border border-white/60 text-white hover:bg-white/10">
        查看展会
      </NuxtLink>
    </ConsultBar>
  </div>
</template>

<script setup lang="ts">
import AppBreadcrumb from '~/components/common/AppBreadcrumb.vue'
import ConsultBar from '~/components/common/ConsultBar.vue'

const route = useRoute()
const api = useApi()
const { data: detail, error } = await useAsyncData(`news-${route.params.id}`, () =>
  api.information.getDetail(Number(route.params.id))
)
if (error.value) {
  throw createError({ statusCode: 500, message: '获取资讯详情失败', fatal: true })
}
if (!detail.value) {
  throw createError({ statusCode: 404, message: '资讯不存在', fatal: true })
}

useSeoMeta({
  title: () => detail.value?.title,
  description: () => toSummary(detail.value?.content)
})
</script>
