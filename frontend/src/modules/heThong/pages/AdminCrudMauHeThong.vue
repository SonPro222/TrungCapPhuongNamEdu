<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Quản lý CRUD gốc</h1>
        <p>Quản lý cấu hình mã sinh viên, vai trò và quyền gốc.</p>
      </div>

      <button type="button" class="btn" @click="taiTatCa">
        Tải lại
      </button>
    </header>

    <nav class="tabs">
      <button type="button" class="tab" :class="{ active: tabDangChon === 'maSinhVien' }" @click="chonTab('maSinhVien')">
        Cấu hình mã sinh viên gốc
      </button>

      <button type="button" class="tab" :class="{ active: tabDangChon === 'vaiTro' }" @click="chonTab('vaiTro')">
        Vai trò gốc
      </button>

      <button type="button" class="tab" :class="{ active: tabDangChon === 'quyen' }" @click="chonTab('quyen')">
        Quyền gốc
      </button>
    </nav>

    <AdminCauHinhMaSinhVien v-if="tabDangChon === 'maSinhVien'" />

    <section v-else class="card">
      <section class="filter-card">
        <label>
          <span>Tìm kiếm</span>
          <input v-model="keyword" type="text" placeholder="Mã, tên, mô tả" />
        </label>

        <div class="filter-actions">
          <button type="button" class="btn primary" @click="taiDanhSachmau">
            Lọc
          </button>

          <button type="button" class="btn" @click="xoaLoc">
            Xóa lọc
          </button>
        </div>
      </section>

      <form class="form-card" @submit.prevent="luu">
        <label>
          <span>{{ tabDangChon === 'vaiTro' ? 'Mã vai trò' : 'Mã quyền' }}</span>
          <input v-model="form.ma" type="text" required />
        </label>

        <label>
          <span>{{ tabDangChon === 'vaiTro' ? 'Tên vai trò' : 'Tên quyền' }}</span>
          <input v-model="form.ten" type="text" required />
        </label>

        <label>
          <span>Mô tả</span>
          <input v-model="form.moTa" type="text" />
        </label>

        <div class="form-actions">
          <button type="submit" class="btn primary">
            {{ idDangSua ? 'Cập nhật' : 'Thêm mới' }}
          </button>

          <button type="button" class="btn" @click="resetForm">
            Làm mới
          </button>
        </div>
      </form>

      <div v-if="thongBao" class="alert success">{{ thongBao }}</div>
      <div v-if="loi" class="alert error">{{ loi }}</div>

      <div class="table-wrap">
        <table>
          <thead>
          <tr>
            <th>ID</th>
            <th>Mã</th>
            <th>Tên</th>
            <th>Mô tả</th>
            <th>Thao tác</th>
          </tr>
          </thead>

          <tbody>
          <tr v-if="dangTai">
            <td colspan="5">Đang tải dữ liệu...</td>
          </tr>

          <tr v-else-if="!danhSachHienThi.length">
            <td colspan="5">Chưa có dữ liệu</td>
          </tr>

          <tr v-for="item in danhSachHienThi" v-else :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ layMa(item) }}</td>
            <td>{{ layTen(item) }}</td>
            <td>{{ item.moTa || '-' }}</td>
            <td class="actions">
              <button type="button" class="btn small" @click="sua(item)">
                Sửa
              </button>

              <button type="button" class="btn small danger" @click="xoa(item)">
                Xóa
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </section>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { heThongApi } from '../api/heThongApi'
import AdminCauHinhMaSinhVien from './AdminCauHinhMaSinhVien.vue'

const tabDangChon = ref('maSinhVien')
const dangTai = ref(false)
const loi = ref('')
const thongBao = ref('')
const keyword = ref('')
const idDangSua = ref(null)

const danhSach = ref([])

const form = reactive({
  ma: '',
  ten: '',
  moTa: ''
})

const apiHienTai = computed(() => {
  return tabDangChon.value === 'vaiTro' ? heThongApi.vaiTro : heThongApi.quyen
})

const danhSachHienThi = computed(() => {
  const value = keyword.value.trim().toLowerCase()

  if (!value) {
    return danhSach.value
  }

  return danhSach.value.filter((item) => {
    return String(layMa(item) || '').toLowerCase().includes(value)
        || String(layTen(item) || '').toLowerCase().includes(value)
        || String(item.moTa || '').toLowerCase().includes(value)
  })
})

onMounted(() => {
  taiTatCa()
})

async function taiTatCa() {
  if (tabDangChon.value === 'maSinhVien') {
    return
  }

  await taiDanhSachmau()
}

async function taiDanhSachmau() {
  if (tabDangChon.value === 'maSinhVien') {
    return
  }

  dangTai.value = true
  loi.value = ''
  thongBao.value = ''

  try {
    const result = await apiHienTai.value.getAll({ page: 0, size: 1000 })
    danhSach.value = layDanhSach(result)
  } catch (error) {
    loi.value = error?.message || 'Không tải được dữ liệu'
  } finally {
    dangTai.value = false
  }
}

function chonTab(tab) {
  tabDangChon.value = tab
  keyword.value = ''
  resetForm()
  taiTatCa()
}

function layDanhSach(result) {
  if (Array.isArray(result)) return result
  if (Array.isArray(result?.content)) return result.content
  if (Array.isArray(result?.data?.content)) return result.data.content
  if (Array.isArray(result?.data)) return result.data
  return []
}

async function luu() {
  loi.value = ''
  thongBao.value = ''

  const payload = tabDangChon.value === 'vaiTro'
      ? {
        maVaiTro: form.ma,
        tenVaiTro: form.ten,
        moTa: form.moTa
      }
      : {
        maQuyen: form.ma,
        tenQuyen: form.ten,
        moTa: form.moTa
      }

  try {
    if (idDangSua.value) {
      await apiHienTai.value.update(idDangSua.value, payload)
      thongBao.value = 'Cập nhật thành công'
    } else {
      await apiHienTai.value.create(payload)
      thongBao.value = 'Thêm mới thành công'
    }

    resetForm()
    await taiDanhSachmau()
  } catch (error) {
    loi.value = error?.message || 'Lưu dữ liệu thất bại'
  }
}

function sua(item) {
  idDangSua.value = item.id
  form.ma = layMa(item)
  form.ten = layTen(item)
  form.moTa = item.moTa || ''
}

async function xoa(item) {
  if (!window.confirm(`Xóa ${layMa(item)}?`)) return

  try {
    await apiHienTai.value.delete(item.id)
    thongBao.value = 'Xóa thành công'
    await taiDanhSachmau()
  } catch (error) {
    loi.value = error?.message || 'Xóa thất bại'
  }
}

function xoaLoc() {
  keyword.value = ''
}

function resetForm() {
  idDangSua.value = null
  form.ma = ''
  form.ten = ''
  form.moTa = ''
}

function layMa(item) {
  return tabDangChon.value === 'vaiTro' ? item.maVaiTro : item.maQuyen
}

function layTen(item) {
  return tabDangChon.value === 'vaiTro' ? item.tenVaiTro : item.tenQuyen
}
</script>

<style scoped>
/* ================= BỐ CỤC CHUNG ================= */
.page,
.card {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.page-head h1 {
  margin: 0;
  font-size: 20px;
  color: #111827;
}

.page-head p {
  margin: 6px 0 0;
  color: #64748b;
  font-size: 14px;
}

/* ================= TABS & BUTTONS ================= */
.tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  border-bottom: 1px solid #e2e8f0;
  padding-bottom: 12px;
}

.tab,
.btn {
  border: 1px solid #cbd5e1;
  background: #fff;
  color: #475467;
  border-radius: 8px;
  padding: 8px 14px;
  cursor: pointer;
  font-weight: 600;
  font-size: 14px;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}

.tab:hover,
.btn:hover {
  background: #f8fafc;
  color: #0f172a;
  border-color: #94a3b8;
}

.tab.active {
  background: #eff6ff;
  color: #1d4ed8;
  border-color: #bfdbfe;
}

.btn.primary {
  background: #0f172a;
  color: #fff;
  border-color: #0f172a;
}

.btn.primary:hover {
  background: #1e293b;
  border-color: #1e293b;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

/* Nút đặc biệt khi ở trạng thái Cập nhật */
.btn.update-btn {
  background: #2563eb;
  color: #fff;
  border-color: #2563eb;
}

.btn.update-btn:hover {
  background: #1d4ed8;
  border-color: #1d4ed8;
  box-shadow: 0 4px 6px -1px rgba(37, 99, 235, 0.2);
}

.btn.small {
  padding: 6px 10px;
  font-size: 13px;
}

.btn.danger {
  color: #b91c1c;
  background: #fef2f2;
  border-color: #fecaca;
}

.btn.danger:hover {
  background: #fee2e2;
  border-color: #fca5a5;
}

/* ================= FORMS & BỘ LỌC ================= */
.filter-card,
.form-card {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 16px;
  padding: 20px;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

/* Hiệu ứng nổi bật Form khi đang ở chế độ Sửa */
.form-card.is-editing {
  border: 2px solid #60a5fa;
  background-color: #eff6ff;
  box-shadow: 0 0 0 4px rgba(59, 130, 246, 0.1);
  transform: translateY(-2px);
}

.filter-card label,
.form-card label {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.filter-card span,
.form-card span {
  font-size: 13px;
  font-weight: 600;
  color: #475467;
}

.form-card.is-editing span {
  color: #1e40af;
}

input,
select {
  border: 1px solid #d0d5dd;
  border-radius: 8px;
  padding: 10px 12px;
  font-size: 14px;
  color: #111827;
  background: #fff;
  outline: none;
  transition: all 0.2s ease;
  box-sizing: border-box;
  width: 100%;
}

input:hover,
select:hover {
  border-color: #94a3b8;
}

input:focus,
select:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15);
}

.filter-actions,
.form-actions {
  display: flex;
  align-items: flex-end;
  gap: 10px;
}

.form-actions {
  grid-column: 1 / -1;
  padding-top: 8px;
}

/* ================= BẢNG DỮ LIỆU (TABLE) ================= */
.table-wrap {
  overflow-x: auto;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
}

table {
  width: 100%;
  border-collapse: collapse;
  min-width: 900px;
}

th,
td {
  border-bottom: 1px solid #e2e8f0;
  padding: 14px 16px;
  text-align: left;
  vertical-align: middle;
  font-size: 14px;
  color: #334155;
}

th {
  background: #f8fafc;
  font-weight: 600;
  color: #475467;
  white-space: nowrap;
}

/* Hiệu ứng rê chuột và highlight dòng */
tbody tr {
  transition: background-color 0.15s ease;
}

tbody tr:hover {
  background-color: #f1f5f9;
}

tbody tr.is-active-row {
  background-color: #eff6ff;
}

.actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

/* ================= THÔNG BÁO (ALERTS) ================= */
.alert {
  padding: 12px 16px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  display: flex;
  align-items: center;
}

.alert.success {
  background: #ecfdf5;
  color: #065f46;
  border: 1px solid #a7f3d0;
}

.alert.error {
  background: #fef2f2;
  color: #991b1b;
  border: 1px solid #fecaca;
}

/* ================= RESPONSIVE ================= */
@media (max-width: 1024px) {
  /* Chuyển Form thành 2 cột trên Tablet/Laptop nhỏ */
  .filter-card,
  .form-card {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 640px) {
  /* Chuyển Form thành 1 cột trên Mobile */
  .filter-card,
  .form-card {
    grid-template-columns: 1fr;
  }
  .filter-actions {
    justify-content: flex-start;
  }
}
</style>