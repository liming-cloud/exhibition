// 站点地图：静态页 + 后端所有展会、案例、资讯、补贴政策详情页
const detailSources = [
  { api: '/exhibition/page', path: '/exhibitions' },
  { api: '/cases/page', path: '/cases' },
  { api: '/information/page', path: '/news' },
  { api: '/subsidies/page', path: '/subsidies' }
]

export default defineEventHandler(async (event) => {
  const { apiBase } = useRuntimeConfig(event)
  const origin = getRequestURL(event).origin

  const staticPaths = ['/', '/exhibitions', '/news', '/cases', '/subsidies', '/about', '/contact']
  const detailPaths = await Promise.all(
    detailSources.map(async ({ api, path }) => {
      const res = await $fetch<{ rows: { id: number }[] }>(apiBase + api).catch(() => ({ rows: [] }))
      return res.rows.map(row => `${path}/${row.id}`)
    })
  )

  const urls = [...staticPaths, ...detailPaths.flat()]
    .map(path => `  <url><loc>${origin}${path}</loc></url>`)
    .join('\n')

  setHeader(event, 'Content-Type', 'application/xml; charset=utf-8')
  return `<?xml version="1.0" encoding="UTF-8"?>\n<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">\n${urls}\n</urlset>\n`
})
