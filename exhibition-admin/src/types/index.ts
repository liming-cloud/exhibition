export interface ExhibitionItem {
  id: number
  title: string
  description: string
  startTime: string
  endTime: string
  location: string
  document: string
  status: 'draft' | 'published' | 'ended'
}

export interface NewsItem {
  id: number
  title: string
  content: string
  image: string
  publishTime: string
  status: boolean
}

export interface CarouselItem {
  id: number
  title: string
  image: string
  url: string
  sort: number
  status: boolean
}

export interface PolicyItem {
  id: number
  title: string
  content: string
  startTime: string
  endTime: string
  status: boolean
}

export interface LoginForm {
  username: string
  password: string
} 