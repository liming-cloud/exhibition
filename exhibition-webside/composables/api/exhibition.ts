import type { CountryStat, Exhibition, ExhibitionQuery, RegistrationSubmit } from '~/types/exhibition'
import type { ApiResponse, TableDataInfo } from '~/types/common'

export const useExhibitionApi = () => {
  return {
    /**
     * 获取展会列表（不含正文），筛选、排序、分页都在后端
     */
    getList: async (params: ExhibitionQuery) => {
      return useHttp<TableDataInfo<Exhibition>>('/exhibition/page', {
        method: 'GET',
        params
      })
    },

    /**
     * 获取展会详情
     */
    getDetail: async (id: number) => {
      const res = await useHttp<ApiResponse<Exhibition>>(`/exhibition/detail/${id}`, {
        method: 'GET'
      }).catch(ignoreNotFound)
      return res?.data ?? null
    },

    /**
     * 有展会的国家及展会数量
     */
    getCountries: async () => {
      const res = await useHttp<ApiResponse<CountryStat[]>>('/exhibition/countries', {
        method: 'GET'
      })
      return res.data ?? []
    },

    /**
     * 有展会的年份，升序
     */
    getYears: async () => {
      const res = await useHttp<ApiResponse<number[]>>('/exhibition/years', {
        method: 'GET'
      })
      return res.data ?? []
    },

    /**
     * 提交报名 / 咨询下一届，失败时抛出
     */
    submitRegistration: async (body: RegistrationSubmit) => {
      await useHttp('/exhibition/registration/submit', {
        method: 'POST',
        body
      })
    }
  }
}
