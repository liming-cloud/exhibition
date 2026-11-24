<template>
  <!-- 通栏轮播：展示后台所有启用的轮播图（按后台排序），客户在后台「轮播图」自行上传、排序、停用 -->
  <section class="relative h-[220px] sm:h-[360px] lg:h-[520px] bg-neutral-800">
    <el-carousel
      v-if="slides.length"
      height="100%"
      class="h-full"
      :interval="5000"
      :arrow="slides.length > 1 ? 'hover' : 'never'"
      :indicator-position="slides.length > 1 ? '' : 'none'"
    >
      <el-carousel-item v-for="(item, index) in slides" :key="item.id">
        <img
          :src="fileUrl(item.imageUrl)"
          :alt="item.title"
          class="w-full h-full object-cover"
          :loading="index === 0 ? 'eager' : 'lazy'"
        />
        <div
          class="absolute inset-0 bg-gradient-to-t from-black/70 via-black/10 to-transparent"
        ></div>
        <div class="absolute inset-x-0 bottom-0">
          <div class="container pb-8 sm:pb-12 lg:pb-16">
            <p
              class="text-xl sm:text-3xl lg:text-4xl font-bold text-white drop-shadow line-clamp-2 max-w-3xl"
            >
              {{ item.title }}
            </p>
            <NuxtLink
              v-if="item.link"
              :to="item.link"
              class="mt-4 inline-flex items-center h-10 px-5 rounded-md bg-primary-600 text-white text-sm font-medium hover:bg-primary-700"
            >
              了解更多<i class="fas fa-arrow-right ml-2 text-xs"></i>
            </NuxtLink>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>

    <!-- 后台还没有启用的轮播图时，显示公司标语，不出现空白 -->
    <div v-else class="h-full bg-gradient-to-br from-primary-800 to-primary-600 flex items-center">
      <div class="container text-white">
        <p class="text-2xl sm:text-4xl font-bold">专注境外展会组展与参展服务</p>
        <p class="mt-3 text-white/80">一站式出展服务，助力企业开拓国际市场</p>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
const api = useApi()
const { data } = await useAsyncData('home-carousel', () =>
  api.carousel.getList({ pageNum: 1, pageSize: 10, status: 1 })
)
const slides = computed(() => [...(data.value?.rows ?? [])].sort((a, b) => a.sort - b.sort))
</script>

<style scoped>
:deep(.el-carousel__indicator .el-carousel__button) {
  width: 24px;
  height: 3px;
}
</style>
