<template>
  <nav class="sticky top-0 z-50 bg-white/80 backdrop-blur-modern border-b border-neutral-200/50">
    <div class="container">
      <div class="flex justify-between items-center h-20">
        <!-- Logo -->
        <NuxtLink to="/" class="flex items-center space-x-3 group">
          <div
            class="w-10 h-10 bg-gradient-to-br from-primary-400 to-primary-600 rounded-xl flex items-center justify-center text-white font-bold text-xl shadow-glow transition-all duration-300 group-hover:scale-105">
            YB
          </div>
          <span class="text-xl font-bold text-neutral-900 group-hover:text-primary-500 transition-colors">雅博展览</span>
        </NuxtLink>

        <!-- 导航链接 -->
        <div class="hidden md:flex items-center space-x-2">
          <NuxtLink v-for="item in navItems" :key="item.path" :to="item.path"
            @click="handleNavClick(item)"
            class="px-4 py-2 text-neutral-700 hover:text-primary-500 rounded-xl transition-all duration-300"
            active-class="text-primary-500 font-semibold">
            {{ item.name }}
          </NuxtLink>
          <NuxtLink to="/contact" class="btn btn-primary ml-4 group">
            联系我们
            <i class="fas fa-arrow-right ml-2 transform transition-transform group-hover:translate-x-1"></i>
          </NuxtLink>
        </div>

        <!-- 移动端菜单按钮 -->
        <button
          class="md:hidden w-10 h-10 flex items-center justify-center text-neutral-600 hover:text-neutral-900 hover:bg-neutral-100 rounded-xl transition-colors focus:outline-none"
          @click="isMenuOpen = !isMenuOpen">
          <i class="fas" :class="isMenuOpen ? 'fa-times' : 'fa-bars'"></i>
        </button>
      </div>

      <!-- 移动端菜单 -->
      <div v-show="isMenuOpen" class="md:hidden py-4 space-y-2 animate-slide-down">
        <NuxtLink v-for="item in navItems" :key="item.path" :to="item.path"
          class="block px-4 py-3 text-neutral-700 hover:text-primary-500 hover:bg-primary-50 rounded-xl transition-all duration-300"
          active-class="text-primary-500 font-semibold bg-primary-50" @click="handleMobileNavClick(item)">
          {{ item.name }}
        </NuxtLink>
        <div class="px-4 pt-4">
          <NuxtLink to="/contact" class="btn btn-primary w-full group">
            联系我们
            <i class="fas fa-arrow-right ml-2 transform transition-transform group-hover:translate-x-1"></i>
          </NuxtLink>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
const isMenuOpen = ref(false)

interface NavItem {
  name: string
  path: string
}

const navItems: NavItem[] = [
  { name: '首页', path: '/' },
  { name: '展会列表', path: '/exhibitions' }, // 改名：展会资讯 -> 展会列表
  { name: '行业动态', path: '/news' }, // 改名：新闻动态 -> 行业动态
  { name: '成功案例', path: '/cases' },
  { name: '关于我们', path: '/about' },
  // 删除：补贴政策
]

// 监听路由变化，关闭移动端菜单
watch(() => useRoute().path, () => {
  isMenuOpen.value = false
})

const handleNavClick = async (item: NavItem) => {
  console.log(`Clicked on ${item.name}, navigating to ${item.path}`)
  try {
    await navigateTo(item.path)
    console.log(`Navigation to ${item.path} completed`)
  } catch (error) {
    console.error(`Navigation failed:`, error)
  }
}

const handleMobileNavClick = async (item: NavItem) => {
  isMenuOpen.value = false
  await handleNavClick(item)
}
</script>

<style scoped>
/* 移动端菜单动画 */
.animate-slide-down {
  animation: slideDown 0.3s ease-out;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>