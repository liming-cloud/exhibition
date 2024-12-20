<template>
  <footer class="bg-gray-800 text-gray-300 py-12">
    <div class="max-w-7xl mx-auto px-4">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
        <!-- 联系我们 -->
        <div>
          <h3 class="text-lg font-semibold mb-4">联系我们</h3>
          <div class="space-y-2 text-sm">
            <p>电话：010-53630591/2/3 18310336602</p>
            <p>地址：北京市海淀区三里河路 9 号住建部大院</p>
          </div>
        </div>
        <!-- 服务项目 -->
        <div>
          <h3 class="text-lg font-semibold mb-4">服务项目</h3>
          <div class="space-y-2 text-sm">
            <p>展会信息咨询</p>
            <p>展位设计搭建</p>
            <p>展会物流服务</p>
            <p>展会推广营销</p>
          </div>
        </div>
        <!-- 快速链接 -->
        <div>
          <h3 class="text-lg font-semibold mb-4">快速链接</h3>
          <div class="space-y-2 text-sm">
            <div>
              <NuxtLink to="/about" class="hover:text-white">关于我们</NuxtLink>
            </div>
            <div>
              <NuxtLink to="/news" class="hover:text-white">新闻中心</NuxtLink>
            </div>
            <div>
              <NuxtLink to="/exhibitions" class="hover:text-white">展会日历</NuxtLink>
            </div>
          </div>
        </div>
        <!-- 关注我们 -->
        <div>
          <h3 class="text-lg font-semibold mb-4">关注我们</h3>
          <div class="flex space-x-4">
            <a href="#"><i class="fab fa-weixin text-2xl"></i></a>
            <a href="#"><i class="fas fa-envelope text-2xl"></i></a>
            <a href="#"><i class="fas fa-share-alt text-2xl"></i></a>
          </div>
        </div>
      </div>
      <div class="mt-8 pt-8 border-t border-gray-700 text-center text-sm">
        <p>© 2024 雅博国际展览（天津）有限公司 保留所有权利.</p>
      </div>
    </div>
  </footer>
</template>
<script setup lang="ts">
</script>