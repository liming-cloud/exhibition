
/** 市场资讯对象 */
export interface Information {
  /** ID */
  id?: number
  /** 标题 */
  title: string
  /** 内容 */
  content: string
  /** 封面图 */
  coverUrl: string
  /** 创建时间 */
  createTime?: string
}

/** 查询参数 */
export interface InformationQuery {
  /** 页码 */
  pageNum: number
  /** 每页数量 */
  pageSize: number
  /** 标题 */
  title?: string
} 