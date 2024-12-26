import type { UseFetchOptions } from 'nuxt/app'
import { ref, type Ref } from 'vue'

export const useHttp = <T = any>(url: string, opts: UseFetchOptions<T> = {}) => {
  const config = useRuntimeConfig()
  const baseURL = '/api'
  
  return new Promise<{ data: Ref<T> }>(async (resolve, reject) => {
    try {
      console.log('发送请求:', baseURL + url)
      const response = await fetch(baseURL + url, {
        method: (opts.method || 'GET') as string,
        headers: {
          'Content-Type': 'application/json',
          'Cache-Control': 'no-cache, no-store, must-revalidate',
          'Pragma': 'no-cache',
          'Expires': '0',
          ...(opts.headers as Record<string, string>)
        },
        body: opts.body ? JSON.stringify(opts.body) : undefined,
        cache: 'no-cache'
      })

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }

      const jsonData = await response.json()
      console.log('请求响应:', jsonData)
      
      resolve({
        data: ref(jsonData) as Ref<T>
      })
    } catch (error) {
      console.error('请求失败:', error)
      reject(error)
    }
  })
} 