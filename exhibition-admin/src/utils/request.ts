import type {AxiosInstance, AxiosRequestConfig, AxiosResponse} from 'axios'
import axios from 'axios'
import {ElMessage} from 'element-plus'

/** 获取图片完整URL */
export function getImageUrl(path: string) {
  if (!path) return ''
  if (path.startsWith('http') || path.startsWith('https')) {
    return path
  }
  return `/api/file/preview?path=${encodeURIComponent(path)}`
}

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 在发送请求之前做些什么
    return config
  },
  (error) => {
    // 对请求错误做些什么
    console.log(error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data

    // 如果返回的状态码不是200，说明接口有问题，把错误信息显示出来
    if (res.code !== 200) {
      ElMessage({
        message: res.msg || '系统出错',
        type: 'error',
        duration: 5 * 1000
      })

      return Promise.reject(new Error(res.msg || '系统出错'))
    } else {
      return res
    }
  },
  (error) => {
    console.log('err' + error)
    ElMessage({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

const request = <T = any>(config: AxiosRequestConfig): Promise<T> => {
  return service(config) as unknown as Promise<T>
}

export default request 