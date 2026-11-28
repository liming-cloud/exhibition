<template>
  <div>
    <PageBanner
      title="联系我们"
      en="Contact"
      desc="电话、微信都能直接找到顾问，免费获取参展方案"
      :crumbs="[{ name: '联系我们' }]"
    />

    <div class="container py-12 md:py-16 space-y-12 md:space-y-16">
      <!-- 2 主联系方式：电话全部列出、可拨；微信二维码；邮箱 -->
      <section class="grid gap-4 md:gap-6 lg:grid-cols-3">
        <div class="rounded-lg border border-neutral-200 bg-white p-5 md:p-6 lg:col-span-2">
          <p class="section-eyebrow">Hotline</p>
          <h2 class="text-lg font-semibold text-neutral-900">咨询电话</h2>
          <ul v-if="phones.length" class="mt-4 space-y-3">
            <li v-for="phone in phones" :key="phone">
              <a
                :href="telHref(phone)"
                class="inline-flex items-center gap-3 text-2xl md:text-3xl font-bold text-primary-700 hover:text-primary-800 break-all"
              >
                <i class="fas fa-phone-alt text-xl text-primary-500"></i>{{ phone }}
              </a>
            </li>
          </ul>
          <p v-else class="mt-4 text-neutral-500">联系方式更新中，请通过微信联系我们</p>
          <p v-if="info?.email" class="mt-6 text-sm text-neutral-600">
            <i class="fas fa-envelope mr-2 text-primary-500"></i>业务邮箱：
            <a :href="`mailto:${info.email}`" class="text-primary-700 hover:underline break-all">{{ info.email }}</a>
          </p>
        </div>
        <div class="flex items-center gap-5 rounded-lg border border-neutral-200 bg-white p-5 md:p-6">
          <img :src="assetUrl(site.qrcode)" alt="微信二维码" class="h-28 w-28 shrink-0 rounded-md border border-neutral-200" />
          <div class="min-w-0">
            <h2 class="text-lg font-semibold text-neutral-900">加微信咨询</h2>
            <p class="mt-1 text-sm text-neutral-600">扫码添加顾问微信</p>
            <p v-if="phones[1]" class="mt-1 text-sm text-neutral-500">手机 {{ phones[1] }} 微信同号</p>
          </div>
        </div>
      </section>

      <!-- 3 顾问卡片（#5）：0 个时隐藏 -->
      <section v-if="consultants.length">
        <SectionTitle eyebrow="Consultants" title="展会顾问" desc="按负责区域联系对应顾问，回复更快" />
        <div class="grid gap-4 sm:grid-cols-2 lg:grid-cols-3 md:gap-6">
          <ConsultantCard v-for="c in consultants" :key="c.id" :consultant="c" />
        </div>
      </section>

      <!-- 4 地址与工作时间：不嵌第三方地图 SDK，给「在地图中打开」外链 -->
      <section v-if="info?.address || info?.workingHours" class="grid gap-4 md:gap-6 md:grid-cols-2">
        <div v-if="info?.address" class="rounded-lg border border-neutral-200 bg-white p-5 md:p-6">
          <h2 class="text-lg font-semibold text-neutral-900"><i class="fas fa-building mr-2 text-primary-500"></i>公司地址</h2>
          <p class="mt-2 text-neutral-600">{{ info.address }}</p>
          <a
            :href="mapHref"
            target="_blank"
            rel="noopener"
            class="btn btn-secondary mt-4"
          >
            <i class="fas fa-map-marker-alt"></i>在地图中打开
          </a>
        </div>
        <div v-if="info?.workingHours" class="rounded-lg border border-neutral-200 bg-white p-5 md:p-6">
          <h2 class="text-lg font-semibold text-neutral-900"><i class="fas fa-clock mr-2 text-primary-500"></i>工作时间</h2>
          <p class="mt-2 text-neutral-600">{{ info.workingHours }}</p>
          <p v-if="info.holidayArrangement" class="mt-1 text-sm text-neutral-500">{{ info.holidayArrangement }}</p>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import PageBanner from '~/components/common/PageBanner.vue'
import SectionTitle from '~/components/common/SectionTitle.vue'
import ConsultantCard from '~/components/common/ConsultantCard.vue'
import { site } from '~/constants/site'

const api = useApi()

const [{ data: info }, { data: consultantList }] = await Promise.all([
  useContactInfo(),
  useAsyncData('contact-consultants', () => api.contact.getConsultants())
])

const phones = computed(() => info.value?.phone?.filter(Boolean) ?? [])
const consultants = computed(() => consultantList.value ?? [])
const mapHref = computed(() => `https://uri.amap.com/search?keyword=${encodeURIComponent(info.value?.address ?? '')}`)

useSeoMeta({
  title: '联系我们',
  description: () =>
    `雅博展览联系方式：${[phones.value[0], info.value?.address].filter(Boolean).join('，')}。电话、微信直接联系展会顾问`
})
</script>
