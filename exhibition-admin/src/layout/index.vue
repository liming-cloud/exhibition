<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="aside-container">
      <div class="logo-container">
        <img src="https://placeholder.co/32x32" alt="logo" class="logo-image" />
        <h1 class="logo-text">管理系统</h1>
      </div>
      <el-scrollbar>
        <el-menu
          :default-active="route.path"
          router
          class="menu-container"
          background-color="transparent"
          text-color="#fff"
          active-text-color="#409eff"
        >
          <el-menu-item index="/carousel">
            <el-icon><Picture /></el-icon>
            <span>轮播图管理</span>
          </el-menu-item>
          <el-menu-item index="/news">
            <el-icon><Document /></el-icon>
            <span>资讯管理</span>
          </el-menu-item>
          <el-menu-item index="/exhibition">
            <el-icon><List /></el-icon>
            <span>展会信息管理</span>
          </el-menu-item>
          <el-menu-item index="/subsidy">
            <el-icon><Money /></el-icon>
            <span>补贴政策管理</span>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>
    <el-container>
      <el-header height="60px" class="header-container">
        <div class="header-left">
          <h2 class="page-title">{{ getPageTitle }}</h2>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand" class="user-dropdown">
            <div class="user-info">
              <el-avatar :size="32" src="https://placeholder.co/32x32" />
              <span class="username">{{ store.state.user?.username }}</span>
              <el-icon class="el-icon--right"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main>
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useStore } from 'vuex'
import { useRouter, useRoute } from 'vue-router'
import { Picture, Document, List, ArrowDown, SwitchButton, Money } from '@element-plus/icons-vue'
import { computed } from 'vue'

defineOptions({
  name: 'LayoutIndex'
})

const store = useStore()
const router = useRouter()
const route = useRoute()

const getPageTitle = computed(() => {
  const { meta } = route
  return meta?.title || ''
})

const handleCommand = (command) => {
  if (command === 'logout') {
    store.dispatch('logout')
    router.push('/login')
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.aside-container {
  background-color: #001529;
  transition: width 0.3s;
  position: relative;
  z-index: 1000;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
}

.logo-container {
  height: 60px;
  padding: 0 16px;
  display: flex;
  align-items: center;
  overflow: hidden;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo-image {
  width: 32px;
  height: 32px;
  border-radius: 50%;
}

.logo-text {
  color: #fff;
  font-size: 18px;
  font-weight: 600;
  margin-left: 12px;
  white-space: nowrap;
}

.menu-container {
  border-right: none;
}

.menu-container :deep(.el-menu-item) {
  height: 50px;
  line-height: 50px;
  margin: 4px 0;
}

.menu-container :deep(.el-menu-item.is-active) {
  background: #1890ff !important;
  color: #fff !important;
}

.menu-container :deep(.el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.1) !important;
}

.header-container {
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
}

.header-left {
  display: flex;
  align-items: center;
}

.page-title {
  margin: 0;
  font-size: 20px;
  font-weight: 500;
  color: #1f2f3d;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 0 8px;
  border-radius: 4px;
  transition: all 0.3s;
}

.user-info:hover {
  background: rgba(0, 0, 0, 0.025);
}

.username {
  margin: 0 8px;
  color: #1f2f3d;
  font-size: 14px;
}

.el-main {
  background-color: #f0f2f5;
  padding: 24px;
}

/* 页面切换动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style> 