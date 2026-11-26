<template>
  <div>
    <PageBanner
      title="展会列表"
      en="Exhibitions"
      :desc="`共 ${all.length} 个展会，覆盖 ${countryCount} 个国家`"
      :crumbs="[{ name: '展会列表' }]"
    />

    <div class="container py-8 md:py-12">
      <!-- 筛选条件全部写进地址栏：可分享，首页「查看更多」带 ?country= 进入 -->
      <FilterBar
        v-model:keyword="keyword"
        placeholder="搜索展会名称"
        :tags="tags"
        @search="setQuery({ q: keyword.trim() })"
        @remove="removeTag"
        @clear="clearAll"
      >
        <el-select
          :model-value="filters.type"
          placeholder="行业"
          clearable
          class="!w-full md:!w-32"
          @update:model-value="v => setQuery({ type: v })"
        >
          <el-option v-for="(label, value) in ExhibitionTypeMap" :key="value" :label="label" :value="value" />
        </el-select>
        <el-select
          :model-value="filters.year"
          placeholder="年份"
          clearable
          class="!w-full md:!w-28"
          @update:model-value="v => setQuery({ year: v })"
        >
          <el-option v-for="y in years" :key="y" :label="`${y} 年`" :value="y" />
        </el-select>
        <el-select
          :model-value="filters.continent"
          placeholder="大洲"
          clearable
          class="!w-full md:!w-32"
          @update:model-value="v => setQuery({ continent: v, country: '' })"
        >
          <el-option v-for="c in continents" :key="c" :label="c" :value="c" />
        </el-select>
        <el-select
          :model-value="filters.country || undefined"
          placeholder="国家"
          clearable
          filterable
          class="!w-full md:!w-36"
          @update:model-value="v => setQuery({ country: v ? String(v) : '' })"
        >
          <el-option v-for="a in countryOptions" :key="a.id" :label="a.countryName" :value="a.id" />
        </el-select>
      </FilterBar>

      <div ref="listRef" class="mt-6 scroll-mt-24">
        <div v-if="pageRows.length" class="space-y-3">
          <ListRow
            v-for="e in pageRows"
            :key="e.id"
            :to="`/exhibitions/${e.id}`"
            :title="e.name"
            :muted="!isUpcoming(e)"
          >
            <template #leading>
              <DateBlock :date="exhibitionStart(e)" :muted="!isUpcoming(e)" />
            </template>
            <template #badge>
              <span v-if="e.featured" class="tag tag-accent h-5 mr-1.5 align-[2px]">主推</span>
              <span v-if="!isUpcoming(e)" class="tag tag-muted h-5 mr-1.5 align-[2px]">已结束</span>
            </template>
            <p class="truncate">
              <i class="fas fa-map-marker-alt mr-1 text-neutral-400"></i>{{ [e.countryName, e.location].filter(Boolean).join(' · ') }}
              <template v-if="ExhibitionTypeMap[e.exhibitionType]">
                <span class="mx-1.5 text-neutral-300">|</span>{{ ExhibitionTypeMap[e.exhibitionType] }}
              </template>
            </p>
            <template #actions>
              <NuxtLink :to="`/exhibitions/${e.id}`" class="btn btn-ghost h-9 px-3">详情</NuxtLink>
              <button v-if="isUpcoming(e)" type="button" class="btn btn-primary h-9" @click="register(e)">报名</button>
            </template>
          </ListRow>
        </div>
        <EmptyState v-else-if="tags.length" title="没有符合条件的展会" desc="换个条件试试，或让顾问帮您找" icon="fa-search">
          <button type="button" class="btn btn-secondary" @click="clearAll">清空筛选</button>
          <NuxtLink to="/contact" class="btn btn-primary">联系顾问</NuxtLink>
        </EmptyState>
        <EmptyState v-else title="暂无展会" desc="展会信息更新中，可以直接联系顾问了解" icon="fa-calendar-alt">
          <NuxtLink to="/contact" class="btn btn-primary">联系顾问</NuxtLink>
        </EmptyState>
      </div>

      <AppPagination
        :model-value="filters.page"
        :total="filtered.length"
        :page-size="pageSize"
        @update:model-value="p => setQuery({ page: p > 1 ? String(p) : '' }, false)"
      />

      <ConsultBar class="mt-12" title="没找到想去的展会？" desc="告诉我们您的行业和目标市场，顾问帮您找" />
    </div>

    <RegistrationModal v-model="registerVisible" :exhibition="registering" />
  </div>
</template>

<script setup lang="ts">
import type { Exhibition } from '~/types/exhibition'
import { ExhibitionTypeMap } from '~/types/exhibition'
import PageBanner from '~/components/common/PageBanner.vue'
import FilterBar, { type FilterTag } from '~/components/common/FilterBar.vue'
import ListRow from '~/components/common/ListRow.vue'
import DateBlock from '~/components/common/DateBlock.vue'
import EmptyState from '~/components/common/EmptyState.vue'
import AppPagination from '~/components/common/AppPagination.vue'
import ConsultBar from '~/components/common/ConsultBar.vue'
import RegistrationModal from '~/components/common/RegistrationModal.vue'

const api = useApi()
const route = useRoute()
const router = useRouter()
const pageSize = 10

// 展会数量在几百条以内，服务端一次取全量，筛选分页在前端完成（页面清单「现状差异」）
const { data } = await useAsyncData('exhibition-list', async () => {
  const [areas, list] = await Promise.all([
    api.exhibition.getGlobalArea(),
    api.exhibition.getList({ pageNum: 1, pageSize: 999 })
  ])
  return { areas, rows: list.rows }
})
const all = computed(() => data.value?.rows ?? [])
const areas = computed(() => data.value?.areas ?? [])
const countryCount = computed(() => new Set(all.value.map(e => e.country)).size)

// 地址栏是唯一的筛选状态：q 关键词、type 行业、year 年份、continent 大洲、country 国家 id、page 页码
const str = (v: unknown) => (typeof v === 'string' ? v : '')
const filters = computed(() => ({
  q: str(route.query.q),
  type: str(route.query.type),
  year: str(route.query.year),
  continent: str(route.query.continent),
  country: Number(route.query.country) || 0,
  page: Math.max(1, Number(route.query.page) || 1)
}))

const keyword = ref(filters.value.q)
watch(() => filters.value.q, q => (keyword.value = q))

// 改筛选条件时回到第 1 页；翻页时保留其他条件
const setQuery = (patch: Record<string, string | undefined>, resetPage = true) => {
  const query: Record<string, string> = {}
  for (const [k, v] of Object.entries({ ...route.query, ...(resetPage ? { page: '' } : {}), ...patch })) {
    if (typeof v === 'string' && v) query[k] = v
  }
  router.replace({ query })
}

const continentOf = (countryId: number) => areas.value.find(a => a.id === countryId)?.continentName ?? ''
const continents = computed(() => [...new Set(areas.value.map(a => a.continentName).filter(Boolean))].sort())
const countryOptions = computed(() =>
  areas.value.filter(a => !filters.value.continent || a.continentName === filters.value.continent)
)
const years = computed(() => [...new Set(all.value.map(e => exhibitionStart(e).slice(0, 4)))].sort())

// 即将开始的按开始日期升序在前，已结束的按日期倒序排后
const filtered = computed(() => {
  const f = filters.value
  const q = f.q.toLowerCase()
  const rows = all.value.filter(
    e =>
      (!q || e.name.toLowerCase().includes(q)) &&
      (!f.type || e.exhibitionType === f.type) &&
      (!f.year || exhibitionStart(e).startsWith(f.year)) &&
      (!f.continent || continentOf(e.country) === f.continent) &&
      (!f.country || e.country === f.country)
  )
  const upcoming = rows.filter(e => isUpcoming(e)).sort((a, b) => exhibitionStart(a).localeCompare(exhibitionStart(b)))
  const ended = rows.filter(e => !isUpcoming(e)).sort((a, b) => exhibitionStart(b).localeCompare(exhibitionStart(a)))
  return [...upcoming, ...ended]
})
const pageRows = computed(() => filtered.value.slice((filters.value.page - 1) * pageSize, filters.value.page * pageSize))

const tags = computed<FilterTag[]>(() => {
  const f = filters.value
  const list: FilterTag[] = []
  if (f.q) list.push({ key: 'q', label: `“${f.q}”` })
  if (f.type) list.push({ key: 'type', label: ExhibitionTypeMap[f.type as keyof typeof ExhibitionTypeMap] ?? f.type })
  if (f.year) list.push({ key: 'year', label: `${f.year} 年` })
  if (f.continent) list.push({ key: 'continent', label: f.continent })
  if (f.country) list.push({ key: 'country', label: areas.value.find(a => a.id === f.country)?.countryName ?? '国家' })
  return list
})
const removeTag = (key: string) => setQuery(key === 'continent' ? { continent: '', country: '' } : { [key]: '' })
const clearAll = () => router.replace({ query: {} })

const listRef = ref<HTMLElement>()
watch(
  () => filters.value.page,
  () => listRef.value?.scrollIntoView({ behavior: 'smooth' })
)

const registerVisible = ref(false)
const registering = ref<Exhibition | null>(null)
const register = (e: Exhibition) => {
  registering.value = e
  registerVisible.value = true
}

useSeoMeta({
  title: '展会列表',
  description: () =>
    `雅博展览海外展会目录：共 ${all.value.length} 个展会，覆盖 ${countryCount.value} 个国家，可按行业、年份、大洲、国家筛选`
})
</script>
