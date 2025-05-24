import { http } from '@/utils/http'
import type { Registration, RegistrationQuery, ContactProgress } from '@/types/registration'

// 获取报名列表
export function listRegistration(query: RegistrationQuery) {
    return http.get<{
        rows: Registration[]
        total: number
    }>('/exhibition/registration/page', { params: query })
}

// 更新联系进度
export function updateProgress(id: number, progress: ContactProgress) {
    return http.put(`/exhibition/registration/update-progress/${id}`, null, {
        params: { progress }
    })
}

// 获取报名详情
export function getRegistration(id: number) {
    return http.get<Registration>(`/exhibition/registration/detail/${id}`)
}

// 删除报名记录
export function delRegistration(id: number) {
    return http.delete(`/exhibition/registration/delete/${id}`)
} 