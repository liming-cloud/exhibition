<template>
  <div ref="chartContainer" class="w-full h-[260px] sm:h-[360px] lg:h-[440px]"></div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts'
import type { Exhibition } from '~/types/exhibition'
import { countryCoordinates } from '~/constants/countryCoordinates'

// 展会数据由首页传入（与下方展会项目板块共用），点击国家通知父组件筛选
const props = defineProps<{ exhibitions: Exhibition[]; selected: number | null }>()
const emit = defineEmits<{ select: [countryId: number] }>()

const chartContainer = ref<HTMLElement | null>(null)
let chart: echarts.ECharts | null = null

// 主色取自 CSS 变量，保证地图与页面同色
const themeColor = (shade: number) => {
  const value = getComputedStyle(document.documentElement).getPropertyValue(
    `--color-primary-${shade}`
  )
  return `rgb(${value.trim().split(/\s+/).join(',')})`
}

const scatterData = computed(() => {
  const stats = new Map<string, { id: number; count: number }>()
  for (const e of props.exhibitions) {
    if (!e.countryName || !e.country) continue
    const item = stats.get(e.countryName) ?? { id: e.country, count: 0 }
    item.count++
    stats.set(e.countryName, item)
  }
  return [...stats.entries()]
    .filter(([name]) => countryCoordinates[name])
    .map(([name, { id, count }]) => ({
      name,
      countryId: id,
      value: [...countryCoordinates[name].coordinates, count]
    }))
})

// 颜色每次渲染时从当前配色读取；选中的国家用深色加粗边框
const render = () => {
  chart?.setOption({
    series: [
      {
        itemStyle: { color: themeColor(500) },
        data: scatterData.value.map(item =>
          item.countryId === props.selected
            ? { ...item, itemStyle: { color: themeColor(700), borderWidth: 3 } }
            : item
        )
      }
    ]
  })
}

onMounted(() => {
  if (!chartContainer.value) return
  chart = echarts.init(chartContainer.value)
  chart.setOption({
    tooltip: {
      trigger: 'item',
      formatter: (p: any) => (p.data ? `${p.name}：${p.data.value[2]} 场展会<br/>点击查看` : '')
    },
    geo: {
      map: 'world',
      roam: false,
      zoom: 1.15,
      center: [15, 22],
      silent: true,
      itemStyle: { areaColor: '#e5e7eb', borderColor: '#ffffff', borderWidth: 0.6 },
      regions: [{ name: 'Antarctica', itemStyle: { opacity: 0 } }]
    },
    series: [
      {
        type: 'scatter',
        coordinateSystem: 'geo',
        symbolSize: (val: number[]) => Math.min(14 + val[2] * 4, 34),
        itemStyle: { borderColor: '#fff', borderWidth: 1.5 },
        label: {
          show: true,
          formatter: (p: any) => p.data.value[2],
          color: '#fff',
          fontSize: 11,
          fontWeight: 'bold'
        },
        emphasis: { scale: 1.2 },
        data: []
      }
    ]
  })
  render()
  chart.on('click', (p: any) => {
    if (typeof p.data?.countryId === 'number') emit('select', p.data.countryId)
  })

  const onResize = () => chart?.resize()
  window.addEventListener('resize', onResize)
  onUnmounted(() => {
    window.removeEventListener('resize', onResize)
    chart?.dispose()
    chart = null
  })
})

watch([scatterData, () => props.selected], render)
</script>
