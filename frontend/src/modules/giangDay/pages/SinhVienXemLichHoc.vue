<template>
  <section class="schedule-page">
    <header class="page-head">
      <div>
        <p class="eyebrow">Sinh viên</p>
        <h1>Xem lịch học</h1>
        <p>Xem lịch học theo ma trận tuần: lớp/môn, giảng viên, phòng, ca học, thứ và ngày học.</p>
      </div>
      <div class="head-actions">
        <button type="button" class="btn" @click="veTuanHienTai">Tuần này</button>
        <button type="button" class="btn primary" @click="taiDuLieu">Tải lại</button>
      </div>
    </header>

    <section class="toolbar-card">
      <button type="button" class="btn icon" @click="doiTuan(-1)">‹</button>
      <label>
        <span>Tuần bắt đầu</span>
        <input v-model="boLoc.tuNgay" type="date" @change="chonTuanTuNgay" />
      </label>
      <button type="button" class="btn icon" @click="doiTuan(1)">›</button>
      <div class="range-text">{{ danhSachNgayTrongTuan[0]?.label }} - {{ danhSachNgayTrongTuan[6]?.label }}</div>
    </section>

    <div v-if="loi" class="alert error">{{ loi }}</div>

    <section class="matrix-card">
      <div class="matrix-head">
        <div>
          <h2>Ma trận lịch học tuần</h2>
          <p>Ô lịch hiển thị môn/lớp, giảng viên, phòng và trạng thái buổi học.</p>
        </div>
      </div>

      <div class="matrix-wrap">
        <div class="matrix-grid" :style="matrixGridStyle">
          <div class="matrix-corner">Ca / Ngày</div>
          <div v-for="ngay in danhSachNgayTrongTuan" :key="ngay.iso" class="matrix-day">
            <strong>{{ ngay.tenThu }}</strong>
            <span>{{ ngay.label }}</span>
          </div>

          <template v-for="ca in danhSachCaHoc" :key="ca.id">
            <div class="matrix-slot">
              <strong>{{ hienThiCa(ca) }}</strong>
              <span>{{ hienThiGioCa(ca) }}</span>
            </div>

            <div v-for="ngay in danhSachNgayTrongTuan" :key="`${ca.id}-${ngay.iso}`" class="matrix-cell">
              <span v-if="!lichTheoNgayVaCa(ngay.iso, ca.id).length" class="empty-text">Trống</span>
              <article
                v-for="item in lichTheoNgayVaCa(ngay.iso, ca.id)"
                :key="item.id"
                class="schedule-card"
                :class="trangThaiClass(item.trangThai)"
              >
                <div class="card-title">{{ item.tenLop || item.maLop || 'Lịch học' }}</div>
                <div class="card-line">GV: {{ item.tenGiaoVien || item.maGiaoVien || '-' }}</div>
                <div class="card-line">Phòng: {{ item.tenPhong || item.maPhong || '-' }}</div>
                <div class="card-line" v-if="item.noiDungBuoiHoc">{{ item.noiDungBuoiHoc }}</div>
                <div class="card-status">{{ hienThiTrangThai(item.trangThai) }}</div>
              </article>
            </div>
          </template>
        </div>
      </div>
    </section>

    <section class="list-card">
      <h2>Danh sách lịch trong tuần</h2>
      <div v-if="dangTai" class="empty-list">Đang tải lịch học...</div>
      <div v-else-if="!danhSach.length" class="empty-list">Chưa có lịch học trong tuần này.</div>
      <div v-else class="day-list">
        <article v-for="item in danhSachSapXep" :key="item.id" class="list-item">
          <strong>{{ hienThiThuNgay(item.ngayHoc) }} - {{ item.tenCa || item.maCa || 'Ca học' }}</strong>
          <span>{{ item.tenLop || item.maLop || '-' }} | GV: {{ item.tenGiaoVien || '-' }} | Phòng: {{ item.tenPhong || item.maPhong || '-' }}</span>
        </article>
      </div>
    </section>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { giangDayService } from '../services/giangDayService'

const danhSach = ref([])
const dangTai = ref(false)
const loi = ref('')
const boLoc = reactive({
  tuNgay: dauTuanIso(new Date()),
  denNgay: congNgayIso(dauTuanIso(new Date()), 6)
})

const danhSachNgayTrongTuan = computed(() => Array.from({ length: 7 }, (_, index) => {
  const iso = congNgayIso(boLoc.tuNgay, index)
  return { iso, tenThu: tenThuTheoIso(iso), label: dinhDangNgayNgan(iso) }
}))

const danhSachCaHoc = computed(() => {
  const map = new Map()
  danhSach.value.forEach((item) => {
    if (item.caHocId) {
      map.set(String(item.caHocId), {
        id: item.caHocId,
        maCa: item.maCa,
        tenCa: item.tenCa,
        gioBatDau: item.gioBatDau,
        gioKetThuc: item.gioKetThuc
      })
    }
  })
  return [...map.values()].sort((a, b) => thuTuCa(a) - thuTuCa(b))
})

const danhSachSapXep = computed(() => {
  return [...danhSach.value].sort((a, b) => `${a.ngayHoc || ''}-${thuTuCa(a)}`.localeCompare(`${b.ngayHoc || ''}-${thuTuCa(b)}`))
})

const matrixGridStyle = computed(() => ({ gridTemplateColumns: '145px repeat(7, minmax(180px, 1fr))' }))

onMounted(taiDuLieu)

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''
  try {
    const page = await giangDayService.layLichHocSinhVien({
      page: 0,
      size: 1000,
      tuNgay: boLoc.tuNgay,
      denNgay: boLoc.denNgay,
      sortBy: 'ngayHoc',
      sortDir: 'asc'
    })
    danhSach.value = page.content || []
  } catch (error) {
    loi.value = error?.message || 'Không tải được lịch học sinh viên'
  } finally {
    dangTai.value = false
  }
}

function lichTheoNgayVaCa(ngayHoc, caHocId) {
  return danhSach.value.filter((item) => item.ngayHoc === ngayHoc && String(item.caHocId) === String(caHocId))
}

function chonTuanTuNgay() {
  boLoc.tuNgay = dauTuanIso(boLoc.tuNgay ? new Date(`${boLoc.tuNgay}T00:00:00`) : new Date())
  boLoc.denNgay = congNgayIso(boLoc.tuNgay, 6)
  taiDuLieu()
}

function doiTuan(delta) {
  boLoc.tuNgay = congNgayIso(boLoc.tuNgay, delta * 7)
  boLoc.denNgay = congNgayIso(boLoc.tuNgay, 6)
  taiDuLieu()
}

function veTuanHienTai() {
  boLoc.tuNgay = dauTuanIso(new Date())
  boLoc.denNgay = congNgayIso(boLoc.tuNgay, 6)
  taiDuLieu()
}

function hienThiCa(ca) { return ca.tenCa || ca.maCa || `Ca ${thuTuCa(ca)}` }
function hienThiGioCa(item) { return item?.gioBatDau || item?.gioKetThuc ? `${item.gioBatDau || '?'} - ${item.gioKetThuc || '?'}` : '' }
function hienThiTrangThai(value) { return { du_kien: 'Dự kiến', da_day: 'Đã dạy', nghi: 'Nghỉ', day_bu: 'Dạy bù', doi_lich: 'Đổi lịch' }[value] || value || '-' }
function trangThaiClass(value) { return value === 'da_day' ? 'done' : ['nghi', 'day_bu', 'doi_lich'].includes(value) ? 'warning' : 'planned' }
function thuTuCa(ca) { const text = `${ca?.maCa || ''} ${ca?.tenCa || ''}`; const digits = text.replace(/\D+/g, ''); return digits ? Number(digits) : Number(ca?.thuTu || ca?.id || ca?.caHocId || 0) }
function tinhThuTrongTuan(ngayHoc) { const day = new Date(`${ngayHoc}T00:00:00`).getDay(); return day === 0 ? 8 : day + 1 }
function tenThuTheoIso(iso) { const thu = tinhThuTrongTuan(iso); return thu === 8 ? 'Chủ nhật' : `Thứ ${thu}` }
function hienThiThuNgay(ngayHoc) { return `${tenThuTheoIso(ngayHoc)}, ${dinhDangNgayNgan(ngayHoc)}` }
function dinhDangNgayNgan(iso) { const date = new Date(`${iso}T00:00:00`); return `${String(date.getDate()).padStart(2, '0')}/${String(date.getMonth() + 1).padStart(2, '0')}` }
function formatIso(date) { return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}` }
function dauTuanIso(date) { const current = new Date(date); const day = current.getDay(); current.setDate(current.getDate() + (day === 0 ? -6 : 1 - day)); return formatIso(current) }
function congNgayIso(iso, days) { const date = new Date(`${iso}T00:00:00`); date.setDate(date.getDate() + days); return formatIso(date) }
</script>

<style scoped>
.schedule-page { display: flex; flex-direction: column; gap: 16px; }
.page-head { display: flex; justify-content: space-between; gap: 16px; align-items: flex-start; }
.eyebrow { margin: 0 0 4px; color: #2563eb; font-weight: 900; text-transform: uppercase; font-size: 12px; }
h1, h2 { margin: 0; color: #0f172a; }
p { margin: 6px 0 0; color: #64748b; }
.head-actions, .toolbar-card { display: flex; gap: 10px; align-items: end; flex-wrap: wrap; }
.toolbar-card, .matrix-card, .list-card { padding: 16px; border: 1px solid var(--color-border, #e2e8f0); background: #fff; border-radius: var(--radius, 14px); }
label { display: grid; gap: 6px; }
label span { font-size: 13px; font-weight: 800; color: #334155; }
input { border: 1px solid #cbd5e1; border-radius: 10px; padding: 9px 10px; }
.btn { border: 1px solid #cbd5e1; background: #fff; border-radius: 10px; padding: 9px 12px; cursor: pointer; font-weight: 800; }
.btn.primary { background: #2563eb; border-color: #2563eb; color: white; }
.btn.icon { width: 38px; height: 38px; padding: 0; font-size: 22px; }
.range-text { color: #475569; font-weight: 800; padding-bottom: 10px; }
.alert.error { padding: 11px 13px; border-radius: 12px; background: #fee2e2; color: #991b1b; font-weight: 700; }
.matrix-head { margin-bottom: 12px; }
.matrix-wrap { overflow: auto; border: 1px solid #e2e8f0; border-radius: 12px; }
.matrix-grid { display: grid; min-width: 1400px; }
.matrix-corner, .matrix-day, .matrix-slot, .matrix-cell { border-right: 1px solid #e2e8f0; border-bottom: 1px solid #e2e8f0; }
.matrix-corner, .matrix-day, .matrix-slot { background: #f8fafc; padding: 12px; }
.matrix-corner { font-weight: 900; color: #475569; }
.matrix-day, .matrix-slot { display: grid; gap: 3px; }
.matrix-day span, .matrix-slot span { color: #64748b; font-size: 12px; }
.matrix-cell { min-height: 112px; padding: 8px; display: grid; gap: 8px; align-content: start; background: #fff; }
.empty-text { color: #cbd5e1; font-weight: 800; text-align: center; padding-top: 28px; }
.schedule-card { border-left: 4px solid #2563eb; border-radius: 12px; padding: 9px; background: #eff6ff; display: grid; gap: 4px; }
.schedule-card.done { border-left-color: #059669; background: #ecfdf5; }
.schedule-card.warning { border-left-color: #f59e0b; background: #fffbeb; }
.card-title { color: #0f172a; font-weight: 900; }
.card-line { color: #475569; font-size: 12px; }
.card-status { color: #334155; font-size: 11px; font-weight: 900; text-transform: uppercase; }
.day-list { display: grid; gap: 8px; }
.list-item { border: 1px solid #e2e8f0; border-radius: 12px; padding: 10px 12px; display: grid; gap: 4px; }
.list-item span, .empty-list { color: #64748b; }
@media (max-width: 760px) { .page-head { flex-direction: column; } }
</style>
