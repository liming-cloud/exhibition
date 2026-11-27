<template>
  <!-- 404 / 错误页：说清找不到、不怪用户、给出下一步（NN/g）；HTTP 状态码保持原错误码，不被搜索引擎收录 -->
  <NuxtLayout>
    <div class="container py-16 md:py-24">
      <div class="mx-auto max-w-2xl text-center">
        <p class="text-5xl font-bold text-primary-600">{{ notFound ? '404' : '抱歉' }}</p>
        <h1 class="mt-4 text-2xl md:text-3xl font-bold text-neutral-900">
          {{ notFound ? '页面不存在或展会已下架' : '页面暂时打不开，请稍后再试' }}
        </h1>
        <p class="mt-3 text-neutral-600">
          {{ notFound ? '链接可能已失效，可以回到首页，或看看近期的展会。' : '可以先回到首页，或直接电话联系我们。' }}
        </p>
        <div class="mt-8 flex flex-wrap justify-center gap-3">
          <NuxtLink to="/" class="btn btn-primary">返回首页</NuxtLink>
          <NuxtLink to="/exhibitions" class="btn btn-secondary">查看全部展会</NuxtLink>
        </div>
        <p class="mt-6 text-sm text-neutral-500">
          咨询电话
          <a :href="telHref(site.hotline)" class="font-medium text-primary-700 hover:underline">{{ site.hotline }}</a>
        </p>
      </div>

      <!-- 近期展会：浏览器端取数，接口失败或没有数据时隐藏 -->
      <section v-if="upcoming.length" class="mx-auto mt-12 max-w-3xl">
        <h2 class="mb-4 text-lg font-semibold text-neutral-900">近期展会</h2>
        <div class="space-y-3">
          <ListRow v-for="e in upcoming" :key="e.id" :to="`/exhibitions/${e.id}`" :title="e.name">
            <template #leading><DateBlock :date="exhibitionStart(e)" /></template>
            <p class="truncate">{{ [e.countryName, e.location].filter(Boolean).join(' · ') }}</p>
          </ListRow>
        </div>
      </section>
    </div>
  </NuxtLayout>
</template>

<script setup lang="ts">
import type { NuxtError } from '#app'
import ListRow from '~/components/common/ListRow.vue'
import DateBlock from '~/components/common/DateBlock.vue'
import { site } from '~/constants/site'

const props = defineProps<{ error: NuxtError }>()
const notFound = computed(() => props.error?.statusCode === 404)

const api = useApi()
const { data } = useLazyAsyncData(
  'error-upcoming',
  async () => {
    try {
      const { rows } = await api.exhibition.getList({ pageNum: 1, pageSize: 3, upcoming: true })
      return rows
    } catch {
      return []
    }
  },
  { server: false }
)
const upcoming = computed(() => data.value ?? [])

useSeoMeta({ title: () => (notFound.value ? '页面不存在' : '页面暂时打不开'), robots: 'noindex' })
</script>
