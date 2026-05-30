<template>
  <form class="login-box" @submit.prevent="handleLogin">
    <h1>Đăng nhập</h1>

    <label>Email</label>
    <input
        v-model="form.email"
        type="email"
        autocomplete="username"
        placeholder="Nhập email"
        required
    />

    <label>Mật khẩu</label>
    <input
        v-model="form.matKhau"
        type="password"
        autocomplete="current-password"
        placeholder="Nhập mật khẩu"
        required
    />

    <p v-if="errorMessage" class="error-text">
      {{ errorMessage }}
    </p>

    <button type="submit" :disabled="loading">
      {{ loading ? 'Đang đăng nhập...' : 'Đăng nhập' }}
    </button>

    <RouterLink class="forgot-link" to="/auth/doi-mat-khau-sinh-vien">
      Sinh viên quên mật khẩu / đổi mật khẩu bằng OTP
    </RouterLink>

    <div class="login-divider">hoặc</div>

    <GoogleLoginButton />
  </form>
</template>

<script setup>
import { reactive } from 'vue'
import { RouterLink } from 'vue-router'
import { useAuth } from '../composables/useAuth'
import GoogleLoginButton from '../components/GoogleLoginButton.vue'

const {
  loading,
  errorMessage,
  login
} = useAuth()

const form = reactive({
  email: '',
  matKhau: ''
})

function handleLogin() {
  login({
    email: form.email,
    matKhau: form.matKhau
  })
}
</script>

<style scoped>
.forgot-link {
  display: block;
  margin-top: 10px;
  text-align: center;
  color: #2563eb;
  text-decoration: none;
  font-weight: 700;
}
</style>
