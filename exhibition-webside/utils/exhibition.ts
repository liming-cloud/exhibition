import type { CountryStat, Exhibition } from '~/types/exhibition'

export const exhibitionStart = (e: Exhibition) => e.startDate

export const exhibitionEnd = (e: Exhibition) => e.endDate

// 展期文字：单日只写一天
export const exhibitionDates = (e: Exhibition) =>
  e.startDate === e.endDate ? e.startDate : `${e.startDate} 至 ${e.endDate}`

export const isUpcoming = (e: Exhibition, today = new Date().toISOString().slice(0, 10)) =>
  exhibitionEnd(e) >= today

// 信任数字（D3）：只放能从展会数据算出的，首页与关于我们共用；countries 来自 /exhibition/countries
export const exhibitionStats = (countries: CountryStat[]) => [
  { label: '覆盖国家', value: countries.length },
  { label: '即将开展', value: countries.reduce((sum, c) => sum + c.upcomingCount, 0) }
]
