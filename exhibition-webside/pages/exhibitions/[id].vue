<template>
  <div v-if="exhibition" class="container py-8 md:py-12">
    <AppBreadcrumb :items="[{ name: '展会列表', to: '/exhibitions' }, { name: exhibition.name }]" />

    <!-- 头图 + 标题区：无封面用主色色块 -->
    <header class="mt-4 grid gap-6 md:grid-cols-[280px_1fr] md:items-center">
      <div class="aspect-[16/9] md:aspect-[4/3] overflow-hidden rounded-lg bg-primary-700 flex items-center justify-center">
        <img
          v-if="exhibition.coverUrl"
          :src="fileUrl(exhibition.coverUrl)"
          :alt="exhibition.name"
          class="h-full w-full object-cover"
        />
        <i v-else class="fas fa-globe-asia text-5xl text-white/40"></i>
      </div>
      <div class="min-w-0">
        <div class="flex flex-wrap gap-2">
          <span v-if="exhibition.featured" class="tag tag-accent">主推</span>
          <span v-if="typeLabel" class="tag tag-primary">{{ typeLabel }}</span>
          <span v-if="ended" class="tag tag-muted">已结束</span>
        </div>
        <h1 class="mt-3 text-2xl md:text-4xl font-bold text-neutral-900 break-words">{{ exhibition.name }}</h1>
        <p v-if="exhibition.summary" class="mt-3 text-neutral-600 leading-relaxed">{{ exhibition.summary }}</p>
      </div>
    </header>

    <div class="mt-8 grid gap-8 lg:grid-cols-[1fr_340px] lg:items-start">
      <!-- 关键信息卡：桌面在右侧随滚动吸附；手机排在最前 -->
      <aside class="lg:order-2 lg:sticky lg:top-24 rounded-lg border border-neutral-200 bg-white p-5">
        <dl class="space-y-3 text-sm">
          <div v-for="item in facts" :key="item.label" class="flex gap-3">
            <dt class="w-14 shrink-0 text-neutral-500">{{ item.label }}</dt>
            <dd class="min-w-0 text-neutral-900 break-words">{{ item.value }}</dd>
          </div>
        </dl>
        <div class="mt-5 hidden md:grid gap-3">
          <button type="button" class="btn btn-lg w-full" :class="ended ? 'btn-secondary' : 'btn-primary'" @click="registerVisible = true">
            {{ ended ? '咨询下一届' : '立即报名' }}
          </button>
          <a v-if="contact.hotline" :href="telHref(contact.hotline)" class="btn btn-secondary btn-lg w-full">
            <i class="fas fa-phone-alt"></i>电话咨询 {{ contact.hotline }}
          </a>
        </div>
      </aside>

      <div class="min-w-0 lg:order-1 space-y-10">
        <!-- 正文为空时隐藏；超宽表格在正文内横向滚动，统一渲染组件见 #30 -->
        <section v-if="hasContent" class="overflow-x-auto">
          <div class="prose prose-neutral max-w-none" v-html="exhibition.content"></div>
        </section>

        <!-- 负责顾问：sort 前 2 位（D6），接口无数据时隐藏 -->
        <section v-if="consultants.length">
          <h2 class="mb-4 text-lg font-semibold text-neutral-900">负责顾问</h2>
          <div class="grid gap-4 sm:grid-cols-2">
            <ConsultantCard v-for="c in consultants" :key="c.id" :consultant="c" />
          </div>
        </section>

        <section v-if="related.length">
          <h2 class="mb-4 text-lg font-semibold text-neutral-900">同国家 / 同行业展会</h2>
          <div class="space-y-3">
            <ListRow v-for="e in related" :key="e.id" :to="`/exhibitions/${e.id}`" :title="e.name">
              <template #leading><DateBlock :date="exhibitionStart(e)" /></template>
              <p class="truncate">{{ [e.countryName, e.location].filter(Boolean).join(' · ') }}</p>
            </ListRow>
          </div>
        </section>
      </div>
    </div>

    <!-- 手机端底部固定栏：电话 + 报名，替代全局联系条（本页 definePageMeta 隐藏全局联系条） -->
    <div
      class="md:hidden fixed inset-x-0 bottom-0 z-40 h-14 grid grid-cols-2 border-t border-neutral-200 bg-white pb-[env(safe-area-inset-bottom)]"
    >
      <a
        :href="telHref(contact.mobile || contact.hotline)"
        class="flex items-center justify-center gap-2 text-primary-700 text-sm font-medium"
      >
        <i class="fas fa-phone-alt"></i>电话咨询
      </a>
      <button
        type="button"
        class="flex items-center justify-center gap-2 bg-primary-600 text-white text-sm font-medium"
        @click="registerVisible = true"
      >
        {{ ended ? '咨询下一届' : '立即报名' }}
      </button>
    </div>

    <RegistrationModal v-model="registerVisible" :exhibition="exhibition" :next="ended" />
  </div>
</template>

<script setup lang="ts">
import { ExhibitionTypeMap, type Exhibition } from '~/types/exhibition'
import AppBreadcrumb from '~/components/common/AppBreadcrumb.vue'
import ListRow from '~/components/common/ListRow.vue'
import DateBlock from '~/components/common/DateBlock.vue'
import ConsultantCard from '~/components/common/ConsultantCard.vue'
import RegistrationModal from '~/components/common/RegistrationModal.vue'

definePageMeta({ hideContactBar: true })

const route = useRoute()
const api = useApi()
const contact = useSiteContact()

const { data: exhibition, error } = await useAsyncData(`exhibition-${route.params.id}`, () =>
  api.exhibition.getDetail(Number(route.params.id))
)
if (error.value) {
  throw createError({ statusCode: 500, message: '获取展会详情失败', fatal: true })
}
if (!exhibition.value) {
  throw createError({ statusCode: 404, message: '展会不存在', fatal: true })
}

// 顾问与同类展会（同国家或同行业、未结束的）：失败不影响详情主体
const RELATED_LIMIT = 4
const { data: extra } = await useAsyncData(`exhibition-extra-${route.params.id}`, async () => {
  const e = exhibition.value!
  // 多取 1 条，去掉当前展会后仍够数
  const fetchRelated = (query: { country?: number; type?: string }) =>
    api.exhibition
      .getList({ pageNum: 1, pageSize: RELATED_LIMIT + 1, upcoming: true, ...query })
      .catch(() => ({ rows: [] as Exhibition[] }))
  const [consultants, sameCountry, sameType] = await Promise.all([
    api.contact.getConsultants(),
    fetchRelated({ country: e.country }),
    fetchRelated({ type: e.exhibitionType })
  ])
  return { consultants: consultants.slice(0, 2), rows: [...sameCountry.rows, ...sameType.rows] }
})
const consultants = computed(() => extra.value?.consultants ?? [])

const ended = computed(() => !isUpcoming(exhibition.value!))
const typeLabel = computed(() => ExhibitionTypeMap[exhibition.value!.exhibitionType])
const hasContent = computed(() => !!toSummary(exhibition.value?.content, 1) || /<img|<table/i.test(exhibition.value?.content ?? ''))
const facts = computed(() => {
  const e = exhibition.value!
  return [
    { label: '时间', value: exhibitionDates(e) },
    { label: '国家', value: e.countryName },
    { label: '场馆', value: e.location },
    { label: '行业', value: typeLabel.value }
  ].filter(f => f.value)
})

// 两组合并去重，按开始日期升序，最多 4 条
const related = computed(() => {
  const id = exhibition.value!.id
  const rows = (extra.value?.rows ?? []).filter((r, i, all) => r.id !== id && all.findIndex(x => x.id === r.id) === i)
  return rows.sort((a, b) => exhibitionStart(a).localeCompare(exhibitionStart(b))).slice(0, RELATED_LIMIT)
})

const registerVisible = ref(false)

useSeoMeta({
  title: () => exhibition.value?.name,
  description: () => exhibition.value?.summary || toSummary(exhibition.value?.content)
})
</script>
