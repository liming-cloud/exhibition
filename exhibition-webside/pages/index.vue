<template>
  <div>
    <!-- 1 通栏轮播（R2） -->
    <HomeCarousel />

    <!-- 2 全球展会分布 + 3 展会项目（R5）：共用同一份展会数据，点地图或国家标签都会筛选下方列表 -->
    <section class="py-12 md:py-16 bg-neutral-50">
      <div class="container">
        <div class="flex flex-wrap items-end justify-between gap-6">
          <div>
            <p class="section-eyebrow">Global Network</p>
            <h2 class="section-title">全球展会分布</h2>
            <p class="section-desc">点击地图上的国家，查看该国展会</p>
          </div>
          <dl class="flex gap-8 md:gap-12">
            <div v-for="stat in stats" :key="stat.label">
              <dt class="text-xs text-neutral-500">{{ stat.label }}</dt>
              <dd class="mt-1 text-3xl font-bold text-primary-600">{{ stat.value }}</dd>
            </div>
          </dl>
        </div>

        <div class="mt-6 rounded-lg bg-white border border-neutral-200 overflow-hidden">
          <ClientOnly>
            <WorldMap
              :exhibitions="exhibitions"
              :selected="selectedCountry"
              @select="selectFromMap"
            />
            <template #fallback>
              <div class="h-[260px] sm:h-[360px] lg:h-[440px]"></div>
            </template>
          </ClientOnly>
        </div>

        <div ref="boardRef" class="mt-10 scroll-mt-24">
          <p class="section-eyebrow">Our Projects</p>
          <h2 class="section-title">展会项目</h2>
          <p class="section-desc mb-6">我们代理和组织的海外展会，按国家查看</p>
          <HomeExhibitionBoard
            v-model="selectedCountry"
            :exhibitions="exhibitions"
            :areas="areas"
          />
        </div>
      </div>
    </section>

    <!-- 4 我们的服务（R3）：放在最后，紧凑一行 -->
    <section class="py-12">
      <div class="container">
        <div class="flex flex-wrap items-end justify-between gap-4 mb-6">
          <div>
            <p class="section-eyebrow">Our Services</p>
            <h2 class="section-title">我们的服务</h2>
          </div>
          <NuxtLink to="/contact" class="text-sm text-primary-600 hover:text-primary-700">
            咨询参展方案<i class="fas fa-arrow-right ml-1 text-xs"></i>
          </NuxtLink>
        </div>
        <HomeServices />
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import HomeCarousel from '~/components/home/HomeCarousel.vue'
import HomeServices from '~/components/home/HomeServices.vue'
import HomeExhibitionBoard from '~/components/home/HomeExhibitionBoard.vue'
import WorldMap from '~/components/home/WorldMap.vue'

const api = useApi()

// 首页展会数据服务端取一次，地图与展会项目板块共用
const { data } = await useAsyncData('home-exhibitions', async () => {
  const [areas, list] = await Promise.all([
    api.exhibition.getGlobalArea(),
    api.exhibition.getList({ pageNum: 1, pageSize: 999 })
  ])
  return { areas, rows: list.rows }
})
const exhibitions = computed(() => data.value?.rows ?? [])
const areas = computed(() => data.value?.areas ?? [])

const stats = computed(() => [
  { label: '覆盖国家', value: new Set(exhibitions.value.map(e => e.country)).size },
  { label: '即将开展', value: exhibitions.value.filter(e => isUpcoming(e)).length }
])

const selectedCountry = ref<number | null>(null)
const boardRef = ref<HTMLElement | null>(null)

const selectFromMap = (countryId: number) => {
  selectedCountry.value = countryId
  boardRef.value?.scrollIntoView({ behavior: 'smooth', block: 'start' })
}
</script>
