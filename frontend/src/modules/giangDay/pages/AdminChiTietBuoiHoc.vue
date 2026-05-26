<template>
  <section class="page">
    <header class="page-head">
      <div>
        <button type="button" class="btn ghost" @click="quayLaiDanhSachBuoi">
          ← Quay lại danh sách buổi học
        </button>

        <h1>Chi tiết buổi học</h1>

        <p v-if="lopHocPhan && lichHoc">
          {{ lopHocPhan.maLop }} - {{ lopHocPhan.tenLop }} / {{ tieuDeBuoiHoc(lichHoc) }}
        </p>
      </div>

      <button type="button" class="btn" @click="taiDuLieu">
        Tải lại
      </button>
    </header>

    <div v-if="loi" class="alert error">
      {{ loi }}
    </div>

    <section v-if="lopHocPhan && lichHoc" class="class-strip">
      <div class="class-main">
        <strong>{{ lopHocPhan.maLop || '-' }}</strong>
        <span>{{ lopHocPhan.tenLop || '-' }}</span>
      </div>

      <div class="class-meta">
        <span><b>Môn:</b> {{ tenMonHocTheoLop(lopHocPhan) }}</span>
        <span><b>Kỳ:</b> {{ tenKyTheoLop(lopHocPhan) }}</span>
        <span><b>Ngày học:</b> {{ lichHoc.ngayHoc || '-' }}</span>
        <span><b>Ca:</b> {{ lichHoc.tenCa || lichHoc.maCa || lichHoc.caHocId || '-' }}</span>
        <span><b>Phòng:</b> {{ lichHoc.tenPhong || lichHoc.maPhong || lichHoc.phongHocId || '-' }}</span>
        <span><b>Sinh viên:</b> {{ danhSachSinhVienTrongLop.length }}</span>
        <span><b>Cột điểm:</b> {{ cotDiemTheoLop.length }}</span>
      </div>
    </section>

    <section class="table-card">
      <div class="table-head">
        <div>
          <h2>Điểm danh buổi học</h2>
          <p>
            Đây là màn hình đào tạo xem. Giáo viên có quyền cập nhật điểm danh ở màn hình dành cho giáo viên.
          </p>
        </div>

        <label class="search-box">
          <span>Tìm sinh viên</span>
          <input
              v-model.trim="tuKhoaSinhVien"
              type="text"
              placeholder="MSSV, họ tên, gmail"
          />
        </label>
      </div>

      <div class="table-wrap">
        <table>
          <thead>
          <tr>
            <th>STT</th>
            <th>MSSV</th>
            <th>Họ tên</th>
            <th>Gmail</th>
            <th>Trạng thái điểm danh</th>
            <th>Thời gian điểm danh</th>
            <th>Ghi chú</th>
          </tr>
          </thead>

          <tbody>
          <tr v-if="dangTai">
            <td colspan="7" class="empty">
              Đang tải dữ liệu...
            </td>
          </tr>

          <tr v-else-if="danhSachSinhVienHienThi.length === 0">
            <td colspan="7" class="empty">
              Buổi học này chưa có sinh viên.
            </td>
          </tr>

          <tr v-for="(sv, index) in danhSachSinhVienHienThi" :key="sv.id">
            <td>{{ index + 1 }}</td>
            <td>{{ sv.maSinhVien || '-' }}</td>
            <td>{{ sv.hoTen || '-' }}</td>
            <td>{{ sv.email || sv.gmail || '-' }}</td>
            <td>
              <span :class="['attendance', classDiemDanh(layDiemDanh(sv.id).trangThai)]">
                {{ hienThiDiemDanh(layDiemDanh(sv.id).trangThai) }}
              </span>
            </td>
            <td>{{ layDiemDanh(sv.id).thoiGianDiemDanh || '-' }}</td>
            <td>{{ layDiemDanh(sv.id).ghiChu || '-' }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </section>

    <section class="table-card">
      <div class="table-head">
        <div>
          <h2>Danh sách sinh viên - điểm của lớp</h2>
          <p>Cột điểm lấy theo cấu hình đánh giá của lớp học phần.</p>
        </div>
      </div>

      <div class="table-wrap bang-diem-wrap">
        <table class="bang-diem">
          <thead>
          <tr>
            <th class="sticky-col col-stt">STT</th>
            <th class="sticky-col col-mssv">MSSV</th>
            <th class="sticky-col col-hoten">Họ tên</th>
            <th class="col-email">Gmail</th>

            <th
                v-for="cotDiem in cotDiemTheoLop"
                :key="cotDiem.id"
                class="col-diem"
                :title="tieuDeCotDiem(cotDiem)"
            >
              {{ tenCotDiem(cotDiem) }}
            </th>

            <th class="col-diem">Điểm chuyên cần</th>
            <th class="col-diem">Điểm quá trình</th>
            <th class="col-diem">Điểm thi</th>
            <th class="col-diem">Điểm tổng</th>
            <th class="col-diem">Điểm quy đổi</th>
            <th class="col-ket-qua">Kết quả</th>
          </tr>
          </thead>

          <tbody>
          <tr v-if="dangTai">
            <td :colspan="tongSoCotBangDiem" class="empty">
              Đang tải dữ liệu...
            </td>
          </tr>

          <tr v-else-if="danhSachSinhVienHienThi.length === 0">
            <td :colspan="tongSoCotBangDiem" class="empty">
              Lớp học phần này chưa có sinh viên.
            </td>
          </tr>

          <tr v-for="(sv, index) in danhSachSinhVienHienThi" :key="`diem-${sv.id}`">
            <td class="sticky-col col-stt">{{ index + 1 }}</td>
            <td class="sticky-col col-mssv">{{ sv.maSinhVien || '-' }}</td>
            <td class="sticky-col col-hoten">{{ sv.hoTen || '-' }}</td>
            <td class="col-email">{{ sv.email || sv.gmail || '-' }}</td>

            <td
                v-for="cotDiem in cotDiemTheoLop"
                :key="`${sv.id}-${cotDiem.id}`"
                class="col-diem"
            >
              {{ hienThiDiem(layDiemTheoCot(sv.id, cotDiem.id).diem) }}
            </td>

            <td class="col-diem">{{ hienThiDiem(layKetQua(sv.id).diemChuyenCan) }}</td>
            <td class="col-diem">{{ hienThiDiem(layKetQua(sv.id).diemQuaTrinh) }}</td>
            <td class="col-diem">{{ hienThiDiem(layKetQua(sv.id).diemThi) }}</td>
            <td class="col-diem">{{ hienThiDiem(layKetQua(sv.id).diemTongKet) }}</td>
            <td class="col-diem">{{ hienThiDiem(layKetQua(sv.id).diemQuyDoi) }}</td>
            <td class="col-ket-qua">{{ hienThiKetQua(layKetQua(sv.id).ketQua) }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </section>

    <section class="note-card">
      <strong>Cột điểm:</strong>

      <span v-if="cotDiemTheoLop.length === 0">
        Chưa có cấu hình cột điểm cho lớp học phần này.
      </span>

      <span v-for="cotDiem in cotDiemTheoLop" :key="cotDiem.id">
        {{ tenCotDiem(cotDiem) }}: {{ tieuDeCotDiem(cotDiem) }}
      </span>
    </section>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { giangDayService } from '../services/giangDayService'

const route = useRoute()
const router = useRouter()

const lopHocPhanId = computed(() => Number(route.params.lopHocPhanId))
const lichHocId = computed(() => Number(route.params.lichHocId))

const dangTai = ref(false)
const loi = ref('')

const lopHocPhan = ref(null)
const lichHoc = ref(null)
const tuKhoaSinhVien = ref('')

const danhSachKhungKy = ref([])
const danhSachChuongTrinhMon = ref([])
const danhSachMonHoc = ref([])
const danhSachLopHocPhanChuongTrinhMon = ref([])
const danhSachSinhVienTrongLop = ref([])
const danhSachDiemDanh = ref([])
const danhSachKetQuaLop = ref([])
const danhSachCauHinhDanhGia = ref([])
const danhSachDiemChiTiet = ref([])

const cotDiemTheoLop = computed(() => {
  return danhSachCauHinhDanhGia.value
      .filter((item) => {
        if (item.lopHocPhanId !== undefined && item.lopHocPhanId !== null) {
          return Number(item.lopHocPhanId) === Number(lopHocPhanId.value)
        }

        if (item.lopHocPhan?.id !== undefined && item.lopHocPhan?.id !== null) {
          return Number(item.lopHocPhan.id) === Number(lopHocPhanId.value)
        }

        return true
      })
      .sort((a, b) => {
        const thuTuA = Number(a.thuTu || a.thuTuCot || 0)
        const thuTuB = Number(b.thuTu || b.thuTuCot || 0)

        if (thuTuA !== thuTuB) {
          return thuTuA - thuTuB
        }

        return Number(a.id || 0) - Number(b.id || 0)
      })
})

const danhSachSinhVienHienThi = computed(() => {
  const keyword = tuKhoaSinhVien.value.toLowerCase()

  if (!keyword) {
    return danhSachSinhVienTrongLop.value
  }

  return danhSachSinhVienTrongLop.value.filter((item) => {
    return `${item.maSinhVien || ''} ${item.hoTen || ''} ${item.email || ''} ${item.gmail || ''}`
        .toLowerCase()
        .includes(keyword)
  })
})

const tongSoCotBangDiem = computed(() => {
  return 4 + cotDiemTheoLop.value.length + 6
})

onMounted(() => {
  taiDuLieu()
})

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''

  try {
    const [
      lop,
      khungKy,
      chuongTrinhMon,
      monHoc,
      lopHocPhanChuongTrinhMon,
      lichHocPage,
      sinhVien,
      diemDanh,
      ketQua,
      cauHinhDanhGia,
      diemChiTiet
    ] = await Promise.all([
      giangDayService.layLopHocPhanTheoId(lopHocPhanId.value),
      giangDayService.layDanhSachKhungKy(),
      giangDayService.layDanhSachChuongTrinhMon(),
      giangDayService.layDanhSachMonHoc(),
      giangDayService.layTrangLopHocPhanChuongTrinhMon({ size: 500 }),
      giangDayService.layTrangLichHoc({ lopHocPhanId: lopHocPhanId.value, size: 500 }),
      giangDayService.layDanhSachSinhVienTrongLopHocPhan(lopHocPhanId.value, { size: 500 }),
      giangDayService.layTrangDiemDanh({ lopHocPhanId: lopHocPhanId.value, size: 3000 }),
      giangDayService.layTrangKetQuaLopHocPhan({ lopHocPhanId: lopHocPhanId.value, size: 500 }),
      giangDayService.layTrangCauHinhDanhGia({ lopHocPhanId: lopHocPhanId.value, size: 1000 }),
      giangDayService.layTrangDiemChiTiet({ lopHocPhanId: lopHocPhanId.value, size: 3000 })
    ])

    lopHocPhan.value = lop
    danhSachKhungKy.value = khungKy
    danhSachChuongTrinhMon.value = chuongTrinhMon
    danhSachMonHoc.value = monHoc
    danhSachLopHocPhanChuongTrinhMon.value = lopHocPhanChuongTrinhMon.content
    lichHoc.value = lichHocPage.content.find((item) => Number(item.id) === Number(lichHocId.value)) || null
    danhSachSinhVienTrongLop.value = sinhVien
    danhSachDiemDanh.value = diemDanh.content
    danhSachKetQuaLop.value = ketQua.content
    danhSachCauHinhDanhGia.value = cauHinhDanhGia.content
    danhSachDiemChiTiet.value = diemChiTiet.content
  } catch (error) {
    loi.value = error?.message || 'Không tải được chi tiết buổi học'
  } finally {
    dangTai.value = false
  }
}

function quayLaiDanhSachBuoi() {
  router.push({
    name: 'GiangDay.ChiTietLopHocPhan',
    params: {
      id: lopHocPhanId.value
    }
  })
}

function layChuongTrinhMonIdsTheoLop(lop) {
  if (!lop) return []

  const ids = []

  if (lop.chuongTrinhMonId) {
    ids.push(Number(lop.chuongTrinhMonId))
  }

  danhSachLopHocPhanChuongTrinhMon.value
      .filter((item) => Number(item.lopHocPhanId) === Number(lop.id))
      .forEach((item) => {
        if (item.chuongTrinhMonId) {
          ids.push(Number(item.chuongTrinhMonId))
        }
      })

  return [...new Set(ids)]
}

function layChuongTrinhMonTheoLop(lop) {
  const ids = layChuongTrinhMonIdsTheoLop(lop)

  return danhSachChuongTrinhMon.value.filter((item) => {
    return ids.includes(Number(item.id))
  })
}

function tenMonHocTheoLop(lop) {
  const ctm = layChuongTrinhMonTheoLop(lop)[0]
  const monHocId = lop?.monHocId || ctm?.monHocId

  const monHoc = danhSachMonHoc.value.find((item) => {
    return Number(item.id) === Number(monHocId)
  })

  return monHoc?.tenMonHoc || monHoc?.tenMon || monHoc?.name || monHocId || '-'
}

function tenKyTheoLop(lop) {
  const ctm = layChuongTrinhMonTheoLop(lop)[0]

  const khungKy = danhSachKhungKy.value.find((item) => {
    return Number(item.id) === Number(ctm?.khungKyId)
  })

  return khungKy?.tenKy || khungKy?.maKy || ctm?.khungKyId || '-'
}

function layKetQua(sinhVienId) {
  return danhSachKetQuaLop.value.find((item) => {
    return Number(item.sinhVienId) === Number(sinhVienId)
  }) || {}
}

function layDiemTheoCot(sinhVienId, cauHinhDanhGiaId) {
  return danhSachDiemChiTiet.value.find((item) => {
    return Number(item.sinhVienId) === Number(sinhVienId) &&
        Number(item.cauHinhDanhGiaId) === Number(cauHinhDanhGiaId)
  }) || {}
}

function layDiemDanh(sinhVienId) {
  return danhSachDiemDanh.value.find((item) => {
    return Number(item.sinhVienId) === Number(sinhVienId) &&
        Number(item.lichHocId) === Number(lichHocId.value)
  }) || {}
}

function tenCotDiem(cotDiem) {
  return cotDiem.tenCotDiem ||
      cotDiem.tenCot ||
      cotDiem.tenThanhPhan ||
      cotDiem.tenCauHinh ||
      `Cột ${cotDiem.thuTu || cotDiem.thuTuCot || cotDiem.id}`
}

function tieuDeCotDiem(cotDiem) {
  const tenCot = tenCotDiem(cotDiem)
  const tyLe = cotDiem.tyLe !== null && cotDiem.tyLe !== undefined ? ` - ${cotDiem.tyLe}%` : ''
  const diemToiDa = cotDiem.diemToiDa !== null && cotDiem.diemToiDa !== undefined ? ` - Tối đa ${cotDiem.diemToiDa}` : ''

  return `${tenCot}${tyLe}${diemToiDa}`
}

function tieuDeBuoiHoc(buoi) {
  if (!buoi) return '-'

  const ca = buoi.tenCa || buoi.maCa || buoi.caHocId || '-'
  const phong = buoi.tenPhong || buoi.maPhong || buoi.phongHocId || '-'
  const noiDung = buoi.noiDungBuoiHoc || 'Buổi học'

  return `${buoi.ngayHoc || '-'} - Ca ${ca} - Phòng ${phong} - ${noiDung}`
}

function hienThiDiem(value) {
  if (value === null || value === undefined || value === '') {
    return '-'
  }

  const numberValue = Number(value)

  if (Number.isNaN(numberValue)) {
    return value
  }

  return numberValue.toFixed(2).replace(/\.00$/, '')
}

function hienThiDiemDanh(value) {
  const map = {
    chua_diem_danh: 'Chưa nhập',
    co_mat: 'Có',
    vang_co_phep: 'Vắng phép',
    vang_khong_phep: 'Vắng',
    di_muon: 'Muộn'
  }

  return map[value] || 'Chưa nhập'
}

function classDiemDanh(value) {
  const map = {
    chua_diem_danh: 'none',
    co_mat: 'present',
    vang_co_phep: 'allowed',
    vang_khong_phep: 'absent',
    di_muon: 'late'
  }

  return map[value] || 'none'
}

function hienThiKetQua(value) {
  const map = {
    dat: 'Đạt',
    khong_dat: 'Không đạt'
  }

  return map[value] || value || '-'
}
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  gap: 14px;
  color: #344054;
  font-size: 14px;
}

.page-head,
.table-head {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: flex-start;
}

.page-head h1,
.table-head h2 {
  margin: 8px 0 0;
  color: #101828;
  font-size: 22px;
  font-weight: 650;
  line-height: 1.3;
}

.page-head p,
.table-head p {
  margin: 6px 0 0;
  color: #667085;
}

.class-strip,
.table-card,
.note-card {
  border: 1px solid #eaecf0;
  border-radius: 12px;
  background: #ffffff;
  padding: 14px;
}

.class-strip {
  display: grid;
  grid-template-columns: minmax(220px, 320px) 1fr;
  gap: 12px;
  align-items: center;
}

.class-main {
  display: grid;
  gap: 4px;
}

.class-main strong {
  color: #101828;
  font-size: 18px;
  font-weight: 700;
}

.class-main span {
  color: #475467;
  font-size: 14px;
  font-weight: 500;
}

.class-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.class-meta span {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  min-height: 30px;
  border: 1px solid #eaecf0;
  border-radius: 999px;
  background: #f9fafb;
  padding: 5px 10px;
  color: #475467;
  font-size: 13px;
  font-weight: 500;
}

.class-meta b {
  color: #101828;
  font-weight: 650;
}

.search-box {
  min-width: 260px;
}

label {
  display: flex;
  flex-direction: column;
  gap: 6px;
  color: #344054;
  font-size: 13px;
  font-weight: 500;
}

input {
  min-height: 38px;
  border: 1px solid #d0d5dd;
  border-radius: 10px;
  padding: 8px 10px;
  background: #ffffff;
  color: #344054;
  font-size: 14px;
}

.btn {
  border: 1px solid #d0d5dd;
  border-radius: 10px;
  background: #ffffff;
  color: #344054;
  padding: 9px 13px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
}

.btn:hover {
  background: #f9fafb;
}

.btn.ghost {
  margin-bottom: 4px;
}

.alert {
  padding: 10px 12px;
  border-radius: 10px;
  font-weight: 500;
}

.alert.error {
  background: #fef3f2;
  color: #b42318;
}

.table-wrap {
  overflow: auto;
  margin-top: 12px;
  border: 1px solid #eaecf0;
  border-radius: 12px;
}

.bang-diem-wrap {
  max-height: 620px;
}

table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
}

.bang-diem {
  table-layout: fixed;
  min-width: 1400px;
}

th,
td {
  border-bottom: 1px solid #eaecf0;
  border-right: 1px solid #f2f4f7;
  padding: 9px 10px;
  text-align: left;
  vertical-align: middle;
  white-space: nowrap;
  background: #ffffff;
  color: #344054;
  font-size: 13px;
  line-height: 1.35;
}

th {
  position: sticky;
  top: 0;
  z-index: 3;
  background: #f9fafb;
  color: #475467;
  font-size: 12px;
  font-weight: 600;
}

tbody tr:hover td {
  background: #fcfcfd;
}

.empty {
  text-align: center;
  color: #667085;
}

.sticky-col {
  position: sticky;
  z-index: 4;
  background: #ffffff;
  box-shadow: 1px 0 0 #eaecf0;
}

th.sticky-col {
  z-index: 5;
  background: #f9fafb;
}

.col-stt {
  left: 0;
  width: 64px;
  min-width: 64px;
  max-width: 64px;
  text-align: center;
}

.col-mssv {
  left: 64px;
  width: 130px;
  min-width: 130px;
  max-width: 130px;
}

.col-hoten {
  left: 194px;
  width: 240px;
  min-width: 240px;
  max-width: 240px;
}

.col-email {
  width: 230px;
  min-width: 230px;
}

.col-diem {
  width: 120px;
  min-width: 120px;
  text-align: center;
}

.col-ket-qua {
  width: 120px;
  min-width: 120px;
  text-align: center;
}

.attendance {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 76px;
  border-radius: 999px;
  padding: 4px 8px;
  font-size: 12px;
  font-weight: 500;
  background: #f2f4f7;
  color: #475467;
}

.attendance.present {
  background: #ecfdf3;
  color: #027a48;
}

.attendance.allowed {
  background: #eff8ff;
  color: #175cd3;
}

.attendance.absent {
  background: #fef3f2;
  color: #b42318;
}

.attendance.late {
  background: #fffaeb;
  color: #b54708;
}

.note-card {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  color: #344054;
  font-size: 13px;
}

.note-card strong {
  color: #101828;
  font-weight: 600;
}

.note-card span {
  color: #667085;
}

@media (max-width: 1100px) {
  .class-strip {
    grid-template-columns: 1fr;
  }

  .page-head,
  .table-head {
    flex-direction: column;
  }
}

@media (max-width: 700px) {
  .class-meta span {
    width: 100%;
    border-radius: 10px;
  }

  .search-box {
    width: 100%;
    min-width: 0;
  }
}
</style>