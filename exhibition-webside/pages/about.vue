<template>
  <div class="container mx-auto px-4 py-8"> <!-- 减小间距 py-12 -> py-8 -->
    <!-- 公司简介 -->
    <section class="mb-10"> <!-- 减小间距 mb-16 -> mb-10 -->
      <h1 class="text-3xl font-bold text-center mb-8">关于我们</h1> <!-- 减小间距 mb-12 -> mb-8 -->
      <div class="max-w-4xl mx-auto bg-white rounded-lg shadow-lg p-8">
        <p class="text-neutral-600 leading-relaxed mb-6">
          雅博国际展览（天津）有限公司成立于2023年，隶属于北京雅博国际展览有限公司（2017年成立）。我司专业从事境外展会的组展工作，同全球各大贸易展览机构（英富曼、励展、Terrapinn、杜塞尔多夫展览、慕尼黑展览集团等）保持着长期，密切的友好合作。
        </p>
        <p class="text-neutral-600 leading-relaxed mb-6">
          雅博展览团队成员均具有十几年海外展会操作经验。我司项目主要集中在电梯、建材、能源、储能、电动车等领域。我司针对企业需求，提供全方位的、细致周到的参展服务，包括：展会咨询、展位售卖、展位设计装修、机票预订、签证服务、境外商务团队行程服务、以及辅助企业申请相关政策性资金补贴等。
        </p>
        <p class="text-neutral-600 leading-relaxed">
          雅博展览，为参展企业提供定制化一站式出展服务，为国内的生产商和贸易商搭建专业的展览平台，助力企业开拓国际市场。
        </p>
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
        <div class="bg-white rounded-lg shadow-lg p-8">
          <div class="space-y-4">
            <div class="flex items-start">
              <i class="fas fa-building mt-1 text-primary-500 w-6"></i>
              <div class="ml-4">
                <h3 class="font-semibold">公司地址</h3>
                <p class="text-neutral-600">{{ contactInfo.address || '北京市海淀区三里河路9号住建部大院' }}</p>
              </div>
            </div>
            <div class="flex items-start">
              <i class="fas fa-phone-alt mt-1 text-primary-500 w-6"></i>
              <div class="ml-4">
                <h3 class="font-semibold">联系电话</h3>
                <p class="text-neutral-600">{{ contactInfo.phone || '010-53630591/2/3' }}</p>
                <p v-if="contactInfo.mobile" class="text-neutral-600">{{ contactInfo.mobile }}</p>
              </div>
            </div>
            <div class="flex items-start">
              <i class="fas fa-user mt-1 text-primary-500 w-6"></i>
              <div class="ml-4">
                <h3 class="font-semibold">联系人</h3>
                <p class="text-neutral-600">{{ contactInfo.contactPerson || '林琳' }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
interface ContactInfo {
  address: string
  phone: string
  mobile?: string
  contactPerson: string
}

const photos = [
  { url: '/images/team/image1.png', alt: '展团照片1' },
  { url: '/images/team/image2.png', alt: '展团照片2' }
]

// 联系信息
const contactInfo = ref<ContactInfo>({
  address: '',
  phone: '',
  mobile: '',
  contactPerson: ''
})

// 获取联系信息
const fetchContactInfo = async () => {
  try {
    const response = await $fetch<{ data: ContactInfo }>('/api/contact/info')
    if (response.data) {
      contactInfo.value = response.data
    }
  } catch (error) {
    console.error('获取联系信息失败:', error)
    // 使用默认值
    contactInfo.value = {
      address: '北京市海淀区三里河路9号住建部大院',
      phone: '010-53630591/2/3',
      mobile: '18310336602',
      contactPerson: '林琳'
    }
  }
}

onMounted(() => {
  fetchContactInfo()
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