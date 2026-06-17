<template>
  <section class="page">
    <header class="page-head">
      <div class="page-title">
        <h1>Tài khoản sinh viên</h1>
        <p>Cấp tài khoản đăng nhập bằng Gmail sinh viên cung cấp, quản lý theo ngành và version chương trình.</p>
      </div>

      <div class="head-actions">
        <button type="button" class="btn compact warning" @click="tuDongKhoaTaiKhoan">
          Tự động khóa
        </button>

        <button type="button" class="btn compact" @click="taiTatCa">
          Tải lại
        </button>
      </div>
    </header>

    <section class="flow-card">
      <div class="flow-step active">
        <span>1</span>
        <strong>Chọn ngành</strong>
      </div>

      <div class="flow-step active">
        <span>2</span>
        <strong>Chọn version</strong>
      </div>

      <div class="flow-step active">
        <span>3</span>
        <strong>Cấp Gmail</strong>
      </div>
    </section>

    <section class="filter-card">
      <label class="search-field">
        <span>Tìm kiếm</span>
        <input
            v-model="boLoc.keyword"
            type="text"
            placeholder="Mã SV, họ tên, email"
            @keyup.enter="locLaiTuDau"
        />
      </label>

      <label>
        <span>Ngành</span>
        <select v-model="boLoc.nganhId" @change="doiNganh">
          <option value="">Tất cả ngành</option>
          <option v-for="item in nganhList" :key="item.id" :value="item.id">
            {{ item.maNganh }} - {{ item.tenNganh }}
          </option>
        </select>
      </label>

      <label>
        <span>Chương trình</span>
        <select v-model="boLoc.chuongTrinhId" @change="doiChuongTrinh">
          <option value="">Tất cả chương trình</option>
          <option v-for="item in chuongTrinhTheoNganh" :key="item.id" :value="item.id">
            {{ item.maChuongTrinh }} - {{ item.tenChuongTrinh }}
          </option>
        </select>
      </label>

      <label>
        <span>Version</span>
        <select v-model="boLoc.chuongTrinhVersionId" @change="locLaiTuDau">
          <option value="">Tất cả version</option>
          <option v-for="item in versionTheoChuongTrinh" :key="item.id" :value="item.id">
            {{ item.maVersion }} - {{ item.tenVersion }}
          </option>
        </select>
      </label>

      <label>
        <span>Trạng thái</span>
        <select v-model="boLoc.trangThaiTaiKhoan" @change="locLaiTuDau">
          <option value="">Tất cả</option>
          <option value="chua_co_tai_khoan">Chưa có tài khoản</option>
          <option value="cho_kich_hoat">Chờ kích hoạt</option>
          <option value="da_kich_hoat">Đã kích hoạt</option>
          <option value="bi_khoa">Bị khóa</option>
        </select>
      </label>

      <label>
        <span>Số dòng</span>
        <select v-model.number="size" @change="doiSize">
          <option :value="10">10 dòng</option>
          <option :value="20">20 dòng</option>
          <option :value="50">50 dòng</option>
          <option :value="100">100 dòng</option>
        </select>
      </label>

      <div class="filter-actions">
        <button type="button" class="btn primary compact" @click="locLaiTuDau">
          Lọc
        </button>

        <button type="button" class="btn compact" @click="xoaBoLoc">
          Xóa
        </button>
      </div>
    </section>

    <div v-if="thongBao" class="alert success">
      {{ thongBao }}
    </div>

    <div v-if="loi" class="alert error">
      {{ loi }}
    </div>

    <section class="table-card">
      <div class="table-toolbar">
        <div class="table-title">
          <strong>Danh sách sinh viên theo ngành và version</strong>
          <span>Hiển thị {{ danhSach.length }} / {{ totalElements }} sinh viên</span>
        </div>

        <div class="pagination compact-pagination">
          <button type="button" class="btn compact" :disabled="page <= 0 || dangTai" @click="doiTrang(page - 1)">
            Trước
          </button>

          <span>Trang {{ page + 1 }} / {{ totalPages || 1 }}</span>

          <button type="button" class="btn compact" :disabled="page + 1 >= totalPages || dangTai" @click="doiTrang(page + 1)">
            Sau
          </button>
        </div>
      </div>

      <div class="table-wrap">
        <table>
          <thead>
          <tr>
            <th class="col-code">Mã SV</th>
            <th class="col-name">Họ tên</th>
            <th class="col-email">Gmail đăng ký</th>
            <th class="col-major">Ngành</th>
            <th class="col-program">Chương trình</th>
            <th class="col-version">Version</th>
            <th class="col-account">Tài khoản</th>
            <th class="col-status">Trạng thái</th>
            <th class="col-actions">Thao tác</th>
          </tr>
          </thead>

          <tbody>
          <tr v-if="dangTai">
            <td colspan="9" class="empty-cell">
              Đang tải dữ liệu...
            </td>
          </tr>

          <tr v-else-if="!danhSach.length">
            <td colspan="9" class="empty-cell">
              Chưa có dữ liệu
            </td>
          </tr>

          <tr v-for="sv in danhSach" v-else :key="sv.sinhVienChuongTrinhId">
            <td class="mono">
              {{ sv.maSinhVien || '-' }}
            </td>

            <td>
              <div class="student-cell">
                <strong>{{ sv.hoTen || '-' }}</strong>
              </div>
            </td>

            <td>
              <span class="email-text">
                {{ sv.emailSinhVien || '-' }}
              </span>
            </td>

            <td>
              <div class="stacked-cell">
                <strong>{{ sv.maNganh || '-' }}</strong>
                <span>{{ sv.tenNganh || '-' }}</span>
              </div>
            </td>

            <td>
              <div class="stacked-cell">
                <strong>{{ sv.maChuongTrinh || '-' }}</strong>
                <span>{{ sv.tenChuongTrinh || '-' }}</span>
              </div>
            </td>

            <td>
              <div class="stacked-cell">
                <strong>{{ sv.maVersion || '-' }}</strong>
                <span>{{ sv.tenVersion || '-' }}</span>
              </div>
            </td>

            <td>
              <span v-if="sv.emailTaiKhoan" class="account-text">
                {{ sv.emailTaiKhoan }}
              </span>
              <span v-else class="empty-inline">
                Chưa cấp
              </span>
            </td>

            <td>
              <span class="badge" :class="sv.trangThaiTaiKhoan">
                {{ hienThiTrangThaiTaiKhoan(sv.trangThaiTaiKhoan) }}
              </span>
            </td>

            <td class="actions">
              <button
                  v-if="!sv.taiKhoanId"
                  type="button"
                  class="btn small primary"
                  @click="moCapTaiKhoan(sv)"
              >
                Cấp
              </button>

              <button
                  v-if="sv.taiKhoanId && sv.trangThaiTaiKhoan !== 'bi_khoa'"
                  type="button"
                  class="btn small danger"
                  @click="khoaTaiKhoan(sv)"
              >
                Khóa
              </button>

              <button
                  v-if="sv.taiKhoanId && sv.trangThaiTaiKhoan === 'bi_khoa'"
                  type="button"
                  class="btn small primary"
                  @click="moKhoaTaiKhoan(sv)"
              >
                Mở khóa
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </section>

    <div v-if="dangMoCapTaiKhoan" class="modal-backdrop">
      <form class="modal" @submit.prevent="capTaiKhoan">
        <div class="modal-head">
          <div>
            <h2>Cấp tài khoản sinh viên</h2>
            <p>
              {{ sinhVienDangChon?.maSinhVien || 'Chưa có mã' }}
              -
              {{ sinhVienDangChon?.hoTen }}
            </p>
          </div>

          <button type="button" class="modal-close" @click="dongCapTaiKhoan">
            ×
          </button>
        </div>

        <label>
          <span>Gmail đăng nhập</span>
          <input v-model="formCap.email" type="email" required />
        </label>

        <label>
          <span>Mật khẩu ban đầu</span>
          <input v-model="formCap.matKhau" type="password" required minlength="6" />
        </label>

        <label>
          <span>Trạng thái</span>
          <select v-model="formCap.trangThai">
            <option value="da_kich_hoat">Đã kích hoạt</option>
            <option value="cho_kich_hoat">Chờ kích hoạt</option>
          </select>
        </label>

        <div class="modal-actions">
          <button type="submit" class="btn primary">
            Cấp tài khoản
          </button>

          <button type="button" class="btn" @click="dongCapTaiKhoan">
            Hủy
          </button>
        </div>
      </form>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { daoTaoApi } from '@/modules/daoTao/api/daoTaoApi'
import { heThongApi } from '../api/heThongApi'

const nganhList = ref([])
const chuongTrinhList = ref([])
const versionList = ref([])
const danhSach = ref([])

const page = ref(0)
const size = ref(20)
const totalPages = ref(0)
const totalElements = ref(0)

const dangTai = ref(false)
const loi = ref('')
const thongBao = ref('')

const dangMoCapTaiKhoan = ref(false)
const sinhVienDangChon = ref(null)

const boLoc = reactive({
  keyword: '',
  nganhId: '',
  chuongTrinhId: '',
  chuongTrinhVersionId: '',
  trangThaiTaiKhoan: ''
})

const formCap = reactive({
  email: '',
  matKhau: '',
  trangThai: 'da_kich_hoat'
})

const chuongTrinhTheoNganh = computed(() => {
  if (!boLoc.nganhId) {
    return chuongTrinhList.value
  }

  return chuongTrinhList.value.filter((item) => Number(item.nganhId) === Number(boLoc.nganhId))
})

const versionTheoChuongTrinh = computed(() => {
  if (!boLoc.chuongTrinhId) {
    return versionList.value
  }

  return versionList.value.filter((item) => Number(item.chuongTrinhId) === Number(boLoc.chuongTrinhId))
})

onMounted(() => {
  taiTatCa()
})

async function taiTatCa() {
  loi.value = ''
  thongBao.value = ''
  page.value = 0

  try {
    const [
      nganhResult,
      chuongTrinhResult,
      versionResult
    ] = await Promise.all([
      daoTaoApi.nganh.getAll({ page: 0, size: 1000 }),
      daoTaoApi.chuongTrinh.getAll({ page: 0, size: 1000 }),
      daoTaoApi.chuongTrinhVersion.getAll({ page: 0, size: 1000 })
    ])

    nganhList.value = layDanhSach(nganhResult)
    chuongTrinhList.value = layDanhSach(chuongTrinhResult)
    versionList.value = layDanhSach(versionResult)

    await taiDanhSach()
  } catch (error) {
    loi.value = error?.message || 'Không tải được dữ liệu hệ thống'
  }
}

async function taiDanhSach() {
  dangTai.value = true
  loi.value = ''
  thongBao.value = ''

  try {
    const result = await heThongApi.taiKhoanSinhVien.getAll({
      keyword: boLoc.keyword || undefined,
      nganhId: boLoc.nganhId || undefined,
      chuongTrinhVersionId: boLoc.chuongTrinhVersionId || undefined,
      trangThaiTaiKhoan: boLoc.trangThaiTaiKhoan || undefined,
      page: page.value,
      size: size.value
    })

    danhSach.value = layDanhSach(result)
    totalPages.value = layTotalPages(result)
    totalElements.value = layTotalElements(result)
  } catch (error) {
    loi.value = error?.message || 'Không tải được danh sách tài khoản sinh viên'
  } finally {
    dangTai.value = false
  }
}

function layDanhSach(result) {
  if (Array.isArray(result)) {
    return result
  }

  if (Array.isArray(result?.content)) {
    return result.content
  }

  if (Array.isArray(result?.data?.content)) {
    return result.data.content
  }

  if (Array.isArray(result?.data)) {
    return result.data
  }

  return []
}

function layTotalPages(result) {
  return result?.totalPages || result?.data?.totalPages || 0
}

function layTotalElements(result) {
  return result?.totalElements || result?.data?.totalElements || layDanhSach(result).length
}

function locLaiTuDau() {
  page.value = 0
  taiDanhSach()
}

function doiTrang(newPage) {
  if (newPage < 0 || newPage >= totalPages.value) {
    return
  }

  page.value = newPage
  taiDanhSach()
}

function doiSize() {
  page.value = 0
  taiDanhSach()
}

function doiNganh() {
  boLoc.chuongTrinhId = ''
  boLoc.chuongTrinhVersionId = ''
  locLaiTuDau()
}

function doiChuongTrinh() {
  boLoc.chuongTrinhVersionId = ''
  locLaiTuDau()
}

function xoaBoLoc() {
  boLoc.keyword = ''
  boLoc.nganhId = ''
  boLoc.chuongTrinhId = ''
  boLoc.chuongTrinhVersionId = ''
  boLoc.trangThaiTaiKhoan = ''
  page.value = 0
  taiDanhSach()
}

function moCapTaiKhoan(sinhVien) {
  sinhVienDangChon.value = sinhVien
  formCap.email = sinhVien.emailSinhVien || ''
  formCap.matKhau = ''
  formCap.trangThai = 'da_kich_hoat'
  dangMoCapTaiKhoan.value = true
}

function dongCapTaiKhoan() {
  dangMoCapTaiKhoan.value = false
  sinhVienDangChon.value = null
  formCap.email = ''
  formCap.matKhau = ''
  formCap.trangThai = 'da_kich_hoat'
}

async function capTaiKhoan() {
  if (!sinhVienDangChon.value) {
    return
  }

  loi.value = ''
  thongBao.value = ''

  try {
    await heThongApi.taiKhoanSinhVien.capTaiKhoan(sinhVienDangChon.value.sinhVienId, {
      email: formCap.email,
      matKhau: formCap.matKhau,
      trangThai: formCap.trangThai
    })

    thongBao.value = 'Cấp tài khoản sinh viên thành công'
    dongCapTaiKhoan()
    await taiDanhSach()
  } catch (error) {
    loi.value = error?.message || 'Cấp tài khoản sinh viên thất bại'
  }
}

async function khoaTaiKhoan(sinhVien) {
  const dongY = window.confirm(`Khóa tài khoản sinh viên ${sinhVien.maSinhVien || ''} - ${sinhVien.hoTen}?`)

  if (!dongY) {
    return
  }

  loi.value = ''
  thongBao.value = ''

  try {
    await heThongApi.taiKhoanSinhVien.khoaTaiKhoan(sinhVien.sinhVienId)
    thongBao.value = 'Khóa tài khoản thành công'
    await taiDanhSach()
  } catch (error) {
    loi.value = error?.message || 'Khóa tài khoản thất bại'
  }
}

async function moKhoaTaiKhoan(sinhVien) {
  loi.value = ''
  thongBao.value = ''

  try {
    await heThongApi.taiKhoanSinhVien.moKhoaTaiKhoan(sinhVien.sinhVienId)
    thongBao.value = 'Mở khóa tài khoản thành công'
    await taiDanhSach()
  } catch (error) {
    loi.value = error?.message || 'Mở khóa tài khoản thất bại'
  }
}


async function tuDongKhoaTaiKhoan() {
  const dongY = window.confirm('Tự động khóa các tài khoản sinh viên đã tốt nghiệp, nghỉ/bỏ học, hoàn thành khóa học hoặc version chương trình đã hết hạn?')

  if (!dongY) {
    return
  }

  loi.value = ''
  thongBao.value = ''

  try {
    const result = await heThongApi.taiKhoanSinhVien.tuDongKhoa()
    const soLuong = result?.data ?? result ?? 0
    thongBao.value = `Đã tự động khóa ${soLuong} tài khoản sinh viên theo nghiệp vụ`
    await taiDanhSach()
  } catch (error) {
    loi.value = error?.message || 'Tự động khóa tài khoản sinh viên thất bại'
  }
}

function hienThiTrangThaiTaiKhoan(value) {
  const map = {
    chua_co_tai_khoan: 'Chưa có tài khoản',
    cho_kich_hoat: 'Chờ kích hoạt',
    da_kich_hoat: 'Đã kích hoạt',
    bi_khoa: 'Bị khóa'
  }

  return map[value] || value || '-'
}

function hienThiGhep(ma, ten) {
  if (ma && ten) {
    return `${ma} - ${ten}`
  }

  return ma || ten || '-'
}
</script>

<style scoped>
.page {
  --primary: #077149;
  --primary-hover: #055436;
  --primary-light: #e6f1ec;
  --primary-soft: #f3faf7;
  --primary-border: #bfe4d2;
  --primary-glow: rgba(7, 113, 73, 0.14);

  --danger: #dc2626;
  --danger-light: #fef2f2;
  --danger-border: #fecaca;

  --warning: #d97706;
  --warning-light: #fff7ed;
  --warning-border: #fed7aa;

  --info-light: #e0f2fe;
  --info-text: #075985;
  --info-border: #bae6fd;

  --text-main: #111827;
  --text-normal: #334155;
  --text-muted: #64748b;

  --bg-card: #ffffff;
  --bg-soft: #f8fafc;

  --border-color: #e2e8f0;
  --border-strong: #cbd5e1;

  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 100%;
  color: var(--text-main);
  font-family: Roboto, system-ui, -apple-system, BlinkMacSystemFont, sans-serif;
}

/* Header */
.page-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  padding: 10px 12px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-top: 3px solid var(--primary);
  border-radius: 10px;
}

.page-title h1 {
  margin: 0;
  color: var(--primary);
  font-size: 21px;
  font-weight: 800;
  letter-spacing: -0.02em;
}

.page-title p {
  margin: 2px 0 0;
  color: var(--text-muted);
  font-size: 12.5px;
}

.head-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* Flow */
.flow-card {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 8px;
}

.flow-step {
  display: flex;
  align-items: center;
  gap: 8px;
  min-height: 34px;
  padding: 7px 10px;
  border: 1px solid var(--primary-border);
  border-radius: 9px;
  background: var(--primary-soft);
  color: var(--primary);
  font-size: 12.5px;
  font-weight: 700;
}

.flow-step span {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 21px;
  height: 21px;
  border-radius: 999px;
  background: var(--primary);
  color: #ffffff;
  font-size: 11px;
}

.flow-step strong {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* Filter */
.filter-card {
  display: grid;
  grid-template-columns:
    minmax(220px, 1.2fr)
    minmax(190px, 1fr)
    minmax(200px, 1fr)
    minmax(190px, 1fr)
    minmax(150px, 0.75fr)
    minmax(120px, 0.55fr)
    auto;
  gap: 8px;
  align-items: end;
  padding: 10px 12px;
  border: 1px solid var(--border-color);
  border-radius: 10px;
  background: #ffffff;
}

.filter-card label,
.modal label {
  display: flex;
  flex-direction: column;
  gap: 4px;
  min-width: 0;
}

.filter-card span,
.modal span {
  color: var(--text-normal);
  font-size: 11.5px;
  font-weight: 700;
}

.search-field {
  min-width: 0;
}

.filter-actions {
  display: flex;
  align-items: end;
  gap: 6px;
}

/* Input */
input,
select {
  width: 100%;
  min-height: 31px;
  box-sizing: border-box;
  border: 1px solid var(--border-strong);
  border-radius: 7px;
  padding: 6px 9px;
  background: #ffffff;
  color: var(--text-main);
  outline: none;
  font-family: inherit;
  font-size: 12.5px;
  transition: border-color 0.16s ease, box-shadow 0.16s ease;
}

input:hover,
select:hover {
  border-color: #94a3b8;
}

input:focus,
select:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 3px var(--primary-glow);
}

/* Button */
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 31px;
  border: 1px solid var(--border-strong);
  border-radius: 7px;
  padding: 6px 10px;
  background: #ffffff;
  color: var(--text-normal);
  cursor: pointer;
  font-family: inherit;
  font-size: 12.5px;
  font-weight: 700;
  line-height: 1;
  white-space: nowrap;
  transition: all 0.16s ease;
}

.btn:hover:not(:disabled) {
  background: var(--bg-soft);
  border-color: #94a3b8;
  color: var(--text-main);
}

.btn:disabled {
  cursor: not-allowed;
  opacity: 0.55;
}

.btn.primary {
  background: var(--primary);
  border-color: var(--primary);
  color: #ffffff;
}

.btn.primary:hover:not(:disabled) {
  background: var(--primary-hover);
  border-color: var(--primary-hover);
}

.btn.warning {
  color: var(--warning);
  background: var(--warning-light);
  border-color: var(--warning-border);
}

.btn.warning:hover:not(:disabled) {
  color: #ffffff;
  background: var(--warning);
  border-color: var(--warning);
}

.btn.compact {
  min-height: 29px;
  padding: 5px 9px;
  font-size: 12px;
}

.btn.small {
  min-height: 28px;
  padding: 5px 8px;
  font-size: 12px;
}

/* Alert */
.alert {
  padding: 8px 11px;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
}

.alert.success {
  background: #ecfdf5;
  color: #065f46;
  border: 1px solid #a7f3d0;
}

.alert.error {
  background: var(--danger-light);
  color: #991b1b;
  border: 1px solid var(--danger-border);
}

/* Table */
.table-card {
  overflow: hidden;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 10px;
}

.table-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  padding: 8px 10px;
  background: #ffffff;
  border-bottom: 1px solid var(--border-color);
}

.table-title {
  display: flex;
  flex-direction: column;
  gap: 1px;
}

.table-title strong {
  color: var(--text-main);
  font-size: 14px;
  font-weight: 800;
}

.table-title span {
  color: var(--text-muted);
  font-size: 12px;
}

.table-wrap {
  max-height: calc(100vh - 245px);
  overflow: auto;
}

table {
  width: 100%;
  min-width: 1280px;
  border-collapse: collapse;
}

thead th {
  position: sticky;
  top: 0;
  z-index: 2;
}

th,
td {
  border-bottom: 1px solid #eef2f7;
  padding: 7px 9px;
  text-align: left;
  vertical-align: middle;
  color: var(--text-normal);
  font-size: 12.5px;
}

th {
  background: #f8fafc;
  color: var(--text-muted);
  font-size: 11px;
  font-weight: 800;
  letter-spacing: 0.035em;
  text-transform: uppercase;
  white-space: nowrap;
}

tbody tr:hover {
  background: var(--bg-soft);
}

.col-code {
  width: 90px;
}

.col-name {
  width: 150px;
}

.col-email {
  width: 190px;
}

.col-major {
  width: 220px;
}

.col-program {
  width: 260px;
}

.col-version {
  width: 260px;
}

.col-account {
  width: 190px;
}

.col-status {
  width: 140px;
}

.col-actions {
  width: 105px;
  text-align: right;
}

.empty-cell {
  padding: 22px 12px !important;
  text-align: center;
  color: var(--text-muted);
}

.mono {
  color: var(--text-main);
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 12px;
  font-weight: 700;
  white-space: nowrap;
}

.student-cell strong {
  display: block;
  color: var(--text-main);
  font-weight: 700;
  line-height: 1.25;
}

.email-text,
.account-text {
  color: var(--text-main);
  font-size: 12.5px;
  white-space: nowrap;
}

.stacked-cell {
  display: flex;
  flex-direction: column;
  gap: 1px;
  min-width: 0;
}

.stacked-cell strong {
  color: var(--text-main);
  font-size: 12px;
  font-weight: 800;
  line-height: 1.25;
}

.stacked-cell span {
  display: -webkit-box;
  max-width: 100%;
  overflow: hidden;
  color: var(--text-muted);
  font-size: 11.5px;
  line-height: 1.3;
  text-overflow: ellipsis;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.empty-inline {
  color: #94a3b8;
  font-size: 12px;
  font-style: italic;
}

/* Badge */
.badge {
  display: inline-flex;
  align-items: center;
  padding: 3px 8px;
  border-radius: 999px;
  font-size: 11.5px;
  font-weight: 800;
  background: #f1f5f9;
  color: #475467;
  white-space: nowrap;
}

.badge.da_kich_hoat {
  background: var(--primary-light);
  color: var(--primary);
  border: 1px solid var(--primary-border);
}

.badge.cho_kich_hoat {
  background: #fef9c3;
  color: #854d0e;
  border: 1px solid #fde68a;
}

.badge.bi_khoa {
  background: var(--danger-light);
  color: #991b1b;
  border: 1px solid var(--danger-border);
}

.badge.chua_co_tai_khoan {
  background: var(--info-light);
  color: var(--info-text);
  border: 1px solid var(--info-border);
}

.actions {
  display: flex;
  justify-content: flex-end;
  gap: 5px;
  flex-wrap: nowrap;
}

/* Pagination */
.pagination {
  display: flex;
  align-items: center;
  gap: 6px;
}

.pagination span {
  color: var(--text-muted);
  font-size: 12px;
  font-weight: 600;
  white-space: nowrap;
}

.compact-pagination {
  margin: 0;
}

/* Modal */
.modal-backdrop {
  position: fixed;
  inset: 0;
  z-index: 50;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  background: rgba(15, 23, 42, 0.55);
  backdrop-filter: blur(2px);
}

.modal {
  width: 430px;
  max-width: 100%;
  display: flex;
  flex-direction: column;
  gap: 11px;
  padding: 16px;
  border-radius: 12px;
  background: #ffffff;
  box-shadow: 0 18px 35px rgba(15, 23, 42, 0.18);
}

.modal-head {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 12px;
  padding-bottom: 10px;
  border-bottom: 1px solid var(--border-color);
}

.modal-head h2 {
  margin: 0;
  color: var(--primary);
  font-size: 17px;
  font-weight: 800;
}

.modal-head p {
  margin: 3px 0 0;
  color: var(--text-muted);
  font-size: 12.5px;
}

.modal-close {
  width: 28px;
  height: 28px;
  border: none;
  border-radius: 7px;
  background: #f1f5f9;
  color: var(--text-muted);
  cursor: pointer;
  font-size: 20px;
  line-height: 1;
}

.modal-close:hover {
  background: var(--danger-light);
  color: var(--danger);
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  padding-top: 4px;
}

/* Responsive */
@media (max-width: 1280px) {
  .page-head,
  .table-toolbar {
    align-items: flex-start;
    flex-direction: column;
  }

  .head-actions,
  .pagination {
    width: 100%;
    justify-content: flex-start;
  }

  .filter-card {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }

  .filter-actions {
    grid-column: 1 / -1;
  }

  .table-wrap {
    max-height: calc(100vh - 320px);
  }
}

@media (max-width: 768px) {
  .flow-card,
  .filter-card {
    grid-template-columns: 1fr;
  }

  .head-actions,
  .filter-actions,
  .pagination,
  .modal-actions {
    align-items: stretch;
    flex-direction: column;
  }

  .head-actions .btn,
  .filter-actions .btn,
  .pagination .btn,
  .modal-actions .btn {
    width: 100%;
  }

  .table-wrap {
    max-height: none;
  }

  thead th {
    position: static;
  }
}
</style>
