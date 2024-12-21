import request from '@/utils/request'
import type { Carousel, CarouselQuery } from '@/types/carousel'
import type { ApiResponse, TableDataInfo } from '@/types/common'

/** 查询轮播图列表 */
export function listCarousel(query: CarouselQuery) {
  return request<TableDataInfo<Carousel>>({
    url: '/carousel/page',
    method: 'get',
    params: query
  })
}

/** 查询轮播图详细 */
export function getCarousel(id: number) {
  return request<ApiResponse<Carousel>>({
    url: `/carousel/detail/${id}`,
    method: 'get'
  })
}

/** 新增或修改轮播图 */
export function saveCarousel(data: Carousel) {
  return request<ApiResponse<void>>({
    url: '/carousel/save',
    method: 'post',
    data
  })
}

/** 删除轮播图 */
export function delCarousel(id: number) {
  return request<ApiResponse<void>>({
    url: `/carousel/delete/${id}`,
    method: 'delete'
  })
} 