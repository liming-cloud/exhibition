// 站点地图：静态页 + 后端所有展会、案例、资讯、补贴政策详情页
const detailSources = [
  { api: '/exhibition/page', path: '/exhibitions' },
  { api: '/cases/page', path: '/cases' },
  { api: '/information/page', path: '/news' },
  { api: '/subsidies/page', path: '/subsidies' }
]

// 后端每页最多 100 条，列表接口不含正文，按页取完
const PAGE_SIZE = 100

const listIds = async (url: string) => {
  const ids: number[] = []
  for (let pageNum = 1; ; pageNum++) {
    const res = await $fetch<{ total: number; rows: { id: number }[] }>(url, {
      query: { pageNum, pageSize: PAGE_SIZE }
    })
    ids.push(...res.rows.map(row => row.id))
    if (!res.rows.length || ids.length >= res.total) return ids
  }
}

export default defineEventHandler(async (event) => {
  const { apiBase } = useRuntimeConfig(event)
  const origin = getRequestURL(event, { xForwardedProto: true }).origin

  const staticPaths = ['/', '/exhibitions', '/news', '/cases', '/subsidies', '/about', '/contact']
  const detailPaths = await Promise.all(
    detailSources.map(async ({ api, path }) => {
      const ids = await listIds(apiBase + api).catch(() => [])
      return ids.map(id => `${path}/${id}`)
    })
  )

  const urls = [...staticPaths, ...detailPaths.flat()]
    .map(path => `  <url><loc>${origin}${path}</loc></url>`)
    .join('\n')

  setHeader(event, 'Content-Type', 'application/xml; charset=utf-8')
  return `<?xml version="1.0" encoding="UTF-8"?>\n<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">\n${urls}\n</urlset>\n`
})
