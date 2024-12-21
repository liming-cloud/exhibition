<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { Fold, Expand } from '@element-plus/icons-vue'

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
  }
]
</script>

<template>
  <el-container class="layout-container">
    <el-aside :width="isCollapse ? '64px' : '200px'">
      <el-menu
        :collapse="isCollapse"
        router
        :default-active="$route.path"
        class="menu-container"
      >
        <div class="logo-container">
          <span v-if="!isCollapse">展会管理系统</span>
        </div>
        <el-menu-item
          v-for="item in menuItems"
          :key="item.path"
          :index="item.path"
        >
          <el-icon><component :is="item.icon" /></el-icon>
          <template #title>{{ item.title }}</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header>
        <div class="header-left">
          <el-icon
            class="collapse-icon"
            @click="isCollapse = !isCollapse"
          >
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleLogout">
            <span class="user-info">
              <el-avatar :size="32" icon="User" />
              <span class="username">admin</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout-container {
  height: 100vh;
}

.menu-container {
  height: 100vh;
  border-right: none;
}

.logo-container {
  height: 60px;
  line-height: 60px;
  text-align: center;
  font-size: 16px;
  font-weight: bold;
  color: var(--el-menu-active-color);
  border-bottom: 1px solid var(--el-border-color-light);
}

.el-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid var(--el-border-color-light);
}

.collapse-icon {
  font-size: 20px;
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.username {
  margin-left: 8px;
}

.el-main {
  background-color: var(--el-bg-color-page);
  padding: 20px;
}
</style> 