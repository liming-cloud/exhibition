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
}