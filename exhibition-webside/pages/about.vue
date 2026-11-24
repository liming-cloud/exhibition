<template>
  <div class="container mx-auto px-4 py-8"> <!-- 减小间距 py-12 -> py-8 -->
    <!-- 公司简介 -->
    <section class="mb-10"> <!-- 减小间距 mb-16 -> mb-10 -->
      <h1 class="text-3xl font-bold text-center mb-8">关于我们</h1> <!-- 减小间距 mb-12 -> mb-8 -->
      <div v-if="contactInfo" class="max-w-4xl mx-auto bg-white rounded-lg shadow-lg p-8">
        <p class="text-neutral-600 leading-relaxed" v-html="contactInfo.companyProfile?.replace(/\n/g, '<br>')"></p>
      </div>
       <div v-else class="text-center p-8">
          <p class="text-neutral-600">正在加载公司简介...</p>
        </div>
    </section>

    <!-- 核心优势 -->
    <section class="mb-10 bg-neutral-50 py-8 rounded-lg"> <!-- 减小间距 mb-16 -> mb-10, py-12 -> py-8 -->
      <div class="max-w-6xl mx-auto">
        <h2 class="text-3xl font-bold text-center mb-8">核心优势</h2> <!-- 减小间距 mb-12 -> mb-8 -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div class="text-center p-6">
            <div class="text-4xl text-primary-500 mb-4">
              <i class="fas fa-globe"></i>
            </div>
            <h3 class="text-xl font-semibold mb-4">全球资源网络</h3>
            <p class="text-neutral-600">
              与全球知名展会主办方建立长期合作关系，包括英富曼、励展、Terrapinn等
            </p>
          </div>
          <div class="text-center p-6">
            <div class="text-4xl text-primary-500 mb-4">
              <i class="fas fa-handshake"></i>
            </div>
            <h3 class="text-xl font-semibold mb-4">专业服务团队</h3>
            <p class="text-neutral-600">
              团队成员均具有十几年海外展会操作经验，提供专业的展会咨询和支持
            </p>
          </div>
          <div class="text-center p-6">
            <div class="text-4xl text-primary-500 mb-4">
              <i class="fas fa-cogs"></i>
            </div>
            <h3 class="text-xl font-semibold mb-4">一站式解决方案</h3>
            <p class="text-neutral-600">
              提供展位设计、签证办理、机票预订等全方位定制化服务
            </p>
          </div>
        </div>
      </div>
    </section>

    <!-- 展团照片 -->
    <section class="mb-10"> <!-- 减小间距 mb-16 -> mb-10 -->
      <h2 class="text-3xl font-bold text-center mb-8">展团风采</h2> <!-- 减小间距 mb-12 -> mb-8 -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6 max-w-4xl mx-auto">
        <div v-for="(photo, index) in photos" :key="index" class="bg-white rounded-lg shadow-md overflow-hidden">
          <div class="relative">
            <img :src="photo.url" :alt="photo.alt" class="w-full h-auto">
          </div>
        </div>
      </div>
    </section>

    <!-- 联系我们 -->
    <section class="bg-neutral-50 py-8 rounded-lg"> <!-- 减小间距 py-12 -> py-8 -->
      <div class="max-w-4xl mx-auto">
        <h2 class="text-3xl font-bold text-center mb-6">联系我们</h2> <!-- 减小间距 mb-8 -> mb-6 -->
        <div v-if="contactInfo" class="bg-white rounded-lg shadow-lg p-8">
          <div class="space-y-4">
            <div class="flex items-start">
              <i class="fas fa-building mt-1 text-primary-500 w-6"></i>
              <div class="ml-4">
                <h3 class="font-semibold">公司地址</h3>
                <p class="text-neutral-600">{{ contactInfo.address }}</p>
              </div>
            </div>
            <div class="flex items-start">
              <i class="fas fa-phone-alt mt-1 text-primary-500 w-6"></i>
              <div class="ml-4">
                <h3 class="font-semibold">联系电话</h3>
                <p v-for="phone in contactInfo.phone" :key="phone" class="text-neutral-600">{{ phone }}</p>
              </div>
            </div>
            <div class="flex items-start">
              <i class="fas fa-user mt-1 text-primary-500 w-6"></i>
              <div class="ml-4">
                <h3 class="font-semibold">联系人</h3>
                <p class="text-neutral-600">{{ contactInfo.contactPerson }}</p>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="text-center p-8">
          <p class="text-neutral-600">正在加载联系信息...</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { useApi } from '~/composables/useApi'

const api = useApi()

const photos = [
  { url: '/images/team/image1.png', alt: '展团照片1' },
  { url: '/images/team/image2.png', alt: '展团照片2' }
]

// 获取联系信息
const { data: contactInfo } = await useAsyncData('contact-info', () => api.contact.getInfo())

useSeoMeta({
  title: '关于我们',
  description: '了解雅博展览：团队、服务与联系方式'
})

</script>

<style scoped>
.aspect-w-4 {
  position: relative;
  padding-bottom: 75%;
}

.aspect-w-4 img {
  position: absolute;
  width: 100%;
  height: 100%;
  left: 0;
  top: 0;
  object-fit: cover;
}
</style> 