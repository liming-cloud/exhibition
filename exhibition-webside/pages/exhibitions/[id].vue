<template>
  <div class="container mx-auto px-4 py-8">
    <div v-if="exhibition" class="bg-white rounded-lg shadow-lg overflow-hidden">
      <!-- 展会标题 -->
      <div class="bg-gradient-to-r from-primary-500 to-primary-600 text-white p-8">
        <h1 class="text-3xl font-bold mb-4">{{ exhibition.name }}</h1>
        <div class="flex flex-wrap gap-6 text-lg">
          <div class="flex items-center">
            <i class="fas fa-map-marker-alt mr-2"></i>
            {{ exhibition.countryName }} - {{ exhibition.location }}
          </div>
          <div class="flex items-center">
            <i class="fas fa-calendar-alt mr-2"></i>
            {{ exhibition.time }}
          </div>
        </div>
      </div>

      <!-- 展会内容 -->
      <div class="p-8">
        <div class="prose max-w-none" v-html="exhibition.content"></div>
      </div>

      <!-- 操作按钮区域 -->
      <div class="p-8 border-t flex justify-between items-center">
        <NuxtLink to="/exhibitions" 
                 class="inline-flex items-center px-6 py-3 bg-neutral-100 text-neutral-700 rounded-lg hover:bg-neutral-200 transition-colors">
          <i class="fas fa-arrow-left mr-2"></i>
          返回展会列表
        </NuxtLink>

        <!-- 报名按钮 -->
        <el-button type="primary" @click="openRegistrationModal"
          class="!bg-primary-500 !border-primary-500 hover:!bg-primary-600 px-8 py-3">
          <i class="fas fa-user-plus mr-2"></i>
          立即报名
        </el-button>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-else class="flex justify-center items-center h-64">
      <el-skeleton :rows="10" animated />
    </div>

    <!-- 报名弹窗 -->
    <RegistrationModal v-model="showRegistrationModal" :exhibition="exhibition" @submit="handleRegistration" />
  </div>
</template>

<script setup lang="ts">
import type { Exhibition } from '~/types/exhibition'
import RegistrationModal from '~/components/common/RegistrationModal.vue'
import { ElMessage } from 'element-plus'

const route = useRoute()
const api = useApi()
const exhibition = ref<Exhibition | null>(null)

// 报名功能相关
const showRegistrationModal = ref(false)

// 获取展会详情
const fetchExhibition = async () => {
  try {
    const id = parseInt(route.params.id as string)
    if (!isNaN(id)) {
      exhibition.value = await api.exhibition.getDetail(id)
    }
  } catch (error) {
    console.error('Failed to fetch exhibition details:', error)
  }
}

// 打开报名弹窗
const openRegistrationModal = () => {
  showRegistrationModal.value = true
}

// 处理报名提交
const handleRegistration = async (data: any) => {
  try {
    await $fetch('/api/exhibition/registration/submit', {
      method: 'POST',
      body: data
    })
    ElMessage.success('报名成功')
    showRegistrationModal.value = false
  } catch (error) {
    console.error('报名失败:', error)
    ElMessage.error('报名失败')
  }
}

// 页面加载时获取数据
onMounted(() => {
  fetchExhibition()
})
</script>

<style>
.prose {
  max-width: none;
}

.prose h1 {
  @apply text-3xl font-bold mb-6 text-neutral-800;
}

.prose h2 {
  @apply text-2xl font-bold mb-4 mt-8 text-neutral-800;
}

.prose h3 {
  @apply text-xl font-bold mb-3 mt-6 text-neutral-800;
}

.prose p {
  @apply mb-4 text-neutral-600 leading-relaxed;
}

.prose ul {
  @apply list-disc list-inside mb-4 text-neutral-600;
}

.prose ol {
  @apply list-decimal list-inside mb-4 text-neutral-600;
}

.prose li {
  @apply mb-2 text-neutral-600;
}

.prose img {
  @apply rounded-lg shadow-md my-6;
}

.prose a {
  @apply text-primary-500 hover:text-primary-600 underline;
}

.prose blockquote {
  @apply border-l-4 border-neutral-200 pl-4 italic my-6 text-neutral-600;
}

.prose pre {
  @apply bg-neutral-50 p-4 rounded-lg my-6;
}

.prose code {
  @apply bg-neutral-50 px-2 py-1 rounded text-neutral-700;
}
</style> 