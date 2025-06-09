<template>
  <NuxtLayout>
    <NuxtPage />
  </NuxtLayout>
</template>

<script setup lang="ts">
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

// 配置 Element Plus
const config = {
  locale: zhCn
}

// 提供给子组件
provide('ep-locale', config)
</script>

<style>
.el-pagination {
  justify-content: center;
}
</style>
