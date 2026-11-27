// 原型 mock 接口：返回结构与后端 R / TableDataInfo 一致。
// 不依赖服务端，SSR（server/mock/middleware.ts）和浏览器（mock/client.ts）共用，静态部署时浏览器端同样可用。
import {
  areas,
  carousels,
  cases,
  consultants,
  contactInfo,
  exhibitions,
  informations,
  subsidies
} from './data'
import { toSummary } from '../utils/text'

export class MockNotFound extends Error {}

const ok = <T>(data: T) => ({ code: 200, msg: '操作成功', data })

// 与后端一致：默认 10 条，上限 100 条
const page = <T>(list: T[], query: Record<string, any>) => {
  const pageNum = Number(query.pageNum) || 1
  const pageSize = Math.min(Number(query.pageSize) || 10, 100)
  return {
    code: 200,
    msg: '查询成功',
    total: list.length,
    rows: list.slice((pageNum - 1) * pageSize, pageNum * pageSize)
  }
}

const byKeyword = <T>(list: T[], field: keyof T, keyword?: string) =>
  keyword ? list.filter(item => String(item[field]).includes(keyword)) : [...list]

// 列表接口不返回正文：展会直接去掉，资讯、案例、补贴换成纯文本摘要
const withoutContent = <T extends { content?: string }>({ content: _content, ...rest }: T) => rest
const withSummary = <T extends { content?: string }>({ content, ...rest }: T) => ({ ...rest, summary: toSummary(content) })
const newestFirst = (a: { id: number; createTime: string }, b: { id: number; createTime: string }) =>
  b.createTime.localeCompare(a.createTime) || b.id - a.id

const today = () => new Date().toISOString().slice(0, 10)
const continentOf = (countryId: number) => areas.find(a => a.id === countryId)?.continentName

// 与后端一致：未结束的按开始日期升序在前，已结束的按开始日期倒序在后，同一天按 sort、id
type MockExhibition = (typeof exhibitions)[number]
const exhibitionOrder = (t: string) => (a: MockExhibition, b: MockExhibition) => {
  const aEnded = a.endDate < t
  const bEnded = b.endDate < t
  if (aEnded !== bEnded) return aEnded ? 1 : -1
  const byStart = aEnded ? b.startDate.localeCompare(a.startDate) : a.startDate.localeCompare(b.startDate)
  return byStart || a.sort - b.sort || a.id - b.id
}

// path 不含 /api 前缀，如 /exhibition/page
export const mockApi = (path: string, query: Record<string, any>) => {
  const id = Number(path.split('/').pop())
  // 与后端一致：查不到时返回 HTTP 404，由页面显示 404
  const detail = <T extends { id: number }>(list: T[]) => {
    const row = list.find(item => item.id === id)
    if (!row) throw new MockNotFound(`${path} 不存在`)
    return ok(row)
  }

  if (path === '/carousel/page') return page([...carousels].sort((a, b) => a.sort - b.sort || a.id - b.id), query)
  if (path === '/exhibition/page') {
    const t = today()
    let list = byKeyword(exhibitions, 'name', query.name)
    if (query.type) list = list.filter(item => item.exhibitionType === query.type)
    if (query.year) list = list.filter(item => item.startDate.startsWith(`${query.year}-`))
    if (query.continent) list = list.filter(item => continentOf(item.country) === query.continent)
    if (query.country) list = list.filter(item => item.country === Number(query.country))
    if (String(query.upcoming) === 'true') list = list.filter(item => item.endDate >= t)
    return page(list.sort(exhibitionOrder(t)).map(withoutContent), query)
  }
  if (path === '/exhibition/countries') {
    const t = today()
    return ok(
      areas.flatMap(area => {
        const rows = exhibitions.filter(item => item.country === area.id)
        if (!rows.length) return []
        return [{ ...area, exhibitionCount: rows.length, upcomingCount: rows.filter(item => item.endDate >= t).length }]
      })
    )
  }
  if (path === '/exhibition/years')
    return ok([...new Set(exhibitions.map(item => Number(item.startDate.slice(0, 4))))].sort((a, b) => a - b))
  // 新增约定：主推展会
  if (path === '/exhibition/featured') return ok(exhibitions.filter(item => item.featured))
  if (path.startsWith('/exhibition/detail/')) return detail(exhibitions)
  if (path === '/exhibition/registration/submit') return ok(null)
  if (path === '/information/page')
    return page(byKeyword(informations, 'title', query.title).sort(newestFirst).map(withSummary), query)
  if (path.startsWith('/information/detail/')) return detail(informations)
  if (path === '/cases/page') return page(byKeyword(cases, 'title', query.title).sort(newestFirst).map(withSummary), query)
  if (path.startsWith('/cases/detail/')) return detail(cases)
  if (path === '/subsidies/page')
    return page(byKeyword(subsidies, 'title', query.title).sort(newestFirst).map(withSummary), query)
  if (path.startsWith('/subsidies/detail/')) return detail(subsidies)
  if (path === '/contact/info') return ok(contactInfo)
  // 新增约定：多顾问联系人
  if (path === '/contact/consultants') return ok(consultants)

  throw new MockNotFound(`mock 未覆盖该接口：${path}`)
}
