import request from '@/utils/request'
import type { Cases, CasesQuery } from '@/types/cases'
import type { ApiResponse, TableDataInfo } from '@/types/common'


/** 查询案例列表 */
export function listCases(query: CasesQuery) {
  return request<TableDataInfo<Cases>>({
    url: '/cases/page',
    method: 'get',
    params: query
  })
}

/** 查询案例详细 */
export function getCases(id: number) {
  return request<ApiResponse<Cases>>({
    url: `/cases/detail/${id}`,
    method: 'get'
  })
}

/** 案例表单字段（新增、修改只提交这些） */
export type CasesForm = Pick<Cases, 'title' | 'content' | 'coverUrl'>

/** 新增案例 */
export function addCases(data: CasesForm) {
  return request<ApiResponse<void>>({
    url: '/cases/add',
    method: 'post',
    data
  })
}

/** 修改案例 */
export function updateCases(id: number, data: CasesForm) {
  return request<ApiResponse<void>>({
    url: `/cases/update/${id}`,
    method: 'put',
    data
  })
}

/** 删除案例 */
export function deleteCases(id: number) {
  return request<ApiResponse<void>>({
    url: `/cases/delete/${id}`,
    method: 'delete'
  })
} 