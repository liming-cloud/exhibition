<template>
  <footer class="bg-neutral-900 text-neutral-400 text-sm">
    <div class="container py-12 grid gap-10 md:grid-cols-12">
      <div class="md:col-span-4">
        <h3 class="footer-title">关于我们</h3>
        <p class="leading-relaxed line-clamp-4">{{ firstParagraph }}</p>
      </div>

      <div class="md:col-span-2">
        <h3 class="footer-title">快速链接</h3>
        <ul class="grid grid-cols-2 md:grid-cols-1 gap-2">
          <li v-for="link in quickLinks" :key="link.path">
            <NuxtLink :to="link.path" class="hover:text-white transition-colors">{{
              link.name
            }}</NuxtLink>
          </li>
        </ul>
      </div>

      <div class="md:col-span-4">
        <h3 class="footer-title">联系我们</h3>
        <ul class="space-y-3">
          <li v-if="contact.hotline" class="flex gap-3">
            <i class="fas fa-phone-alt w-4 mt-1 text-primary-400"></i>
            <span>服务热线：{{ contact.hotline }}</span>
          </li>
          <li v-if="contact.mobile" class="flex gap-3">
            <i class="fas fa-mobile-alt w-4 mt-1 text-primary-400"></i>
            <span>手机 / 微信同号：{{ contact.mobile }}</span>
          </li>
          <li v-if="contact.email" class="flex gap-3">
            <i class="fas fa-envelope w-4 mt-1 text-primary-400"></i>
            <span>{{ contact.email }}</span>
          </li>
          <li v-if="contact.address" class="flex gap-3">
            <i class="fas fa-map-marker-alt w-4 mt-1 text-primary-400"></i>
            <span>{{ contact.address }}</span>
          </li>
        </ul>
      </div>

      <div class="md:col-span-2">
        <h3 class="footer-title">关注我们</h3>
        <img
          :src="site.qrcode"
          alt="微信二维码"
          class="w-32 h-32 rounded bg-white p-1.5"
          loading="lazy"
        />
        <p class="mt-2 text-xs">扫码添加微信咨询</p>
      </div>
    </div>

    <div class="border-t border-neutral-800">
      <div class="container py-5 text-xs text-neutral-500 text-center">
        &copy; {{ new Date().getFullYear() }} {{ site.company }} 版权所有
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
import { site } from '~/constants/site'

const contact = useSiteContact()
const firstParagraph = computed(() => contact.value.profile.split('\n')[0])

const quickLinks = [
  { name: '展会列表', path: '/exhibitions' },
  { name: '市场简讯', path: '/news' },
  { name: '成功案例', path: '/cases' },
  { name: '补贴政策', path: '/subsidies' },
  { name: '关于我们', path: '/about' },
  { name: '联系我们', path: '/contact' }
]
</script>

<style scoped>
.footer-title {
  @apply mb-4 text-base font-semibold text-white;
}
</style>
