// 用户相关类型
export interface UserInfo {
  username: string;
}

// 轮播图相关类型
export interface Carousel {
  id: number;
  title: string;
  imageUrl: string;
  sort: number;
  status: boolean;
}

// 资讯相关类型
export interface News {
  id: number;
  title: string;
  imageUrl: string;
  content: string;
  createTime: string;
  status: boolean;
}

// 展会相关类型
export interface Exhibition {
  id: number;
  title: string;
  imageUrl: string;
  content: string;
  startTime: string;
  endTime: string;
  location: string;
  status: boolean;
}

// 补贴政策相关类型
export interface Subsidy {
  id: number;
  title: string;
  amount: number;
  content: string;
  conditions: string;
  startTime: string;
  endTime: string;
  status: boolean;
}

// 表单规则类型
export interface FormRules {
  [key: string]: {
    required?: boolean;
    message: string;
    trigger: string;
    min?: number;
    max?: number;
  }[];
}

// 编辑器配置类型
export interface EditorConfig {
  placeholder: string;
} 