/** 轮播图对象 */
export interface Carousel {
  /** 轮播图ID */
  id?: number
  /** 标题 */
  title: string
  /** 图片地址 */
  imageUrl: string
  /** 链接地址 */
  link: string
  /** 排序 */
  sort: number
  /** 状态（0正常 1停用） */
  status: number
  /** 创建时间 */
  createTime?: string
  /** 更新时间 */
  updateTime?: string
}

/** 轮播图查询参数 */
export interface CarouselQuery {
  /** 页码 */
  pageNum: number
  /** 每页条数 */
  pageSize: number
  /** 标题 */
  title?: string
  /** 状态（0正常 1停用） */
  status?: number
} 