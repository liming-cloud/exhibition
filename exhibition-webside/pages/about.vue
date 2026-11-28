<template>
  <div>
    <PageBanner
      title="关于我们"
      en="About Us"
      desc="专注境外展会组展，为参展企业提供一站式出展服务"
      :crumbs="[{ name: '关于我们' }]"
    />

    <!-- 2 公司简介（后台「联系我们」维护）+ 最新案例封面（D11）；简介为空时隐藏 -->
    <section v-if="profile.length" class="container py-12 md:py-16">
      <div class="grid gap-8 lg:grid-cols-2 lg:items-center">
        <div>
          <SectionTitle eyebrow="Who We Are" title="公司简介" />
          <p v-for="(para, i) in profile" :key="i" class="mt-3 first:mt-0 text-neutral-600 leading-relaxed">
            {{ para }}
          </p>
        </div>
        <div v-if="photos[0]" class="aspect-[4/3] overflow-hidden rounded-lg bg-neutral-100">
          <img :src="fileUrl(photos[0].coverUrl)" :alt="photos[0].title" class="h-full w-full object-cover" />
        </div>
      </div>
    </section>

    <!-- 3 数字（与首页同一组，D3）+ 4 核心优势 -->
    <section class="bg-neutral-50 py-12 md:py-16">
      <div class="container">
        <SectionTitle eyebrow="Why Us" title="核心优势" />
        <dl
          v-if="stats.some(s => s.value)"
          class="mb-6 grid grid-cols-2 gap-px overflow-hidden rounded-lg border border-neutral-200 bg-neutral-200 sm:max-w-md"
        >
          <div v-for="stat in stats" :key="stat.label" class="bg-white p-5">
            <dt class="text-sm text-neutral-500">{{ stat.label }}</dt>
            <dd class="mt-1 text-3xl font-bold text-primary-700"><CountUp :value="stat.value" /></dd>
          </div>
        </dl>
        <ul class="grid gap-4 md:grid-cols-3 md:gap-6">
          <li
            v-for="(item, i) in advantages"
            :key="item.title"
            v-reveal="i"
            class="rounded-lg border border-neutral-200 bg-white p-5"
          >
            <i class="fas text-2xl text-primary-600" :class="item.icon"></i>
            <h3 class="mt-3 text-base font-semibold text-neutral-900">{{ item.title }}</h3>
            <p class="mt-1 text-sm text-neutral-600 leading-relaxed">{{ item.desc }}</p>
          </li>
        </ul>
      </div>
    </section>

    <!-- 6 展团风采：最新 9 个案例封面，点开放大（D11） -->
    <section v-if="photos.length" class="container py-12 md:py-16">
      <SectionTitle eyebrow="Our Delegations" title="展团风采">
        <template #action>
          <NuxtLink to="/cases" class="text-sm text-primary-600 hover:text-primary-700">
            查看成功案例<i class="fas fa-arrow-right ml-1 text-xs"></i>
          </NuxtLink>
        </template>
      </SectionTitle>
      <div class="grid grid-cols-2 gap-2 sm:gap-4 md:grid-cols-3">
        <el-image
          v-for="(photo, i) in photos"
          :key="photo.id"
          v-reveal="i"
          :src="fileUrl(photo.coverUrl)"
          :alt="photo.title"
          :preview-src-list="previewList"
          :initial-index="i"
          fit="cover"
          loading="lazy"
          preview-teleported
          class="aspect-[4/3] w-full rounded-lg bg-neutral-100"
        />
      </div>
    </section>

    <!-- 7 顾问团队（#5）：0 个时隐藏 -->
    <section v-if="consultants.length" class="bg-neutral-50 py-12 md:py-16">
      <div class="container">
        <SectionTitle eyebrow="Consultants" title="顾问团队" desc="按负责区域对接，电话可直接拨打" />
        <div class="grid gap-4 sm:grid-cols-2 lg:grid-cols-3 md:gap-6">
          <ConsultantCard v-for="(c, i) in consultants" :key="c.id" v-reveal="i" :consultant="c" />
        </div>
      </div>
    </section>

    <div class="container py-12 md:py-16">
      <ConsultBar title="想了解适合您的海外展会？" desc="告诉我们行业和目标市场，顾问为您推荐展会和参展方案" />
    </div>
  </div>
</template>

<script setup lang="ts">
import PageBanner from '~/components/common/PageBanner.vue'
import SectionTitle from '~/components/common/SectionTitle.vue'
import ConsultantCard from '~/components/common/ConsultantCard.vue'
import ConsultBar from '~/components/common/ConsultBar.vue'
import CountUp from '~/components/common/CountUp.vue'

const api = useApi()

const [{ data: info }, { data }] = await Promise.all([
  useContactInfo(),
  useAsyncData('about', async () => {
    const [cases, countries, consultants] = await Promise.all([
      api.cases.getList({ pageNum: 1, pageSize: 9 }),
      api.exhibition.getCountries(),
      api.contact.getConsultants()
    ])
    return { photos: cases.rows, countries, consultants }
  })
])

const profile = computed(() =>
  (info.value?.companyProfile ?? '').split('\n').map(s => s.trim()).filter(Boolean)
)
const photos = computed(() => (data.value?.photos ?? []).filter(p => p.coverUrl))
const previewList = computed(() => photos.value.map(p => fileUrl(p.coverUrl)))
const stats = computed(() => exhibitionStats(data.value?.countries ?? []))
const consultants = computed(() => data.value?.consultants ?? [])

const advantages = [
  {
    icon: 'fa-globe',
    title: '全球资源网络',
    desc: '与全球知名展会主办方建立长期合作关系，包括英富曼、励展、Terrapinn 等'
  },
  {
    icon: 'fa-handshake',
    title: '专业服务团队',
    desc: '团队成员均具有十几年海外展会操作经验，提供专业的展会咨询和支持'
  },
  {
    icon: 'fa-cogs',
    title: '一站式解决方案',
    desc: '提供展位设计、签证办理、机票预订等全方位定制化服务'
  }
]

useSeoMeta({
  title: '关于我们',
  description: () =>
    profile.value[0] ?? '雅博展览专注境外展会组展，提供展位预订、展台搭建、签证行程、物流与补贴申请等一站式出展服务'
})
</script>
