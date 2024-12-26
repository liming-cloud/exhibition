<template>
  <div class="world-map-container">
    <div ref="chartContainer" class="world-map"></div>
  </div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts'
import { ref, onMounted, onUnmounted } from 'vue'
import type { Exhibition } from '~/types/exhibition'
import { countryCoordinates } from '~/constants/countryCoordinates'

const api = useApi()
const chartContainer = ref<HTMLElement | null>(null)
let chart: echarts.ECharts | null = null

const initChart = () => {
  if (!chartContainer.value) return
  
  chart = echarts.init(chartContainer.value)
  
  const option = {
    backgroundColor: '#F5F7FA',
    tooltip: {
      trigger: 'item',
      formatter: (params: any) => {
        if (params.data) {
          return `${params.name}<br/>展会数量：${params.value[2]}`
        }
        return ''
      },
      backgroundColor: 'rgba(255, 255, 255, 0.9)',
      borderColor: '#409EFF',
      borderWidth: 1,
      padding: [8, 12],
      textStyle: {
        color: '#606266'
      },
      extraCssText: 'transition: opacity 0.3s ease-in-out;'
    },
    geo: {
      map: 'world',
      roam: false,
      zoom: 1.2,
      center: [10, 20],
      itemStyle: {
        areaColor: '#DCDFE6',
        borderColor: '#FFFFFF',
        borderWidth: 1
      },
      emphasis: {
        itemStyle: {
          areaColor: '#C0C4CC',
          transition: 'all 0.3s'
        },
        label: {
          show: false
        }
      },
      regions: [{
        name: 'Antarctica',
        selected: false
      }]
    },
    series: [{
      name: '展会数量',
      type: 'scatter',
      coordinateSystem: 'geo',
      data: [],
      symbol: 'circle',
      symbolSize: (val: any) => {
        return Math.max(val[2] * 5, 15)
      },
      itemStyle: {
        color: '#409EFF',
        opacity: 0.8,
        borderColor: '#fff',
        borderWidth: 1,
        shadowColor: 'rgba(0, 0, 0, 0.2)',
        shadowBlur: 10
      },
      emphasis: {
        itemStyle: {
          color: '#409EFF',
          opacity: 1,
          borderWidth: 2,
          shadowBlur: 20,
          transition: 'all 0.3s'
        },
        label: {
          show: true,
          formatter: (params: any) => `${params.name}: ${params.value[2]}`,
          position: 'top',
          backgroundColor: 'rgba(255, 255, 255, 0.9)',
          padding: [4, 8],
          borderRadius: 4,
          color: '#303133',
          fontSize: 12
        }
      },
      label: {
        show: true,
        formatter: (params: any) => params.value[2],
        position: 'inside',
        color: '#fff',
        fontSize: 12,
        fontWeight: 'bold',
        textShadowColor: 'rgba(0, 0, 0, 0.5)',
        textShadowBlur: 2
      },
      animation: true,
      animationDuration: 1000,
      animationEasing: 'cubicOut',
      animationDelay: (idx: number) => idx * 50
    }]
  }
  
  chart.setOption(option)
  
  chart.on('click', (params: any) => {
    if (params.componentType === 'series' && params.data) {
      const { countryId } = params.data
      if (typeof countryId === 'number') {
        navigateTo(`/exhibitions?country=${countryId}`)
      }
    }
  })
}

const updateChartData = async () => {
  if (!chart) return
  
  try {
    const res = await api.exhibition.getList({
      pageNum: 1,
      pageSize: 1000
    })
    
    if (res?.code === 200 && Array.isArray(res.rows)) {
      // 统计每个国家的展会数量和ID
      const countryStats = res.rows.reduce((acc: Record<string, { count: number; id: number }>, exhibition: Exhibition) => {
        const countryName = exhibition.countryName
        if (countryName && exhibition.country) {
          if (!acc[countryName]) {
            acc[countryName] = { count: 0, id: exhibition.country }
          }
          acc[countryName].count++
        }
        return acc
      }, {})
      
      // 转换为散点数据，使用具体的经纬度坐标
      const scatterData = Object.entries(countryStats).map(([name, data]) => {
        const country = countryCoordinates[name]
        if (!country) {
          console.warn(`未找到国家坐标: ${name}`)
          return null
        }
        return {
          name,
          countryId: data.id,
          value: [...country.coordinates, data.count]
        }
      }).filter(Boolean)
      
      chart.setOption({
        series: [{
          data: scatterData
        }]
      })
    }
  } catch (error) {
    console.error('获取展会数据失败:', error)
  }
}

onMounted(() => {
  initChart()
  updateChartData()
  
  let resizeTimer: NodeJS.Timeout | null = null
  const handleResize = () => {
    if (resizeTimer) clearTimeout(resizeTimer)
    resizeTimer = setTimeout(() => {
      chart?.resize()
    }, 100)
  }
  
  window.addEventListener('resize', handleResize)
  
  onUnmounted(() => {
    if (resizeTimer) clearTimeout(resizeTimer)
    window.removeEventListener('resize', handleResize)
    if (chart) {
      chart.dispose()
      chart = null
    }
  })
})
</script>

<style scoped>
.world-map-container {
  @apply flex flex-col items-center p-6 bg-white rounded-lg shadow-md w-full max-w-[1400px] mx-auto;
  animation: fadeIn 0.8s ease-in-out;
}

.world-map-title {
  @apply text-2xl font-bold text-gray-800 mb-6;
  animation: slideDown 0.8s ease-out;
}

.world-map {
  @apply w-full h-[500px] rounded-lg overflow-hidden;
  min-height: 400px;
  max-height: 700px;
  height: calc(100vh - 300px);
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.05);
  animation: scaleIn 0.8s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideDown {
  from {
    transform: translateY(-20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

@keyframes scaleIn {
  from {
    transform: scale(0.95);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}

@media (max-width: 1200px) {
  .world-map {
    height: calc(100vh - 250px);
  }
}

@media (max-width: 768px) {
  .world-map {
    height: calc(100vh - 200px);
  }
  
  .world-map-title {
    @apply text-xl mb-4;
  }
}
</style> 