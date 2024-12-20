<!-- layouts/default.vue -->
<template>
  <div>
    <Navbar />
    <main>
      <NuxtPage />
    </main>
    <Footer />
  </div>
</template>

<script setup>
import Navbar from '~/components/Navbar.vue'
import Footer from '~/components/Footer.vue'
</script>

<style>
/* 重置 body 的 margin，以避免固定导航栏遮挡内容 */
body {
  margin: 0;
}
main {
  margin-top: 64px; /* 导航栏高度为 64px */
}
</style>