import request from '@/utils/request'
import type {Exhibition, ExhibitionQuery, GlobalArea} from '@/types/exhibition'
import type {ApiResponse, TableDataInfo} from '@/types/common'

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

/** 展会表单字段（新增、修改只提交这些） */
export type ExhibitionForm = Pick<Exhibition, 'name' | 'exhibitionType' | 'content' | 'location' | 'startDate' | 'endDate' | 'country' | 'sort'>

/** 新增展会 */
export function addExhibition(data: ExhibitionForm) {
  return request<ApiResponse<void>>({
    url: '/exhibition/add',
    method: 'post',
    data
  })
}

/** 修改展会 */
export function updateExhibition(id: number, data: ExhibitionForm) {
  return request<ApiResponse<void>>({
    url: `/exhibition/update/${id}`,
    method: 'put',
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