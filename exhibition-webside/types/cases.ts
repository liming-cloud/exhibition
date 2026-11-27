/** 案例对象 */
export interface Cases {
  /** ID */
  id: number
  /** 标题 */
  title: string
  /** 内容，只有详情接口返回 */
  content?: string
  /** 正文纯文本摘要，只有列表接口返回 */
  summary?: string
  /** 封面图 */
  coverUrl: string
  /** 创建时间 */
  createTime?: string
}

/** 查询参数 */
export interface CasesQuery {
  /** 页码 */
  pageNum: number
  /** 每页数量 */
  pageSize: number
  /** 标题 */
  title?: string
} 