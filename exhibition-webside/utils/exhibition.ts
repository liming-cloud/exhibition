import type { Exhibition } from '~/types/exhibition'

export const exhibitionStart = (e: Exhibition) => e.startDate

export const exhibitionEnd = (e: Exhibition) => e.endDate

// 展期文字：单日只写一天
export const exhibitionDates = (e: Exhibition) =>
  e.startDate === e.endDate ? e.startDate : `${e.startDate} 至 ${e.endDate}`

export const isUpcoming = (e: Exhibition, today = new Date().toISOString().slice(0, 10)) =>
  exhibitionEnd(e) >= today

// 信任数字（D3）：只放能从展会数据算出的，首页与关于我们共用
export const exhibitionStats = (rows: Exhibition[]) => [
  { label: '覆盖国家', value: new Set(rows.map(e => e.country)).size },
  { label: '即将开展', value: rows.filter(e => isUpcoming(e)).length }
]
