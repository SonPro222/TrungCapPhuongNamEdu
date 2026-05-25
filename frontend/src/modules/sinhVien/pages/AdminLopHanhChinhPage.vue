<template>
  <section class="sv-page">
    <div class="sv-card">
      <div class="sv-card-title">
        <h2>Tạo lớp hành chính</h2>
        <p>Tạo lớp hành chính theo ngành, chương trình và version trước khi tiếp nhận sinh viên.</p>
      </div>

      <div v-if="thongBao" class="sv-message" :class="thongBaoLoai">
        {{ thongBao }}
      </div>

      <div class="sv-grid four">
        <label>
          Ngành
          <select v-model="boLoc.nganhId" @change="doiNganh">
            <option value="">Chọn ngành</option>
            <option v-for="nganh in danhSachNganh" :key="nganh.id" :value="nganh.id">
              {{ nganh.maNganh }} - {{ nganh.tenNganh }}
            </option>
          </select>
        </label>

        <label>
          Chương trình
          <select v-model="boLoc.chuongTrinhId" @change="doiChuongTrinh">
            <option value="">Chọn chương trình</option>
            <option v-for="ct in chuongTrinhTheoNganh" :key="ct.id" :value="ct.id">
              {{ ct.maChuongTrinh }} - {{ ct.tenChuongTrinh }}
            </option>
          </select>
        </label>

        <label>
          Version chương trình
          <select v-model="boLoc.chuongTrinhVersionId">
            <option value="">Chọn version</option>
            <option v-for="item in versionTheoChuongTrinh" :key="item.id" :value="item.id">
              {{ item.maVersion }} - {{ item.tenVersion || 'Version' }}
            </option>
          </select>
        </label>

        <label>
          Trạng thái
          <select v-model="form.trangThai">
            <option value="du_kien">Dự kiến</option>
            <option value="dang_hoc">Đang học</option>
            <option value="tam_dung">Tạm dừng</option>
            <option value="da_tot_nghiep">Đã tốt nghiệp</option>
            <option value="huy">Hủy</option>
          </select>
        </label>
      </div>

      <form class="sv-grid four" @submit.prevent="taoLopHanhChinh">
        <label>
          Mã lớp hành chính
          <input v-model.trim="form.maLop" required />

        </label>

        <label>
          Tên lớp hành chính
          <input v-model.trim="form.tenLop" required />
        </label>

        <label>
          Sĩ số tối đa
          <input v-model.number="form.siSo" type="number" min="0" />
        </label>

        <label>
          Ghi chú
          <input v-model.trim="form.ghiChu" />
        </label>

        <div class="span-4 sv-actions">
          <button type="submit" :disabled="dangLuu">
            {{ dangLuu ? 'Đang lưu...' : (idDangSua ? 'Cập nhật lớp hành chính' : 'Tạo lớp hành chính') }}
          </button>
          <button type="button" class="secondary" @click="resetForm">
            Làm mới
          </button>
        </div>
      </form>
    </div>

    <div class="sv-card">
      <div class="sv-card-title">
        <h2>Danh sách lớp hành chính theo version</h2>
        <p>Chọn version để xem các lớp hành chính đã tạo.</p>
      </div>

      <div class="sv-table-wrap">
        <table class="sv-table">
          <thead>
          <tr>
            <th>Mã lớp</th>
            <th>Tên lớp</th>
            <th>Version</th>
            <th>Sĩ số</th>
            <th>Trạng thái</th>
            <th>Ghi chú</th>
            <th>Thao tác</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="lop in lopHanhChinhTheoVersion" :key="lop.id">
            <td>{{ lop.maLop }}</td>
            <td>{{ lop.tenLop }}</td>
            <td>{{ tenVersion(lop.chuongTrinhVersionId) }}</td>
            <td>{{ lop.siSo || 0 }}</td>
            <td>{{ lop.trangThai }}</td>
            <td>{{ lop.ghiChu }}</td>
            <td>
              <div class="sv-row-actions">
                <button type="button" class="secondary" @click="chonSuaLopHanhChinh(lop)">
                  Sửa
                </button>
                <button type="button" class="danger" @click="xoaLopHanhChinh(lop)">
                  Xóa
                </button>
              </div>
            </td>
          </tr>
          <tr v-if="!lopHanhChinhTheoVersion.length">
            <td colspan="7">Chưa có lớp hành chính cho version đang chọn.</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { sinhVienService } from '../services/sinhVienService'

const thongBao = ref('')
const thongBaoLoai = ref('success')
const dangLuu = ref(false)
const idDangSua = ref(null)
const danhSachNganh = ref([])
const danhSachChuongTrinh = ref([])
const danhSachVersion = ref([])
const danhSachLopHanhChinh = ref([])

const boLoc = reactive({
  nganhId: '',
  chuongTrinhId: '',
  chuongTrinhVersionId: ''
})

const form = reactive({
  maLop: '',
  tenLop: '',
  siSo: 0,
  trangThai: 'dang_hoc',
  ghiChu: ''
})

const chuongTrinhTheoNganh = computed(() => {
  if (!boLoc.nganhId) return []
  return danhSachChuongTrinh.value.filter(item => String(item.nganhId) === String(boLoc.nganhId))
})

const versionTheoChuongTrinh = computed(() => {
  if (!boLoc.chuongTrinhId) return []
  return danhSachVersion.value.filter(item => String(item.chuongTrinhId) === String(boLoc.chuongTrinhId))
})

const lopHanhChinhTheoVersion = computed(() => {
  if (!boLoc.chuongTrinhVersionId) return []
  return danhSachLopHanhChinh.value.filter(item => String(item.chuongTrinhVersionId) === String(boLoc.chuongTrinhVersionId))
})

onMounted(async () => {
  await taiDuLieu()
})

async function taiDuLieu() {
  try {
    const [nganh, chuongTrinh, version, lopHanhChinh] = await Promise.all([
      sinhVienService.layNganh(),
      sinhVienService.layChuongTrinh(),
      sinhVienService.layVersion(),
      sinhVienService.layLopHanhChinh()
    ])

    danhSachNganh.value = nganh
    danhSachChuongTrinh.value = chuongTrinh
    danhSachVersion.value = version
    danhSachLopHanhChinh.value = lopHanhChinh
  } catch (error) {
    baoLoi(error?.message || 'Không tải được dữ liệu lớp hành chính')
  }
}

function doiNganh() {
  boLoc.chuongTrinhId = ''
  doiChuongTrinh()
}

function doiChuongTrinh() {
  boLoc.chuongTrinhVersionId = ''
}

async function taoLopHanhChinh() {
  if (!boLoc.chuongTrinhVersionId) {
    baoLoi('Phải chọn version chương trình trước khi tạo lớp hành chính')
    return
  }

  if (!form.maLop || !form.tenLop) {
    baoLoi('Phải nhập mã lớp và tên lớp hành chính')
    return
  }

  dangLuu.value = true

  try {
    const payload = lamSachPayload({
      maLop: form.maLop,
      tenLop: form.tenLop,
      chuongTrinhVersionId: Number(boLoc.chuongTrinhVersionId),
      siSo: Number(form.siSo || 0),
      trangThai: form.trangThai,
      ghiChu: form.ghiChu
    })

    if (idDangSua.value) {
      const response = await sinhVienService.capNhatLopHanhChinh(idDangSua.value, payload)
      const lopCapNhat = layMotBanGhi(response)

      if (lopCapNhat?.id) {
        const index = danhSachLopHanhChinh.value.findIndex(item => String(item.id) === String(lopCapNhat.id))
        if (index !== -1) {
          danhSachLopHanhChinh.value[index] = lopCapNhat
        }
      } else {
        await taiDuLieu()
      }

      resetForm()
      baoThanhCong('Đã cập nhật lớp hành chính')
      return
    }

    const response = await sinhVienService.taoLopHanhChinh(payload)
    const lopMoi = layMotBanGhi(response)

    if (lopMoi?.id) {
      danhSachLopHanhChinh.value.push(lopMoi)
    } else {
      await taiDuLieu()
    }

    resetForm()
    baoThanhCong('Đã tạo lớp hành chính')
  } catch (error) {
    baoLoi(error?.message || 'Lưu lớp hành chính thất bại')
  } finally {
    dangLuu.value = false
  }
}
function chonSuaLopHanhChinh(lop) {
  idDangSua.value = lop.id
  boLoc.chuongTrinhVersionId = lop.chuongTrinhVersionId ? String(lop.chuongTrinhVersionId) : boLoc.chuongTrinhVersionId
  form.maLop = lop.maLop || ''
  form.tenLop = lop.tenLop || ''
  form.siSo = Number(lop.siSo || 0)
  form.trangThai = lop.trangThai || 'dang_hoc'
  form.ghiChu = lop.ghiChu || ''
}

async function xoaLopHanhChinh(lop) {
  const dongY = window.confirm(`Xóa lớp hành chính "${lop.maLop}"?`)
  if (!dongY) return

  try {
    await sinhVienService.xoaLopHanhChinh(lop.id)
    danhSachLopHanhChinh.value = danhSachLopHanhChinh.value.filter(item => String(item.id) !== String(lop.id))

    if (String(idDangSua.value) === String(lop.id)) {
      resetForm()
    }

    baoThanhCong('Đã xóa lớp hành chính')
  } catch (error) {
    baoLoi(error?.message || 'Không xóa được lớp hành chính')
  }
}
function resetForm() {
  idDangSua.value = null
  form.maLop = ''
  form.tenLop = ''
  form.siSo = 0
  form.trangThai = 'dang_hoc'
  form.ghiChu = ''
}
function tenVersion(id) {
  const item = danhSachVersion.value.find(version => String(version.id) === String(id))
  if (!item) return id
  return `${item.maVersion} - ${item.tenVersion || 'Version'}`
}

function layMotBanGhi(response) {
  if (response?.data && !Array.isArray(response.data)) return response.data
  if (response && !Array.isArray(response)) return response
  return null
}

function lamSachPayload(payload) {
  return Object.fromEntries(
      Object.entries(payload).filter(([, value]) => value !== '' && value !== null && value !== undefined)
  )
}
function baoThanhCong(message) {
  thongBao.value = message
  thongBaoLoai.value = 'success'
}

function baoLoi(message) {
  thongBao.value = message
  thongBaoLoai.value = 'error'
}
</script>

<style scoped>
.sv-page {
  display: grid;
  gap: 18px;
}

.sv-card {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
  padding: 18px;
  box-shadow: 0 10px 30px rgba(15, 23, 42, 0.06);
}

.sv-card-title {
  margin-bottom: 14px;
}

.sv-card-title h2 {
  margin: 0;
  font-size: 20px;
  color: #0f172a;
}

.sv-card-title p {
  margin: 6px 0 0;
  color: #64748b;
}

.sv-grid {
  display: grid;
  gap: 14px;
}

.sv-grid.four {
  grid-template-columns: repeat(4, minmax(0, 1fr));
}

.span-4 {
  grid-column: span 4;
}

label {
  display: grid;
  gap: 8px;
  font-weight: 700;
  color: #334155;
}

input,
select {
  min-height: 42px;
  border: 1px solid #cbd5e1;
  border-radius: 12px;
  padding: 9px 12px;
  outline: none;
}

.sv-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

button {
  border: 0;
  border-radius: 10px;
  padding: 10px 14px;
  font-weight: 700;
  cursor: pointer;
  background: #1d4ed8;
  color: #ffffff;
}

button.secondary {
  background: #e2e8f0;
  color: #334155;
}

button:disabled {
  opacity: 0.65;
  cursor: not-allowed;
}

.sv-message {
  border-radius: 14px;
  padding: 12px 14px;
  font-weight: 700;
  margin-bottom: 14px;
}

.sv-message.success {
  background: #ecfdf5;
  color: #047857;
  border: 1px solid #a7f3d0;
}

.sv-message.error {
  background: #fef2f2;
  color: #b91c1c;
  border: 1px solid #fecaca;
}

.sv-table-wrap {
  overflow: auto;
}

.sv-table {
  width: 100%;
  border-collapse: collapse;
}

.sv-table th,
.sv-table td {
  border-bottom: 1px solid #e2e8f0;
  padding: 10px;
  text-align: left;
}

.sv-table th {
  font-size: 12px;
  text-transform: uppercase;
  color: #64748b;
  background: #f8fafc;
}

@media (max-width: 1100px) {
  .sv-grid.four {
    grid-template-columns: 1fr;
  }

  .span-4 {
    grid-column: span 1;
  }
}
.sv-row-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

button.danger {
  background: #dc2626;
  color: #ffffff;
}
</style>