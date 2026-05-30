<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Quản lý điểm danh</h1>
        <p>Chọn ngành, version, lớp học phần và buổi học để xem tình trạng điểm danh sinh viên.</p>
      </div>

      <button type="button" class="btn" @click="taiDuLieu">
        Tải lại
      </button>
    </header>

    <section class="flow-card">
      <label>
        <span>Ngành</span>
        <select v-model="boLoc.nganhId" @change="doiNganh">
          <option value="">Chọn ngành</option>
          <option v-for="item in danhSachNganh" :key="item.id" :value="item.id">
            {{ item.maNganh ? item.maNganh + ' - ' : '' }}{{ item.tenNganh || item.ten || item.id }}
          </option>
        </select>
      </label>

      <label>
        <span>Version chương trình</span>
        <select v-model="boLoc.chuongTrinhVersionId" @change="doiVersion">
          <option value="">Chọn version</option>
          <option v-for="item in danhSachVersionTheoNganh" :key="item.id" :value="item.id">
            {{ hienThiVersion(item) }}
          </option>
        </select>
      </label>

      <label>
        <span>Lớp học phần</span>
        <select v-model="boLoc.lopHocPhanId" @change="doiLopHocPhan">
          <option value="">Chọn lớp học phần</option>
          <option v-for="item in danhSachLopHocPhan" :key="item.id" :value="item.id">
            {{ item.maLop }} - {{ item.tenLop }}
          </option>
        </select>
      </label>

      <label>
        <span>Buổi học</span>
        <select v-model="boLoc.lichHocId" @change="locDuLieu">
          <option value="">Chọn buổi học</option>
          <option v-for="item in danhSachLichHoc" :key="item.id" :value="item.id">
            {{ item.ngayHoc }} - {{ item.noiDungBuoiHoc || 'Buổi học #' + item.id }}
          </option>
        </select>
      </label>

      <label>
        <span>Tìm sinh viên</span>
        <input
            v-model.trim="boLoc.keywordSinhVien"
            type="text"
            placeholder="Mã SV hoặc họ tên"
            @keyup.enter="locDuLieu"
        />
      </label>

      <label>
        <span>Trạng thái điểm danh</span>
        <select v-model="boLoc.trangThai" @change="locDuLieu">
          <option value="">Tất cả</option>
          <option value="co_mat">Có mặt</option>
          <option value="vang_co_phep">Vắng có phép</option>
          <option value="vang_khong_phep">Vắng không phép</option>
          <option value="di_muon">Đi muộn</option>
        </select>
      </label>

      <div class="filter-actions">
        <button type="button" class="btn primary" @click="locDuLieu">
          Xem điểm danh
        </button>

        <button type="button" class="btn" @click="xoaLoc">
          Xóa chọn
        </button>
      </div>
    </section>

    <section class="context-card">
      <div>
        <span>Ngành</span>
        <strong>{{ tenNganhDangChon }}</strong>
      </div>

      <div>
        <span>Version</span>
        <strong>{{ tenVersionDangChon }}</strong>
      </div>

      <div>
        <span>Lớp học phần</span>
        <strong>{{ tenLopDangChon }}</strong>
      </div>

      <div>
        <span>Buổi học</span>
        <strong>{{ tenBuoiHocDangChon }}</strong>
      </div>
    </section>

    <section class="summary-grid">
      <div>
        <span>Tổng bản ghi</span>
        <strong>{{ tongBanGhi }}</strong>
      </div>

      <div>
        <span>Có mặt</span>
        <strong>{{ thongKe.coMat }}</strong>
      </div>

      <div>
        <span>Vắng có phép</span>
        <strong>{{ thongKe.vangCoPhep }}</strong>
      </div>

      <div>
        <span>Vắng không phép</span>
        <strong>{{ thongKe.vangKhongPhep }}</strong>
      </div>

      <div>
        <span>Đi muộn</span>
        <strong>{{ thongKe.diMuon }}</strong>
      </div>
    </section>

    <section v-if="form.id" class="form-card">
      <h2>Cập nhật điểm danh</h2>

      <form class="form-grid" @submit.prevent="capNhatDiemDanh">
        <label>
          <span>Sinh viên</span>
          <input :value="`${form.maSinhVien} - ${form.tenSinhVien}`" type="text" disabled />
        </label>

        <label>
          <span>Trạng thái</span>
          <select v-model="form.trangThai" required>
            <option value="co_mat">Có mặt</option>
            <option value="vang_co_phep">Vắng có phép</option>
            <option value="vang_khong_phep">Vắng không phép</option>
            <option value="di_muon">Đi muộn</option>
          </select>
        </label>

        <label>
          <span>Thời gian điểm danh</span>
          <input v-model="form.thoiGianDiemDanh" type="datetime-local" />
        </label>

        <label class="field-full">
          <span>Ghi chú</span>
          <textarea v-model.trim="form.ghiChu" rows="2" />
        </label>

        <div class="form-actions">
          <button type="submit" class="btn primary">Cập nhật</button>
          <button type="button" class="btn" @click="resetForm">Hủy sửa</button>
        </div>
      </form>
    </section>

    <div v-if="thongBao" class="alert success">{{ thongBao }}</div>
    <div v-if="loi" class="alert error">{{ loi }}</div>

    <div class="pagination-bar">
      <span>Trang {{ trangHienTai + 1 }} / {{ tongTrang || 1 }}</span>

      <button type="button" class="btn" :disabled="trangHienTai <= 0 || dangTai" @click="doiTrang(trangHienTai - 1)">
        Trang trước
      </button>

      <button type="button" class="btn" :disabled="tongTrang === 0 || trangHienTai + 1 >= tongTrang || dangTai" @click="doiTrang(trangHienTai + 1)">
        Trang sau
      </button>
    </div>

    <div class="table-wrap">
      <table>
        <thead>
        <tr>
          <th>Mã SV</th>
          <th>Họ tên</th>
          <th>Lớp học phần</th>
          <th>Ngày học</th>
          <th>Trạng thái</th>
          <th>Thời gian điểm danh</th>
          <th>Ghi chú</th>
          <th>Thao tác</th>
        </tr>
        </thead>

        <tbody>
        <tr v-if="dangTai">
          <td colspan="8">Đang tải dữ liệu...</td>
        </tr>

        <tr v-else-if="!boLoc.lopHocPhanId">
          <td colspan="8">Chọn ngành, version và lớp học phần để xem điểm danh.</td>
        </tr>

        <tr v-else-if="!boLoc.lichHocId">
          <td colspan="8">Chọn buổi học của lớp để xem điểm danh.</td>
        </tr>

        <tr v-else-if="!danhSach.length">
          <td colspan="8">Chưa có dữ liệu điểm danh cho buổi học này.</td>
        </tr>

        <tr v-for="item in danhSach" v-else :key="item.id">
          <td>{{ item.maSinhVien || '-' }}</td>
          <td>{{ item.tenSinhVien || '-' }}</td>
          <td>
            <div class="cell-main">
              <strong>{{ item.maLop || '-' }}</strong>
              <span>{{ item.tenLop || '-' }}</span>
            </div>
          </td>
          <td>{{ item.ngayHoc || '-' }}</td>
          <td>
            <span :class="['status-badge', item.trangThai]">
              {{ hienThiTrangThai(item.trangThai) }}
            </span>
          </td>
          <td>{{ item.thoiGianDiemDanh || '-' }}</td>
          <td>{{ item.ghiChu || '-' }}</td>
          <td>
            <button type="button" class="btn small" @click="chonSua(item)">
              Sửa
            </button>
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
const danhSachNganh = ref([])
const danhSachChuongTrinh = ref([])
const danhSachVersion = ref([])
const danhSachLopHocPhan = ref([])
const danhSachLichHoc = ref([])

const dangTai = ref(false)
const loi = ref('')
const thongBao = ref('')
const trangHienTai = ref(0)
const tongTrang = ref(0)
const tongBanGhi = ref(0)
const kichThuocTrang = 100

const boLoc = reactive({
  nganhId: '',
  chuongTrinhVersionId: '',
  lopHocPhanId: '',
  lichHocId: '',
  keywordSinhVien: '',
  trangThai: ''
})

const form = reactive({
  id: null,
  lichHocId: '',
  sinhVienId: '',
  maSinhVien: '',
  tenSinhVien: '',
  trangThai: 'co_mat',
  thoiGianDiemDanh: '',
  ghiChu: ''
})

const thongKe = computed(() => {
  return {
    coMat: danhSach.value.filter((item) => item.trangThai === 'co_mat').length,
    vangCoPhep: danhSach.value.filter((item) => item.trangThai === 'vang_co_phep').length,
    vangKhongPhep: danhSach.value.filter((item) => item.trangThai === 'vang_khong_phep').length,
    diMuon: danhSach.value.filter((item) => item.trangThai === 'di_muon').length
  }
})

const danhSachChuongTrinhTheoNganh = computed(() => {
  if (!boLoc.nganhId) return danhSachChuongTrinh.value
  return danhSachChuongTrinh.value.filter((item) => String(item.nganhId || '') === String(boLoc.nganhId))
})

const danhSachVersionTheoNganh = computed(() => {
  const chuongTrinhIds = danhSachChuongTrinhTheoNganh.value.map((item) => Number(item.id))

  return danhSachVersion.value.filter((item) => {
    if (!boLoc.nganhId) return true
    return chuongTrinhIds.includes(Number(item.chuongTrinhId))
  })
})

const tenNganhDangChon = computed(() => {
  const item = danhSachNganh.value.find((row) => String(row.id) === String(boLoc.nganhId))
  if (!item) return 'Chưa chọn'
  return `${item.maNganh ? item.maNganh + ' - ' : ''}${item.tenNganh || item.ten || item.id}`
})

const tenVersionDangChon = computed(() => {
  const item = danhSachVersion.value.find((row) => String(row.id) === String(boLoc.chuongTrinhVersionId))
  return item ? hienThiVersion(item) : 'Chưa chọn'
})

const tenLopDangChon = computed(() => {
  const item = danhSachLopHocPhan.value.find((row) => String(row.id) === String(boLoc.lopHocPhanId))
  return item ? `${item.maLop} - ${item.tenLop}` : 'Chưa chọn'
})

const tenBuoiHocDangChon = computed(() => {
  const item = danhSachLichHoc.value.find((row) => String(row.id) === String(boLoc.lichHocId))
  return item ? `${item.ngayHoc} - ${item.noiDungBuoiHoc || 'Buổi học #' + item.id}` : 'Chưa chọn'
})

onMounted(async () => {
  await taiDuLieuNen()
})

async function taiDuLieuNen() {
  try {
    const [nganh, chuongTrinh, version] = await Promise.all([
      giangDayService.layDanhSachNganh(),
      giangDayService.layDanhSachChuongTrinh(),
      giangDayService.layDanhSachChuongTrinhVersion()
    ])

    danhSachNganh.value = nganh
    danhSachChuongTrinh.value = chuongTrinh
    danhSachVersion.value = version
  } catch (error) {
    loi.value = error?.message || 'Không tải được dữ liệu nền điểm danh'
  }
}

async function taiDanhSachLopHocPhan() {
  if (!boLoc.chuongTrinhVersionId) {
    danhSachLopHocPhan.value = []
    return
  }

  const page = await giangDayService.layTrangLopHocPhan({
    page: 0,
    size: 100,
    nganhId: Number(boLoc.nganhId),
    chuongTrinhVersionId: Number(boLoc.chuongTrinhVersionId),
    sortBy: 'maLop',
    sortDir: 'asc'
  })

  danhSachLopHocPhan.value = page.content
}

async function taiDanhSachLichHoc() {
  if (!boLoc.lopHocPhanId) {
    danhSachLichHoc.value = []
    return
  }

  const page = await giangDayService.layTrangLichHoc({
    page: 0,
    size: 100,
    lopHocPhanId: Number(boLoc.lopHocPhanId),
    sortBy: 'ngayHoc',
    sortDir: 'asc'
  })

  danhSachLichHoc.value = page.content
}

async function taiDuLieu() {
  if (!boLoc.lichHocId) {
    danhSach.value = []
    tongBanGhi.value = 0
    tongTrang.value = 0
    return
  }

  dangTai.value = true
  loi.value = ''
  thongBao.value = ''

  try {
    const page = await giangDayService.layTrangDiemDanh({
      page: trangHienTai.value,
      size: kichThuocTrang,
      lopHocPhanId: boLoc.lopHocPhanId ? Number(boLoc.lopHocPhanId) : undefined,
      lichHocId: Number(boLoc.lichHocId),
      keywordSinhVien: boLoc.keywordSinhVien || undefined,
      trangThai: boLoc.trangThai || undefined,
      sortBy: 'id',
      sortDir: 'asc'
    })

    danhSach.value = page.content
    trangHienTai.value = page.number
    tongTrang.value = page.totalPages
    tongBanGhi.value = page.totalElements
  } catch (error) {
    loi.value = error?.message || 'Không tải được danh sách điểm danh'
  } finally {
    dangTai.value = false
  }
}

async function capNhatDiemDanh() {
  const payload = {
    lichHocId: Number(form.lichHocId),
    sinhVienId: Number(form.sinhVienId),
    trangThai: form.trangThai,
    thoiGianDiemDanh: form.thoiGianDiemDanh || null,
    ghiChu: form.ghiChu || null
  }

  try {
    await giangDayService.capNhatDiemDanh(form.id, payload)
    thongBao.value = 'Cập nhật điểm danh thành công'
    resetForm()
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Cập nhật điểm danh thất bại'
  }
}

function chonSua(item) {
  form.id = item.id
  form.lichHocId = item.lichHocId || ''
  form.sinhVienId = item.sinhVienId || ''
  form.maSinhVien = item.maSinhVien || ''
  form.tenSinhVien = item.tenSinhVien || ''
  form.trangThai = item.trangThai || 'co_mat'
  form.thoiGianDiemDanh = item.thoiGianDiemDanh ? String(item.thoiGianDiemDanh).slice(0, 16) : ''
  form.ghiChu = item.ghiChu || ''
}

function resetForm() {
  form.id = null
  form.lichHocId = ''
  form.sinhVienId = ''
  form.maSinhVien = ''
  form.tenSinhVien = ''
  form.trangThai = 'co_mat'
  form.thoiGianDiemDanh = ''
  form.ghiChu = ''
}

async function doiNganh() {
  boLoc.chuongTrinhVersionId = ''
  boLoc.lopHocPhanId = ''
  boLoc.lichHocId = ''
  danhSachLopHocPhan.value = []
  danhSachLichHoc.value = []
  danhSach.value = []
}

async function doiVersion() {
  boLoc.lopHocPhanId = ''
  boLoc.lichHocId = ''
  danhSachLichHoc.value = []
  danhSach.value = []
  await taiDanhSachLopHocPhan()
}

async function doiLopHocPhan() {
  boLoc.lichHocId = ''
  danhSach.value = []
  await taiDanhSachLichHoc()
}

function locDuLieu() {
  trangHienTai.value = 0
  taiDuLieu()
}

function xoaLoc() {
  boLoc.nganhId = ''
  boLoc.chuongTrinhVersionId = ''
  boLoc.lopHocPhanId = ''
  boLoc.lichHocId = ''
  boLoc.keywordSinhVien = ''
  boLoc.trangThai = ''
  danhSachLopHocPhan.value = []
  danhSachLichHoc.value = []
  danhSach.value = []
  tongBanGhi.value = 0
  tongTrang.value = 0
  trangHienTai.value = 0
  resetForm()
}

function doiTrang(page) {
  if (page < 0) return
  if (tongTrang.value && page >= tongTrang.value) return
  trangHienTai.value = page
  taiDuLieu()
}

function hienThiVersion(item) {
  const chuongTrinh = danhSachChuongTrinh.value.find((ct) => Number(ct.id) === Number(item.chuongTrinhId))
  const tenChuongTrinh = chuongTrinh?.tenChuongTrinh || chuongTrinh?.ten || chuongTrinh?.name || `CT #${item.chuongTrinhId}`
  return `${tenChuongTrinh} - ${item.maVersion || item.tenVersion || item.ten || 'Version #' + item.id}`
}

function hienThiTrangThai(value) {
  const map = {
    co_mat: 'Có mặt',
    vang_co_phep: 'Vắng có phép',
    vang_khong_phep: 'Vắng không phép',
    di_muon: 'Đi muộn'
  }
  return map[value] || value || '-'
}
</script>

<style scoped>
.page { display: flex; flex-direction: column; gap: 16px; }
.page-head { display: flex; justify-content: space-between; gap: 16px; }
.page-head h1 { margin: 0; }
.page-head p { margin: 6px 0 0; color: #64748b; }
.flow-card, .context-card, .form-card { padding: 16px; border: 1px solid var(--color-border); border-radius: var(--radius); background: var(--color-white); }
.flow-card { display: grid; grid-template-columns: repeat(4, minmax(160px, 1fr)); gap: 12px; }
.context-card { display: grid; grid-template-columns: repeat(4, minmax(0, 1fr)); gap: 12px; }
.context-card div { display: grid; gap: 4px; }
.context-card span { color: #64748b; font-weight: 700; font-size: 13px; }
.context-card strong { color: #0f172a; }
.form-card h2 { margin: 0 0 12px; }
.form-grid { display: grid; grid-template-columns: repeat(3, minmax(160px, 1fr)); gap: 12px; }
label { display: flex; flex-direction: column; gap: 6px; }
label span { font-size: 13px; font-weight: 700; color: #334155; }
.field-full { grid-column: 1 / -1; }
input, select, textarea { border: 1px solid var(--color-border); border-radius: var(--radius); padding: 8px 10px; }
.filter-actions, .form-actions, .pagination-bar { display: flex; gap: 8px; align-items: flex-end; }
.form-actions { grid-column: 1 / -1; }
.pagination-bar { justify-content: flex-end; align-items: center; }
.btn { border: 1px solid var(--color-border); background: var(--color-white); color: var(--color-text); border-radius: var(--radius); padding: 8px 12px; cursor: pointer; font-weight: 700; }
.btn.primary { background: var(--color-primary); color: var(--color-white); border-color: var(--color-primary); }
.btn.small { padding: 6px 10px; font-size: 12px; }
.btn:disabled { opacity: 0.55; cursor: not-allowed; }
.alert { padding: 10px 12px; border-radius: var(--radius); }
.alert.success { background: #ecfdf5; color: #047857; }
.alert.error { background: #fee2e2; color: #991b1b; }
.summary-grid { display: grid; grid-template-columns: repeat(5, minmax(0, 1fr)); gap: 12px; }
.summary-grid div { border: 1px solid var(--color-border); background: var(--color-white); border-radius: var(--radius); padding: 14px; display: grid; gap: 6px; }
.summary-grid span { color: #64748b; font-weight: 700; font-size: 13px; }
.summary-grid strong { font-size: 22px; }
.cell-main { display: grid; gap: 4px; }
.cell-main span { color: #64748b; font-size: 12px; }
.status-badge { display: inline-flex; padding: 4px 8px; border-radius: 999px; font-weight: 700; font-size: 12px; background: #f1f5f9; }
.status-badge.co_mat { background: #dcfce7; color: #166534; }
.status-badge.vang_co_phep { background: #fef3c7; color: #92400e; }
.status-badge.vang_khong_phep { background: #fee2e2; color: #991b1b; }
.status-badge.di_muon { background: #dbeafe; color: #1d4ed8; }
.table-wrap { overflow: auto; border: 1px solid var(--color-border); border-radius: var(--radius); background: var(--color-white); }
table { width: 100%; border-collapse: collapse; min-width: 1100px; }
th, td { padding: 10px 12px; border-bottom: 1px solid var(--color-border); text-align: left; vertical-align: top; }
th { background: #f8fafc; color: #475569; font-size: 12px; text-transform: uppercase; }
@media (max-width: 1100px) {
  .flow-card, .context-card, .form-grid, .summary-grid { grid-template-columns: 1fr; }
}
</style>