<template>
  <div>
    <PageBanner
      title="市场简讯"
      en="Market News"
      desc="海外市场动态与参展攻略"
      :crumbs="[{ name: '市场简讯' }]"
    />

    <div class="container py-8 md:py-12">
      <div class="grid gap-8 lg:grid-cols-[1fr_320px]">
        <div class="min-w-0">
          <FilterBar v-model:keyword="keyword" placeholder="搜索简讯标题" @search="search" @clear="clear" />

          <div ref="listRef" class="mt-6 scroll-mt-24">
            <el-skeleton v-if="status === 'pending'" :rows="6" animated class="rounded-lg border border-neutral-200 p-4" />
            <div v-else-if="rows.length" class="space-y-3">
              <ListRow v-for="n in rows" :key="n.id" :to="`/news/${n.id}`" :title="n.title">
                <!-- 无封面图时不留位，退化为纯文字行 -->
                <template #leading>
                  <img
                    v-if="n.coverUrl"
                    :src="fileUrl(n.coverUrl)"
                    alt=""
                    class="w-24 h-[72px] md:w-32 md:h-24 shrink-0 rounded-md object-cover bg-neutral-100"
                    loading="lazy"
                  />
                </template>
                <p class="line-clamp-2">{{ toSummary(n.content, 120) }}</p>
                <p class="text-xs text-neutral-400">{{ formatDate(n.createTime) }}</p>
              </ListRow>
            </div>
            <EmptyState
              v-else-if="applied"
              title="没有找到相关简讯"
              desc="换个关键词试试"
              icon="fa-search"
            >
              <button type="button" class="btn btn-secondary" @click="clear">清空搜索</button>
            </EmptyState>
            <EmptyState v-else title="暂无市场简讯" desc="内容整理中，可以先看看近期展会" icon="fa-newspaper">
              <NuxtLink to="/exhibitions" class="btn btn-primary">查看展会</NuxtLink>
            </EmptyState>
          </div>

          <AppPagination v-model="page" :total="total" :page-size="pageSize" />
        </div>

        <!-- 侧栏：桌面在右侧，手机排在列表之后 -->
        <aside class="space-y-4">
          <div v-if="upcoming.length" class="rounded-lg border border-neutral-200 bg-white p-5">
            <h2 class="text-base font-semibold text-neutral-900">近期展会</h2>
            <ul class="mt-3 divide-y divide-neutral-100">
              <li v-for="e in upcoming" :key="e.id" class="py-2.5">
                <NuxtLink :to="`/exhibitions/${e.id}`" class="group block">
                  <p class="text-sm text-neutral-900 line-clamp-2 group-hover:text-primary-700">{{ e.name }}</p>
                  <p class="mt-0.5 text-xs text-neutral-500">{{ exhibitionStart(e) }} · {{ e.countryName }}</p>
                </NuxtLink>
              </li>
            </ul>
            <NuxtLink to="/exhibitions" class="mt-2 inline-block text-sm text-primary-600 hover:text-primary-700">
              查看全部展会<i class="fas fa-arrow-right ml-1 text-xs"></i>
            </NuxtLink>
          </div>
          <div v-if="contact.hotline" class="rounded-lg bg-primary-50 p-5">
            <p class="text-sm text-neutral-600">想了解某个市场的展会？</p>
            <a :href="telHref(contact.hotline)" class="mt-1 block text-xl font-bold text-primary-700 break-all">
              <i class="fas fa-phone-alt mr-1.5 text-base"></i>{{ contact.hotline }}
            </a>
          </div>
        </aside>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import PageBanner from '~/components/common/PageBanner.vue'
import FilterBar from '~/components/common/FilterBar.vue'
import ListRow from '~/components/common/ListRow.vue'
import EmptyState from '~/components/common/EmptyState.vue'
import AppPagination from '~/components/common/AppPagination.vue'

const api = useApi()
const contact = useSiteContact()

const pageSize = 12
const page = ref(1)
const keyword = ref('')
// 点搜索后才生效的关键词，输入过程中不请求
const applied = ref('')

const { data, status } = await useAsyncData(
  'news-list',
  () => api.information.getList({ pageNum: page.value, pageSize, title: applied.value || undefined }),
  { watch: [page, applied] }
)
const rows = computed(() => data.value?.rows ?? [])
const total = computed(() => data.value?.total ?? 0)

const { data: exhibitions } = await useAsyncData('news-upcoming', () =>
  api.exhibition.getList({ pageNum: 1, pageSize: 999 })
)
const upcoming = computed(() =>
  (exhibitions.value?.rows ?? [])
    .filter(e => isUpcoming(e))
    .sort((a, b) => exhibitionStart(a).localeCompare(exhibitionStart(b)))
    .slice(0, 5)
)

const search = () => {
  page.value = 1
  applied.value = keyword.value.trim()
}
const clear = () => {
  keyword.value = ''
  search()
}

const listRef = ref<HTMLElement>()
watch(page, () => listRef.value?.scrollIntoView({ behavior: 'smooth' }))

useSeoMeta({
  title: '市场简讯',
  description: '雅博展览市场简讯：海外市场动态、展会资讯与参展攻略'
})
</script>
