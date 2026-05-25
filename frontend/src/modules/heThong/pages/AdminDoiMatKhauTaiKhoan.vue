<template>
  <section class="page doi-mat-khau-page">
    <header class="page-head">
      <div>
        <h1>Đổi mật khẩu tài khoản</h1>
        <p>Admin nhập Gmail để tìm tài khoản. Sau khi tìm thấy tài khoản, hệ thống đổi mật khẩu theo ID tài khoản đó.</p>
      </div>
    </header>

    <section class="panel">
      <div class="search-row">
        <label>
          <span>Gmail tài khoản</span>
          <input
              v-model.trim="emailTimKiem"
              type="email"
              placeholder="Nhập Gmail tài khoản cần đổi mật khẩu"
              autocomplete="username"
              @keyup.enter="timTaiKhoan"
          />
        </label>

        <button type="button" class="btn primary" @click="timTaiKhoan" :disabled="dangTai">
          {{ dangTai ? 'Đang tìm...' : 'Tìm tài khoản' }}
        </button>
      </div>

      <div v-if="loi" class="alert error">{{ loi }}</div>
      <div v-if="thongBao" class="alert success">{{ thongBao }}</div>
    </section>

    <form v-if="taiKhoanDangChon" class="panel" @submit.prevent="doiMatKhau">
      <div class="info-box">
        <strong>Tài khoản đang đổi mật khẩu</strong>
        <span>ID: {{ taiKhoanDangChon.id }}</span>
        <span>Gmail: {{ taiKhoanDangChon.email }}</span>
        <span>Loại tài khoản: {{ taiKhoanDangChon.loaiTaiKhoan }}</span>
        <span>Trạng thái: {{ taiKhoanDangChon.trangThai }}</span>
      </div>

      <div class="form-grid">
        <label>
          <span>Mật khẩu mới</span>
          <input
              v-model="form.matKhauMoi"
              type="password"
              placeholder="Nhập mật khẩu mới"
              autocomplete="new-password"
              minlength="6"
              required
          />
        </label>
      </div>

      <div class="form-actions">
        <button type="submit" class="btn primary" :disabled="dangXuLy">
          {{ dangXuLy ? 'Đang đổi mật khẩu...' : 'Đổi mật khẩu' }}
        </button>

        <button type="button" class="btn" @click="lamMoi" :disabled="dangXuLy">
          Làm mới
        </button>
      </div>
    </form>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { heThongApi } from '../api/heThongApi'

const emailTimKiem = ref('')
const taiKhoanDangChon = ref(null)
const dangTai = ref(false)
const dangXuLy = ref(false)
const loi = ref('')
const thongBao = ref('')

const form = reactive({
  matKhauMoi: ''
})

async function timTaiKhoan() {
  loi.value = ''
  thongBao.value = ''
  taiKhoanDangChon.value = null
  form.matKhauMoi = ''

  if (!emailTimKiem.value) {
    loi.value = 'Gmail tài khoản không được để trống'
    return
  }

  dangTai.value = true

  try {
    const result = await heThongApi.taiKhoan.getByEmail(emailTimKiem.value)
    const taiKhoan = result?.data || result

    if (!taiKhoan?.id) {
      loi.value = 'Không tìm thấy ID tài khoản'
      return
    }

    taiKhoanDangChon.value = taiKhoan
    thongBao.value = 'Đã tìm thấy tài khoản'
  } catch (error) {
    loi.value = error?.message || 'Không tìm thấy tài khoản'
  } finally {
    dangTai.value = false
  }
}

async function doiMatKhau() {
  loi.value = ''
  thongBao.value = ''

  if (!taiKhoanDangChon.value?.id) {
    loi.value = 'Chưa chọn tài khoản cần đổi mật khẩu'
    return
  }

  if (!form.matKhauMoi) {
    loi.value = 'Mật khẩu mới không được để trống'
    return
  }

  if (form.matKhauMoi.length < 6) {
    loi.value = 'Mật khẩu mới phải có ít nhất 6 ký tự'
    return
  }

  dangXuLy.value = true

  try {
    await heThongApi.taiKhoan.doiMatKhauQuanTri(taiKhoanDangChon.value.id, {
      matKhauMoi: form.matKhauMoi
    })

    thongBao.value = 'Đổi mật khẩu thành công'
    form.matKhauMoi = ''
  } catch (error) {
    loi.value = error?.message || 'Đổi mật khẩu thất bại'
  } finally {
    dangXuLy.value = false
  }
}

function lamMoi() {
  emailTimKiem.value = ''
  taiKhoanDangChon.value = null
  form.matKhauMoi = ''
  loi.value = ''
  thongBao.value = ''
}
</script>

<style scoped>
.page {
  display: grid;
  gap: 18px;
}

.page-head h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
}

.page-head p {
  margin: 6px 0 0;
  color: #64748b;
}

.panel {
  max-width: 820px;
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 10px 30px rgba(15, 23, 42, 0.06);
  display: grid;
  gap: 16px;
}

.search-row {
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 12px;
  align-items: end;
}

.form-grid {
  display: grid;
  gap: 14px;
}

label {
  display: grid;
  gap: 8px;
  font-weight: 600;
  color: #334155;
}

input {
  min-height: 42px;
  border: 1px solid #cbd5e1;
  border-radius: 12px;
  padding: 10px 12px;
  outline: none;
  font-size: 14px;
}

input:focus {
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.12);
}

.info-box {
  display: grid;
  gap: 4px;
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 12px 14px;
  color: #334155;
}

.form-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  flex-wrap: wrap;
}

.alert {
  border-radius: 12px;
  padding: 12px 14px;
  font-weight: 600;
}

.alert.success {
  background: #ecfdf5;
  color: #047857;
  border: 1px solid #a7f3d0;
}

.alert.error {
  background: #fef2f2;
  color: #b91c1c;
  border: 1px solid #fecaca;
}

@media (max-width: 720px) {
  .search-row {
    grid-template-columns: 1fr;
  }
}
</style>