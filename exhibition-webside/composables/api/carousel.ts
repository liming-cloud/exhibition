import type { Carousel, CarouselQuery } from '~/types/carousel'
import type { ApiResponse, TableDataInfo } from '~/types/common'

export const useCarouselApi = () => {
  return {
    /**
     * 获取轮播图列表
     */
    getList: async (params: CarouselQuery) => {
      const queryParams = new URLSearchParams()
      if (params.pageSize) queryParams.append('pageSize', params.pageSize.toString())
      if (params.pageNum) queryParams.append('pageNum', params.pageNum.toString())
      if (params.title) queryParams.append('title', params.title)
      if (params.status !== undefined) queryParams.append('status', params.status.toString())

      const { data } = await useHttp<TableDataInfo<Carousel>>(
        `/carousel/page?${queryParams.toString()}`,
        {
          method: 'GET'
        }
      )
      return data.value || { total: 0, rows: [], code: 0, msg: '' }
    },

    /**
     * 获取轮播图详情
     */
    getDetail: async (id: number) => {
      const { data } = await useHttp<ApiResponse<Carousel>>(`/carousel/detail/${id}`, {
        method: 'GET'
      })
      return data.value?.data
    }
  }
}