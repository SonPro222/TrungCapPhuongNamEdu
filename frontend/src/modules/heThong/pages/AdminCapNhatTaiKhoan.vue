<template>
  <section class="page cap-nhat-tai-khoan-page">
    <header class="page-head">
      <div>
        <h1>Cập nhật tài khoản</h1>
        <p>Nhập Gmail tài khoản cần cập nhật. Sau khi tìm thấy mới hiển thị thông tin chỉnh sửa.</p>
      </div>
    </header>

    <section class="panel tim-kiem-panel">
      <div class="search-row">
        <label>
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
      <div class="info-box">
        <strong>Tài khoản đang cập nhật</strong>
        <span>ID: {{ taiKhoanDangChon.id }}</span>
        <span>Gmail hiện tại: {{ taiKhoanDangChon.email }}</span>
      </div>

      <div class="form-grid">
        <label>
          <span>Gmail</span>
          <input v-model.trim="form.email" type="email" required />
        </label>

        <label>
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

        <label>
          <span>Trạng thái</span>
          <select v-model="form.trangThai" required>
            <option value="">-- Chọn trạng thái --</option>
            <option value="cho_kich_hoat">Chờ kích hoạt</option>
            <option value="da_kich_hoat">Đã kích hoạt</option>
            <option value="bi_khoa">Bị khóa</option>
          </select>
        </label>

        <label>
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
        <button type="submit" class="btn primary" :disabled="dangLuu">
          {{ dangLuu ? 'Đang lưu...' : 'Lưu cập nhật' }}
        </button>

        <button type="button" class="btn" @click="boChon" :disabled="dangLuu">
          Hủy
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
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 10px 30px rgba(15, 23, 42, 0.06);
}

.tim-kiem-panel,
.form-panel {
  max-width: 900px;
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

input,
select {
  min-height: 42px;
  border: 1px solid #cbd5e1;
  border-radius: 12px;
  padding: 10px 12px;
  outline: none;
  font-size: 14px;
}

select[multiple] {
  min-height: 140px;
}

input:focus,
select:focus {
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.12);
}

small {
  color: #64748b;
  font-weight: 500;
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