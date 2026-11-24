const shades = [50, 100, 200, 300, 400, 500, 600, 700, 800, 900]
const themeColor = (name) =>
  Object.fromEntries(shades.map((n) => [n, `rgb(var(--color-${name}-${n}) / <alpha-value>)`]))

/** @type {import('tailwindcss').Config} */
export default {
  content: [
    './components/**/*.{js,vue,ts}',
    './layouts/**/*.vue',
    './pages/**/*.vue',
    './plugins/**/*.{js,ts}',
    './app.vue',
    './assets/**/*.css',
  ],
  theme: {
    extend: {
      colors: {
        // 主色、强调色引用 assets/css/main.css 里的 CSS 变量，原型阶段可按 data-theme 切换蓝/橙两套
        primary: themeColor('primary'),
        accent: themeColor('accent'),
        neutral: {
          50: '#fafafa',
          100: '#f4f4f5',
          200: '#e4e4e7',
          300: '#d4d4d8',
          400: '#a1a1aa',
          500: '#71717a',
          600: '#52525b',
          700: '#3f3f46',
          800: '#27272a',
          900: '#18181b',
        }
      },
      fontFamily: {
        // 使用系统中文字体，不加载外部字体文件
        sans: ['-apple-system', 'BlinkMacSystemFont', '"PingFang SC"', '"Microsoft YaHei"', '"Helvetica Neue"', 'sans-serif'],
        display: ['-apple-system', 'BlinkMacSystemFont', '"PingFang SC"', '"Microsoft YaHei"', '"Helvetica Neue"', 'sans-serif'],
      },
      boxShadow: {
        'soft': '0 2px 15px -3px rgba(0, 0, 0, 0.04), 0 4px 6px -2px rgba(0, 0, 0, 0.02)',
        'elegant': '0 10px 25px -3px rgba(0, 0, 0, 0.08), 0 4px 6px -2px rgba(0, 0, 0, 0.01)',
        'hover': '0 25px 50px -12px rgba(0, 0, 0, 0.1)',
        'modern': '0 20px 40px -8px rgba(0, 0, 0, 0.12), 0 0 20px -5px rgba(0, 0, 0, 0.1)',
        'glow': '0 0 20px rgb(var(--color-primary-500) / 0.2)',
      },
      backdropBlur: {
        'modern': '20px',
      },
    },
  },
  plugins: [
    require('@tailwindcss/forms'),
    require('@tailwindcss/typography'),
    require('@tailwindcss/aspect-ratio'),
  ],
} 