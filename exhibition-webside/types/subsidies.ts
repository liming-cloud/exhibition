/** 补贴信息对象 */
export interface Subsidies {
  /** ID */
  id: number
  /** 标题 */
  title: string
  /** 内容，只有详情接口返回 */
  content?: string
  /** 正文纯文本摘要，只有列表接口返回 */
  summary?: string
  /** 补贴金额（元），页面显示为万元 */
  price: number
  /** 开始时间 */
  startTime: string
  /** 结束时间 */
  endTime: string
  /** 创建时间 */
  createTime?: string
}

/** 查询参数 */
export interface SubsidiesQuery {
  /** 页码 */
  pageNum: number
  /** 每页数量 */
  pageSize: number
  /** 标题 */
  title?: string
} 