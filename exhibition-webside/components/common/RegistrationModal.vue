<template>
  <!-- 报名弹窗：展会名回显；标签在上；提交中禁用防重复；成功后原位给出下一步；失败保留已填内容 -->
  <el-dialog
    v-model="visible"
    :title="title"
    width="min(480px, 92vw)"
    align-center
    append-to-body
    @closed="reset"
  >
    <div v-if="exhibition" class="mb-5 rounded-md bg-primary-50 px-4 py-3">
      <p class="font-medium text-neutral-900 line-clamp-2">{{ exhibition.name }}</p>
      <p class="mt-1 text-sm text-neutral-600">
        {{ exhibitionStart(exhibition) }} · {{ exhibition.countryName }}
      </p>
    </div>

    <div v-if="submitted" class="rounded-md bg-emerald-50 px-4 py-5 text-center text-emerald-700">
      <i class="fas fa-check-circle text-3xl"></i>
      <p class="mt-2 font-medium">提交成功，顾问将在 1 个工作日内联系您</p>
      <p v-if="contact.hotline" class="mt-1 text-sm">
        着急可直接致电
        <a :href="telHref(contact.hotline)" class="font-medium underline">{{ contact.hotline }}</a>
      </p>
      <button type="button" class="btn btn-secondary mt-4" @click="visible = false">知道了</button>
    </div>

    <template v-else>
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top" size="large">
        <el-form-item label="公司/个人名称" prop="companyOrPersonalName">
          <el-input v-model="form.companyOrPersonalName" placeholder="请输入公司或个人名称" maxlength="100" />
        </el-form-item>
        <el-form-item label="联系人" prop="contactName">
          <el-input v-model="form.contactName" placeholder="请输入联系人姓名" maxlength="50" />
        </el-form-item>
        <el-form-item label="手机号" prop="contactPhone">
          <el-input v-model="form.contactPhone" placeholder="11 位手机号" maxlength="11" inputmode="tel" />
        </el-form-item>
      </el-form>
      <p v-if="failed" class="mb-3 rounded-md bg-red-50 px-4 py-2 text-sm text-red-700">
        提交失败，请稍后重试；也可以直接电话联系我们
      </p>
      <button type="button" class="btn btn-primary btn-lg w-full" :disabled="submitting" @click="submit">
        <i v-if="submitting" class="fas fa-spinner fa-spin"></i>{{ submitting ? '提交中…' : '提交' }}
      </button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import type { FormInstance, FormRules } from 'element-plus'
import type { Exhibition } from '~/types/exhibition'

// next：已结束展会「咨询下一届」，用同一张表，便于顾问跟进
const props = defineProps<{ exhibition: Exhibition | null; next?: boolean }>()
const visible = defineModel<boolean>({ required: true })

const api = useApi()
const contact = useSiteContact()
const title = computed(() => (props.next ? '咨询下一届' : '展会报名'))

const formRef = ref<FormInstance>()
const form = reactive({ companyOrPersonalName: '', contactName: '', contactPhone: '' })
const rules: FormRules = {
  companyOrPersonalName: [requiredRule('公司或个人名称')],
  contactName: [requiredRule('联系人姓名')],
  contactPhone: [requiredRule('手机号'), mobileRule]
}

const submitting = ref(false)
const submitted = ref(false)
const failed = ref(false)

const submit = async () => {
  if (!props.exhibition || submitting.value) return
  if (!(await formRef.value?.validate().catch(() => false))) return
  submitting.value = true
  failed.value = false
  try {
    await api.exhibition.submitRegistration({ exhibitionId: props.exhibition.id, ...form })
    submitted.value = true
  } catch {
    failed.value = true
  } finally {
    submitting.value = false
  }
}

// 关闭后：提交成功的清空表单；未成功的保留已填内容，方便重试
const reset = () => {
  if (submitted.value) formRef.value?.resetFields()
  submitted.value = false
  failed.value = false
}
</script>
