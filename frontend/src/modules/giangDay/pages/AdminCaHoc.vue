<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Quản lý ca học</h1>
        <p>Quản lý mã ca, thời gian bắt đầu, kết thúc để dùng cho lịch học và lịch thi.</p>
      </div>

      <button type="button" class="btn" @click="taiDuLieu">Tải lại</button>
    </header>

    <section class="filter-card">
      <label>
        <span>Từ khóa</span>
        <input v-model.trim="boLoc.keyword" type="text" placeholder="Mã ca, tên ca, mô tả" @keyup.enter="locDuLieu" />
      </label>

      <label>
        <span>Sắp xếp</span>
        <select v-model="boLoc.sortBy" @change="locDuLieu">
          <option value="id">ID</option>
          <option value="maCa">Mã ca</option>
          <option value="tenCa">Tên ca</option>
          <option value="gioBatDau">Giờ bắt đầu</option>
          <option value="gioKetThuc">Giờ kết thúc</option>
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
      <h2>{{ form.id ? 'Cập nhật ca học' : 'Thêm ca học' }}</h2>

      <form class="form-grid" @submit.prevent="luuCaHoc">
        <label>
          <span>Mã ca</span>
          <input v-model.trim="form.maCa" type="text" required />
        </label>

        <label>
          <span>Tên ca</span>
          <input v-model.trim="form.tenCa" type="text" required />
        </label>

        <label>
          <span>Giờ bắt đầu</span>
          <input v-model="form.gioBatDau" type="time" required />
        </label>

        <label>
          <span>Giờ kết thúc</span>
          <input v-model="form.gioKetThuc" type="time" required />
        </label>

        <label class="field-full">
          <span>Mô tả</span>
          <textarea v-model.trim="form.moTa" rows="2" />
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
          <th>Mã ca</th>
          <th>Tên ca</th>
          <th>Giờ bắt đầu</th>
          <th>Giờ kết thúc</th>
          <th>Mô tả</th>
          <th>Thao tác</th>
        </tr>
        </thead>

        <tbody>
        <tr v-if="dangTai"><td colspan="7">Đang tải dữ liệu...</td></tr>
        <tr v-else-if="!danhSach.length"><td colspan="7">Không có dữ liệu</td></tr>

        <tr v-for="item in danhSach" v-else :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.maCa }}</td>
          <td>{{ item.tenCa }}</td>
          <td>{{ item.gioBatDau }}</td>
          <td>{{ item.gioKetThuc }}</td>
          <td>{{ item.moTa || '-' }}</td>
          <td>
            <div class="row-actions">
              <button type="button" class="btn small" @click="chonSua(item)">Sửa</button>
              <button type="button" class="btn small danger" @click="xoaCaHoc(item)">Xóa</button>
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
  sortBy: 'id',
  sortDir: 'desc'
})

const form = reactive({
  id: null,
  maCa: '',
  tenCa: '',
  gioBatDau: '',
  gioKetThuc: '',
  moTa: ''
})

onMounted(() => taiDuLieu())

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''
  thongBao.value = ''

  try {
    const page = await giangDayService.layTrangCaHoc({
      page: trangHienTai.value,
      size: kichThuocTrang,
      keyword: boLoc.keyword || undefined,
      sortBy: boLoc.sortBy,
      sortDir: boLoc.sortDir
    })

    danhSach.value = page.content
    trangHienTai.value = page.number
    tongTrang.value = page.totalPages
    tongBanGhi.value = page.totalElements
  } catch (error) {
    loi.value = error?.message || 'Không tải được danh sách ca học'
  } finally {
    dangTai.value = false
  }
}

async function luuCaHoc() {
  const payload = {
    maCa: form.maCa,
    tenCa: form.tenCa,
    gioBatDau: form.gioBatDau,
    gioKetThuc: form.gioKetThuc,
    moTa: form.moTa || null
  }

  try {
    if (form.id) {
      await giangDayService.capNhatCaHoc(form.id, payload)
      thongBao.value = 'Cập nhật ca học thành công'
    } else {
      await giangDayService.taoCaHoc(payload)
      thongBao.value = 'Thêm ca học thành công'
    }

    resetForm()
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Lưu ca học thất bại'
  }
}

function chonSua(item) {
  form.id = item.id
  form.maCa = item.maCa || ''
  form.tenCa = item.tenCa || ''
  form.gioBatDau = item.gioBatDau || ''
  form.gioKetThuc = item.gioKetThuc || ''
  form.moTa = item.moTa || ''
}

async function xoaCaHoc(item) {
  const ok = window.confirm(`Xóa ca học "${item.tenCa}"?`)
  if (!ok) return

  try {
    await giangDayService.xoaCaHoc(item.id)
    thongBao.value = 'Xóa ca học thành công'
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Xóa ca học thất bại'
  }
}

function resetForm() {
  form.id = null
  form.maCa = ''
  form.tenCa = ''
  form.gioBatDau = ''
  form.gioKetThuc = ''
  form.moTa = ''
}

function locDuLieu() {
  trangHienTai.value = 0
  taiDuLieu()
}

function xoaLoc() {
  boLoc.keyword = ''
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
</script>

<style scoped>
.page { display: flex; flex-direction: column; gap: 16px; }
.page-head { display: flex; justify-content: space-between; gap: 16px; }
.page-head h1 { margin: 0; }
.page-head p { margin: 6px 0 0; color: #64748b; }
.filter-card, .form-card { padding: 16px; border: 1px solid var(--color-border); border-radius: var(--radius); background: var(--color-white); }
.filter-card { display: grid; grid-template-columns: 1.5fr 1fr 1fr auto; gap: 12px; }
.form-card h2 { margin: 0 0 12px; }
.form-grid { display: grid; grid-template-columns: repeat(4, minmax(160px, 1fr)); gap: 12px; }
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
table { width: 100%; border-collapse: collapse; min-width: 850px; }
th, td { padding: 10px 12px; border-bottom: 1px solid var(--color-border); text-align: left; vertical-align: top; }
th { background: #f8fafc; color: #475569; font-size: 12px; text-transform: uppercase; }
@media (max-width: 1100px) {
  .filter-card, .form-grid, .summary-grid { grid-template-columns: 1fr; }
  .filter-actions { align-items: stretch; }
}
</style>