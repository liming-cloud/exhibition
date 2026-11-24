import { ref, type Ref } from 'vue'

interface HttpOptions {
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE'
  params?: Record<string, any>
  body?: Record<string, any>
}

// 请求统一走 /api（跟随 app.baseURL），由 server/routes/api 转发到后端；SSR 时 $fetch 直接在服务端内部调用该路由
export const useHttp = async <T = any>(url: string, opts: HttpOptions = {}) => {
  const data = await $fetch<T>(url, {
    baseURL: assetUrl('/api'),
    method: opts.method,
    query: opts.params,
    body: opts.body
  })
  return { data: ref(data) as Ref<T> }
}
