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

// 后端查不到记录时返回 HTTP 404；详情接口用它把 404 转成空结果，由页面显示 404，其他错误照常抛出
export const ignoreNotFound = (error: unknown) => {
  if ((error as { statusCode?: number })?.statusCode === 404) return undefined
  throw error
}
