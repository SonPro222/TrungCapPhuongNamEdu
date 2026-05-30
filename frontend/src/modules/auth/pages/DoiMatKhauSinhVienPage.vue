<template>
  <section class="doi-mat-khau-page">
    <form class="doi-mat-khau-box" @submit.prevent="xacNhanDoiMatKhau">
      <h1>Đổi mật khẩu sinh viên</h1>
      <p class="mo-ta">Nhập Gmail tài khoản sinh viên để nhận OTP, sau đó đặt mật khẩu mới.</p>

      <label>
        <span>Gmail sinh viên</span>
        <input
            v-model.trim="form.email"
            type="email"
            autocomplete="username"
            placeholder="Nhập Gmail sinh viên"
            required
        />
      </label>

      <button type="button" class="btn secondary" :disabled="dangGuiOtp" @click="guiOtp">
        {{ dangGuiOtp ? 'Đang gửi OTP...' : 'Gửi OTP về Gmail' }}
      </button>

      <label>
        <span>Mã OTP</span>
        <input
            v-model.trim="form.otp"
            type="text"
            inputmode="numeric"
            maxlength="6"
            placeholder="Nhập 6 số OTP"
            required
        />
      </label>

      <label>
        <span>Mật khẩu mới</span>
        <input
            v-model="form.matKhauMoi"
            type="password"
            autocomplete="new-password"
            placeholder="Nhập mật khẩu mới"
            required
        />
      </label>

      <label>
        <span>Xác nhận mật khẩu</span>
        <input
            v-model="form.xacNhanMatKhau"
            type="password"
            autocomplete="new-password"
            placeholder="Nhập lại mật khẩu mới"
            required
        />
      </label>

      <p v-if="thongBao" class="success-text">{{ thongBao }}</p>
      <p v-if="loi" class="error-text">{{ loi }}</p>

      <button type="submit" class="btn primary" :disabled="dangDoiMatKhau">
        {{ dangDoiMatKhau ? 'Đang đổi mật khẩu...' : 'Xác nhận đổi mật khẩu' }}
      </button>

      <RouterLink class="back-link" to="/auth/login">
        Quay lại đăng nhập
      </RouterLink>
    </form>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { RouterLink } from 'vue-router'
import { heThongApi } from '@/modules/heThong/api/heThongApi'

const dangGuiOtp = ref(false)
const dangDoiMatKhau = ref(false)
const thongBao = ref('')
const loi = ref('')

const form = reactive({
  email: '',
  otp: '',
  matKhauMoi: '',
  xacNhanMatKhau: ''
})

async function guiOtp() {
  loi.value = ''
  thongBao.value = ''

  if (!form.email) {
    loi.value = 'Vui lòng nhập Gmail sinh viên'
    return
  }

  dangGuiOtp.value = true

  try {
    await heThongApi.taiKhoanSinhVien.guiOtpDoiMatKhau({
      email: form.email
    })

    thongBao.value = 'Đã gửi OTP về Gmail sinh viên. Vui lòng kiểm tra hộp thư.'
  } catch (error) {
    loi.value = error?.message || 'Không gửi được OTP'
  } finally {
    dangGuiOtp.value = false
  }
}

async function xacNhanDoiMatKhau() {
  loi.value = ''
  thongBao.value = ''

  if (form.matKhauMoi !== form.xacNhanMatKhau) {
    loi.value = 'Mật khẩu xác nhận không khớp'
    return
  }

  dangDoiMatKhau.value = true

  try {
    await heThongApi.taiKhoanSinhVien.xacNhanDoiMatKhau({
      email: form.email,
      otp: form.otp,
      matKhauMoi: form.matKhauMoi,
      xacNhanMatKhau: form.xacNhanMatKhau
    })

    thongBao.value = 'Đổi mật khẩu thành công. Sinh viên có thể đăng nhập bằng mật khẩu mới.'
    form.otp = ''
    form.matKhauMoi = ''
    form.xacNhanMatKhau = ''
  } catch (error) {
    loi.value = error?.message || 'Đổi mật khẩu thất bại'
  } finally {
    dangDoiMatKhau.value = false
  }
}
</script>

<style scoped>
.doi-mat-khau-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  background: #f8fafc;
}

.doi-mat-khau-box {
  width: min(100%, 430px);
  display: flex;
  flex-direction: column;
  gap: 14px;
  padding: 28px;
  border: 1px solid #e2e8f0;
  border-radius: 18px;
  background: #ffffff;
  box-shadow: 0 18px 45px rgba(15, 23, 42, 0.08);
}

.doi-mat-khau-box h1 {
  margin: 0;
  font-size: 24px;
  color: #0f172a;
}

.mo-ta {
  margin: 0;
  color: #64748b;
  line-height: 1.5;
}

label {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

label span {
  font-weight: 700;
  color: #334155;
}

input {
  border: 1px solid #cbd5e1;
  border-radius: 10px;
  padding: 10px 12px;
  font-size: 15px;
}

.btn {
  border: 1px solid #cbd5e1;
  border-radius: 10px;
  padding: 10px 12px;
  font-weight: 700;
  cursor: pointer;
}

.btn:disabled {
  cursor: not-allowed;
  opacity: 0.65;
}

.btn.primary {
  border-color: #0f172a;
  background: #0f172a;
  color: #fff;
}

.btn.secondary {
  background: #ffffff;
  color: #0f172a;
}

.success-text {
  margin: 0;
  color: #15803d;
  font-weight: 700;
}

.error-text {
  margin: 0;
  color: #b91c1c;
  font-weight: 700;
}

.back-link {
  text-align: center;
  color: #2563eb;
  text-decoration: none;
  font-weight: 700;
}
</style>
