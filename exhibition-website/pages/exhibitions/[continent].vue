<template>
  <div class="max-w-7xl mx-auto mt-12 px-4">
    <h1 class="text-3xl font-bold mb-8">展会信息 - {{ continentMap[continentName] }}</h1>
    <!-- 年份筛选 -->
    <div class="mb-6 flex items-center space-x-4">
      <span class="text-gray-600">选择年份：</span>
      <select
          v-model="selectedYear"
          @change="filterExhibitions"
          class="w-32 border border-gray-300 rounded-lg px-3 py-2"
      >
        <option v-for="year in years" :key="year" :value="year">{{ year }}</option>
      </select>
    </div>
    <!-- 展会卡片列表 -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div
          v-for="ex in filteredExhibitions"
          :key="ex.id"
          class="bg-white rounded-lg shadow-lg overflow-hidden hover:shadow-xl transition-shadow"
      >
        <img
            :src="ex.image"
            :alt="ex.title"
            class="w-full h-48 object-cover object-top"
        />
        <div class="p-4">
          <h3 class="text-lg font-semibold mb-2">{{ ex.title }}</h3>
          <div class="flex items-center text-gray-600 mb-2">
            <i class="fas fa-map-marker-alt"></i>
            <span class="ml-1">{{ ex.location }}</span>
          </div>
          <div class="flex items-center text-gray-600 mb-3">
            <i class="fas fa-calendar"></i>
            <span class="ml-1">{{ ex.date }}</span>
          </div>
          <p class="text-gray-600 text-sm line-clamp-2">{{ ex.description }}</p>
          <NuxtLink
              :to="`/exhibitions/detail/${ex.id}`"
              class="mt-4 w-full block bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition-colors text-center"
          >
            查看详情
          </NuxtLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from '#app'
import exhibitionsData from '~/content/exhibitions.json'

const route = useRoute()
const continentName = route.params.continent // 从URL获取大洲参数

const continentMap = {
  asia: '亚洲',
  europe: '欧美',
  africa: '非洲',
  australia: '澳洲',
  middleeast: '中东',
  intersolar: 'intersolar系列',
  setex: 'setex系列',
  battery: 'the battery show系列',
  solarshow: 'the solar show系列'
}

const exhibitions = ref([])
const filteredExhibitions = ref([])
const selectedYear = ref('2025')
const years = ['2025','2024','2023']

onMounted(() => {
  // 过滤出属于该大洲的展会
  exhibitions.value = exhibitionsData.filter(e => e.continent === continentName)
  filterExhibitions()
})

function filterExhibitions() {
  let result = exhibitions.value
  if (selectedYear.value) {
    result = result.filter(e => e.year === selectedYear.value)
  }
  filteredExhibitions.value = result
  console.log(filteredExhibitions.value)
}
</script>