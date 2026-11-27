import request from '@/utils/request'
import type {Carousel, CarouselQuery} from '@/types/carousel'
import type {ApiResponse, TableDataInfo} from '@/types/common'

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

/** 轮播图表单字段（新增、修改只提交这些） */
export type CarouselForm = Pick<Carousel, 'title' | 'imageUrl' | 'link' | 'sort' | 'status'>

/** 新增轮播图 */
export function addCarousel(data: CarouselForm) {
  return request<ApiResponse<void>>({
    url: '/carousel/add',
    method: 'post',
    data
  })
}

/** 修改轮播图 */
export function updateCarousel(id: number, data: CarouselForm) {
  return request<ApiResponse<void>>({
    url: `/carousel/update/${id}`,
    method: 'put',
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