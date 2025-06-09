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
      title: '展会官网',
      meta: [
        { charset: 'utf-8' },
        { name: 'viewport', content: 'width=device-width, initial-scale=1' },
        { hid: 'description', name: 'description', content: '展会官网 - 您的专业展会服务平台' }
      ],
      link: [
        { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }
      ]
    },
    baseURL: '/'
  },

  runtimeConfig: {
    public: {
      baseURL: '/'
    }
  },

  nitro: {
    devProxy: {
      '/api': {
        target: 'http://127.0.0.1:8080',
        changeOrigin: true,
        ws: true
      }
    }
  },

  plugins: [
    '~/plugins/echarts.ts'
  ],

  compatibilityDate: '2025-06-09'
})