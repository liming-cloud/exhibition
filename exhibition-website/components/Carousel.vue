<template>
  <div class="mt-16 relative">
    <div ref="glideRef" class="glide">
      <div class="glide__track" data-glide-el="track">
        <ul class="glide__slides">
          <li
              v-for="(slide, index) in slides"
              :key="index"
              class="glide__slide relative h-[500px]"
          >
            <img
                :src="slide.image"
                :alt="slide.title"
                class="w-full max-h-100 object-cover object-top"
            />
            <div
                class="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/60 to-transparent p-8"
            >
              <h2 class="text-white text-3xl font-bold">{{ slide.title }}</h2>
              <p class="text-white/90 mt-2">{{ slide.description }}</p>
            </div>
          </li>
        </ul>
      </div>
      <div class="glide__bullets" data-glide-el="controls[nav]">
        <button
            v-for="(slide, index) in slides"
            :key="index"
            class="glide__bullet"
            :data-glide-dir="'='+index"
        ></button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, watch } from 'vue'
import Glide from '@glidejs/glide'
import carouselData from '~/content/carousel.json'

const glideRef = ref(null)
const slides = ref([])

onMounted(async () => {
  // 直接使用导入的本地 JSON 数据赋值
  slides.value = carouselData

  // 等待 DOM 更新完成
  await nextTick()

  // 初始化 Glide
  const glideInstance = new Glide(glideRef.value, {
    type: 'carousel',
    autoplay: 3000,
    hoverpause: true,
    perView: 1,
    gap: 0
  })
  glideInstance.mount()
})

</script>

<style scoped>
.glide__bullet {
  background-color: white;
}
</style>