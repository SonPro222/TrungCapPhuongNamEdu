<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Cấu hình mã sinh viên</h1>
        <p>Cấu hình tiền tố và mã đầu theo ngành + version chương trình.</p>
      </div>

      <button type="button" class="btn" @click="taiDuLieu">
        Tải lại
      </button>
    </header>

    <form class="form-card" @submit.prevent="luu">
      <label>
        <span>Ngành</span>
        <select v-model="form.nganhId" required @change="doiNganh">
          <option value="">-- Chọn ngành --</option>
          <option v-for="item in nganhList" :key="item.id" :value="item.id">
            {{ item.maNganh }} - {{ item.tenNganh }}
          </option>
        </select>
      </label>

      <label>
        <span>Chương trình</span>
        <select v-model="form.chuongTrinhId" @change="doiChuongTrinh">
          <option value="">-- Chọn chương trình --</option>
          <option v-for="item in chuongTrinhTheoNganh" :key="item.id" :value="item.id">
            {{ item.maChuongTrinh }} - {{ item.tenChuongTrinh }}
          </option>
        </select>
      </label>

      <label>
        <span>Version</span>
        <select v-model="form.chuongTrinhVersionId" required>
          <option value="">-- Chọn version --</option>
          <option v-for="item in versionTheoChuongTrinh" :key="item.id" :value="item.id">
            {{ item.maVersion }} - {{ item.tenVersion }}
          </option>
        </select>
      </label>

      <label>
        <span>Tiền tố</span>
        <input v-model="form.tienTo" type="text" required placeholder="PN" />
      </label>

      <label>
        <span>Mã đầu</span>
        <input v-model="form.maDau" type="text" required placeholder="60 hoặc 70" />
      </label>

      <label>
        <span>Số hiện tại</span>
        <input v-model.number="form.soHienTai" type="number" min="0" />
      </label>

      <label>
        <span>Độ dài số thứ tự</span>
        <input v-model.number="form.doDaiSoThuTu" type="number" min="1" />
      </label>

      <label>
        <span>Ghi chú</span>
        <input v-model="form.ghiChu" type="text" />
      </label>

      <div class="form-actions">
        <button type="submit" class="btn primary">
          {{ idDangSua ? 'Cập nhật' : 'Thêm cấu hình' }}
        </button>

        <button type="button" class="btn" @click="resetForm">
          Làm mới
        </button>
      </div>
    </form>

    <div v-if="thongBao" class="alert success">{{ thongBao }}</div>
    <div v-if="loi" class="alert error">{{ loi }}</div>

    <section class="table-card">
      <table>
        <thead>
        <tr>
          <th>Ngành</th>
          <th>Version</th>
          <th>Tiền tố</th>
          <th>Mã đầu</th>
          <th>Số hiện tại</th>
          <th>Mã tiếp theo</th>
          <th>Ghi chú</th>
          <th>Thao tác</th>
        </tr>
        </thead>

        <tbody>
        <tr v-if="dangTai">
          <td colspan="8">Đang tải dữ liệu...</td>
        </tr>

        <tr v-else-if="!danhSach.length">
          <td colspan="8">Chưa có cấu hình</td>
        </tr>

        <tr v-for="item in danhSach" v-else :key="item.id">
          <td>{{ item.maNganh }} - {{ item.tenNganh }}</td>
          <td>{{ item.maVersion }} - {{ item.tenVersion }}</td>
          <td>{{ item.tienTo }}</td>
          <td>{{ item.maDau }}</td>
          <td>{{ item.soHienTai }}</td>
          <td>{{ item.maSinhVienTiepTheo }}</td>
          <td>{{ item.ghiChu || '-' }}</td>
          <td class="actions">
            <button type="button" class="btn small" @click="sua(item)">Sửa</button>
            <button type="button" class="btn small danger" @click="xoa(item)">Xóa</button>
          </td>
        </tr>
        </tbody>
      </table>

      <div class="pagination">
        <button type="button" class="btn" :disabled="page <= 0" @click="doiTrang(page - 1)">
          Trước
        </button>

        <span>Trang {{ page + 1 }} / {{ totalPages || 1 }} - Tổng {{ totalElements }}</span>

        <button type="button" class="btn" :disabled="page + 1 >= totalPages" @click="doiTrang(page + 1)">
          Sau
        </button>
      </div>
    </section>
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

const dangTai = ref(false)
const loi = ref('')
const thongBao = ref('')
const idDangSua = ref(null)

const page = ref(0)
const size = ref(20)
const totalPages = ref(0)
const totalElements = ref(0)

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

onMounted(() => {
  taiDuLieu()
})

async function taiDuLieu() {
  loi.value = ''

  try {
    const [nganhResult, chuongTrinhResult, versionResult, cauHinhResult] = await Promise.all([
      daoTaoApi.nganh.getAll({ page: 0, size: 1000 }),
      chuongTrinhApi.chuongTrinh.getAll({ page: 0, size: 1000 }),
      chuongTrinhApi.chuongTrinhVersion.getAll({ page: 0, size: 1000 }),
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

async function luu() {
  loi.value = ''
  thongBao.value = ''

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
    if (idDangSua.value) {
      await heThongApi.cauHinhMaSinhVien.update(idDangSua.value, payload)
      thongBao.value = 'Cập nhật cấu hình thành công'
    } else {
      await heThongApi.cauHinhMaSinhVien.create(payload)
      thongBao.value = 'Thêm cấu hình thành công'
    }

    resetForm()
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Lưu cấu hình thất bại'
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
}

async function xoa(item) {
  if (!window.confirm(`Xóa cấu hình mã sinh viên ID ${item.id}?`)) return

  try {
    await heThongApi.cauHinhMaSinhVien.delete(item.id)
    thongBao.value = 'Xóa cấu hình thành công'
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Xóa cấu hình thất bại'
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

.form-card {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
  padding: 16px;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  background: #fff;
}

.form-card label {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-card span {
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

.form-actions {
  grid-column: 1 / -1;
  display: flex;
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
  overflow: auto;
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 16px;
}

table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1100px;
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
}

.actions,
.pagination {
  display: flex;
  gap: 8px;
  align-items: center;
}

.pagination {
  justify-content: flex-end;
  margin-top: 12px;
}
</style>