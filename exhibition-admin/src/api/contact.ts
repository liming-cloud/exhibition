import { http } from '@/utils/http'

// 联系信息接口类型定义
export interface ContactInfo {
    id?: number
    address: string
    phone: string
    mobile: string
    contactPerson: string
    createTime?: string
    updateTime?: string
}

// 获取联系信息
export function getContactInfo() {
    return http.get<ContactInfo>('/contact/info')
}

// 更新联系信息
export function updateContactInfo(data: ContactInfo) {
    return http.put('/contact/update', data)
} 