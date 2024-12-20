<template>
  <div class="max-w-7xl mx-auto mt-12 px-4">
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
          <ExhibitionCard
              v-for="exhibition in filteredExhibitions"
              :key="exhibition.id"
              :exhibition="exhibition"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import ExhibitionCard from './ExhibitionCard.vue'
import exhibitionsData from '~/content/exhibitions.json'

// 大洲选项
const continents = ['澳洲','亚洲','欧美','非洲','中东','intersoLar','setex','the battery show','the solar show']
const years = ['2026', '2025', '2024']

const exhibitions = ref([])
const filteredExhibitions = ref([])

const selectedContinent = ref('')
const selectedYear = ref('2025')

onMounted(() => {
  // 初始化时展示默认数据（按默认年份和未选定大洲）
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