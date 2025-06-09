import request from '@/utils/request'
import type {Subsidies, SubsidiesQuery} from '@/types/subsidies'
import type {ApiResponse, TableDataInfo} from '@/types/common'


/** 查询补贴信息列表 */
export function listSubsidies(query: SubsidiesQuery) {
  return request<TableDataInfo<Subsidies>>({
    url: '/subsidies/page',
    method: 'get',
    params: query
  })
}

/** 查询补贴信息详细 */
export function getSubsidies(id: number) {
  return request<ApiResponse<Subsidies>>({
    url: `/subsidies/detail/${id}`,
    method: 'get'
  })
}

/** 新增补贴信息 */
export function saveCarousel(data: Subsidies) {
  return request<ApiResponse<void>>({
    url: '/subsidies/save',
    method: 'post',
    data
  })
}


/** 删除补贴信息 */
export function deleteSubsidies(id: number) {
  return request<ApiResponse<void>>({
    url: `/subsidies/delete/${id}`,
    method: 'delete'
  })
} 