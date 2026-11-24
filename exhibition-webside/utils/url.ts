// public/ 下的静态资源地址，跟随 app.baseURL（GitHub Pages 预览部署在 /exhibition/ 子路径下）
export const assetUrl = (path: string) =>
  useRuntimeConfig().app.baseURL.replace(/\/$/, '') + '/' + path.replace(/^\//, '')

// 后台上传的文件地址；mock 模式下直接使用静态占位图
export const fileUrl = (path?: string) => {
  if (!path) return ''
  return useRuntimeConfig().public.mock
    ? assetUrl(path)
    : `/api/file/preview?path=${encodeURIComponent(path)}`
}

// 电话号码转拨号链接：多个号码用「/」分隔时取第一个，去掉空格和横线
export const telHref = (phone: string) => `tel:${phone.split('/')[0].replace(/[^\d+]/g, '')}`
