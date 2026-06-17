<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Quản lý tệp đính kèm</h1>
        <p>Quản lý tệp theo module, nghiệp vụ, đối tượng liên quan và người gửi.</p>
      </div>
    </header>

    <section class="filter-card">
      <label>
        <span>Module</span>
        <input
            v-model.trim="boLoc.module"
            type="text"
            placeholder="sinh_vien, chuong_trinh..."
            @keyup.enter="taiDuLieu"
        />
      </label>

      <label>
        <span>Nghiệp vụ</span>
        <input
            v-model.trim="boLoc.nghiepVu"
            type="text"
            placeholder="ho_so, bang_cap, syllabus..."
            @keyup.enter="taiDuLieu"
        />
      </label>

      <label>
        <span>Đối tượng ID</span>
        <input
            v-model.trim="boLoc.doiTuongId"
            type="number"
            placeholder="ID bản ghi"
            @keyup.enter="taiDuLieu"
        />
      </label>

      <label>
        <span>Người gửi loại</span>
        <select v-model="boLoc.nguoiGuiLoai">
          <option value="">Tất cả</option>
          <option value="TAI_KHOAN">TAI_KHOAN</option>
          <option value="SINH_VIEN">SINH_VIEN</option>
          <option value="GIAO_VIEN">GIAO_VIEN</option>
          <option value="NHAN_VIEN">NHAN_VIEN</option>
        </select>
      </label>

      <label>
        <span>Người gửi ID</span>
        <input
            v-model.trim="boLoc.nguoiGuiId"
            type="number"
            placeholder="ID người gửi"
            @keyup.enter="taiDuLieu"
        />
      </label>

      <div class="filter-actions">
        <button type="button" class="btn primary" @click="taiDuLieu">
          Lọc
        </button>

        <button type="button" class="btn" @click="xoaLoc">
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

    <div class="pn-table-wrapper">
      <div class="table-toolbar">
        <div class="table-stats">
          <span>Hiển thị: <strong>{{ danhSach.length }}</strong></span>
          <span>Giới hạn: <strong>100</strong></span>
          <span>Trang: <strong>{{ trangHienTai + 1 }}</strong></span>
          <span>Tổng: <strong>{{ tongBanGhi }}</strong></span>
        </div>

        <button type="button" class="btn compact primary-outline" @click="taiDuLieu">
          Tải lại
        </button>
      </div>

      <table class="pn-table">
        <thead>
        <tr>
          <th class="col-icon"></th>
          <th>Tên tệp tin</th>
          <th>Nguồn gốc</th>
          <th>Người gửi</th>
          <th>Ngày tạo</th>
          <th>Trạng thái</th>
          <th class="text-right">Thao tác</th>
        </tr>
        </thead>

        <tbody>
        <tr v-if="dangTai">
          <td colspan="7" class="text-center py-4">
            Đang tải dữ liệu...
          </td>
        </tr>

        <tr v-else-if="!danhSach.length">
          <td colspan="7" class="text-center py-4 text-muted">
            Chưa có tệp đính kèm nào phù hợp với bộ lọc.
          </td>
        </tr>

        <template v-else v-for="item in danhSach" :key="item.id">
          <tr
              class="pn-row-main"
              :class="{ 'is-expanded': expanded === item.id }"
              @click="toggleExpand(item.id)"
          >
            <td class="col-icon">
              <svg
                  class="chevron-icon"
                  :class="{ open: expanded === item.id }"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
              >
                <path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7" />
              </svg>
            </td>

            <td>
              <div class="file-info-cell">
                <div class="file-type-badge">
                  {{ getFileType(item.contentType) }}
                </div>

                <div class="file-details">
                  <strong class="file-name">
                    {{ layTenTep(item) }}
                  </strong>
                  <span class="file-meta">
                    {{ item.contentType || 'Không rõ định dạng' }}
                  </span>
                </div>
              </div>
            </td>

            <td>
              <div class="stacked-cell">
                <strong>{{ item.module || '-' }}</strong>
                <span>{{ item.nghiepVu || '-' }}</span>
              </div>
            </td>

            <td>
              <div class="stacked-cell">
                <strong>{{ item.nguoiGuiTen || '-' }}</strong>
                <span>
                  {{ item.nguoiGuiLoai || 'Loại: Khác' }}
                  <template v-if="item.nguoiGuiId">
                    #{{ item.nguoiGuiId }}
                  </template>
                </span>
              </div>
            </td>

            <td class="date-cell">
              {{ item.ngayTao || item.createdAt || '-' }}
            </td>

            <td>
              <span class="status-badge" :class="getBadgeClass(item.trangThai)">
                {{ item.trangThai || 'KHÔNG RÕ' }}
              </span>
            </td>

            <td class="text-right actions-cell" @click.stop>
              <a
                  v-if="item.id"
                  :href="layLinkTaiTep(item.id)"
                  target="_blank"
                  rel="noopener"
                  class="btn-action-download"
              >
                <svg
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                    class="icon-sm"
                >
                  <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"
                  />
                </svg>
                Tải xuống
              </a>
            </td>
          </tr>

          <tr v-show="expanded === item.id" class="pn-row-detail">
            <td colspan="7">
              <div class="detail-container">
                <div class="detail-grid">
                  <div class="detail-col">
                    <span class="detail-label">ID tệp tin</span>
                    <span class="detail-value mono">#{{ item.id }}</span>
                  </div>

                  <div class="detail-col">
                    <span class="detail-label">ID đối tượng</span>
                    <span class="detail-value mono">
                      #{{ item.doiTuongId || 'N/A' }}
                    </span>
                  </div>

                  <div class="detail-col">
                    <span class="detail-label">Module</span>
                    <span class="detail-value">
                      {{ item.module || '-' }}
                    </span>
                  </div>

                  <div class="detail-col">
                    <span class="detail-label">Nghiệp vụ</span>
                    <span class="detail-value">
                      {{ item.nghiepVu || '-' }}
                    </span>
                  </div>

                  <div class="detail-col">
                    <span class="detail-label">Người gửi loại</span>
                    <span class="detail-value">
                      {{ item.nguoiGuiLoai || '-' }}
                    </span>
                  </div>

                  <div class="detail-col">
                    <span class="detail-label">Người gửi ID</span>
                    <span class="detail-value mono">
                      #{{ item.nguoiGuiId || 'N/A' }}
                    </span>
                  </div>

                  <div class="detail-col span-2">
                    <span class="detail-label">Mô tả / Ghi chú</span>
                    <p class="detail-desc">
                      {{ item.moTa || 'Không có mô tả cho tệp đính kèm này.' }}
                    </p>
                  </div>
                </div>
              </div>
            </td>
          </tr>
        </template>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'

import { heThongApi } from '../api/heThongApi'



const danhSach = ref([])
const dangTai = ref(false)
const loi = ref('')
const thongBao = ref('')
const trangHienTai = ref(0)
const tongBanGhi = ref(0)
const expanded = ref(null)

const boLoc = reactive({
  module: '',
  nghiepVu: '',
  doiTuongId: '',
  nguoiGuiLoai: '',
  nguoiGuiId: ''
})



onMounted(() => {
  taiDuLieu()
})

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''
  thongBao.value = ''
  expanded.value = null

  try {
    const page = 0
    const size = 100

    let response

    if (boLoc.doiTuongId) {
      response = await heThongApi.tepDinhKem.getByDoiTuong({
        module: boLoc.module,
        nghiepVu: boLoc.nghiepVu,
        doiTuongId: Number(boLoc.doiTuongId),
        page,
        size
      })
    } else if (boLoc.nguoiGuiLoai && boLoc.nguoiGuiId) {
      response = await heThongApi.tepDinhKem.getByNguoiGui({
        nguoiGuiLoai: boLoc.nguoiGuiLoai,
        nguoiGuiId: Number(boLoc.nguoiGuiId),
        page,
        size
      })
    } else {
      const params = {
        page,
        size
      }

      if (boLoc.module) params.module = boLoc.module
      if (boLoc.nghiepVu) params.nghiepVu = boLoc.nghiepVu

      response = await heThongApi.tepDinhKem.getAll(params)
    }

    const pageData = layPage(response)
    danhSach.value = pageData.content
    trangHienTai.value = pageData.number
    tongBanGhi.value = pageData.totalElements
  } catch (error) {
    loi.value = error?.response?.data?.message || error?.message || 'Không tải được danh sách tệp đính kèm'
  } finally {
    dangTai.value = false
  }
}

function layPage(response) {
  const data = response?.data?.data || response?.data || response

  if (Array.isArray(data)) {
    return {
      content: data,
      number: 0,
      totalElements: data.length
    }
  }

  if (Array.isArray(data?.content)) {
    return {
      content: data.content,
      number: data.number || 0,
      totalElements: data.totalElements || data.content.length
    }
  }

  return {
    content: [],
    number: 0,
    totalElements: 0
  }
}



function xoaLoc() {
  boLoc.module = ''
  boLoc.nghiepVu = ''
  boLoc.doiTuongId = ''
  boLoc.nguoiGuiLoai = ''
  boLoc.nguoiGuiId = ''
  taiDuLieu()
}



function layLinkTaiTep(id) {
  return heThongApi.tepDinhKem.downloadUrl(id)
}

function toggleExpand(id) {
  expanded.value = expanded.value === id ? null : id
}

function layTenTep(item) {
  return item?.tenMau || item?.tenmau || item?.tenFile || item?.tenTep || '-'
}

function getFileType(contentType) {
  if (!contentType) {
    return 'FILE'
  }

  const value = contentType.toLowerCase()

  if (value.includes('pdf')) {
    return 'PDF'
  }

  if (value.includes('image')) {
    return 'IMG'
  }

  if (value.includes('word') || value.includes('document')) {
    return 'DOC'
  }

  if (value.includes('excel') || value.includes('spreadsheet')) {
    return 'XLS'
  }

  if (value.includes('zip') || value.includes('rar')) {
    return 'ZIP'
  }

  return contentType.split('/')[1]?.substring(0, 4).toUpperCase() || 'FILE'
}

function getBadgeClass(status) {
  if (!status) {
    return 'badge-default'
  }

  const value = status.toUpperCase()

  if (
      value.includes('DANG_SU_DUNG') ||
      value.includes('HOAT_DONG') ||
      value.includes('DUYET') ||
      value.includes('OK') ||
      value.includes('ACTIVE')
  ) {
    return 'badge-success'
  }

  if (
      value.includes('LOI') ||
      value.includes('HUY') ||
      value.includes('XOA') ||
      value.includes('INACTIVE')
  ) {
    return 'badge-danger'
  }

  return 'badge-warning'
}
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding-bottom: 16px;
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
  color: #111827;
  font-weight: 700;
}

.page-head p {
  margin: 2px 0 0;
  color: #6b7280;
  font-size: 13px;
}

.filter-card {
  display: grid;
  grid-template-columns: 1.05fr 1.05fr 0.9fr 0.95fr 0.9fr auto;
  gap: 10px;
  align-items: end;
  padding: 12px 14px;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  background: #ffffff;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.04);
}

label {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

label span {
  font-size: 12px;
  font-weight: 600;
  color: #374151;
}

input,
select,
textarea {
  border: 1px solid #d1d5db;
  border-radius: 7px;
  padding: 7px 10px;
  font-size: 13px;
  color: #111827;
  transition: border-color 0.2s;
  width: 100%;
  box-sizing: border-box;
  background: #ffffff;
}

input:focus,
select:focus,
textarea:focus {
  border-color: #077149;
  outline: none;
  box-shadow: 0 0 0 3px rgba(7, 113, 73, 0.1);
}

.filter-actions {
  display: flex;
  align-items: end;
  gap: 8px;
}

.btn {
  border: 1px solid #d1d5db;
  background: #ffffff;
  color: #374151;
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
  background: #f9fafb;
  border-color: #9ca3af;
}

.btn.primary {
  background: #077149;
  color: #ffffff;
  border-color: #077149;
}

.btn.primary:hover:not(:disabled) {
  background: #055a3a;
  border-color: #055a3a;
}

.btn.primary-outline {
  color: #077149;
  border-color: #077149;
}

.btn.primary-outline:hover:not(:disabled) {
  background: #eaf6f0;
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

.alert.success {
  background: #eaf6f0;
  color: #065f3d;
  border: 1px solid #d0eadb;
}

.alert.error {
  background: #fef2f2;
  color: #991b1b;
  border: 1px solid #fecaca;
}

.pn-table-wrapper {
  max-height: calc(100vh - 190px);
  overflow: auto;
  background: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
}

.table-toolbar {
  position: sticky;
  top: 0;
  z-index: 4;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  padding: 7px 12px;
  border-bottom: 1px solid #e5e7eb;
  background: #ffffff;
}

.table-stats {
  display: flex;
  align-items: center;
  gap: 14px;
  flex-wrap: wrap;
  color: #64748b;
  font-size: 12px;
}

.table-stats strong {
  color: #111827;
  font-size: 13px;
}

.pn-table {
  width: 100%;
  border-collapse: collapse;
  min-width: 900px;
  text-align: left;
}

.pn-table thead th {
  position: sticky;
  top: 43px;
  z-index: 3;
}

.pn-table th {
  background: #f9fafb;
  color: #4b5563;
  font-weight: 600;
  font-size: 12px;
  padding: 9px 12px;
  border-bottom: 1px solid #e5e7eb;
  white-space: nowrap;
}

.pn-table td {
  padding: 8px 12px;
  border-bottom: 1px solid #f3f4f6;
  vertical-align: middle;
  font-size: 13px;
  color: #1f2937;
}

.col-icon {
  width: 32px;
  text-align: center;
  padding-right: 0 !important;
  cursor: pointer;
}

.text-right {
  text-align: right;
}

.text-center {
  text-align: center;
}

.text-muted {
  color: #6b7280;
}

.py-4 {
  padding-top: 18px !important;
  padding-bottom: 18px !important;
}

.pn-row-main {
  transition: background-color 0.2s;
  cursor: pointer;
}

.pn-row-main:hover {
  background-color: #f9fafb;
}

.pn-row-main.is-expanded {
  background-color: #f0fdf4;
}

.chevron-icon {
  width: 16px;
  height: 16px;
  color: #9ca3af;
  transition: transform 0.3s ease, color 0.2s;
}

.pn-row-main:hover .chevron-icon {
  color: #077149;
}

.chevron-icon.open {
  transform: rotate(90deg);
  color: #077149;
}

.file-info-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.file-type-badge {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f3f4f6;
  border: 1px solid #e5e7eb;
  border-radius: 7px;
  font-size: 10px;
  font-weight: 700;
  color: #4b5563;
  flex-shrink: 0;
}

.file-details {
  display: flex;
  flex-direction: column;
  gap: 1px;
  min-width: 0;
}

.file-name {
  color: #111827;
  font-weight: 600;
  transition: color 0.2s;
  word-break: break-word;
  line-height: 1.25;
}

.pn-row-main:hover .file-name {
  color: #077149;
}

.file-meta {
  font-size: 11px;
  color: #6b7280;
  word-break: break-word;
}

.stacked-cell {
  display: flex;
  flex-direction: column;
  gap: 1px;
}

.stacked-cell strong {
  color: #111827;
  font-weight: 500;
  line-height: 1.25;
}

.stacked-cell span {
  font-size: 11px;
  color: #6b7280;
}

.date-cell {
  color: #4b5563;
  white-space: nowrap;
  font-size: 12px;
}

.actions-cell {
  white-space: nowrap;
}

.btn-action-download {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  padding: 5px 9px;
  font-size: 12px;
  font-weight: 600;
  color: #077149;
  background: #eaf6f0;
  border-radius: 6px;
  text-decoration: none;
  transition: all 0.2s;
}

.btn-action-download:hover {
  background: #d0eadb;
  color: #055a3a;
}

.icon-sm {
  width: 14px;
  height: 14px;
}

.status-badge {
  padding: 3px 8px;
  border-radius: 6px;
  font-size: 11px;
  font-weight: 600;
  border: 1px solid transparent;
  white-space: nowrap;
}

.badge-success {
  background: #eaf6f0;
  color: #077149;
  border-color: #d0eadb;
}

.badge-warning {
  background: #fef3c7;
  color: #92400e;
  border-color: #fde68a;
}

.badge-danger {
  background: #fee2e2;
  color: #b91c1c;
  border-color: #fecaca;
}

.badge-default {
  background: #f3f4f6;
  color: #4b5563;
  border-color: #e5e7eb;
}

.pn-row-detail {
  background: #f9fafb;
  border-bottom: 1px solid #e5e7eb;
}

.pn-row-detail td {
  padding: 0;
  border: none;
}

.detail-container {
  padding: 10px 16px 12px 44px;
  border-left: 3px solid #077149;
  box-shadow: inset 0 2px 4px 0 rgba(0, 0, 0, 0.02);
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}

.detail-col {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.detail-col.span-2 {
  grid-column: span 2;
}

.detail-label {
  font-size: 10px;
  font-weight: 700;
  color: #9ca3af;
  text-transform: uppercase;
  letter-spacing: 0.4px;
}

.detail-value {
  font-size: 12px;
  color: #111827;
}

.detail-value.mono {
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  background: #ffffff;
  padding: 2px 6px;
  border: 1px solid #e5e7eb;
  border-radius: 4px;
  display: inline-block;
  width: max-content;
}

.detail-desc {
  font-size: 12px;
  color: #374151;
  background: #ffffff;
  border: 1px solid #e5e7eb;
  padding: 8px;
  border-radius: 7px;
  margin: 0;
  line-height: 1.4;
}

@media (max-width: 1280px) {
  .filter-card {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }

  .filter-actions {
    grid-column: span 3;
  }

  .pn-table-wrapper {
    max-height: calc(100vh - 230px);
  }
}

@media (max-width: 1024px) {
  .detail-grid {
    grid-template-columns: 1fr 1fr;
  }

  .detail-col.span-2 {
    grid-column: span 2;
  }
}

@media (max-width: 768px) {
  .filter-card {
    grid-template-columns: 1fr;
  }

  .filter-actions {
    grid-column: auto;
    align-items: stretch;
    flex-direction: column;
  }

  .filter-actions .btn {
    width: 100%;
  }

  .table-toolbar {
    align-items: flex-start;
    flex-direction: column;
  }

  .pn-table-wrapper {
    max-height: none;
  }

  .pn-table thead th {
    position: static;
  }
}

@media (max-width: 640px) {
  .detail-grid {
    grid-template-columns: 1fr;
  }

  .detail-col.span-2 {
    grid-column: span 1;
  }

  .page-head {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>