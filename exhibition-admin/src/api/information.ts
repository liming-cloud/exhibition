import request from '@/utils/request'
import type {Information, InformationQuery} from '@/types/information'
import type {ApiResponse, TableDataInfo} from '@/types/common'


/** 查询市场资讯列表 */
export function listInformation(query: InformationQuery) {
  return request<TableDataInfo<Information>>({
    url: '/information/page',
    method: 'get',
    params: query
  })
}

/** 查询市场资讯详细 */
export function getInformation(id: number) {
  return request<ApiResponse<Information>>({
    url: `/information/detail/${id}`,
    method: 'get'
  })
}

/** 市场资讯表单字段（新增、修改只提交这些） */
export type InformationForm = Pick<Information, 'title' | 'content' | 'coverUrl'>

/** 新增市场资讯 */
export function addInformation(data: InformationForm) {
  return request<ApiResponse<void>>({
    url: '/information/add',
    method: 'post',
    data
  })
}

/** 修改市场资讯 */
export function updateInformation(id: number, data: InformationForm) {
  return request<ApiResponse<void>>({
    url: `/information/update/${id}`,
    method: 'put',
    data
  })
}

/** 删除市场资讯 */
export function deleteInformation(id: number) {
  return request<ApiResponse<void>>({
    url: `/information/delete/${id}`,
    method: 'delete'
  })
} 