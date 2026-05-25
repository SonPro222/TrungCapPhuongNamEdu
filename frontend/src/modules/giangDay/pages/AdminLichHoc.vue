<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Quản lý lịch học</h1>
        <p>Xếp lịch học theo lớp học phần, giáo viên, phòng học, ca học và ngày học.</p>
      </div>

      <button type="button" class="btn" @click="taiDuLieu">Tải lại</button>
    </header>

    <section class="filter-card">
      <label>
        <span>Lọc lớp học phần</span>
        <input
            v-model.trim="boLoc.keywordLop"
            type="text"
            placeholder="Mã lớp hoặc tên lớp"
            @keyup.enter="locDuLieu"
        />
      </label>

      <label>
        <span>Lọc giáo viên</span>
        <input
            v-model.trim="boLoc.keywordGiaoVien"
            type="text"
            placeholder="Mã giáo viên hoặc tên giáo viên"
            @keyup.enter="locDuLieu"
        />
      </label>

      <label>
        <span>Lọc phòng học</span>
        <input
            v-model.trim="boLoc.keywordPhong"
            type="text"
            placeholder="Mã phòng hoặc tên phòng"
            @keyup.enter="locDuLieu"
        />
      </label>

      <label>
        <span>Lọc ca học</span>
        <input
            v-model.trim="boLoc.keywordCa"
            type="text"
            placeholder="Mã ca hoặc tên ca"
            @keyup.enter="locDuLieu"
        />
      </label>

      <label>
        <span>Trạng thái</span>
        <select v-model="boLoc.trangThai" @change="locDuLieu">
          <option value="">Tất cả</option>
          <option value="du_kien">Dự kiến</option>
          <option value="da_hoc">Đã học</option>
          <option value="nghi">Nghỉ</option>
          <option value="doi_lich">Đổi lịch</option>
        </select>
      </label>

      <label>
        <span>Từ ngày</span>
        <input v-model="boLoc.tuNgay" type="date" @change="locDuLieu" />
      </label>

      <label>
        <span>Đến ngày</span>
        <input v-model="boLoc.denNgay" type="date" @change="locDuLieu" />
      </label>

      <label>
        <span>Sắp xếp</span>
        <select v-model="boLoc.sortBy" @change="locDuLieu">
          <option value="ngayHoc">Ngày học</option>
          <option value="id">ID</option>
          <option value="lopHocPhanId">Lớp học phần</option>
          <option value="giaoVienId">Giáo viên</option>
          <option value="phongHocId">Phòng học</option>
          <option value="caHocId">Ca học</option>
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
      <h2>{{ form.id ? 'Cập nhật lịch học' : 'Thêm lịch học' }}</h2>

      <form class="form-grid" @submit.prevent="luuLichHoc">
        <label>
          <span>Lớp học phần ID</span>
          <input v-model.trim="form.lopHocPhanId" type="number" required />
          <small>{{ moTaLopTrongForm }}</small>
        </label>

        <label>
          <span>Giáo viên ID</span>
          <input v-model.trim="form.giaoVienId" type="number" />
          <small>{{ moTaGiaoVienTrongForm }}</small>
        </label>

        <label>
          <span>Phòng học ID</span>
          <input v-model.trim="form.phongHocId" type="number" />
          <small>{{ moTaPhongTrongForm }}</small>
        </label>

        <label>
          <span>Ca học ID</span>
          <input v-model.trim="form.caHocId" type="number" />
          <small>{{ moTaCaTrongForm }}</small>
        </label>

        <label>
          <span>Ngày học</span>
          <input v-model="form.ngayHoc" type="date" required />
        </label>

        <label>
          <span>Trạng thái</span>
          <select v-model="form.trangThai" required>
            <option value="du_kien">Dự kiến</option>
            <option value="da_hoc">Đã học</option>
            <option value="nghi">Nghỉ</option>
            <option value="doi_lich">Đổi lịch</option>
          </select>
        </label>

        <label class="field-full">
          <span>Nội dung buổi học</span>
          <textarea v-model.trim="form.noiDungBuoiHoc" rows="2" />
        </label>

        <label class="field-full">
          <span>Ghi chú</span>
          <textarea v-model.trim="form.ghiChu" rows="2" />
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
          <th>Lớp học phần</th>
          <th>Giáo viên</th>
          <th>Phòng học</th>
          <th>Ca học</th>
          <th>Ngày học</th>
          <th>Trạng thái</th>
          <th>Nội dung</th>
          <th>Ghi chú</th>
          <th>Thao tác</th>
        </tr>
        </thead>

        <tbody>
        <tr v-if="dangTai"><td colspan="10">Đang tải dữ liệu...</td></tr>
        <tr v-else-if="!danhSach.length"><td colspan="10">Không có dữ liệu</td></tr>

        <tr v-for="item in danhSach" v-else :key="item.id">
          <td>{{ item.id }}</td>

          <td>
            <div class="cell-main">
              <strong>{{ item.maLop || '-' }}</strong>
              <span>{{ item.tenLop || '-' }}</span>
              <small>ID: {{ item.lopHocPhanId }}</small>
            </div>
          </td>

          <td>
            <div class="cell-main">
              <strong>{{ item.maGiaoVien || '-' }}</strong>
              <span>{{ item.tenGiaoVien || '-' }}</span>
              <small>ID: {{ item.giaoVienId || '-' }}</small>
            </div>
          </td>

          <td>
            <div class="cell-main">
              <strong>{{ item.maPhong || '-' }}</strong>
              <span>{{ item.tenPhong || '-' }}</span>
              <small>ID: {{ item.phongHocId || '-' }}</small>
            </div>
          </td>

          <td>
            <div class="cell-main">
              <strong>{{ item.maCa || '-' }}</strong>
              <span>{{ item.tenCa || '-' }}</span>
              <small>{{ hienThiGioCa(item) }}</small>
            </div>
          </td>

          <td>{{ item.ngayHoc }}</td>
          <td>{{ hienThiTrangThai(item.trangThai) }}</td>
          <td>{{ item.noiDungBuoiHoc || '-' }}</td>
          <td>{{ item.ghiChu || '-' }}</td>

          <td>
            <div class="row-actions">
              <button type="button" class="btn small" @click="chonSua(item)">Sửa</button>
              <button type="button" class="btn small danger" @click="xoaLichHoc(item)">Xóa</button>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
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
  keywordLop: '',
  keywordGiaoVien: '',
  keywordPhong: '',
  keywordCa: '',
  trangThai: '',
  tuNgay: '',
  denNgay: '',
  sortBy: 'ngayHoc',
  sortDir: 'desc'
})

const form = reactive({
  id: null,
  lopHocPhanId: '',
  giaoVienId: '',
  phongHocId: '',
  caHocId: '',
  ngayHoc: '',
  noiDungBuoiHoc: '',
  trangThai: 'du_kien',
  ghiChu: ''
})

const moTaLopTrongForm = computed(() => {
  if (!form.lopHocPhanId) return 'Nhập ID lớp học phần'
  const item = danhSach.value.find((row) => String(row.lopHocPhanId) === String(form.lopHocPhanId))
  if (!item) return 'Lớp không nằm trong trang hiện tại'
  return `${item.maLop || '-'} - ${item.tenLop || '-'}`
})

const moTaGiaoVienTrongForm = computed(() => {
  if (!form.giaoVienId) return 'Nhập ID giáo viên'
  const item = danhSach.value.find((row) => String(row.giaoVienId) === String(form.giaoVienId))
  if (!item) return 'Giáo viên không nằm trong trang hiện tại'
  return `${item.maGiaoVien || '-'} - ${item.tenGiaoVien || '-'}`
})

const moTaPhongTrongForm = computed(() => {
  if (!form.phongHocId) return 'Nhập ID phòng học'
  const item = danhSach.value.find((row) => String(row.phongHocId) === String(form.phongHocId))
  if (!item) return 'Phòng không nằm trong trang hiện tại'
  return `${item.maPhong || '-'} - ${item.tenPhong || '-'}`
})

const moTaCaTrongForm = computed(() => {
  if (!form.caHocId) return 'Nhập ID ca học'
  const item = danhSach.value.find((row) => String(row.caHocId) === String(form.caHocId))
  if (!item) return 'Ca không nằm trong trang hiện tại'
  return `${item.maCa || '-'} - ${item.tenCa || '-'} ${hienThiGioCa(item)}`
})

onMounted(() => taiDuLieu())

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''
  thongBao.value = ''

  try {
    const page = await giangDayService.layTrangLichHoc({
      page: trangHienTai.value,
      size: kichThuocTrang,
      keywordLop: boLoc.keywordLop || undefined,
      keywordGiaoVien: boLoc.keywordGiaoVien || undefined,
      keywordPhong: boLoc.keywordPhong || undefined,
      keywordCa: boLoc.keywordCa || undefined,
      trangThai: boLoc.trangThai || undefined,
      tuNgay: boLoc.tuNgay || undefined,
      denNgay: boLoc.denNgay || undefined,
      sortBy: boLoc.sortBy,
      sortDir: boLoc.sortDir
    })

    danhSach.value = page.content
    trangHienTai.value = page.number
    tongTrang.value = page.totalPages
    tongBanGhi.value = page.totalElements
  } catch (error) {
    loi.value = error?.message || 'Không tải được danh sách lịch học'
  } finally {
    dangTai.value = false
  }
}

async function luuLichHoc() {
  const payload = {
    lopHocPhanId: Number(form.lopHocPhanId),
    giaoVienId: form.giaoVienId === '' ? null : Number(form.giaoVienId),
    phongHocId: form.phongHocId === '' ? null : Number(form.phongHocId),
    caHocId: form.caHocId === '' ? null : Number(form.caHocId),
    ngayHoc: form.ngayHoc,
    noiDungBuoiHoc: form.noiDungBuoiHoc || null,
    trangThai: form.trangThai,
    ghiChu: form.ghiChu || null
  }

  try {
    if (form.id) {
      await giangDayService.capNhatLichHoc(form.id, payload)
      thongBao.value = 'Cập nhật lịch học thành công'
    } else {
      await giangDayService.taoLichHoc(payload)
      thongBao.value = 'Thêm lịch học thành công'
    }

    resetForm()
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Lưu lịch học thất bại'
  }
}

function chonSua(item) {
  form.id = item.id
  form.lopHocPhanId = item.lopHocPhanId || ''
  form.giaoVienId = item.giaoVienId || ''
  form.phongHocId = item.phongHocId || ''
  form.caHocId = item.caHocId || ''
  form.ngayHoc = item.ngayHoc || ''
  form.noiDungBuoiHoc = item.noiDungBuoiHoc || ''
  form.trangThai = item.trangThai || 'du_kien'
  form.ghiChu = item.ghiChu || ''
}

async function xoaLichHoc(item) {
  const ok = window.confirm(`Xóa lịch học lớp ${item.maLop || item.lopHocPhanId} ngày ${item.ngayHoc}?`)
  if (!ok) return

  try {
    await giangDayService.xoaLichHoc(item.id)
    thongBao.value = 'Xóa lịch học thành công'
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Xóa lịch học thất bại'
  }
}

function resetForm() {
  form.id = null
  form.lopHocPhanId = ''
  form.giaoVienId = ''
  form.phongHocId = ''
  form.caHocId = ''
  form.ngayHoc = ''
  form.noiDungBuoiHoc = ''
  form.trangThai = 'du_kien'
  form.ghiChu = ''
}

function locDuLieu() {
  trangHienTai.value = 0
  taiDuLieu()
}

function xoaLoc() {
  boLoc.keywordLop = ''
  boLoc.keywordGiaoVien = ''
  boLoc.keywordPhong = ''
  boLoc.keywordCa = ''
  boLoc.trangThai = ''
  boLoc.tuNgay = ''
  boLoc.denNgay = ''
  boLoc.sortBy = 'ngayHoc'
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
  const map = {
    du_kien: 'Dự kiến',
    da_hoc: 'Đã học',
    nghi: 'Nghỉ',
    doi_lich: 'Đổi lịch'
  }
  return map[value] || value || '-'
}

function hienThiGioCa(item) {
  if (!item?.gioBatDau && !item?.gioKetThuc) return ''
  return `${item.gioBatDau || '?'} - ${item.gioKetThuc || '?'}`
}
</script>

<style scoped>
.page { display: flex; flex-direction: column; gap: 16px; }
.page-head { display: flex; justify-content: space-between; gap: 16px; }
.page-head h1 { margin: 0; }
.page-head p { margin: 6px 0 0; color: #64748b; }
.filter-card, .form-card { padding: 16px; border: 1px solid var(--color-border); border-radius: var(--radius); background: var(--color-white); }
.filter-card { display: grid; grid-template-columns: repeat(5, minmax(140px, 1fr)) auto; gap: 12px; }
.form-card h2 { margin: 0 0 12px; }
.form-grid { display: grid; grid-template-columns: repeat(4, minmax(150px, 1fr)); gap: 12px; }
label { display: flex; flex-direction: column; gap: 6px; }
label span { font-size: 13px; font-weight: 700; color: #334155; }
label small { color: #64748b; font-size: 12px; }
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
.cell-main { display: grid; gap: 4px; }
.cell-main strong { color: #0f172a; }
.cell-main span { color: #334155; }
.cell-main small { color: #64748b; font-size: 12px; }
.table-wrap { overflow: auto; border: 1px solid var(--color-border); border-radius: var(--radius); background: var(--color-white); }
table { width: 100%; border-collapse: collapse; min-width: 1350px; }
th, td { padding: 10px 12px; border-bottom: 1px solid var(--color-border); text-align: left; vertical-align: top; }
th { background: #f8fafc; color: #475569; font-size: 12px; text-transform: uppercase; }
@media (max-width: 1100px) {
  .filter-card, .form-grid, .summary-grid { grid-template-columns: 1fr; }
  .filter-actions { align-items: stretch; }
}
</style>