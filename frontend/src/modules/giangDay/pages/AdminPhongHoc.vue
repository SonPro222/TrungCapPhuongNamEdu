<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Quản lý phòng học</h1>
        <p>Quản lý phòng lý thuyết, phòng máy, phòng thi, xưởng và trạng thái sử dụng.</p>
      </div>

      <button type="button" class="btn" @click="taiDuLieu">Tải lại</button>
    </header>

    <section class="filter-card">
      <label>
        <span>Từ khóa</span>
        <input v-model.trim="boLoc.keyword" type="text" placeholder="Mã phòng, tên phòng, địa điểm" @keyup.enter="locDuLieu" />
      </label>

      <label>
        <span>Loại phòng</span>
        <select v-model="boLoc.loaiPhong" @change="locDuLieu">
          <option value="">Tất cả</option>
          <option value="ly_thuyet">Lý thuyết</option>
          <option value="thuc_hanh">Thực hành</option>
          <option value="phong_may">Phòng máy</option>
          <option value="phong_thi">Phòng thi</option>
          <option value="xuong">Xưởng</option>
          <option value="khac">Khác</option>
        </select>
      </label>

      <label>
        <span>Trạng thái</span>
        <select v-model="boLoc.trangThai" @change="locDuLieu">
          <option value="">Tất cả</option>
          <option value="dang_su_dung">Đang sử dụng</option>
          <option value="bao_tri">Bảo trì</option>
          <option value="ngung_su_dung">Ngưng sử dụng</option>
        </select>
      </label>

      <label>
        <span>Sắp xếp</span>
        <select v-model="boLoc.sortBy" @change="locDuLieu">
          <option value="id">ID</option>
          <option value="maPhong">Mã phòng</option>
          <option value="tenPhong">Tên phòng</option>
          <option value="sucChua">Sức chứa</option>
          <option value="trangThai">Trạng thái</option>
        </select>
      </label>

      <label>
        <span>Chiều</span>
        <select v-model="boLoc.sortDir" @change="locDuLieu">
          <option value="desc">Giảm dần</option>
          <option value="asc">Tăng dần</option>
        </select>
      </label>

      <div class="filter-actions">
        <button type="button" class="btn primary" @click="locDuLieu">Lọc</button>
        <button type="button" class="btn" @click="xoaLoc">Xóa lọc</button>
      </div>
    </section>

    <section class="form-card">
      <h2>{{ form.id ? 'Cập nhật phòng học' : 'Thêm phòng học' }}</h2>

      <form class="form-grid" @submit.prevent="luuPhongHoc">
        <label>
          <span>Mã phòng</span>
          <input v-model.trim="form.maPhong" type="text" required />
        </label>

        <label>
          <span>Tên phòng</span>
          <input v-model.trim="form.tenPhong" type="text" required />
        </label>

        <label>
          <span>Loại phòng</span>
          <select v-model="form.loaiPhong">
            <option value="">-- Chọn --</option>
            <option value="ly_thuyet">Lý thuyết</option>
            <option value="thuc_hanh">Thực hành</option>
            <option value="phong_may">Phòng máy</option>
            <option value="phong_thi">Phòng thi</option>
            <option value="xuong">Xưởng</option>
            <option value="khac">Khác</option>
          </select>
        </label>

        <label>
          <span>Sức chứa</span>
          <input v-model.trim="form.sucChua" type="number" min="0" />
        </label>

        <label>
          <span>Trạng thái</span>
          <select v-model="form.trangThai" required>
            <option value="dang_su_dung">Đang sử dụng</option>
            <option value="bao_tri">Bảo trì</option>
            <option value="ngung_su_dung">Ngưng sử dụng</option>
          </select>
        </label>

        <label class="field-full">
          <span>Địa điểm</span>
          <textarea v-model.trim="form.diaDiem" rows="2" />
        </label>

        <div class="form-actions">
          <button type="submit" class="btn primary">{{ form.id ? 'Cập nhật' : 'Thêm mới' }}</button>
          <button type="button" class="btn" @click="resetForm">Làm mới</button>
        </div>
      </form>
    </section>

    <div v-if="thongBao" class="alert success">{{ thongBao }}</div>
    <div v-if="loi" class="alert error">{{ loi }}</div>

    <section class="summary-grid">
      <div><span>Đang hiển thị</span><strong>{{ danhSach.length }}</strong></div>
      <div><span>Tổng bản ghi</span><strong>{{ tongBanGhi }}</strong></div>
      <div><span>Trang</span><strong>{{ trangHienTai + 1 }} / {{ tongTrang || 1 }}</strong></div>
    </section>

    <div class="pagination-bar">
      <button type="button" class="btn" :disabled="trangHienTai <= 0 || dangTai" @click="doiTrang(trangHienTai - 1)">Trang trước</button>
      <button type="button" class="btn" :disabled="tongTrang === 0 || trangHienTai + 1 >= tongTrang || dangTai" @click="doiTrang(trangHienTai + 1)">Trang sau</button>
    </div>

    <div class="table-wrap">
      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>Mã phòng</th>
          <th>Tên phòng</th>
          <th>Loại</th>
          <th>Sức chứa</th>
          <th>Địa điểm</th>
          <th>Trạng thái</th>
          <th>Thao tác</th>
        </tr>
        </thead>

        <tbody>
        <tr v-if="dangTai"><td colspan="8">Đang tải dữ liệu...</td></tr>
        <tr v-else-if="!danhSach.length"><td colspan="8">Không có dữ liệu</td></tr>

        <tr v-for="item in danhSach" v-else :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.maPhong }}</td>
          <td>{{ item.tenPhong }}</td>
          <td>{{ hienThiLoaiPhong(item.loaiPhong) }}</td>
          <td>{{ item.sucChua ?? '-' }}</td>
          <td>{{ item.diaDiem || '-' }}</td>
          <td>{{ hienThiTrangThai(item.trangThai) }}</td>
          <td>
            <div class="row-actions">
              <button type="button" class="btn small" @click="chonSua(item)">Sửa</button>
              <button type="button" class="btn small danger" @click="xoaPhongHoc(item)">Xóa</button>
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
  loaiPhong: '',
  trangThai: '',
  sortBy: 'id',
  sortDir: 'desc'
})

const form = reactive({
  id: null,
  maPhong: '',
  tenPhong: '',
  loaiPhong: '',
  sucChua: '',
  diaDiem: '',
  trangThai: 'dang_su_dung'
})

onMounted(() => taiDuLieu())

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''
  thongBao.value = ''

  try {
    const page = await giangDayService.layTrangPhongHoc({
      page: trangHienTai.value,
      size: kichThuocTrang,
      keyword: boLoc.keyword || undefined,
      loaiPhong: boLoc.loaiPhong || undefined,
      trangThai: boLoc.trangThai || undefined,
      sortBy: boLoc.sortBy,
      sortDir: boLoc.sortDir
    })

    danhSach.value = page.content
    trangHienTai.value = page.number
    tongTrang.value = page.totalPages
    tongBanGhi.value = page.totalElements
  } catch (error) {
    loi.value = error?.message || 'Không tải được danh sách phòng học'
  } finally {
    dangTai.value = false
  }
}

async function luuPhongHoc() {
  const payload = {
    maPhong: form.maPhong,
    tenPhong: form.tenPhong,
    loaiPhong: form.loaiPhong || null,
    sucChua: form.sucChua === '' ? null : Number(form.sucChua),
    diaDiem: form.diaDiem || null,
    trangThai: form.trangThai
  }

  try {
    if (form.id) {
      await giangDayService.capNhatPhongHoc(form.id, payload)
      thongBao.value = 'Cập nhật phòng học thành công'
    } else {
      await giangDayService.taoPhongHoc(payload)
      thongBao.value = 'Thêm phòng học thành công'
    }

    resetForm()
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Lưu phòng học thất bại'
  }
}

function chonSua(item) {
  form.id = item.id
  form.maPhong = item.maPhong || ''
  form.tenPhong = item.tenPhong || ''
  form.loaiPhong = item.loaiPhong || ''
  form.sucChua = item.sucChua ?? ''
  form.diaDiem = item.diaDiem || ''
  form.trangThai = item.trangThai || 'dang_su_dung'
}

async function xoaPhongHoc(item) {
  const ok = window.confirm(`Xóa phòng học "${item.tenPhong}"?`)
  if (!ok) return

  try {
    await giangDayService.xoaPhongHoc(item.id)
    thongBao.value = 'Xóa phòng học thành công'
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Xóa phòng học thất bại'
  }
}

function resetForm() {
  form.id = null
  form.maPhong = ''
  form.tenPhong = ''
  form.loaiPhong = ''
  form.sucChua = ''
  form.diaDiem = ''
  form.trangThai = 'dang_su_dung'
}

function locDuLieu() {
  trangHienTai.value = 0
  taiDuLieu()
}

function xoaLoc() {
  boLoc.keyword = ''
  boLoc.loaiPhong = ''
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

function hienThiLoaiPhong(value) {
  const map = {
    ly_thuyet: 'Lý thuyết',
    thuc_hanh: 'Thực hành',
    phong_may: 'Phòng máy',
    phong_thi: 'Phòng thi',
    xuong: 'Xưởng',
    khac: 'Khác'
  }
  return map[value] || value || '-'
}

function hienThiTrangThai(value) {
  const map = {
    dang_su_dung: 'Đang sử dụng',
    bao_tri: 'Bảo trì',
    ngung_su_dung: 'Ngưng sử dụng'
  }
  return map[value] || value || '-'
}
</script>

<style scoped>
.page { display: flex; flex-direction: column; gap: 16px; }
.page-head { display: flex; justify-content: space-between; gap: 16px; }
.page-head h1 { margin: 0; }
.page-head p { margin: 6px 0 0; color: #64748b; }
.filter-card, .form-card { padding: 16px; border: 1px solid var(--color-border); border-radius: var(--radius); background: var(--color-white); }
.filter-card { display: grid; grid-template-columns: 1.5fr 1fr 1fr 1fr 1fr auto; gap: 12px; }
.form-card h2 { margin: 0 0 12px; }
.form-grid { display: grid; grid-template-columns: repeat(3, minmax(160px, 1fr)); gap: 12px; }
label { display: flex; flex-direction: column; gap: 6px; }
label span { font-size: 13px; font-weight: 700; color: #334155; }
.field-full { grid-column: 1 / -1; }
input, select, textarea { border: 1px solid var(--color-border); border-radius: var(--radius); padding: 8px 10px; }
.filter-actions, .form-actions, .row-actions, .pagination-bar { display: flex; gap: 8px; }
.filter-actions { align-items: flex-end; }
.form-actions { grid-column: 1 / -1; }
.pagination-bar { justify-content: flex-end; }
.btn { border: 1px solid var(--color-border); background: var(--color-white); color: var(--color-text); border-radius: var(--radius); padding: 8px 12px; cursor: pointer; font-weight: 700; }
.btn.primary { background: var(--color-primary); color: var(--color-white); border-color: var(--color-primary); }
.btn.small { padding: 6px 10px; font-size: 12px; }
.btn.danger { color: #b91c1c; border-color: #fecaca; background: #fff5f5; }
.btn:disabled { opacity: 0.55; cursor: not-allowed; }
.alert { padding: 10px 12px; border-radius: var(--radius); }
.alert.success { background: #ecfdf5; color: #047857; }
.alert.error { background: #fee2e2; color: #991b1b; }
.summary-grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 12px; }
.summary-grid div { border: 1px solid var(--color-border); background: var(--color-white); border-radius: var(--radius); padding: 14px; display: grid; gap: 6px; }
.summary-grid span { color: #64748b; font-weight: 700; font-size: 13px; }
.summary-grid strong { font-size: 22px; }
.table-wrap { overflow: auto; border: 1px solid var(--color-border); border-radius: var(--radius); background: var(--color-white); }
table { width: 100%; border-collapse: collapse; min-width: 1000px; }
th, td { padding: 10px 12px; border-bottom: 1px solid var(--color-border); text-align: left; vertical-align: top; }
th { background: #f8fafc; color: #475569; font-size: 12px; text-transform: uppercase; }
@media (max-width: 1100px) {
  .filter-card, .form-grid, .summary-grid { grid-template-columns: 1fr; }
  .filter-actions { align-items: stretch; }
}
</style>