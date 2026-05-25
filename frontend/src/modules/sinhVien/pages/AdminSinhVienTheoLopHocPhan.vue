<template>
    <section class="sv-lhp-page">
      <div class="page-title">
        <div>
          <h2>Sinh viên theo lớp học phần</h2>
          <p>Chọn ngành, chương trình, version, kỳ, môn và lớp học phần để xem hoặc phân sinh viên.</p>
        </div>
      </div>

    <div v-if="thongBao" class="message" :class="thongBaoLoai">
      {{ thongBao }}
    </div>

    <section class="panel">
      <div class="filter-grid">
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
          Version
          <select v-model="boLoc.chuongTrinhVersionId" @change="doiVersion">
            <option value="">Chọn version</option>
            <option v-for="version in versionTheoChuongTrinh" :key="version.id" :value="version.id">
              {{ version.maVersion }} - {{ version.tenVersion || 'Version' }}
            </option>
          </select>
        </label>

        <label>
          Kỳ / khung kỳ
          <select v-model="boLoc.khungKyId" @change="doiKhungKy">
            <option value="">Chọn kỳ</option>
            <option v-for="ky in khungKyTheoVersion" :key="ky.id" :value="ky.id">
              {{ ky.maKy }} - {{ ky.tenKy }}
            </option>
          </select>
        </label>
      </div>
    </section>

    <section class="content-grid">
      <div class="panel">
        <div class="panel-title">
          <h2>Môn trong kỳ</h2>
          <span>{{ monTrongKy.length }} môn</span>
        </div>

        <table>
          <thead>
          <tr>
            <th>Thứ tự</th>
            <th>Mã môn CT</th>
            <th>Tín chỉ</th>
            <th></th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="mon in monTrongKy" :key="mon.id" :class="{ selected: monDangChon?.id === mon.id }">
            <td>{{ mon.thuTu }}</td>
            <td>{{ mon.maMonTrongCt }}</td>
            <td>{{ mon.soTinChi }}</td>
            <td><button type="button" @click="chonMon(mon)">Chọn</button></td>
          </tr>
          <tr v-if="!monTrongKy.length">
            <td colspan="4" class="empty">Chưa có môn trong kỳ đã chọn</td>
          </tr>
          </tbody>
        </table>
      </div>

      <div class="panel">
        <div class="panel-title">
          <h2>Lớp học phần</h2>
          <span>{{ lopHocPhanTheoMon.length }} lớp</span>
        </div>

        <table>
          <thead>
          <tr>
            <th>Mã lớp</th>
            <th>Tên lớp</th>
            <th>Sĩ số</th>
            <th>Trạng thái</th>
            <th></th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="lop in lopHocPhanTheoMon" :key="lop.id" :class="{ selected: lopDangChon?.id === lop.id }">
            <td>{{ lop.maLop }}</td>
            <td>{{ lop.tenLop }}</td>
            <td>{{ lop.soLuongHienTai || 0 }}/{{ lop.soLuongToiDa || '-' }}</td>
            <td>{{ lop.trangThai }}</td>
            <td><button type="button" @click="chonLop(lop)">Xem</button></td>
          </tr>
          <tr v-if="!lopHocPhanTheoMon.length">
            <td colspan="5" class="empty">Chưa có lớp học phần cho môn đã chọn</td>
          </tr>
          </tbody>
        </table>
      </div>
    </section>

    <section v-if="lopDangChon" class="content-grid">
      <div class="panel">
        <div class="panel-title">
          <h2>Sinh viên trong lớp</h2>
          <span>{{ sinhVienTrongLop.length }} sinh viên</span>
        </div>

        <table>
          <thead>
          <tr>
            <th>Mã SV</th>
            <th>Họ tên</th>
            <th>Gmail</th>
            <th>Trạng thái</th>
            <th></th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="sv in sinhVienTrongLop" :key="sv.id">
            <td>{{ sv.maSinhVien }}</td>
            <td>{{ sv.hoTen }}</td>
            <td>{{ sv.email }}</td>
            <td>{{ sv.trangThai }}</td>
            <td>
              <button type="button" class="danger" @click="xoaSinhVienKhoiLop(sv)">
                Xóa khỏi lớp
              </button>
            </td>
          </tr>
          <tr v-if="!sinhVienTrongLop.length">
            <td colspan="5" class="empty">Lớp học phần chưa có sinh viên</td>
          </tr>
          </tbody>
        </table>
      </div>

      <div class="panel">
        <div class="panel-title">
          <h2>Sinh viên chưa vào lớp này</h2>
          <span>{{ sinhVienChuaVaoLop.length }} sinh viên</span>
        </div>

        <table>
          <thead>
          <tr>
            <th>Mã SV</th>
            <th>Họ tên</th>
            <th>Gmail</th>
            <th></th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="sv in sinhVienChuaVaoLop" :key="sv.id">
            <td>{{ sv.maSinhVien }}</td>
            <td>{{ sv.hoTen }}</td>
            <td>{{ sv.email }}</td>
            <td>
              <button type="button" @click="themSinhVienVaoLop(sv)">
                Thêm vào lớp
              </button>
            </td>
          </tr>
          <tr v-if="!sinhVienChuaVaoLop.length">
            <td colspan="4" class="empty">Không còn sinh viên phù hợp để thêm</td>
          </tr>
          </tbody>
        </table>
      </div>
    </section>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { sinhVienApi } from '../api/sinhVienApi'

const danhSachNganh = ref([])
const danhSachChuongTrinh = ref([])
const danhSachVersion = ref([])
const danhSachKhungKy = ref([])
const danhSachChuongTrinhMon = ref([])
const danhSachLopHocPhan = ref([])
const danhSachLopHocPhanChuongTrinhMon = ref([])
const danhSachDangKyLop = ref([])
const monDangChon = ref(null)
const lopDangChon = ref(null)
const sinhVienTrongLop = ref([])
const sinhVienChuaVaoLop = ref([])

const thongBao = ref('')
const thongBaoLoai = ref('success')

const boLoc = reactive({
  nganhId: '',
  chuongTrinhId: '',
  chuongTrinhVersionId: '',
  khungKyId: ''
})

const chuongTrinhTheoNganh = computed(() => {
  if (!boLoc.nganhId) return []
  return danhSachChuongTrinh.value.filter(item => String(item.nganhId) === String(boLoc.nganhId))
})

const versionTheoChuongTrinh = computed(() => {
  if (!boLoc.chuongTrinhId) return []
  return danhSachVersion.value.filter(item => String(item.chuongTrinhId) === String(boLoc.chuongTrinhId))
})

const khungKyTheoVersion = computed(() => {
  if (!boLoc.chuongTrinhVersionId) return []
  return danhSachKhungKy.value
      .filter(item => String(item.chuongTrinhVersionId) === String(boLoc.chuongTrinhVersionId))
      .sort((a, b) => Number(a.thuTu || 0) - Number(b.thuTu || 0))
})

const monTrongKy = computed(() => {
  if (!boLoc.chuongTrinhVersionId || !boLoc.khungKyId) return []
  return danhSachChuongTrinhMon.value
      .filter(item =>
          String(item.chuongTrinhVersionId) === String(boLoc.chuongTrinhVersionId)
          && String(item.khungKyId) === String(boLoc.khungKyId)
      )
      .sort((a, b) => Number(a.thuTu || 0) - Number(b.thuTu || 0))
})

const lopHocPhanTheoMon = computed(() => {
  if (!monDangChon.value?.id) return []

  return danhSachLopHocPhan.value.filter(lop => {
    if (laLopHocChung(lop)) {
      return danhSachLopHocPhanChuongTrinhMon.value.some(item => {
        return String(item.lopHocPhanId) === String(lop.id)
            && String(item.chuongTrinhMonId) === String(monDangChon.value.id)
      })
    }

    return String(lop.chuongTrinhMonId) === String(monDangChon.value.id)
  })
})
onMounted(async () => {
  await taiDuLieuBanDau()
})

async function taiDuLieuBanDau() {
  try {
    const [nganh, chuongTrinh, version, khungKy, chuongTrinhMon, lopHocPhan, lopHocPhanChuongTrinhMon, dangKyLop] = await Promise.all([
      sinhVienApi.nganh.getAll({ size: 1000 }),
      sinhVienApi.chuongTrinh.getAll({ size: 1000 }),
      sinhVienApi.chuongTrinhVersion.getAll({ size: 1000 }),
      sinhVienApi.khungKy.getAll({ size: 1000 }),
      sinhVienApi.chuongTrinhMon.getAll({ size: 1000 }),
      sinhVienApi.lopHocPhan.getAll({ size: 1000 }),
      sinhVienApi.lopHocPhanChuongTrinhMon.getAll({ size: 1000 }),
      sinhVienApi.sinhVienLopHocPhan.getAll({ size: 1000 })
    ])

    danhSachNganh.value = layDanhSach(nganh)
    danhSachChuongTrinh.value = layDanhSach(chuongTrinh)
    danhSachVersion.value = layDanhSach(version)
    danhSachKhungKy.value = layDanhSach(khungKy)
    danhSachChuongTrinhMon.value = layDanhSach(chuongTrinhMon)
    danhSachLopHocPhan.value = layDanhSach(lopHocPhan)
    danhSachLopHocPhanChuongTrinhMon.value = layDanhSach(lopHocPhanChuongTrinhMon)
    danhSachDangKyLop.value = layDanhSach(dangKyLop)
  } catch (error) {
    baoLoi(error?.message || 'Không tải được dữ liệu phân lớp học phần')
  }
}
function laLopHocChung(lop) {
  return String(lop?.loaiLopHocPhan || '').toUpperCase() === 'HOC_CHUNG'
}
function doiNganh() {
  boLoc.chuongTrinhId = ''
  doiChuongTrinh()
}

function doiChuongTrinh() {
  boLoc.chuongTrinhVersionId = ''
  doiVersion()
}

function doiVersion() {
  boLoc.khungKyId = ''
  doiKhungKy()
}

function doiKhungKy() {
  monDangChon.value = null
  lopDangChon.value = null
  sinhVienTrongLop.value = []
  sinhVienChuaVaoLop.value = []
}

function chonMon(mon) {
  monDangChon.value = mon
  lopDangChon.value = null
  sinhVienTrongLop.value = []
  sinhVienChuaVaoLop.value = []
}

async function chonLop(lop) {
  lopDangChon.value = lop
  await taiSinhVienTheoLop()
}

async function taiSinhVienTheoLop() {
  if (!lopDangChon.value?.id || !boLoc.chuongTrinhVersionId) return

  try {
    const [trongLop, chuaVaoLop, dangKyLop, lopHocPhan, lopHocPhanChuongTrinhMon] = await Promise.all([
      sinhVienApi.sinhVienLopHocPhan.getSinhVienTrongLop(lopDangChon.value.id),
      sinhVienApi.sinhVienLopHocPhan.getSinhVienChuaVaoLop(lopDangChon.value.id, boLoc.chuongTrinhVersionId),
      sinhVienApi.sinhVienLopHocPhan.getTheoLopHocPhan(lopDangChon.value.id),
      sinhVienApi.lopHocPhan.getAll({ size: 1000 }),
      sinhVienApi.lopHocPhanChuongTrinhMon.getAll({ size: 1000 })
    ])

    sinhVienTrongLop.value = layDanhSach(trongLop)
    sinhVienChuaVaoLop.value = layDanhSach(chuaVaoLop)
    danhSachDangKyLop.value = [
      ...danhSachDangKyLop.value.filter(item => String(item.lopHocPhanId) !== String(lopDangChon.value.id)),
      ...layDanhSach(dangKyLop)
    ]
    danhSachLopHocPhan.value = layDanhSach(lopHocPhan)
    danhSachLopHocPhanChuongTrinhMon.value = layDanhSach(lopHocPhanChuongTrinhMon)
    capNhatLopDangChon()
  } catch (error) {
    baoLoi(error?.message || 'Không tải được danh sách sinh viên theo lớp học phần')
  }
}

async function themSinhVienVaoLop(sinhVien) {
  if (!lopDangChon.value?.id) {
    baoLoi('Chưa chọn lớp học phần')
    return
  }

  try {
    await sinhVienApi.sinhVienLopHocPhan.create({
      sinhVienId: sinhVien.id,
      lopHocPhanId: lopDangChon.value.id,
      trangThai: 'da_dang_ky',
      laHocLai: false,
      ghiChu: ''
    })

    baoThanhCong('Đã thêm sinh viên vào lớp học phần')
    await taiSinhVienTheoLop()
  } catch (error) {
    baoLoi(error?.message || 'Không thêm được sinh viên vào lớp học phần')
  }
}

async function xoaSinhVienKhoiLop(sinhVien) {
  const dangKy = danhSachDangKyLop.value.find(item =>
      String(item.sinhVienId) === String(sinhVien.id)
      && String(item.lopHocPhanId) === String(lopDangChon.value?.id)
  )

  if (!dangKy?.id) {
    baoLoi('Không tìm thấy bản ghi sinh viên trong lớp học phần')
    return
  }

  if (!window.confirm(`Xóa sinh viên ${sinhVien.hoTen} khỏi lớp học phần này?`)) {
    return
  }

  try {
    await sinhVienApi.sinhVienLopHocPhan.delete(dangKy.id)
    baoThanhCong('Đã xóa sinh viên khỏi lớp học phần')
    await taiSinhVienTheoLop()
  } catch (error) {
    baoLoi(error?.message || 'Không xóa được sinh viên khỏi lớp học phần')
  }
}

function capNhatLopDangChon() {
  if (!lopDangChon.value?.id) return
  const lopMoi = danhSachLopHocPhan.value.find(item => String(item.id) === String(lopDangChon.value.id))
  if (lopMoi) lopDangChon.value = lopMoi
}

function layDanhSach(response) {
  if (Array.isArray(response)) return response
  if (Array.isArray(response?.content)) return response.content
  if (Array.isArray(response?.data?.content)) return response.data.content
  if (Array.isArray(response?.data)) return response.data
  return []
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
.sv-lhp-page {
  display: grid;
  gap: 18px;
}


.page-head h1 {
  margin: 0;
  font-size: 26px;
}

.page-head p {
  margin: 8px 0 0;
  max-width: 780px;
  color: #dbeafe;
}

.panel {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
  padding: 18px;
  box-shadow: 0 10px 30px rgba(15, 23, 42, 0.06);
}

.filter-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 14px;
}

.content-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 18px;
}

label {
  display: grid;
  gap: 8px;
  font-weight: 700;
  color: #334155;
}

select,
input {
  min-height: 42px;
  border: 1px solid #cbd5e1;
  border-radius: 12px;
  padding: 9px 12px;
  outline: none;
  background: #ffffff;
}

select:focus,
input:focus {
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.12);
}

.panel-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.panel-title h2 {
  margin: 0;
  font-size: 18px;
}

.panel-title span {
  color: #64748b;
  font-weight: 700;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  border-bottom: 1px solid #e2e8f0;
  padding: 10px;
  text-align: left;
  vertical-align: top;
}

th {
  font-size: 12px;
  text-transform: uppercase;
  color: #64748b;
  background: #f8fafc;
}

tr.selected td {
  background: #eff6ff;
}

button {
  border: 0;
  border-radius: 10px;
  padding: 8px 12px;
  font-weight: 700;
  cursor: pointer;
  background: #e0edff;
  color: #1d4ed8;
}

button:hover {
  filter: brightness(0.98);
}

button.danger {
  background: #fee2e2;
  color: #b91c1c;
}

.empty {
  text-align: center;
  color: #64748b;
  padding: 20px;
}

.message {
  border-radius: 14px;
  padding: 12px 14px;
  font-weight: 700;
}

.message.success {
  background: #ecfdf5;
  color: #047857;
  border: 1px solid #a7f3d0;
}

.message.error {
  background: #fef2f2;
  color: #b91c1c;
  border: 1px solid #fecaca;
}

@media (max-width: 1100px) {
  .filter-grid,
  .content-grid {
    grid-template-columns: 1fr;
  }
}
.page-title {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
  padding: 18px;
}

.page-title h2 {
  margin: 0;
  font-size: 22px;
  color: #0f172a;
}

.page-title p {
  margin: 6px 0 0;
  color: #64748b;
}
</style>