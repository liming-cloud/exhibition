// 原型 mock：构建时设置 NUXT_PUBLIC_MOCK=1 才会注册（见 nuxt.config.ts）。
// 浏览器和 SSR 的请求都经过 /api，在这里直接返回模拟数据，不再转发到后端。
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

const placeholderColors: Record<string, string> = {
  banner: '#1e3a5f',
  exhibition: '#334155',
  news: '#475569',
  case: '#3f3f46'
}

// 占位图：深色底 + 图片用途说明，正式内容由后台上传
const placeholder = (path: string) => {
  const kind = path.replace(/^mock\//, '').replace(/-\d+\.\w+$/, '')
  const label = path.replace(/^mock\//, '').replace(/\.\w+$/, '')
  const color = placeholderColors[kind] || '#52525b'
  return `<svg xmlns="http://www.w3.org/2000/svg" width="1600" height="900" viewBox="0 0 1600 900">
<defs><linearGradient id="g" x1="0" y1="0" x2="1" y2="1"><stop offset="0" stop-color="${color}"/><stop offset="1" stop-color="#0f172a"/></linearGradient></defs>
<rect width="1600" height="900" fill="url(#g)"/>
<text x="800" y="470" font-family="sans-serif" font-size="56" fill="#ffffff" fill-opacity="0.35" text-anchor="middle">示意图 ${label}</text>
</svg>`
}

export default defineEventHandler(async event => {
  if (!event.path.startsWith('/api/')) return

  const path = event.path.replace(/^\/api/, '').split('?')[0]
  const query = getQuery(event)
  const id = Number(path.split('/').pop())
  // 与后端一致：查不到时返回 data: null，由页面显示 404
  const detail = <T extends { id: number }>(list: T[]) =>
    ok(list.find(row => row.id === id) ?? null)

  if (path === '/file/preview') {
    setResponseHeader(event, 'Content-Type', 'image/svg+xml')
    return placeholder(String(query.path || ''))
  }
  if (path === '/carousel/page') return page(carousels, query)
  if (path === '/exhibition/page') {
    let list = byKeyword(exhibitions, 'name', query.name as string)
    if (query.country) list = list.filter(item => item.country === Number(query.country))
    return page(list, query)
  }
  if (path === '/exhibition/area') return ok(areas)
  // 新增约定：主推展会
  if (path === '/exhibition/featured') return ok(exhibitions.filter(item => item.featured))
  if (path.startsWith('/exhibition/detail/')) return detail(exhibitions)
  if (path === '/exhibition/registration/submit') {
    await readBody(event)
    return ok(null)
  }
  if (path === '/information/page')
    return page(byKeyword(informations, 'title', query.title as string), query)
  if (path.startsWith('/information/detail/')) return detail(informations)
  if (path === '/cases/page') return page(byKeyword(cases, 'title', query.title as string), query)
  if (path.startsWith('/cases/detail/')) return detail(cases)
  if (path === '/subsidies/page')
    return page(byKeyword(subsidies, 'title', query.title as string), query)
  if (path.startsWith('/subsidies/detail/')) return detail(subsidies)
  if (path === '/contact/info') return ok(contactInfo)
  // 新增约定：多顾问联系人
  if (path === '/contact/consultants') return ok(consultants)

  throw createError({ statusCode: 404, statusMessage: `mock 未覆盖该接口：${path}` })
})
