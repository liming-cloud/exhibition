<template>
  <!-- 设计规范与通用组件样例页：只在 mock 模式存在（nuxt.config.ts 的 pages:extend），正式构建不包含 -->
  <div>
    <PageBanner
      title="设计规范与通用组件"
      en="Design System"
      desc="规范说明见 docs/prototype/设计规范.md"
      :crumbs="[{ name: '设计规范' }]"
      :image="fileUrl('mock/bg-hall.jpg')"
    />

    <div class="container py-12 md:py-16 space-y-16">
      <!-- 1 配色 -->
      <section>
        <SectionTitle
          eyebrow="Colors"
          title="配色"
          desc="主色用于按钮、链接、选中态；强调色只用于「主推」等少量醒目标记"
        />
        <div class="space-y-4">
          <div v-for="palette in palettes" :key="palette.name">
            <p class="mb-2 text-sm font-medium text-neutral-700">{{ palette.name }}</p>
            <div class="grid grid-cols-5 md:grid-cols-10 gap-2">
              <div v-for="shade in shades" :key="shade" class="text-center">
                <div class="h-10 rounded-md border border-black/5" :class="palette.bg[shade]"></div>
                <p class="mt-1 text-[11px] text-neutral-500">{{ shade }}</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 2 字号 -->
      <section>
        <SectionTitle
          eyebrow="Typography"
          title="字号层级"
          desc="系统字体（苹方 / 微软雅黑），不加载外部字体"
        />
        <div class="divide-y divide-neutral-100 rounded-lg border border-neutral-200">
          <div
            v-for="t in typography"
            :key="t.name"
            class="flex flex-col md:flex-row md:items-baseline gap-1 md:gap-6 px-4 py-3"
          >
            <span class="w-40 shrink-0 text-xs text-neutral-400">{{ t.name }}</span>
            <span :class="t.cls">{{ t.sample }}</span>
          </div>
        </div>
      </section>

      <!-- 3 按钮与标签 -->
      <section>
        <SectionTitle eyebrow="Buttons & Tags" title="按钮与标签" />
        <div class="flex flex-wrap gap-3">
          <button class="btn btn-primary">立即报名</button>
          <button class="btn btn-secondary"><i class="fas fa-phone-alt"></i>电话咨询</button>
          <button class="btn btn-accent">主推展会</button>
          <button class="btn btn-ghost">查看全部<i class="fas fa-arrow-right text-xs"></i></button>
          <button class="btn btn-primary" disabled>提交中…</button>
          <button class="btn btn-primary btn-lg">大按钮 48px</button>
        </div>
        <div class="mt-6 flex flex-wrap gap-2">
          <span class="tag tag-accent">主推</span>
          <span class="tag tag-primary">能源展</span>
          <span class="tag tag-success">申报中</span>
          <span class="tag">未开始</span>
          <span class="tag tag-muted">已截止</span>
        </div>
        <div class="mt-6 flex flex-wrap gap-2">
          <button class="chip chip-active">全部国家</button>
          <button class="chip">德国 <span class="text-xs opacity-60">3</span></button>
          <button class="chip">阿联酋 <span class="text-xs opacity-60">2</span></button>
        </div>
      </section>

      <!-- 4 筛选栏 + 列表行 + 分页 + 空状态 -->
      <section>
        <SectionTitle eyebrow="List" title="筛选栏、列表行、分页">
          <template #action>
            <NuxtLink to="/exhibitions" class="text-sm text-primary-600 hover:text-primary-700">
              查看全部<i class="fas fa-arrow-right ml-1 text-xs"></i>
            </NuxtLink>
          </template>
        </SectionTitle>

        <FilterBar
          v-model:keyword="keyword"
          placeholder="搜索展会名称"
          :tags="filterTags"
          @remove="removeFilter"
          @clear="clearFilters"
        >
          <el-select v-model="filters.type" placeholder="行业" clearable class="!w-full md:!w-32">
            <el-option
              v-for="(label, value) in ExhibitionTypeMap"
              :key="value"
              :label="label"
              :value="value"
            />
          </el-select>
          <el-select v-model="filters.year" placeholder="年份" clearable class="!w-full md:!w-28">
            <el-option v-for="y in ['2026', '2027']" :key="y" :label="`${y} 年`" :value="y" />
          </el-select>
        </FilterBar>

        <div class="mt-6 space-y-3">
          <ListRow
            v-for="e in sampleExhibitions"
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
              <i class="fas fa-map-marker-alt mr-1 text-neutral-400"></i>{{ e.countryName }} ·
              {{ e.location }}
              <span class="mx-1.5 text-neutral-300">|</span
              >{{ ExhibitionTypeMap[e.exhibitionType] }}
            </p>
            <template #actions>
              <NuxtLink :to="`/exhibitions/${e.id}`" class="btn btn-ghost h-9 px-3">详情</NuxtLink>
              <button v-if="isUpcoming(e)" class="btn btn-primary h-9">报名</button>
              <button v-else class="btn btn-secondary h-9">咨询下一届</button>
            </template>
          </ListRow>

          <!-- 简讯行：缩略图 + 两行摘要；无封面时不留位 -->
          <ListRow v-for="n in sampleNews" :key="`n${n.id}`" :to="`/news/${n.id}`" :title="n.title">
            <template #leading>
              <img
                v-if="n.coverUrl"
                :src="fileUrl(n.coverUrl)"
                alt=""
                class="w-24 h-[72px] md:w-32 md:h-24 shrink-0 rounded-md object-cover bg-neutral-100"
                loading="lazy"
              />
            </template>
            <p class="line-clamp-2">{{ toSummary(n.content, 120) }}</p>
            <p class="text-xs text-neutral-400">{{ formatDate(n.createTime) }}</p>
          </ListRow>

          <!-- 政策行：状态标签 + 有效期 + 金额 -->
          <ListRow
            v-for="(s, i) in sampleSubsidies"
            :key="`s${s.id}`"
            :to="`/subsidies/${s.id}`"
            :title="s.title"
            :muted="i === 1"
          >
            <template #badge>
              <span
                class="tag h-5 mr-1.5 align-[2px]"
                :class="i === 1 ? 'tag-muted' : 'tag-success'"
                >{{ i === 1 ? '已截止' : '申报中' }}</span
              >
            </template>
            <p>有效期 {{ s.startTime?.slice(0, 10) }} 至 {{ s.endTime?.slice(0, 10) }}</p>
          </ListRow>
        </div>

        <AppPagination v-model="page" :total="57" :page-size="10" />

        <div class="mt-8">
          <EmptyState
            title="没有符合条件的展会"
            desc="换个条件试试，或让顾问帮您找"
            icon="fa-search"
          >
            <button class="btn btn-secondary" @click="clearFilters">清空筛选</button>
            <NuxtLink to="/contact" class="btn btn-primary">联系顾问</NuxtLink>
          </EmptyState>
        </div>

        <div class="mt-8">
          <p class="mb-2 text-sm text-neutral-500">翻页加载中：列表区显示骨架屏（el-skeleton）</p>
          <el-skeleton :rows="3" animated class="rounded-lg border border-neutral-200 p-4" />
        </div>
      </section>

      <!-- 5 图文卡片 -->
      <section>
        <SectionTitle
          eyebrow="Cards"
          title="图文卡片"
          desc="封面 4:3 裁切；桌面 3 列、平板 2 列、手机 1 列；最后一张演示图片缺失"
        />
        <div class="grid gap-4 md:gap-6 sm:grid-cols-2 lg:grid-cols-3">
          <ContentCard
            v-for="(c, i) in sampleCases"
            :key="c.id"
            v-reveal="i"
            :to="`/cases/${c.id}`"
            :title="c.title"
            :cover="fileUrl(c.coverUrl)"
          >
            {{ formatDate(c.createTime) }}
          </ContentCard>
          <ContentCard
            to="/cases"
            title="封面图缺失时显示占位底色，标题照常显示（最多两行，超出省略）"
          >
            2026/01/01
          </ContentCard>
        </div>
      </section>

      <!-- 6 顾问卡片 -->
      <section>
        <SectionTitle
          eyebrow="Consultants"
          title="顾问卡片"
          desc="展会详情取前 2 位（D6），关于我们、联系我们显示全部"
        />
        <div class="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
          <ConsultantCard v-for="(c, i) in consultants" :key="c.id" v-reveal="i" :consultant="c" />
        </div>
      </section>

      <!-- 7 表单 -->
      <section>
        <SectionTitle
          eyebrow="Form"
          title="表单"
          desc="标签在上；必填项标星；提交中按钮禁用防重复；成功后给出下一步说明"
        />
        <div class="max-w-lg rounded-lg border border-neutral-200 p-5 md:p-6">
          <el-form ref="formRef" :model="form" :rules="rules" label-position="top" size="large">
            <el-form-item label="公司/个人名称" prop="companyOrPersonalName">
              <el-input v-model="form.companyOrPersonalName" placeholder="请输入公司或个人名称" />
            </el-form-item>
            <el-form-item label="联系人" prop="contactName">
              <el-input v-model="form.contactName" placeholder="请输入联系人姓名" />
            </el-form-item>
            <el-form-item label="手机号" prop="contactPhone">
              <el-input
                v-model="form.contactPhone"
                placeholder="11 位手机号"
                maxlength="11"
                inputmode="tel"
              />
            </el-form-item>
          </el-form>
          <button class="btn btn-primary btn-lg w-full" :disabled="submitting" @click="submit">
            <i v-if="submitting" class="fas fa-spinner fa-spin"></i
            >{{ submitting ? '提交中…' : '提交报名' }}
          </button>
          <p
            v-if="submitted"
            class="mt-4 rounded-md bg-emerald-50 px-4 py-3 text-sm text-emerald-700"
          >
            <i class="fas fa-check-circle mr-1"></i>提交成功，顾问将在 1 个工作日内联系您
          </p>
        </div>
      </section>

      <!-- 8 咨询转化条 -->
      <section>
        <SectionTitle
          eyebrow="Call to Action"
          title="咨询转化条"
          desc="每页页脚前放一条，文案按页面场景写"
        />
        <ConsultBar title="不知道选哪个展？" desc="告诉我们您的行业和目标市场，顾问免费为您推荐" />
        <ConsultBar
          class="mt-6"
          title="想参加同类展会？"
          desc="带大图背景的版本，每页最多用一处"
          :image="fileUrl('mock/bg-port.jpg')"
        />
      </section>

      <!-- 9 动效与背景图 -->
      <section>
        <SectionTitle
          eyebrow="Motion"
          title="动效与背景图"
          desc="规则见设计规范第 10 节；首页轮播的切换效果在首页查看"
        >
          <template #action>
            <NuxtLink to="/" class="text-sm text-primary-600 hover:text-primary-700">
              查看首页轮播<i class="fas fa-arrow-right ml-1 text-xs"></i>
            </NuxtLink>
          </template>
        </SectionTitle>
        <dl
          class="grid grid-cols-2 md:grid-cols-4 gap-px overflow-hidden rounded-lg border border-neutral-200 bg-neutral-200"
        >
          <div v-for="(stat, i) in stats" :key="stat.label" v-reveal="i" class="bg-white p-5">
            <dt class="text-sm text-neutral-500">{{ stat.label }}</dt>
            <dd class="mt-1 text-3xl font-bold text-primary-700">
              <CountUp :value="stat.value" :suffix="stat.suffix" />
            </dd>
          </div>
        </dl>
        <ul class="mt-6 grid gap-3 md:grid-cols-3 text-sm">
          <li
            v-for="(rule, i) in motionRules"
            :key="rule.name"
            v-reveal="i"
            class="rounded-lg border border-neutral-200 p-4"
          >
            <p class="font-medium text-neutral-900">{{ rule.name }}</p>
            <p class="mt-1 text-neutral-500">{{ rule.desc }}</p>
          </li>
        </ul>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { FormInstance } from 'element-plus'
import PageBanner from '~/components/common/PageBanner.vue'
import SectionTitle from '~/components/common/SectionTitle.vue'
import FilterBar from '~/components/common/FilterBar.vue'
import ListRow from '~/components/common/ListRow.vue'
import DateBlock from '~/components/common/DateBlock.vue'
import AppPagination from '~/components/common/AppPagination.vue'
import EmptyState from '~/components/common/EmptyState.vue'
import ContentCard from '~/components/common/ContentCard.vue'
import ConsultantCard from '~/components/common/ConsultantCard.vue'
import ConsultBar from '~/components/common/ConsultBar.vue'
import CountUp from '~/components/common/CountUp.vue'
import { ExhibitionTypeMap } from '~/types/exhibition'
import { exhibitions, informations, cases, subsidies, consultants } from '~/mock/data'

useSeoMeta({ title: '设计规范', robots: 'noindex' })

const shades = [50, 100, 200, 300, 400, 500, 600, 700, 800, 900] as const
// 类名写全，Tailwind 才能扫描到
const palettes = [
  {
    name: '主色 primary（按钮、链接、选中）',
    bg: {
      50: 'bg-primary-50',
      100: 'bg-primary-100',
      200: 'bg-primary-200',
      300: 'bg-primary-300',
      400: 'bg-primary-400',
      500: 'bg-primary-500',
      600: 'bg-primary-600',
      700: 'bg-primary-700',
      800: 'bg-primary-800',
      900: 'bg-primary-900'
    }
  },
  {
    name: '强调色 accent（主推标记）',
    bg: {
      50: 'bg-accent-50',
      100: 'bg-accent-100',
      200: 'bg-accent-200',
      300: 'bg-accent-300',
      400: 'bg-accent-400',
      500: 'bg-accent-500',
      600: 'bg-accent-600',
      700: 'bg-accent-700',
      800: 'bg-accent-800',
      900: 'bg-accent-900'
    }
  },
  {
    name: '中性色 neutral（文字、边框、底色）',
    bg: {
      50: 'bg-neutral-50',
      100: 'bg-neutral-100',
      200: 'bg-neutral-200',
      300: 'bg-neutral-300',
      400: 'bg-neutral-400',
      500: 'bg-neutral-500',
      600: 'bg-neutral-600',
      700: 'bg-neutral-700',
      800: 'bg-neutral-800',
      900: 'bg-neutral-900'
    }
  }
]

const typography = [
  {
    name: '页面标题 24 / 36',
    cls: 'text-2xl md:text-4xl font-bold text-neutral-900',
    sample: '展会列表'
  },
  {
    name: '区块标题 24 / 30',
    cls: 'text-2xl md:text-3xl font-bold text-neutral-900',
    sample: '展会项目'
  },
  {
    name: '卡片标题 16 中粗',
    cls: 'text-base font-medium text-neutral-900',
    sample: '2026 德国汉诺威工业博览会'
  },
  {
    name: '正文 16',
    cls: 'text-base text-neutral-600',
    sample: '为参展企业提供定制化一站式出展服务'
  },
  {
    name: '辅助信息 14',
    cls: 'text-sm text-neutral-500',
    sample: '德国 · 汉诺威展览中心 | 能源展'
  },
  { name: '英文小字 12 大写', cls: 'section-eyebrow', sample: 'Our Projects' }
]

// 列表行样例：取一条即将开始的（优先主推）、一条已结束，再加一条超长中英文混排标题
const upcoming = exhibitions.filter(e => isUpcoming(e))
const featured = upcoming.find(e => e.featured) ?? upcoming[0] ?? exhibitions[0]
const ended = exhibitions.find(e => !isUpcoming(e)) ?? exhibitions[1]
const longName = {
  ...featured,
  id: 999,
  featured: false,
  name: '2026 年第 45 届中东（迪拜）国际电力、照明及新能源展览会 Middle East Energy Dubai 2026 International Exhibition'
}
const sampleExhibitions = [featured, longName, ended]
const sampleNews = informations.slice(0, 2)
const sampleSubsidies = subsidies.slice(0, 2)
const sampleCases = cases.slice(0, 5)

const keyword = ref('')
const filters = reactive({
  type: 'ENERGY' as string | undefined,
  year: '2026' as string | undefined
})
const filterTags = computed(() => [
  ...(filters.type
    ? [{ key: 'type', label: ExhibitionTypeMap[filters.type as keyof typeof ExhibitionTypeMap] }]
    : []),
  ...(filters.year ? [{ key: 'year', label: `${filters.year} 年` }] : [])
])
const removeFilter = (key: string) => {
  filters[key as 'type' | 'year'] = undefined
}
const clearFilters = () => {
  keyword.value = ''
  filters.type = undefined
  filters.year = undefined
}

const page = ref(1)

// 信任数字样例（D3：真实数字由人提供前只放能算出的）
const stats = [
  { label: '覆盖国家', value: new Set(exhibitions.map(e => e.country)).size, suffix: '' },
  { label: '在售展会', value: exhibitions.length, suffix: '' },
  { label: '服务企业（示例）', value: 500, suffix: '+' },
  { label: '成立年份（示例）', value: 2012, suffix: '' }
]

const motionRules = [
  { name: '入场', desc: '滚动到视口时淡入上浮 16px，400ms，只播一次；首屏内容不做入场' },
  { name: '切换', desc: '轮播图片淡入 800ms + 7 秒缓慢缩放，文字依次入场；带暂停按钮' },
  { name: '悬停', desc: '卡片图片放大 1.05、边框变主色，300ms；不旋转、不弹跳' }
]

const formRef = ref<FormInstance>()
const form = reactive({ companyOrPersonalName: '', contactName: '', contactPhone: '' })
const rules = {
  companyOrPersonalName: [requiredRule('公司或个人名称')],
  contactName: [requiredRule('联系人姓名')],
  contactPhone: [requiredRule('手机号'), mobileRule]
}
const submitting = ref(false)
const submitted = ref(false)
const submit = async () => {
  if (!(await formRef.value?.validate().catch(() => false))) return
  submitting.value = true
  setTimeout(() => {
    submitting.value = false
    submitted.value = true
  }, 800)
}
</script>
