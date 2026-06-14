<template>
  <div class="login-card">
    <div class="login-card-logo">
      <i class="fa-solid fa-graduation-cap"></i>
    </div>

    <h2>ĐĂNG NHẬP</h2>

    <p>
      Chào mừng bạn đến với<br />
      Ứng dụng Trường Trung cấp Phương Nam
    </p>

    <form class="login-form" @submit.prevent="xuLyDangNhap">
      <label class="input-box">
        <i class="fa-solid fa-user"></i>

        <input
            v-model.trim="form.tenDangNhap"
            type="text"
            placeholder="Tên đăng nhập"
            autocomplete="username"
            required
        />
      </label>

      <label class="input-box">
        <i class="fa-solid fa-lock"></i>

        <input
            v-model.trim="form.matKhau"
            :type="hienMatKhau ? 'text' : 'password'"
            placeholder="Mật khẩu"
            autocomplete="current-password"
            required
        />

        <button
            type="button"
            class="show-password-button"
            @click="hienMatKhau = !hienMatKhau"
        >
          <i :class="hienMatKhau ? 'fa-solid fa-eye-slash' : 'fa-solid fa-eye'"></i>
        </button>
      </label>

      <div class="login-options">
        <label>
          <input v-model="form.ghiNho" type="checkbox" />
          <span>Ghi nhớ đăng nhập</span>
        </label>

        <RouterLink :to="{ name: 'Public.LienHe' }">
          Quên mật khẩu?
        </RouterLink>
      </div>

      <p v-if="errorMessage" class="error-text">
        {{ errorMessage }}
      </p>

      <button class="login-submit-button" type="submit" :disabled="loading">
        <template v-if="loading">
          <i class="fa-solid fa-circle-notch fa-spin"></i>
          ĐANG ĐĂNG NHẬP...
        </template>

        <template v-else>
          ĐĂNG NHẬP
        </template>
      </button>

      <button
          type="button"
          class="login-submit-button test-admin-button"
          @click="dangNhapTestAdmin"
      >
        ĐĂNG NHẬP TEST ADMIN
      </button>
    </form>

    <div class="divider">
      <span></span>
      <p>hoặc</p>
      <span></span>
    </div>

    <button type="button" class="google-login-button" @click="dangNhapGoogle">
      <span>G</span>
      Đăng nhập với Google
    </button>

    <div class="register-text">
      Chưa có tài khoản?
      <RouterLink :to="{ name: 'Public.TuyenSinh' }">
        Đăng ký ngay
      </RouterLink>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '@/modules/auth/composables/useAuth.js'

const router = useRouter()

const {
  loading,
  errorMessage,
  login
} = useAuth()

const hienMatKhau = ref(false)

const form = reactive({
  tenDangNhap: '',
  matKhau: '',
  ghiNho: false
})

const taoTokenTestAdmin = () => {
  const header = {
    alg: 'HS256',
    typ: 'JWT'
  }

  const payload = {
    sub: 'admin-test',
    email: 'admin@test.local',
    roles: ['ADMIN'],
    exp: Math.floor(Date.now() / 1000) + 60 * 60 * 24
  }

  const base64Url = (duLieu) => {
    return btoa(JSON.stringify(duLieu))
        .replaceAll('+', '-')
        .replaceAll('/', '_')
        .replaceAll('=', '')
  }

  return `${base64Url(header)}.${base64Url(payload)}.test-admin-signature`
}

const xuLyDangNhap = () => {
  login({
    email: form.tenDangNhap,
    tenDangNhap: form.tenDangNhap,
    username: form.tenDangNhap,
    matKhau: form.matKhau,
    password: form.matKhau,
    ghiNho: form.ghiNho
  })
}

const dangNhapTestAdmin = () => {
  const token = taoTokenTestAdmin()

  const user = {
    id: 'admin-test',
    taiKhoanId: 'admin-test',
    hoTen: 'Admin Test',
    username: 'admin',
    email: 'admin@test.local',
    roles: ['ADMIN'],
    permissions: []
  }

  localStorage.setItem('access_token', token)
  localStorage.setItem('refresh_token', 'refresh-token-test-admin')
  localStorage.setItem('auth_user', JSON.stringify(user))

  router.replace('/admin/public')
}

const dangNhapGoogle = () => {
  window.location.href = 'http://localhost:8080/oauth2/authorization/google'
}
</script>

<style scoped>
.login-card {
  width: min(100%, 560px);
  min-height: 760px;
  padding: 72px 70px 46px;
  border-radius: 36px;
  background: #ffffff;
  box-shadow: 0 24px 70px rgba(0, 22, 80, 0.22);
  text-align: center;
}

.login-card-logo {
  width: 128px;
  height: 128px;
  display: grid;
  place-items: center;
  margin: 0 auto 28px;
  border-radius: 999px;
  background: #eff6ff;
  color: #075fc8;
  font-size: 56px;
}

.login-card h2 {
  margin: 0 0 12px;
  color: #063b7a;
  font-size: 32px;
  font-weight: 900;
  letter-spacing: 0.03em;
}

.login-card > p {
  margin: 0 0 34px;
  color: #64748b;
  font-size: 18px;
  line-height: 1.45;
}

.login-form {
  display: grid;
  gap: 20px;
}

.input-box {
  min-height: 68px;
  display: grid;
  grid-template-columns: 34px 1fr 30px;
  align-items: center;
  gap: 12px;
  border: 1px solid #d1d5db;
  border-radius: 10px;
  padding: 0 18px;
  background: #ffffff;
}

.input-box:focus-within {
  border-color: #075fc8;
  box-shadow: 0 0 0 3px rgba(7, 95, 200, 0.1);
}

.input-box > i {
  color: #64748b;
  font-size: 21px;
}

.input-box input {
  width: 100%;
  border: none;
  outline: none;
  color: #1e293b;
  font-size: 17px;
  font-family: inherit;
}

.input-box input::placeholder {
  color: #a8aeb8;
}

.show-password-button {
  border: none;
  background: transparent;
  color: #64748b;
  font-size: 19px;
  cursor: pointer;
}

.login-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 2px 0 8px;
}

.login-options label {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  color: #4b5563;
  font-size: 16px;
  cursor: pointer;
}

.login-options input {
  width: 22px;
  height: 22px;
  accent-color: #075fc8;
}

.login-options a {
  color: #075fc8;
  font-size: 16px;
  font-weight: 600;
  text-decoration: none;
}

.error-text {
  margin: -8px 0 0;
  padding: 12px 14px;
  border: 1px solid #fecaca;
  border-radius: 12px;
  background: #fef2f2;
  color: #b91c1c;
  font-size: 14px;
  font-weight: 700;
  text-align: left;
}

.login-submit-button {
  min-height: 66px;
  border: none;
  border-radius: 10px;
  background: #075fc8;
  color: #ffffff;
  font-size: 20px;
  font-weight: 900;
  cursor: pointer;
  box-shadow: 0 10px 26px rgba(7, 95, 200, 0.28);
}

.login-submit-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.test-admin-button {
  min-height: 52px;
  background: #063b7a;
  font-size: 15px;
  box-shadow: none;
}

.divider {
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  align-items: center;
  gap: 18px;
  margin: 30px 0;
}

.divider span {
  height: 1px;
  background: #e5e7eb;
}

.divider p {
  margin: 0;
  color: #6b7280;
  font-size: 18px;
}

.google-login-button {
  width: 100%;
  min-height: 66px;
  display: inline-flex;
  justify-content: center;
  align-items: center;
  gap: 14px;
  border: 1px solid #075fc8;
  border-radius: 10px;
  background: #ffffff;
  color: #334155;
  font-size: 18px;
  font-weight: 600;
  cursor: pointer;
}

.google-login-button span {
  color: #ea4335;
  font-size: 26px;
  font-weight: 900;
}

.register-text {
  margin-top: 42px;
  color: #64748b;
  font-size: 18px;
}

.register-text a {
  color: #075fc8;
  font-weight: 800;
  text-decoration: none;
}

@media (max-width: 700px) {
  .login-card {
    min-height: auto;
    padding: 42px 24px;
    border-radius: 24px;
  }

  .login-card-logo {
    width: 96px;
    height: 96px;
    font-size: 42px;
  }

  .login-options {
    align-items: flex-start;
    flex-direction: column;
    gap: 12px;
  }
}
</style>