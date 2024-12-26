import type { Exhibition, ExhibitionQuery, GlobalArea } from '~/types/exhibition'
import type { TableDataInfo, ApiResponse } from '~/types/common'

export const useExhibitionApi = () => {
  return {
    /**
     * 获取展会列表
     */
    getList: async (params: ExhibitionQuery) => {
      const { data } = await useHttp<TableDataInfo<Exhibition>>('/exhibition/page', {
        method: 'GET',
        params: {
          pageSize: params.pageSize,
          pageNum: params.pageNum,
          name: params.name,
          country: params.country
        }
      })
      return data.value || { total: 0, rows: [], code: 0, msg: '' }
    },

    /**
     * 获取展会详情
     */
    getDetail: async (id: number) => {
      const { data } = await useHttp<ApiResponse<Exhibition>>(`/exhibition/detail/${id}`, {
        method: 'GET'
      })
      return data.value?.data || null
    },

    /**
     * 获取全球区域列表
     */
    getGlobalArea: async () => {
      const { data } = await useHttp<ApiResponse<GlobalArea[]>>('/exhibition/area', {
        method: 'GET'
      })
      return data.value?.data || []
    }
  }
} 