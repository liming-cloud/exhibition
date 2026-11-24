// https://nuxt.com/docs/api/configuration/nuxt-config
import * as mockData from './mock/data'

// 原型 mock 模式：构建或启动开发服务时设置 NUXT_PUBLIC_MOCK=1；正式构建不包含 mock 代码与占位图
const mock = process.env.NUXT_PUBLIC_MOCK === '1'

// 静态生成（GitHub Pages 预览）时按 mock 数据列出全部页面，不依赖爬链接（带子路径时爬取不可靠）
const mockRoutes = () => [
  '/',
  ...['/exhibitions', '/news', '/cases', '/subsidies', '/about', '/contact'],
  ...mockData.exhibitions.map(e => `/exhibitions/${e.id}`),
  ...mockData.informations.map(e => `/news/${e.id}`),
  ...mockData.cases.map(e => `/cases/${e.id}`),
  ...mockData.subsidies.map(e => `/subsidies/${e.id}`)
]

export default defineNuxtConfig({
  devtools: { enabled: true },

  modules: [
    '@nuxtjs/tailwindcss',
    '@pinia/nuxt',
    '@nuxtjs/color-mode',
    '@element-plus/nuxt'
  ],

  css: [
    '~/assets/css/main.css',
    'element-plus/dist/index.css',
    '@fortawesome/fontawesome-free/css/all.css'
  ],

  vite: {
    json: {
      stringify: true
    }
  },

  colorMode: {
    classSuffix: ''
  },

  app: {
    head: {
      htmlAttrs: { lang: 'zh-CN' },
      meta: [
        { charset: 'utf-8' },
        { name: 'viewport', content: 'width=device-width, initial-scale=1' },
        { name: 'description', content: '雅博展览，专注国际展会组展与参展服务，助力中国企业开拓海外市场' }
      ],
      link: [
        { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }
      ]
    },
    // GitHub Pages 预览部署在 /exhibition/ 子路径，构建时用 NUXT_APP_BASE_URL 指定
    baseURL: process.env.NUXT_APP_BASE_URL || '/'
  },

  runtimeConfig: {
    // 后端地址，部署时用环境变量 NUXT_API_BASE 覆盖
    apiBase: 'http://127.0.0.1:8080',
    public: {
      baseURL: '/',
      mock
    }
  },

  nitro: {
    // element-plus 通过 npm 别名依赖 @popperjs/core，按包名追踪会漏掉，直接打进产物
    externals: {
      inline: ['@popperjs/core']
    },
    // mock 模式：SSR 的 /api 请求返回模拟数据，占位图挂在 /mock/ 下
    handlers: mock ? [{ middleware: true, handler: '~/server/mock/middleware.ts' }] : [],
    publicAssets: mock ? [{ dir: '../mock/public' }] : [],
    prerender: mock ? { crawlLinks: false, routes: mockRoutes() } : {}
  },

  plugins: [
    '~/plugins/echarts.ts',
    // mock 模式：浏览器端 /api 请求同样返回模拟数据（静态部署时没有服务端）
    ...(mock ? [{ src: '~/mock/client.ts', mode: 'client' as const }] : [])
  ],

  compatibilityDate: '2025-06-09'
})