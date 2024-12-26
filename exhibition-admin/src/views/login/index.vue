<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { LoginForm } from '@/types'

const router = useRouter()
const route = useRoute()

const loginForm = reactive<LoginForm>({
  username: '',
  password: ''
})

const loading = ref(false)

const handleLogin = () => {
  if (!loginForm.username || !loginForm.password) {
    ElMessage.error('请输入用户名和密码')
    return
  }
  
  loading.value = true
  // 模拟登录，这里使用前端写死的账号密码
  if (loginForm.username === 'admin' && loginForm.password === '***REMOVED***') {
    localStorage.setItem('token', 'mock-token')
    ElMessage.success('登录成功')
    const redirect = route.query.redirect as string
    router.push(redirect || '/admin')
  } else {
    ElMessage.error('用户名或密码错误')
  }
  loading.value = false
}
</script>

<template>
  <div class="login-container">
    <div class="login-box">
      <h2>展会管理系统</h2>
      <el-form :model="loginForm" class="login-form">
        <el-form-item>
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            prefix-icon="User"
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            prefix-icon="Lock"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            :loading="loading"
            type="primary"
            class="login-button"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f5f5;
}

.login-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.login-box h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #409EFF;
}

.login-form {
  margin-top: 30px;
}

.login-button {
  width: 100%;
}

.login-tips {
  margin-top: 20px;
  text-align: center;
  color: #909399;
  font-size: 14px;
}

.login-tips p {
  margin: 5px 0;
}
</style> 