<template>
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div v-for="case_ in cases" :key="case_.id"
            class="group relative overflow-hidden rounded-xl shadow-elegant hover:shadow-modern transition-all duration-500">
            <img :src="`/api/file/preview?path=${encodeURIComponent(case_.coverUrl)}`" :alt="case_.title"
                class="w-full h-64 object-cover group-hover:scale-110 transition-transform duration-500" />
            <div
                class="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                <div class="absolute bottom-4 left-4 text-white">
                    <h4 class="font-bold text-lg">{{ case_.title }}</h4>
                </div>
            </div>
        </div>
    </div>
    <div v-if="cases.length" class="text-center mt-8">
        <NuxtLink to="/cases"
            class="inline-flex items-center px-8 py-3 bg-gradient-to-r from-primary-500 to-primary-600 text-white rounded-lg hover:shadow-glow transition-all duration-300">
            查看更多案例
            <i class="fas fa-arrow-right ml-2"></i>
        </NuxtLink>
    </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { Cases } from '~/types/cases'

const api = useApi()
const cases = ref<Cases[]>([])

const fetchCases = async () => {
    try {
        const res = await api.cases.getList({ pageNum: 1, pageSize: 3 })
        if (res.code === 200) {
            cases.value = res.rows
        } else {
            console.error('获取成功案例失败:', res.msg)
        }
    } catch (error) {
        console.error('获取成功案例失败:', error)
    }
}

onMounted(fetchCases)
</script>