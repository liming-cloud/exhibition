import request from '@/utils/request'
import type { ApiResponse } from '@/types/common'

// 联系信息接口类型定义
export interface ContactInfo {
    id?: number
    address: string
    phone: string[]
    contactPerson: string
    workingHours: string
    holidayArrangement: string
    email: string
    companyProfile: string
    createTime?: string
    updateTime?: string
}

// 获取联系信息
export function getContactInfo() {
    return request<ApiResponse<ContactInfo>>({
        url: '/contact/info',
        method: 'get'
    })
}

// 更新联系信息
export function updateContactInfo(data: ContactInfo) {
    return request<ApiResponse<void>>({
        url: '/contact/update',
        method: 'put',
        data
    })
} 