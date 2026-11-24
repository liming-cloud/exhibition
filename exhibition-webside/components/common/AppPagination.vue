<template>
  <!-- 分页：只有一页时不显示。桌面显示总数；手机端用小尺寸、最多 5 个页码，避免横向溢出 -->
  <div v-if="total > pageSize" class="mt-8 flex justify-center">
    <div class="hidden md:block">
      <el-pagination
        v-model:current-page="page"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        background
      />
    </div>
    <div class="md:hidden">
      <el-pagination
        v-model:current-page="page"
        :page-size="pageSize"
        :total="total"
        :pager-count="5"
        layout="prev, pager, next"
        size="small"
        background
      />
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{ total: number; pageSize: number }>()
const page = defineModel<number>({ required: true })
</script>
