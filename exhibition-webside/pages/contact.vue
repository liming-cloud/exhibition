<template>
  <div class="min-h-screen bg-white">
    <!-- 页面标题 -->
    <section class="relative py-24 overflow-hidden">
      <div class="container relative z-10">
        <div class="text-center">
          <h1 class="text-4xl md:text-5xl font-bold mb-6 bg-gradient-to-r from-primary-500 to-primary-600 bg-clip-text text-transparent">
            联系我们
          </h1>
          <p class="text-lg text-neutral-600 max-w-2xl mx-auto">
            雅博展览，专注于为企业提供专业的展会服务，助力开拓国际市场
          </p>
        </div>
      </div>
      <!-- 装饰背景 -->
      <div class="absolute inset-0 pointer-events-none">
        <div class="absolute top-0 left-1/2 -translate-x-1/2 w-[800px] h-[600px] bg-gradient-to-b from-primary-100/50 to-transparent rounded-[60px] rotate-12 transform -translate-y-1/2"></div>
        <div class="absolute top-0 left-1/2 -translate-x-1/2 w-[800px] h-[600px] bg-gradient-to-b from-accent-100/30 to-transparent rounded-[60px] -rotate-12 transform -translate-y-1/2"></div>
      </div>
    </section>

    <div class="container pb-24">
      <div class="max-w-3xl mx-auto">
        <!-- 公司简介 -->
        <div v-if="contactInfo" class="bg-white rounded-3xl shadow-modern p-8 mb-8 hover:shadow-glow transition-shadow duration-300">
          <div class="flex items-center mb-6">
            <div class="w-1.5 h-8 bg-gradient-to-b from-primary-400 to-primary-600 rounded-full mr-4"></div>
            <h3 class="text-2xl font-bold text-neutral-900">关于我们</h3>
          </div>
          <div class="space-y-4">
            <p class="text-neutral-700 leading-relaxed" v-html="contactInfo.companyProfile?.replace(/\n/g, '<br>')"></p>
          </div>
        </div>

        <!-- 联系方式 -->
        <div v-if="contactInfo" class="bg-white rounded-3xl shadow-modern p-8 hover:shadow-glow transition-shadow duration-300">
          <div class="flex items-center mb-8">
            <div class="w-1.5 h-8 bg-gradient-to-b from-primary-400 to-primary-600 rounded-full mr-4"></div>
            <h3 class="text-2xl font-bold text-neutral-900">联系方式</h3>
          </div>
          
          <div class="grid gap-8">
            <div class="flex items-start group">
              <div class="w-14 h-14 rounded-2xl bg-gradient-to-br from-primary-400 to-primary-600 flex items-center justify-center flex-shrink-0 shadow-glow transition-all duration-300 group-hover:scale-110">
                <i class="fas fa-building text-white text-xl"></i>
              </div>
              <div class="ml-5">
                <h4 class="text-lg font-semibold text-neutral-900 mb-2">公司地址</h4>
                <p class="text-neutral-700">{{ contactInfo.address }}</p>
              </div>
            </div>

            <div class="flex items-start group">
              <div class="w-14 h-14 rounded-2xl bg-gradient-to-br from-primary-400 to-primary-600 flex items-center justify-center flex-shrink-0 shadow-glow transition-all duration-300 group-hover:scale-110">
                <i class="fas fa-phone-alt text-white text-xl"></i>
              </div>
              <div class="ml-5">
                <h4 class="text-lg font-semibold text-neutral-900 mb-2">联系电话</h4>
                <p v-for="phone in contactInfo.phone" :key="phone" class="text-neutral-700">{{ phone }}</p>
              </div>
            </div>

            <div class="flex items-start group">
              <div class="w-14 h-14 rounded-2xl bg-gradient-to-br from-primary-400 to-primary-600 flex items-center justify-center flex-shrink-0 shadow-glow transition-all duration-300 group-hover:scale-110">
                <i class="fas fa-user text-white text-xl"></i>
              </div>
              <div class="ml-5">
                <h4 class="text-lg font-semibold text-neutral-900 mb-2">联系人</h4>
                <p class="text-neutral-700">{{ contactInfo.contactPerson }}</p>
              </div>
            </div>

            <div class="flex items-start group">
              <div class="w-14 h-14 rounded-2xl bg-gradient-to-br from-primary-400 to-primary-600 flex items-center justify-center flex-shrink-0 shadow-glow transition-all duration-300 group-hover:scale-110">
                <i class="fas fa-clock text-white text-xl"></i>
              </div>
              <div class="ml-5">
                <h4 class="text-lg font-semibold text-neutral-900 mb-2">工作时间</h4>
                <p class="text-neutral-700">{{ contactInfo.workingHours }}</p>
                <p class="text-neutral-700">{{ contactInfo.holidayArrangement }}</p>
              </div>
            </div>

            <div class="flex items-start group">
              <div class="w-14 h-14 rounded-2xl bg-gradient-to-br from-primary-400 to-primary-600 flex items-center justify-center flex-shrink-0 shadow-glow transition-all duration-300 group-hover:scale-110">
                <i class="fas fa-envelope text-white text-xl"></i>
              </div>
              <div class="ml-5">
                <h4 class="text-lg font-semibold text-neutral-900 mb-2">业务咨询</h4>
                <p class="text-neutral-700">如果您有任何业务咨询，欢迎通过电话或邮件与我们联系</p>
                <p class="text-neutral-700 mt-2">邮箱：{{ contactInfo.email }}</p>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 加载状态 -->
        <div v-else class="text-center p-8">
          <p class="text-neutral-600">正在加载联系信息...</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useApi } from '~/composables/useApi'

const api = useApi()

// 获取联系信息
const { data: contactInfo } = await useAsyncData('contact-info', () => api.contact.getInfo())

useSeoMeta({
  title: '联系我们',
  description: '雅博展览联系方式：地址、电话与联系人'
})

</script> 