/** 展会类型枚举 */
export enum ExhibitionType {
  ENERGY = 'ENERGY',
  ELEVATOR = 'ELEVATOR',
  MEDICAL = 'MEDICAL'
}

/** 展会类型显示映射 */
export const ExhibitionTypeMap = {
  [ExhibitionType.ENERGY]: '能源展',
  [ExhibitionType.ELEVATOR]: '电梯展',
  [ExhibitionType.MEDICAL]: '医疗展'
}

/** 展会对象 */
export interface Exhibition {
  /** 展会ID */
  id: number
  /** 展会名称 */
  name: string
  /** 展会内容 */
  content: string
  /** 展会时间 */
  time: string
  /** 年份 */
  year: string
  /** 月份 */
  month: string
  /** 国家ID */
  country: number
  /** 国家名称 */
  countryName: string
  /** 地址 */
  location: string
  /** 排序 */
  sort: number
  /** 展会类型 */
  exhibitionType: ExhibitionType
}

/** 展会查询参数 */
export interface ExhibitionQuery {
  /** 页码 */
  pageNum: number
  /** 每页条数 */
  pageSize: number
  /** 展会名称 */
  name?: string
  /** 国家ID */
  country?: number
  /** 展会类型 */
  exhibitionType?: ExhibitionType
}

/** 区域对象 */
export interface GlobalArea {
  /** ID */
  id: number
  /** 大洲编码 */
  continentCode: string
  /** 大洲名称 */
  continentName: string
  /** 国家编码 */
  countryCode: string
  /** 国家名称 */
  countryName: string
} 