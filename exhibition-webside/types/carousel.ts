export interface Carousel {
  id: number
  title: string
  imageUrl: string
  link: string
  sort: number
  status: number
  createTime: string
  updateTime: string
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