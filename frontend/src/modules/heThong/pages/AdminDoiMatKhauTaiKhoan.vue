<template>
  <section class="page doi-mat-khau-page">
    <header class="page-head">
      <div class="page-title-block">
        <p class="eyebrow">Quản trị tài khoản</p>
        <h1>Đổi mật khẩu tài khoản</h1>
        <p>
          Nhập Gmail để tìm tài khoản. Sau khi tìm thấy, hệ thống sẽ đổi mật khẩu theo đúng ID tài khoản đó.
        </p>
      </div>
    </header>

    <section class="panel search-panel">
      <div class="section-title">
        <div>
          <h2>Tìm tài khoản</h2>
          <p>Tra cứu nhanh bằng Gmail tài khoản cần đổi mật khẩu.</p>
        </div>
      </div>

      <div class="search-row">
        <label class="field">
          <span>Gmail tài khoản</span>
          <input
              v-model.trim="emailTimKiem"
              type="email"
              placeholder="Ví dụ: example@gmail.com"
              autocomplete="username"
              @keyup.enter="timTaiKhoan"
          />
        </label>

        <button type="button" class="btn primary" @click="timTaiKhoan" :disabled="dangTai">
          {{ dangTai ? 'Đang tìm...' : 'Tìm tài khoản' }}
        </button>
      </div>

      <div v-if="loi" class="alert error">
        {{ loi }}
      </div>

      <div v-if="thongBao" class="alert success">
        {{ thongBao }}
      </div>
    </section>

    <form v-if="taiKhoanDangChon" class="panel update-panel" @submit.prevent="doiMatKhau">
      <div class="section-title">
        <div>
          <h2>Tài khoản đang xử lý</h2>
          <p>Kiểm tra đúng thông tin trước khi đổi mật khẩu.</p>
        </div>

        <span class="status-pill">
          ID: {{ taiKhoanDangChon.id }}
        </span>
      </div>

      <div class="account-card">
        <div class="account-main">
          <div class="avatar">
            {{ taiKhoanDangChon.email?.charAt(0)?.toUpperCase() || 'T' }}
          </div>

          <div class="account-summary">
            <strong>{{ taiKhoanDangChon.email }}</strong>
            <span>{{ dinhDangLoaiTaiKhoan(taiKhoanDangChon.loaiTaiKhoan) }}</span>
          </div>
        </div>

        <div class="account-meta">
          <div class="meta-item">
            <span>ID</span>
            <strong>{{ taiKhoanDangChon.id }}</strong>
          </div>

          <div class="meta-item">
            <span>Gmail</span>
            <strong>{{ taiKhoanDangChon.email }}</strong>
          </div>

          <div class="meta-item">
            <span>Loại tài khoản</span>
            <strong>{{ dinhDangLoaiTaiKhoan(taiKhoanDangChon.loaiTaiKhoan) }}</strong>
          </div>

          <div class="meta-item">
            <span>Trạng thái</span>
            <strong>{{ dinhDangTrangThai(taiKhoanDangChon.trangThai) }}</strong>
          </div>
        </div>
      </div>

      <div class="form-grid">
        <label class="field">
          <span>Mật khẩu mới</span>
          <input
              v-model="form.matKhauMoi"
              type="password"
              placeholder="Nhập mật khẩu mới, tối thiểu 6 ký tự"
              autocomplete="new-password"
              minlength="6"
              required
          />
        </label>
      </div>

      <div class="form-actions">
        <button type="button" class="btn ghost" @click="lamMoi" :disabled="dangXuLy">
          Làm mới
        </button>

        <button type="submit" class="btn primary" :disabled="dangXuLy">
          {{ dangXuLy ? 'Đang đổi mật khẩu...' : 'Đổi mật khẩu' }}
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

const nhanLoaiTaiKhoan = {
  sinh_vien: 'Sinh viên',
  giao_vien: 'Giáo viên',
  nhan_vien: 'Nhân viên',
  dao_tao: 'Đào tạo',
  admin: 'Quản trị hệ thống'
}

const nhanTrangThai = {
  da_kich_hoat: 'Đã kích hoạt',
  chua_kich_hoat: 'Chưa kích hoạt',
  bi_khoa: 'Bị khóa',
  khoa: 'Đã khóa',
  dang_hoat_dong: 'Đang hoạt động',
  ngung_hoat_dong: 'Ngừng hoạt động'
}

function dinhDangTuKhoa(giaTri) {
  if (!giaTri) return '—'

  return String(giaTri)
      .replaceAll('_', ' ')
      .trim()
      .toLowerCase()
      .replace(/^\p{L}/u, kyTu => kyTu.toUpperCase())
}

function dinhDangLoaiTaiKhoan(loaiTaiKhoan) {
  if (!loaiTaiKhoan) return '—'

  const key = String(loaiTaiKhoan).trim().toLowerCase()
  return nhanLoaiTaiKhoan[key] || dinhDangTuKhoa(loaiTaiKhoan)
}

function dinhDangTrangThai(trangThai) {
  if (!trangThai) return '—'

  const key = String(trangThai).trim().toLowerCase()
  return nhanTrangThai[key] || dinhDangTuKhoa(trangThai)
}

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
.doi-mat-khau-page {
  --primary: #077149;
  --primary-dark: #055d3c;
  --primary-soft: #e8f6f0;
  --primary-border: #9fd8c2;
  --text-main: #172033;
  --text-muted: #64748b;
  --border: #dbe5ee;
  --bg-soft: #f7faf9;
  --danger: #b91c1c;
  --danger-bg: #fef2f2;
  --danger-border: #fecaca;

  display: grid;
  gap: 14px;
  width: 100%;
  max-width: 980px;
  color: var(--text-main);
}

.page-head {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 16px;
}

.page-title-block {
  display: grid;
  gap: 4px;
}

.eyebrow {
  margin: 0;
  color: var(--primary);
  font-size: 13px;
  font-weight: 700;
  letter-spacing: 0.02em;
  text-transform: uppercase;
}

.page-head h1 {
  margin: 0;
  color: #102033;
  font-size: 24px;
  line-height: 1.25;
  font-weight: 800;
}

.page-head p {
  margin: 0;
  color: var(--text-muted);
  font-size: 14px;
  line-height: 1.5;
}

.panel {
  width: 100%;
  background:
      linear-gradient(180deg, rgba(255, 255, 255, 0.98), rgba(255, 255, 255, 0.94)),
      var(--bg-soft);
  border: 1px solid var(--border);
  border-radius: 18px;
  padding: 18px 20px;
  box-shadow: 0 12px 28px rgba(15, 23, 42, 0.06);
  display: grid;
  gap: 14px;
}

.search-panel,
.update-panel {
  border-top: 4px solid var(--primary);
}

.section-title {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 14px;
}

.section-title h2 {
  margin: 0;
  color: #1f2937;
  font-size: 17px;
  line-height: 1.3;
  font-weight: 800;
}

.section-title p {
  margin: 3px 0 0;
  color: var(--text-muted);
  font-size: 13px;
  line-height: 1.45;
}

.search-row {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  gap: 12px;
  align-items: end;
}

.field {
  display: grid;
  gap: 7px;
  min-width: 0;
}

.field > span {
  color: #334155;
  font-size: 14px;
  font-weight: 700;
}

input {
  width: 100%;
  min-height: 42px;
  border: 1px solid #cbd5e1;
  border-radius: 12px;
  background: #ffffff;
  padding: 9px 12px;
  color: #111827;
  outline: none;
  font-size: 14px;
  font-weight: 600;
  transition: border-color 0.18s ease, box-shadow 0.18s ease, background 0.18s ease;
}

input::placeholder {
  color: #94a3b8;
  font-weight: 500;
}

input:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(7, 113, 73, 0.14);
}

.btn {
  min-height: 42px;
  border: 1px solid #cbd5e1;
  border-radius: 12px;
  background: #ffffff;
  padding: 0 16px;
  color: #334155;
  font-size: 14px;
  font-weight: 800;
  cursor: pointer;
  white-space: nowrap;
  transition: transform 0.15s ease, box-shadow 0.15s ease, border-color 0.15s ease, background 0.15s ease;
}

.btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 8px 18px rgba(15, 23, 42, 0.08);
}

.btn:disabled {
  cursor: not-allowed;
  opacity: 0.65;
}

.btn.primary {
  border-color: var(--primary);
  background: var(--primary);
  color: #ffffff;
}

.btn.primary:hover:not(:disabled) {
  background: var(--primary-dark);
  border-color: var(--primary-dark);
}

.btn.ghost {
  border-color: var(--primary-border);
  background: var(--primary-soft);
  color: var(--primary-dark);
}

.alert {
  border-radius: 12px;
  padding: 11px 13px;
  font-size: 14px;
  line-height: 1.45;
  font-weight: 700;
}

.alert.success {
  background: var(--primary-soft);
  color: var(--primary-dark);
  border: 1px solid var(--primary-border);
}

.alert.error {
  background: var(--danger-bg);
  color: var(--danger);
  border: 1px solid var(--danger-border);
}

.status-pill {
  display: inline-flex;
  align-items: center;
  min-height: 30px;
  border-radius: 999px;
  background: var(--primary-soft);
  border: 1px solid var(--primary-border);
  padding: 0 12px;
  color: var(--primary-dark);
  font-size: 13px;
  font-weight: 800;
  white-space: nowrap;
}

.account-card {
  display: grid;
  gap: 14px;
  border: 1px solid #dbe9e3;
  border-radius: 16px;
  background: linear-gradient(135deg, #f8fffc 0%, #ffffff 60%);
  padding: 14px;
}

.account-main {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 0;
}

.avatar {
  width: 42px;
  height: 42px;
  border-radius: 14px;
  display: grid;
  place-items: center;
  flex: 0 0 auto;
  background: var(--primary);
  color: #ffffff;
  font-size: 18px;
  font-weight: 900;
  box-shadow: 0 8px 18px rgba(7, 113, 73, 0.2);
}

.account-summary {
  display: grid;
  gap: 2px;
  min-width: 0;
}

.account-summary strong {
  overflow: hidden;
  color: #102033;
  font-size: 15px;
  font-weight: 900;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.account-summary span {
  color: var(--text-muted);
  font-size: 13px;
  font-weight: 600;
}

.account-meta {
  display: grid;
  grid-template-columns: 80px minmax(220px, 1.4fr) minmax(140px, 0.8fr) minmax(130px, 0.8fr);
  gap: 8px;
}

.meta-item {
  min-width: 0;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  background: #ffffff;
  padding: 9px 11px;
  display: grid;
  gap: 3px;
}

.meta-item span {
  color: var(--text-muted);
  font-size: 12px;
  font-weight: 700;
}

.meta-item strong {
  overflow: hidden;
  color: #1e293b;
  font-size: 13px;
  line-height: 1.35;
  font-weight: 800;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.form-grid {
  display: grid;
  gap: 12px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  flex-wrap: wrap;
}

@media (max-width: 860px) {
  .doi-mat-khau-page {
    max-width: 100%;
  }

  .account-meta {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 720px) {
  .panel {
    padding: 16px;
    border-radius: 16px;
  }

  .search-row {
    grid-template-columns: 1fr;
  }

  .btn {
    width: 100%;
  }

  .form-actions {
    flex-direction: column-reverse;
  }
}

@media (max-width: 520px) {
  .page-head h1 {
    font-size: 21px;
  }

  .section-title {
    display: grid;
  }

  .account-main {
    align-items: flex-start;
  }

  .account-meta {
    grid-template-columns: 1fr;
  }

  .status-pill {
    width: fit-content;
  }
}
</style>