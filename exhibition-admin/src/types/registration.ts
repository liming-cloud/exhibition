// 联系进度枚举
export enum ContactProgress {
    UNCONTACTED = 'UNCONTACTED',
    CONTACTED = 'CONTACTED',
    REPLIED = 'REPLIED',
    CLOSED = 'CLOSED',
    CANCELLED = 'CANCELLED'
}

// 联系进度显示映射
export const ContactProgressMap = {
    [ContactProgress.UNCONTACTED]: '未联系',
    [ContactProgress.CONTACTED]: '已联系',
    [ContactProgress.REPLIED]: '已回复',
    [ContactProgress.CLOSED]: '已成交',
    [ContactProgress.CANCELLED]: '已取消'
}

// 联系进度标签类型映射
export const ContactProgressTagMap = {
    [ContactProgress.UNCONTACTED]: 'info',
    [ContactProgress.CONTACTED]: 'warning',
    [ContactProgress.REPLIED]: 'primary',
    [ContactProgress.CLOSED]: 'success',
    [ContactProgress.CANCELLED]: 'danger'
}

// 报名接口类型定义
export interface Registration {
    id: number
    exhibitionId: number
    exhibitionName: string
    companyOrPersonalName: string
    contactName: string
    contactPhone: string
    contactProgress: ContactProgress
    createTime: string
    updateTime: string
}

export interface RegistrationQuery {
    pageNum: number
    pageSize: number
    exhibitionName?: string
    contactName?: string
    contactProgress?: ContactProgress
} 