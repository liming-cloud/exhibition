import type { Information, InformationQuery } from '~/types/information'
import type { ApiResponse, TableDataInfo } from '~/types/common'

export const useInformationApi = () => {
  return {
    /**
     * 获取市场资讯列表
     */
    getList: async (params: InformationQuery): Promise<TableDataInfo<Information>> => {
      const queryParams = new URLSearchParams()
      if (params.pageSize) queryParams.append('pageSize', params.pageSize.toString())
      if (params.pageNum) queryParams.append('pageNum', params.pageNum.toString())
      if (params.title) queryParams.append('title', params.title)

      return useHttp<TableDataInfo<Information>>(
        `/information/page?${queryParams.toString()}`,
        {
          method: 'GET'
        }
      )
    },

    /**
     * 获取市场资讯详情
     */
    getDetail: async (id: number): Promise<Information | undefined> => {
      const res = await useHttp<ApiResponse<Information>>(`/information/detail/${id}`, {
        method: 'GET'
      }).catch(ignoreNotFound)
      return res?.data
    }
  }
}