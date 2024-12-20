<!-- pages/about.vue -->
<template>
  <div class="max-w-7xl mx-auto py-12 px-4 sm:px-6 lg:px-8">
    <!-- 页面标题 -->
    <h1 class="text-4xl font-bold text-center mb-8">关于我们</h1>

    <!-- 公司简介 -->
    <section class="mb-12">
      <h2 class="text-2xl font-semibold mb-4">公司简介</h2>
      <p class="text-gray-700 mb-4">
        雅博国际展览（天津）有限公司成立于 2023 年，隶属于北京雅博国际展览有限公司（2017 年成立）。我司专业从事境外展会的组展工作，与全球各大贸易展览机构（英富曼、励展、Terrapinn、杜塞尔多夫展览、慕尼黑展览集团等）保持着长期、密切的友好合作。
      </p>
      <p class="text-gray-700 mb-4">
        雅博展览团队成员均具有十几年海外展会操作经验。我司项目主要集中在电梯、建材、能源、储能、电动车等领域。针对企业需求，提供全方位的、细致周到的参展服务，包括：展会咨询、展位售卖、展位设计装修、机票预订、签证服务、境外商务团队行程服务，以及辅助企业申请相关政策性资金补贴等。
      </p>
      <p class="text-gray-700">
        雅博展览，为参展企业提供定制化一站式出展服务，为国内的生产商和贸易商搭建专业的展览平台，助力企业开拓国际市场。
      </p>
    </section>

    <!-- 展团照片展示 -->
    <section class="mb-12">
      <h2 class="text-2xl font-semibold mb-4">展团照片</h2>
      <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
        <!-- 展团照1 -->
        <div class="overflow-hidden rounded-lg shadow">
          <img
              src="/images/news/slide2.jpg"
          alt="北京雅博国际展览有限公司展团照"
          class="w-full h-64 object-cover object-center"
          />
        </div>
        <!-- 展团照2 -->
        <div class="overflow-hidden rounded-lg shadow">
          <img
              src="/images/news/slide2.jpg"
          alt="雅博国际展览（天津）有限公司展团照"
          class="w-full h-64 object-cover object-center"
          />
        </div>
        <!-- 更多展团照可根据需要添加 -->
      </div>
    </section>
  </div>
</template>

<script setup>
// 如果需要，可以在这里添加组件逻辑
</script>

<style scoped>
/* 您可以根据需要在这里添加自定义样式 */
</style>