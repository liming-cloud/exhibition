<!-- components/GlobalExhibitions.vue -->
<template>
  <div class="max-w-7xl mx-auto mt-12 px-4">
    <h2 class="text-2xl font-bold mb-6">全球展会分布</h2>
    <div class="bg-white rounded-lg shadow p-6">
      <div id="map" class="h-[400px]"></div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import * as echarts from 'echarts'

onMounted(async () => {
  // 加载地图数据
  const response = await fetch('/map/world.json')
  const mapData = await response.json()
  echarts.registerMap('world', mapData)

  const map = echarts.init(document.getElementById('map'))
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}'
    },
    series: [
      {
        type: 'map',
        map: 'world',
        roam: true,
        label: {
          show: false
        },
        itemStyle: {
          areaColor: '#eee',
          borderColor: '#999'
        },
        emphasis: {
          label: {
            show: true
          },
          itemStyle: {
            areaColor: '#4299e1'
          }
        }
      }
    ]
  }
  map.setOption(option)
  window.addEventListener('resize', () => {
    map.resize()
  })
})
</script>