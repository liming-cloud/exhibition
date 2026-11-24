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

export class MockNotFound extends Error {}

const ok = <T>(data: T) => ({ code: 200, msg: '操作成功', data })

const page = <T>(list: T[], query: Record<string, any>) => {
  const pageNum = Number(query.pageNum) || 1
  const pageSize = Number(query.pageSize) || 10
  return {
    code: 200,
    msg: '查询成功',
    total: list.length,
    rows: list.slice((pageNum - 1) * pageSize, pageNum * pageSize)
  }
}

const byKeyword = <T>(list: T[], field: keyof T, keyword?: string) =>
  keyword ? list.filter(item => String(item[field]).includes(keyword)) : list

// path 不含 /api 前缀，如 /exhibition/page
export const mockApi = (path: string, query: Record<string, any>) => {
  const id = Number(path.split('/').pop())
  // 与后端一致：查不到时返回 data: null，由页面显示 404
  const detail = <T extends { id: number }>(list: T[]) =>
    ok(list.find(row => row.id === id) ?? null)

  if (path === '/carousel/page') return page(carousels, query)
  if (path === '/exhibition/page') {
    let list = byKeyword(exhibitions, 'name', query.name)
    if (query.country) list = list.filter(item => item.country === Number(query.country))
    return page(list, query)
  }
  if (path === '/exhibition/area') return ok(areas)
  // 新增约定：主推展会
  if (path === '/exhibition/featured') return ok(exhibitions.filter(item => item.featured))
  if (path.startsWith('/exhibition/detail/')) return detail(exhibitions)
  if (path === '/exhibition/registration/submit') return ok(null)
  if (path === '/information/page')
    return page(byKeyword(informations, 'title', query.title), query)
  if (path.startsWith('/information/detail/')) return detail(informations)
  if (path === '/cases/page') return page(byKeyword(cases, 'title', query.title), query)
  if (path.startsWith('/cases/detail/')) return detail(cases)
  if (path === '/subsidies/page') return page(byKeyword(subsidies, 'title', query.title), query)
  if (path.startsWith('/subsidies/detail/')) return detail(subsidies)
  if (path === '/contact/info') return ok(contactInfo)
  // 新增约定：多顾问联系人
  if (path === '/contact/consultants') return ok(consultants)

  throw new MockNotFound(`mock 未覆盖该接口：${path}`)
}
