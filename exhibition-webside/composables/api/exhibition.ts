import type { CountryStat, Exhibition, ExhibitionQuery } from '~/types/exhibition'
import type { ApiResponse, TableDataInfo } from '~/types/common'

export const useExhibitionApi = () => {
  return {
    /**
     * 获取展会列表（不含正文），筛选、排序、分页都在后端
     */
    getList: async (params: ExhibitionQuery) => {
      const { data } = await useHttp<TableDataInfo<Exhibition>>('/exhibition/page', {
        method: 'GET',
        params
      })
      return data.value || { total: 0, rows: [], code: 0, msg: '' }
    },

    /**
     * 获取展会详情
     */
    getDetail: async (id: number) => {
      const res = await useHttp<ApiResponse<Exhibition>>(`/exhibition/detail/${id}`, {
        method: 'GET'
      }).catch(ignoreNotFound)
      return res?.data.value?.data || null
    },

    /**
     * 有展会的国家及展会数量
     */
    getCountries: async () => {
      const { data } = await useHttp<ApiResponse<CountryStat[]>>('/exhibition/countries', {
        method: 'GET'
      })
      return data.value?.data || []
    },

    /**
     * 有展会的年份，升序
     */
    getYears: async () => {
      const { data } = await useHttp<ApiResponse<number[]>>('/exhibition/years', {
        method: 'GET'
      })
      return data.value?.data || []
    }
  }
}
