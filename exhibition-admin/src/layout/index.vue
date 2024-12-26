<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { Fold, Expand, ArrowDown, SwitchButton } from '@element-plus/icons-vue'

const router = useRouter()
const isCollapse = ref(false)

const handleLogout = () => {
  ElMessageBox.confirm('确认退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    localStorage.removeItem('token')
    router.push('/login')
  })
}

const menuItems = [
  {
    icon: 'Picture',
    title: '轮播图管理',
    path: '/admin/carousel'
  },
  {
    icon: 'Document',
    title: '市场资讯管理',
    path: '/admin/news'
  },
  {
    icon: 'Discount',
    title: '补贴信息管理',
    path: '/admin/policy'
  },
  {
    icon: 'Calendar',
    title: '展会管理',
    path: '/admin/exhibition'
  },
  {
    icon: 'Collection',
    title: '案例管理',
    path: '/admin/cases'
  }
]
</script>

<template>
  <el-container class="layout-container">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="aside-container">
      <el-menu
        :collapse="isCollapse"
        router
        :default-active="$route.path"
        class="menu-container"
        :collapse-transition="false"
        background-color="#001529"
        text-color="#fff"
        active-text-color="#409EFF"
      >
        <div class="logo-container">
          <!-- <img src="@/assets/logo.png" alt="Logo" class="logo-image" v-if="!isCollapse"> -->
          <span v-if="!isCollapse" class="logo-text">展会管理系统</span>
        </div>
        <el-menu-item
          v-for="item in menuItems"
          :key="item.path"
          :index="item.path"
          class="menu-item"
        >
          <el-icon><component :is="item.icon" /></el-icon>
          <template #title>{{ item.title }}</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header-container">
        <div class="header-left">
          <el-icon
            class="collapse-icon"
            @click="isCollapse = !isCollapse"
          >
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/" class="breadcrumb">
            <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ $route.meta.title }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleLogout" class="user-dropdown">
            <span class="user-info">
              <el-avatar :size="32" icon="User" class="user-avatar" />
              <span class="username">admin</span>
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">
                  <el-icon><switch-button /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="main-container">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout-container {
  height: 100vh;
}

.aside-container {
  transition: width 0.3s;
  overflow: hidden;
  background-color: #001529;
}

.menu-container {
  height: 100vh;
  border-right: none;
}

.logo-container {
  height: 64px;
  line-height: 64px;
  padding: 0 20px;
  display: flex;
  align-items: center;
  overflow: hidden;
  background-color: #002140;
}

.logo-image {
  height: 32px;
  margin-right: 12px;
}

.logo-text {
  color: #fff;
  font-size: 18px;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
}

.header-container {
  background-color: #fff;
  border-bottom: 1px solid var(--border-color);
  box-shadow: 0 1px 4px var(--shadow-color);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 64px;
}

.header-left {
  display: flex;
  align-items: center;
}

.collapse-icon {
  font-size: 20px;
  cursor: pointer;
  margin-right: 20px;
  color: var(--text-color);
}

.breadcrumb {
  margin-left: 16px;
}

.user-dropdown {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-avatar {
  background-color: var(--primary-color);
  margin-right: 8px;
}

.username {
  font-size: 14px;
  margin: 0 8px;
  color: var(--text-color);
}

.main-container {
  background-color: var(--container-background-color);
  padding: 20px;
  overflow-y: auto;
}

/* 页面切换动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style> 