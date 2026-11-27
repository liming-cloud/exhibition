<template>
  <div>
    <!-- 国家标签：按大洲分组，打开即可看到我们做哪些国家的项目 -->
    <div class="divide-y divide-neutral-100 border border-neutral-200 rounded-lg bg-white">
      <div class="flex flex-wrap items-center gap-2 px-4 py-3">
        <span class="continent-label">全部</span>
        <button class="chip" :class="{ 'chip-active': selected === null }" @click="selected = null">
          全部国家 <span class="chip-count">{{ totalCount }}</span>
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
        >即将开始 {{ upcomingCount }} 场</span
      >
    </h3>

    <div v-if="upcoming.length" class="mt-4 grid grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-3">
      <NuxtLink
        v-for="e in upcoming"
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
              >主推</span
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
import { ExhibitionTypeMap, type CountryStat, type Exhibition } from '~/types/exhibition'

// countries：有展会的国家及数量；upcoming：当前选中国家（或全部）即将开始的前几个，由页面按选中国家查询
const props = defineProps<{ countries: CountryStat[]; upcoming: Exhibition[] }>()
const selected = defineModel<number | null>({ default: null })

const continentOrder = ['亚洲', '欧洲', '北美洲', '南美洲', '非洲', '大洋洲']

const continents = computed(() => {
  const groups = new Map<string, { id: number; name: string; count: number }[]>()
  for (const c of props.countries) {
    const continent = c.continentName || '其他'
    const list = groups.get(continent) ?? []
    list.push({ id: c.id, name: c.countryName, count: c.exhibitionCount })
    groups.set(continent, list)
  }
  const rank = (name: string) => (continentOrder.includes(name) ? continentOrder.indexOf(name) : 99)
  return [...groups.entries()]
    .sort(([a], [b]) => rank(a) - rank(b))
    .map(([name, countries]) => ({
      name,
      countries: countries.sort((a, b) => b.count - a.count)
    }))
})

const totalCount = computed(() => props.countries.reduce((sum, c) => sum + c.exhibitionCount, 0))
const selectedCountry = computed(() => props.countries.find(c => c.id === selected.value))
const selectedName = computed(() => selectedCountry.value?.countryName ?? '')
const upcomingCount = computed(() =>
  selected.value === null
    ? props.countries.reduce((sum, c) => sum + c.upcomingCount, 0)
    : (selectedCountry.value?.upcomingCount ?? 0)
)
const allLink = computed(() =>
  selected.value ? `/exhibitions?country=${selected.value}` : '/exhibitions'
)
</script>

<style scoped>
.continent-label {
  @apply w-14 shrink-0 text-sm font-medium text-neutral-500;
}

.chip-count {
  @apply text-xs opacity-60;
}
</style>
