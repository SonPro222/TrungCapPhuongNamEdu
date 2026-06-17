<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Hồ sơ giáo viên</h1>
        <p>Xem thông tin giáo viên và các lớp học phần đang được phân công.</p>
      </div>

      <button type="button" class="btn" @click="quayLai">
        Quay lại danh sách
      </button>
    </header>

    <div v-if="dangTai" class="alert info">
      Đang tải dữ liệu...
    </div>

    <div v-if="loi" class="alert error">
      {{ loi }}
    </div>

    <template v-if="giaoVien">
      <section class="profile-card">
        <div>
          <span>Mã giáo viên</span>
          <strong>{{ giaoVien.maGiaoVien }}</strong>
        </div>

        <div>
          <span>Họ tên</span>
          <strong>{{ giaoVien.hoTen }}</strong>
        </div>

        <div>
          <span>Email</span>
          <strong>{{ giaoVien.email }}</strong>
        </div>

        <div>
          <span>Số điện thoại</span>
          <strong>{{ giaoVien.soDienThoai || '-' }}</strong>
        </div>

        <div>
          <span>Trạng thái</span>
          <strong>{{ hienThiTrangThaiGiaoVien(giaoVien.trangThai) }}</strong>
        </div>

        <div>
          <span>Tài khoản ID</span>
          <strong>{{ giaoVien.taiKhoanId || '-' }}</strong>
        </div>

        <div class="full">
          <span>Chuyên môn</span>
          <strong>{{ giaoVien.chuyenMon || '-' }}</strong>
        </div>
      </section>

      <section class="summary-grid">
        <div>
          <span>Tổng phân công</span>
          <strong>{{ danhSachLopDangDay.length }}</strong>
        </div>

        <div>
          <span>Lớp đang học / đang mở</span>
          <strong>{{ tongLopDangHoatDong }}</strong>
        </div>

        <div>
          <span>Lớp đã kết thúc</span>
          <strong>{{ tongLopDaKetThuc }}</strong>
        </div>
      </section>

      <section class="table-card">
        <div class="section-head">
          <div>
            <h2>Lớp giáo viên đang được phân công</h2>
            <p>Theo dõi lớp học phần, vai trò, ngành, chương trình, version và kỳ liên quan.</p>
          </div>

          <button type="button" class="btn" @click="taiDuLieu">
            Tải lại
          </button>
        </div>

        <div class="table-wrap">
          <table>
            <thead>
            <tr>
              <th>Mã lớp</th>
              <th>Tên lớp</th>
              <th>Vai trò</th>
              <th>Trạng thái lớp</th>
              <th>Ngành</th>
              <th>Chương trình</th>
              <th>Version</th>
              <th>Kỳ</th>
              <th>Ngày học</th>
              <th>Thao tác</th>
            </tr>
            </thead>

            <tbody>
            <tr v-if="!danhSachLopDangDay.length">
              <td colspan="10">Giáo viên chưa có phân công giảng dạy.</td>
            </tr>

            <tr v-for="item in danhSachLopDangDay" v-else :key="item.key">
              <td>{{ item.maLop || '-' }}</td>
              <td>{{ item.tenLop || '-' }}</td>
              <td>{{ hienThiVaiTro(item.vaiTro) }}</td>
              <td>{{ hienThiTrangThaiLop(item.trangThaiLop) }}</td>
              <td>{{ item.tenNganh || '-' }}</td>
              <td>{{ item.tenChuongTrinh || '-' }}</td>
              <td>{{ item.tenVersion || '-' }}</td>
              <td>{{ item.tenKy || '-' }}</td>
              <td>{{ hienThiKhoangNgay(item.ngayBatDau, item.ngayKetThuc) }}</td>
              <td>
                <button
                    type="button"
                    class="btn small"
                    :disabled="!item.coLopHocPhan"
                    @click="xemBuoiHoc(item)"
                >
                  Buổi học
                </button>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </section>
    </template>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { giangDayService } from '../services/giangDayService'

const route = useRoute()
const router = useRouter()

const giaoVienId = computed(() => Number(route.params.id))
const giaoVien = ref(null)
const danhSachPhanCong = ref([])
const danhSachLopHocPhan = ref([])
const danhSachLopHocPhanChuongTrinhMon = ref([])
const danhSachChuongTrinhMon = ref([])
const danhSachChuongTrinhVersion = ref([])
const danhSachChuongTrinh = ref([])
const danhSachNganh = ref([])
const danhSachKhungKy = ref([])
const dangTai = ref(false)
const loi = ref('')

const danhSachLopDangDay = computed(() => {
  return danhSachPhanCong.value
      .filter((phanCong) => Number(phanCong.giaoVienId) === giaoVienId.value)
      .flatMap((phanCong) => {
        const lopHocPhan = timLopHocPhan(phanCong.lopHocPhanId)
        const chuongTrinhMonList = layChuongTrinhMonCuaLop(lopHocPhan)

        if (!chuongTrinhMonList.length) {
          return [taoDongPhanCong(phanCong, lopHocPhan, null, 0)]
        }

        return chuongTrinhMonList.map((chuongTrinhMon, index) => {
          return taoDongPhanCong(phanCong, lopHocPhan, chuongTrinhMon, index)
        })
      })
})

const tongLopDangHoatDong = computed(() => {
  return danhSachLopDangDay.value.filter((item) => {
    return ['du_kien', 'dang_mo', 'dang_hoc'].includes(String(item.trangThaiLop || '').toLowerCase())
  }).length
})

const tongLopDaKetThuc = computed(() => {
  return danhSachLopDangDay.value.filter((item) => String(item.trangThaiLop || '').toLowerCase() === 'da_ket_thuc').length
})

onMounted(() => {
  taiDuLieu()
})

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''

  try {
    const [
      giaoVienResult,
      phanCongPage,
      lopHocPhanPage,
      lopHocPhanChuongTrinhMonPage,
      chuongTrinhMonList,
      versionList,
      chuongTrinhList,
      nganhList,
      khungKyList
    ] = await Promise.all([
      giangDayService.layGiaoVienTheoId(giaoVienId.value),
      giangDayService.layTrangPhanCongGiangDay({ size: 1000 }),
      giangDayService.layTrangLopHocPhan({ size: 1000 }),
      giangDayService.layTrangLopHocPhanChuongTrinhMon({ size: 1000 }),
      giangDayService.layDanhSachChuongTrinhMon({ size: 1000 }),
      giangDayService.layDanhSachChuongTrinhVersion({ size: 1000 }),
      giangDayService.layDanhSachChuongTrinh({ size: 1000 }),
      giangDayService.layDanhSachNganh({ size: 1000 }),
      giangDayService.layDanhSachKhungKy({ size: 1000 })
    ])

    giaoVien.value = giaoVienResult
    danhSachPhanCong.value = phanCongPage.content
    danhSachLopHocPhan.value = lopHocPhanPage.content
    danhSachLopHocPhanChuongTrinhMon.value = lopHocPhanChuongTrinhMonPage.content
    danhSachChuongTrinhMon.value = chuongTrinhMonList
    danhSachChuongTrinhVersion.value = versionList
    danhSachChuongTrinh.value = chuongTrinhList
    danhSachNganh.value = nganhList
    danhSachKhungKy.value = khungKyList
  } catch (error) {
    loi.value = error?.message || 'Không tải được hồ sơ giáo viên'
  } finally {
    dangTai.value = false
  }
}

function taoDongPhanCong(phanCong, lopHocPhan, chuongTrinhMon, index) {
  const version = timVersion(chuongTrinhMon?.chuongTrinhVersionId)
  const chuongTrinh = timChuongTrinh(version?.chuongTrinhId)
  const nganh = timNganh(chuongTrinh?.nganhId)
  const khungKy = timKhungKy(chuongTrinhMon?.khungKyId)

  return {
    key: `${phanCong.id}-${lopHocPhan?.id || 'lop'}-${chuongTrinhMon?.id || 'ctm'}-${index}`,
    phanCongId: phanCong.id,
    lopHocPhanId: lopHocPhan?.id || phanCong.lopHocPhanId,
    coLopHocPhan: Boolean(lopHocPhan?.id),
    maLop: lopHocPhan?.maLop || phanCong.maLop,
    tenLop: lopHocPhan?.tenLop || phanCong.tenLop,
    vaiTro: phanCong.vaiTro,
    trangThaiLop: lopHocPhan?.trangThai,
    ngayBatDau: lopHocPhan?.ngayBatDau,
    ngayKetThuc: lopHocPhan?.ngayKetThuc,
    tenNganh: nganh?.tenNganh || nganh?.ten || nganh?.name,
    tenChuongTrinh: chuongTrinh?.tenChuongTrinh || chuongTrinh?.ten || chuongTrinh?.name,
    tenVersion: version?.tenVersion || version?.maVersion || version?.ten || version?.name,
    tenKy: khungKy?.tenKhungKy || khungKy?.tenKy || khungKy?.ten || khungKy?.name
  }
}

function layChuongTrinhMonCuaLop(lopHocPhan) {
  if (!lopHocPhan) return []

  if (lopHocPhan.chuongTrinhMonId) {
    const chuongTrinhMon = timChuongTrinhMon(lopHocPhan.chuongTrinhMonId)
    return chuongTrinhMon ? [chuongTrinhMon] : []
  }

  const ids = danhSachLopHocPhanChuongTrinhMon.value
      .filter((item) => Number(item.lopHocPhanId) === Number(lopHocPhan.id))
      .map((item) => Number(item.chuongTrinhMonId))
      .filter(Boolean)

  return ids
      .map((id) => timChuongTrinhMon(id))
      .filter(Boolean)
}

function timLopHocPhan(id) {
  return danhSachLopHocPhan.value.find((item) => Number(item.id) === Number(id))
}

function timChuongTrinhMon(id) {
  return danhSachChuongTrinhMon.value.find((item) => Number(item.id) === Number(id))
}

function timVersion(id) {
  return danhSachChuongTrinhVersion.value.find((item) => Number(item.id) === Number(id))
}

function timChuongTrinh(id) {
  return danhSachChuongTrinh.value.find((item) => Number(item.id) === Number(id))
}

function timNganh(id) {
  return danhSachNganh.value.find((item) => Number(item.id) === Number(id))
}

function timKhungKy(id) {
  return danhSachKhungKy.value.find((item) => Number(item.id) === Number(id))
}

function xemBuoiHoc(item) {
  if (!item?.coLopHocPhan || !item?.lopHocPhanId) {
    loi.value = 'Phân công này không còn tham chiếu lớp học phần hợp lệ nên không thể mở điều phối giảng dạy.'
    return
  }

  router.push({
    name: 'GiangDay.ChiTietLopHocPhan',
    params: {
      id: item.lopHocPhanId
    }
  })
}

function quayLai() {
  router.push({ name: 'GiangDay.GiaoVien' })
}

function hienThiTrangThaiGiaoVien(value) {
  if (value === 'dang_day') return 'Đang dạy'
  if (value === 'tam_nghi') return 'Tạm nghỉ'
  if (value === 'nghi_viec') return 'Nghỉ việc'
  return value || '-'
}

function hienThiTrangThaiLop(value) {
  if (value === 'du_kien') return 'Dự kiến'
  if (value === 'dang_mo') return 'Đang mở'
  if (value === 'dang_hoc') return 'Đang học'
  if (value === 'da_ket_thuc') return 'Đã kết thúc'
  if (value === 'huy') return 'Hủy'
  return value || '-'
}

function hienThiVaiTro(value) {
  if (value === 'giang_vien_chinh') return 'Giảng viên chính'
  if (value === 'tro_giang') return 'Trợ giảng'
  if (value === 'coi_thi') return 'Coi thi'
  if (value === 'cham_thi') return 'Chấm thi'
  return value || '-'
}

function hienThiKhoangNgay(ngayBatDau, ngayKetThuc) {
  if (!ngayBatDau && !ngayKetThuc) return '-'
  if (ngayBatDau && ngayKetThuc) return `${ngayBatDau} → ${ngayKetThuc}`
  return ngayBatDau || ngayKetThuc
}
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-head,
.section-head {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: flex-start;
}

.page-head h1,
.section-head h2 {
  margin: 0;
}

.page-head p,
.section-head p {
  margin: 6px 0 0;
  color: #64748b;
}

.profile-card,
.table-card {
  padding: 16px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  background: var(--color-white);
}

.profile-card {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
}

.profile-card div {
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  padding: 12px;
  background: #f8fafc;
}

.profile-card .full {
  grid-column: 1 / -1;
}

.profile-card span,
.summary-grid span {
  display: block;
  color: #64748b;
  font-size: 13px;
  margin-bottom: 6px;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
}

.summary-grid div {
  border: 1px solid var(--color-border);
  background: var(--color-white);
  border-radius: var(--radius);
  padding: 12px;
}

.alert {
  padding: 10px 12px;
  border-radius: var(--radius);
}

.alert.info {
  background: #eff6ff;
  color: #1d4ed8;
}

.alert.error {
  background: #fee2e2;
  color: #991b1b;
}

.btn {
  border: 1px solid var(--color-border);
  background: var(--color-white);
  color: var(--color-text);
  border-radius: var(--radius);
  padding: 8px 12px;
  cursor: pointer;
  font-weight: 700;
}

.btn.small {
  padding: 6px 10px;
  font-size: 12px;
}

.table-wrap {
  overflow-x: auto;
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  margin-top: 12px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  padding: 10px 12px;
  border-bottom: 1px solid var(--color-border);
  text-align: left;
  white-space: nowrap;
}

th {
  background: #f8fafc;
  font-size: 12px;
  text-transform: uppercase;
}
</style>
