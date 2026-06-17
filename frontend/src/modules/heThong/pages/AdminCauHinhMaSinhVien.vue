<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Cấu hình mã sinh viên</h1>
        <p>Cấu hình tiền tố và mã đầu theo ngành + version chương trình.</p>
      </div>

      <button type="button" class="btn compact" @click="taiDuLieu" :disabled="dangTai">
        <span v-if="dangTai" class="spinner"></span>
        {{ dangTai ? 'Đang tải...' : 'Tải lại' }}
      </button>
    </header>

    <div v-if="thongBao" class="alert success">
      {{ thongBao }}
    </div>

    <div v-if="loi" class="alert error">
      {{ loi }}
    </div>

    <form class="config-card" @submit.prevent="luu">
      <div class="config-toolbar">
        <div>
          <h2>{{ idDangSua ? 'Cập nhật cấu hình' : 'Thêm cấu hình mã sinh viên' }}</h2>
          <p>Chọn ngành, chương trình, version và thông số tạo mã.</p>
        </div>

        <div class="preview-box" :class="{ warning: isWarningLength }">
          <span>Mã tiếp theo</span>
          <strong>{{ previewCode }}</strong>
        </div>
      </div>

      <div class="config-grid">
        <label>
          <span>Ngành <b class="required">*</b></span>
          <select v-model="form.nganhId" required @change="doiNganh" :disabled="isSaving">
            <option value="">-- Chọn ngành --</option>
            <option v-for="item in nganhList" :key="item.id" :value="item.id">
              {{ item.maNganh }} - {{ item.tenNganh }}
            </option>
          </select>
        </label>

        <label>
          <span>Chương trình</span>
          <select v-model="form.chuongTrinhId" @change="doiChuongTrinh" :disabled="isSaving">
            <option value="">-- Chọn chương trình --</option>
            <option v-for="item in chuongTrinhTheoNganh" :key="item.id" :value="item.id">
              {{ item.maChuongTrinh }} - {{ item.tenChuongTrinh }}
            </option>
          </select>
        </label>

        <label>
          <span>Version <b class="required">*</b></span>
          <select v-model="form.chuongTrinhVersionId" required :disabled="isSaving">
            <option value="">-- Chọn version --</option>
            <option v-for="item in versionTheoChuongTrinh" :key="item.id" :value="item.id">
              {{ item.maVersion }} - {{ item.tenVersion }}
            </option>
          </select>
        </label>

        <label class="small-field">
          <span>Tiền tố <b class="required">*</b></span>
          <input
              v-model="form.tienTo"
              type="text"
              required
              placeholder="PN"
              :disabled="isSaving"
              @input="form.tienTo = $event.target.value.toUpperCase()"
          />
        </label>

        <label class="small-field">
          <span>Mã đầu <b class="required">*</b></span>
          <input
              v-model="form.maDau"
              type="text"
              required
              placeholder="60"
              :disabled="isSaving"
          />
        </label>

        <label class="small-field">
          <span>Số hiện tại</span>
          <input
              v-model.number="form.soHienTai"
              type="number"
              min="0"
              :disabled="isSaving"
          />
        </label>

        <label class="small-field">
          <span>Độ dài STT</span>
          <input
              v-model.number="form.doDaiSoThuTu"
              type="number"
              min="1"
              max="10"
              :disabled="isSaving"
          />
        </label>

        <label class="note-field">
          <span>Ghi chú</span>
          <input
              v-model="form.ghiChu"
              type="text"
              placeholder="Nhập ghi chú nếu cần..."
              :disabled="isSaving"
          />
        </label>

        <div class="form-actions">
          <button type="submit" class="btn primary" :disabled="isSaving || !isValidForm">
            {{ isSaving ? 'Đang lưu...' : (idDangSua ? 'Cập nhật' : 'Thêm') }}
          </button>

          <button type="button" class="btn" @click="resetForm" :disabled="isSaving">
            Làm mới
          </button>
        </div>
      </div>

      <p v-if="isWarningLength" class="warning-text">
        Số thứ tự tiếp theo đã vượt độ dài số thứ tự đang cấu hình.
      </p>
    </form>

    <section class="table-card">
      <div class="table-toolbar">
        <div class="table-search">
          <input
              v-model="searchQuery"
              type="text"
              placeholder="Tìm ngành, version, tiền tố, mã đầu..."
          />
        </div>

        <div class="table-filter">
          <select v-model="filterNganh">
            <option value="">Tất cả ngành</option>
            <option v-for="item in nganhList" :key="item.id" :value="item.id">
              {{ item.maNganh }} - {{ item.tenNganh }}
            </option>
          </select>
        </div>

        <div class="table-stats">
          <span>Trang <strong>{{ page + 1 }} / {{ totalPages || 1 }}</strong></span>
          <span>Tổng <strong>{{ totalElements }}</strong></span>
          <span>Hiển thị <strong>{{ filteredDanhSach.length }}</strong></span>
        </div>

        <div class="table-actions">
          <button type="button" class="btn compact" :disabled="page <= 0 || dangTai" @click="doiTrang(page - 1)">
            Trước
          </button>

          <button type="button" class="btn compact" :disabled="page + 1 >= totalPages || dangTai" @click="doiTrang(page + 1)">
            Sau
          </button>
        </div>
      </div>

      <div class="table-scroll">
        <table>
          <thead>
          <tr>
            <th>Ngành / Version</th>
            <th class="center">Tiền tố</th>
            <th class="center">Mã đầu</th>
            <th class="center">Số hiện tại</th>
            <th>Mã tiếp theo</th>
            <th>Ghi chú</th>
            <th class="right">Thao tác</th>
          </tr>
          </thead>

          <tbody>
          <tr v-if="dangTai">
            <td colspan="7" class="empty-cell">
              Đang tải dữ liệu...
            </td>
          </tr>

          <tr v-else-if="!danhSach.length">
            <td colspan="7" class="empty-cell">
              Chưa có cấu hình mã sinh viên.
            </td>
          </tr>

          <tr v-else-if="!filteredDanhSach.length">
            <td colspan="7" class="empty-cell">
              Không tìm thấy cấu hình phù hợp trong trang hiện tại.
            </td>
          </tr>

          <tr
              v-for="item in filteredDanhSach"
              v-else
              :key="item.id"
              :class="{ 'row-highlight': item.id === highlightedRowId }"
          >
            <td>
              <div class="cell-title">
                {{ item.maNganh }} - {{ item.tenNganh }}
              </div>
              <div class="cell-sub">
                {{ item.maVersion }} - {{ item.tenVersion }}
              </div>
            </td>

            <td class="center">
              <span class="badge">{{ item.tienTo }}</span>
            </td>

            <td class="center">
              <span class="badge muted">{{ item.maDau }}</span>
            </td>

            <td class="center">
              {{ item.soHienTai }}
            </td>

            <td>
              <span class="code-text">
                {{ item.maSinhVienTiepTheo || taoMaPreviewChoBang(item) }}
              </span>
            </td>

            <td class="note-cell">
              {{ item.ghiChu || '-' }}
            </td>

            <td class="actions right">
              <button type="button" class="btn small" @click="sua(item)">
                Sửa
              </button>

              <button type="button" class="btn small danger" @click="xacNhanXoa(item)">
                Xóa
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </section>

    <div v-if="showDeleteModal" class="modal-overlay" @click.self="dongModalXoa">
      <div class="modal">
        <div class="modal-head">
          <h3>Xác nhận xóa cấu hình</h3>
          <button type="button" class="modal-close" @click="dongModalXoa">×</button>
        </div>

        <div class="modal-body">
          <p>Bạn có chắc chắn muốn xóa cấu hình mã sinh viên này?</p>

          <div class="delete-info">
            <div>
              <span>Ngành</span>
              <strong>{{ itemDangXoa?.maNganh }} - {{ itemDangXoa?.tenNganh }}</strong>
            </div>

            <div>
              <span>Version</span>
              <strong>{{ itemDangXoa?.maVersion }} - {{ itemDangXoa?.tenVersion }}</strong>
            </div>

            <div>
              <span>Mã tiếp theo</span>
              <strong>{{ itemDangXoa?.maSinhVienTiepTheo || taoMaPreviewChoBang(itemDangXoa || {}) }}</strong>
            </div>
          </div>

          <p class="delete-warning">Hành động này không thể hoàn tác.</p>
        </div>

        <div class="modal-actions">
          <button type="button" class="btn" @click="dongModalXoa" :disabled="isSaving">
            Hủy
          </button>

          <button type="button" class="btn danger-filled" @click="xoaTuModal" :disabled="isSaving">
            {{ isSaving ? 'Đang xóa...' : 'Xóa cấu hình' }}
          </button>
        </div>
      </div>
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

const dangTai = ref(false)
const isSaving = ref(false)
const loi = ref('')
const thongBao = ref('')
const idDangSua = ref(null)
const highlightedRowId = ref(null)

const page = ref(0)
const size = ref(20)
const totalPages = ref(0)
const totalElements = ref(0)

const searchQuery = ref('')
const filterNganh = ref('')

const showDeleteModal = ref(false)
const itemDangXoa = ref(null)

const form = reactive({
  nganhId: '',
  chuongTrinhId: '',
  chuongTrinhVersionId: '',
  tienTo: 'PN',
  maDau: '',
  soHienTai: 0,
  doDaiSoThuTu: 3,
  ghiChu: ''
})

const chuongTrinhTheoNganh = computed(() => {
  if (!form.nganhId) return chuongTrinhList.value
  return chuongTrinhList.value.filter((item) => Number(item.nganhId) === Number(form.nganhId))
})

const versionTheoChuongTrinh = computed(() => {
  if (!form.chuongTrinhId) return versionList.value
  return versionList.value.filter((item) => Number(item.chuongTrinhId) === Number(form.chuongTrinhId))
})

const previewCode = computed(() => {
  if (!form.tienTo || !form.maDau) return 'Nhập tiền tố và mã đầu'

  const nextNum = (Number(form.soHienTai || 0) + 1).toString()
  const paddingLength = Number(form.doDaiSoThuTu || 1)
  const paddedNum = nextNum.padStart(paddingLength, '0')

  return `${form.tienTo || ''}${form.maDau || ''}${paddedNum}`
})

const isWarningLength = computed(() => {
  const nextNum = (Number(form.soHienTai || 0) + 1).toString()
  const paddingLength = Number(form.doDaiSoThuTu || 1)
  return nextNum.length > paddingLength
})

const isValidForm = computed(() => {
  return (
      form.nganhId &&
      form.chuongTrinhVersionId &&
      form.tienTo &&
      form.maDau &&
      Number(form.soHienTai || 0) >= 0 &&
      Number(form.doDaiSoThuTu || 1) >= 1
  )
})

const filteredDanhSach = computed(() => {
  const keyword = searchQuery.value.trim().toLowerCase()

  return danhSach.value.filter((item) => {
    const matchSearch = !keyword || [
      item.maNganh,
      item.tenNganh,
      item.maVersion,
      item.tenVersion,
      item.tienTo,
      item.maDau,
      item.maSinhVienTiepTheo,
      item.ghiChu
    ].some((value) => String(value || '').toLowerCase().includes(keyword))

    const matchNganh = filterNganh.value
        ? Number(item.nganhId) === Number(filterNganh.value)
        : true

    return matchSearch && matchNganh
  })
})

onMounted(() => {
  taiDuLieu()
})

async function taiDuLieu() {
  loi.value = ''
  dangTai.value = true

  try {
    const [nganhResult, chuongTrinhResult, versionResult, cauHinhResult] = await Promise.all([
      daoTaoApi.nganh.getAll({ page: 0, size: 1000 }),
      daoTaoApi.chuongTrinh.getAll({ page: 0, size: 1000 }),
      daoTaoApi.chuongTrinhVersion.getAll({ page: 0, size: 1000 }),
      heThongApi.cauHinhMaSinhVien.getAll({ page: page.value, size: size.value })
    ])

    nganhList.value = layDanhSach(nganhResult)
    chuongTrinhList.value = layDanhSach(chuongTrinhResult)
    versionList.value = layDanhSach(versionResult)
    setPageData(cauHinhResult)
  } catch (error) {
    loi.value = error?.message || 'Không tải được dữ liệu cấu hình mã sinh viên'
  } finally {
    dangTai.value = false
  }
}

function setPageData(result) {
  danhSach.value = layDanhSach(result)
  totalPages.value = result?.totalPages || 0
  totalElements.value = result?.totalElements || danhSach.value.length
}

function layDanhSach(result) {
  if (Array.isArray(result)) return result
  if (Array.isArray(result?.content)) return result.content
  if (Array.isArray(result?.data)) return result.data
  return []
}

function taoMaPreviewChoBang(item) {
  if (!item) return '-'

  const nextNum = (Number(item.soHienTai || 0) + 1).toString()
  const paddingLength = Number(item.doDaiSoThuTu || 1)
  const paddedNum = nextNum.padStart(paddingLength, '0')

  return `${item.tienTo || ''}${item.maDau || ''}${paddedNum}`
}

async function luu() {
  loi.value = ''
  thongBao.value = ''
  isSaving.value = true

  const payload = {
    nganhId: Number(form.nganhId),
    chuongTrinhVersionId: Number(form.chuongTrinhVersionId),
    tienTo: form.tienTo,
    maDau: form.maDau,
    soHienTai: Number(form.soHienTai || 0),
    doDaiSoThuTu: Number(form.doDaiSoThuTu || 3),
    ghiChu: form.ghiChu
  }

  try {
    let savedId = idDangSua.value

    if (idDangSua.value) {
      await heThongApi.cauHinhMaSinhVien.update(idDangSua.value, payload)
      thongBao.value = 'Cập nhật cấu hình thành công'
    } else {
      const result = await heThongApi.cauHinhMaSinhVien.create(payload)
      thongBao.value = 'Thêm cấu hình thành công'
      savedId = result?.id || result?.data?.id || null
    }

    resetForm()
    await taiDuLieu()

    if (savedId) {
      highlightedRowId.value = savedId
      setTimeout(() => {
        if (highlightedRowId.value === savedId) highlightedRowId.value = null
      }, 3000)
    }
  } catch (error) {
    loi.value = error?.message || 'Lưu cấu hình thất bại'
  } finally {
    isSaving.value = false
  }
}

function sua(item) {
  idDangSua.value = item.id
  form.nganhId = item.nganhId
  form.chuongTrinhVersionId = item.chuongTrinhVersionId
  form.tienTo = item.tienTo
  form.maDau = item.maDau
  form.soHienTai = item.soHienTai
  form.doDaiSoThuTu = item.doDaiSoThuTu
  form.ghiChu = item.ghiChu || ''

  const version = versionList.value.find((v) => Number(v.id) === Number(item.chuongTrinhVersionId))
  form.chuongTrinhId = version?.chuongTrinhId || ''

  window.scrollTo({ top: 0, behavior: 'smooth' })
}

function xacNhanXoa(item) {
  itemDangXoa.value = item
  showDeleteModal.value = true
}

function dongModalXoa() {
  if (isSaving.value) return
  showDeleteModal.value = false
  itemDangXoa.value = null
}

async function xoaTuModal() {
  if (!itemDangXoa.value) return
  await xoa(itemDangXoa.value)
  showDeleteModal.value = false
  itemDangXoa.value = null
}

async function xoa(item) {
  try {
    isSaving.value = true
    loi.value = ''
    thongBao.value = ''

    await heThongApi.cauHinhMaSinhVien.delete(item.id)
    thongBao.value = 'Xóa cấu hình thành công'
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Xóa cấu hình thất bại'
  } finally {
    isSaving.value = false
  }
}

function resetForm() {
  idDangSua.value = null
  form.nganhId = ''
  form.chuongTrinhId = ''
  form.chuongTrinhVersionId = ''
  form.tienTo = 'PN'
  form.maDau = ''
  form.soHienTai = 0
  form.doDaiSoThuTu = 3
  form.ghiChu = ''
}

function doiNganh() {
  form.chuongTrinhId = ''
  form.chuongTrinhVersionId = ''
}

function doiChuongTrinh() {
  form.chuongTrinhVersionId = ''
}

function doiTrang(newPage) {
  page.value = newPage
  taiDuLieu()
}
</script>

<style scoped>
.page {
  --primary: #077149;
  --primary-hover: #055436;
  --primary-light: #e6f1ec;
  --primary-soft: #f3faf7;
  --primary-glow: rgba(7, 113, 73, 0.16);

  --danger: #dc2626;
  --danger-hover: #b91c1c;
  --danger-light: #fef2f2;
  --danger-border: #fecaca;

  --success-light: #dcfce7;
  --success-border: #bbf7d0;

  --warning: #d97706;
  --warning-light: #fff7ed;

  --text-main: #111827;
  --text-normal: #374151;
  --text-muted: #64748b;

  --bg-card: #ffffff;
  --bg-soft: #f8fafc;

  --border-color: #e5e7eb;
  --border-strong: #d1d5db;

  --radius: 10px;
  --shadow: 0 1px 2px rgba(15, 23, 42, 0.05);

  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 100%;
  max-width: 1560px;
  margin: 0 auto;
  padding: 10px 14px;
  box-sizing: border-box;
  color: var(--text-main);
  font-family: Roboto, system-ui, -apple-system, BlinkMacSystemFont, sans-serif;
}

.page-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  padding: 10px 14px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-top: 3px solid var(--primary);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
}

.page-head h1 {
  margin: 0;
  color: var(--primary);
  font-size: 19px;
  font-weight: 700;
}

.page-head p {
  margin: 2px 0 0;
  color: var(--text-muted);
  font-size: 12.5px;
}

.alert {
  padding: 8px 11px;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
}

.alert.success {
  background: var(--success-light);
  color: #166534;
  border: 1px solid var(--success-border);
}

.alert.error {
  background: var(--danger-light);
  color: #991b1b;
  border: 1px solid var(--danger-border);
}

.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  min-height: 31px;
  border: 1px solid var(--border-strong);
  border-radius: 7px;
  padding: 6px 10px;
  background: #ffffff;
  color: var(--text-normal);
  cursor: pointer;
  font-family: inherit;
  font-size: 12.5px;
  font-weight: 600;
  line-height: 1;
  white-space: nowrap;
  transition: all 0.18s ease;
}

.btn:hover:not(:disabled) {
  background: var(--bg-soft);
  border-color: #9ca3af;
  color: var(--text-main);
}

.btn:disabled {
  opacity: 0.55;
  cursor: not-allowed;
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

.btn.compact {
  min-height: 29px;
  padding: 5px 9px;
  font-size: 12px;
}

.btn.small {
  min-height: 28px;
  padding: 5px 9px;
  font-size: 12px;
}

.btn.danger {
  color: var(--danger);
  background: var(--danger-light);
  border-color: var(--danger-border);
}

.btn.danger:hover:not(:disabled) {
  color: #ffffff;
  background: var(--danger);
  border-color: var(--danger);
}

.btn.danger-filled {
  color: #ffffff;
  background: var(--danger);
  border-color: var(--danger);
}

.btn.danger-filled:hover:not(:disabled) {
  background: var(--danger-hover);
  border-color: var(--danger-hover);
}

.config-card {
  padding: 10px 12px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
}

.config-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  padding-bottom: 8px;
  margin-bottom: 9px;
  border-bottom: 1px solid var(--border-color);
}

.config-toolbar h2 {
  margin: 0;
  color: var(--primary);
  font-size: 16px;
  font-weight: 700;
}

.config-toolbar p {
  margin: 2px 0 0;
  color: var(--text-muted);
  font-size: 12px;
}

.config-grid {
  display: grid;
  grid-template-columns:
    minmax(190px, 1.25fr)
    minmax(190px, 1.15fr)
    minmax(170px, 1fr)
    78px
    78px
    88px
    88px
    minmax(180px, 1fr)
    auto;
  gap: 8px;
  align-items: end;
}

label {
  display: flex;
  flex-direction: column;
  gap: 4px;
  min-width: 0;
}

label span {
  color: #4b5563;
  font-size: 11.5px;
  font-weight: 600;
  white-space: nowrap;
}

.required {
  color: var(--danger);
}

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
  transition: border-color 0.18s ease, box-shadow 0.18s ease;
}

input:hover,
select:hover {
  border-color: #9ca3af;
}

input:focus,
select:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 3px var(--primary-glow);
}

input:disabled,
select:disabled {
  background: #f3f4f6;
  color: #9ca3af;
  cursor: not-allowed;
}

.small-field input {
  text-align: center;
  font-weight: 600;
}

.note-field {
  min-width: 0;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 6px;
  white-space: nowrap;
}

.preview-box {
  display: flex;
  align-items: center;
  gap: 8px;
  min-height: 30px;
  padding: 5px 9px;
  border: 1px dashed var(--primary);
  border-radius: 8px;
  background: var(--primary-light);
  white-space: nowrap;
}

.preview-box span {
  color: var(--primary);
  font-size: 11.5px;
  font-weight: 700;
}

.preview-box strong {
  padding: 3px 8px;
  border: 1px solid #c2e0d3;
  border-radius: 6px;
  background: #ffffff;
  color: var(--primary);
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 13px;
  font-weight: 700;
}

.preview-box.warning {
  border-color: var(--warning);
  background: var(--warning-light);
}

.warning-text {
  margin: 7px 0 0;
  color: var(--warning);
  font-size: 12px;
  font-weight: 600;
}

.table-card {
  overflow: hidden;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
}

.table-toolbar {
  display: grid;
  grid-template-columns: minmax(260px, 1fr) minmax(180px, 260px) auto auto;
  gap: 8px;
  align-items: center;
  padding: 8px 10px;
  background: #ffffff;
  border-bottom: 1px solid var(--border-color);
}

.table-search,
.table-filter {
  min-width: 0;
}

.table-stats {
  display: flex;
  align-items: center;
  gap: 10px;
  color: var(--text-muted);
  font-size: 12px;
  white-space: nowrap;
}

.table-stats strong {
  color: var(--text-main);
}

.table-actions {
  display: flex;
  justify-content: flex-end;
  gap: 6px;
}

.table-scroll {
  max-height: calc(100vh - 265px);
  overflow: auto;
}

table {
  width: 100%;
  min-width: 900px;
  border-collapse: collapse;
  text-align: left;
}

thead th {
  position: sticky;
  top: 0;
  z-index: 2;
}

th,
td {
  padding: 8px 10px;
  border-bottom: 1px solid var(--border-color);
  color: var(--text-normal);
  font-size: 12.5px;
  vertical-align: middle;
}

th {
  background: #f8fafc;
  color: var(--text-muted);
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.035em;
  text-transform: uppercase;
  white-space: nowrap;
}

tbody tr {
  transition: background-color 0.16s ease;
}

tbody tr:hover {
  background: var(--bg-soft);
}

.center {
  text-align: center;
}

.right {
  text-align: right;
}

.cell-title {
  color: var(--text-main);
  font-weight: 700;
  line-height: 1.25;
}

.cell-sub {
  margin-top: 2px;
  color: var(--text-muted);
  font-size: 11.5px;
  line-height: 1.25;
}

.badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 34px;
  padding: 2px 7px;
  border: 1px solid #d9e8df;
  border-radius: 6px;
  background: var(--primary-light);
  color: var(--primary);
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 11.5px;
  font-weight: 700;
}

.badge.muted {
  background: #f3f4f6;
  color: #4b5563;
  border-color: #e5e7eb;
}

.code-text {
  color: var(--primary);
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 12px;
  font-weight: 700;
  white-space: nowrap;
}

.note-cell {
  max-width: 210px;
  color: var(--text-muted);
  line-height: 1.35;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.actions {
  display: flex;
  justify-content: flex-end;
  gap: 5px;
}

.empty-cell {
  padding: 24px 12px !important;
  color: var(--text-muted);
  text-align: center;
}

.row-highlight {
  animation: highlightFade 3s ease-out;
}

@keyframes highlightFade {
  0% {
    background: #d1fae5;
  }

  100% {
    background: transparent;
  }
}

.modal-overlay {
  position: fixed;
  inset: 0;
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px;
  background: rgba(0, 0, 0, 0.45);
  backdrop-filter: blur(2px);
}

.modal {
  width: 100%;
  max-width: 430px;
  overflow: hidden;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 18px 35px rgba(15, 23, 42, 0.18);
  animation: modalIn 0.2s ease-out;
}

.modal-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  padding: 13px 16px;
  border-bottom: 1px solid var(--border-color);
}

.modal-head h3 {
  margin: 0;
  color: var(--danger);
  font-size: 16px;
  font-weight: 700;
}

.modal-close {
  width: 28px;
  height: 28px;
  border: none;
  border-radius: 6px;
  background: transparent;
  color: var(--text-muted);
  cursor: pointer;
  font-size: 21px;
  line-height: 1;
}

.modal-close:hover {
  background: var(--danger-light);
  color: var(--danger);
}

.modal-body {
  padding: 16px;
  color: var(--text-normal);
  font-size: 13px;
  line-height: 1.5;
}

.delete-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin: 12px 0;
  padding: 11px;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  background: var(--bg-soft);
}

.delete-info div {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.delete-info span {
  color: var(--text-muted);
  font-size: 12px;
  font-weight: 600;
}

.delete-info strong {
  color: var(--text-main);
  font-size: 13px;
}

.delete-warning {
  margin: 0;
  color: var(--danger);
  font-size: 13px;
  font-weight: 600;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  padding: 13px 16px;
  background: var(--bg-soft);
  border-top: 1px solid var(--border-color);
}

@keyframes modalIn {
  from {
    opacity: 0;
    transform: scale(0.96);
  }

  to {
    opacity: 1;
    transform: scale(1);
  }
}

.spinner {
  width: 14px;
  height: 14px;
  border: 2px solid #d1d5db;
  border-top-color: var(--primary);
  border-radius: 999px;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 1400px) {
  .config-grid {
    grid-template-columns: repeat(4, minmax(0, 1fr));
  }

  .note-field {
    grid-column: span 2;
  }

  .form-actions {
    grid-column: span 2;
  }

  .table-toolbar {
    grid-template-columns: minmax(260px, 1fr) minmax(180px, 260px);
  }

  .table-stats,
  .table-actions {
    grid-column: span 1;
  }
}

@media (max-width: 900px) {
  .page {
    padding: 10px;
  }

  .page-head,
  .config-toolbar,
  .table-toolbar {
    align-items: stretch;
    flex-direction: column;
  }

  .config-toolbar {
    display: flex;
  }

  .config-grid,
  .table-toolbar {
    grid-template-columns: 1fr;
  }

  .note-field,
  .form-actions,
  .table-stats,
  .table-actions {
    grid-column: auto;
  }

  .form-actions,
  .table-actions,
  .modal-actions {
    align-items: stretch;
    flex-direction: column;
  }

  .form-actions .btn,
  .table-actions .btn,
  .modal-actions .btn {
    width: 100%;
  }

  .preview-box {
    justify-content: space-between;
  }

  .table-scroll {
    max-height: none;
  }
}
</style>