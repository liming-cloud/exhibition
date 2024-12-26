import * as echarts from 'echarts'

export default defineNuxtPlugin(async () => {
  try {
    const worldJson = await import('~/public/json/world.json')
    echarts.registerMap('world', worldJson.default as any)
  } catch (error) {
    console.error('Failed to load world map data:', error)
  }
}) 