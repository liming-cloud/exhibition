<template>
  <div class="relative">
    <el-carousel v-if="carouselList.length > 0" height="500px" :interval="5000" arrow="always">
      <el-carousel-item v-for="item in carouselList" :key="item.id">
        <img
          :src="`/api/file/preview?path=${encodeURIComponent(item.imageUrl)}`"
          :alt="item.title"
          class="w-full h-full object-cover"
        />
        <div class="absolute bottom-0 left-0 right-0 bg-black bg-opacity-50 text-white p-4">
          <h3 class="text-xl font-bold">{{ item.title }}</h3>
          <a
            v-if="item.link"
            :href="item.link"
            target="_blank"
            class="text-blue-400 hover:text-blue-300 mt-2 inline-block"
          >
            了解更多
          </a>
        </div>
      </el-carousel-item>
    </el-carousel>
    <div v-else class="h-[500px] flex items-center justify-center bg-gray-100">
      <p class="text-gray-500">{{ loading ? '加载中...' : '暂无轮播图数据' }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted } from 'vue'

const api = useApi()
const { data, pending: loading, refresh } = await useAsyncData('home-carousel', () =>
  api.carousel.getList({
    pageNum: 1,
    pageSize: 10,
    status: 1
  })
)
const carouselList = computed(() => data.value?.rows ?? [])

// 定期刷新数据
let refreshInterval: NodeJS.Timeout | null = null

onMounted(() => {
  // 每5分钟刷新一次数据
  refreshInterval = setInterval(refresh, 5 * 60 * 1000)
})

onBeforeUnmount(() => {
  if (refreshInterval) {
    clearInterval(refreshInterval)
  }
})
</script>

<style scoped>
.el-carousel {
  --el-carousel-arrow-size: 36px;
  --el-carousel-arrow-background: rgba(0, 0, 0, 0.3);
  --el-carousel-arrow-hover-background: rgba(0, 0, 0, 0.5);
}

:deep(.el-carousel__arrow) {
  border-radius: 50%;
}

:deep(.el-carousel__button) {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.4);
}

:deep(.el-carousel__button:hover),
:deep(.el-carousel__button--active) {
  background-color: white;
}
</style>