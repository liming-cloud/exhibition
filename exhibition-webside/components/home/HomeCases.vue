<template>
  <div class="grid-cols-auto-fit">
    <!-- 加载状态 -->
    <template v-if="loading">
      <div v-for="n in 4" :key="n" class="animate-pulse">
        <div class="aspect-w-4 aspect-h-3 rounded-2xl bg-neutral-100"></div>
        <div class="mt-4 space-y-2">
          <div class="h-4 bg-neutral-100 rounded-full w-3/4"></div>
          <div class="h-3 bg-neutral-100 rounded-full w-1/2"></div>
        </div>
      </div>
    </template>

    <!-- 错误状态 -->
    <template v-else-if="error">
      <div class="col-span-full py-12 text-center">
        <div class="w-16 h-16 mx-auto mb-4 rounded-full bg-primary-50 flex items-center justify-center">
          <i class="fas fa-exclamation-circle text-primary-500 text-2xl"></i>
        </div>
        <p class="text-neutral-600 mb-4">{{ error }}</p>
        <button 
          @click="getCasesList" 
          class="btn btn-secondary inline-flex items-center text-sm group"
        >
          <i class="fas fa-redo mr-2 group-hover:rotate-180 transition-transform duration-500"></i>
          重新加载
        </button>
      </div>
    </template>

    <!-- 数据列表 -->
    <template v-else>
      <div v-for="item in casesList" 
           :key="item.id" 
           class="group overflow-hidden rounded-2xl bg-white shadow-modern hover:shadow-hover transition-all duration-300 hover:-translate-y-1"
      >
        <div class="aspect-w-4 aspect-h-3 overflow-hidden">
          <img 
            :src="`/api/file/preview?path=${encodeURIComponent(item.coverUrl)}`" 
            :alt="item.title" 
            class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110"
          >
          <div class="absolute inset-0 bg-gradient-to-t from-black/80 via-black/30 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>
        </div>
        <div class="p-6">
          <h3 class="text-lg font-semibold text-neutral-900 mb-2 line-clamp-1 group-hover:text-primary-500 transition-colors">
            {{ item.title }}
          </h3>
          <p class="text-neutral-600 text-sm line-clamp-2 mb-4" v-html="item.content"></p>
          <NuxtLink 
            :to="'/cases/' + item.id" 
            class="inline-flex items-center text-sm text-primary-500 hover:text-primary-600 font-medium group/link"
          >
            查看详情
            <i class="fas fa-arrow-right ml-2 transform transition-transform group-hover/link:translate-x-1"></i>
          </NuxtLink>
        </div>
      </div>

      <!-- 空状态 -->
      <template v-if="casesList.length === 0">
        <div class="col-span-full py-12 text-center">
          <div class="w-16 h-16 mx-auto mb-4 rounded-full bg-primary-50 flex items-center justify-center">
            <i class="fas fa-inbox text-primary-500 text-2xl"></i>
          </div>
          <p class="text-neutral-600">暂无案例</p>
        </div>
      </template>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { Cases } from '~/types/cases'

const api = useApi()
const casesList = ref<Cases[]>([])
const loading = ref(true)
const error = ref('')

const getCasesList = async () => {
  loading.value = true
  error.value = ''

  try {
    const res = await api.cases.getList({
      pageNum: 1,
      pageSize: 4
    })
    if (res.code === 200) {
      casesList.value = res.rows
    } else {
      error.value = res.msg || '获取案例失败'
    }
  } catch (err) {
    error.value = '网络错误，请稍后重试'
    console.error('获取项目案例失败:', err)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  getCasesList()
})
</script>

<style scoped>
.aspect-w-4 {
  position: relative;
  padding-bottom: 75%;
}

.aspect-w-4 > * {
  position: absolute;
  width: 100%;
  height: 100%;
  left: 0;
  top: 0;
}

.line-clamp-1 {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style> 