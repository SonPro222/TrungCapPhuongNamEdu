<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Quản lý lớp học phần</h1>
        <p>Xem danh sách lớp học phần đã tạo từ module sinh viên, lọc theo ngành, version và môn học.</p>
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
            placeholder="Mã lớp, tên lớp"
            @keyup.enter="locDuLieu"
        />
      </label>

      <label>
        <span>Ngành</span>
        <select v-model="boLoc.nganhId" @change="doiNganh">
          <option value="">Tất cả ngành</option>
          <option
              v-for="item in danhSachNganh"
              :key="item.id"
              :value="item.id"
          >
            {{ item.maNganh ? item.maNganh + ' - ' : '' }}{{ item.tenNganh || item.ten || item.name || item.id }}
          </option>
        </select>
      </label>

      <label>
        <span>Version chương trình</span>
        <select v-model="boLoc.chuongTrinhVersionId" @change="doiVersion">
          <option value="">Tất cả version</option>
          <option
              v-for="item in danhSachVersionTheoNganh"
              :key="item.id"
              :value="item.id"
          >
            {{ hienThiVersion(item) }}
          </option>
        </select>
      </label>

      <label>
        <span>Môn học</span>
        <select v-model="boLoc.monHocId" @change="locDuLieu">
          <option value="">Tất cả môn học</option>
          <option
              v-for="item in danhSachMonHocTheoVersion"
              :key="item.id"
              :value="item.id"
          >
            {{ hienThiMonHoc(item) }}
          </option>
        </select>
      </label>

      <label>
        <span>Loại lớp</span>
        <select v-model="boLoc.loaiLopHocPhan" @change="locDuLieu">
          <option value="">Tất cả</option>
          <option value="CHUYEN_NGANH">Chuyên ngành</option>
          <option value="HOC_CHUNG">Học chung</option>
        </select>
      </label>

      <label>
        <span>Trạng thái</span>
        <select v-model="boLoc.trangThai" @change="locDuLieu">
          <option value="">Tất cả</option>
          <option value="du_kien">Dự kiến</option>
          <option value="dang_hoc">Đang học</option>
          <option value="da_ket_thuc">Đã kết thúc</option>
          <option value="huy">Hủy</option>
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
          <option value="id">ID</option>
          <option value="maLop">Mã lớp</option>
          <option value="tenLop">Tên lớp</option>
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
        <button type="button" class="btn primary" @click="locDuLieu">
          Lọc
        </button>

        <button type="button" class="btn" @click="xoaLoc">
          Xóa lọc
        </button>
      </div>
    </section>

    <section v-if="form.id" class="form-card">
      <h2>Cập nhật lớp học phần</h2>

      <form class="form-grid" @submit.prevent="capNhatLopHocPhan">
        <label>
          <span>Mã lớp</span>
          <input v-model.trim="form.maLop" type="text" required />
        </label>

        <label>
          <span>Tên lớp</span>
          <input v-model.trim="form.tenLop" type="text" required />
        </label>

        <label>
          <span>Loại lớp</span>
          <select v-model="form.loaiLopHocPhan" required>
            <option value="CHUYEN_NGANH">Chuyên ngành</option>
            <option value="HOC_CHUNG">Học chung</option>
          </select>
        </label>

        <label>
          <span>Chương trình môn ID</span>
          <input v-model.trim="form.chuongTrinhMonId" type="number" />
          <small>{{ tenChuongTrinhMon(form.chuongTrinhMonId) }}</small>
        </label>

        <label>
          <span>Môn học ID</span>
          <input v-model.trim="form.monHocId" type="number" />
          <small>{{ tenMonHoc(form.monHocId) }}</small>
        </label>

        <label>
          <span>Sĩ số tối thiểu</span>
          <input v-model.trim="form.siSoToiThieu" type="number" min="1" required />
        </label>

        <label>
          <span>Số lượng tối đa</span>
          <input v-model.trim="form.soLuongToiDa" type="number" min="1" required />
        </label>

        <label>
          <span>Số lượng hiện tại</span>
          <input v-model.trim="form.soLuongHienTai" type="number" min="0" />
        </label>

        <label>
          <span>Số buổi học</span>
          <input v-model.trim="form.soBuoiHoc" type="number" min="1" disabled />
          <small>Số buổi học được lấy tự động từ syllabus, không nhập tay tại lớp học phần.</small>
        </label>

        <!-- Ngày bắt đầu/kết thúc bỏ: thời gian học thuộc lịch học, tính từ MIN/MAX ngày lịch -->

        <label>
          <span>Trạng thái</span>
          <select v-model="form.trangThai" required>
            <option value="du_kien">Dự kiến</option>
            <option value="dang_hoc">Đang học</option>
            <option value="da_ket_thuc">Đã kết thúc</option>
            <option value="huy">Hủy</option>
          </select>
        </label>

        <label class="field-full">
          <span>Ghi chú</span>
          <textarea v-model.trim="form.ghiChu" rows="2" />
        </label>

        <div class="form-actions">
          <button type="submit" class="btn primary">
            Cập nhật
          </button>

          <button type="button" class="btn" @click="resetForm">
            Hủy sửa
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
          <th>Mã lớp</th>
          <th>Tên lớp</th>
          <th>Loại</th>
          <th>Chương trình môn</th>
          <th>Môn học</th>
          <th>Sĩ số</th>
          <th>Số buổi</th>
          <th>Trạng thái</th>
          <th>Ghi chú</th>
          <th>Thao tác</th>
        </tr>
        </thead>

        <tbody>
        <tr v-if="dangTai">
          <td colspan="11">Đang tải dữ liệu...</td>
        </tr>

        <tr v-else-if="!danhSach.length">
          <td colspan="11">Không có dữ liệu</td>
        </tr>

        <tr v-for="item in danhSach" v-else :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.maLop }}</td>
          <td>{{ item.tenLop }}</td>
          <td>{{ hienThiLoaiLop(item.loaiLopHocPhan) }}</td>

          <td>
            <div class="cell-main">
              <strong>{{ item.chuongTrinhMonId || '-' }}</strong>
              <span>{{ tenChuongTrinhMon(item.chuongTrinhMonId) }}</span>
            </div>
          </td>

          <td>
            <div class="cell-main">
              <strong>{{ item.monHocId || '-' }}</strong>
              <span>{{ tenMonHoc(item.monHocId) }}</span>
            </div>
          </td>

          <td>{{ item.soLuongHienTai ?? 0 }}/{{ item.soLuongToiDa ?? '-' }}</td>
          <td>{{ item.soBuoiHoc ?? '-' }}</td>
          <td>{{ hienThiTrangThai(item.trangThai) }}</td>
          <td>{{ item.ghiChu || '-' }}</td>
          <td>
            <div class="row-actions">
              <button type="button" class="btn small" @click="chonSua(item)">
                Sửa
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
import { computed, onMounted, reactive, ref } from 'vue'
import { giangDayService } from '../services/giangDayService'

const danhSach = ref([])
const danhSachNganh = ref([])
const danhSachChuongTrinh = ref([])
const danhSachVersion = ref([])
const danhSachChuongTrinhMon = ref([])
const danhSachMonHoc = ref([])

const dangTai = ref(false)
const loi = ref('')
const thongBao = ref('')
const trangHienTai = ref(0)
const tongTrang = ref(0)
const tongBanGhi = ref(0)
const kichThuocTrang = 100

const boLoc = reactive({
  keyword: '',
  nganhId: '',
  chuongTrinhVersionId: '',
  monHocId: '',
  loaiLopHocPhan: '',
  trangThai: '',
  tuNgay: '',
  denNgay: '',
  sortBy: 'id',
  sortDir: 'desc'
})

const form = reactive({
  id: null,
  maLop: '',
  tenLop: '',
  loaiLopHocPhan: 'CHUYEN_NGANH',
  chuongTrinhMonId: '',
  monHocId: '',
  siSoToiThieu: 1,
  soLuongToiDa: 40,
  soLuongHienTai: 0,
  soBuoiHoc: 1,
  // ngayBatDau/ngayKetThuc đã bỏ: thời gian học thuộc lịch học
  trangThai: 'du_kien',
  ghiChu: ''
})

const danhSachChuongTrinhTheoNganh = computed(() => {
  if (!boLoc.nganhId) {
    return danhSachChuongTrinh.value
  }

  return danhSachChuongTrinh.value.filter((item) => {
    return String(item.nganhId || '') === String(boLoc.nganhId)
  })
})

const danhSachVersionTheoNganh = computed(() => {
  const chuongTrinhIds = danhSachChuongTrinhTheoNganh.value.map((item) => Number(item.id))

  return danhSachVersion.value.filter((item) => {
    if (!boLoc.nganhId) return true
    return chuongTrinhIds.includes(Number(item.chuongTrinhId))
  })
})

const danhSachChuongTrinhMonTheoVersion = computed(() => {
  if (!boLoc.chuongTrinhVersionId) {
    return danhSachChuongTrinhMon.value
  }

  return danhSachChuongTrinhMon.value.filter((item) => {
    return String(item.chuongTrinhVersionId || '') === String(boLoc.chuongTrinhVersionId)
  })
})

const danhSachMonHocTheoVersion = computed(() => {
  const monHocIds = new Set(
      danhSachChuongTrinhMonTheoVersion.value
          .map((item) => Number(item.monHocId))
          .filter(Boolean)
  )

  if (!boLoc.chuongTrinhVersionId) {
    return danhSachMonHoc.value
  }

  return danhSachMonHoc.value.filter((item) => monHocIds.has(Number(item.id)))
})

onMounted(async () => {
  await taiDuLieuNen()
  await taiDuLieu()
})

async function taiDuLieuNen() {
  try {
    const [
      nganh,
      chuongTrinh,
      version,
      chuongTrinhMon,
      monHoc
    ] = await Promise.all([
      giangDayService.layDanhSachNganh(),
      giangDayService.layDanhSachChuongTrinh(),
      giangDayService.layDanhSachChuongTrinhVersion(),
      giangDayService.layDanhSachChuongTrinhMon(),
      giangDayService.layDanhSachMonHoc()
    ])

    danhSachNganh.value = nganh
    danhSachChuongTrinh.value = chuongTrinh
    danhSachVersion.value = version
    danhSachChuongTrinhMon.value = chuongTrinhMon
    danhSachMonHoc.value = monHoc
  } catch (error) {
    loi.value = error?.message || 'Không tải được dữ liệu nền lớp học phần'
  }
}

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''
  thongBao.value = ''

  try {
    const params = {
      page: trangHienTai.value,
      size: kichThuocTrang,
      keyword: boLoc.keyword || undefined,
      nganhId: boLoc.nganhId ? Number(boLoc.nganhId) : undefined,
      chuongTrinhVersionId: boLoc.chuongTrinhVersionId ? Number(boLoc.chuongTrinhVersionId) : undefined,
      monHocId: boLoc.monHocId ? Number(boLoc.monHocId) : undefined,
      loaiLopHocPhan: boLoc.loaiLopHocPhan || undefined,
      trangThai: boLoc.trangThai || undefined,
      tuNgay: boLoc.tuNgay || undefined,
      denNgay: boLoc.denNgay || undefined,
      sortBy: boLoc.sortBy,
      sortDir: boLoc.sortDir
    }

    const page = await giangDayService.layTrangLopHocPhan(params)

    danhSach.value = page.content
    trangHienTai.value = page.number
    tongTrang.value = page.totalPages
    tongBanGhi.value = page.totalElements
  } catch (error) {
    loi.value = error?.message || 'Không tải được danh sách lớp học phần'
  } finally {
    dangTai.value = false
  }
}

async function capNhatLopHocPhan() {
  const payload = {
    maLop: form.maLop,
    tenLop: form.tenLop,
    loaiLopHocPhan: form.loaiLopHocPhan,
    chuongTrinhMonId: form.chuongTrinhMonId === '' ? null : Number(form.chuongTrinhMonId),
    monHocId: form.monHocId === '' ? null : Number(form.monHocId),
    siSoToiThieu: Number(form.siSoToiThieu),
    soLuongToiDa: Number(form.soLuongToiDa),
    soLuongHienTai: form.soLuongHienTai === '' ? 0 : Number(form.soLuongHienTai),
    // ngayBatDau/ngayKetThuc đã bỏ: thời gian học thuộc lịch học
    trangThai: form.trangThai,
    ghiChu: form.ghiChu || null
  }

  try {
    await giangDayService.capNhatLopHocPhan(form.id, payload)
    thongBao.value = 'Cập nhật lớp học phần thành công'
    resetForm()
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Cập nhật lớp học phần thất bại'
  }
}

function chonSua(item) {
  form.id = item.id
  form.maLop = item.maLop || ''
  form.tenLop = item.tenLop || ''
  form.loaiLopHocPhan = item.loaiLopHocPhan || 'CHUYEN_NGANH'
  form.chuongTrinhMonId = item.chuongTrinhMonId || ''
  form.monHocId = item.monHocId || ''
  form.siSoToiThieu = item.siSoToiThieu ?? 1
  form.soLuongToiDa = item.soLuongToiDa ?? 40
  form.soLuongHienTai = item.soLuongHienTai ?? 0
  form.soBuoiHoc = item.soBuoiHoc ?? 1
  // ngayBatDau/ngayKetThuc đã bỏ: thời gian học thuộc lịch học
  form.trangThai = item.trangThai || 'du_kien'
  form.ghiChu = item.ghiChu || ''
}

function resetForm() {
  form.id = null
  form.maLop = ''
  form.tenLop = ''
  form.loaiLopHocPhan = 'CHUYEN_NGANH'
  form.chuongTrinhMonId = ''
  form.monHocId = ''
  form.siSoToiThieu = 1
  form.soLuongToiDa = 40
  form.soLuongHienTai = 0
  form.soBuoiHoc = 1
  // ngayBatDau/ngayKetThuc đã bỏ
  form.trangThai = 'du_kien'
  form.ghiChu = ''
}

function doiNganh() {
  boLoc.chuongTrinhVersionId = ''
  boLoc.monHocId = ''
  locDuLieu()
}

function doiVersion() {
  boLoc.monHocId = ''
  locDuLieu()
}

function locDuLieu() {
  trangHienTai.value = 0
  taiDuLieu()
}

function xoaLoc() {
  boLoc.keyword = ''
  boLoc.nganhId = ''
  boLoc.chuongTrinhVersionId = ''
  boLoc.monHocId = ''
  boLoc.loaiLopHocPhan = ''
  boLoc.trangThai = ''
  boLoc.tuNgay = ''
  boLoc.denNgay = ''
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

function hienThiVersion(item) {
  const chuongTrinh = danhSachChuongTrinh.value.find((ct) => Number(ct.id) === Number(item.chuongTrinhId))
  const tenChuongTrinh = chuongTrinh?.tenChuongTrinh || chuongTrinh?.ten || chuongTrinh?.name || `CT #${item.chuongTrinhId}`
  return `${tenChuongTrinh} - ${item.maVersion || item.tenVersion || item.ten || 'Version #' + item.id}`
}

function hienThiMonHoc(item) {
  const ma = item.maMonHoc || item.maMon || item.code
  const ten = item.tenMonHoc || item.tenMon || item.ten || item.name || item.id
  return ma ? `${ma} - ${ten}` : String(ten)
}

function tenMonHoc(monHocId) {
  if (!monHocId) return '-'
  const monHoc = danhSachMonHoc.value.find((item) => String(item.id) === String(monHocId))
  return monHoc ? hienThiMonHoc(monHoc) : 'Không tìm thấy tên môn'
}

function tenChuongTrinhMon(chuongTrinhMonId) {
  if (!chuongTrinhMonId) return '-'

  const chuongTrinhMon = danhSachChuongTrinhMon.value.find((item) => {
    return String(item.id) === String(chuongTrinhMonId)
  })

  if (!chuongTrinhMon) {
    return 'Không tìm thấy chương trình môn'
  }

  const tenMon = tenMonHoc(chuongTrinhMon.monHocId)
  const version = danhSachVersion.value.find((item) => Number(item.id) === Number(chuongTrinhMon.chuongTrinhVersionId))
  const tenVersion = version ? hienThiVersion(version) : `Version #${chuongTrinhMon.chuongTrinhVersionId}`

  return `${tenMon} / ${tenVersion}`
}

function hienThiLoaiLop(value) {
  const map = {
    CHUYEN_NGANH: 'Chuyên ngành',
    HOC_CHUNG: 'Học chung'
  }
  return map[value] || value || '-'
}

function hienThiTrangThai(value) {
  const map = {
    du_kien: 'Dự kiến',
    dang_hoc: 'Đang học',
    da_ket_thuc: 'Đã kết thúc',
    huy: 'Hủy'
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
.cell-main span { color: #64748b; font-size: 12px; line-height: 1.35; }
.table-wrap { overflow: auto; border: 1px solid var(--color-border); border-radius: var(--radius); background: var(--color-white); }
table { width: 100%; border-collapse: collapse; min-width: 1450px; }
th, td { padding: 10px 12px; border-bottom: 1px solid var(--color-border); text-align: left; vertical-align: top; }
th { background: #f8fafc; color: #475569; font-size: 12px; text-transform: uppercase; }
@media (max-width: 1100px) {
  .filter-card, .form-grid, .summary-grid { grid-template-columns: 1fr; }
  .filter-actions { align-items: stretch; }
}
</style>