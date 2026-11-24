<template>
  <!-- 顶部联系条：只在桌面显示，滚动后随页面上移 -->
  <div class="hidden md:block bg-neutral-900 text-neutral-300 text-xs">
    <div class="container flex items-center justify-between h-9">
      <span>专注境外展会组展与参展服务</span>
      <div class="flex items-center gap-6">
        <a
          v-if="contact.hotline"
          :href="`tel:${contact.hotline.split('/')[0]}`"
          class="hover:text-white"
        >
          <i class="fas fa-phone-alt mr-1.5 text-primary-400"></i>服务热线 {{ contact.hotline }}
        </a>
        <a v-if="contact.mobile" :href="`tel:${contact.mobile}`" class="hover:text-white">
          <i class="fab fa-weixin mr-1.5 text-primary-400"></i>手机 / 微信 {{ contact.mobile }}
        </a>
      </div>
    </div>
  </div>

  <nav class="sticky top-0 z-50 bg-white border-b border-neutral-200">
    <div class="container flex items-center justify-between h-16 md:h-20">
      <NuxtLink to="/" class="flex items-center shrink-0" :aria-label="site.name">
        <img :src="site.logo" :alt="site.name" class="h-9 md:h-11 w-auto" />
      </NuxtLink>

      <div class="hidden md:flex items-center h-full">
        <NuxtLink
          v-for="item in navItems"
          :key="item.path"
          :to="item.path"
          class="nav-link"
          :class="{ 'nav-link-active': isActive(item.path) }"
        >
          <span class="text-[15px] font-medium">{{ item.name }}</span>
          <span class="text-[10px] tracking-wider text-neutral-400 uppercase">{{ item.en }}</span>
        </NuxtLink>
        <NuxtLink
          to="/contact"
          class="ml-6 inline-flex items-center h-10 px-5 rounded-md bg-primary-600 text-white text-sm font-medium hover:bg-primary-700 transition-colors"
        >
          联系我们
        </NuxtLink>
      </div>

      <button
        class="md:hidden w-10 h-10 -mr-2 flex items-center justify-center text-neutral-700"
        :aria-label="isMenuOpen ? '关闭菜单' : '打开菜单'"
        @click="isMenuOpen = !isMenuOpen"
      >
        <i class="fas text-lg" :class="isMenuOpen ? 'fa-times' : 'fa-bars'"></i>
      </button>
    </div>

    <!-- 手机端菜单 -->
    <div v-show="isMenuOpen" class="md:hidden border-t border-neutral-200 bg-white">
      <div class="container py-2">
        <NuxtLink
          v-for="item in navItems"
          :key="item.path"
          :to="item.path"
          class="flex items-center justify-between py-3 border-b border-neutral-100 text-neutral-800"
          :class="{ 'text-primary-600 font-semibold': isActive(item.path) }"
        >
          {{ item.name }}
          <i class="fas fa-chevron-right text-xs text-neutral-300"></i>
        </NuxtLink>
        <div class="grid grid-cols-2 gap-3 py-4">
          <a
            v-if="contact.mobile"
            :href="`tel:${contact.mobile}`"
            class="h-11 inline-flex items-center justify-center rounded-md border border-primary-600 text-primary-600 text-sm"
          >
            <i class="fas fa-phone-alt mr-2"></i>电话咨询
          </a>
          <NuxtLink
            to="/contact"
            class="h-11 inline-flex items-center justify-center rounded-md bg-primary-600 text-white text-sm"
          >
            联系我们
          </NuxtLink>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { site } from '~/constants/site'

const route = useRoute()
const contact = useSiteContact()
const isMenuOpen = ref(false)

const navItems = [
  { name: '首页', en: 'Home', path: '/' },
  { name: '展会列表', en: 'Exhibitions', path: '/exhibitions' },
  { name: '市场简讯', en: 'News', path: '/news' },
  { name: '成功案例', en: 'Cases', path: '/cases' },
  { name: '关于我们', en: 'About', path: '/about' }
]

const isActive = (path: string) => (path === '/' ? route.path === '/' : route.path.startsWith(path))

watch(
  () => route.path,
  () => {
    isMenuOpen.value = false
  }
)
</script>

<style scoped>
.nav-link {
  @apply relative flex flex-col items-center justify-center h-full px-5 text-neutral-700 hover:text-primary-600 transition-colors;
}

.nav-link-active {
  @apply text-primary-600;
}

.nav-link-active::after {
  content: '';
  @apply absolute left-5 right-5 bottom-0 h-0.5 bg-primary-600;
}
</style>
