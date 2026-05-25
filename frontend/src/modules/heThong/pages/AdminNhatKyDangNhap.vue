<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Nhật ký đăng nhập</h1>
        <p>Theo dõi lịch sử đăng nhập, tài khoản đăng nhập, chủ tài khoản, IP và thiết bị.</p>
      </div>

      <button type="button" class="btn" @click="taiDuLieu">
        Tải lại
      </button>
    </header>

    <section class="filter-card">
      <label>
        <span>Tìm kiếm</span>
        <input
            v-model.trim="keyword"
            type="text"
            placeholder="Email, tên tài khoản, mã/tên người dùng, IP, thiết bị, lý do"
        />
      </label>

      <label>
        <span>Trạng thái</span>
        <select v-model="thanhCong">
          <option value="">Tất cả</option>
          <option value="true">Thành công</option>
          <option value="false">Thất bại</option>
        </select>
      </label>

      <label>
        <span>Loại tài khoản</span>
        <select v-model="loaiTaiKhoan">
          <option value="">Tất cả</option>
          <option value="nhan_vien">Nhân viên</option>
          <option value="sinh_vien">Sinh viên</option>
          <option value="giao_vien">Giáo viên</option>
          <option value="admin">Admin</option>
        </select>
      </label>

      <div class="filter-actions">
        <button type="button" class="btn primary" @click="locClient">
          Lọc
        </button>

        <button type="button" class="btn" @click="xoaLoc">
          Xóa lọc
        </button>
      </div>
    </section>

    <div class="summary-grid">
      <div>
        <span>Tổng log</span>
        <strong>{{ danhSach.length }}</strong>
      </div>
      <div>
        <span>Đăng nhập thành công</span>
        <strong>{{ tongThanhCong }}</strong>
      </div>
      <div>
        <span>Đăng nhập thất bại</span>
        <strong>{{ tongThatBai }}</strong>
      </div>
      <div>
        <span>Đang hiển thị</span>
        <strong>{{ danhSachHienThi.length }}</strong>
      </div>
    </div>
    <div class="pagination-bar">
      <button
          type="button"
          class="btn"
          :disabled="trangHienTai <= 0 || dangTai"
          @click="doiTrang(trangHienTai - 1)"
      >
        Trang trước
      </button>

      <span>
    Trang {{ trangHienTai + 1 }} / {{ tongTrang || 1 }}
    - Tổng {{ tongBanGhi }} bản ghi
  </span>

      <button
          type="button"
          class="btn"
          :disabled="tongTrang === 0 || trangHienTai + 1 >= tongTrang || dangTai"
          @click="doiTrang(trangHienTai + 1)"
      >
        Trang sau
      </button>
    </div>
    <div v-if="loi" class="alert error">{{ loi }}</div>

    <div class="table-wrap">
      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>Thời gian</th>
          <th>Tài khoản ID</th>
          <th>Email nhập</th>
          <th>Email tài khoản</th>
          <th>Tên tài khoản</th>
          <th>Loại tài khoản</th>
          <th>Đối tượng</th>
          <th>Phương thức</th>
          <th>IP</th>
          <th>Thiết bị / trình duyệt</th>
          <th>Kết quả</th>
          <th>Lý do thất bại</th>
        </tr>
        </thead>

        <tbody>
        <tr v-if="dangTai">
          <td colspan="13">Đang tải dữ liệu...</td>
        </tr>

        <tr v-else-if="!danhSachHienThi.length">
          <td colspan="13">Chưa có dữ liệu</td>
        </tr>

        <tr v-for="item in danhSachHienThi" v-else :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ hienThiThoiGian(item) }}</td>
          <td>{{ item.taiKhoanId || '-' }}</td>
          <td>{{ item.emailNhap || '-' }}</td>
          <td>{{ item.taiKhoanEmail || item.email || '-' }}</td>
          <td>{{ item.tenTaiKhoan || '-' }}</td>
          <td>{{ hienThiLoaiTaiKhoan(item.loaiTaiKhoan) }}</td>
          <td>
            <div class="object-cell">
              <strong>{{ item.doiTuongTen || '-' }}</strong>
              <span>{{ item.doiTuongLoai || '-' }} {{ item.doiTuongId ? '#' + item.doiTuongId : '' }}</span>
            </div>
          </td>
          <td>{{ item.phuongThuc || '-' }}</td>
          <td>{{ item.ipAddress || '-' }}</td>
          <td class="user-agent">{{ item.userAgent || '-' }}</td>
          <td>
            <span class="status" :class="item.thanhCong === true ? 'success' : 'error'">
              {{ item.thanhCong === true ? 'Thành công' : 'Thất bại' }}
            </span>
          </td>
          <td>{{ item.lyDoThatBai || '-' }}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { heThongApi } from '../api/heThongApi'

const danhSach = ref([])
const keyword = ref('')
const thanhCong = ref('')
const loaiTaiKhoan = ref('')
const dangTai = ref(false)
const loi = ref('')
const trangHienTai = ref(0)
const tongTrang = ref(0)
const tongBanGhi = ref(0)
const kichThuocTrang = 100

const danhSachHienThi = computed(() => {
  const key = keyword.value.trim().toLowerCase()
  const loai = loaiTaiKhoan.value.trim().toLowerCase()

  return danhSach.value.filter((item) => {
    const json = JSON.stringify(item || {}).toLowerCase()

    const dungKeyword = !key || json.includes(key)

    const dungTrangThai = thanhCong.value === ''
        || String(item.thanhCong) === thanhCong.value

    const dungLoaiTaiKhoan = !loai
        || String(item.loaiTaiKhoan || '').toLowerCase() === loai

    return dungKeyword && dungTrangThai && dungLoaiTaiKhoan
  })
})

const tongThanhCong = computed(() => {
  return danhSach.value.filter(item => item.thanhCong === true).length
})

const tongThatBai = computed(() => {
  return danhSach.value.filter(item => item.thanhCong === false).length
})

onMounted(() => {
  taiDuLieu()
})

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''

  try {
    const result = await heThongApi.nhatKyDangNhap.getAll({
      page: trangHienTai.value,
      size: kichThuocTrang,
      sort: 'id,desc'
    })

    const pageData = layPage(result)
    danhSach.value = pageData.content
    trangHienTai.value = pageData.number
    tongTrang.value = pageData.totalPages
    tongBanGhi.value = pageData.totalElements
  } catch (error) {
    loi.value = error?.message || 'Không tải được nhật ký đăng nhập'
  } finally {
    dangTai.value = false
  }
}
function layPage(result) {
  const data = result?.data?.data || result?.data || result

  if (Array.isArray(data)) {
    return {
      content: data,
      number: 0,
      totalPages: 1,
      totalElements: data.length
    }
  }

  if (Array.isArray(data?.content)) {
    return {
      content: data.content,
      number: data.number || 0,
      totalPages: data.totalPages || 1,
      totalElements: data.totalElements || data.content.length
    }
  }

  return {
    content: [],
    number: 0,
    totalPages: 0,
    totalElements: 0
  }
}
function doiTrang(page) {
  if (page < 0) return
  if (tongTrang.value && page >= tongTrang.value) return

  trangHienTai.value = page
  taiDuLieu()
}
function layDanhSach(result) {
  if (Array.isArray(result)) return result
  if (Array.isArray(result?.content)) return result.content
  if (Array.isArray(result?.data?.content)) return result.data.content
  if (Array.isArray(result?.data)) return result.data
  return []
}

function hienThiThoiGian(item) {
  return item.thoiGian || item.createdAt || item.updatedAt || '-'
}

function hienThiLoaiTaiKhoan(value) {
  if (!value) return '-'

  const text = String(value)

  if (text === 'nhan_vien') return 'Nhân viên'
  if (text === 'sinh_vien') return 'Sinh viên'
  if (text === 'giao_vien') return 'Giáo viên'
  if (text === 'admin') return 'Admin'

  return text
}

function locClient() {
  trangHienTai.value = 0
  taiDuLieu()
}
function xoaLoc() {
  keyword.value = ''
  thanhCong.value = ''
  loaiTaiKhoan.value = ''
  trangHienTai.value = 0
  taiDuLieu()
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

.filter-card {
  display: grid;
  grid-template-columns: minmax(260px, 1.4fr) 180px 200px auto;
  gap: 12px;
  padding: 16px;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  background: #fff;
}

.filter-card label {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.filter-card span {
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
  font-weight: 700;
}

.btn.primary {
  background: #0f172a;
  color: #fff;
  border-color: #0f172a;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
}

.summary-grid div {
  border: 1px solid #e2e8f0;
  background: #ffffff;
  border-radius: 12px;
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
  color: #0f172a;
}

.alert {
  padding: 10px 12px;
  border-radius: 8px;
}

.alert.error {
  background: #fee2e2;
  color: #991b1b;
}

.table-wrap {
  overflow: auto;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  background: #ffffff;
}

table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1700px;
}

th,
td {
  padding: 10px 12px;
  border-bottom: 1px solid #e2e8f0;
  text-align: left;
  vertical-align: top;
}

th {
  background: #f8fafc;
  color: #475569;
  font-size: 12px;
  text-transform: uppercase;
}

.object-cell {
  display: grid;
  gap: 4px;
}

.object-cell span {
  color: #64748b;
  font-size: 12px;
}

.user-agent {
  max-width: 320px;
  white-space: normal;
  color: #475569;
  font-size: 13px;
}

.status {
  display: inline-flex;
  border-radius: 999px;
  padding: 5px 10px;
  font-size: 12px;
  font-weight: 800;
}

.status.success {
  background: #ecfdf5;
  color: #047857;
}

.status.error {
  background: #fef2f2;
  color: #b91c1c;
}

@media (max-width: 1000px) {
  .filter-card,
  .summary-grid {
    grid-template-columns: 1fr;
  }

  .filter-actions {
    align-items: stretch;
  }
}
.pagination-bar {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 12px;
  padding: 10px 0;
}

.pagination-bar span {
  color: #475569;
  font-weight: 700;
}
</style>