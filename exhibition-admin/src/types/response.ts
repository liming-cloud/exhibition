// 通用响应格式
export interface ApiResponse<T> {
  code: number;
  msg: string;
  data: T;
}

// 分页响应格式
export interface PageResponse<T> {
  total: number;
  rows: T[];
  code: number;
  msg: string;
}

// 分页请求参数
export interface PageParams {
  pageNum?: number;
  pageSize?: number;
  [key: string]: any;
} 