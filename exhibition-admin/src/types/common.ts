/** API响应结果 */
export interface ApiResponse<T> {
  /** 状态码 */
  code: number
  /** 数据 */
  data: T
  /** 消息 */
  msg: string
}

/** 表格数据信息 */
export interface TableDataInfo<T> {
  /** 总记录数 */
  total: number
  /** 列表数据 */
  rows: T[]
  /** 消息状态码 */
  code: number
  /** 消息内容 */
  msg: string
} 