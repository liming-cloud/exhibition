// https://nuxt.com/docs/api/configuration/nuxt-config
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
    baseURL: '/'
  },

  runtimeConfig: {
    // 后端地址，部署时用环境变量 NUXT_API_BASE 覆盖
    apiBase: 'http://127.0.0.1:8080',
    public: {
      baseURL: '/'
    }
  },

  nitro: {
    // element-plus 通过 npm 别名依赖 @popperjs/core，按包名追踪会漏掉，直接打进产物
    externals: {
      inline: ['@popperjs/core']
    },
    // 原型 mock 模式：构建或启动开发服务时设置 NUXT_PUBLIC_MOCK=1，/api 请求返回模拟数据；正式构建不包含这部分代码
    handlers:
      process.env.NUXT_PUBLIC_MOCK === '1'
        ? [{ middleware: true, handler: '~/server/mock/middleware.ts' }]
        : []
  },

  plugins: [
    '~/plugins/echarts.ts'
  ],

  compatibilityDate: '2025-06-09'
})