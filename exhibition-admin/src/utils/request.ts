import axios from 'axios'
import type { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import type { ApiResponse, PageResponse } from '@/types/response'

// 创建 axios 实例
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API, // 基础请求地址
  timeout: 15000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const { data } = response
    
    // 检查响应状态码
    if (data.code !== 200) {
      ElMessage.error(data.msg || '请求失败')
      return Promise.reject(new Error(data.msg || '请求失败'))
    }

    // 判断是否为分页响应
    if ('rows' in data && 'total' in data) {
      return {
        rows: data.rows,
        total: data.total
      }
    }

    // 普通响应直接返回data字段
    return data.data
  },
  (error) => {
    ElMessage.error(error.message || '请求失败')
    return Promise.reject(error)
  }
)

// 封装GET请求
export function get<T>(url: string, params?: any): Promise<T> {
  return service.get(url, { params })
}

// 封装POST请求
export function post<T>(url: string, data?: any): Promise<T> {
  return service.post(url, data)
}

// 封装PUT请求
export function put<T>(url: string, data?: any): Promise<T> {
  return service.put(url, data)
}

// 封装DELETE请求
export function del<T>(url: string): Promise<T> {
  return service.delete(url)
}

export default service 