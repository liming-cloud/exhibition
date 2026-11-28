import type { Subsidies, SubsidiesQuery } from '~/types/subsidies'
import type { ApiResponse, TableDataInfo } from '~/types/common'

export const useSubsidiesApi = () => {
  return {
    /**
     * 获取补贴政策列表
     */
    getList: async (params: SubsidiesQuery): Promise<TableDataInfo<Subsidies>> => {
      const queryParams = new URLSearchParams()
      if (params.pageSize) queryParams.append('pageSize', params.pageSize.toString())
      if (params.pageNum) queryParams.append('pageNum', params.pageNum.toString())
      if (params.title) queryParams.append('title', params.title)

      return useHttp<TableDataInfo<Subsidies>>(
        `/subsidies/page?${queryParams.toString()}`,
        {
          method: 'GET'
        }
      )
    },

    /**
     * 获取补贴政策详情
     */
    getDetail: async (id: number): Promise<Subsidies | undefined> => {
      const res = await useHttp<ApiResponse<Subsidies>>(`/subsidies/detail/${id}`, {
        method: 'GET'
      }).catch(ignoreNotFound)
      return res?.data
    }
  }
}