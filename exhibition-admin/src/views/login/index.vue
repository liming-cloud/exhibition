<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="login-header">
          <img src="https://placeholder.co/50x50" class="login-logo" alt="logo" />
          <h2 class="login-title">管理系统</h2>
        </div>
      </template>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" class="login-form">
        <el-form-item prop="username">
          <el-input 
            v-model="loginForm.username" 
            placeholder="用户名" 
            :prefix-icon="User"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            placeholder="密码" 
            :prefix-icon="Lock"
            size="large"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button 
            type="primary" 
            :loading="loading" 
            @click="handleLogin" 
            class="login-button"
            size="large"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'

defineOptions({
  name: 'LoginPage'
})

interface LoginForm {
  username: string;
  password: string;
}

const store = useStore()
const router = useRouter()
const loginFormRef = ref<FormInstance>()
const loading = ref<boolean>(false)

const loginForm = reactive<LoginForm>({
  username: '',
  password: ''
})

const rules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async (): Promise<void> => {
  if (!loginFormRef.value) return
  
  const valid = await loginFormRef.value.validate()
  if (valid) {
    loading.value = true
    try {
      await store.dispatch('login', loginForm)
      ElMessage.success('登录成功')
      router.push('/')
    } catch (error) {
      ElMessage.error(error as string)
    } finally {
      loading.value = false
    }
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #1890ff 0%, #1d39c4 100%);
  background-size: cover;
}

.login-card {
  width: 420px;
  border-radius: 8px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
}

.login-header {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
}

.login-logo {
  width: 40px;
  height: 40px;
  margin-right: 12px;
  border-radius: 50%;
}

.login-title {
  margin: 0;
  color: #1890ff;
  font-size: 24px;
  font-weight: 600;
}

.login-form {
  padding: 20px 30px 30px;
}

:deep(.el-input__wrapper) {
  box-shadow: 0 0 0 1px #dcdfe6 inset;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #1890ff inset;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #1890ff inset !important;
}

.login-button {
  width: 100%;
  height: 40px;
  font-size: 16px;
  background: linear-gradient(90deg, #1890ff 0%, #1d39c4 100%);
  border: none;
  margin-top: 10px;
}

.login-button:hover {
  background: linear-gradient(90deg, #40a9ff 0%, #2f54eb 100%);
  transform: translateY(-1px);
  transition: all 0.3s;
}
</style> 