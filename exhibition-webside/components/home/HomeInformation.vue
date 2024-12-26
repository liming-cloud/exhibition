<template>
  <div class="space-y-4">
    <!-- 加载状态 -->
    <div v-if="loading" class="space-y-4">
      <div v-for="n in 3" :key="n" class="animate-pulse">
        <div class="h-4 bg-neutral-100 rounded-full w-3/4 mb-2"></div>
        <div class="h-3 bg-neutral-100 rounded-full w-1/4"></div>
      </div>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="py-8 text-center">
      <div class="w-16 h-16 mx-auto mb-4 rounded-full bg-primary-50 flex items-center justify-center">
        <i class="fas fa-exclamation-circle text-primary-500 text-2xl"></i>
      </div>
      <p class="text-neutral-600 mb-4">{{ error }}</p>
      <button 
        @click="fetchInformationList" 
        class="btn btn-secondary inline-flex items-center text-sm group"
      >
        <i class="fas fa-redo mr-2 group-hover:rotate-180 transition-transform duration-500"></i>
        重新加载
      </button>
    </div>

    <!-- 数据列表 -->
    <template v-else>
      <div class="space-y-4">
        <div v-for="(item, index) in informationList" 
             :key="item.id" 
             class="list-item group"
        >
          <div class="flex-1 min-w-0">
            <NuxtLink :to="'/news/' + item.id" class="block group">
              <h3 class="text-neutral-800 group-hover:text-primary-500 truncate pr-4 font-medium transition-colors">
                {{ item.title }}
              </h3>
              <p class="text-neutral-500 text-sm mt-1 flex items-center">
                <i class="far fa-clock mr-2"></i>
                {{ formatDate(item.createTime) }}
              </p>
            </NuxtLink>
          </div>
          <div class="flex-shrink-0">
            <NuxtLink :to="'/news/' + item.id" class="text-primary-500 hover:text-primary-600">
              <i class="fas fa-chevron-right"></i>
            </NuxtLink>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="informationList.length === 0" class="py-12 text-center">
        <div class="w-16 h-16 mx-auto mb-4 rounded-full bg-primary-50 flex items-center justify-center">
          <i class="fas fa-inbox text-primary-500 text-2xl"></i>
        </div>
        <p class="text-neutral-600">暂无资讯</p>
      </div>

      <!-- 更多按钮 -->
      <div class="pt-4 text-center">
        <NuxtLink to="/news" class="btn btn-secondary inline-flex items-center group">
          查看更多
          <i class="fas fa-arrow-right ml-2 transform transition-transform group-hover:translate-x-1"></i>
        </NuxtLink>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { Information } from '~/types/information'
import { formatDate } from '~/utils/date'

const api = useApi()
const informationList = ref<Information[]>([])
const loading = ref(true)
const error = ref('')

const fetchInformationList = async () => {
  loading.value = true
  error.value = ''
  
  try {
    const res = await api.information.getList({
      pageNum: 1,
      pageSize: 5
    })
    if (res.code === 200) {
      informationList.value = res.rows
    } else {
      error.value = res.msg || '获取资讯失败'
    }
  } catch (err) {
    error.value = '网络错误，请稍后重试'
    console.error('获取市场资讯失败:', err)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchInformationList()
})
</script> 