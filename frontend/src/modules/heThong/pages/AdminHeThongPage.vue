<template>
  <section class="admin-he-thong">
    <header class="page-head">
      <div>
        <h1>Quản lý hệ thống</h1>
        <p>Quản lý tài khoản, vai trò, quyền, nhân viên, nhật ký và tệp đính kèm.</p>
      </div>

      <button type="button" class="btn" @click="taiDuLieu">
        Tải lại
      </button>
    </header>

    <div class="tabs">
      <button
          v-for="tab in tabsDuocXem"
          :key="tab.key"
          type="button"
          class="tab"
          :class="{ active: tab.key === tabDangChon }"
          @click="chonTab(tab.key)"
      >
        {{ tab.label }}
      </button>
    </div>

    <div v-if="thongBao" class="alert success">
      {{ thongBao }}
    </div>

    <div v-if="loi" class="alert error">
      {{ loi }}
    </div>

    <section v-if="tabHienTai" class="content-card">
      <div class="content-head">
        <div>
          <h2>{{ tabHienTai.label }}</h2>
          <p>{{ tabHienTai.moTa }}</p>
        </div>

        <div class="filter-box">
          <input
              v-model="tuKhoa"
              type="text"
              placeholder="Tìm kiếm"
              @keyup.enter="taiDuLieu"
          />

          <button type="button" class="btn" @click="taiDuLieu">
            Lọc
          </button>
        </div>
      </div>
      <div v-if="tabDangChon === 'tepDinhKem'" class="form-grid">
        <label class="field">
          <span>Lọc module</span>
          <input
              v-model="boLocTep.module"
              type="text"
              placeholder="sinh_vien, chuong_trinh, giang_day..."
              @keyup.enter="taiDuLieu"
          />
        </label>

        <label class="field">
          <span>Lọc nghiệp vụ</span>
          <input
              v-model="boLocTep.nghiepVu"
              type="text"
              placeholder="ho_so, bang_cap, syllabus..."
              @keyup.enter="taiDuLieu"
          />
        </label>

        <label class="field">
          <span>Đối tượng ID</span>
          <input
              v-model="boLocTep.doiTuongId"
              type="number"
              placeholder="ID bản ghi liên quan"
              @keyup.enter="taiDuLieu"
          />
        </label>

        <label class="field">
          <span>Người gửi loại</span>
          <select v-model="boLocTep.nguoiGuiLoai">
            <option value="">-- Tất cả --</option>
            <option value="TAI_KHOAN">TAI_KHOAN</option>
            <option value="SINH_VIEN">SINH_VIEN</option>
            <option value="GIAO_VIEN">GIAO_VIEN</option>
            <option value="NHAN_VIEN">NHAN_VIEN</option>
          </select>
        </label>

        <label class="field">
          <span>Người gửi ID</span>
          <input
              v-model="boLocTep.nguoiGuiId"
              type="number"
              placeholder="ID người gửi"
              @keyup.enter="taiDuLieu"
          />
        </label>

        <div class="form-actions">
          <button type="button" class="btn primary" @click="taiDuLieu">
            Lọc tệp
          </button>

          <button
              type="button"
              class="btn"
              @click="boLocTep.module = ''; boLocTep.nghiepVu = ''; boLocTep.doiTuongId = ''; boLocTep.nguoiGuiLoai = ''; boLocTep.nguoiGuiId = ''; taiDuLieu()"
          >
            Xóa lọc
          </button>
        </div>
      </div>
      <form
          v-if="duocSuaTab && tabHienTai.fields.length"
          class="form-grid"
          @submit.prevent="luuDuLieu"
      >
        <label
            v-for="field in tabHienTai.fields"
            :key="field.name"
            class="field"
        >
          <span>{{ field.label }}</span>

          <select
              v-if="field.type === 'select'"
              v-model="form[field.name]"
          >
            <option value="">-- Chọn --</option>
            <option
                v-for="option in field.options"
                :key="option.value"
                :value="option.value"
            >
              {{ option.label }}
            </option>
          </select>

          <textarea
              v-else-if="field.type === 'textarea'"
              v-model="form[field.name]"
              rows="2"
          />

          <input
              v-else
              v-model="form[field.name]"
              :type="field.type || 'text'"
          />
        </label>

        <div class="form-actions">
          <button type="submit" class="btn primary">
            {{ idDangSua ? 'Cập nhật' : 'Thêm mới' }}
          </button>

          <button type="button" class="btn" @click="resetForm">
            Làm mới
          </button>
        </div>
      </form>

      <form
          v-if="tabDangChon === 'tepDinhKem' && laAdmin"
          class="form-grid"
          @submit.prevent="uploadTep"
      >
        <label class="field">
          <span>File</span>
          <input type="file" @change="chonFile" />
        </label>

        <label class="field">
          <span>Module</span>
          <input v-model="formUpload.module" type="text" />
        </label>

        <label class="field">
          <span>Nghiệp vụ</span>
          <input v-model="formUpload.nghiepVu" type="text" />
        </label>

        <label class="field">
          <span>Đối tượng ID</span>
          <input v-model="formUpload.doiTuongId" type="number" />
        </label>

        <label class="field">
          <span>Người gửi loại</span>
          <select v-model="formUpload.nguoiGuiLoai">
            <option value="">-- Chọn --</option>
            <option value="TAI_KHOAN">TAI_KHOAN</option>
            <option value="SINH_VIEN">SINH_VIEN</option>
            <option value="GIAO_VIEN">GIAO_VIEN</option>
            <option value="NHAN_VIEN">NHAN_VIEN</option>
          </select>
        </label>

        <label class="field">
          <span>Người gửi ID</span>
          <input v-model="formUpload.nguoiGuiId" type="number" />
        </label>

        <label class="field">
          <span>Người gửi tên</span>
          <input v-model="formUpload.nguoiGuiTen" type="text" />
        </label>

        <label class="field field-full">
          <span>Mô tả</span>
          <textarea v-model="formUpload.moTa" rows="2" />
        </label>

        <div class="form-actions">
          <button type="submit" class="btn primary">
            Upload
          </button>
        </div>
      </form>

      <div class="table-wrap">
        <table>
          <thead>
          <tr>
            <th
                v-for="col in tabHienTai.columns"
                :key="col.key"
            >
              {{ col.label }}
            </th>
            <th v-if="tabDangChon === 'tepDinhKem'">
              Tải
            </th>
            <th v-if="duocSuaTab || tabDangChon === 'tepDinhKem'">
              Thao tác
            </th>
          </tr>
          </thead>

          <tbody>
          <tr v-if="dangTai">
            <td :colspan="soCot">
              Đang tải dữ liệu...
            </td>
          </tr>

          <tr v-else-if="!duLieu.length">
            <td :colspan="soCot">
              Chưa có dữ liệu
            </td>
          </tr>

          <tr
              v-for="row in duLieu"
              v-else
              :key="row.id"
          >
            <td
                v-for="col in tabHienTai.columns"
                :key="col.key"
            >
              {{ hienThi(row, col.key) }}
            </td>

            <td v-if="tabDangChon === 'tepDinhKem'">
              <a
                  v-if="row.id"
                  :href="layLinkTaiTep(row.id)"
                  target="_blank"
                  rel="noopener"
              >
                Tải xuống
              </a>
            </td>

            <td v-if="duocSuaTab || tabDangChon === 'tepDinhKem'" class="actions">
              <button
                  v-if="duocSuaTab"
                  type="button"
                  class="btn small"
                  @click="suaDong(row)"
              >
                Sửa
              </button>

              <button
                  v-if="duocXoaTab"
                  type="button"
                  class="btn small danger"
                  @click="xoaDong(row)"
              >
                Xóa
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </section>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'

import { ROLES } from '@/core/constants/roles'
import { authService } from '@/core/services/authService'
import { heThongApi } from '../api/heThongApi'

const laAdmin = computed(() => authService.hasRole(ROLES.ADMIN))
const laDaoTao = computed(() => authService.hasRole(ROLES.DAO_TAO))

const tabs = [
  {
    key: 'taiKhoan',
    label: 'Tài khoản',
    moTa: 'Quản lý tài khoản đăng nhập hệ thống.',
    api: heThongApi.taiKhoan,
    page: true,
    roles: [ROLES.ADMIN],
    columns: [
      { key: 'id', label: 'ID' },
      { key: 'email', label: 'Email' },
      { key: 'loaiTaiKhoan', label: 'Loại tài khoản' },
      { key: 'trangThai', label: 'Trạng thái' },
      { key: 'roles', label: 'Vai trò' },
      { key: 'permissions', label: 'Quyền' }
    ],
    fields: [
      { name: 'email', label: 'Email', type: 'text' },
      { name: 'matKhau', label: 'Mật khẩu', type: 'password' },
      {
        name: 'loaiTaiKhoan',
        label: 'Loại tài khoản',
        type: 'select',
        options: [
          { label: 'admin', value: 'admin' },
          { label: 'nhan_vien', value: 'nhan_vien' },
          { label: 'giao_vien', value: 'giao_vien' },
          { label: 'sinh_vien', value: 'sinh_vien' }
        ]
      },
      {
        name: 'trangThai',
        label: 'Trạng thái',
        type: 'select',
        options: [
          { label: 'cho_kich_hoat', value: 'cho_kich_hoat' },
          { label: 'da_kich_hoat', value: 'da_kich_hoat' },
          { label: 'bi_khoa', value: 'bi_khoa' }
        ]
      }
    ]
  },
  {
    key: 'vaiTro',
    label: 'Vai trò',
    moTa: 'Quản lý vai trò dùng để phân quyền tài khoản.',
    api: heThongApi.vaiTro,
    page: true,
    roles: [ROLES.ADMIN],
    columns: [
      { key: 'id', label: 'ID' },
      { key: 'maVaiTro', label: 'Mã vai trò' },
      { key: 'tenVaiTro', label: 'Tên vai trò' },
      { key: 'moTa', label: 'Mô tả' }
    ],
    fields: [
      { name: 'maVaiTro', label: 'Mã vai trò', type: 'text' },
      { name: 'tenVaiTro', label: 'Tên vai trò', type: 'text' },
      { name: 'moTa', label: 'Mô tả', type: 'textarea' }
    ]
  },
  {
    key: 'quyen',
    label: 'Quyền',
    moTa: 'Quản lý quyền chi tiết trong hệ thống.',
    api: heThongApi.quyen,
    page: true,
    roles: [ROLES.ADMIN],
    columns: [
      { key: 'id', label: 'ID' },
      { key: 'maQuyen', label: 'Mã quyền' },
      { key: 'tenQuyen', label: 'Tên quyền' },
      { key: 'moTa', label: 'Mô tả' }
    ],
    fields: [
      { name: 'maQuyen', label: 'Mã quyền', type: 'text' },
      { name: 'tenQuyen', label: 'Tên quyền', type: 'text' },
      { name: 'moTa', label: 'Mô tả', type: 'textarea' }
    ]
  },
  {
    key: 'taiKhoanVaiTro',
    label: 'Gán vai trò',
    moTa: 'Gán vai trò cho tài khoản.',
    api: heThongApi.taiKhoanVaiTro,
    page: true,
    roles: [ROLES.ADMIN],
    columns: [
      { key: 'id', label: 'ID' },
      { key: 'taiKhoanId', label: 'Tài khoản ID' },
      { key: 'vaiTroId', label: 'Vai trò ID' }
    ],
    fields: [
      { name: 'taiKhoanId', label: 'Tài khoản ID', type: 'number' },
      { name: 'vaiTroId', label: 'Vai trò ID', type: 'number' }
    ]
  },
  {
    key: 'vaiTroQuyen',
    label: 'Gán quyền',
    moTa: 'Gán quyền cho vai trò.',
    api: heThongApi.vaiTroQuyen,
    page: true,
    roles: [ROLES.ADMIN],
    columns: [
      { key: 'id', label: 'ID' },
      { key: 'vaiTroId', label: 'Vai trò ID' },
      { key: 'quyenId', label: 'Quyền ID' }
    ],
    fields: [
      { name: 'vaiTroId', label: 'Vai trò ID', type: 'number' },
      { name: 'quyenId', label: 'Quyền ID', type: 'number' }
    ]
  },
  {
    key: 'nhanVien',
    label: 'Nhân viên',
    moTa: 'Quản lý nhân viên liên kết với tài khoản.',
    api: heThongApi.nhanVien,
    page: true,
    roles: [ROLES.ADMIN],
    columns: [
      { key: 'id', label: 'ID' },
      { key: 'taiKhoanId', label: 'Tài khoản ID' },
      { key: 'maNhanVien', label: 'Mã nhân viên' },
      { key: 'hoTen', label: 'Họ tên' },
      { key: 'email', label: 'Email' },
      { key: 'soDienThoai', label: 'SĐT' },
      { key: 'phongBan', label: 'Phòng ban' },
      { key: 'chucVu', label: 'Chức vụ' },
      { key: 'trangThai', label: 'Trạng thái' }
    ],
    fields: [
      { name: 'taiKhoanId', label: 'Tài khoản ID', type: 'number' },
      { name: 'maNhanVien', label: 'Mã nhân viên', type: 'text' },
      { name: 'hoTen', label: 'Họ tên', type: 'text' },
      { name: 'email', label: 'Email', type: 'text' },
      { name: 'soDienThoai', label: 'SĐT', type: 'text' },
      { name: 'phongBan', label: 'Phòng ban', type: 'text' },
      { name: 'chucVu', label: 'Chức vụ', type: 'text' },
      {
        name: 'trangThai',
        label: 'Trạng thái',
        type: 'select',
        options: [
          { label: 'dang_lam', value: 'dang_lam' },
          { label: 'tam_nghi', value: 'tam_nghi' },
          { label: 'nghi_viec', value: 'nghi_viec' }
        ]
      }
    ]
  },
  {
    key: 'nhatKyDangNhap',
    label: 'Nhật ký đăng nhập',
    moTa: 'Admin và Đào tạo được xem nhật ký đăng nhập.',
    api: heThongApi.nhatKyDangNhap,
    page: false,
    readOnly: true,
    roles: [ROLES.ADMIN, ROLES.DAO_TAO],
    columns: [
      { key: 'id', label: 'ID' },
      { key: 'taiKhoanId', label: 'Tài khoản ID' },
      { key: 'thoiGian', label: 'Thời gian' },
      { key: 'ipAddress', label: 'IP' },
      { key: 'userAgent', label: 'User-Agent' },
      { key: 'thanhCong', label: 'Thành công' },
      { key: 'lyDoThatBai', label: 'Lý do thất bại' }
    ],
    fields: []
  },
  {
    key: 'nhatKyHeThong',
    label: 'Nhật ký hệ thống',
    moTa: 'Admin và Đào tạo được xem nhật ký thao tác hệ thống.',
    api: heThongApi.nhatKyHeThong,
    page: false,
    readOnly: true,
    roles: [ROLES.ADMIN, ROLES.DAO_TAO],
    columns: [
      { key: 'id', label: 'ID' },
      { key: 'taiKhoanId', label: 'Tài khoản ID' },
      { key: 'hanhDong', label: 'Hành động' },
      { key: 'bangTacDong', label: 'Bảng tác động' },
      { key: 'banGhiId', label: 'Bản ghi ID' },
      { key: 'ipAddress', label: 'IP' },
      { key: 'userAgent', label: 'User-Agent' },
      { key: 'createdAt', label: 'Thời gian' }
    ],
    fields: []
  },
  {
    key: 'tepDinhKem',
    label: 'Tệp đính kèm',
    moTa: 'Quản lý file đính kèm dùng chung.',
    api: heThongApi.tepDinhKem,
    page: false,
    readOnly: false,
    roles: [ROLES.ADMIN, ROLES.DAO_TAO],
    columns: [
      { key: 'id', label: 'ID' },
      { key: 'tenGoc', label: 'Tên gốc' },
      { key: 'module', label: 'Module' },
      { key: 'nghiepVu', label: 'Nghiệp vụ' },
      { key: 'doiTuongId', label: 'Đối tượng ID' },
      { key: 'nguoiGuiLoai', label: 'Người gửi loại' },
      { key: 'nguoiGuiId', label: 'Người gửi ID' },
      { key: 'nguoiGuiTen', label: 'Người gửi tên' },
      { key: 'trangThai', label: 'Trạng thái' },
      { key: 'ngayTao', label: 'Ngày tạo' }
    ],
    fields: []
  }
]

const tabDangChon = ref('')
const duLieu = ref([])
const dangTai = ref(false)
const loi = ref('')
const thongBao = ref('')
const tuKhoa = ref('')
const idDangSua = ref(null)
const form = reactive({})

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
const boLocTep = reactive({
  module: '',
  nghiepVu: '',
  doiTuongId: '',
  nguoiGuiLoai: '',
  nguoiGuiId: ''
})
const tabsDuocXem = computed(() => {
  return tabs.filter((tab) => {
    if (laAdmin.value) {
      return true
    }

    if (laDaoTao.value) {
      return tab.roles.includes(ROLES.DAO_TAO)
    }

    return false
  })
})

const tabHienTai = computed(() => {
  return tabs.find((item) => item.key === tabDangChon.value)
})

const duocSuaTab = computed(() => {
  if (!tabHienTai.value) {
    return false
  }

  if (tabHienTai.value.readOnly) {
    return false
  }

  return laAdmin.value
})

const duocXoaTab = computed(() => {
  if (tabDangChon.value === 'tepDinhKem') {
    return laAdmin.value
  }

  return duocSuaTab.value
})

const soCot = computed(() => {
  let total = tabHienTai.value?.columns.length || 1

  if (tabDangChon.value === 'tepDinhKem') {
    total += 1
  }

  if (duocSuaTab.value || tabDangChon.value === 'tepDinhKem') {
    total += 1
  }

  return total
})

onMounted(() => {
  tabDangChon.value = tabsDuocXem.value[0]?.key || ''
  resetForm()
  taiDuLieu()
})

function chonTab(key) {
  tabDangChon.value = key
  tuKhoa.value = ''
  resetForm()
  taiDuLieu()
}

async function taiDuLieu() {
  if (!tabHienTai.value) {
    return
  }

  dangTai.value = true
  loi.value = ''
  thongBao.value = ''

  try {
    const api = tabHienTai.value.api

    let result

    if (tabDangChon.value === 'tepDinhKem') {
      const params = {
        page: 0,
        size: 100
      }

      if (boLocTep.module) params.module = boLocTep.module
      if (boLocTep.nghiepVu) params.nghiepVu = boLocTep.nghiepVu

      if (boLocTep.doiTuongId) {
        result = await api.getByDoiTuong({
          module: boLocTep.module,
          nghiepVu: boLocTep.nghiepVu,
          doiTuongId: Number(boLocTep.doiTuongId),
          page: 0,
          size: 100
        })
      } else if (boLocTep.nguoiGuiLoai && boLocTep.nguoiGuiId) {
        result = await api.getByNguoiGui({
          nguoiGuiLoai: boLocTep.nguoiGuiLoai,
          nguoiGuiId: Number(boLocTep.nguoiGuiId),
          page: 0,
          size: 100
        })
      } else {
        result = await api.getAll(params)
      }
    } else if (tabHienTai.value.page) {
      result = await api.getAll({
        keyword: tuKhoa.value || undefined,
        page: 0,
        size: 100
      })
    } else {
      result = await api.getAll({
        page: 0,
        size: 100
      })
    }

    duLieu.value = layDanhSach(result)
  } catch (error) {
    loi.value = error?.message || 'Không tải được dữ liệu'
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

async function luuDuLieu() {
  if (!tabHienTai.value || !duocSuaTab.value) {
    return
  }

  loi.value = ''
  thongBao.value = ''

  try {
    const payload = taoPayload()

    if (idDangSua.value) {
      await tabHienTai.value.api.update(idDangSua.value, payload)
      thongBao.value = 'Cập nhật thành công'
    } else {
      await tabHienTai.value.api.create(payload)
      thongBao.value = 'Thêm mới thành công'
    }

    resetForm()
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Lưu dữ liệu thất bại'
  }
}

function taoPayload() {
  const payload = {}

  tabHienTai.value.fields.forEach((field) => {
    const value = form[field.name]

    if (field.type === 'number') {
      payload[field.name] = value === '' || value === null || value === undefined
          ? null
          : Number(value)
    } else {
      payload[field.name] = value
    }
  })

  return payload
}

function suaDong(row) {
  if (!duocSuaTab.value) {
    return
  }

  idDangSua.value = row.id

  tabHienTai.value.fields.forEach((field) => {
    form[field.name] = row[field.name] ?? ''
  })

  if (tabDangChon.value === 'taiKhoan') {
    form.matKhau = ''
  }
}

async function xoaDong(row) {
  if (!duocXoaTab.value) {
    return
  }

  const dongY = window.confirm(`Xóa bản ghi ID ${row.id}?`)

  if (!dongY) {
    return
  }

  loi.value = ''
  thongBao.value = ''

  try {
    await tabHienTai.value.api.delete(row.id)
    thongBao.value = 'Xóa thành công'
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Xóa thất bại'
  }
}

function resetForm() {
  idDangSua.value = null

  Object.keys(form).forEach((key) => {
    delete form[key]
  })

  if (!tabHienTai.value) {
    return
  }

  tabHienTai.value.fields.forEach((field) => {
    form[field.name] = ''
  })
}

function hienThi(row, key) {
  const value = row?.[key]

  if (Array.isArray(value)) {
    return value.join(', ')
  }

  if (typeof value === 'boolean') {
    return value ? 'Có' : 'Không'
  }

  if (value === null || value === undefined || value === '') {
    return '-'
  }

  return value
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

  loi.value = ''
  thongBao.value = ''

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
    thongBao.value = 'Upload thành công'

    formUpload.file = null
    formUpload.module = ''
    formUpload.nghiepVu = ''
    formUpload.doiTuongId = ''
    formUpload.nguoiGuiLoai = ''
    formUpload.nguoiGuiId = ''
    formUpload.nguoiGuiTen = ''
    formUpload.moTa = ''

    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Upload thất bại'
  }
}

function layLinkTaiTep(id) {
  return heThongApi.tepDinhKem.downloadUrl(id)
}
</script>

<style scoped>
.admin-he-thong {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-head,
.content-head {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: flex-start;
}

.page-head h1,
.content-head h2 {
  margin: 0;
}

.page-head p,
.content-head p {
  margin: 6px 0 0;
  color: #64748b;
}

.tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tab,
.btn {
  border: 1px solid #cbd5e1;
  background: #fff;
  color: #0f172a;
  border-radius: 8px;
  padding: 8px 12px;
  cursor: pointer;
}

.tab.active,
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

.content-card {
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 16px;
}

.filter-box {
  display: flex;
  gap: 8px;
}

.filter-box input,
.field input,
.field select,
.field textarea {
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  padding: 8px 10px;
  width: 100%;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
  margin: 16px 0;
  padding: 12px;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  background: #f8fafc;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.field span {
  font-size: 13px;
  font-weight: 600;
  color: #334155;
}

.field-full {
  grid-column: 1 / -1;
}

.form-actions {
  grid-column: 1 / -1;
  display: flex;
  gap: 8px;
}

.table-wrap {
  overflow: auto;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
}

table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1000px;
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
</style>