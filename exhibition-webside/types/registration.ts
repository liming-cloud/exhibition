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

// 展会报名接口
export interface ExhibitionRegistration {
    id?: number
    exhibitionId: number
    companyOrPersonalName: string
    contactName: string
    contactPhone: string
    contactProgress?: ContactProgress
    createTime?: string
    updateTime?: string
    exhibitionName?: string
} 