// 官网所有 /api 请求（浏览器与 SSR）都经此转发到后端，后端地址由 NUXT_API_BASE 配置
export default defineEventHandler((event) => {
  const { apiBase } = useRuntimeConfig(event)
  return proxyRequest(event, apiBase + event.path.replace(/^\/api/, ''))
})
