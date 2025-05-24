<template>
    <el-dialog v-model="visible" title="展会报名" width="600px" @close="handleClose">
        <!-- 展会信息展示 -->
        <div class="mb-6 p-4 bg-gradient-to-r from-primary-50 to-accent-50 rounded-lg">
            <h4 class="font-bold text-lg text-primary-600 mb-2">{{ exhibition?.name }}</h4>
            <div class="text-sm text-neutral-600 space-y-1">
                <p><i class="fas fa-map-marker-alt mr-2 text-primary-500"></i>{{ exhibition?.countryName }} - {{
                    exhibition?.location }}</p>
                <p><i class="fas fa-calendar-alt mr-2 text-primary-500"></i>{{ exhibition?.time }}</p>
                <p><i class="fas fa-tag mr-2 text-primary-500"></i>{{ ExhibitionTypeMap[exhibition?.exhibitionType] ||
                    '能源展' }}</p>
            </div>
        </div>

        <!-- 报名表单 -->
        <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
            <el-form-item label="公司/个人名称" prop="companyOrPersonalName">
                <el-input v-model="form.companyOrPersonalName" placeholder="请输入公司或个人名称" />
            </el-form-item>

            <el-form-item label="联系人姓名" prop="contactName">
                <el-input v-model="form.contactName" placeholder="请输入联系人姓名" />
            </el-form-item>

            <el-form-item label="联系电话" prop="contactPhone">
                <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
        </el-form>

        <template #footer>
            <div class="dialog-footer">
                <el-button @click="handleClose">取消</el-button>
                <el-button type="primary" @click="handleSubmit" :loading="loading"
                    class="!bg-primary-500 !border-primary-500 hover:!bg-primary-600">
                    提交报名
                </el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup lang="ts">
import type { Exhibition } from '~/types/exhibition'
import { ExhibitionTypeMap } from '~/types/exhibition'
import type { FormInstance, FormRules } from 'element-plus'

interface Props {
    modelValue: boolean
    exhibition: Exhibition | null
}

interface Emits {
    (e: 'update:modelValue', value: boolean): void
    (e: 'submit', data: any): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const visible = computed({
    get: () => props.modelValue,
    set: (value) => emit('update:modelValue', value)
})

const formRef = ref<FormInstance>()
const loading = ref(false)

// 表单数据
const form = reactive({
    exhibitionId: 0,
    companyOrPersonalName: '',
    contactName: '',
    contactPhone: ''
})

// 表单验证规则
const rules: FormRules = {
    companyOrPersonalName: [
        { required: true, message: '请输入公司或个人名称', trigger: 'blur' }
    ],
    contactName: [
        { required: true, message: '请输入联系人姓名', trigger: 'blur' }
    ],
    contactPhone: [
        { required: true, message: '请输入联系电话', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
    ]
}

// 监听展会信息变化
watch(() => props.exhibition, (newExhibition) => {
    if (newExhibition) {
        form.exhibitionId = newExhibition.id!
    }
}, { immediate: true })

// 提交表单
const handleSubmit = async () => {
    if (!formRef.value) return

    try {
        await formRef.value.validate()
        loading.value = true
        emit('submit', { ...form })
    } catch (error) {
        console.error('表单验证失败:', error)
    } finally {
        loading.value = false
    }
}

// 关闭弹窗
const handleClose = () => {
    // 重置表单
    if (formRef.value) {
        formRef.value.resetFields()
    }
    Object.assign(form, {
        exhibitionId: 0,
        companyOrPersonalName: '',
        contactName: '',
        contactPhone: ''
    })
    emit('update:modelValue', false)
}
</script>