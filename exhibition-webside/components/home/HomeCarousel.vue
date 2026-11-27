<template>
  <!-- 通栏轮播：展示后台所有启用的轮播图（按后台排序），客户在后台「轮播图」自行上传、排序、停用。
       切换：图片淡入 + 缓慢缩放，文字依次上浮入场；自动播放 6 秒一张，带暂停按钮（WCAG 2.2.2） -->
  <section
    class="relative h-[260px] sm:h-[420px] lg:h-[560px] bg-neutral-900 overflow-hidden"
    aria-roledescription="carousel"
    aria-label="首页轮播"
    @touchstart.passive="onTouchStart($event.touches[0].clientX)"
    @touchend="onSwipe($event.changedTouches[0].clientX)"
  >
    <template v-if="slides.length">
      <div
        v-for="(item, index) in slides"
        :key="item.id"
        class="slide absolute inset-0"
        :class="{ 'is-active': index === current }"
        :inert="index !== current"
        role="group"
        aria-roledescription="slide"
        :aria-label="`${index + 1} / ${slides.length}`"
      >
        <MediaBackground :src="fileUrl(item.imageUrl)" :eager="index === 0" img-class="slide-img" />
        <div class="relative h-full container flex items-center">
          <div class="max-w-2xl">
            <p
              class="slide-text text-xs sm:text-sm tracking-[0.2em] text-white/70"
              style="--d: 0ms"
            >
              {{ site.name }} · 境外展会组展与参展服务
            </p>
            <p
              class="slide-text mt-3 text-2xl sm:text-4xl lg:text-5xl font-bold leading-tight text-white line-clamp-2 text-balance"
              style="--d: 100ms"
            >
              {{ item.title }}
            </p>
            <NuxtLink
              v-if="item.link"
              :to="item.link"
              class="slide-text btn btn-primary btn-lg mt-6 sm:mt-8"
              style="--d: 200ms"
            >
              了解更多<i class="fas fa-arrow-right text-xs"></i>
            </NuxtLink>
          </div>
        </div>
      </div>

      <div v-if="slides.length > 1" class="absolute inset-x-0 bottom-0 z-10">
        <div class="container flex items-center gap-3 pb-4 sm:pb-8">
          <button
            type="button"
            class="ctrl w-8 h-8"
            :aria-label="playing ? '暂停轮播' : '播放轮播'"
            @click="playing = !playing"
          >
            <i class="fas text-xs" :class="playing ? 'fa-pause' : 'fa-play'"></i>
          </button>
          <button
            v-for="(item, index) in slides"
            :key="item.id"
            type="button"
            class="h-1 rounded-full transition-all duration-300"
            :class="index === current ? 'w-8 bg-white' : 'w-4 bg-white/40 hover:bg-white/70'"
            :aria-label="`第 ${index + 1} 张：${item.title}`"
            :aria-current="index === current"
            @click="select(index)"
          ></button>
          <div class="ml-auto hidden sm:flex gap-2">
            <button
              type="button"
              class="ctrl w-10 h-10"
              aria-label="上一张"
              @click="select(current - 1)"
            >
              <i class="fas fa-chevron-left text-sm"></i>
            </button>
            <button
              type="button"
              class="ctrl w-10 h-10"
              aria-label="下一张"
              @click="select(current + 1)"
            >
              <i class="fas fa-chevron-right text-sm"></i>
            </button>
          </div>
        </div>
      </div>
    </template>

    <!-- 后台还没有启用的轮播图时，显示公司标语，不出现空白 -->
    <div v-else class="h-full bg-gradient-to-br from-primary-800 to-primary-600 flex items-center">
      <div class="container text-white">
        <p class="text-2xl sm:text-4xl font-bold">专注境外展会组展与参展服务</p>
        <p class="mt-3 text-white/80">一站式出展服务，助力企业开拓国际市场</p>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import MediaBackground from '~/components/common/MediaBackground.vue'
import { site } from '~/constants/site'

const api = useApi()
const { data } = await useAsyncData('home-carousel', () =>
  api.carousel.getList({ pageNum: 1, pageSize: 10, status: 1 })
)
// 后端已按 sort 升序返回
const slides = computed(() => data.value?.rows ?? [])

const INTERVAL = 6000
const current = ref(0)
// 服务端渲染时不播放；挂载后按系统「减少动态效果」设置决定是否自动播放
const playing = ref(false)
let timer: ReturnType<typeof setInterval> | undefined

const stop = () => clearInterval(timer)
const start = () => {
  stop()
  if (slides.value.length > 1) {
    timer = setInterval(() => {
      current.value = (current.value + 1) % slides.value.length
    }, INTERVAL)
  }
}
// 手动切换后重新计时，避免刚切过去马上又自动跳走
const select = (index: number) => {
  current.value = (index + slides.value.length) % slides.value.length
  if (playing.value) start()
}

// 手机端左右滑动切换
let touchX = 0
const onTouchStart = (x: number) => (touchX = x)
const onSwipe = (x: number) => {
  const dx = x - touchX
  if (Math.abs(dx) > 50) select(current.value + (dx > 0 ? -1 : 1))
}

watch(playing, value => (value ? start() : stop()))
onMounted(() => {
  playing.value = motionAllowed()
})
onBeforeUnmount(stop)
</script>

<style scoped>
.slide {
  opacity: 0;
  transition: opacity 800ms ease-out;
}

.slide.is-active {
  opacity: 1;
  z-index: 1;
}

/* 缓慢缩放：当前张 7 秒内从 1.06 缩到 1；离开的那张等淡出结束再复位，避免淡出时跳动 */
:deep(.slide-img) {
  transform: scale(1.06);
  transition: transform 0s linear 800ms;
}

.is-active :deep(.slide-img) {
  transform: scale(1);
  transition: transform 7000ms ease-out;
}

/* 文字依次入场：上浮 16px，400ms，按 --d 错开，在图片开始淡入后 300ms 开始 */
.slide-text {
  opacity: 0;
  transform: translateY(16px);
  transition:
    opacity 400ms ease-out,
    transform 400ms ease-out;
}

.is-active .slide-text {
  opacity: 1;
  transform: none;
  transition-delay: calc(var(--d) + 300ms);
}

.ctrl {
  @apply inline-flex items-center justify-center rounded-full border border-white/40 text-white hover:bg-white/15 transition-colors;
}
</style>
