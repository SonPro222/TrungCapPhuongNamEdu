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

    <div class="login-divider">hoặc</div>

    <GoogleLoginButton />
  </form>
</template>

<script setup>
import { reactive } from 'vue'
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