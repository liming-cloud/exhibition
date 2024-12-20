<template>
  <div class="bg-gray-50">
    <!-- 轮播图 -->
    <Carousel />
    <!-- 最新资讯 -->
    <NewsSection />
    <!-- 全球展会分布 -->
    <GlobalExhibitions />
    <ExhibitionFilter/>
    <!-- 关于我们 -->
    <AboutUs />
  </div>
</template>

<script setup>
import Carousel from '~/components/Carousel.vue'
import NewsSection from '~/components/NewsSection.vue'
import GlobalExhibitions from '~/components/GlobalExhibitions.vue'
import AboutUs from '~/components/AboutUs.vue'
import ExhibitionFilter from '~/components/ExhibitionFilter.vue'
</script>