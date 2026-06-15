<template>
  <div class="page-container">
    <div class="alert-wrap">
      <div v-if="thongBao" class="alert alert-success">
        {{ thongBao }}
      </div>

      <div v-if="loi" class="alert alert-error">
        {{ loi }}
      </div>
    </div>

    <section class="page">
      <header class="page-head">
        <div>
          <h1>Quản lý CRUD gốc</h1>
          <p>Quản lý cấu hình mã sinh viên, vai trò và quyền gốc trong hệ thống.</p>
        </div>

        <div class="head-actions">
          <nav class="tabs">
            <button
                type="button"
                class="tab"
                :class="{ active: tabDangChon === 'maSinhVien' }"
                @click="chonTab('maSinhVien')"
            >
              Mã sinh viên
            </button>

            <button
                type="button"
                class="tab"
                :class="{ active: tabDangChon === 'vaiTro' }"
                @click="chonTab('vaiTro')"
            >
              Vai trò
            </button>

            <button
                type="button"
                class="tab"
                :class="{ active: tabDangChon === 'quyen' }"
                @click="chonTab('quyen')"
            >
              Quyền
            </button>
          </nav>

          <button type="button" class="btn compact" @click="taiTatCa">
            Tải lại
          </button>
        </div>
      </header>

      <AdminCauHinhMaSinhVien v-if="tabDangChon === 'maSinhVien'" />

      <section v-else class="crud-layout">
        <form class="form-card" :class="{ 'is-editing': idDangSua }" @submit.prevent="luu">
          <div class="form-head">
            <div>
              <h2>{{ idDangSua ? 'Cập nhật' : 'Thêm mới' }}</h2>
              <p>
                {{
                  tabDangChon === 'vaiTro'
                      ? 'Khai báo vai trò sử dụng trong hệ thống.'
                      : 'Khai báo quyền hạn sử dụng trong hệ thống.'
                }}
              </p>
            </div>

            <span v-if="idDangSua" class="editing-badge">
              ID {{ idDangSua }}
            </span>
          </div>

          <div class="form-grid">
            <label>
              <span>
                {{ tabDangChon === 'vaiTro' ? 'Mã vai trò' : 'Mã quyền' }}
                <em>*</em>
              </span>
              <input
                  v-model="form.ma"
                  type="text"
                  required
                  :placeholder="tabDangChon === 'vaiTro' ? 'ADMIN, GIAO_VU...' : 'XEM_DIEM, SUA_DIEM...'"
              />
            </label>

            <label>
              <span>
                {{ tabDangChon === 'vaiTro' ? 'Tên vai trò' : 'Tên quyền' }}
                <em>*</em>
              </span>
              <input
                  v-model="form.ten"
                  type="text"
                  required
                  placeholder="Nhập tên hiển thị"
              />
            </label>

            <label class="desc-field">
              <span>Mô tả</span>
              <input
                  v-model="form.moTa"
                  type="text"
                  placeholder="Nhập mô tả ngắn gọn"
              />
            </label>

            <div class="form-actions">
              <button
                  type="submit"
                  class="btn primary"
                  :class="{ warning: idDangSua }"
              >
                {{ idDangSua ? 'Cập nhật' : 'Thêm' }}
              </button>

              <button type="button" class="btn" @click="resetForm">
                {{ idDangSua ? 'Hủy' : 'Làm mới' }}
              </button>
            </div>
          </div>
        </form>

        <section class="table-card">
          <div class="table-toolbar">
            <div class="table-title">
              <strong>
                {{ tabDangChon === 'vaiTro' ? 'Danh sách vai trò gốc' : 'Danh sách quyền gốc' }}
              </strong>
              <span>Hiển thị {{ danhSachHienThi.length }} bản ghi</span>
            </div>

            <div class="search-box">
              <input
                  v-model="keyword"
                  type="text"
                  placeholder="Tìm theo mã, tên hoặc mô tả..."
              />

              <button
                  v-if="keyword"
                  type="button"
                  class="clear-search"
                  title="Xóa tìm kiếm"
                  @click="xoaLoc"
              >
                ×
              </button>
            </div>
          </div>

          <div class="table-wrap">
            <table>
              <thead>
              <tr>
                <th class="col-id">ID</th>
                <th class="col-code">Mã</th>
                <th class="col-name">Tên</th>
                <th>Mô tả</th>
                <th class="col-actions">Thao tác</th>
              </tr>
              </thead>

              <tbody>
              <tr v-if="dangTai">
                <td colspan="5" class="table-status">
                  Đang tải dữ liệu...
                </td>
              </tr>

              <tr v-else-if="!danhSachHienThi.length">
                <td colspan="5" class="table-status">
                  {{
                    keyword
                        ? 'Không tìm thấy bản ghi phù hợp.'
                        : 'Danh sách hiện chưa có bản ghi nào.'
                  }}
                </td>
              </tr>

              <tr
                  v-for="item in danhSachHienThi"
                  v-else
                  :key="item.id"
                  :class="{ 'is-active-row': idDangSua === item.id }"
              >
                <td class="font-mono">
                  #{{ item.id }}
                </td>

                <td>
                  <span class="badge-code">
                    {{ layMa(item) }}
                  </span>
                </td>

                <td class="font-semibold">
                  {{ layTen(item) }}
                </td>

                <td class="text-muted">
                  <div class="text-truncate">
                    {{ item.moTa || '-' }}
                  </div>
                </td>

                <td class="actions">
                  <button
                      type="button"
                      class="btn small"
                      title="Sửa"
                      @click="sua(item)"
                  >
                    Sửa
                  </button>

                  <button
                      type="button"
                      class="btn small danger"
                      title="Xóa"
                      @click="xoa(item)"
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
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { heThongApi } from '../api/heThongApi'
import AdminCauHinhMaSinhVien from './AdminCauHinhMaSinhVien.vue'

const tabDangChon = ref('maSinhVien')
const dangTai = ref(false)
const loi = ref('')
const thongBao = ref('')
const keyword = ref('')
const idDangSua = ref(null)

const danhSach = ref([])

const form = reactive({
  ma: '',
  ten: '',
  moTa: ''
})

// Quản lý thời gian tự tắt thông báo
let toastTimeoutId = null;

function showToast(message, isError = false) {
  if (isError) {
    loi.value = message;
    thongBao.value = '';
  } else {
    thongBao.value = message;
    loi.value = '';
  }

  if (toastTimeoutId) clearTimeout(toastTimeoutId);
  toastTimeoutId = setTimeout(() => {
    loi.value = '';
    thongBao.value = '';
  }, 3000); // 3 giây tự tắt
}

const apiHienTai = computed(() => {
  return tabDangChon.value === 'vaiTro' ? heThongApi.vaiTro : heThongApi.quyen
})

const danhSachHienThi = computed(() => {
  const value = keyword.value.trim().toLowerCase()

  if (!value) {
    return danhSach.value
  }

  return danhSach.value.filter((item) => {
    return String(layMa(item) || '').toLowerCase().includes(value)
        || String(layTen(item) || '').toLowerCase().includes(value)
        || String(item.moTa || '').toLowerCase().includes(value)
  })
})

onMounted(() => {
  taiTatCa()
})

async function taiTatCa() {
  if (tabDangChon.value === 'maSinhVien') {
    return
  }
  await taiDanhSachmau()
}

async function taiDanhSachmau() {
  if (tabDangChon.value === 'maSinhVien') {
    return
  }

  dangTai.value = true
  loi.value = ''
  thongBao.value = ''

  try {
    const result = await apiHienTai.value.getAll({ page: 0, size: 1000 })
    danhSach.value = layDanhSach(result)
  } catch (error) {
    showToast(error?.message || 'Không tải được dữ liệu', true)
  } finally {
    dangTai.value = false
  }
}

function chonTab(tab) {
  tabDangChon.value = tab
  keyword.value = ''
  resetForm()
  taiTatCa()
}

function layDanhSach(result) {
  if (Array.isArray(result)) return result
  if (Array.isArray(result?.content)) return result.content
  if (Array.isArray(result?.data?.content)) return result.data.content
  if (Array.isArray(result?.data)) return result.data
  return []
}

async function luu() {
  const payload = tabDangChon.value === 'vaiTro'
      ? {
        maVaiTro: form.ma,
        tenVaiTro: form.ten,
        moTa: form.moTa
      }
      : {
        maQuyen: form.ma,
        tenQuyen: form.ten,
        moTa: form.moTa
      }

  try {
    if (idDangSua.value) {
      await apiHienTai.value.update(idDangSua.value, payload)
      showToast('Cập nhật thành công')
    } else {
      await apiHienTai.value.create(payload)
      showToast('Thêm mới thành công')
    }

    resetForm()
    await taiDanhSachmau()
  } catch (error) {
    showToast(error?.message || 'Lưu dữ liệu thất bại', true)
  }
}

function sua(item) {
  idDangSua.value = item.id
  form.ma = layMa(item)
  form.ten = layTen(item)
  form.moTa = item.moTa || ''
}

async function xoa(item) {
  if (!window.confirm(`Bạn có chắc chắn muốn xóa [${layMa(item)}]?`)) return

  try {
    await apiHienTai.value.delete(item.id)
    showToast('Xóa thành công')
    await taiDanhSachmau()
  } catch (error) {
    showToast(error?.message || 'Xóa thất bại', true)
  }
}

function xoaLoc() {
  keyword.value = ''
}

function resetForm() {
  idDangSua.value = null
  form.ma = ''
  form.ten = ''
  form.moTa = ''
}

function layMa(item) {
  return tabDangChon.value === 'vaiTro' ? item.maVaiTro : item.maQuyen
}

function layTen(item) {
  return tabDangChon.value === 'vaiTro' ? item.tenVaiTro : item.tenQuyen
}
</script>

<style scoped>
.page-container {
  --primary: #077149;
  --primary-hover: #055436;
  --primary-light: #e6f1ec;
  --primary-glow: rgba(7, 113, 73, 0.14);

  --warning: #ea580c;
  --warning-hover: #c2410c;
  --warning-light: #fff7ed;
  --warning-border: #fdba74;

  --danger: #dc2626;
  --danger-light: #fef2f2;
  --danger-border: #fecaca;

  --success: #16a34a;

  --text-main: #0f172a;
  --text-normal: #334155;
  --text-muted: #64748b;

  --bg-page: #f1f5f9;
  --bg-card: #ffffff;
  --bg-soft: #f8fafc;

  --border-color: #e2e8f0;
  --border-strong: #cbd5e1;

  --radius: 10px;
  --shadow: 0 1px 2px rgba(15, 23, 42, 0.05);

  min-height: 100vh;
  padding: 10px 14px;
  background: var(--bg-page);
  color: var(--text-main);
  box-sizing: border-box;
  font-family: Roboto, system-ui, -apple-system, BlinkMacSystemFont, sans-serif;
}

.page {
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 100%;
  max-width: 1560px;
  margin: 0 auto;
}

/* HEADER */
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

.head-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* TABS */
.tabs {
  display: inline-flex;
  gap: 3px;
  padding: 4px;
  background: #e2e8f0;
  border-radius: 9px;
}

.tab {
  border: none;
  background: transparent;
  color: var(--text-muted);
  border-radius: 7px;
  padding: 6px 10px;
  cursor: pointer;
  font-family: inherit;
  font-size: 12.5px;
  font-weight: 600;
  white-space: nowrap;
  transition: all 0.16s ease;
}

.tab:hover {
  color: var(--text-main);
  background: rgba(255, 255, 255, 0.55);
}

.tab.active {
  background: #ffffff;
  color: var(--primary);
  box-shadow: 0 1px 2px rgba(15, 23, 42, 0.12);
}

/* BUTTONS */
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
  font-weight: 600;
  line-height: 1;
  white-space: nowrap;
  transition: all 0.16s ease;
}

.btn:hover:not(:disabled) {
  background: var(--bg-soft);
  border-color: #94a3b8;
  color: var(--text-main);
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

.btn.primary.warning {
  background: var(--warning);
  border-color: var(--warning);
}

.btn.primary.warning:hover:not(:disabled) {
  background: var(--warning-hover);
  border-color: var(--warning-hover);
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

/* LAYOUT */
.crud-layout {
  display: grid;
  grid-template-columns: minmax(0, 420px) minmax(0, 1fr);
  gap: 10px;
  align-items: start;
}

/* FORM */
.form-card {
  position: sticky;
  top: 10px;
  padding: 10px 12px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
}

.form-card.is-editing {
  background: var(--warning-light);
  border-color: var(--warning-border);
}

.form-head {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  padding-bottom: 8px;
  margin-bottom: 8px;
  border-bottom: 1px solid var(--border-color);
}

.form-head h2 {
  margin: 0;
  color: var(--text-main);
  font-size: 15px;
  font-weight: 700;
}

.form-head p {
  margin: 2px 0 0;
  color: var(--text-muted);
  font-size: 12px;
  line-height: 1.35;
}

.editing-badge {
  display: inline-flex;
  align-items: center;
  height: 24px;
  padding: 0 8px;
  border: 1px solid var(--warning-border);
  border-radius: 999px;
  background: #ffedd5;
  color: #9a3412;
  font-size: 11px;
  font-weight: 700;
  white-space: nowrap;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 8px;
}

.form-card label {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.form-card label span {
  color: var(--text-normal);
  font-size: 11.5px;
  font-weight: 700;
}

.form-card label em {
  color: var(--danger);
  font-style: normal;
}

input,
textarea {
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
textarea:hover {
  border-color: #94a3b8;
}

input:focus,
textarea:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 3px var(--primary-glow);
}

.desc-field input {
  min-height: 31px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 6px;
}

/* TABLE AREA */
.table-card {
  overflow: hidden;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
}

.table-toolbar {
  display: grid;
  grid-template-columns: minmax(180px, auto) minmax(260px, 1fr);
  gap: 10px;
  align-items: center;
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
  font-size: 13px;
  font-weight: 700;
}

.table-title span {
  color: var(--text-muted);
  font-size: 12px;
}

.search-box {
  position: relative;
  min-width: 0;
}

.search-box input {
  padding-right: 32px;
  background: var(--bg-soft);
  border-color: var(--border-color);
}

.search-box input:focus {
  background: #ffffff;
}

.clear-search {
  position: absolute;
  right: 7px;
  top: 50%;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 21px;
  height: 21px;
  border: none;
  border-radius: 999px;
  background: #e2e8f0;
  color: #475569;
  cursor: pointer;
  transform: translateY(-50%);
  font-size: 16px;
  line-height: 1;
}

.clear-search:hover {
  background: #cbd5e1;
  color: var(--text-main);
}

.table-wrap {
  max-height: calc(100vh - 176px);
  overflow: auto;
}

table {
  width: 100%;
  min-width: 720px;
  border-collapse: collapse;
}

thead th {
  position: sticky;
  top: 0;
  z-index: 2;
}

th,
td {
  padding: 8px 10px;
  text-align: left;
  vertical-align: middle;
  border-bottom: 1px solid #eef2f7;
  color: var(--text-normal);
  font-size: 12.5px;
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

tbody tr:hover {
  background: var(--bg-soft);
}

tbody tr.is-active-row {
  background: var(--warning-light);
  box-shadow: inset 3px 0 0 var(--warning);
}

.col-id {
  width: 68px;
}

.col-code {
  width: 160px;
}

.col-name {
  width: 190px;
}

.col-actions {
  width: 120px;
  text-align: right;
}

.font-mono {
  color: var(--text-muted);
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 12px;
}

.font-semibold {
  color: var(--text-main);
  font-weight: 700;
}

.text-muted {
  color: #475569;
}

.text-truncate {
  max-width: 420px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.badge-code {
  display: inline-flex;
  align-items: center;
  max-width: 180px;
  padding: 3px 7px;
  overflow: hidden;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  background: #f8fafc;
  color: var(--text-normal);
  font-size: 12px;
  font-weight: 700;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.actions {
  display: flex;
  justify-content: flex-end;
  gap: 5px;
}

.table-status {
  padding: 24px 12px !important;
  text-align: center;
  color: var(--text-muted);
}

/* ALERTS */
.alert-wrap {
  position: fixed;
  top: 12px;
  right: 14px;
  z-index: 50;
  display: flex;
  flex-direction: column;
  gap: 8px;
  max-width: min(380px, calc(100vw - 28px));
}

.alert {
  padding: 10px 13px;
  border-radius: 8px;
  background: #ffffff;
  box-shadow: 0 12px 24px rgba(15, 23, 42, 0.12);
  font-size: 13px;
  font-weight: 600;
}

.alert-success {
  color: #166534;
  border-left: 4px solid var(--success);
}

.alert-error {
  color: #991b1b;
  border-left: 4px solid var(--danger);
}

/* RESPONSIVE */
@media (max-width: 1180px) {
  .crud-layout {
    grid-template-columns: 1fr;
  }

  .form-card {
    position: static;
  }

  .form-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .desc-field {
    grid-column: 1 / -1;
  }

  .form-actions {
    grid-column: 1 / -1;
  }

  .table-wrap {
    max-height: calc(100vh - 260px);
  }
}

@media (max-width: 768px) {
  .page-container {
    padding: 10px;
  }

  .page-head,
  .head-actions,
  .table-toolbar {
    align-items: stretch;
    flex-direction: column;
  }

  .head-actions,
  .table-toolbar {
    display: flex;
  }

  .tabs {
    overflow-x: auto;
  }

  .tab {
    flex: 1;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .form-actions {
    flex-direction: column;
  }

  .form-actions .btn,
  .head-actions .btn {
    width: 100%;
  }

  .table-wrap {
    max-height: none;
  }

  thead th {
    position: static;
  }

  .alert-wrap {
    top: 10px;
    left: 10px;
    right: 10px;
    max-width: none;
  }
}
</style>