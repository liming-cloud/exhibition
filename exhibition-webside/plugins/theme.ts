// 原型阶段的配色切换：地址加 ?theme=blue 或 ?theme=orange，选择记在 cookie 里，站内跳转保持不变。
// 配色定下来后删掉本插件和 main.css 里另一套变量。
const themes = ['orange', 'blue']

export default defineNuxtPlugin(() => {
  const theme = useCookie<string>('theme', { default: () => 'orange', maxAge: 60 * 60 * 24 * 30 })
  const query = useRoute().query.theme
  if (typeof query === 'string' && themes.includes(query)) {
    theme.value = query
  }

  useHead({
    htmlAttrs: { 'data-theme': () => (themes.includes(theme.value) ? theme.value : 'orange') }
  })
})
