<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Tài khoản sinh viên</h1>
        <p>Cấp tài khoản đăng nhập bằng Gmail sinh viên cung cấp, quản lý theo ngành và version chương trình.</p>
      </div>

      <div class="head-actions">
        <button type="button" class="btn warning" @click="tuDongKhoaTaiKhoan">
          Tự động khóa theo nghiệp vụ
        </button>

        <button type="button" class="btn" @click="taiTatCa">
          Tải lại
        </button>
      </div>
    </header>

    <section class="flow-card">
      <div class="flow-step active">1. Chọn ngành</div>
      <div class="flow-step active">2. Chọn version chương trình</div>
      <div class="flow-step active">3. Cấp tài khoản Gmail</div>
    </section>

    <section class="filter-card">
      <label>
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
        <span>Trạng thái tài khoản</span>
        <select v-model="boLoc.trangThaiTaiKhoan" @change="locLaiTuDau">
          <option value="">Tất cả</option>
          <option value="chua_co_tai_khoan">Chưa có tài khoản</option>
          <option value="cho_kich_hoat">Chờ kích hoạt</option>
          <option value="da_kich_hoat">Đã kích hoạt</option>
          <option value="bi_khoa">Bị khóa</option>
        </select>
      </label>

      <label>
        <span>Số dòng/trang</span>
        <select v-model.number="size" @change="doiSize">
          <option :value="10">10 dòng</option>
          <option :value="20">20 dòng</option>
          <option :value="50">50 dòng</option>
          <option :value="100">100 dòng</option>
        </select>
      </label>

      <div class="filter-actions">
        <button type="button" class="btn primary" @click="locLaiTuDau">
          Lọc danh sách
        </button>

        <button type="button" class="btn" @click="xoaBoLoc">
          Xóa lọc
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
      <div class="table-head">
        <h2>Danh sách sinh viên theo ngành và version</h2>
        <span>Tổng {{ totalElements }} sinh viên</span>
      </div>

      <div class="table-wrap">
        <table>
          <thead>
          <tr>
            <th>Mã SV</th>
            <th>Họ tên</th>
            <th>Gmail đăng ký</th>
            <th>Ngành</th>
            <th>Chương trình</th>
            <th>Version</th>
            <th>Tài khoản</th>
            <th>Trạng thái</th>
            <th>Thao tác</th>
          </tr>
          </thead>

          <tbody>
          <tr v-if="dangTai">
            <td colspan="9">Đang tải dữ liệu...</td>
          </tr>

          <tr v-else-if="!danhSach.length">
            <td colspan="9">Chưa có dữ liệu</td>
          </tr>

          <tr v-for="sv in danhSach" v-else :key="sv.sinhVienChuongTrinhId">
            <td>{{ sv.maSinhVien || '-' }}</td>
            <td>{{ sv.hoTen || '-' }}</td>
            <td>{{ sv.emailSinhVien || '-' }}</td>
            <td>{{ hienThiGhep(sv.maNganh, sv.tenNganh) }}</td>
            <td>{{ hienThiGhep(sv.maChuongTrinh, sv.tenChuongTrinh) }}</td>
            <td>{{ hienThiGhep(sv.maVersion, sv.tenVersion) }}</td>
            <td>{{ sv.emailTaiKhoan || 'Chưa cấp' }}</td>
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
                Cấp tài khoản
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

      <div class="pagination">
        <button type="button" class="btn" :disabled="page <= 0 || dangTai" @click="doiTrang(page - 1)">
          Trước
        </button>

        <span>Trang {{ page + 1 }} / {{ totalPages || 1 }} - Tổng {{ totalElements }}</span>

        <button type="button" class="btn" :disabled="page + 1 >= totalPages || dangTai" @click="doiTrang(page + 1)">
          Sau
        </button>
      </div>
    </section>

    <div v-if="dangMoCapTaiKhoan" class="modal-backdrop">
      <form class="modal" @submit.prevent="capTaiKhoan">
        <h2>Cấp tài khoản sinh viên</h2>

        <p>
          Sinh viên:
          <strong>{{ sinhVienDangChon?.maSinhVien || 'Chưa có mã' }} - {{ sinhVienDangChon?.hoTen }}</strong>
        </p>

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
import { chuongTrinhApi } from '@/modules/chuongTrinh/api/chuongTrinhApi'
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
      chuongTrinhApi.chuongTrinh.getAll({ page: 0, size: 1000 }),
      chuongTrinhApi.chuongTrinhVersion.getAll({ page: 0, size: 1000 })
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
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-head,
.table-head,
.head-actions {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: flex-start;
}

.page-head h1,
.table-head h2 {
  margin: 0;
}

.page-head p {
  margin: 6px 0 0;
  color: #64748b;
}

.flow-card {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 8px;
}

.flow-step {
  border: 1px solid #cbd5e1;
  border-radius: 10px;
  padding: 10px;
  background: #f8fafc;
  font-weight: 600;
}

.flow-step.active {
  background: #0f172a;
  color: #fff;
  border-color: #0f172a;
}

.filter-card {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
  padding: 16px;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  background: #fff;
}

.filter-card label,
.modal label {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.filter-card span,
.modal span {
  font-size: 13px;
  font-weight: 700;
  color: #334155;
}

input,
select {
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  padding: 8px 10px;
}

.filter-actions {
  display: flex;
  align-items: flex-end;
  gap: 8px;
}

.btn {
  border: 1px solid #cbd5e1;
  background: #fff;
  color: #0f172a;
  border-radius: 8px;
  padding: 8px 12px;
  cursor: pointer;
}

.btn:disabled {
  cursor: not-allowed;
  opacity: 0.55;
}

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

.table-card {
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 16px;
}

.table-wrap {
  overflow: auto;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
}

table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1200px;
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
  font-weight: 700;
}

.actions {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.badge {
  display: inline-flex;
  padding: 4px 8px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 700;
  background: #e2e8f0;
}

.badge.da_kich_hoat {
  background: #dcfce7;
  color: #166534;
}

.badge.cho_kich_hoat {
  background: #fef9c3;
  color: #854d0e;
}

.badge.bi_khoa {
  background: #fee2e2;
  color: #991b1b;
}

.badge.chua_co_tai_khoan {
  background: #e0f2fe;
  color: #075985;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 8px;
  margin-top: 12px;
}

.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(15, 23, 42, 0.55);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 24px;
  z-index: 50;
}

.modal {
  width: 460px;
  max-width: 100%;
  display: flex;
  flex-direction: column;
  gap: 12px;
  background: #fff;
  border-radius: 12px;
  padding: 20px;
}

.modal h2 {
  margin: 0;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}
</style>