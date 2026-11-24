// 原型 mock：构建时设置 NUXT_PUBLIC_MOCK=1 才会注册（见 nuxt.config.ts），SSR 与浏览器的 /api 请求在此返回模拟数据
import { mockApi, MockNotFound } from '../../mock/api'

export default defineEventHandler(event => {
  if (!event.path.startsWith('/api/')) return
  const path = event.path.replace(/^\/api/, '').split('?')[0]
  try {
    return mockApi(path, getQuery(event))
  } catch (e) {
    if (e instanceof MockNotFound) throw createError({ statusCode: 404, statusMessage: e.message })
    throw e
  }
})
