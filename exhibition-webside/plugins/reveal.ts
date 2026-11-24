// 滚动入场 v-reveal：元素滚动进入视口时淡入上浮一次（样式见 main.css 的 .reveal）。
// 绑定值是同组里的序号，每项错开 80ms，最多错开 5 项
type RevealEl = HTMLElement & { _revealIo?: IntersectionObserver }

export default defineNuxtPlugin(nuxtApp => {
  nuxtApp.vueApp.directive<RevealEl, number | undefined>('reveal', {
    getSSRProps: () => ({}),
    mounted(el, binding) {
      if (!motionAllowed() || !belowFold(el)) return
      el.classList.add('reveal')
      el.style.transitionDelay = `${Math.min(binding.value ?? 0, 5) * 80}ms`
      el._revealIo = new IntersectionObserver(
        ([entry]) => {
          if (!entry.isIntersecting) return
          el.classList.add('reveal-in')
          el._revealIo?.disconnect()
        },
        { rootMargin: '0px 0px -8% 0px' }
      )
      el._revealIo.observe(el)
    },
    unmounted(el) {
      el._revealIo?.disconnect()
    }
  })
})
