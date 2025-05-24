<template>
    <div class="contact-container">
        <el-card class="box-card">
            <template #header>
                <div class="card-header">
                    <span class="card-title">联系信息管理</span>
                    <el-button type="primary" :icon="Edit" @click="handleEdit">编辑信息</el-button>
                </div>
            </template>

            <!-- 当前联系信息展示 -->
            <div class="contact-info-display">
                <el-descriptions :column="2" border size="large">
                    <el-descriptions-item label="公司地址" :span="2">
                        <div class="info-item">
                            <el-icon>
                                <Location />
                            </el-icon>
                            <span>{{ contactInfo.address || '暂无地址信息' }}</span>
                        </div>
                    </el-descriptions-item>
                    <el-descriptions-item label="联系电话">
                        <div class="info-item">
                            <el-icon>
                                <Phone />
                            </el-icon>
                            <span>{{ contactInfo.phone || '暂无电话信息' }}</span>
                        </div>
                    </el-descriptions-item>
                    <el-descriptions-item label="手机号码">
                        <div class="info-item">
                            <el-icon>
                                <Cellphone />
                            </el-icon>
                            <span>{{ contactInfo.mobile || '暂无手机信息' }}</span>
                        </div>
                    </el-descriptions-item>
                    <el-descriptions-item label="联系人" :span="2">
                        <div class="info-item">
                            <el-icon>
                                <User />
                            </el-icon>
                            <span>{{ contactInfo.contactPerson || '暂无联系人信息' }}</span>
                        </div>
                    </el-descriptions-item>
                    <el-descriptions-item label="最后更新时间" :span="2">
                        <div class="info-item">
                            <el-icon>
                                <Clock />
                            </el-icon>
                            <span>{{ contactInfo.updateTime || '暂无更新时间' }}</span>
                        </div>
                    </el-descriptions-item>
                </el-descriptions>
            </div>

            <!-- 使用说明 -->
            <el-alert title="使用说明" type="info" :closable="false" class="usage-tip">
                <template #default>
                    <p><strong>功能说明：</strong></p>
                    <ul>
                        <li>此页面用于管理官网"关于我们"页面的联系信息</li>
                        <li>修改后的信息将实时同步到官网前台展示</li>
                        <li>建议定期更新联系信息，确保客户能够准确联系到公司</li>
                    </ul>
                </template>
            </el-alert>

            <!-- 编辑对话框 -->
            <el-dialog title="编辑联系信息" v-model="editOpen" width="600px" append-to-body :close-on-click-modal="false">
                <el-form ref="contactFormRef" :model="editForm" :rules="rules" label-width="100px" class="contact-form">
                    <el-form-item label="公司地址" prop="address">
                        <el-input v-model="editForm.address" type="textarea" :rows="3" placeholder="请输入公司详细地址"
                            maxlength="500" show-word-limit />
                    </el-form-item>

                    <el-form-item label="联系电话" prop="phone">
                        <el-input v-model="editForm.phone" placeholder="请输入联系电话，如：010-12345678" maxlength="100" />
                    </el-form-item>

                    <el-form-item label="手机号码" prop="mobile">
                        <el-input v-model="editForm.mobile" placeholder="请输入手机号码，如：138-0000-0000" maxlength="50" />
                    </el-form-item>

                    <el-form-item label="联系人" prop="contactPerson">
                        <el-input v-model="editForm.contactPerson" placeholder="请输入主要联系人姓名" maxlength="100" />
                    </el-form-item>
                </el-form>

                <template #footer>
                    <div class="dialog-footer">
                        <el-button @click="cancelEdit">取消</el-button>
                        <el-button type="primary" :loading="submitLoading" @click="submitEdit">保存修改</el-button>
                    </div>
                </template>
            </el-dialog>
        </el-card>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Edit, Location, Phone, Cellphone, User, Clock } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import { getContactInfo, updateContactInfo, type ContactInfo } from '@/api/contact'

// ContactInfo类型已从API导入

const editOpen = ref(false)
const submitLoading = ref(false)
const contactFormRef = ref<FormInstance>()

// 当前联系信息
const contactInfo = ref<ContactInfo>({
    address: '',
    phone: '',
    mobile: '',
    contactPerson: ''
})

// 编辑表单
const editForm = reactive<ContactInfo>({
    address: '',
    phone: '',
    mobile: '',
    contactPerson: ''
})

// 表单验证规则
const rules: FormRules = {
    address: [
        { required: true, message: '请输入公司地址', trigger: 'blur' },
        { min: 5, max: 500, message: '地址长度应在5-500个字符之间', trigger: 'blur' }
    ],
    phone: [
        { required: true, message: '请输入联系电话', trigger: 'blur' },
        { pattern: /^[\d\-\s\(\)]+$/, message: '请输入有效的电话号码格式', trigger: 'blur' }
    ],
    mobile: [
        { pattern: /^[\d\-\s\(\)]*$/, message: '请输入有效的手机号码格式', trigger: 'blur' }
    ],
    contactPerson: [
        { required: true, message: '请输入联系人姓名', trigger: 'blur' },
        { min: 2, max: 100, message: '联系人姓名长度应在2-100个字符之间', trigger: 'blur' }
    ]
}

// 获取联系信息
const fetchContactInfo = async () => {
    try {
        const result = await getContactInfo()
        if (result.data) {
            contactInfo.value = result.data
        } else {
            // 如果没有数据，使用默认值
            contactInfo.value = {
                address: '北京市海淀区三里河路9号住建部大院',
                phone: '010-53630591/2/3',
                mobile: '18310336602',
                contactPerson: '林琳'
            }
        }
    } catch (error) {
        console.error('获取联系信息失败:', error)
        ElMessage.error('获取联系信息失败')
    }
}

// 编辑联系信息
const handleEdit = () => {
    // 复制当前信息到编辑表单
    Object.assign(editForm, {
        id: contactInfo.value.id,
        address: contactInfo.value.address,
        phone: contactInfo.value.phone,
        mobile: contactInfo.value.mobile,
        contactPerson: contactInfo.value.contactPerson
    })
    editOpen.value = true
}

// 取消编辑
const cancelEdit = () => {
    contactFormRef.value?.resetFields()
    editOpen.value = false
}

// 提交编辑
const submitEdit = async () => {
    if (!contactFormRef.value) return

    try {
        await contactFormRef.value.validate()
        submitLoading.value = true

        await updateContactInfo(editForm)
        ElMessage.success('联系信息更新成功')
        editOpen.value = false
        // 重新获取最新信息
        await fetchContactInfo()
    } catch (error) {
        console.error('更新联系信息失败:', error)
        ElMessage.error('更新联系信息失败')
    } finally {
        submitLoading.value = false
    }
}

onMounted(() => {
    fetchContactInfo()
})
</script>

<style scoped>
.contact-container {
    padding: 20px;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.card-title {
    font-size: 18px;
    font-weight: bold;
    color: #303133;
}

.contact-info-display {
    margin-bottom: 30px;
}

.info-item {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
}

.info-item .el-icon {
    color: #409eff;
    font-size: 16px;
}

.usage-tip {
    margin-top: 30px;
}

.usage-tip ul {
    margin: 10px 0 0 0;
    padding-left: 20px;
}

.usage-tip li {
    margin-bottom: 5px;
    line-height: 1.6;
}

.contact-form .el-form-item {
    margin-bottom: 25px;
}

.dialog-footer {
    text-align: right;
}

:deep(.el-descriptions-item__label) {
    font-weight: bold;
    background-color: #fafafa;
}

:deep(.el-descriptions-item__content) {
    padding: 15px;
}

:deep(.el-alert__content) {
    line-height: 1.6;
}
</style>