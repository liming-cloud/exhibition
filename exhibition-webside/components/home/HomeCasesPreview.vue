<template>
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div v-for="case_ in cases.slice(0, 3)" :key="case_.id"
            class="group relative overflow-hidden rounded-xl shadow-elegant hover:shadow-modern transition-all duration-500">
            <img :src="case_.coverUrl" :alt="case_.title"
                class="w-full h-64 object-cover group-hover:scale-110 transition-transform duration-500" />
            <div
                class="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                <div class="absolute bottom-4 left-4 text-white">
                    <h4 class="font-bold text-lg">{{ case_.title }}</h4>
                </div>
            </div>
        </div>
    </div>
    <div class="text-center mt-8">
        <NuxtLink to="/cases"
            class="inline-flex items-center px-8 py-3 bg-gradient-to-r from-primary-500 to-primary-600 text-white rounded-lg hover:shadow-glow transition-all duration-300">
            查看更多案例
            <i class="fas fa-arrow-right ml-2"></i>
        </NuxtLink>
    </div>
</template>

<script setup lang="ts">
interface Case {
    id: number
    title: string
    coverUrl: string
    description?: string
    createTime?: string
}

const cases = ref<Case[]>([])

const fetchCases = async () => {
    try {
        // 假设有一个获取案例的API
        const { data } = await $fetch<{ data: Case[] }>('/api/cases/list?pageNum=1&pageSize=3')
        cases.value = data || []
    } catch (error) {
        console.error('获取成功案例失败:', error)
        // 设置一些默认的案例数据作为示例
        cases.value = [
            {
                id: 1,
                title: '德国汉诺威工业博览会',
                coverUrl: '/images/cases/hannover.jpg'
            },
            {
                id: 2,
                title: '美国拉斯维加斯消费电子展',
                coverUrl: '/images/cases/ces.jpg'
            },
            {
                id: 3,
                title: '广州国际家具博览会',
                coverUrl: '/images/cases/furniture.jpg'
            }
        ]
    }
}

onMounted(fetchCases)
</script>