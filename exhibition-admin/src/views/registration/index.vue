<template>
    <div class="registration-container">
        <el-card class="box-card">
            <!-- 搜索区域 -->
            <el-form :model="queryParams" ref="queryRef" :inline="true">
                <el-form-item label="展会名称" prop="exhibitionName">
                    <el-input v-model="queryParams.exhibitionName" placeholder="请输入展会名称" clearable style="width: 200px"
                        @keyup.enter="handleQuery" />
                </el-form-item>
                <el-form-item label="联系人" prop="contactName">
                    <el-input v-model="queryParams.contactName" placeholder="请输入联系人" clearable style="width: 160px"
                        @keyup.enter="handleQuery" />
                </el-form-item>
                <el-form-item label="联系进度" prop="contactProgress">
                    <el-select v-model="queryParams.contactProgress" placeholder="请选择联系进度" clearable
                        style="width: 140px">
                        <el-option v-for="(label, value) in ContactProgressMap" :key="value" :label="label"
                            :value="value" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                    <el-button icon="Refresh" @click="resetQuery">重置</el-button>
                </el-form-item>
            </el-form>

            <!-- 统计信息 -->
            <el-row :gutter="20" class="mb8">
                <el-col :span="6">
                    <el-statistic title="总报名数" :value="statistics.total" class="statistic-card" />
                </el-col>
                <el-col :span="6">
                    <el-statistic title="未联系" :value="statistics.uncontacted" class="statistic-card" />
                </el-col>
                <el-col :span="6">
                    <el-statistic title="已联系" :value="statistics.contacted" class="statistic-card" />
                </el-col>
                <el-col :span="6">
                    <el-statistic title="已成交" :value="statistics.closed" class="statistic-card" />
                </el-col>
            </el-row>

            <!-- 表格区域 -->
            <el-table v-loading="loading" :data="registrationList" style="width: 100%" border>
                <el-table-column type="index" label="序号" width="60" />
                <el-table-column prop="exhibitionName" label="展会名称" min-width="200" show-overflow-tooltip />
                <el-table-column prop="companyOrPersonalName" label="公司/个人名称" min-width="160" />
                <el-table-column prop="contactName" label="联系人" width="100" />
                <el-table-column prop="contactPhone" label="联系电话" width="130" />
                <el-table-column prop="contactProgress" label="联系进度" width="100" align="center">
                    <template #default="scope">
                        <el-tag :type="ContactProgressTagMap[scope.row.contactProgress]" size="small">
                            {{ ContactProgressMap[scope.row.contactProgress] }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" label="报名时间" width="160" />
                <el-table-column label="操作" width="200" align="center" fixed="right">
                    <template #default="scope">
                        <el-button type="primary" link icon="View" @click="handleDetail(scope.row)">详情</el-button>
                        <el-button type="warning" link icon="Edit" @click="handleProgress(scope.row)">进度</el-button>
                        <el-button type="danger" link icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <!-- 分页 -->
            <div class="pagination-container">
                <Pagination v-if="total > 0" :total="total" :page="queryParams.pageNum" :limit="queryParams.pageSize"
                    @pagination="handlePagination" />
            </div>

            <!-- 详情对话框 -->
            <el-dialog title="报名详情" v-model="detailOpen" width="600px" append-to-body>
                <el-descriptions :column="2" border>
                    <el-descriptions-item label="展会名称" :span="2">{{ detailForm.exhibitionName }}</el-descriptions-item>
                    <el-descriptions-item label="公司/个人名称" :span="2">{{ detailForm.companyOrPersonalName
                        }}</el-descriptions-item>
                    <el-descriptions-item label="联系人">{{ detailForm.contactName }}</el-descriptions-item>
                    <el-descriptions-item label="联系电话">{{ detailForm.contactPhone }}</el-descriptions-item>
                    <el-descriptions-item label="联系进度">
                        <el-tag :type="ContactProgressTagMap[detailForm.contactProgress]">
                            {{ ContactProgressMap[detailForm.contactProgress] }}
                        </el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="报名时间">{{ detailForm.createTime }}</el-descriptions-item>
                </el-descriptions>
                <template #footer>
                    <el-button @click="detailOpen = false">关闭</el-button>
                </template>
            </el-dialog>

            <!-- 更新进度对话框 -->
            <el-dialog title="更新联系进度" v-model="progressOpen" width="400px" append-to-body>
                <el-form :model="progressForm" label-width="100px">
                    <el-form-item label="联系人">
                        <span>{{ progressForm.contactName }}</span>
                    </el-form-item>
                    <el-form-item label="联系电话">
                        <span>{{ progressForm.contactPhone }}</span>
                    </el-form-item>
                    <el-form-item label="当前进度">
                        <el-tag :type="ContactProgressTagMap[progressForm.currentProgress]">
                            {{ ContactProgressMap[progressForm.currentProgress] }}
                        </el-tag>
                    </el-form-item>
                    <el-form-item label="更新进度" required>
                        <el-select v-model="progressForm.newProgress" placeholder="请选择新的联系进度">
                            <el-option v-for="(label, value) in ContactProgressMap" :key="value" :label="label"
                                :value="value" />
                        </el-select>
                    </el-form-item>
                </el-form>
                <template #footer>
                    <el-button type="primary" @click="submitProgress">确认更新</el-button>
                    <el-button @click="progressOpen = false">取消</el-button>
                </template>
            </el-dialog>
        </el-card>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import Pagination from '@/components/Pagination/index.vue'
import { listRegistration, updateProgress, delRegistration } from '@/api/registration'
import type { Registration, RegistrationQuery, ContactProgress } from '@/types/registration'
import { ContactProgressMap, ContactProgressTagMap } from '@/types/registration'

// 统计数据接口
interface Statistics {
    total: number
    uncontacted: number
    contacted: number
    closed: number
}

const loading = ref(false)
const total = ref(0)
const detailOpen = ref(false)
const progressOpen = ref(false)
const registrationList = ref<Registration[]>([])

// 统计数据
const statistics = ref<Statistics>({
    total: 0,
    uncontacted: 0,
    contacted: 0,
    closed: 0
})

// 查询参数
const queryParams = reactive<RegistrationQuery>({
    pageNum: 1,
    pageSize: 10,
    exhibitionName: undefined,
    contactName: undefined,
    contactProgress: undefined
})

// 详情表单
const detailForm = ref<Registration>({} as Registration)

// 进度更新表单
const progressForm = reactive({
    id: 0,
    contactName: '',
    contactPhone: '',
    currentProgress: '' as ContactProgress,
    newProgress: '' as ContactProgress
})

const queryRef = ref<FormInstance>()

// 删除getProgressTagType函数，使用ContactProgressTagMap替代

// 获取报名列表
const getList = async () => {
    loading.value = true
    try {
        const result = await listRegistration(queryParams)
        registrationList.value = result.data.rows || []
        total.value = result.data.total || 0

        // 计算统计数据
        calculateStatistics()
    } catch (error) {
        console.error('获取报名列表失败:', error)
        ElMessage.error('获取报名列表失败')
    } finally {
        loading.value = false
    }
}

// 计算统计数据
const calculateStatistics = () => {
    statistics.value.total = registrationList.value.length
    statistics.value.uncontacted = registrationList.value.filter(item => item.contactProgress === ContactProgress.UNCONTACTED).length
    statistics.value.contacted = registrationList.value.filter(item => [ContactProgress.CONTACTED, ContactProgress.REPLIED].includes(item.contactProgress)).length
    statistics.value.closed = registrationList.value.filter(item => item.contactProgress === ContactProgress.CLOSED).length
}

// 搜索
const handleQuery = () => {
    queryParams.pageNum = 1
    getList()
}

// 重置搜索
const resetQuery = () => {
    queryRef.value?.resetFields()
    queryParams.pageNum = 1
    getList()
}

// 分页
const handlePagination = (page: { page: number; limit: number }) => {
    queryParams.pageNum = page.page
    queryParams.pageSize = page.limit
    getList()
}

// 查看详情
const handleDetail = (row: Registration) => {
    detailForm.value = { ...row }
    detailOpen.value = true
}

// 更新进度
const handleProgress = (row: Registration) => {
    progressForm.id = row.id
    progressForm.contactName = row.contactName
    progressForm.contactPhone = row.contactPhone
    progressForm.currentProgress = row.contactProgress
    progressForm.newProgress = row.contactProgress
    progressOpen.value = true
}

// 提交进度更新
const submitProgress = async () => {
    if (!progressForm.newProgress) {
        ElMessage.warning('请选择新的联系进度')
        return
    }

    try {
        await updateProgress(progressForm.id, progressForm.newProgress)
        ElMessage.success('更新成功')
        progressOpen.value = false
        getList()
    } catch (error) {
        console.error('更新进度失败:', error)
        ElMessage.error('更新进度失败')
    }
}

// 删除
const handleDelete = (row: Registration) => {
    ElMessageBox.confirm(
        `确认删除联系人"${row.contactName}"的报名记录吗？`,
        '系统提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }
    ).then(async () => {
        try {
            await delRegistration(row.id)
            ElMessage.success('删除成功')
            getList()
        } catch (error) {
            console.error('删除失败:', error)
            ElMessage.error('删除失败')
        }
    })
}

onMounted(() => {
    getList()
})
</script>

<style scoped>
.registration-container {
    padding: 20px;
}

.statistic-card {
    padding: 20px;
    background: #f5f5f5;
    border-radius: 8px;
    text-align: center;
}

.mb8 {
    margin-bottom: 8px;
}

.pagination-container {
    margin-top: 20px;
    text-align: right;
}
</style>