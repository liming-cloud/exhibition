// 原型 mock（浏览器端）：拦截 /api 请求直接返回模拟数据，静态部署（GitHub Pages）没有服务端时也能翻页、筛选、提交报名
import { mockApi, MockNotFound } from './api'

export default defineNuxtPlugin({
  enforce: 'pre',
  setup() {
    const apiPrefix = assetUrl('/api/')
    const realFetch = window.fetch.bind(window)
    window.fetch = (input, init) => {
      const url = new URL(input instanceof Request ? input.url : String(input), location.origin)
      if (url.origin !== location.origin || !url.pathname.startsWith(apiPrefix)) {
        return realFetch(input, init)
      }
      const path = '/' + url.pathname.slice(apiPrefix.length)
      try {
        return Promise.resolve(Response.json(mockApi(path, Object.fromEntries(url.searchParams))))
      } catch (e) {
        if (e instanceof MockNotFound)
          return Promise.resolve(new Response(e.message, { status: 404 }))
        throw e
      }
    }
  }
})
