<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Quản lý giảng dạy</h1>
        <p>Chọn lớp học phần theo ngành, chương trình, version, kỳ để xem danh sách sinh viên, cột điểm, điểm sinh viên và điểm danh theo buổi.</p>
      </div>

      <button type="button" class="btn" @click="taiDuLieuNen">
        Tải lại
      </button>
    </header>

    <section class="filter-card">
      <label>
        <span>Ngành</span>
        <select v-model="boLoc.nganhId" @change="doiNganh">
          <option value="">Tất cả ngành</option>
          <option v-for="item in danhSachNganh" :key="item.id" :value="item.id">
            {{ hienThiNganh(item) }}
          </option>
        </select>
      </label>

      <label>
        <span>Chương trình</span>
        <select v-model="boLoc.chuongTrinhId" @change="doiChuongTrinh">
          <option value="">Tất cả chương trình</option>
          <option v-for="item in danhSachChuongTrinhTheoNganh" :key="item.id" :value="item.id">
            {{ hienThiChuongTrinh(item) }}
          </option>
        </select>
      </label>

      <label>
        <span>Version</span>
        <select v-model="boLoc.chuongTrinhVersionId" @change="doiVersion">
          <option value="">Tất cả version</option>
          <option v-for="item in danhSachVersionTheoChuongTrinh" :key="item.id" :value="item.id">
            {{ hienThiVersion(item) }}
          </option>
        </select>
      </label>

      <label>
        <span>Kỳ</span>
        <select v-model="boLoc.khungKyId" @change="locDuLieu">
          <option value="">Tất cả kỳ</option>
          <option v-for="item in danhSachKhungKyTheoVersion" :key="item.id" :value="item.id">
            {{ hienThiKhungKy(item) }}
          </option>
        </select>
      </label>

      <label>
        <span>Từ khóa lớp</span>
        <input
            v-model.trim="boLoc.keyword"
            type="text"
            placeholder="Mã lớp, tên lớp"
            @keyup.enter="locDuLieu"
        />
      </label>

      <label>
        <span>Loại lớp</span>
        <select v-model="boLoc.loaiLopHocPhan" @change="locDuLieu">
          <option value="">Tất cả</option>
          <option value="CHUYEN_NGANH">Chuyên ngành</option>
          <option value="HOC_CHUNG">Học chung</option>
        </select>
      </label>

      <div class="filter-actions">
        <button type="button" class="btn primary" @click="locDuLieu">
          Lọc
        </button>

        <button type="button" class="btn" @click="xoaLoc">
          Xóa lọc
        </button>
      </div>
    </section>

    <div v-if="loi" class="alert error">
      {{ loi }}
    </div>

    <div v-if="thongBao" class="alert success">
      {{ thongBao }}
    </div>

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
            <td colspan="9" class="empty">
              Đang tải danh sách lớp học phần...
            </td>
          </tr>

          <tr v-else-if="danhSachLopHocPhanHienThi.length === 0">
            <td colspan="9" class="empty">
              Không có lớp học phần phù hợp bộ lọc.
            </td>
          </tr>

          <tr v-for="item in danhSachLopHocPhanHienThi" :key="item.id">
            <td>{{ item.maLop || '-' }}</td>
            <td>{{ item.tenLop || '-' }}</td>
            <td>{{ tenMonHocTheoLop(item) }}</td>
            <td>{{ tenKyTheoLop(item) }}</td>
            <td>{{ hienThiLoaiLop(item.loaiLopHocPhan) }}</td>
            <td>{{ item.soLuongHienTai || 0 }}/{{ item.soLuongToiDa || '-' }}</td>
            <td>{{ demLichHoc(item.id) }}/{{ item.soBuoiHoc || '-' }}</td>
            <td>{{ hienThiTrangThaiLop(item.trangThai) }}</td>
            <td>
              <button type="button" class="btn small primary" @click="xemChiTietLop(item)">
                Xem thêm
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
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { giangDayService } from '../services/giangDayService'

const router = useRouter()

const dangTai = ref(false)
const loi = ref('')
const thongBao = ref('')

const danhSachNganh = ref([])
const danhSachChuongTrinh = ref([])
const danhSachVersion = ref([])
const danhSachKhungKy = ref([])
const danhSachChuongTrinhMon = ref([])
const danhSachMonHoc = ref([])
const danhSachLopHocPhan = ref([])
const danhSachLopHocPhanChuongTrinhMon = ref([])
const danhSachLichHoc = ref([])

const boLoc = reactive({
  nganhId: '',
  chuongTrinhId: '',
  chuongTrinhVersionId: '',
  khungKyId: '',
  keyword: '',
  loaiLopHocPhan: ''
})

const danhSachChuongTrinhTheoNganh = computed(() => {
  if (!boLoc.nganhId) {
    return danhSachChuongTrinh.value
  }

  return danhSachChuongTrinh.value.filter((item) => {
    return String(item.nganhId || '') === String(boLoc.nganhId)
  })
})

const danhSachVersionTheoChuongTrinh = computed(() => {
  const chuongTrinhIds = danhSachChuongTrinhTheoNganh.value.map((item) => Number(item.id))

  return danhSachVersion.value.filter((item) => {
    if (boLoc.chuongTrinhId) {
      return String(item.chuongTrinhId || '') === String(boLoc.chuongTrinhId)
    }

    if (boLoc.nganhId) {
      return chuongTrinhIds.includes(Number(item.chuongTrinhId))
    }

    return true
  })
})

const danhSachKhungKyTheoVersion = computed(() => {
  const khungKyIds = new Set(
      danhSachChuongTrinhMon.value
          .filter((item) => {
            if (!boLoc.chuongTrinhVersionId) {
              return true
            }

            return String(item.chuongTrinhVersionId || '') === String(boLoc.chuongTrinhVersionId)
          })
          .map((item) => Number(item.khungKyId))
          .filter(Boolean)
  )

  if (khungKyIds.size === 0) {
    return danhSachKhungKy.value
  }

  return danhSachKhungKy.value.filter((item) => {
    return khungKyIds.has(Number(item.id))
  })
})

const danhSachLopHocPhanHienThi = computed(() => {
  const keyword = boLoc.keyword.toLowerCase()

  return danhSachLopHocPhan.value.filter((lop) => {
    if (keyword && !`${lop.maLop || ''} ${lop.tenLop || ''}`.toLowerCase().includes(keyword)) {
      return false
    }

    if (boLoc.loaiLopHocPhan && lop.loaiLopHocPhan !== boLoc.loaiLopHocPhan) {
      return false
    }

    if (boLoc.nganhId && !lopThuocNganh(lop, boLoc.nganhId)) {
      return false
    }

    if (boLoc.chuongTrinhId && !lopThuocChuongTrinh(lop, boLoc.chuongTrinhId)) {
      return false
    }

    if (boLoc.chuongTrinhVersionId && !lopThuocVersion(lop, boLoc.chuongTrinhVersionId)) {
      return false
    }

    if (boLoc.khungKyId && !lopThuocKhungKy(lop, boLoc.khungKyId)) {
      return false
    }

    return true
  })
})

onMounted(async () => {
  await taiDuLieuNen()
})

async function taiDuLieuNen() {
  dangTai.value = true
  loi.value = ''
  thongBao.value = ''

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
    loi.value = error?.message || 'Không tải được dữ liệu quản lý giảng dạy'
  } finally {
    dangTai.value = false
  }
}

function locDuLieu() {
  thongBao.value = ''
}

function xoaLoc() {
  boLoc.nganhId = ''
  boLoc.chuongTrinhId = ''
  boLoc.chuongTrinhVersionId = ''
  boLoc.khungKyId = ''
  boLoc.keyword = ''
  boLoc.loaiLopHocPhan = ''
  thongBao.value = ''
}

function doiNganh() {
  boLoc.chuongTrinhId = ''
  boLoc.chuongTrinhVersionId = ''
  boLoc.khungKyId = ''
  locDuLieu()
}

function doiChuongTrinh() {
  boLoc.chuongTrinhVersionId = ''
  boLoc.khungKyId = ''
  locDuLieu()
}

function doiVersion() {
  boLoc.khungKyId = ''
  locDuLieu()
}

function xemChiTietLop(item) {
  router.push({
    name: 'GiangDay.ChiTietLopHocPhan',
    params: {
      id: item.id
    }
  })
}

function layChuongTrinhMonIdsTheoLop(lop) {
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

function lopThuocNganh(lop, nganhId) {
  return layChuongTrinhMonTheoLop(lop).some((ctm) => {
    const version = timVersion(ctm.chuongTrinhVersionId)
    const chuongTrinh = timChuongTrinh(version?.chuongTrinhId)
    return String(chuongTrinh?.nganhId || '') === String(nganhId)
  })
}

function lopThuocChuongTrinh(lop, chuongTrinhId) {
  return layChuongTrinhMonTheoLop(lop).some((ctm) => {
    const version = timVersion(ctm.chuongTrinhVersionId)
    return String(version?.chuongTrinhId || '') === String(chuongTrinhId)
  })
}

function lopThuocVersion(lop, versionId) {
  return layChuongTrinhMonTheoLop(lop).some((ctm) => {
    return String(ctm.chuongTrinhVersionId || '') === String(versionId)
  })
}

function lopThuocKhungKy(lop, khungKyId) {
  return layChuongTrinhMonTheoLop(lop).some((ctm) => {
    return String(ctm.khungKyId || '') === String(khungKyId)
  })
}

function timVersion(id) {
  return danhSachVersion.value.find((item) => {
    return Number(item.id) === Number(id)
  })
}

function timChuongTrinh(id) {
  return danhSachChuongTrinh.value.find((item) => {
    return Number(item.id) === Number(id)
  })
}

function tenMonHocTheoLop(lop) {
  const ctm = layChuongTrinhMonTheoLop(lop)[0]
  const monHocId = lop.monHocId || ctm?.monHocId

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

function demLichHoc(lopHocPhanId) {
  return danhSachLichHoc.value.filter((item) => {
    return Number(item.lopHocPhanId) === Number(lopHocPhanId)
  }).length
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
    dang_mo: 'Đang mở',
    dang_hoc: 'Đang học',
    da_ket_thuc: 'Đã kết thúc',
    huy: 'Hủy'
  }

  return map[value] || value || '-'
}

function hienThiNganh(item) {
  return `${item.maNganh ? item.maNganh + ' - ' : ''}${item.tenNganh || item.ten || item.name || item.id}`
}

function hienThiChuongTrinh(item) {
  return `${item.maChuongTrinh ? item.maChuongTrinh + ' - ' : ''}${item.tenChuongTrinh || item.ten || item.id}`
}

function hienThiVersion(item) {
  return `${item.maVersion ? item.maVersion + ' - ' : ''}${item.tenVersion || item.id}`
}

function hienThiKhungKy(item) {
  return `${item.maKy ? item.maKy + ' - ' : ''}${item.tenKy || item.id}`
}
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  gap: 16px;
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
  margin: 0;
}

.page-head p {
  margin: 6px 0 0;
  color: var(--color-muted);
}

.filter-card,
.table-card {
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  background: var(--color-white);
  padding: 16px;
}

.filter-card {
  display: grid;
  grid-template-columns: repeat(4, minmax(170px, 1fr));
  gap: 12px;
}

label {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-weight: 700;
}

input,
select {
  min-height: 38px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  padding: 8px 10px;
  background: var(--color-white);
}

.filter-actions {
  display: flex;
  align-items: flex-end;
  gap: 8px;
}

.btn {
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  background: var(--color-white);
  padding: 9px 13px;
  cursor: pointer;
  font-weight: 700;
}

.btn.primary {
  background: var(--color-primary);
  border-color: var(--color-primary);
  color: var(--color-white);
}

.btn.small {
  padding: 6px 10px;
  font-size: 13px;
}

.alert {
  padding: 10px 12px;
  border-radius: var(--radius);
  font-weight: 700;
}

.alert.success {
  background: #ecfdf3;
  color: #027a48;
}

.alert.error {
  background: #fef3f2;
  color: #b42318;
}

.table-wrap {
  overflow: auto;
  margin-top: 12px;
}

table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1100px;
}

th,
td {
  border-bottom: 1px solid var(--color-border);
  padding: 10px;
  text-align: left;
  vertical-align: middle;
  white-space: nowrap;
}

th {
  background: var(--color-background);
  font-weight: 800;
}

.empty {
  text-align: center;
  color: var(--color-muted);
}

@media (max-width: 1100px) {
  .filter-card {
    grid-template-columns: repeat(2, minmax(170px, 1fr));
  }
}

@media (max-width: 700px) {
  .filter-card {
    grid-template-columns: 1fr;
  }

  .page-head,
  .table-head {
    flex-direction: column;
  }
}
</style>