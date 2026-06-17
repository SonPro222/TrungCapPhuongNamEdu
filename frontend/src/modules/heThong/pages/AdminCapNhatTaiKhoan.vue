<template>
  <section class="page cap-nhat-tai-khoan-page">
    <header class="page-head">
      <div>
        <p class="eyebrow">Quản trị tài khoản</p>
        <h1>Cập nhật tài khoản</h1>
        <p>Nhập Gmail tài khoản cần cập nhật. Sau khi tìm thấy mới hiển thị thông tin chỉnh sửa.</p>
      </div>
    </header>

    <section class="panel search-panel">
      <div class="panel-title">
        <div>
          <h2>Tìm tài khoản</h2>
          <p>Tra cứu theo Gmail tài khoản cần chỉnh sửa.</p>
        </div>
      </div>

      <div class="search-row">
        <label class="field">
          <span>Gmail tài khoản</span>
          <input
              v-model.trim="emailTimKiem"
              type="email"
              placeholder="Nhập Gmail tài khoản cần cập nhật"
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

    <form v-if="taiKhoanDangChon" class="panel form-panel" @submit.prevent="capNhatTaiKhoan">
      <div class="account-strip">
        <div class="account-left">
          <div class="avatar">
            {{ taiKhoanDangChon.email?.charAt(0)?.toUpperCase() || 'T' }}
          </div>

          <div class="account-text">
            <strong>{{ taiKhoanDangChon.email }}</strong>
            <span>Tài khoản đang cập nhật</span>
          </div>
        </div>

        <div class="account-id">
          ID: {{ taiKhoanDangChon.id }}
        </div>
      </div>

      <div class="form-grid">
        <label class="field email-field">
          <span>Gmail</span>
          <input v-model.trim="form.email" type="email" required />
        </label>

        <label class="field">
          <span>Loại tài khoản</span>
          <select v-model="form.loaiTaiKhoan" required>
            <option value="">-- Chọn loại tài khoản --</option>
            <option value="admin">Admin</option>
            <option value="dao_tao">Phòng đào tạo</option>
            <option value="nhan_vien">Nhân viên</option>
            <option value="giao_vien">Giáo viên</option>
            <option value="sinh_vien">Sinh viên</option>
          </select>
        </label>

        <label class="field">
          <span>Trạng thái</span>
          <select v-model="form.trangThai" required>
            <option value="">-- Chọn trạng thái --</option>
            <option value="cho_kich_hoat">Chờ kích hoạt</option>
            <option value="da_kich_hoat">Đã kích hoạt</option>
            <option value="bi_khoa">Bị khóa</option>
          </select>
        </label>

        <label class="field roles-field">
          <span>Vai trò</span>
          <select v-model="form.roles" multiple required>
            <option v-for="role in vaiTroList" :key="role.id" :value="role.maVaiTro">
              {{ role.maVaiTro }} - {{ role.tenVaiTro }}
            </option>
          </select>
          <small>Giữ Ctrl để chọn nhiều vai trò.</small>
        </label>
      </div>

      <div class="form-actions">
        <button type="button" class="btn ghost" @click="boChon" :disabled="dangLuu">
          Hủy
        </button>

        <button type="submit" class="btn primary" :disabled="dangLuu">
          {{ dangLuu ? 'Đang lưu...' : 'Lưu cập nhật' }}
        </button>
      </div>
    </form>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { heThongApi } from '../api/heThongApi'

const emailTimKiem = ref('')
const taiKhoanDangChon = ref(null)
const vaiTroList = ref([])
const dangTai = ref(false)
const dangLuu = ref(false)
const loi = ref('')
const thongBao = ref('')

const form = reactive({
  email: '',
  loaiTaiKhoan: '',
  trangThai: '',
  roles: []
})

onMounted(taiVaiTro)

async function taiVaiTro() {
  try {
    const result = await heThongApi.vaiTro.getAll({ page: 0, size: 1000 })
    vaiTroList.value = layDanhSach(result)
  } catch (error) {
    loi.value = error?.message || 'Không tải được danh sách vai trò'
  }
}

async function timTaiKhoan() {
  loi.value = ''
  thongBao.value = ''
  taiKhoanDangChon.value = null

  if (!emailTimKiem.value) {
    loi.value = 'Gmail tài khoản không được để trống'
    return
  }

  dangTai.value = true

  try {
    const result = await heThongApi.taiKhoan.getByEmail(emailTimKiem.value)
    const taiKhoan = result?.data || result

    taiKhoanDangChon.value = taiKhoan
    ganDuLieuForm(taiKhoan)
    thongBao.value = 'Đã tìm thấy tài khoản'
  } catch (error) {
    loi.value = error?.message || 'Không tìm thấy tài khoản'
  } finally {
    dangTai.value = false
  }
}

function ganDuLieuForm(taiKhoan) {
  form.email = taiKhoan?.email || ''
  form.loaiTaiKhoan = taiKhoan?.loaiTaiKhoan || ''
  form.trangThai = taiKhoan?.trangThai || 'da_kich_hoat'
  form.roles = Array.isArray(taiKhoan?.roles) ? [...taiKhoan.roles] : []
}

async function capNhatTaiKhoan() {
  loi.value = ''
  thongBao.value = ''

  if (!taiKhoanDangChon.value?.id) {
    loi.value = 'Chưa chọn tài khoản cần cập nhật'
    return
  }

  if (!form.roles.length) {
    loi.value = 'Tài khoản phải có ít nhất một vai trò'
    return
  }

  dangLuu.value = true

  try {
    await heThongApi.taiKhoan.update(taiKhoanDangChon.value.id, {
      email: form.email,
      loaiTaiKhoan: form.loaiTaiKhoan,
      trangThai: form.trangThai,
      roles: form.roles
    })

    thongBao.value = 'Cập nhật tài khoản thành công'
    emailTimKiem.value = form.email
    await timTaiKhoan()
  } catch (error) {
    loi.value = error?.message || 'Cập nhật tài khoản thất bại'
  } finally {
    dangLuu.value = false
  }
}

function boChon() {
  taiKhoanDangChon.value = null
  form.email = ''
  form.loaiTaiKhoan = ''
  form.trangThai = ''
  form.roles = []
  thongBao.value = ''
}

function layDanhSach(result) {
  if (Array.isArray(result)) return result
  if (Array.isArray(result?.content)) return result.content
  if (Array.isArray(result?.data?.content)) return result.data.content
  if (Array.isArray(result?.data)) return result.data
  return []
}
</script>

<style scoped>
.cap-nhat-tai-khoan-page {
  --primary: #077149;
  --primary-dark: #055d3c;
  --primary-soft: #e8f6f0;
  --primary-soft-2: #f6fbf8;
  --primary-border: #9bd8c1;
  --text-main: #0f172a;
  --text-muted: #64748b;
  --border: #d7e2ec;
  --surface: #ffffff;
  --danger: #b91c1c;
  --danger-bg: #fef2f2;
  --danger-border: #fecaca;

  width: 100%;
  max-width: 1040px;
  display: grid;
  gap: 14px;
  color: var(--text-main);
}

.page-head {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
}

.page-head h1 {
  margin: 2px 0 0;
  font-size: 24px;
  line-height: 1.2;
  font-weight: 800;
  color: #102033;
}

.page-head p {
  margin: 4px 0 0;
  color: var(--text-muted);
  font-size: 14px;
  line-height: 1.45;
}

.eyebrow {
  margin: 0;
  color: var(--primary);
  font-size: 12px;
  font-weight: 900;
  letter-spacing: 0.05em;
  text-transform: uppercase;
}

.panel {
  width: 100%;
  display: grid;
  gap: 12px;
  background: var(--surface);
  border: 1px solid var(--border);
  border-top: 4px solid var(--primary);
  border-radius: 16px;
  padding: 14px 18px;
  box-shadow: 0 10px 24px rgba(15, 23, 42, 0.055);
}

.panel-title {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
}

.panel-title h2 {
  margin: 0;
  font-size: 16px;
  line-height: 1.25;
  font-weight: 800;
  color: #1f2937;
}

.panel-title p {
  margin: 2px 0 0;
  color: var(--text-muted);
  font-size: 13px;
}

.search-row {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 120px;
  gap: 12px;
  align-items: end;
}

.field {
  min-width: 0;
  display: grid;
  gap: 6px;
}

.field > span {
  color: #334155;
  font-size: 13px;
  font-weight: 800;
}

input,
select {
  width: 100%;
  min-height: 40px;
  border: 1px solid #cbd5e1;
  border-radius: 11px;
  background: #ffffff;
  padding: 8px 11px;
  color: #111827;
  outline: none;
  font-size: 14px;
  font-weight: 600;
  transition: border-color 0.16s ease, box-shadow 0.16s ease, background 0.16s ease;
}

input::placeholder {
  color: #94a3b8;
  font-weight: 500;
}

select {
  cursor: pointer;
}

select[multiple] {
  min-height: 116px;
  padding: 7px;
  overflow: auto;
}

select[multiple] option {
  padding: 6px 8px;
  border-radius: 7px;
  margin-bottom: 2px;
}

input:focus,
select:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(7, 113, 73, 0.13);
}

small {
  margin-top: -1px;
  color: var(--text-muted);
  font-size: 12px;
  line-height: 1.35;
  font-weight: 600;
}

.btn {
  min-height: 40px;
  border: 1px solid #cbd5e1;
  border-radius: 11px;
  background: #ffffff;
  padding: 0 15px;
  color: #334155;
  font-size: 14px;
  font-weight: 800;
  cursor: pointer;
  white-space: nowrap;
  transition: background 0.16s ease, border-color 0.16s ease, box-shadow 0.16s ease, transform 0.16s ease;
}

.btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 7px 16px rgba(15, 23, 42, 0.08);
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
  border-radius: 11px;
  padding: 10px 12px;
  font-size: 13px;
  line-height: 1.4;
  font-weight: 800;
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

.account-strip {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  border: 1px solid #dbe9e3;
  border-radius: 14px;
  background: linear-gradient(135deg, var(--primary-soft-2), #ffffff);
  padding: 10px 12px;
}

.account-left {
  min-width: 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.avatar {
  width: 38px;
  height: 38px;
  display: grid;
  place-items: center;
  flex: 0 0 auto;
  border-radius: 12px;
  background: var(--primary);
  color: #ffffff;
  font-size: 17px;
  font-weight: 900;
  box-shadow: 0 7px 16px rgba(7, 113, 73, 0.2);
}

.account-text {
  min-width: 0;
  display: grid;
  gap: 2px;
}

.account-text strong {
  overflow: hidden;
  color: #102033;
  font-size: 15px;
  line-height: 1.3;
  font-weight: 900;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.account-text span {
  color: var(--text-muted);
  font-size: 12px;
  font-weight: 700;
}

.account-id {
  flex: 0 0 auto;
  min-height: 28px;
  display: inline-flex;
  align-items: center;
  border-radius: 999px;
  border: 1px solid var(--primary-border);
  background: var(--primary-soft);
  padding: 0 11px;
  color: var(--primary-dark);
  font-size: 13px;
  font-weight: 900;
}

.form-grid {
  display: grid;
  grid-template-columns: minmax(260px, 1.25fr) minmax(180px, 0.85fr) minmax(180px, 0.85fr);
  gap: 12px;
  align-items: start;
}

.email-field {
  grid-column: 1 / 2;
}

.roles-field {
  grid-column: 1 / -1;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding-top: 2px;
}

@media (max-width: 900px) {
  .cap-nhat-tai-khoan-page {
    max-width: 100%;
  }

  .form-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .email-field,
  .roles-field {
    grid-column: 1 / -1;
  }
}

@media (max-width: 720px) {
  .panel {
    padding: 14px;
    border-radius: 15px;
  }

  .search-row,
  .form-grid {
    grid-template-columns: 1fr;
  }

  .email-field,
  .roles-field {
    grid-column: auto;
  }

  .account-strip {
    align-items: flex-start;
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

  .account-strip {
    display: grid;
  }

  .account-id {
    width: fit-content;
  }
}
</style>