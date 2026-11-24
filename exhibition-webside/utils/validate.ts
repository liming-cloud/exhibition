import type { FormItemRule } from 'element-plus'

// 表单通用校验规则（报名表等），配合 el-form 的 rules 使用
export const requiredRule = (label: string): FormItemRule => ({
  required: true,
  message: `请输入${label}`,
  trigger: 'blur'
})

export const mobileRule: FormItemRule = {
  pattern: /^1[3-9]\d{9}$/,
  message: '请输入 11 位手机号',
  trigger: 'blur'
}
