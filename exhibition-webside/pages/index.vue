<template>
  <div class="min-h-screen bg-white">
    <!-- 轮播图 -->
    <HomeCarousel />

    <!-- 服务介绍 -->
    <section class="py-20">
      <div class="container">
        <div class="text-center mb-12">
          <h2 class="text-gradient mb-4">我们的服务</h2>
          <p class="text-lg text-neutral-600 max-w-2xl mx-auto">
            为您提供专业的展会服务，助力企业开拓国际市场
          </p>
        </div>
        <HomeServices />
      </div>
    </section>

    <!-- 世界地图 -->
    <section class="py-20 bg-gradient-to-b from-neutral-50/50">
      <div class="container">
        <div class="text-center mb-12">
          <h2 class="text-gradient mb-4">全球展会分布</h2>
          <p class="text-lg text-neutral-600 max-w-2xl mx-auto">
            覆盖全球主要展会地区，为您提供全方位的展会信息
          </p>
        </div>
        <WorldMap />
      </div>
    </section>

    <!-- 资讯和补贴 -->
    <section class="py-20">
      <div class="container">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
          <!-- 展会资讯 -->
          <div class="card">
            <div class="card-header">
              <div class="card-title-bar"></div>
              <h3 class="card-title">展会资讯</h3>
            </div>
            <HomeInformation />
          </div>

          <!-- 补贴政策 -->
          <div class="card">
            <div class="card-header">
              <div class="card-title-bar"></div>
              <h3 class="card-title">补贴政策</h3>
            </div>
            <HomeSubsidies />
          </div>
        </div>
      </div>
    </section>

    <!-- 成功案例 -->
    <section class="py-20 bg-gradient-to-b from-neutral-50/50">
      <div class="container">
        <div class="text-center mb-12">
          <h2 class="text-gradient mb-4">成功案例</h2>
          <p class="text-lg text-neutral-600 max-w-2xl mx-auto">
            展示我们的服务成果，分享客户的成功故事
          </p>
        </div>
        <HomeCases />
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import HomeCarousel from '~/components/home/HomeCarousel.vue'
import HomeServices from '~/components/home/HomeServices.vue'
import WorldMap from '~/components/home/WorldMap.vue'
import HomeInformation from '~/components/home/HomeInformation.vue'
import HomeSubsidies from '~/components/home/HomeSubsidies.vue'
import HomeCases from '~/components/home/HomeCases.vue'
</script>

<style scoped>
.container {
  max-width: 1280px;
}
</style> 