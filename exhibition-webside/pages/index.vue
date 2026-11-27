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
            <p class="section-desc">
              <span class="hidden sm:inline">点击地图上的国家，查看该国展会</span>
              <span class="sm:hidden">按国家查看下方展会</span>
            </p>
          </div>
          <dl class="flex gap-8 md:gap-12">
            <div v-for="stat in stats" :key="stat.label">
              <dt class="text-xs text-neutral-500">{{ stat.label }}</dt>
              <dd class="mt-1 text-3xl font-bold text-primary-600"><CountUp :value="stat.value" /></dd>
            </div>
          </dl>
        </div>

        <!-- 手机端地图太小、打点点不准：不渲染地图，直接用下方「展会项目」的国家标签 -->
        <div class="mt-6 hidden sm:block rounded-lg bg-white border border-neutral-200 overflow-hidden">
          <ClientOnly>
            <WorldMap
              v-if="showMap"
              :countries="countries"
              :selected="selectedCountry"
              @select="selectFromMap"
            />
            <template #fallback>
              <div class="h-[360px] lg:h-[440px]"></div>
            </template>
          </ClientOnly>
        </div>

        <div ref="boardRef" class="mt-10 scroll-mt-24">
          <p class="section-eyebrow">Our Projects</p>
          <h2 class="section-title">展会项目</h2>
          <p class="section-desc mb-6">我们代理和组织的海外展会，按国家查看</p>
          <HomeExhibitionBoard
            v-model="selectedCountry"
            :countries="countries"
            :upcoming="upcoming"
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

        <!-- 5 咨询条 -->
        <ConsultBar class="mt-12" title="不知道选哪个展？" desc="告诉我们您的行业和目标市场，顾问免费为您推荐" />
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import HomeCarousel from '~/components/home/HomeCarousel.vue'
import HomeServices from '~/components/home/HomeServices.vue'
import HomeExhibitionBoard from '~/components/home/HomeExhibitionBoard.vue'
import WorldMap from '~/components/home/WorldMap.vue'
import ConsultBar from '~/components/common/ConsultBar.vue'
import CountUp from '~/components/common/CountUp.vue'

const api = useApi()
// 首页只放 6 个展会，其余点「查看更多」进展会列表
const HOME_UPCOMING_LIMIT = 6

// 有展会的国家及数量：地图、国家标签、信任数字共用
const { data: countryData } = await useAsyncData('home-countries', () => api.exhibition.getCountries())
const countries = computed(() => countryData.value ?? [])

const stats = computed(() => exhibitionStats(countries.value))

const selectedCountry = ref<number | null>(null)

// 即将开始的展会：选中国家后重新查
const { data: upcomingData } = await useAsyncData(
  'home-upcoming',
  () =>
    api.exhibition.getList({
      pageNum: 1,
      pageSize: HOME_UPCOMING_LIMIT,
      upcoming: true,
      country: selectedCountry.value ?? undefined
    }),
  { watch: [selectedCountry] }
)
const upcoming = computed(() => upcomingData.value?.rows ?? [])

// 只在 sm（640px）及以上加载地图
const showMap = ref(false)
onMounted(() => (showMap.value = window.matchMedia('(min-width: 640px)').matches))
const boardRef = ref<HTMLElement | null>(null)

const selectFromMap = (countryId: number) => {
  selectedCountry.value = countryId
  boardRef.value?.scrollIntoView({ behavior: 'smooth', block: 'start' })
}

useSeoMeta({
  description: () =>
    `雅博展览专注境外展会组展与参展服务，覆盖 ${stats.value[0].value} 个国家的能源、电梯、医疗等行业展会，提供展位预订、展台搭建、签证行程与补贴申请一站式服务`
})
</script>
