<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Ma trận lịch học</h1>
        <p>Xem lịch theo tuần, phòng, giáo viên hoặc lớp học phần để phát hiện trùng và thiếu lịch nhanh.</p>
      </div>

      <button type="button" class="btn" @click="taiDuLieu">Tải lại</button>
    </header>

    <div v-if="loi" class="alert error">{{ loi }}</div>

    <section class="table-card filter-card">
      <label>
        <span>Từ ngày</span>
        <input v-model="boLoc.tuNgay" type="date" />
      </label>

      <label>
        <span>Đến ngày</span>
        <input v-model="boLoc.denNgay" type="date" />
      </label>

      <label>
        <span>Kiểu xem</span>
        <select v-model="boLoc.kieuXem">
          <option value="phong">Theo phòng</option>
          <option value="giaoVien">Theo giáo viên</option>
          <option value="lop">Theo lớp</option>
        </select>
      </label>

      <label>
        <span>Từ khóa</span>
        <input v-model="boLoc.tuKhoa" type="search" placeholder="Tìm phòng, giáo viên, lớp..." />
      </label>
    </section>

    <section class="stat-grid">
      <article>
        <strong>{{ lichLoc.length }}</strong>
        <span>Buổi trong khoảng</span>
      </article>
      <article>
        <strong>{{ nhomHienThi.length }}</strong>
        <span>{{ tenKieuXem }}</span>
      </article>
      <article>
        <strong>{{ canhBaoTrung.length }}</strong>
        <span>Cảnh báo trùng</span>
      </article>
    </section>

    <section v-if="canhBaoTrung.length" class="alert error warning-list">
      <div v-for="msg in canhBaoTrung" :key="msg">{{ msg }}</div>
    </section>

    <section class="table-card">
      <div class="table-head">
        <div>
          <h2>Bảng ma trận</h2>
          <p>Mỗi ô là một ngày + ca. Bấm vào mã lớp trong ô để quay về điều phối lớp học phần.</p>
        </div>
      </div>

      <div v-if="dangTai" class="empty-box">Đang tải dữ liệu...</div>

      <div v-else class="matrix-wrap">
        <table class="matrix-table">
          <thead>
            <tr>
              <th class="sticky-col">{{ tenKieuXem }}</th>
              <th v-for="cot in cotMaTran" :key="cot.key">
                <span>{{ hienThiNgay(cot.ngay) }}</span>
                <small>{{ tenCaTheoId(cot.caHocId) }}</small>
              </th>
            </tr>
          </thead>

          <tbody>
            <tr v-if="!nhomHienThi.length">
              <td :colspan="cotMaTran.length + 1" class="empty">Không có lịch trong khoảng đang chọn.</td>
            </tr>

            <tr v-for="nhom in nhomHienThi" :key="nhom.key">
              <th class="sticky-col row-title">{{ nhom.ten }}</th>
              <td v-for="cot in cotMaTran" :key="`${nhom.key}-${cot.key}`" :class="{ conflict: layItems(nhom, cot).length > 1 }">
                <button
                    v-for="item in layItems(nhom, cot)"
                    :key="item.id"
                    type="button"
                    class="schedule-pill"
                    @click="diDenLop(item)"
                >
                  <strong>{{ item.maLop || item.lopHocPhanId }}</strong>
                  <span>{{ item.maPhong || tenPhongTheoId(item.phongHocId) }}</span>
                  <small>{{ item.tenGiaoVien || tenGiaoVienTheoId(item.giaoVienId) }}</small>
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
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { giangDayService } from '../services/giangDayService'

const router = useRouter()

const dangTai = ref(false)
const loi = ref('')
const danhSachLichHoc = ref([])
const danhSachCaHoc = ref([])
const danhSachPhongHoc = ref([])
const danhSachGiaoVien = ref([])
const danhSachLopHocPhan = ref([])

const today = new Date()
const start = new Date(today)
start.setDate(today.getDate() - today.getDay() + 1)
const end = new Date(start)
end.setDate(start.getDate() + 6)

const boLoc = ref({
  tuNgay: toDateInput(start),
  denNgay: toDateInput(end),
  kieuXem: 'phong',
  tuKhoa: ''
})

const tenKieuXem = computed(() => {
  if (boLoc.value.kieuXem === 'giaoVien') return 'Giáo viên'
  if (boLoc.value.kieuXem === 'lop') return 'Lớp học phần'
  return 'Phòng học'
})

const lichLoc = computed(() => {
  return danhSachLichHoc.value.filter((item) => {
    if (item.trangThai === 'nghi') return false
    if (boLoc.value.tuNgay && item.ngayHoc < boLoc.value.tuNgay) return false
    if (boLoc.value.denNgay && item.ngayHoc > boLoc.value.denNgay) return false
    return true
  })
})

const cotMaTran = computed(() => {
  const ngay = []
  const batDau = new Date(`${boLoc.value.tuNgay}T00:00:00`)
  const ketThuc = new Date(`${boLoc.value.denNgay}T00:00:00`)

  for (let d = new Date(batDau); d <= ketThuc; d.setDate(d.getDate() + 1)) {
    const value = toDateInput(d)
    danhSachCaHoc.value.forEach((ca) => {
      ngay.push({ key: `${value}-${ca.id}`, ngay: value, caHocId: ca.id })
    })
  }

  return ngay
})

const nhomHienThi = computed(() => {
  const map = new Map()
  lichLoc.value.forEach((item) => {
    const key = khoaNhom(item)
    const ten = tenNhom(item)
    if (!map.has(key)) map.set(key, { key, ten, items: [] })
    map.get(key).items.push(item)
  })

  const keyword = boLoc.value.tuKhoa.trim().toLowerCase()
  return [...map.values()]
      .filter((item) => !keyword || item.ten.toLowerCase().includes(keyword))
      .sort((a, b) => a.ten.localeCompare(b.ten))
})

const canhBaoTrung = computed(() => {
  const map = new Map()
  const result = []

  lichLoc.value.forEach((item) => {
    const keys = [
      `Phòng ${item.maPhong || item.phongHocId} trùng ngày ${item.ngayHoc} ${tenCaTheoId(item.caHocId)}|P|${item.phongHocId}|${item.ngayHoc}|${item.caHocId}`,
      `Giáo viên ${item.tenGiaoVien || item.giaoVienId} trùng ngày ${item.ngayHoc} ${tenCaTheoId(item.caHocId)}|G|${item.giaoVienId}|${item.ngayHoc}|${item.caHocId}`,
      `Lớp ${item.maLop || item.lopHocPhanId} trùng ngày ${item.ngayHoc} ${tenCaTheoId(item.caHocId)}|L|${item.lopHocPhanId}|${item.ngayHoc}|${item.caHocId}`
    ]
    keys.forEach((key) => {
      if (!map.has(key)) map.set(key, [])
      map.get(key).push(item)
    })
  })

  map.forEach((items, key) => {
    if (items.length > 1) result.push(key.split('|')[0])
  })

  return result.slice(0, 20)
})

onMounted(() => taiDuLieu())

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''
  try {
    const [lichHoc, caHoc, phongHoc, giaoVien, lopHocPhan] = await Promise.all([
      giangDayService.layTrangLichHoc({ size: 2000, sortBy: 'ngayHoc', sortDir: 'asc' }),
      giangDayService.layDanhSachCaHoc(),
      giangDayService.layDanhSachPhongHoc(),
      giangDayService.layDanhSachGiaoVien(),
      giangDayService.layDanhSachLopHocPhan()
    ])
    danhSachLichHoc.value = lichHoc.content.map(boSungTen)
    danhSachCaHoc.value = caHoc
    danhSachPhongHoc.value = phongHoc
    danhSachGiaoVien.value = giaoVien
    danhSachLopHocPhan.value = lopHocPhan
  } catch (error) {
    loi.value = error?.response?.data?.message || error?.message || 'Không tải được ma trận lịch học'
  } finally {
    dangTai.value = false
  }
}

function boSungTen(item) {
  return {
    ...item,
    maPhong: item.maPhong || timTheoId(danhSachPhongHoc.value, item.phongHocId)?.maPhong,
    tenGiaoVien: item.tenGiaoVien || timTheoId(danhSachGiaoVien.value, item.giaoVienId)?.hoTen,
    maLop: item.maLop || timTheoId(danhSachLopHocPhan.value, item.lopHocPhanId)?.maLop
  }
}

function layItems(nhom, cot) {
  return nhom.items.filter((item) => item.ngayHoc === cot.ngay && Number(item.caHocId) === Number(cot.caHocId))
}

function khoaNhom(item) {
  if (boLoc.value.kieuXem === 'giaoVien') return `gv-${item.giaoVienId}`
  if (boLoc.value.kieuXem === 'lop') return `lop-${item.lopHocPhanId}`
  return `phong-${item.phongHocId}`
}

function tenNhom(item) {
  if (boLoc.value.kieuXem === 'giaoVien') return item.tenGiaoVien || tenGiaoVienTheoId(item.giaoVienId)
  if (boLoc.value.kieuXem === 'lop') return item.maLop || tenLopTheoId(item.lopHocPhanId)
  return item.maPhong || tenPhongTheoId(item.phongHocId)
}

function diDenLop(item) {
  router.push({ name: 'GiangDay.ChiTietLopHocPhan', params: { id: item.lopHocPhanId } })
}

function tenCaTheoId(id) {
  const ca = timTheoId(danhSachCaHoc.value, id)
  return ca ? `${ca.maCa || ca.id} ${ca.tenCa ? '- ' + ca.tenCa : ''}` : id || '-'
}

function tenPhongTheoId(id) {
  const phong = timTheoId(danhSachPhongHoc.value, id)
  return phong ? `${phong.maPhong || phong.id}` : id || '-'
}

function tenGiaoVienTheoId(id) {
  const gv = timTheoId(danhSachGiaoVien.value, id)
  return gv ? `${gv.maGiaoVien ? gv.maGiaoVien + ' - ' : ''}${gv.hoTen || gv.tenGiaoVien || gv.id}` : id || '-'
}

function tenLopTheoId(id) {
  const lop = timTheoId(danhSachLopHocPhan.value, id)
  return lop ? `${lop.maLop || lop.id}` : id || '-'
}

function timTheoId(list, id) {
  return list.find((item) => Number(item.id) === Number(id))
}

function hienThiNgay(value) {
  const date = new Date(`${value}T00:00:00`)
  const thu = ['CN', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7'][date.getDay()]
  return `${thu} ${value.slice(8, 10)}/${value.slice(5, 7)}`
}

function toDateInput(date) {
  const yyyy = date.getFullYear()
  const mm = String(date.getMonth() + 1).padStart(2, '0')
  const dd = String(date.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}
</script>

<style scoped>
.page { display: flex; flex-direction: column; gap: 14px; color: #344054; font-size: 14px; }
.page-head, .table-head { display: flex; justify-content: space-between; gap: 12px; align-items: flex-start; }
h1, h2 { margin: 0; color: #101828; }
p { margin: 6px 0 0; color: #667085; }
.table-card { border: 1px solid #eaecf0; border-radius: 12px; background: #fff; padding: 14px; }
.filter-card { display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 12px; }
label { display: grid; gap: 6px; font-weight: 650; }
input, select { border: 1px solid #d0d5dd; border-radius: 10px; padding: 9px 11px; font-size: 14px; }
.btn { border: 1px solid #d0d5dd; border-radius: 10px; background: #fff; color: #344054; padding: 9px 13px; cursor: pointer; font-weight: 500; }
.alert { padding: 10px 12px; border-radius: 10px; font-weight: 500; }
.alert.error { background: #fef3f2; color: #b42318; }
.warning-list { display: grid; gap: 4px; }
.stat-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 12px; }
.stat-grid article { border: 1px solid #eaecf0; border-radius: 12px; background: #fff; padding: 14px; display: grid; gap: 4px; }
.stat-grid strong { color: #101828; font-size: 24px; }
.stat-grid span { color: #667085; }
.matrix-wrap { overflow: auto; margin-top: 12px; max-height: 72vh; }
.matrix-table { border-collapse: collapse; min-width: 1100px; width: 100%; }
.matrix-table th, .matrix-table td { border: 1px solid #eaecf0; padding: 8px; vertical-align: top; min-width: 150px; }
.matrix-table thead th { position: sticky; top: 0; background: #f9fafb; z-index: 2; }
.matrix-table th small { display: block; color: #667085; font-weight: 500; }
.sticky-col { position: sticky; left: 0; z-index: 3; background: #fff; min-width: 180px; }
.row-title { color: #101828; text-align: left; }
.conflict { background: #fff1f3; }
.schedule-pill { width: 100%; border: 1px solid #bfdbfe; border-radius: 10px; background: #eff6ff; padding: 8px; margin-bottom: 6px; text-align: left; cursor: pointer; display: grid; gap: 2px; color: #175cd3; }
.schedule-pill strong { color: #101828; }
.schedule-pill span, .schedule-pill small { color: #475467; }
.empty-box, .empty { color: #667085; text-align: center; font-weight: 500; padding: 16px; }
</style>
