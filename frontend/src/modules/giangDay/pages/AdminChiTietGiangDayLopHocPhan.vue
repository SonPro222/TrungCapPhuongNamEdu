<template>
  <section class="page">
    <header class="page-head">
      <div>
        <button type="button" class="btn ghost" @click="quayLai">
          ← Quay lại danh sách lớp học phần
        </button>

        <h1>Danh sách buổi học</h1>

        <p v-if="lopHocPhan">
          Chọn một buổi học của lớp {{ lopHocPhan.maLop }} để xem danh sách sinh viên, điểm và điểm danh.
        </p>
      </div>

      <button type="button" class="btn" @click="taiDuLieu">
        Tải lại
      </button>
    </header>

    <div v-if="loi" class="alert error">
      {{ loi }}
    </div>

    <section v-if="lopHocPhan" class="class-strip">
      <div class="class-main">
        <strong>{{ lopHocPhan.maLop || '-' }}</strong>
        <span>{{ lopHocPhan.tenLop || '-' }}</span>
      </div>

      <div class="class-meta">
        <span><b>Môn:</b> {{ tenMonHocTheoLop(lopHocPhan) }}</span>
        <span><b>Kỳ:</b> {{ tenKyTheoLop(lopHocPhan) }}</span>
        <span><b>Loại:</b> {{ hienThiLoaiLop(lopHocPhan.loaiLopHocPhan) }}</span>
        <span><b>Sĩ số:</b> {{ lopHocPhan.soLuongHienTai ?? 0 }}/{{ lopHocPhan.soLuongToiDa || '-' }}</span>
        <span><b>Buổi:</b> {{ lichHocTheoLop.length }}/{{ lopHocPhan.soBuoiHoc || '-' }}</span>
        <span><b>Cột điểm:</b> {{ cotDiemTheoLop.length }}</span>
      </div>
    </section>

    <section class="table-card">
      <div class="table-head">
        <div>
          <h2>Buổi học của lớp</h2>
          <p>Bấm vào từng buổi để xem danh sách sinh viên, điểm của lớp và điểm danh đúng buổi đó.</p>
        </div>
      </div>

      <div v-if="dangTai" class="empty-box">
        Đang tải dữ liệu...
      </div>

      <div v-else-if="lichHocTheoLop.length === 0" class="empty-box">
        Lớp học phần này chưa có buổi học. Cần xếp lịch học trước khi xem điểm danh.
      </div>

      <div v-else class="buoi-grid">
        <article
            v-for="(buoi, index) in lichHocTheoLop"
            :key="buoi.id"
            class="buoi-card"
        >
          <div class="buoi-top">
            <strong>B{{ index + 1 }}</strong>
            <span :class="['status', classTrangThaiLichHoc(buoi.trangThai)]">
              {{ hienThiTrangThaiLichHoc(buoi.trangThai) }}
            </span>
          </div>

          <h3>{{ buoi.ngayHoc || '-' }}</h3>

          <div class="buoi-info">
            <span><b>Ca:</b> {{ buoi.tenCa || buoi.maCa || buoi.caHocId || '-' }}</span>
            <span><b>Phòng:</b> {{ buoi.tenPhong || buoi.maPhong || buoi.phongHocId || '-' }}</span>
            <span><b>Giáo viên:</b> {{ buoi.tenGiaoVien || buoi.giaoVienId || '-' }}</span>
            <span><b>Nội dung:</b> {{ buoi.noiDungBuoiHoc || 'Buổi học' }}</span>
          </div>

          <button type="button" class="btn primary full" @click="xemBuoiHoc(buoi)">
            Xem buổi học
          </button>
        </article>
      </div>
    </section>

    <section class="note-card">
      <strong>Cột điểm của lớp:</strong>

      <span v-if="cotDiemTheoLop.length === 0">
        Chưa có cấu hình cột điểm.
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

const lopHocPhanId = computed(() => Number(route.params.id))

const dangTai = ref(false)
const loi = ref('')

const lopHocPhan = ref(null)
const danhSachKhungKy = ref([])
const danhSachChuongTrinhMon = ref([])
const danhSachMonHoc = ref([])
const danhSachLopHocPhanChuongTrinhMon = ref([])
const danhSachLichHoc = ref([])
const danhSachCauHinhDanhGia = ref([])

const lichHocTheoLop = computed(() => {
  return danhSachLichHoc.value
      .filter((item) => Number(item.lopHocPhanId) === Number(lopHocPhanId.value))
      .sort((a, b) => {
        const ngayA = a.ngayHoc || ''
        const ngayB = b.ngayHoc || ''

        if (ngayA !== ngayB) {
          return ngayA.localeCompare(ngayB)
        }

        return Number(a.caHocId || 0) - Number(b.caHocId || 0)
      })
})

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
      lichHoc,
      cauHinhDanhGia
    ] = await Promise.all([
      giangDayService.layLopHocPhanTheoId(lopHocPhanId.value),
      giangDayService.layDanhSachKhungKy(),
      giangDayService.layDanhSachChuongTrinhMon(),
      giangDayService.layDanhSachMonHoc(),
      giangDayService.layTrangLopHocPhanChuongTrinhMon({ size: 500 }),
      giangDayService.layTrangLichHoc({ lopHocPhanId: lopHocPhanId.value, size: 500 }),
      giangDayService.layTrangCauHinhDanhGia({ lopHocPhanId: lopHocPhanId.value, size: 1000 })
    ])

    lopHocPhan.value = lop
    danhSachKhungKy.value = khungKy
    danhSachChuongTrinhMon.value = chuongTrinhMon
    danhSachMonHoc.value = monHoc
    danhSachLopHocPhanChuongTrinhMon.value = lopHocPhanChuongTrinhMon.content
    danhSachLichHoc.value = lichHoc.content
    danhSachCauHinhDanhGia.value = cauHinhDanhGia.content
  } catch (error) {
    loi.value = error?.message || 'Không tải được danh sách buổi học'
  } finally {
    dangTai.value = false
  }
}

function quayLai() {
  const nganhId = route.query.nganhId
  const chuongTrinhId = route.query.chuongTrinhId
  const versionId = route.query.versionId
  const khungKyId = route.query.khungKyId

  if (nganhId && chuongTrinhId && versionId && khungKyId) {
    router.push({
      name: 'GiangDay.LopHocPhanTheoKy',
      params: {
        nganhId,
        chuongTrinhId,
        versionId,
        khungKyId
      }
    })
    return
  }

  router.push({ name: 'GiangDay.LopHocPhan' })
}

function xemBuoiHoc(buoi) {
  router.push({
    name: 'GiangDay.ChiTietBuoiHoc',
    params: {
      lopHocPhanId: lopHocPhanId.value,
      lichHocId: buoi.id
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

function hienThiLoaiLop(value) {
  const map = {
    CHUYEN_NGANH: 'Chuyên ngành',
    HOC_CHUNG: 'Học chung'
  }

  return map[value] || value || '-'
}

function hienThiTrangThaiLichHoc(value) {
  const map = {
    du_kien: 'Dự kiến',
    da_day: 'Đã dạy',
    nghi: 'Nghỉ',
    day_bu: 'Dạy bù'
  }

  return map[value] || value || 'Dự kiến'
}

function classTrangThaiLichHoc(value) {
  const map = {
    du_kien: 'pending',
    da_day: 'done',
    nghi: 'cancel',
    day_bu: 'makeup'
  }

  return map[value] || 'pending'
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

.btn.primary {
  background: #2563eb;
  border-color: #2563eb;
  color: #ffffff;
}

.btn.primary:hover {
  background: #1d4ed8;
}

.btn.full {
  width: 100%;
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

.empty-box {
  margin-top: 12px;
  border: 1px dashed #d0d5dd;
  border-radius: 12px;
  background: #f9fafb;
  padding: 14px;
  color: #667085;
  text-align: center;
  font-weight: 500;
}

.buoi-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(230px, 1fr));
  gap: 12px;
  margin-top: 12px;
}

.buoi-card {
  border: 1px solid #eaecf0;
  border-radius: 12px;
  background: #ffffff;
  padding: 12px;
  display: grid;
  gap: 10px;
}

.buoi-card:hover {
  border-color: #bfdbfe;
  background: #f8fbff;
}

.buoi-top {
  display: flex;
  justify-content: space-between;
  gap: 8px;
  align-items: center;
}

.buoi-top strong {
  color: #2563eb;
  font-size: 16px;
  font-weight: 700;
}

.buoi-card h3 {
  margin: 0;
  color: #101828;
  font-size: 17px;
  font-weight: 700;
}

.buoi-info {
  display: grid;
  gap: 5px;
  color: #475467;
  font-size: 13px;
}

.buoi-info b {
  color: #101828;
}

.status {
  border-radius: 999px;
  padding: 4px 8px;
  font-size: 12px;
  font-weight: 600;
  background: #f2f4f7;
  color: #475467;
}

.status.done {
  background: #ecfdf3;
  color: #027a48;
}

.status.cancel {
  background: #fef3f2;
  color: #b42318;
}

.status.makeup {
  background: #eff8ff;
  color: #175cd3;
}

.status.pending {
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
}
</style>
