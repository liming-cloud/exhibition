import type { Cases, CasesQuery } from '~/types/cases'
import type { ApiResponse, TableDataInfo } from '~/types/common'

export const useCasesApi = () => {
  return {
    /**
     * 获取项目案例列表
     */
    getList: async (params: CasesQuery): Promise<TableDataInfo<Cases>> => {
      const queryParams = new URLSearchParams()
      if (params.pageSize) queryParams.append('pageSize', params.pageSize.toString())
      if (params.pageNum) queryParams.append('pageNum', params.pageNum.toString())
      if (params.title) queryParams.append('title', params.title)

      const { data } = await useHttp<TableDataInfo<Cases>>(
        `/cases/page?${queryParams.toString()}`,
        {
          method: 'GET'
        }
      )
      return data.value || { total: 0, rows: [], code: 0, msg: '' }
    },

    /**
     * 获取项目案例详情
     */
    getDetail: async (id: number): Promise<Cases | undefined> => {
      const res = await useHttp<ApiResponse<Cases>>(`/cases/detail/${id}`, {
        method: 'GET'
      }).catch(ignoreNotFound)
      return res?.data.value?.data
    }
  }
}