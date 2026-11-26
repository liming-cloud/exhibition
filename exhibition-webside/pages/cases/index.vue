<template>
  <div>
    <PageBanner
      title="成功案例"
      en="Cases"
      desc="我们带团参加过的海外展会，用现场照片说话"
      :crumbs="[{ name: '成功案例' }]"
    />

    <div ref="listRef" class="container py-8 md:py-12 scroll-mt-24">
      <el-skeleton v-if="status === 'pending'" :rows="6" animated class="rounded-lg border border-neutral-200 p-4" />
      <!-- 每页 6 个（R4）：桌面 3×2、平板 2 列、手机 1 列；不足 6 个不补空卡 -->
      <div v-else-if="rows.length" class="grid gap-4 md:gap-6 sm:grid-cols-2 lg:grid-cols-3">
        <ContentCard
          v-for="(c, i) in rows"
          :key="c.id"
          v-reveal="i"
          :to="`/cases/${c.id}`"
          :title="c.title"
          :cover="c.coverUrl ? fileUrl(c.coverUrl) : ''"
        >
          {{ formatDate(c.createTime) }}
        </ContentCard>
      </div>
      <EmptyState v-else title="暂无成功案例" desc="案例整理中，可以先看看我们正在组织的展会" icon="fa-images">
        <NuxtLink to="/exhibitions" class="btn btn-primary">查看展会</NuxtLink>
      </EmptyState>

      <AppPagination v-model="page" :total="total" :page-size="pageSize" />

      <ConsultBar class="mt-12" title="想参加同类展会？" desc="顾问为您介绍展会情况和参展方案" />
    </div>
  </div>
</template>

<script setup lang="ts">
import PageBanner from '~/components/common/PageBanner.vue'
import ContentCard from '~/components/common/ContentCard.vue'
import EmptyState from '~/components/common/EmptyState.vue'
import AppPagination from '~/components/common/AppPagination.vue'
import ConsultBar from '~/components/common/ConsultBar.vue'

const api = useApi()

const pageSize = 6
const page = ref(1)

const { data, status } = await useAsyncData(
  'cases-list',
  () => api.cases.getList({ pageNum: page.value, pageSize }),
  { watch: [page] }
)
const rows = computed(() => data.value?.rows ?? [])
const total = computed(() => data.value?.total ?? 0)

const listRef = ref<HTMLElement>()
watch(page, () => listRef.value?.scrollIntoView({ behavior: 'smooth' }))

useSeoMeta({
  title: '成功案例',
  description: '雅博展览带团参加海外展会的成功案例与现场照片'
})
</script>
