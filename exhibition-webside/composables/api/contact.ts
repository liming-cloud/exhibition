import type { ApiResponse } from '~/types/common'
import type { Consultant, ContactInfo } from '~/types/contact'
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
            const res = await useHttp<ApiResponse<ContactInfo>>('/contact/info', {
                method: 'GET'
            })
            return res.data
        },

        /**
         * 顾问联系人（后端由 #5 实现；接口不可用时返回空列表，页面隐藏顾问区）
         */
        getConsultants: async () => {
            try {
                const res = await useHttp<ApiResponse<Consultant[]>>('/contact/consultants', {
                    method: 'GET'
                })
                return [...(res.data ?? [])].sort((a, b) => a.sort - b.sort)
            } catch {
                return []
            }
        }
    }
} 