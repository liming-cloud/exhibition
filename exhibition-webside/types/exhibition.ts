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
  time: string
  year: string
  month: string
  sort: number
  content: string
  country: number
  countryName: string
  exhibitionType: ExhibitionType // 修改为枚举类型
}

export interface GlobalArea {
  id: number
  continentCode: string
  continentName: string
  countryCode: string
  countryName: string
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
  exhibitionType?: ExhibitionType // 修改为枚举类型
}