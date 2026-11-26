<template>
  <div v-if="detail" class="container py-8 md:py-12">
    <article class="mx-auto max-w-4xl">
      <AppBreadcrumb :items="[{ name: '成功案例', to: '/cases' }, { name: detail.title }]" />
      <h1 class="mt-4 text-2xl md:text-4xl font-bold text-neutral-900 break-words">{{ detail.title }}</h1>
      <p v-if="detail.createTime" class="mt-3 text-sm text-neutral-500">
        <i class="far fa-calendar-alt mr-1.5"></i>{{ formatDate(detail.createTime) }}
      </p>
      <!-- 封面大图，点开放大 -->
      <el-image
        v-if="detail.coverUrl"
        :src="fileUrl(detail.coverUrl)"
        :alt="detail.title"
        :preview-src-list="[fileUrl(detail.coverUrl)]"
        fit="cover"
        preview-teleported
        class="mt-6 aspect-[16/9] w-full rounded-lg bg-neutral-100"
      />
      <!-- 正文：超宽内容在容器内横向滚动；图片原生懒加载、点开放大；统一渲染组件见 #30 -->
      <div class="mt-8 overflow-x-auto">
        <div class="prose prose-neutral max-w-none [&_img]:cursor-zoom-in" @click="openImage" v-html="content"></div>
      </div>
      <el-image-viewer
        v-if="viewer"
        :url-list="viewer.urls"
        :initial-index="viewer.index"
        teleported
        @close="viewer = null"
      />
      <NuxtLink to="/cases" class="btn btn-ghost mt-10 px-0">
        <i class="fas fa-arrow-left text-xs"></i>返回成功案例
      </NuxtLink>
    </article>

    <ConsultBar class="mt-12" title="想参加这个展？" desc="顾问为您介绍下一届的时间、展位和费用">
      <NuxtLink to="/contact" class="btn btn-lg border border-white/60 text-white hover:bg-white/10">
        立即咨询
      </NuxtLink>
    </ConsultBar>
  </div>
</template>

<script setup lang="ts">
import AppBreadcrumb from '~/components/common/AppBreadcrumb.vue'
import ConsultBar from '~/components/common/ConsultBar.vue'

const route = useRoute()
const api = useApi()
const { data: detail, error } = await useAsyncData(`case-${route.params.id}`, () =>
  api.cases.getDetail(Number(route.params.id))
)
if (error.value) {
  throw createError({ statusCode: 500, message: '获取案例详情失败', fatal: true })
}
if (!detail.value) {
  throw createError({ statusCode: 404, message: '案例不存在', fatal: true })
}

// 正文多图：补上原生懒加载
const content = computed(() => (detail.value?.content ?? '').replace(/<img(?![^>]*\bloading=)/g, '<img loading="lazy"'))

const viewer = ref<{ urls: string[]; index: number } | null>(null)
const openImage = (e: MouseEvent) => {
  if (!(e.target instanceof HTMLImageElement)) return
  const imgs = [...(e.currentTarget as HTMLElement).querySelectorAll('img')]
  viewer.value = { urls: imgs.map(img => img.src), index: imgs.indexOf(e.target) }
}

useSeoMeta({
  title: () => detail.value?.title,
  description: () => toSummary(detail.value?.content)
})
</script>
