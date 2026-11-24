import type { Exhibition } from '~/types/exhibition'

// time 字段形如「2026-06-23 至 2026-06-25」；取不到日期时退回 year/month
const dates = (e: Exhibition) => e.time?.match(/\d{4}-\d{2}-\d{2}/g) ?? []

export const exhibitionStart = (e: Exhibition) => dates(e)[0] ?? `${e.year}-${e.month || '01'}-01`

export const exhibitionEnd = (e: Exhibition) => dates(e)[1] ?? exhibitionStart(e)

export const isUpcoming = (e: Exhibition, today = new Date().toISOString().slice(0, 10)) =>
  exhibitionEnd(e) >= today
