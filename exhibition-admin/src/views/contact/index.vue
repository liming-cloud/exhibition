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
                    <el-descriptions-item label="公司简介" :span="2">
                        <div class="info-item">
                            <el-icon><Tickets /></el-icon>
                            <pre class="info-pre">{{ contactInfo.companyProfile || '暂无公司简介' }}</pre>
                        </div>
                    </el-descriptions-item>
                    <el-descriptions-item label="公司地址" :span="2">
                        <div class="info-item">
                            <el-icon><Location /></el-icon>
                            <span>{{ contactInfo.address || '暂无地址信息' }}</span>
                        </div>
                    </el-descriptions-item>
                    <el-descriptions-item label="联系电话" :span="2">
                        <div class="info-item" v-for="(p, index) in contactInfo.phone" :key="index">
                            <el-icon><Phone /></el-icon>
                            <span>{{ p }}</span>
                        </div>
                         <div v-if="!contactInfo.phone || contactInfo.phone.length === 0" class="info-item">
                            <el-icon><Phone /></el-icon>
                            <span>暂无电话信息</span>
                        </div>
                    </el-descriptions-item>
                    <el-descriptions-item label="联系人">
                        <div class="info-item">
                            <el-icon><User /></el-icon>
                            <span>{{ contactInfo.contactPerson || '暂无联系人信息' }}</span>
                        </div>
                    </el-descriptions-item>
                    <el-descriptions-item label="邮箱">
                        <div class="info-item">
                            <el-icon><Message /></el-icon>
                            <span>{{ contactInfo.email || '暂无邮箱信息' }}</span>
                        </div>
                    </el-descriptions-item>
                     <el-descriptions-item label="工作时间">
                        <div class="info-item">
                            <el-icon><Clock /></el-icon>
                            <span>{{ contactInfo.workingHours || '暂无工作时间' }}</span>
                        </div>
                    </el-descriptions-item>
                    <el-descriptions-item label="节假日安排">
                        <div class="info-item">
                            <el-icon><Calendar /></el-icon>
                            <span>{{ contactInfo.holidayArrangement || '暂无节假日安排' }}</span>
                        </div>
                    </el-descriptions-item>
                    <el-descriptions-item label="最后更新时间" :span="2">
                        <div class="info-item">
                            <el-icon><Clock /></el-icon>
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
            <el-dialog title="编辑联系信息" v-model="editOpen" width="700px" append-to-body :close-on-click-modal="false">
                <el-form ref="contactFormRef" :model="editForm" :rules="rules" label-width="120px" class="contact-form">
                    <el-form-item label="公司简介" prop="companyProfile">
                        <el-input v-model="editForm.companyProfile" type="textarea" :rows="5" placeholder="请输入公司简介"
                            maxlength="2000" show-word-limit />
                    </el-form-item>
                    <el-form-item label="公司地址" prop="address">
                        <el-input v-model="editForm.address" type="textarea" :rows="3" placeholder="请输入公司详细地址"
                            maxlength="500" show-word-limit />
                    </el-form-item>

                    <el-form-item v-for="(_, index) in editForm.phone" :key="index" :label="`联系电话 ${index + 1}`" :prop="`phone.${index}`" :rules="rules.phone_item">
                         <el-input v-model="editForm.phone[index]" placeholder="请输入联系电话" style="width: 80%; margin-right: 10px;"></el-input>
                         <el-button @click.prevent="removePhone(index)" type="danger" :icon="Delete"></el-button>
                    </el-form-item>
                     <el-form-item>
                        <el-button @click="addPhone" type="primary" plain>添加电话</el-button>
                    </el-form-item>

                    <el-form-item label="联系人" prop="contactPerson">
                        <el-input v-model="editForm.contactPerson" placeholder="请输入主要联系人姓名" maxlength="100" />
                    </el-form-item>
                    <el-form-item label="邮箱" prop="email">
                        <el-input v-model="editForm.email" placeholder="请输入联系邮箱" maxlength="100" />
                    </el-form-item>
                    <el-form-item label="工作时间" prop="workingHours">
                        <el-input v-model="editForm.workingHours" placeholder="例如：周一至周五 9:00-18:00" maxlength="100" />
                    </el-form-item>
                    <el-form-item label="节假日安排" prop="holidayArrangement">
                        <el-input v-model="editForm.holidayArrangement" placeholder="例如：节假日需提前预约" maxlength="100" />
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
import { onMounted, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { Calendar, Clock, Delete, Edit, Location, Message, Phone, Tickets, User } from '@element-plus/icons-vue'
import { type ContactInfo, getContactInfo, updateContactInfo } from '@/api/contact'

const editOpen = ref(false)
const submitLoading = ref(false)
const contactFormRef = ref<FormInstance>()

// 当前联系信息
const contactInfo = ref<ContactInfo>({
  address: '',
  phone: [],
  contactPerson: '',
  companyProfile: '',
  email: '',
  workingHours: '',
  holidayArrangement: ''
})

// 编辑表单
const editForm = reactive<ContactInfo>({
  address: '',
  phone: [],
  contactPerson: '',
  companyProfile: '',
  email: '',
  workingHours: '',
  holidayArrangement: ''
})

// 表单验证规则
const rules = reactive<FormRules>({
  companyProfile: [
      { required: true, message: '请输入公司简介', trigger: 'blur' }
  ],
  address: [
    { required: true, message: '请输入公司地址', trigger: 'blur' },
    { min: 5, max: 500, message: '地址长度应在5-500个字符之间', trigger: 'blur' }
  ],
  phone_item: [ // 对电话数组中的每个项目应用此规则
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^[\d\-\s()]+$/, message: '请输入有效的电话号码格式', trigger: 'blur' }
  ],
  contactPerson: [
    { required: true, message: '请输入联系人姓名', trigger: 'blur' },
    { min: 2, max: 100, message: '联系人姓名长度应在2-100个字符之间', trigger: 'blur' }
  ],
  email: [
      { required: true, message: '请输入邮箱地址', trigger: 'blur' },
      { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
  ],
  workingHours: [
      { required: true, message: '请输入工作时间', trigger: 'blur' }
  ],
  holidayArrangement: [
      { required: true, message: '请输入节假日安排', trigger: 'blur' }
  ]
})

// 获取联系信息
const fetchContactInfo = async () => {
  try {
    const result = await getContactInfo()
    if (result.data) {
      contactInfo.value = result.data
    }
  }
  catch (error) {
    console.error('获取联系信息失败:', error)
    ElMessage.error('获取联系信息失败')
  }
}

// 编辑联系信息
const handleEdit = () => {
  // 深拷贝，防止修改 editForm 时影响到 contactInfo
  editForm.id = contactInfo.value.id
  editForm.address = contactInfo.value.address
  editForm.phone = JSON.parse(JSON.stringify(contactInfo.value.phone || []))
  editForm.contactPerson = contactInfo.value.contactPerson
  editForm.companyProfile = contactInfo.value.companyProfile
  editForm.email = contactInfo.value.email
  editForm.workingHours = contactInfo.value.workingHours
  editForm.holidayArrangement = contactInfo.value.holidayArrangement
  
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
    await fetchContactInfo()
  }
  catch (error) {
    console.error('更新联系信息失败:', error)
    ElMessage.error('更新联系信息失败')
  }
  finally {
    submitLoading.value = false
  }
}

// 添加电话
const addPhone = () => {
  editForm.phone.push('')
}

// 移除电话
const removePhone = (index: number) => {
  editForm.phone.splice(index, 1)
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