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

useHead({
  titleTemplate: title => (title ? `${title} - 雅博展览` : '雅博展览 - 专业国际展会服务')
})
</script>

<style>
.el-pagination {
  justify-content: center;
}
</style>
