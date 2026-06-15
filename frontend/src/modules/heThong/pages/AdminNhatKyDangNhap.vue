<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Nhật ký đăng nhập</h1>
        <p>Theo dõi lịch sử đăng nhập, tài khoản đăng nhập, chủ tài khoản, IP và thiết bị.</p>
      </div>
    </header>

    <section class="filter-card">
      <label class="search-field">
        <span>Tìm kiếm</span>
        <input
            v-model.trim="keyword"
            type="text"
            placeholder="Email, tài khoản, người dùng, IP, thiết bị, lý do..."
            @keyup.enter="locClient"
        />
      </label>

      <label>
        <span>Trạng thái</span>
        <select v-model="thanhCong" @change="locClient">
          <option value="">Tất cả</option>
          <option value="true">Thành công</option>
          <option value="false">Thất bại</option>
        </select>
      </label>

      <label>
        <span>Loại tài khoản</span>
        <select v-model="loaiTaiKhoan" @change="locClient">
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
          Xóa
        </button>
      </div>
    </section>

    <div v-if="loi" class="alert error">
      {{ loi }}
    </div>

    <div class="table-wrap">
      <div class="table-toolbar">
        <div class="table-stats">
          <span>Tổng: <strong>{{ tongBanGhi }}</strong></span>
          <span>Hiển thị: <strong>{{ danhSachHienThi.length }}</strong></span>
          <span>Thành công: <strong>{{ tongThanhCong }}</strong></span>
          <span>Thất bại: <strong>{{ tongThatBai }}</strong></span>
          <span>Trang: <strong>{{ trangHienTai + 1 }} / {{ tongTrang || 1 }}</strong></span>
        </div>

        <div class="table-actions">
          <button
              type="button"
              class="btn compact"
              :disabled="trangHienTai <= 0 || dangTai"
              @click="doiTrang(trangHienTai - 1)"
          >
            Trước
          </button>

          <button type="button" class="btn compact primary-outline" @click="taiDuLieu">
            Tải lại
          </button>

          <button
              type="button"
              class="btn compact"
              :disabled="tongTrang === 0 || trangHienTai + 1 >= tongTrang || dangTai"
              @click="doiTrang(trangHienTai + 1)"
          >
            Sau
          </button>
        </div>
      </div>

      <table>
        <thead>
        <tr>
          <th class="col-toggle"></th>
          <th>Thời gian</th>
          <th>Tài khoản</th>
          <th>Loại</th>
          <th>IP</th>
          <th>Thiết bị / trình duyệt</th>
          <th>Kết quả</th>
        </tr>
        </thead>

        <tbody>
        <tr v-if="dangTai">
          <td colspan="7" class="empty-cell">
            Đang tải dữ liệu...
          </td>
        </tr>

        <tr v-else-if="!danhSachHienThi.length">
          <td colspan="7" class="empty-cell">
            Chưa có dữ liệu
          </td>
        </tr>

        <template v-else>
          <template v-for="item in danhSachHienThi" :key="item.id">
            <tr
                class="main-row"
                :class="{ 'is-expanded': expandedRows.includes(item.id) }"
            >
              <td class="col-toggle">
                <button
                    type="button"
                    class="toggle-btn"
                    :class="{ 'is-open': expandedRows.includes(item.id) }"
                    title="Xem chi tiết"
                    @click="toggleRow(item.id)"
                >
                  <svg width="14" height="14" fill="currentColor" viewBox="0 0 256 256">
                    <path
                        d="M213.66,101.66l-80,80a8,8,0,0,1-11.32,0l-80-80A8,8,0,0,1,53.66,90.34L128,164.69l74.34-74.35a8,8,0,0,1,11.32,11.32Z"
                    />
                  </svg>
                </button>
              </td>

              <td>
                <div class="time-cell">
                  <strong>#{{ item.id }}</strong>
                  <span>{{ hienThiThoiGian(item) }}</span>
                </div>
              </td>

              <td>
                <div class="account-cell">
                  <strong>{{ item.tenTaiKhoan || item.emailNhap || '-' }}</strong>
                  <span>{{ item.taiKhoanEmail || item.email || item.emailNhap || '-' }}</span>
                </div>
              </td>

              <td>
                {{ hienThiLoaiTaiKhoan(item.loaiTaiKhoan) }}
              </td>

              <td class="ip-cell">
                {{ item.ipAddress || '-' }}
              </td>

              <td class="user-agent">
                {{ item.userAgent || '-' }}
              </td>

              <td>
                <span class="status" :class="item.thanhCong === true ? 'success' : 'error'">
                  {{ item.thanhCong === true ? 'Thành công' : 'Thất bại' }}
                </span>
              </td>
            </tr>

            <tr v-if="expandedRows.includes(item.id)" class="expanded-row">
              <td colspan="7">
                <div class="expanded-content">
                  <section class="detail-panel">
                    <h4>Thông tin đăng nhập</h4>

                    <div class="detail-grid">
                      <div class="detail-row">
                        <strong>ID log</strong>
                        <span>#{{ item.id }}</span>
                      </div>

                      <div class="detail-row">
                        <strong>Thời gian</strong>
                        <span>{{ hienThiThoiGian(item) }}</span>
                      </div>

                      <div class="detail-row">
                        <strong>Tài khoản ID</strong>
                        <span>{{ item.taiKhoanId || '-' }}</span>
                      </div>

                      <div class="detail-row">
                        <strong>Email nhập</strong>
                        <span>{{ item.emailNhap || '-' }}</span>
                      </div>

                      <div class="detail-row">
                        <strong>Email tài khoản</strong>
                        <span>{{ item.taiKhoanEmail || item.email || '-' }}</span>
                      </div>

                      <div class="detail-row">
                        <strong>Tên tài khoản</strong>
                        <span>{{ item.tenTaiKhoan || '-' }}</span>
                      </div>

                      <div class="detail-row">
                        <strong>Loại tài khoản</strong>
                        <span>{{ hienThiLoaiTaiKhoan(item.loaiTaiKhoan) }}</span>
                      </div>

                      <div class="detail-row">
                        <strong>Phương thức</strong>
                        <span class="badge-method">{{ item.phuongThuc || 'N/A' }}</span>
                      </div>

                      <div class="detail-row">
                        <strong>IP</strong>
                        <span>{{ item.ipAddress || '-' }}</span>
                      </div>

                      <div class="detail-row full">
                        <strong>Thiết bị</strong>
                        <span>{{ item.userAgent || '-' }}</span>
                      </div>
                    </div>
                  </section>

                  <section class="detail-panel">
                    <h4>Đối tượng và kết quả</h4>

                    <div class="detail-grid">
                      <div class="detail-row">
                        <strong>Đối tượng tên</strong>
                        <span>{{ item.doiTuongTen || '-' }}</span>
                      </div>

                      <div class="detail-row">
                        <strong>Đối tượng loại</strong>
                        <span>{{ item.doiTuongLoai || '-' }}</span>
                      </div>

                      <div class="detail-row">
                        <strong>Đối tượng ID</strong>
                        <span>{{ item.doiTuongId || '-' }}</span>
                      </div>

                      <div class="detail-row">
                        <strong>Kết quả</strong>
                        <span>
                          <span class="status" :class="item.thanhCong === true ? 'success' : 'error'">
                            {{ item.thanhCong === true ? 'Thành công' : 'Thất bại' }}
                          </span>
                        </span>
                      </div>

                      <div class="detail-row full">
                        <strong>Lý do thất bại</strong>
                        <span :class="{ danger: item.thanhCong === false }">
                          {{ item.lyDoThatBai || '-' }}
                        </span>
                      </div>

                      <div class="detail-row full">
                        <strong>Mô tả</strong>
                        <span>
                          {{ item.emailNhap || item.taiKhoanEmail || item.email || 'Tài khoản' }}
                          thực hiện đăng nhập,
                          kết quả
                          <b :class="item.thanhCong === true ? 'text-success' : 'danger'">
                            {{ item.thanhCong === true ? 'thành công' : 'thất bại' }}
                          </b>.
                        </span>
                      </div>

                      <div class="payload-box full">
                        <div class="payload-header">Dữ liệu gốc</div>
                        <pre class="payload-body">{{ dinhDangJson(item) }}</pre>
                      </div>
                    </div>
                  </section>
                </div>
              </td>
            </tr>
          </template>
        </template>
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

const expandedRows = ref([])

const danhSachHienThi = computed(() => {
  const key = keyword.value.trim().toLowerCase()
  const loai = loaiTaiKhoan.value.trim().toLowerCase()

  return danhSach.value.filter((item) => {
    const json = JSON.stringify(item || {}).toLowerCase()

    const dungKeyword = !key || json.includes(key)

    const dungTrangThai =
        thanhCong.value === '' ||
        String(item.thanhCong) === thanhCong.value

    const dungLoaiTaiKhoan =
        !loai ||
        String(item.loaiTaiKhoan || '').toLowerCase() === loai

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
  expandedRows.value = []

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

function toggleRow(id) {
  const index = expandedRows.value.indexOf(id)

  if (index > -1) {
    expandedRows.value.splice(index, 1)
  } else {
    expandedRows.value.push(id)
  }
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

function dinhDangJson(value) {
  try {
    return JSON.stringify(value || {}, null, 2)
  } catch (error) {
    return '{}'
  }
}

function locClient() {
  trangHienTai.value = 0
  expandedRows.value = []
  taiDuLieu()
}

function xoaLoc() {
  keyword.value = ''
  thanhCong.value = ''
  loaiTaiKhoan.value = ''
  trangHienTai.value = 0
  expandedRows.value = []
  taiDuLieu()
}
</script>

<style scoped>
.page {
  --primary: #077149;
  --primary-hover: #055839;
  --primary-light: #e6f1ec;
  --primary-border: #a3d4c1;
  --text-main: #111827;
  --text-muted: #64748b;
  --border-color: #e2e8f0;
  --danger: #dc2626;
  --danger-bg: #fef2f2;
  --danger-border: #fecaca;

  display: flex;
  flex-direction: column;
  gap: 12px;
  padding-bottom: 16px;
  color: var(--text-main);
}

.page-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.page-head h1 {
  margin: 0;
  font-size: 20px;
  color: var(--primary);
  font-weight: 700;
}

.page-head p {
  margin: 2px 0 0;
  color: var(--text-muted);
  font-size: 13px;
}

.filter-card {
  display: grid;
  grid-template-columns: minmax(320px, 1.5fr) 180px 190px auto;
  gap: 10px;
  align-items: end;
  padding: 12px 14px;
  border: 1px solid var(--border-color);
  border-radius: 10px;
  background: #ffffff;
  box-shadow: 0 1px 2px rgba(15, 23, 42, 0.04);
}

.filter-card label {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.filter-card span {
  font-size: 12px;
  font-weight: 600;
  color: #374151;
}

input,
select {
  width: 100%;
  box-sizing: border-box;
  border: 1px solid #d1d5db;
  border-radius: 7px;
  padding: 7px 10px;
  font-size: 13px;
  color: var(--text-main);
  background: #ffffff;
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
}

input:hover,
select:hover {
  border-color: #94a3b8;
}

input:focus,
select:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(7, 113, 73, 0.1);
}

.filter-actions {
  display: flex;
  align-items: end;
  gap: 8px;
}

.btn {
  border: 1px solid #cbd5e1;
  background: #ffffff;
  color: #475467;
  border-radius: 7px;
  padding: 7px 12px;
  cursor: pointer;
  font-weight: 600;
  font-size: 13px;
  transition: all 0.2s;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  white-space: nowrap;
}

.btn:hover:not(:disabled) {
  background: #f8fafc;
  color: var(--text-main);
  border-color: #94a3b8;
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn.primary {
  background: var(--primary);
  color: #ffffff;
  border-color: var(--primary);
}

.btn.primary:hover:not(:disabled) {
  background: var(--primary-hover);
  border-color: var(--primary-hover);
}

.btn.primary-outline {
  color: var(--primary);
  border-color: var(--primary);
}

.btn.primary-outline:hover:not(:disabled) {
  background: var(--primary-light);
}

.btn.compact {
  padding: 5px 10px;
  font-size: 12px;
}

.alert {
  padding: 9px 12px;
  border-radius: 7px;
  font-size: 13px;
  font-weight: 500;
}

.alert.error {
  background: var(--danger-bg);
  color: var(--danger);
  border: 1px solid var(--danger-border);
}

.table-wrap {
  max-height: calc(100vh - 165px);
  overflow: auto;
  border: 1px solid var(--border-color);
  border-radius: 10px;
  background: #ffffff;
  box-shadow: 0 1px 2px rgba(15, 23, 42, 0.05);
}

.table-toolbar {
  position: sticky;
  top: 0;
  z-index: 5;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  padding: 7px 12px;
  border-bottom: 1px solid var(--border-color);
  background: #ffffff;
}

.table-stats {
  display: flex;
  align-items: center;
  gap: 14px;
  flex-wrap: wrap;
  color: var(--text-muted);
  font-size: 12px;
}

.table-stats strong {
  color: var(--text-main);
  font-size: 13px;
}

.table-actions {
  display: flex;
  align-items: center;
  gap: 6px;
}

table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1080px;
}

thead th {
  position: sticky;
  top: 39px;
  z-index: 4;
}

th,
td {
  padding: 8px 12px;
  border-bottom: 1px solid var(--border-color);
  text-align: left;
  vertical-align: middle;
  font-size: 13px;
  color: #334155;
}

th {
  background: #f8fafc;
  color: #475467;
  font-size: 12px;
  font-weight: 600;
  white-space: nowrap;
}

tbody tr {
  transition: background-color 0.15s ease;
}

tbody tr:hover {
  background-color: #f8fafc;
}

.empty-cell {
  text-align: center;
  padding: 20px !important;
  color: var(--text-muted);
}

.col-toggle {
  width: 34px;
  text-align: center;
}

.main-row.is-expanded {
  background-color: #f0fdf4;
}

.toggle-btn {
  width: 24px;
  height: 24px;
  border-radius: 6px;
  background-color: var(--primary);
  color: #ffffff;
  border: none;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.2s, transform 0.2s;
}

.toggle-btn:hover {
  background-color: var(--primary-hover);
}

.toggle-btn svg {
  transition: transform 0.25s ease;
}

.toggle-btn.is-open svg {
  transform: rotate(180deg);
}

.time-cell,
.account-cell {
  display: flex;
  flex-direction: column;
  gap: 1px;
}

.time-cell strong,
.account-cell strong {
  color: var(--text-main);
  font-weight: 600;
  line-height: 1.25;
}

.time-cell span,
.account-cell span {
  color: var(--text-muted);
  font-size: 11px;
  line-height: 1.25;
}

.ip-cell {
  white-space: nowrap;
}

.user-agent {
  max-width: 430px;
  color: #475569;
  font-size: 12px;
  line-height: 1.35;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.status {
  display: inline-flex;
  align-items: center;
  border-radius: 999px;
  padding: 3px 8px;
  font-size: 11px;
  font-weight: 600;
  white-space: nowrap;
}

.status.success {
  background: var(--primary-light);
  color: var(--primary);
  border: 1px solid var(--primary-border);
}

.status.error {
  background: var(--danger-bg);
  color: var(--danger);
  border: 1px solid var(--danger-border);
}

.text-success {
  color: var(--primary);
}

.danger {
  color: var(--danger) !important;
}

.expanded-row td {
  padding: 0 !important;
  border-bottom: 2px solid var(--primary);
}

.expanded-content {
  background-color: #f8fafc;
  border-top: 1px dashed var(--primary-border);
  padding: 12px 16px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.detail-panel {
  background: #ffffff;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 12px;
}

.detail-panel h4 {
  color: var(--primary);
  margin: 0 0 10px;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 0.4px;
}

.detail-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px 12px;
  font-size: 12px;
}

.detail-row {
  display: flex;
  flex-direction: column;
  gap: 3px;
  min-width: 0;
}

.detail-row.full,
.payload-box.full {
  grid-column: 1 / -1;
}

.detail-row strong {
  color: var(--text-main);
  font-weight: 600;
  font-size: 11px;
}

.detail-row span {
  color: var(--text-muted);
  word-break: break-word;
  line-height: 1.35;
}

.badge-method {
  width: max-content;
  background: #e2e8f0;
  color: #334155 !important;
  padding: 2px 7px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 600;
  font-family: monospace;
}

.payload-box {
  background: #ffffff;
  border: 1px solid var(--border-color);
  border-radius: 7px;
  overflow: hidden;
  margin-top: 2px;
}

.payload-header {
  background: #f8fafc;
  padding: 7px 10px;
  border-bottom: 1px solid var(--border-color);
  font-weight: 600;
  font-size: 12px;
  color: var(--text-main);
}

.payload-body {
  margin: 0;
  padding: 10px;
  font-family: monospace;
  font-size: 11px;
  color: var(--text-muted);
  background: #fafafa;
  max-height: 180px;
  overflow: auto;
  white-space: pre-wrap;
}

@media (max-width: 1280px) {
  .filter-card {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .filter-actions {
    grid-column: 1 / -1;
  }

  .table-wrap {
    max-height: calc(100vh - 220px);
  }

  .expanded-content {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .filter-card {
    grid-template-columns: 1fr;
  }

  .filter-actions,
  .table-actions {
    align-items: stretch;
    flex-direction: column;
  }

  .filter-actions .btn,
  .table-actions .btn {
    width: 100%;
  }

  .table-toolbar {
    align-items: flex-start;
    flex-direction: column;
  }

  .table-wrap {
    max-height: none;
  }

  thead th {
    position: static;
  }
}
</style>