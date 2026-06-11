<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Quản lý tệp đính kèm</h1>
        <p>Quản lý tệp theo module, nghiệp vụ, đối tượng liên quan và người gửi.</p>
      </div>

      <button type="button" class="btn" @click="taiDuLieu">
        Tải lại
      </button>
    </header>

    <section class="filter-card">
      <label>
        <span>Module</span>
        <input
            v-model.trim="boLoc.module"
            type="text"
            placeholder="sinh_vien, chuong_trinh, giang_day..."
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
          Lọc tệp
        </button>

        <button type="button" class="btn" @click="xoaLoc">
          Xóa lọc
        </button>
      </div>
    </section>

    <section v-if="laAdmin" class="upload-card">
      <h2>Upload tệp</h2>

      <form class="form-grid" @submit.prevent="uploadTep">
        <label>
          <span>File</span>
          <input type="file" @change="chonFile" />
        </label>

        <label>
          <span>Module</span>
          <input
              v-model.trim="formUpload.module"
              type="text"
              placeholder="sinh_vien"
              required
          />
        </label>

        <label>
          <span>Nghiệp vụ</span>
          <input
              v-model.trim="formUpload.nghiepVu"
              type="text"
              placeholder="ho_so"
              required
          />
        </label>

        <label>
          <span>Đối tượng ID</span>
          <input
              v-model.trim="formUpload.doiTuongId"
              type="number"
              placeholder="ID bản ghi liên quan"
          />
        </label>

        <label>
          <span>Người gửi loại</span>
          <select v-model="formUpload.nguoiGuiLoai">
            <option value="">-- Chọn --</option>
            <option value="TAI_KHOAN">TAI_KHOAN</option>
            <option value="SINH_VIEN">SINH_VIEN</option>
            <option value="GIAO_VIEN">GIAO_VIEN</option>
            <option value="NHAN_VIEN">NHAN_VIEN</option>
          </select>
        </label>

        <label>
          <span>Người gửi ID</span>
          <input
              v-model.trim="formUpload.nguoiGuiId"
              type="number"
              placeholder="ID người gửi"
          />
        </label>

        <label>
          <span>Người gửi tên</span>
          <input
              v-model.trim="formUpload.nguoiGuiTen"
              type="text"
              placeholder="Tên người gửi"
          />
        </label>

        <label class="field-full">
          <span>Mô tả</span>
          <textarea
              v-model.trim="formUpload.moTa"
              rows="2"
              placeholder="Ghi chú về tệp"
          />
        </label>

        <div class="filter-actions">
          <button type="submit" class="btn primary">
            Upload
          </button>

          <button type="button" class="btn" @click="resetUpload">
            Làm mới
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

    <div class="summary-grid">
      <div>
        <span>Đang hiển thị</span>
        <strong>{{ danhSach.length }}</strong>
      </div>

      <div>
        <span>Giới hạn mỗi lần</span>
        <strong>100</strong>
      </div>

      <div>
        <span>Trang hiện tại</span>
        <strong>{{ trangHienTai + 1 }}</strong>
      </div>

      <div>
        <span>Tổng bản ghi</span>
        <strong>{{ tongBanGhi }}</strong>
      </div>
    </div>

    <div class="table-wrap">
      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>Tên tệp</th>
          <th>Module</th>
          <th>Nghiệp vụ</th>
          <th>Đối tượng ID</th>
          <th>Người gửi</th>
          <th>Trạng thái</th>
          <th>Ngày tạo</th>
          <th>Mô tả</th>
          <th>Tải</th>
          <th v-if="laAdmin">Thao tác</th>
        </tr>
        </thead>

        <tbody>
        <tr v-if="dangTai">
          <td :colspan="laAdmin ? 11 : 10">Đang tải dữ liệu...</td>
        </tr>

        <tr v-else-if="!danhSach.length">
          <td :colspan="laAdmin ? 11 : 10">Chưa có tệp đính kèm</td>
        </tr>

        <tr v-for="item in danhSach" v-else :key="item.id">
          <td>{{ item.id }}</td>
          <td>
            <div class="file-cell">
              <strong>{{ item.tenmau || item.tenFile || '-' }}</strong>
              <span>{{ item.contentType || '-' }}</span>
            </div>
          </td>
          <td>{{ item.module || '-' }}</td>
          <td>{{ item.nghiepVu || '-' }}</td>
          <td>{{ item.doiTuongId || '-' }}</td>
          <td>
            <div class="file-cell">
              <strong>{{ item.nguoiGuiTen || '-' }}</strong>
              <span>{{ item.nguoiGuiLoai || '-' }} {{ item.nguoiGuiId ? '#' + item.nguoiGuiId : '' }}</span>
            </div>
          </td>
          <td>{{ item.trangThai || '-' }}</td>
          <td>{{ item.ngayTao || item.createdAt || '-' }}</td>
          <td class="desc-cell">{{ item.moTa || '-' }}</td>
          <td>
            <a
                v-if="item.id"
                :href="layLinkTaiTep(item.id)"
                target="_blank"
                rel="noopener"
            >
              Tải xuống
            </a>
          </td>
          <td v-if="laAdmin">
            <button type="button" class="btn small danger" @click="xoaTep(item)">
              Xóa
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

import { ROLES } from '@/core/constants/roles'
import { authService } from '@/core/services/authService'
import { heThongApi } from '../api/heThongApi'

const laAdmin = computed(() => authService.hasRole(ROLES.ADMIN))

const danhSach = ref([])
const dangTai = ref(false)
const loi = ref('')
const thongBao = ref('')
const trangHienTai = ref(0)
const tongBanGhi = ref(0)

const boLoc = reactive({
  module: '',
  nghiepVu: '',
  doiTuongId: '',
  nguoiGuiLoai: '',
  nguoiGuiId: ''
})

const formUpload = reactive({
  file: null,
  module: '',
  nghiepVu: '',
  doiTuongId: '',
  nguoiGuiLoai: '',
  nguoiGuiId: '',
  nguoiGuiTen: '',
  moTa: ''
})

onMounted(() => {
  taiDuLieu()
})

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''
  thongBao.value = ''

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

function chonFile(event) {
  formUpload.file = event.target.files?.[0] || null
}

async function uploadTep() {
  if (!laAdmin.value) {
    return
  }

  if (!formUpload.file) {
    loi.value = 'Chưa chọn file'
    return
  }

  const payload = {
    module: formUpload.module,
    nghiepVu: formUpload.nghiepVu,
    doiTuongId: formUpload.doiTuongId === '' ? null : Number(formUpload.doiTuongId),
    nguoiGuiLoai: formUpload.nguoiGuiLoai || null,
    nguoiGuiId: formUpload.nguoiGuiId === '' ? null : Number(formUpload.nguoiGuiId),
    nguoiGuiTen: formUpload.nguoiGuiTen,
    moTa: formUpload.moTa
  }

  try {
    await heThongApi.tepDinhKem.upload(formUpload.file, payload)
    thongBao.value = 'Upload tệp thành công'
    resetUpload()
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.response?.data?.message || error?.message || 'Upload tệp thất bại'
  }
}

async function xoaTep(item) {
  if (!laAdmin.value || !item?.id) {
    return
  }

  const ok = window.confirm(`Xóa tệp "${item.tenmau || item.tenFile || item.id}"?`)
  if (!ok) {
    return
  }

  try {
    await heThongApi.tepDinhKem.delete(item.id)
    thongBao.value = 'Xóa tệp thành công'
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.response?.data?.message || error?.message || 'Xóa tệp thất bại'
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

function resetUpload() {
  formUpload.file = null
  formUpload.module = ''
  formUpload.nghiepVu = ''
  formUpload.doiTuongId = ''
  formUpload.nguoiGuiLoai = ''
  formUpload.nguoiGuiId = ''
  formUpload.nguoiGuiTen = ''
  formUpload.moTa = ''
}

function layLinkTaiTep(id) {
  return heThongApi.tepDinhKem.downloadUrl(id)
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

.filter-card,
.upload-card {
  padding: 16px;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  background: #fff;
}

.filter-card {
  display: grid;
  grid-template-columns: repeat(5, minmax(160px, 1fr)) auto;
  gap: 12px;
}

.upload-card h2 {
  margin: 0 0 12px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(160px, 1fr));
  gap: 12px;
}

label {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

label span {
  font-size: 13px;
  font-weight: 700;
  color: #334155;
}

.field-full {
  grid-column: 1 / -1;
}

input,
select,
textarea {
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

.btn.small {
  padding: 6px 10px;
  font-size: 12px;
}

.btn.danger {
  color: #b91c1c;
  border-color: #fecaca;
  background: #fff5f5;
}

.alert {
  padding: 10px 12px;
  border-radius: 8px;
}

.alert.success {
  background: #ecfdf5;
  color: #047857;
}

.alert.error {
  background: #fee2e2;
  color: #991b1b;
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

.table-wrap {
  overflow: auto;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  background: #ffffff;
}

table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1300px;
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

.file-cell {
  display: grid;
  gap: 4px;
}

.file-cell span {
  color: #64748b;
  font-size: 12px;
}

.desc-cell {
  max-width: 280px;
  white-space: normal;
}

@media (max-width: 1200px) {
  .filter-card,
  .form-grid,
  .summary-grid {
    grid-template-columns: 1fr;
  }

  .filter-actions {
    align-items: stretch;
  }
}
</style>