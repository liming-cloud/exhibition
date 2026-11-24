<template>
  <!-- 手机端底部固定联系条（D2）：拨打电话 / 加微信，高 56px；桌面端由页头顶部联系条承担 -->
  <div
    class="md:hidden fixed inset-x-0 bottom-0 z-40 h-14 grid grid-cols-2 border-t border-neutral-200 bg-white pb-[env(safe-area-inset-bottom)]"
  >
    <a
      :href="telHref(contact.mobile || contact.hotline)"
      class="flex items-center justify-center gap-2 bg-primary-600 text-white text-sm font-medium"
    >
      <i class="fas fa-phone-alt"></i>拨打电话
    </a>
    <button
      type="button"
      class="flex items-center justify-center gap-2 text-primary-700 text-sm font-medium"
      @click="qrVisible = true"
    >
      <i class="fab fa-weixin text-base"></i>加微信
    </button>
  </div>

  <ClientOnly>
    <el-dialog v-model="qrVisible" title="添加微信咨询" width="300px" align-center>
      <div class="text-center">
        <img :src="assetUrl(site.qrcode)" alt="微信二维码" class="mx-auto w-44 h-44" />
        <p class="mt-3 text-sm text-neutral-600">长按识别二维码，或搜索微信号</p>
        <p v-if="contact.mobile" class="mt-1 font-medium text-neutral-900">
          {{ contact.mobile }}（手机同号）
        </p>
      </div>
    </el-dialog>
  </ClientOnly>
</template>

<script setup lang="ts">
import { site } from '~/constants/site'

const contact = useSiteContact()
const qrVisible = ref(false)
</script>
