/**
 * 联系信息接口
 */
export interface ContactInfo {
    /** 公司地址 */
    address: string
    /** 联系电话 */
    phone: string[]
    /** 联系人 */
    contactPerson: string
    /** 工作时间 */
    workingHours: string
    /** 节假日安排 */
    holidayArrangement: string
    /** 业务咨询邮箱 */
    email: string
    /** 公司简介 */
    companyProfile: string
} 