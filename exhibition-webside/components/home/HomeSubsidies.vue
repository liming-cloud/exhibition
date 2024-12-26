<template>
  <div class="space-y-4">
    <!-- 加载状态 -->
    <div v-if="loading" class="py-8">
      <div class="flex justify-center items-center space-x-3">
        <div class="w-10 h-10 rounded-full border-2 border-primary-500 border-t-transparent animate-spin"></div>
        <span class="text-neutral-500">加载中...</span>
      </div>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="py-8 text-center">
      <div class="w-16 h-16 mx-auto mb-4 rounded-full bg-primary-50 flex items-center justify-center">
        <i class="fas fa-exclamation-circle text-primary-500 text-2xl"></i>
      </div>
      <p class="text-neutral-600 mb-4">{{ error }}</p>
      <button 
        @click="fetchSubsidiesList" 
        class="btn btn-secondary inline-flex items-center text-sm group"
      >
        <i class="fas fa-redo mr-2 group-hover:rotate-180 transition-transform duration-500"></i>
        重新加载
      </button>
    </div>

    <!-- 数据列表 -->
    <template v-else>
      <div class="space-y-4">
        <div v-for="item in subsidiesList" 
             :key="item.id" 
             class="list-item group"
        >
          <div class="flex-1 min-w-0">
            <NuxtLink :to="'/subsidies/' + item.id" class="block group">
              <h3 class="text-neutral-800 group-hover:text-primary-500 truncate pr-4 font-medium transition-colors">
                {{ item.title }}
              </h3>
              <div class="flex items-center justify-between mt-2">
                <span class="text-primary-500 font-medium flex items-center">
                  <i class="fas fa-yen-sign mr-1"></i>
                  {{ formatPrice(item.price) }}
                </span>
                <span class="text-neutral-500 text-sm flex items-center">
                  <i class="far fa-calendar-alt mr-2"></i>
                  {{ formatDate(item.createTime) }}
                </span>
              </div>
            </NuxtLink>
          </div>
          <div class="flex-shrink-0">
            <NuxtLink :to="'/subsidies/' + item.id" class="text-primary-500 hover:text-primary-600">
              <i class="fas fa-chevron-right"></i>
            </NuxtLink>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="subsidiesList.length === 0" class="py-12 text-center">
        <div class="w-16 h-16 mx-auto mb-4 rounded-full bg-primary-50 flex items-center justify-center">
          <i class="fas fa-inbox text-primary-500 text-2xl"></i>
        </div>
        <p class="text-neutral-600">暂无政策</p>
      </div>

      <!-- 更多按钮 -->
      <div class="pt-4 text-center">
        <NuxtLink to="/subsidies" class="btn btn-secondary inline-flex items-center group">
          查看更多
          <i class="fas fa-arrow-right ml-2 transform transition-transform group-hover:translate-x-1"></i>
        </NuxtLink>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { Subsidies } from '~/types/subsidies'
import { formatDate } from '~/utils/date'

const api = useApi()
const subsidiesList = ref<Subsidies[]>([])
const loading = ref(true)
const error = ref('')

// 格式化价格
const formatPrice = (price: number) => {
  const amount = price / 10000
  return amount >= 1 ? `${amount.toFixed(2)}万` : `${price}元`
}

const fetchSubsidiesList = async () => {
  loading.value = true
  error.value = ''
  
  try {
    const res = await api.subsidies.getList({
      pageNum: 1,
      pageSize: 5
    })
    if (res.code === 200) {
      subsidiesList.value = res.rows
    } else {
      error.value = res.msg || '获取政策失败'
    }
  } catch (err) {
    error.value = '网络错误，请稍后重试'
    console.error('获取补贴政策失败:', err)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchSubsidiesList()
})
</script> 