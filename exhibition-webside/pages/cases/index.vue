<template>
  <div class="container mx-auto px-4 py-8">
    <!-- 页面标题 -->
    <div class="text-center mb-12">
      <h1
        class="text-5xl font-bold bg-gradient-to-r from-accent-500 to-primary-500 bg-clip-text text-transparent mb-6 animate-pulse"
      >
        成功案例
      </h1>
      <p class="text-lg text-neutral-600 max-w-2xl mx-auto">
        展示我们的服务成果，分享客户的成功故事
      </p>
    </div>

    <!-- 成功案例卡片列表 - 参照展会列表样式 -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
      <div
        v-for="item in casesList"
        :key="item.id"
        class="bg-gradient-to-br from-white to-neutral-50 rounded-xl shadow-elegant hover:shadow-modern overflow-hidden transition-all duration-500 border border-neutral-100 group"
      >
        <!-- 案例图片 -->
        <div class="relative overflow-hidden h-64">
          <img
            :src="`/api/file/preview?path=${encodeURIComponent(item.coverUrl)}`"
            :alt="item.title"
            class="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500"
          />
          <div
            class="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300"
          >
            <div class="absolute bottom-4 left-4 text-white">
              <span
                class="px-3 py-1 bg-gradient-to-r from-accent-500 to-accent-600 text-white text-sm rounded-full"
              >
                成功案例
              </span>
            </div>
          </div>
        </div>

        <!-- 案例内容 -->
        <div class="p-6">
          <h3 class="text-xl font-bold mb-4 text-neutral-800">{{ item.title }}</h3>

          <div class="text-neutral-600 mb-6 line-clamp-3" v-html="item.content"></div>

          <!-- 查看详情按钮 -->
          <div class="flex justify-center">
            <NuxtLink
              :to="`/cases/${item.id}`"
              class="inline-flex items-center px-6 py-2 bg-gradient-to-r from-accent-500 to-accent-600 text-white rounded-lg hover:shadow-glow transition-all duration-300"
            >
              查看详情
              <i class="fas fa-arrow-right ml-2"></i>
            </NuxtLink>
          </div>
        </div>
      </div>
    </div>
    <!-- 分页 -->
    <div class="mt-8 flex justify-center">
      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[12, 24, 36]"
        layout="total, sizes, prev, pager, next"
        :pager-count="5"
        locale="zh-cn"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import type { Cases } from '~/types/cases'

const api = useApi()
const casesList = ref<Cases[]>([])
const total = ref(0)

const queryParams = reactive({
  pageNum: 1,
  pageSize: 12
})

const getCasesList = async () => {
  try {
    const res = await api.cases.getList(queryParams)
    if (res.code === 200) {
      casesList.value = res.rows
      total.value = res.total
    }
  } catch (error) {
    console.error('获取案例列表失败:', error)
  }
}

const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  getCasesList()
}

const handleCurrentChange = (val: number) => {
  queryParams.pageNum = val
  getCasesList()
}

onMounted(() => {
  getCasesList()
})
</script>

<style scoped>
.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>