import request from '@/utils/request'
import type { Exhibition, ExhibitionQuery, GlobalArea } from '@/types/exhibition'
import type { ApiResponse, TableDataInfo } from '@/types/common'

/** 查询展会列表 */
export function listExhibition(query: ExhibitionQuery) {
  return request<TableDataInfo<Exhibition>>({
    url: '/exhibition/page',
    method: 'get',
    params: query
  })
}

/** 查询展会详细 */
export function getExhibition(id: number) {
  return request<ApiResponse<Exhibition>>({
    url: `/exhibition/detail/${id}`,
    method: 'get'
  })
}

/** 新增或修改展会 */
export function saveExhibition(data: Exhibition) {
  return request<ApiResponse<void>>({
    url: '/exhibition/save',
    method: 'post',
    data
  })
}

/** 删除展会 */
export function delExhibition(id: number) {
  return request<ApiResponse<void>>({
    url: `/exhibition/delete/${id}`,
    method: 'delete'
  })
}

/** 获取全球区域列表 */
export function findGlobalArea() {
  return request<ApiResponse<GlobalArea[]>>({
    url: '/exhibition/area',
    method: 'get'
  })
} 