<template>
  <!-- 手机端底部留出联系条的高度，避免遮住页脚 -->
  <div class="min-h-screen flex flex-col pb-14 md:pb-0">
    <TheNavbar />
    <main class="flex-grow">
      <slot />
    </main>
    <TheFooter />
    <MobileContactBar />
  </div>
</template>

<script setup lang="ts">
import TheNavbar from '~/components/layout/TheNavbar.vue'
import TheFooter from '~/components/layout/TheFooter.vue'
import MobileContactBar from '~/components/layout/MobileContactBar.vue'
</script>
