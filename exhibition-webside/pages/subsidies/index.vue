<template>
  <div>
    <!-- 补贴比例措辞未定（D14），不写具体比例 -->
    <PageBanner
      title="补贴政策"
      en="Subsidies"
      desc="出国参展可申请政府补贴，顾问免费帮您评估可申报的补贴"
      :crumbs="[{ name: '补贴政策' }]"
    />

    <div class="container py-8 md:py-12">
      <FilterBar v-model:keyword="keyword" placeholder="搜索政策标题，如地区名" @search="search" @clear="clear" />

      <div ref="listRef" class="mt-6 scroll-mt-24">
        <el-skeleton v-if="status === 'pending'" :rows="6" animated class="rounded-lg border border-neutral-200 p-4" />
        <div v-else-if="rows.length" class="space-y-3">
          <ListRow
            v-for="s in rows"
            :key="s.id"
            :to="`/subsidies/${s.id}`"
            :title="s.title"
            :muted="subsidyStatus(s).closed"
          >
            <template #badge>
              <span class="tag h-5 mr-1.5 align-[2px]" :class="subsidyStatus(s).cls">{{ subsidyStatus(s).label }}</span>
            </template>
            <p>
              有效期 {{ s.startTime?.slice(0, 10) }} 至 {{ s.endTime?.slice(0, 10) }}
              <template v-if="s.price > 0">
                <span class="mx-1.5 text-neutral-300">|</span>最高补贴
                <span class="font-medium text-primary-700">{{ formatSubsidyPrice(s.price) }}</span>
              </template>
            </p>
          </ListRow>
        </div>
        <EmptyState v-else-if="applied" title="没有找到相关政策" desc="换个关键词试试，或直接问顾问" icon="fa-search">
          <button type="button" class="btn btn-secondary" @click="clear">清空搜索</button>
          <NuxtLink to="/contact" class="btn btn-primary">联系顾问</NuxtLink>
        </EmptyState>
        <EmptyState v-else title="政策整理中" desc="可以直接电话咨询您所在地区的补贴政策" icon="fa-file-alt">
          <a v-if="contact.hotline" :href="telHref(contact.hotline)" class="btn btn-primary">
            <i class="fas fa-phone-alt"></i>{{ contact.hotline }}
          </a>
        </EmptyState>
      </div>

      <AppPagination v-model="page" :total="total" :page-size="pageSize" />

      <ConsultBar class="mt-12" title="不确定能不能申报？" desc="告诉顾问您的所在地和参展计划，免费帮您评估" />
    </div>
  </div>
</template>

<script setup lang="ts">
import PageBanner from '~/components/common/PageBanner.vue'
import FilterBar from '~/components/common/FilterBar.vue'
import ListRow from '~/components/common/ListRow.vue'
import EmptyState from '~/components/common/EmptyState.vue'
import AppPagination from '~/components/common/AppPagination.vue'
import ConsultBar from '~/components/common/ConsultBar.vue'

const api = useApi()
const contact = useSiteContact()

const pageSize = 12
const page = ref(1)
const keyword = ref('')
const applied = ref('')

const { data, status } = await useAsyncData(
  'subsidies-list',
  () => api.subsidies.getList({ pageNum: page.value, pageSize, title: applied.value || undefined }),
  { watch: [page, applied] }
)
// 已截止的排在本页后面
const rows = computed(() =>
  [...(data.value?.rows ?? [])].sort((a, b) => Number(subsidyStatus(a).closed) - Number(subsidyStatus(b).closed))
)
const total = computed(() => data.value?.total ?? 0)

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
  title: '补贴政策',
  description: '出国参展补贴政策汇总：申报时间、补贴额度与申报材料，顾问免费帮您评估'
})
</script>
