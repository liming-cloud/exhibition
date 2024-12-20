import { get, post, put, del } from '@/utils/request'
import type { ApiResponse, PageResponse } from '@/types/response'
import type { Carousel, CarouselQuery } from '@/types/carousel'

// 获取轮播图列表
export function getCarouselList(params: CarouselQuery) {
  return get<PageResponse<Carousel>>('/carousel/list', params)
}

// 获取轮播图详情
export function getCarouselDetail(id: number) {
  return get<ApiResponse<Carousel>>(`/carousel/${id}`)
}

// 新增轮播图
export function addCarousel(data: Carousel) {
  return post<ApiResponse<void>>('/carousel', data)
}

// 更新轮播图
export function updateCarousel(data: Carousel) {
  return put<ApiResponse<void>>('/carousel', data)
}

// 删除轮播图
export function deleteCarousel(id: number) {
  return del<ApiResponse<void>>(`/carousel/${id}`)
}

// 更新轮播图状态
export function updateCarouselStatus(id: number, status: number) {
  return put<ApiResponse<void>>(`/carousel/status/${id}/${status}`)
} 