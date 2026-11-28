// 展会类型枚举
export enum ExhibitionType {
  ENERGY = 'ENERGY',
  ELEVATOR = 'ELEVATOR',
  MEDICAL = 'MEDICAL'
}

// 展会类型显示映射
export const ExhibitionTypeMap = {
  [ExhibitionType.ENERGY]: '能源展',
  [ExhibitionType.ELEVATOR]: '电梯展',
  [ExhibitionType.MEDICAL]: '医疗展'
}

export interface Exhibition {
  id: number
  name: string
  location: string
  startDate: string // YYYY-MM-DD
  endDate: string // YYYY-MM-DD，单日展会与 startDate 相同
  sort: number
  content?: string // 只有详情接口返回

  country: number
  countryName: string
  exhibitionType: ExhibitionType // 修改为枚举类型
  createTime?: string
  featured?: boolean // 新增约定：主推展会（Issue #5 后端实现前为空）
  coverUrl?: string // 新增约定：封面图（#5）
  summary?: string // 新增约定：一句话简介（#5）
}

/** 有展会的国家及展会数量（/exhibition/countries） */
export interface CountryStat {
  id: number
  continentCode: string
  continentName: string
  countryCode: string
  countryName: string
  /** 展会总数 */
  exhibitionCount: number
  /** 未结束的展会数 */
  upcomingCount: number
}
/** 展会查询参数，列表按「未结束的开始日期升序在前、已结束倒序在后」排好 */
export interface ExhibitionQuery {
  /** 页码 */
  pageNum: number
  /** 每页条数，上限 100 */
  pageSize: number
  /** 展会名称 */
  name?: string
  /** 展会类型 */
  type?: ExhibitionType | string
  /** 开始日期所在年份 */
  year?: number | string
  /** 大洲名称 */
  continent?: string
  /** 国家ID */
  country?: number
  /** 只看未结束的 */
  upcoming?: boolean
}
/** 展会报名 / 咨询下一届提交内容 */
export interface RegistrationSubmit {
  exhibitionId: number
  /** 公司或个人名称 */
  companyOrPersonalName: string
  /** 联系人姓名 */
  contactName: string
  /** 手机号 */
  contactPhone: string
}
