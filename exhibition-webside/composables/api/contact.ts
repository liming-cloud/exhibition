import type { ApiResponse } from '~/types/common'
import type { ContactInfo } from '~/types/contact'
import { useHttp } from '~/composables/useHttp'

/**
 * 联系我们 API
 */
export function useContactApi() {
    return {
        /**
         * 获取联系信息
         */
        getInfo: async () => {
            const { data } = await useHttp<ApiResponse<ContactInfo>>('/contact/info', {
                method: 'GET'
            })
            return data.value?.data
        }
    }
} 