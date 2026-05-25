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
          <button type="button" class="btn primary" @click="taiDanhSachGoc">
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

  await taiDanhSachGoc()
}

async function taiDanhSachGoc() {
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
    await taiDanhSachGoc()
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
    await taiDanhSachGoc()
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
.page,
.card {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-head {
  display: flex;
  justify-content: space-between;
  gap: 16px;
}

.page-head h1 {
  margin: 0;
}

.page-head p {
  margin: 6px 0 0;
  color: #64748b;
}

.tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tab,
.btn {
  border: 1px solid #cbd5e1;
  background: #fff;
  color: #0f172a;
  border-radius: 8px;
  padding: 8px 12px;
  cursor: pointer;
}

.tab.active,
.btn.primary {
  background: #0f172a;
  color: #fff;
  border-color: #0f172a;
}

.btn.small {
  padding: 5px 8px;
  font-size: 13px;
}

.btn.danger {
  color: #b91c1c;
  border-color: #fecaca;
}

.filter-card,
.form-card {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
  padding: 16px;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  background: #fff;
}

.filter-card label,
.form-card label {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.filter-card span,
.form-card span {
  font-size: 13px;
  font-weight: 700;
  color: #334155;
}

input {
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  padding: 8px 10px;
}

.filter-actions,
.form-actions {
  display: flex;
  align-items: flex-end;
  gap: 8px;
}

.form-actions {
  grid-column: 1 / -1;
}

.table-wrap {
  overflow: auto;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
}

table {
  width: 100%;
  border-collapse: collapse;
  min-width: 900px;
}

th,
td {
  border-bottom: 1px solid #e2e8f0;
  padding: 10px;
  text-align: left;
  vertical-align: top;
}

th {
  background: #f8fafc;
}

.actions {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.alert {
  padding: 10px 12px;
  border-radius: 8px;
}

.alert.success {
  background: #dcfce7;
  color: #166534;
}

.alert.error {
  background: #fee2e2;
  color: #991b1b;
}
</style>