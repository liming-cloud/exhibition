import type { Subsidies } from '~/types/subsidies'

// 补贴政策状态：按有效期算，列表与详情共用
export const subsidyStatus = (s: Subsidies, today = new Date().toISOString().slice(0, 10)) => {
  if (s.endTime && s.endTime.slice(0, 10) < today) return { label: '已截止', cls: 'tag-muted', closed: true }
  if (s.startTime && s.startTime.slice(0, 10) > today) return { label: '未开始', cls: '', closed: false }
  return { label: '申报中', cls: 'tag-success', closed: false }
}

// 补贴金额：沿用原页面的换算（price 按元存，显示为万元）；price 为 0 时页面不显示
export const formatSubsidyPrice = (price: number) => `¥ ${(price / 10000).toFixed(2)}万`
