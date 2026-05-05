<!-- src/components/login/LoginPanel.vue -->
<template>
  <div class="login-card">
    <div class="login-head">
      <div class="logo">PN</div>
      <div>
        <h2>Đăng nhập</h2>
        <p>Tài khoản đã kích hoạt</p>
      </div>
    </div>

    <form class="login-form" @submit.prevent="handleLogin">
      <div class="form-group">
        <label>Email</label>
        <input
            v-model.trim="form.email"
            type="email"
            placeholder="email@example.com"
            autocomplete="username"
            required
        />
      </div>

      <div class="form-group">
        <label>Mật khẩu</label>
        <input
            v-model="form.password"
            type="password"
            placeholder="Nhập mật khẩu"
            autocomplete="current-password"
            required
        />
      </div>

      <p v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </p>

      <button class="login-button" type="submit" :disabled="loading">
        {{ loading ? 'Đang đăng nhập...' : 'Đăng nhập' }}
      </button>

      <button class="google-button" type="button" @click="handleGoogleLogin">
        <span>G</span>
        Google
      </button>
    </form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { loginGoogle, loginThuong } from '@/api/auth/authApi.js';
import {
  extractLoginPayload,
  extractRole,
  extractStatus,
  extractToken,
  getHomeByRole,
} from '@/utils/authUtils.js';

const router = useRouter();

const loading = ref(false);
const errorMessage = ref('');

const form = reactive({
  email: '',
  password: '',
});

const isActiveAccount = (status) => {
  if (!status) return true;

  const value = String(status).trim().toLowerCase();

  return value === 'da_kich_hoat' || value === 'active' || value === 'activated';
};

const handleLogin = async () => {
  try {
    loading.value = true;
    errorMessage.value = '';

    const res = await loginThuong({
      email: form.email,
      matKhau: form.password,
    });

    const token = extractToken(res);
    const role = extractRole(res);
    const status = extractStatus(res);
    const payload = extractLoginPayload(res);

    if (!token) {
      throw new Error('Backend chưa trả token đăng nhập');
    }

    if (!role) {
      throw new Error('Backend chưa trả quyền tài khoản');
    }

    if (!isActiveAccount(status)) {
      throw new Error('Tài khoản chưa được kích hoạt hoặc đã bị khóa');
    }

    localStorage.setItem('accessToken', token);
    localStorage.setItem('token', token);
    localStorage.setItem('role', role);
    localStorage.setItem('user', JSON.stringify(payload));

    router.replace(getHomeByRole(role));
  } catch (error) {
    errorMessage.value =
        error?.message ||
        error?.data?.message ||
        'Đăng nhập thất bại';
  } finally {
    loading.value = false;
  }
};

const handleGoogleLogin = () => {
  loginGoogle();
};
</script>

<style scoped>
.login-card {
  width: 100%;
  max-width: 380px;
  padding: 24px;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.96);
  box-shadow: 0 24px 70px rgba(15, 23, 42, 0.18);
  border: 1px solid rgba(226, 232, 240, 0.9);
  backdrop-filter: blur(14px);
}

.login-head {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-bottom: 22px;
}

.logo {
  width: 48px;
  height: 48px;
  border-radius: 16px;
  background: linear-gradient(135deg, #2563eb, #06b6d4);
  color: white;
  font-weight: 900;
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 auto;
}

.login-head h2 {
  margin: 0;
  color: #0f172a;
  font-size: 24px;
  font-weight: 900;
}

.login-head p {
  margin: 4px 0 0;
  color: #64748b;
  font-size: 13px;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 7px;
}

.form-group label {
  color: #334155;
  font-size: 13px;
  font-weight: 800;
}

.form-group input {
  width: 100%;
  height: 44px;
  border-radius: 13px;
  border: 1px solid #cbd5e1;
  padding: 0 13px;
  outline: none;
  font-size: 14px;
}

.form-group input:focus {
  border-color: #2563eb;
  box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.12);
}

.error-message {
  margin: 0;
  padding: 10px 12px;
  border-radius: 12px;
  background: #fee2e2;
  color: #b91c1c;
  font-size: 13px;
  font-weight: 700;
}

.login-button,
.google-button {
  width: 100%;
  height: 44px;
  border-radius: 13px;
  font-size: 14px;
  font-weight: 900;
  cursor: pointer;
}

.login-button {
  border: none;
  color: white;
  background: linear-gradient(135deg, #2563eb, #1d4ed8);
}

.login-button:disabled {
  opacity: 0.65;
  cursor: not-allowed;
}

.google-button {
  border: 1px solid #cbd5e1;
  background: white;
  color: #0f172a;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.google-button span {
  width: 22px;
  height: 22px;
  border-radius: 50%;
  background: #fee2e2;
  color: #dc2626;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
</style>