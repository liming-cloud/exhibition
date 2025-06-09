<template>
  <div class="min-h-screen bg-white">
    <!-- 轮播图 -->
    <HomeCarousel />

    <!-- 服务介绍 - 加大标题字体，增加特效，减小间距 -->
    <section class="py-12">
      <!-- 原py-20改为py-12 -->
      <div class="container">
        <div class="text-center mb-8">
          <!-- 原mb-12改为mb-8 -->
          <h2
            class="text-5xl font-bold bg-gradient-to-r from-primary-500 to-primary-600 bg-clip-text text-transparent mb-6 animate-pulse"
          >
            我们的服务
          </h2>
          <p class="text-lg text-neutral-600 max-w-2xl mx-auto">
            为您提供专业的展会服务，助力企业开拓国际市场
          </p>
        </div>
        <HomeServices />
      </div>
    </section>

    <!-- 世界地图 - 加大标题字体，增加特效，减小间距 -->
    <section class="py-12 bg-gradient-to-b from-neutral-50/50">
      <!-- 原py-20改为py-12 -->
      <div class="container">
        <div class="text-center mb-8">
          <!-- 原mb-12改为mb-8 -->
          <h2
            class="text-5xl font-bold bg-gradient-to-r from-primary-500 to-accent-500 bg-clip-text text-transparent mb-6 animate-bounce"
          >
            全球展会分布
          </h2>
          <p class="text-lg text-neutral-600 max-w-2xl mx-auto">
            覆盖全球主要展会地区，为您提供全方位的展会信息
          </p>
        </div>
        <WorldMap />
      </div>
    </section>

    <!-- 删除展会资讯和补贴政策部分 -->

    <!-- 成功案例 - 只展示主图，加大标题字体，增加特效，减小间距 -->
    <section class="py-12 bg-gradient-to-b from-neutral-50/50">
      <!-- 原py-20改为py-12 -->
      <div class="container">
        <div class="text-center mb-8">
          <!-- 原mb-12改为mb-8 -->
          <h2
            class="text-5xl font-bold bg-gradient-to-r from-accent-500 to-primary-500 bg-clip-text text-transparent mb-6 animate-pulse"
          >
            成功案例
          </h2>
          <p class="text-lg text-neutral-600 max-w-2xl mx-auto">
            展示我们的服务成果，分享客户的成功故事
          </p>
        </div>
        <HomeCasesPreview />
        <!-- 新组件：只展示主图 -->
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import HomeCarousel from '~/components/home/HomeCarousel.vue'
import HomeServices from '~/components/home/HomeServices.vue'
import WorldMap from '~/components/home/WorldMap.vue'
import HomeCasesPreview from '~/components/home/HomeCasesPreview.vue' // 新组件
</script>

<style scoped>
.container {
  max-width: 1280px;
}
</style>