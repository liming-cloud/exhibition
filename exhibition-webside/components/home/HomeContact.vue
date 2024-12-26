<template>
  <div class="bg-gray-100 py-16">
    <div class="container mx-auto px-4">
      <h2 class="text-3xl font-bold text-center mb-12">联系我们</h2>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-12">
        <!-- 联系信息 -->
        <div class="space-y-6">
          <div class="flex items-center">
            <i class="fas fa-map-marker-alt text-2xl text-blue-600 w-8"></i>
            <span class="text-gray-700">地址：北京市朝阳区建国路88号</span>
          </div>
          <div class="flex items-center">
            <i class="fas fa-phone text-2xl text-blue-600 w-8"></i>
            <span class="text-gray-700">电话：010-88888888</span>
          </div>
          <div class="flex items-center">
            <i class="fas fa-envelope text-2xl text-blue-600 w-8"></i>
            <span class="text-gray-700">邮箱：contact@exhibition.com</span>
          </div>
          <div class="flex items-center">
            <i class="fas fa-clock text-2xl text-blue-600 w-8"></i>
            <span class="text-gray-700">工作时间：周一至周五 9:00-18:00</span>
          </div>
        </div>

        <!-- 联系表单 -->
        <div class="bg-white rounded-lg shadow-md p-8">
          <form class="space-y-6">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">姓名</label>
              <input type="text" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">电话</label>
              <input type="tel" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">邮箱</label>
              <input type="email" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">留言</label>
              <textarea rows="4" class="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500"></textarea>
            </div>
            <button type="submit" class="w-full bg-blue-600 text-white py-3 rounded-lg hover:bg-blue-700 transition-colors">
              提交
            </button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template> 