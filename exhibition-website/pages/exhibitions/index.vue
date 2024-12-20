<template>
  <div class="max-w-7xl mx-auto mt-12 px-4">
    <h1 class="text-3xl font-bold mb-8">展会信息</h1>
    <div class="flex space-x-8">
      <!-- 左侧按大洲筛选 -->
      <div class="w-48 bg-white rounded-lg shadow p-4 h-fit">
        <h3 class="text-lg font-semibold mb-4">按地区筛选</h3>
        <div class="space-y-2">
          <div
              v-for="cont in continents"
              :key="cont"
              @click="filterByContinent(cont)"
              :class="[
              'p-2 rounded cursor-pointer',
              selectedContinent === cont ? 'bg-blue-100 text-blue-600' : 'hover:bg-gray-100'
            ]"
          >
            {{ cont }}
          </div>
        </div>
      </div>

      <div class="flex-1">
        <!-- 顶部选择年份 -->
        <div class="mb-6 flex items-center space-x-4">
          <span class="text-gray-600">选择年份：</span>
          <select
              v-model="selectedYear"
              @change="fetchExhibitions"
              class="w-32 border border-gray-300 rounded-lg px-3 py-2"
          >
            <option v-for="year in years" :key="year" :value="year">{{ year }}</option>
          </select>
        </div>

        <!-- 展会卡片列表 -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div
              v-for="exhibition in filteredExhibitions"
              :key="exhibition.id"
              class="bg-white rounded-lg shadow-lg overflow-hidden hover:shadow-xl transition-shadow"
          >
            <img
                :src="exhibition.image"
                :alt="exhibition.title"
                class="w-full h-48 object-cover object-top"
            />
            <div class="p-4">
              <h3 class="text-lg font-semibold mb-2">{{ exhibition.title }}</h3>
              <div class="flex items-center text-gray-600 mb-2">
                <i class="fas fa-map-marker-alt"></i>
                <span class="ml-1">{{ exhibition.location }}</span>
              </div>
              <div class="flex items-center text-gray-600 mb-3">
                <i class="fas fa-calendar"></i>
                <span class="ml-1">{{ exhibition.date }}</span>
              </div>
              <p class="text-gray-600 text-sm line-clamp-2">{{ exhibition.description }}</p>
              <button class="mt-4 w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition-colors">
                查看详情
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import exhibitionsData from '~/content/exhibitions.json'

const continents = ['澳洲','亚洲','欧美','非洲','中东','intersoLar','setex','the battery show','the solar show']
const years = ['2025', '2024', '2023']

const exhibitions = ref([])
const filteredExhibitions = ref([])

const selectedContinent = ref('')
const selectedYear = ref('2025')

onMounted(() => {
  exhibitions.value = exhibitionsData
  fetchExhibitions()
})

function fetchExhibitions() {
  let result = exhibitions.value
  if (selectedContinent.value) {
    result = result.filter(e => e.continent === selectedContinent.value)
  }
  if (selectedYear.value) {
    result = result.filter(e => e.year === selectedYear.value)
  }
  filteredExhibitions.value = result
}

function filterByContinent(continent) {
  selectedContinent.value = continent
  fetchExhibitions()
}
</script>