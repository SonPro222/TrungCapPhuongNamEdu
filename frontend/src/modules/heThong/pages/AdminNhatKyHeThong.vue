<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Nhật ký hệ thống</h1>
        <p>Theo dõi thao tác tạo, cập nhật, xóa trên toàn hệ thống theo từng tầng nghiệp vụ.</p>
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
            placeholder="Tài khoản, người thao tác, đường dẫn, IP, nội dung"
        />
      </label>

      <label>
        <span>Module nghiệp vụ</span>
        <select v-model="moduleNghiepVu">
          <option value="">Tất cả</option>
          <option value="SINH_VIEN">Sinh viên</option>
          <option value="DAO_TAO">Đào tạo</option>
          <option value="CHUONG_TRINH">Chương trình</option>
          <option value="GIANG_DAY">Giảng dạy</option>
          <option value="DIEM">Điểm</option>
          <option value="HOC_PHI">Học phí</option>
          <option value="THI">Thi</option>
          <option value="LMS">LMS</option>
          <option value="HE_THONG">Hệ thống</option>
          <option value="KHAC">Khác</option>
        </select>
      </label>

      <label>
        <span>Hành động</span>
        <select v-model="hanhDong">
          <option value="">Tất cả</option>
          <option value="TAO_MOI">Tạo mới</option>
          <option value="CAP_NHAT">Cập nhật</option>
          <option value="XOA">Xóa</option>
        </select>
      </label>

      <label>
        <span>Kết quả</span>
        <select v-model="ketQua">
          <option value="">Tất cả</option>
          <option value="THANH_CONG">Thành công</option>
          <option value="THAT_BAI">Thất bại</option>
        </select>
      </label>
      <label>
        <span>Từ ngày</span>
        <input v-model="tuNgay" type="date" />
      </label>

      <label>
        <span>Đến ngày</span>
        <input v-model="denNgay" type="date" />
      </label>

      <label>
        <span>Tài khoản / Gmail</span>
        <input
            v-model.trim="taiKhoanEmail"
            type="text"
            placeholder="admin@gmail.com"
        />
      </label>

      <label>
        <span>Bản ghi ID</span>
        <input
            v-model.trim="banGhiId"
            type="number"
            placeholder="ID bản ghi"
        />
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
        <span>Tổng thao tác</span>
        <strong>{{ danhSach.length }}</strong>
      </div>
      <div>
        <span>Thành công</span>
        <strong>{{ tongThanhCong }}</strong>
      </div>
      <div>
        <span>Thất bại</span>
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
          <th>Tài khoản</th>
          <th>Đối tượng thao tác</th>
          <th>Module</th>
          <th>Chức năng</th>
          <th>Hành động</th>
          <th>Kết quả</th>
          <th>Phương thức</th>
          <th>Đường dẫn</th>
          <th>Bảng tác động</th>
          <th>Bản ghi ID</th>
          <th>IP</th>
          <th>Thiết bị / trình duyệt</th>
          <th>Mô tả</th>
          <th>Nội dung cũ</th>
          <th>Nội dung mới</th>
        </tr>
        </thead>

        <tbody>
        <tr v-if="dangTai">
          <td colspan="17">Đang tải dữ liệu...</td>
        </tr>

        <tr v-else-if="!danhSachHienThi.length">
          <td colspan="17">Chưa có dữ liệu</td>
        </tr>

        <tr v-for="item in danhSachHienThi" v-else :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.createdAt || item.thoiGian || '-' }}</td>
          <td>
            <div class="object-cell">
              <strong>{{ item.taiKhoanEmail || item.taiKhoanId || '-' }}</strong>
              <span>ID: {{ item.taiKhoanId || '-' }}</span>
              <span>{{ item.tenTaiKhoan || '-' }}</span>
              <span>{{ hienThiLoaiTaiKhoan(item.loaiTaiKhoan) }}</span>
            </div>
          </td>
          <td>
            <div class="object-cell">
              <strong>{{ item.doiTuongTen || '-' }}</strong>
              <span>{{ item.doiTuongLoai || '-' }} {{ item.doiTuongId ? '#' + item.doiTuongId : '' }}</span>
            </div>
          </td>
          <td>{{ item.moduleNghiepVu || '-' }}</td>
          <td>{{ item.chucNang || '-' }}</td>
          <td>
            <span class="action" :class="item.hanhDong">
              {{ hienThiHanhDong(item.hanhDong) }}
            </span>
          </td>
          <td>
            <span class="status" :class="item.ketQua === 'THANH_CONG' ? 'success' : 'error'">
              {{ hienThiKetQua(item.ketQua) }}
            </span>
          </td>
          <td>{{ item.phuongThuc || '-' }}</td>
          <td class="path-cell">{{ item.duongDan || '-' }}</td>
          <td>{{ item.bangTacDong || '-' }}</td>
          <td>{{ item.banGhiId || '-' }}</td>
          <td>{{ item.ipAddress || '-' }}</td>
          <td class="user-agent">{{ item.userAgent || '-' }}</td>
          <td class="desc-cell">{{ item.moTa || '-' }}</td>
          <td><pre>{{ item.noiDungCu || '-' }}</pre></td>
          <td><pre>{{ item.noiDungMoi || '-' }}</pre></td>
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
const moduleNghiepVu = ref('')
const hanhDong = ref('')
const ketQua = ref('')
const tuNgay = ref('')
const denNgay = ref('')
const taiKhoanEmail = ref('')
const banGhiId = ref('')
const dangTai = ref(false)
const loi = ref('')
const trangHienTai = ref(0)
const tongTrang = ref(0)
const tongBanGhi = ref(0)
const kichThuocTrang = 100
const danhSachHienThi = computed(() => {
  const key = keyword.value.trim().toLowerCase()
  const module = moduleNghiepVu.value.trim().toLowerCase()
  const hanh = hanhDong.value.trim().toLowerCase()
  const kq = ketQua.value.trim().toLowerCase()

  return danhSach.value.filter((item) => {
    const json = JSON.stringify(item || {}).toLowerCase()

    const dungKeyword = !key || json.includes(key)

    const dungModule = !module
        || String(item.moduleNghiepVu || '').toLowerCase() === module

    const dungHanhDong = !hanh
        || String(item.hanhDong || '').toLowerCase() === hanh

    const dungKetQua = !kq
        || String(item.ketQua || '').toLowerCase() === kq

    return dungKeyword && dungModule && dungHanhDong && dungKetQua
  })
})

const tongThanhCong = computed(() => {
  return danhSach.value.filter(item => item.ketQua === 'THANH_CONG').length
})

const tongThatBai = computed(() => {
  return danhSach.value.filter(item => item.ketQua === 'THAT_BAI').length
})

onMounted(() => {
  taiDuLieu()
})

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''

  try {
    const params = {
      page: trangHienTai.value,
      size: kichThuocTrang,
      sort: 'id,desc'
    }
    if (keyword.value) params.keyword = keyword.value
    if (moduleNghiepVu.value) params.moduleNghiepVu = moduleNghiepVu.value
    if (hanhDong.value) params.hanhDong = hanhDong.value
    if (ketQua.value) params.ketQua = ketQua.value
    if (tuNgay.value) params.tuNgay = tuNgay.value
    if (denNgay.value) params.denNgay = denNgay.value
    if (taiKhoanEmail.value) params.taiKhoanEmail = taiKhoanEmail.value
    if (banGhiId.value) params.banGhiId = banGhiId.value

    const response = await heThongApi.nhatKyHeThong.getAll(params)
    const pageData = layPage(response)

    danhSach.value = pageData.content
    trangHienTai.value = pageData.number
    tongTrang.value = pageData.totalPages
    tongBanGhi.value = pageData.totalElements
  } catch (error) {
    loi.value = error?.response?.data?.message || error?.message || 'Không tải được nhật ký hệ thống'
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

function hienThiLoaiTaiKhoan(value) {
  if (!value) return '-'

  const text = String(value)

  if (text === 'nhan_vien') return 'Nhân viên'
  if (text === 'sinh_vien') return 'Sinh viên'
  if (text === 'giao_vien') return 'Giáo viên'
  if (text === 'admin') return 'Admin'

  return text
}

function hienThiHanhDong(value) {
  if (value === 'TAO_MOI') return 'Tạo mới'
  if (value === 'CAP_NHAT') return 'Cập nhật'
  if (value === 'XOA') return 'Xóa'
  return value || '-'
}

function hienThiKetQua(value) {
  if (value === 'THANH_CONG') return 'Thành công'
  if (value === 'THAT_BAI') return 'Thất bại'
  return value || '-'
}

function locClient() {
  trangHienTai.value = 0
  taiDuLieu()
}
function xoaLoc() {
  keyword.value = ''
  moduleNghiepVu.value = ''
  hanhDong.value = ''
  ketQua.value = ''
  tuNgay.value = ''
  denNgay.value = ''
  taiKhoanEmail.value = ''
  banGhiId.value = ''
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
  grid-template-columns: minmax(260px, 1.3fr) 220px 180px 180px auto;
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
  min-width: 2300px;
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

.path-cell {
  max-width: 320px;
  white-space: normal;
  color: #334155;
}

.user-agent {
  max-width: 320px;
  white-space: normal;
  color: #475569;
  font-size: 13px;
}

.desc-cell {
  max-width: 280px;
  white-space: normal;
}

.status,
.action {
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

.action.TAO_MOI {
  background: #eff6ff;
  color: #1d4ed8;
}

.action.CAP_NHAT {
  background: #fef3c7;
  color: #b45309;
}

.action.XOA {
  background: #fee2e2;
  color: #b91c1c;
}

pre {
  margin: 0;
  max-width: 320px;
  white-space: pre-wrap;
  word-break: break-word;
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 12px;
  color: #334155;
}

@media (max-width: 1200px) {
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