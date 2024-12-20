<template>
  <div class="bg-white rounded-lg shadow-lg overflow-hidden hover:shadow-xl transition-shadow">
    <img
        :src="exhibition.image"
        :alt="exhibition.title"
        class="w-full h-48 object-cover object-top"
    />
    <div class="p-4">
      <h3 class="text-lg font-semibold mb-2">{{ exhibition.title }}</h3>
      <div class="flex items-center text-gray-600 mb-2">
        <i class="fas fa-map-marker-alt"></i>
        <span class="ml-1">{{ exhibition.location }}</span>
      </div>
      <div class="flex items-center text-gray-600 mb-3">
        <i class="fas fa-calendar"></i>
        <span class="ml-1">{{ exhibition.date }}</span>
      </div>
      <p class="text-gray-600 text-sm line-clamp-2">{{ exhibition.description }}</p>
      <NuxtLink
          class="mt-4 w-full block bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition-colors text-center"
          :to="`/exhibitions/detail/${exhibition.id}`"
      >
        查看详情
      </NuxtLink>
    </div>
  </div>
</template>

<script setup>
defineProps({
  exhibition: {
    type: Object,
    required: true
  }
})
</script>