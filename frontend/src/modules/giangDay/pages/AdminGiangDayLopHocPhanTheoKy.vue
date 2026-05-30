<template>
  <section class="page">
    <header class="page-head">
      <div>
        <button type="button" class="btn ghost" @click="quayLaiChonKy">← Quay lại chọn kỳ</button>
        <h1>Quản lý lớp học phần theo kỳ</h1>
        <p>Danh sách lớp học phần sau khi đã chọn Ngành → Chương trình → Version → Kỳ.</p>
      </div>

      <button type="button" class="btn" @click="taiDuLieu">Tải lại</button>
    </header>

    <section class="info-card">
      <span><b>Ngành:</b> {{ tenNganhDaChon }}</span>
      <span><b>Chương trình:</b> {{ tenChuongTrinhDaChon }}</span>
      <span><b>Version:</b> {{ tenVersionDaChon }}</span>
      <span><b>Kỳ:</b> {{ tenKhungKyDaChon }}</span>
    </section>

    <section class="filter-card">
      <label>
        <span>Từ khóa lớp</span>
        <input v-model.trim="keyword" type="text" placeholder="Mã lớp, tên lớp" />
      </label>

      <label>
        <span>Loại lớp</span>
        <select v-model="loaiLopHocPhan">
          <option value="">Tất cả</option>
          <option value="CHUYEN_NGANH">Chuyên ngành</option>
          <option value="HOC_CHUNG">Học chung</option>
        </select>
      </label>
    </section>

    <div v-if="loi" class="alert error">{{ loi }}</div>

    <section class="table-card">
      <div class="table-head">
        <h2>Danh sách lớp học phần</h2>
        <span>{{ dangTai ? 'Đang tải...' : `${danhSachLopHocPhanHienThi.length} lớp` }}</span>
      </div>

      <div class="table-wrap">
        <table>
          <thead>
          <tr>
            <th>Mã lớp</th>
            <th>Tên lớp</th>
            <th>Môn học</th>
            <th>Kỳ</th>
            <th>Loại lớp</th>
            <th>Sĩ số</th>
            <th>Số buổi</th>
            <th>Trạng thái</th>
            <th>Thao tác</th>
          </tr>
          </thead>

          <tbody>
          <tr v-if="dangTai">
            <td colspan="9" class="empty">Đang tải danh sách lớp học phần...</td>
          </tr>

          <tr v-else-if="danhSachLopHocPhanHienThi.length === 0">
            <td colspan="9" class="empty">Không có lớp học phần trong kỳ đã chọn.</td>
          </tr>

          <tr v-for="item in danhSachLopHocPhanHienThi" v-else :key="item.id">
            <td>{{ item.maLop || '-' }}</td>
            <td>{{ item.tenLop || '-' }}</td>
            <td>{{ tenMonHocTheoLop(item) }}</td>
            <td>{{ tenKyTheoLop(item) }}</td>
            <td>{{ hienThiLoaiLop(item.loaiLopHocPhan) }}</td>
            <td>{{ item.soLuongHienTai || 0 }}/{{ item.soLuongToiDa || '-' }}</td>
            <td>{{ demLichHoc(item.id) }}/{{ item.soBuoiHoc || '-' }}</td>
            <td>{{ hienThiTrangThaiLop(item.trangThai) }}</td>
            <td>
              <div class="row-actions">
                <button type="button" class="btn small primary" @click="xemBuoiHoc(item)">
                  Xem buổi học
                </button>

                <button type="button" class="btn small" @click="phanCong(item)">
                  Phân công
                </button>
              </div>
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
import { useRoute, useRouter } from 'vue-router'
import { giangDayService } from '../services/giangDayService'

const route = useRoute()
const router = useRouter()

const nganhId = computed(() => Number(route.params.nganhId))
const chuongTrinhId = computed(() => Number(route.params.chuongTrinhId))
const versionId = computed(() => Number(route.params.versionId))
const khungKyId = computed(() => Number(route.params.khungKyId))

const dangTai = ref(false)
const loi = ref('')
const keyword = ref('')
const loaiLopHocPhan = ref('')

const danhSachNganh = ref([])
const danhSachChuongTrinh = ref([])
const danhSachVersion = ref([])
const danhSachKhungKy = ref([])
const danhSachChuongTrinhMon = ref([])
const danhSachMonHoc = ref([])
const danhSachLopHocPhan = ref([])
const danhSachLopHocPhanChuongTrinhMon = ref([])
const danhSachLichHoc = ref([])

const nganhDaChon = computed(() => danhSachNganh.value.find((item) => Number(item.id) === nganhId.value))
const chuongTrinhDaChon = computed(() => danhSachChuongTrinh.value.find((item) => Number(item.id) === chuongTrinhId.value))
const versionDaChon = computed(() => danhSachVersion.value.find((item) => Number(item.id) === versionId.value))
const khungKyDaChon = computed(() => danhSachKhungKy.value.find((item) => Number(item.id) === khungKyId.value))

const tenNganhDaChon = computed(() => hienThiNganh(nganhDaChon.value) || `ID ${nganhId.value}`)
const tenChuongTrinhDaChon = computed(() => hienThiChuongTrinh(chuongTrinhDaChon.value) || `ID ${chuongTrinhId.value}`)
const tenVersionDaChon = computed(() => hienThiVersion(versionDaChon.value) || `ID ${versionId.value}`)
const tenKhungKyDaChon = computed(() => hienThiKhungKy(khungKyDaChon.value) || `ID ${khungKyId.value}`)

const danhSachLopHocPhanHienThi = computed(() => {
  const tuKhoa = keyword.value.toLowerCase()

  return danhSachLopHocPhan.value.filter((lop) => {
    if (tuKhoa && !`${lop.maLop || ''} ${lop.tenLop || ''}`.toLowerCase().includes(tuKhoa)) {
      return false
    }

    if (loaiLopHocPhan.value && lop.loaiLopHocPhan !== loaiLopHocPhan.value) {
      return false
    }

    if (!lopThuocVersion(lop, versionId.value)) {
      return false
    }

    if (!lopThuocKhungKy(lop, khungKyId.value)) {
      return false
    }

    return true
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
      nganh,
      chuongTrinh,
      version,
      khungKy,
      chuongTrinhMon,
      monHoc,
      lopHocPhan,
      lopHocPhanChuongTrinhMon,
      lichHoc
    ] = await Promise.all([
      giangDayService.layDanhSachNganh(),
      giangDayService.layDanhSachChuongTrinh(),
      giangDayService.layDanhSachChuongTrinhVersion(),
      giangDayService.layDanhSachKhungKy(),
      giangDayService.layDanhSachChuongTrinhMon(),
      giangDayService.layDanhSachMonHoc(),
      giangDayService.layTrangLopHocPhan({ size: 500 }),
      giangDayService.layTrangLopHocPhanChuongTrinhMon({ size: 500 }),
      giangDayService.layTrangLichHoc({ size: 500 })
    ])

    danhSachNganh.value = nganh
    danhSachChuongTrinh.value = chuongTrinh
    danhSachVersion.value = version
    danhSachKhungKy.value = khungKy
    danhSachChuongTrinhMon.value = chuongTrinhMon
    danhSachMonHoc.value = monHoc
    danhSachLopHocPhan.value = lopHocPhan.content
    danhSachLopHocPhanChuongTrinhMon.value = lopHocPhanChuongTrinhMon.content
    danhSachLichHoc.value = lichHoc.content
  } catch (error) {
    loi.value = error?.message || 'Không tải được danh sách lớp học phần theo kỳ'
  } finally {
    dangTai.value = false
  }
}

function quayLaiChonKy() {
  router.push({
    name: 'GiangDay.ChonKy',
    params: {
      nganhId: nganhId.value,
      chuongTrinhId: chuongTrinhId.value,
      versionId: versionId.value
    }
  })
}

function xemBuoiHoc(item) {
  router.push({
    name: 'GiangDay.ChiTietLopHocPhan',
    params: { id: item.id },
    query: queryFlow()
  })
}

function phanCong(item) {
  router.push({
    name: 'GiangDay.PhanCong',
    query: {
      ...queryFlow(),
      lopHocPhanId: item.id
    }
  })
}

function queryFlow() {
  return {
    nganhId: nganhId.value,
    chuongTrinhId: chuongTrinhId.value,
    versionId: versionId.value,
    khungKyId: khungKyId.value
  }
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
        if (item.chuongTrinhMonId) ids.push(Number(item.chuongTrinhMonId))
      })

  return [...new Set(ids)]
}

function layChuongTrinhMonTheoLop(lop) {
  const ids = layChuongTrinhMonIdsTheoLop(lop)
  return danhSachChuongTrinhMon.value.filter((item) => ids.includes(Number(item.id)))
}

function lopThuocVersion(lop, id) {
  return layChuongTrinhMonTheoLop(lop).some((item) => Number(item.chuongTrinhVersionId) === Number(id))
}

function lopThuocKhungKy(lop, id) {
  return layChuongTrinhMonTheoLop(lop).some((item) => Number(item.khungKyId) === Number(id))
}

function tenMonHocTheoLop(lop) {
  const ctm = layChuongTrinhMonTheoLop(lop)[0]
  const monHocId = lop?.monHocId || ctm?.monHocId

  const monHoc = danhSachMonHoc.value.find((item) => Number(item.id) === Number(monHocId))
  return monHoc?.tenMonHoc || monHoc?.tenMon || monHoc?.name || monHocId || '-'
}

function tenKyTheoLop(lop) {
  const ctm = layChuongTrinhMonTheoLop(lop)[0]
  const khungKy = danhSachKhungKy.value.find((item) => Number(item.id) === Number(ctm?.khungKyId))
  return hienThiKhungKy(khungKy) || ctm?.khungKyId || '-'
}

function demLichHoc(lopHocPhanId) {
  return danhSachLichHoc.value.filter((item) => Number(item.lopHocPhanId) === Number(lopHocPhanId)).length
}

function hienThiLoaiLop(value) {
  const map = {
    CHUYEN_NGANH: 'Chuyên ngành',
    HOC_CHUNG: 'Học chung'
  }
  return map[value] || value || '-'
}

function hienThiTrangThaiLop(value) {
  const map = {
    du_kien: 'Dự kiến',
    dang_hoc: 'Đang học',
    da_ket_thuc: 'Đã kết thúc',
    huy: 'Hủy'
  }
  return map[value] || value || '-'
}

function hienThiNganh(item) {
  if (!item) return ''
  return `${item.maNganh ? item.maNganh + ' - ' : ''}${item.tenNganh || item.ten || item.name || item.id}`
}

function hienThiChuongTrinh(item) {
  if (!item) return ''
  return `${item.maChuongTrinh ? item.maChuongTrinh + ' - ' : ''}${item.tenChuongTrinh || item.ten || item.name || item.id}`
}

function hienThiVersion(item) {
  if (!item) return ''
  return `${item.maVersion ? item.maVersion + ' - ' : ''}${item.tenVersion || item.ten || item.name || item.id}`
}

function hienThiKhungKy(item) {
  if (!item) return ''
  return `${item.maKy ? item.maKy + ' - ' : ''}${item.tenKy || item.ten || item.name || item.id}`
}
</script>

<style scoped>
.page { display: flex; flex-direction: column; gap: 16px; }
.page-head, .table-head { display: flex; justify-content: space-between; gap: 12px; align-items: flex-start; }
.page-head h1, .table-head h2 { margin: 8px 0 0; }
.page-head p { margin: 6px 0 0; color: #64748b; }
.info-card, .filter-card, .table-card { border: 1px solid var(--color-border); border-radius: var(--radius); padding: 16px; background: var(--color-white); }
.info-card { display: flex; gap: 12px; flex-wrap: wrap; }
.filter-card { display: grid; grid-template-columns: repeat(2, minmax(180px, 1fr)); gap: 12px; }
label { display: flex; flex-direction: column; gap: 6px; }
label span { font-weight: 700; color: #334155; }
input, select { border: 1px solid var(--color-border); border-radius: var(--radius); padding: 9px 11px; }
.table-wrap { overflow: auto; }
table { width: 100%; border-collapse: collapse; min-width: 1000px; }
th, td { padding: 10px 12px; border-bottom: 1px solid var(--color-border); text-align: left; vertical-align: top; }
th { background: #f8fafc; color: #475569; font-size: 12px; text-transform: uppercase; }
.empty { text-align: center; color: #64748b; }
.row-actions { display: flex; gap: 8px; flex-wrap: wrap; }
.btn { border: 1px solid var(--color-border); background: var(--color-white); color: var(--color-text); border-radius: var(--radius); padding: 8px 12px; cursor: pointer; font-weight: 700; }
.btn.primary { background: var(--color-primary); color: var(--color-white); border-color: var(--color-primary); }
.btn.small { padding: 6px 10px; font-size: 12px; }
.btn.ghost { border-color: transparent; padding-left: 0; }
.alert { padding: 10px 12px; border-radius: var(--radius); }
.alert.error { background: #fee2e2; color: #991b1b; }
@media (max-width: 900px) { .filter-card { grid-template-columns: 1fr; } }
</style>
