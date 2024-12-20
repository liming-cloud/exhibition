// nuxt.config.js
export default defineNuxtConfig({
  css: [
    '@/assets/css/tailwind.css',
    '@fortawesome/fontawesome-free/css/all.min.css',
    '@glidejs/glide/dist/css/glide.core.min.css',
    '@glidejs/glide/dist/css/glide.theme.min.css',
  ],
  ssr: true, // 启用服务器端渲染
  build: {
    transpile: ['@fortawesome/fontawesome-free'],
  },
  postcss: {
    plugins: {
      tailwindcss: {},
      autoprefixer: {},
    },
  },
  hooks: {
    'build:before': () => {
      const { execSync } = require('child_process')
      execSync('node scripts/convert-docs.cjs', { stdio: 'inherit' })
    }
  }
})