<template>
  <footer class="bg-gradient-to-br from-neutral-900 to-neutral-800 text-white py-12">
    <div class="container mx-auto px-4">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
        <!-- 公司信息 -->
        <div class="space-y-3">
          <h3 class="text-lg font-bold mb-3 relative inline-block text-white">
            关于我们
            <span class="absolute bottom-0 left-0 w-1/2 h-0.5 bg-primary-500"></span>
          </h3>
          <p class="text-neutral-400 leading-relaxed text-sm">
            雅博国际展览（天津）有限公司专业从事境外展会的组展工作，为参展企业提供定制化一站式出展服务，助力企业开拓国际市场。
          </p>
        </div>

        <!-- 快速链接 -->
        <div class="space-y-3">
          <h3 class="text-lg font-bold mb-3 relative inline-block text-white">
            快速链接
            <span class="absolute bottom-0 left-0 w-1/2 h-0.5 bg-primary-500"></span>
          </h3>
          <ul class="space-y-2">
            <li v-for="link in quickLinks" :key="link.path">
              <NuxtLink
                :to="link.path"
                class="text-neutral-400 hover:text-white hover:translate-x-1 transition-all duration-300 flex items-center text-sm"
              >
                <i class="fas fa-chevron-right text-xs mr-2"></i>
                {{ link.name }}
              </NuxtLink>
            </li>
          </ul>
        </div>

        <!-- 联系方式 -->
        <div class="space-y-3">
          <h3 class="text-lg font-bold mb-3 relative inline-block text-white">
            联系我们
            <span class="absolute bottom-0 left-0 w-1/2 h-0.5 bg-primary-500"></span>
          </h3>
          <ul class="space-y-3 text-neutral-400">
            <li class="flex items-center group">
              <div class="w-7 h-7 bg-primary-500 bg-opacity-20 rounded-lg flex items-center justify-center group-hover:bg-primary-500 transition-colors duration-300">
                <i class="fas fa-phone text-primary-400 group-hover:text-white text-sm"></i>
              </div>
              <span class="ml-3 text-sm">010-53630591/2/3</span>
            </li>
            <li class="flex items-center group">
              <div class="w-7 h-7 bg-primary-500 bg-opacity-20 rounded-lg flex items-center justify-center group-hover:bg-primary-500 transition-colors duration-300">
                <i class="fas fa-mobile-alt text-primary-400 group-hover:text-white text-sm"></i>
              </div>
              <span class="ml-3 text-sm">18310336602</span>
            </li>
            <li class="flex items-center group">
              <div class="w-7 h-7 bg-primary-500 bg-opacity-20 rounded-lg flex items-center justify-center group-hover:bg-primary-500 transition-colors duration-300">
                <i class="fas fa-map-marker-alt text-primary-400 group-hover:text-white text-sm"></i>
              </div>
              <span class="ml-3 text-sm">北京市海淀区三里河路9号住建部大院</span>
            </li>
          </ul>
        </div>

        <!-- 社交媒体 -->
        <div class="space-y-3">
          <h3 class="text-lg font-bold mb-3 relative inline-block text-white">
            关注我们
            <span class="absolute bottom-0 left-0 w-1/2 h-0.5 bg-primary-500"></span>
          </h3>
          <div class="flex space-x-3">
            <a
              v-for="(social, index) in socialMedia"
              :key="index"
              :href="social.link"
              target="_blank"
              class="w-8 h-8 bg-primary-500 bg-opacity-20 rounded-lg flex items-center justify-center hover:bg-primary-500 transition-colors duration-300 group"
            >
              <i :class="[social.icon, 'text-primary-400 group-hover:text-white text-lg']"></i>
            </a>
          </div>
        </div>
      </div>

      <!-- 版权信息 -->
      <div class="mt-8 pt-6 border-t border-neutral-700/50 text-center">
        <p class="text-neutral-400 text-sm">&copy; {{ new Date().getFullYear() }} 雅博国际展览（天津）有限公司. All rights reserved.</p>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
const quickLinks = [
  { name: '首页', path: '/' },
  { name: '展会信息', path: '/exhibitions' },
  { name: '项目案例', path: '/cases' },
  { name: '补贴信息', path: '/subsidies' },
  { name: '市场咨询', path: '/news' },
  { name: '联系我们', path: '/contact' }
]

const socialMedia = [
  { icon: 'fab fa-weixin', link: '#' },
  { icon: 'fab fa-weibo', link: '#' }
]
</script>

<style scoped>
.container {
  max-width: 1280px;
}
</style> 