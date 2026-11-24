<template>
  <!-- 数字滚动计数：滚动到视口时 1 秒内从 0 数到目标值，只播一次。
       服务端渲染输出真实数字（搜索引擎可见）；首屏内的数字不播放，避免先变 0 再变回来 -->
  <span ref="el" class="tabular-nums">{{ display }}{{ suffix }}</span>
</template>

<script setup lang="ts">
const props = defineProps<{ value: number; suffix?: string }>()

const el = ref<HTMLElement>()
const display = ref(props.value)
let io: IntersectionObserver | undefined

const play = () => {
  const begin = performance.now()
  const step = (now: number) => {
    const t = Math.min((now - begin) / 1000, 1)
    // ease-out：开始快、结束慢
    display.value = Math.round(props.value * (1 - (1 - t) ** 3))
    if (t < 1) requestAnimationFrame(step)
  }
  requestAnimationFrame(step)
}

onMounted(() => {
  if (!el.value || !motionAllowed() || !belowFold(el.value)) return
  display.value = 0
  io = new IntersectionObserver(([entry]) => {
    if (!entry.isIntersecting) return
    io?.disconnect()
    play()
  })
  io.observe(el.value)
})
onBeforeUnmount(() => io?.disconnect())
</script>
