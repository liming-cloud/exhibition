import request from '@/utils/request'
import type { ContactProgress, Registration, RegistrationQuery } from '@/types/registration'
import type { ApiResponse, TableDataInfo } from '@/types/common'

// 获取报名列表
export function listRegistration(query: RegistrationQuery) {
    return request<TableDataInfo<Registration>>({
        url: '/exhibition/registration/page',
        method: 'get',
        params: query
    })
}

// 更新联系进度
export function updateProgress(id: number, progress: ContactProgress) {
    return request<ApiResponse<void>>({
        url: `/exhibition/registration/update-progress/${id}`,
        method: 'put',
        params: { progress }
    })
}

// 获取报名详情
export function getRegistration(id: number) {
    return request<ApiResponse<Registration>>({
        url: `/exhibition/registration/detail/${id}`,
        method: 'get'
    })
}

// 删除报名记录
export function delRegistration(id: number) {
    return request<ApiResponse<void>>({
        url: `/exhibition/registration/delete/${id}`,
        method: 'delete'
    })
} 