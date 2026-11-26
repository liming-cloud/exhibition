<template>
  <div>
    <!-- 国家标签：按大洲分组，打开即可看到我们做哪些国家的项目 -->
    <div class="divide-y divide-neutral-100 border border-neutral-200 rounded-lg bg-white">
      <div class="flex flex-wrap items-center gap-2 px-4 py-3">
        <span class="continent-label">全部</span>
        <button class="chip" :class="{ 'chip-active': selected === null }" @click="selected = null">
          全部国家 <span class="chip-count">{{ exhibitions.length }}</span>
        </button>
      </div>
      <div
        v-for="group in continents"
        :key="group.name"
        class="flex flex-wrap items-center gap-2 px-4 py-3"
      >
        <span class="continent-label">{{ group.name }}</span>
        <button
          v-for="c in group.countries"
          :key="c.id"
          class="chip"
          :class="{ 'chip-active': selected === c.id }"
          @click="selected = selected === c.id ? null : c.id"
        >
          {{ c.name }} <span class="chip-count">{{ c.count }}</span>
        </button>
      </div>
    </div>

    <!-- 展会列表：首页只放 6 个（即将开始的在前，日期放在最前面），其余点底部「查看更多」进展会列表 -->
    <h3 class="mt-6 text-lg font-semibold text-neutral-900">
      {{ selectedName ? `${selectedName}展会` : '近期展会' }}
      <span class="ml-2 text-sm font-normal text-neutral-500"
        >即将开始 {{ upcoming.length }} 场</span
      >
    </h3>

    <div v-if="shown.length" class="mt-4 grid grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-3">
      <NuxtLink
        v-for="e in shown"
        :key="e.id"
        :to="`/exhibitions/${e.id}`"
        class="group flex gap-4 p-4 rounded-lg border border-neutral-200 bg-white hover:border-primary-300 hover:shadow-sm transition"
      >
        <div
          class="w-16 shrink-0 rounded-md bg-primary-50 text-primary-700 flex flex-col items-center justify-center py-2"
        >
          <span class="text-lg font-bold leading-none">{{
            exhibitionStart(e).slice(5).replace('-', '.')
          }}</span>
          <span class="mt-1 text-xs text-primary-600/80">{{ exhibitionStart(e).slice(0, 4) }}</span>
        </div>
        <div class="min-w-0 flex-1">
          <p class="font-medium text-neutral-900 line-clamp-2 group-hover:text-primary-700">
            <span
              v-if="e.featured"
              class="mr-1.5 align-[2px] inline-block px-1.5 py-0.5 rounded bg-accent-600 text-white text-[11px] leading-none"
              >重点推荐</span
            >
            {{ e.name }}
          </p>
          <p class="mt-1.5 text-sm text-neutral-500 truncate">
            <i class="fas fa-map-marker-alt mr-1 text-neutral-400"></i>{{ e.countryName }} ·
            {{ e.location }}
          </p>
          <p class="mt-1 text-sm text-neutral-500">
            <i class="fas fa-tag mr-1 text-neutral-400"></i>{{ ExhibitionTypeMap[e.exhibitionType] }}
          </p>
        </div>
      </NuxtLink>
    </div>
    <div
      v-else
      class="mt-4 rounded-lg border border-dashed border-neutral-300 bg-white py-10 text-center text-neutral-500"
    >
      {{ selectedName }}暂无即将开始的展会，
      <NuxtLink to="/contact" class="text-primary-600 hover:underline">联系顾问</NuxtLink
      >了解下一届安排
    </div>

    <div class="mt-8 text-center">
      <NuxtLink :to="allLink" class="btn btn-secondary btn-lg">
        查看更多展会<span class="text-xs opacity-70">MORE</span
        ><i class="fas fa-arrow-right text-xs"></i>
      </NuxtLink>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ExhibitionTypeMap, type Exhibition, type GlobalArea } from '~/types/exhibition'

const props = defineProps<{ exhibitions: Exhibition[]; areas: GlobalArea[] }>()
const selected = defineModel<number | null>({ default: null })

const continentOrder = ['亚洲', '欧洲', '北美洲', '南美洲', '非洲', '大洋洲']
const SHOW_LIMIT = 6

const continents = computed(() => {
  const groups = new Map<string, Map<number, { id: number; name: string; count: number }>>()
  for (const e of props.exhibitions) {
    const continent = props.areas.find(a => a.id === e.country)?.continentName || '其他'
    const countries = groups.get(continent) ?? new Map()
    const item = countries.get(e.country) ?? { id: e.country, name: e.countryName, count: 0 }
    item.count++
    countries.set(e.country, item)
    groups.set(continent, countries)
  }
  const rank = (name: string) => (continentOrder.includes(name) ? continentOrder.indexOf(name) : 99)
  return [...groups.entries()]
    .sort(([a], [b]) => rank(a) - rank(b))
    .map(([name, countries]) => ({
      name,
      countries: [...countries.values()].sort((a, b) => b.count - a.count)
    }))
})

const selectedName = computed(
  () => props.exhibitions.find(e => e.country === selected.value)?.countryName ?? ''
)

const upcoming = computed(() =>
  props.exhibitions
    .filter(e => (selected.value === null || e.country === selected.value) && isUpcoming(e))
    .sort((a, b) => exhibitionStart(a).localeCompare(exhibitionStart(b)))
)
const shown = computed(() => upcoming.value.slice(0, SHOW_LIMIT))
const allLink = computed(() =>
  selected.value ? `/exhibitions?country=${selected.value}` : '/exhibitions'
)
</script>

<style scoped>
.continent-label {
  @apply w-14 shrink-0 text-sm font-medium text-neutral-500;
}

.chip {
  @apply inline-flex items-center gap-1 h-8 px-3 rounded-full border border-neutral-200 text-sm text-neutral-700 hover:border-primary-400 hover:text-primary-700 transition-colors;
}

.chip-active {
  @apply bg-primary-600 border-primary-600 text-white hover:text-white;
}

.chip-count {
  @apply text-xs opacity-60;
}
</style>
