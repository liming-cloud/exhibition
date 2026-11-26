<template>
  <div v-if="detail" class="container py-8 md:py-12">
    <article class="mx-auto max-w-3xl">
      <AppBreadcrumb :items="[{ name: '补贴政策', to: '/subsidies' }, { name: detail.title }]" />
      <h1 class="mt-4 text-2xl md:text-4xl font-bold text-neutral-900 break-words">{{ detail.title }}</h1>
      <!-- 关键信息：状态、有效期、补贴金额（>0 才显示） -->
      <dl class="mt-6 grid gap-px overflow-hidden rounded-lg border border-neutral-200 bg-neutral-200 sm:grid-cols-3">
        <div class="bg-white p-4">
          <dt class="text-xs text-neutral-500">状态</dt>
          <dd class="mt-1"><span class="tag" :class="status.cls">{{ status.label }}</span></dd>
        </div>
        <div class="bg-white p-4" :class="{ 'sm:col-span-2': !(detail.price > 0) }">
          <dt class="text-xs text-neutral-500">有效期</dt>
          <dd class="mt-1 text-sm text-neutral-900">
            {{ detail.startTime?.slice(0, 10) }} 至 {{ detail.endTime?.slice(0, 10) }}
          </dd>
        </div>
        <div v-if="detail.price > 0" class="bg-white p-4">
          <dt class="text-xs text-neutral-500">最高补贴</dt>
          <dd class="mt-1 text-lg font-bold text-primary-700">{{ formatSubsidyPrice(detail.price) }}</dd>
        </div>
      </dl>
      <!-- 正文：比例/限额表格在容器内横向滚动；统一渲染组件见 #30 -->
      <div class="mt-8 overflow-x-auto">
        <div class="prose prose-neutral max-w-none" v-html="detail.content"></div>
      </div>
      <NuxtLink to="/subsidies" class="btn btn-ghost mt-10 px-0">
        <i class="fas fa-arrow-left text-xs"></i>返回补贴政策
      </NuxtLink>
    </article>

    <ConsultBar class="mt-12" title="我们可协助整理申报材料" desc="从资格评估到材料准备，顾问全程协助" />
  </div>
</template>

<script setup lang="ts">
import AppBreadcrumb from '~/components/common/AppBreadcrumb.vue'
import ConsultBar from '~/components/common/ConsultBar.vue'

const route = useRoute()
const api = useApi()
const { data: detail, error } = await useAsyncData(`subsidy-${route.params.id}`, () =>
  api.subsidies.getDetail(Number(route.params.id))
)
if (error.value) {
  throw createError({ statusCode: 500, message: '获取补贴详情失败', fatal: true })
}
if (!detail.value) {
  throw createError({ statusCode: 404, message: '政策不存在', fatal: true })
}

const status = computed(() => subsidyStatus(detail.value!))

useSeoMeta({
  title: () => detail.value?.title,
  description: () => toSummary(detail.value?.content)
})
</script>
