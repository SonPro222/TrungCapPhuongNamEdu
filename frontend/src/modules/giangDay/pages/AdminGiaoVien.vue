<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Quản lý giáo viên</h1>
        <p>Quản lý hồ sơ giáo viên, tài khoản liên kết và trạng thái giảng dạy.</p>
      </div>

      <button type="button" class="btn" @click="taiDuLieu">
        Tải lại
      </button>
    </header>

    <section class="filter-card">
      <label>
        <span>Từ khóa</span>
        <input
            v-model.trim="boLoc.keyword"
            type="text"
            placeholder="Mã, họ tên, email"
            @keyup.enter="locDuLieu"
        />
      </label>

      <label>
        <span>Trạng thái</span>
        <select v-model="boLoc.trangThai" @change="locDuLieu">
          <option value="">Tất cả</option>
          <option value="dang_day">Đang dạy</option>
          <option value="tam_nghi">Tạm nghỉ</option>
          <option value="nghi_viec">Nghỉ việc</option>
        </select>
      </label>

      <label>
        <span>Sắp xếp</span>
        <select v-model="boLoc.sortBy" @change="locDuLieu">
          <option value="id">ID</option>
          <option value="maGiaoVien">Mã giáo viên</option>
          <option value="hoTen">Họ tên</option>
          <option value="email">Email</option>
          <option value="trangThai">Trạng thái</option>
          <option value="createdAt">Ngày tạo</option>
        </select>
      </label>

      <label>
        <span>Chiều sắp xếp</span>
        <select v-model="boLoc.sortDir" @change="locDuLieu">
          <option value="desc">Mới nhất</option>
          <option value="asc">Cũ nhất</option>
        </select>
      </label>

      <div class="filter-actions">
        <button type="button" class="btn primary" @click="locDuLieu">
          Lọc
        </button>

        <button type="button" class="btn" @click="xoaLoc">
          Xóa lọc
        </button>
      </div>
    </section>

    <section class="form-card">
      <h2>{{ form.id ? 'Cập nhật giáo viên' : 'Thêm giáo viên' }}</h2>

      <form class="form-grid" @submit.prevent="luuGiaoVien">
        <label>
          <span>Mã giáo viên</span>
          <input v-model.trim="form.maGiaoVien" type="text" required />
        </label>

        <label>
          <span>Họ tên</span>
          <input v-model.trim="form.hoTen" type="text" required />
        </label>

        <label>
          <span>Email</span>
          <input v-model.trim="form.email" type="email" required />
        </label>

        <label>
          <span>Số điện thoại</span>
          <input v-model.trim="form.soDienThoai" type="text" />
        </label>

        <label>
          <span>Trạng thái</span>
          <select v-model="form.trangThai" required>
            <option value="dang_day">Đang dạy</option>
            <option value="tam_nghi">Tạm nghỉ</option>
            <option value="nghi_viec">Nghỉ việc</option>
          </select>
        </label>

        <label>
          <span>Tài khoản ID</span>
          <input v-model.trim="form.taiKhoanId" type="number" />
        </label>

        <label class="field-full">
          <span>Chuyên môn</span>
          <textarea v-model.trim="form.chuyenMon" rows="2" />
        </label>

        <div class="form-actions">
          <button type="submit" class="btn primary">
            {{ form.id ? 'Cập nhật' : 'Thêm mới' }}
          </button>

          <button type="button" class="btn" @click="resetForm">
            Làm mới
          </button>
        </div>
      </form>
    </section>

    <div v-if="thongBao" class="alert success">
      {{ thongBao }}
    </div>

    <div v-if="loi" class="alert error">
      {{ loi }}
    </div>

    <section class="summary-grid">
      <div>
        <span>Đang hiển thị</span>
        <strong>{{ danhSach.length }}</strong>
      </div>

      <div>
        <span>Tổng bản ghi</span>
        <strong>{{ tongBanGhi }}</strong>
      </div>

      <div>
        <span>Trang</span>
        <strong>{{ trangHienTai + 1 }} / {{ tongTrang || 1 }}</strong>
      </div>
    </section>

    <div class="pagination-bar">
      <button
          type="button"
          class="btn"
          :disabled="trangHienTai <= 0 || dangTai"
          @click="doiTrang(trangHienTai - 1)"
      >
        Trang trước
      </button>

      <button
          type="button"
          class="btn"
          :disabled="tongTrang === 0 || trangHienTai + 1 >= tongTrang || dangTai"
          @click="doiTrang(trangHienTai + 1)"
      >
        Trang sau
      </button>
    </div>

    <div class="table-wrap">
      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>Mã giáo viên</th>
          <th>Họ tên</th>
          <th>Email</th>
          <th>SĐT</th>
          <th>Chuyên môn</th>
          <th>Trạng thái</th>
          <th>Tài khoản ID</th>
          <th>Thao tác</th>
        </tr>
        </thead>

        <tbody>
        <tr v-if="dangTai">
          <td colspan="9">Đang tải dữ liệu...</td>
        </tr>

        <tr v-else-if="!danhSach.length">
          <td colspan="9">Không có dữ liệu</td>
        </tr>

        <tr v-for="item in danhSach" v-else :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.maGiaoVien }}</td>
          <td>{{ item.hoTen }}</td>
          <td>{{ item.email }}</td>
          <td>{{ item.soDienThoai || '-' }}</td>
          <td>{{ item.chuyenMon || '-' }}</td>
          <td>{{ hienThiTrangThai(item.trangThai) }}</td>
          <td>{{ item.taiKhoanId || '-' }}</td>
          <td>
            <div class="row-actions">
              <button type="button" class="btn small" @click="chonSua(item)">
                Sửa
              </button>

              <button type="button" class="btn small danger" @click="xoaGiaoVien(item)">
                Xóa
              </button>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { giangDayService } from '../services/giangDayService'

const danhSach = ref([])
const dangTai = ref(false)
const loi = ref('')
const thongBao = ref('')
const trangHienTai = ref(0)
const tongTrang = ref(0)
const tongBanGhi = ref(0)
const kichThuocTrang = 100

const boLoc = reactive({
  keyword: '',
  trangThai: '',
  sortBy: 'id',
  sortDir: 'desc'
})

const form = reactive({
  id: null,
  maGiaoVien: '',
  hoTen: '',
  email: '',
  soDienThoai: '',
  chuyenMon: '',
  trangThai: 'dang_day',
  taiKhoanId: ''
})

onMounted(() => {
  taiDuLieu()
})

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''
  thongBao.value = ''

  try {
    const page = await giangDayService.layTrangGiaoVien({
      page: trangHienTai.value,
      size: kichThuocTrang,
      keyword: boLoc.keyword || undefined,
      trangThai: boLoc.trangThai || undefined,
      sortBy: boLoc.sortBy,
      sortDir: boLoc.sortDir
    })

    danhSach.value = page.content
    trangHienTai.value = page.number
    tongTrang.value = page.totalPages
    tongBanGhi.value = page.totalElements
  } catch (error) {
    loi.value = error?.message || 'Không tải được danh sách giáo viên'
  } finally {
    dangTai.value = false
  }
}

async function luuGiaoVien() {
  loi.value = ''
  thongBao.value = ''

  const payload = {
    maGiaoVien: form.maGiaoVien,
    hoTen: form.hoTen,
    email: form.email,
    soDienThoai: form.soDienThoai || null,
    chuyenMon: form.chuyenMon || null,
    trangThai: form.trangThai,
    taiKhoanId: form.taiKhoanId === '' ? null : Number(form.taiKhoanId)
  }

  try {
    if (form.id) {
      await giangDayService.capNhatGiaoVien(form.id, payload)
      thongBao.value = 'Cập nhật giáo viên thành công'
    } else {
      await giangDayService.taoGiaoVien(payload)
      thongBao.value = 'Thêm giáo viên thành công'
    }

    resetForm()
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Lưu giáo viên thất bại'
  }
}

function chonSua(item) {
  form.id = item.id
  form.maGiaoVien = item.maGiaoVien || ''
  form.hoTen = item.hoTen || ''
  form.email = item.email || ''
  form.soDienThoai = item.soDienThoai || ''
  form.chuyenMon = item.chuyenMon || ''
  form.trangThai = item.trangThai || 'dang_day'
  form.taiKhoanId = item.taiKhoanId || ''
}

async function xoaGiaoVien(item) {
  const ok = window.confirm(`Xóa giáo viên "${item.hoTen}"?`)
  if (!ok) return

  try {
    await giangDayService.xoaGiaoVien(item.id)
    thongBao.value = 'Xóa giáo viên thành công'
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Xóa giáo viên thất bại'
  }
}

function resetForm() {
  form.id = null
  form.maGiaoVien = ''
  form.hoTen = ''
  form.email = ''
  form.soDienThoai = ''
  form.chuyenMon = ''
  form.trangThai = 'dang_day'
  form.taiKhoanId = ''
}

function locDuLieu() {
  trangHienTai.value = 0
  taiDuLieu()
}

function xoaLoc() {
  boLoc.keyword = ''
  boLoc.trangThai = ''
  boLoc.sortBy = 'id'
  boLoc.sortDir = 'desc'
  trangHienTai.value = 0
  taiDuLieu()
}

function doiTrang(page) {
  if (page < 0) return
  if (tongTrang.value && page >= tongTrang.value) return

  trangHienTai.value = page
  taiDuLieu()
}

function hienThiTrangThai(value) {
  if (value === 'dang_day') return 'Đang dạy'
  if (value === 'tam_nghi') return 'Tạm nghỉ'
  if (value === 'nghi_viec') return 'Nghỉ việc'
  return value || '-'
}
</script>

<style scoped>
.page {
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

.filter-card,
.form-card {
  padding: 16px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  background: var(--color-white);
}

.filter-card {
  display: grid;
  grid-template-columns: 1.5fr 1fr 1fr 1fr auto;
  gap: 12px;
}

.form-card h2 {
  margin: 0 0 12px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(160px, 1fr));
  gap: 12px;
}

label {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

label span {
  font-size: 13px;
  font-weight: 700;
  color: #334155;
}

.field-full {
  grid-column: 1 / -1;
}

input,
select,
textarea {
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  padding: 8px 10px;
}

.filter-actions,
.form-actions,
.row-actions,
.pagination-bar {
  display: flex;
  gap: 8px;
}

.filter-actions {
  align-items: flex-end;
}

.form-actions {
  grid-column: 1 / -1;
}

.pagination-bar {
  justify-content: flex-end;
}

.btn {
  border: 1px solid var(--color-border);
  background: var(--color-white);
  color: var(--color-text);
  border-radius: var(--radius);
  padding: 8px 12px;
  cursor: pointer;
  font-weight: 700;
}

.btn.primary {
  background: var(--color-primary);
  color: var(--color-white);
  border-color: var(--color-primary);
}

.btn.small {
  padding: 6px 10px;
  font-size: 12px;
}

.btn.danger {
  color: #b91c1c;
  border-color: #fecaca;
  background: #fff5f5;
}

.btn:disabled {
  opacity: 0.55;
  cursor: not-allowed;
}

.alert {
  padding: 10px 12px;
  border-radius: var(--radius);
}

.alert.success {
  background: #ecfdf5;
  color: #047857;
}

.alert.error {
  background: #fee2e2;
  color: #991b1b;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
}

.summary-grid div {
  border: 1px solid var(--color-border);
  background: var(--color-white);
  border-radius: var(--radius);
  padding: 14px;
  display: grid;
  gap: 6px;
}

.summary-grid span {
  color: #64748b;
  font-weight: 700;
  font-size: 13px;
}

.summary-grid strong {
  font-size: 22px;
}

.table-wrap {
  overflow: auto;
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  background: var(--color-white);
}

table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1100px;
}

th,
td {
  padding: 10px 12px;
  border-bottom: 1px solid var(--color-border);
  text-align: left;
  vertical-align: top;
}

th {
  background: #f8fafc;
  color: #475569;
  font-size: 12px;
  text-transform: uppercase;
}

@media (max-width: 1100px) {
  .filter-card,
  .form-grid,
  .summary-grid {
    grid-template-columns: 1fr;
  }

  .filter-actions {
    align-items: stretch;
  }
}
</style>