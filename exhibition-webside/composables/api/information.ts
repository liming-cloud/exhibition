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

      const { data } = await useHttp<TableDataInfo<Information>>(
        `/information/page?${queryParams.toString()}`,
        {
          method: 'GET'
        }
      )
      return data.value || { total: 0, rows: [], code: 0, msg: '' }
    },

    /**
     * 获取市场资讯详情
     */
    getDetail: async (id: number): Promise<Information | undefined> => {
      const { data } = await useHttp<ApiResponse<Information>>(`/information/detail/${id}`, {
        method: 'GET'
      })
      return data.value?.data
    }
  }
}