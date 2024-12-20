import type { PageParams } from './response'

// 轮播图数据类型
export interface Carousel {
  id?: number;
  title: string;
  imageUrl: string;
  link: string;
  sort: number;
  status: number;
  createTime?: string;
  updateTime?: string;
}

// 轮播图查询参数
export interface CarouselQuery extends PageParams {
  title?: string;
  status?: number;
} 