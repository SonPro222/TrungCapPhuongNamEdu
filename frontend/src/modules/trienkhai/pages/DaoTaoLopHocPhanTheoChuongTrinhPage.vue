<template>
  <main class="tk-page">
    <section class="page-hero">
      <div>
        <p class="hero-kicker">Triển khai đào tạo</p>
        <h1>Lớp học phần theo chương trình</h1>
        <p class="hero-desc">
          Theo dõi lớp học phần, sinh viên, giảng viên, lịch học và điểm theo đúng cây
          ngành → hệ đào tạo → chương trình → version hiện hành → kỳ.
        </p>
      </div>
      <div class="hero-badge">
        <strong>{{ lopHocPhans.length }}</strong>
        <span>lớp đang hiển thị</span>
      </div>
    </section>

    <LopHocPhanChuongTrinhFilter
      v-model="boLoc"
      :nganhs="nganhs"
      :nganh-he-dao-taos="nganhHeDaoTaos"
      :chuong-trinhs="chuongTrinhs"
      :versions="versions"
      :khung-kys="khungKys"
      :loading="dangTai"
      :loading-he="dangTaiHe"
      :loading-chuong-trinh="dangTaiChuongTrinh"
      :loading-version="dangTaiVersion"
      :loading-ky="dangTaiKy"
      :co-the-tai-lop="Boolean(boLoc.khungKyId)"
      @change="xuLyDoiBoLoc"
      @refresh="taiLaiTheoNguCanh"
    />

    <ChuongTrinhHienHanhSummary
      :nganh="nganhDangChon"
      :nganh-he-dao-tao="heDangChon"
      :chuong-trinh="chuongTrinhDangChon"
      :version="versionDangChon"
      :khung-kys="khungKys"
      :tong-lop="lopHocPhans.length"
    />

    <section v-if="canhBao" class="alert-card">
      <strong>Lưu ý</strong>
      <span>{{ canhBao }}</span>
    </section>

    <LopHocPhanTable
      :items="lopHocPhansLoc"
      :loading="dangTaiLop"
      :title="tieuDeBangLop"
      @select="moChiTietLop"
    />

    <LopHocPhanDetailDrawer
      :open="drawerOpen"
      :lop-hoc-phan="lopDangChon"
      :sinh-viens="sinhViens"
      :giang-viens="giangViens"
      :lich-hocs="lichHocs"
      :diems="diems"
      :loading-sinh-vien="dangTaiSinhVien"
      :loading-giang-vien="dangTaiGiangVien"
      :loading-lich-hoc="dangTaiLichHoc"
      :loading-diem="dangTaiDiem"
      @close="drawerOpen = false"
    />
  </main>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import LopHocPhanChuongTrinhFilter from '../components/lopHocPhanTheoChuongTrinh/LopHocPhanChuongTrinhFilter.vue'
import ChuongTrinhHienHanhSummary from '../components/lopHocPhanTheoChuongTrinh/ChuongTrinhHienHanhSummary.vue'
import LopHocPhanTable from '../components/lopHocPhanTheoChuongTrinh/LopHocPhanTable.vue'
import LopHocPhanDetailDrawer from '../components/lopHocPhanTheoChuongTrinh/LopHocPhanDetailDrawer.vue'
import { trienKhaiService } from '../services/trienKhaiService'

const boLoc = reactive({
  nganhId: '',
  nganhHeDaoTaoId: '',
  chuongTrinhId: '',
  versionId: '',
  khungKyId: '',
  keyword: ''
})

const nganhs = ref([])
const nganhHeDaoTaos = ref([])
const chuongTrinhs = ref([])
const versions = ref([])
const khungKys = ref([])
const chuongTrinhMons = ref([])
const lopHocPhans = ref([])

const drawerOpen = ref(false)
const lopDangChon = ref(null)
const sinhViens = ref([])
const giangViens = ref([])
const lichHocs = ref([])
const diems = ref([])
const canhBao = ref('')

const dangTai = ref(false)
const dangTaiHe = ref(false)
const dangTaiChuongTrinh = ref(false)
const dangTaiVersion = ref(false)
const dangTaiKy = ref(false)
const dangTaiLop = ref(false)
const dangTaiSinhVien = ref(false)
const dangTaiGiangVien = ref(false)
const dangTaiLichHoc = ref(false)
const dangTaiDiem = ref(false)

const nganhDangChon = computed(() => nganhs.value.find(item => String(item.id) === String(boLoc.nganhId)) || null)
const heDangChon = computed(() => nganhHeDaoTaos.value.find(item => String(item.id) === String(boLoc.nganhHeDaoTaoId)) || null)
const chuongTrinhDangChon = computed(() => chuongTrinhs.value.find(item => String(item.id) === String(boLoc.chuongTrinhId)) || null)
const versionDangChon = computed(() => versions.value.find(item => String(item.id) === String(boLoc.versionId)) || null)
const khungKyDangChon = computed(() => khungKys.value.find(item => String(item.id) === String(boLoc.khungKyId)) || null)

const tieuDeBangLop = computed(() => {
  if (!boLoc.khungKyId) return 'Chọn kỳ để xem lớp học phần'
  return `Lớp học phần ${khungKyDangChon.value?.tenKy || khungKyDangChon.value?.tenKhungKy || ''}`.trim()
})

const lopHocPhansLoc = computed(() => {
  const keyword = boLoc.keyword?.trim().toLowerCase()
  if (!keyword) return lopHocPhans.value
  return lopHocPhans.value.filter(item => {
    return [
      item.maLop,
      item.tenLop,
      item.maMonHoc,
      item.tenMonHoc,
      item.chuongTrinhMon?.maMonHoc,
      item.chuongTrinhMon?.tenMonHoc
    ].filter(Boolean).some(value => String(value).toLowerCase().includes(keyword))
  })
})

onMounted(async () => {
  await taiNganh()
})

async function taiNganh() {
  dangTai.value = true
  canhBao.value = ''
  try {
    const result = await trienKhaiService.nganh.getAll({ page: 0, size: 1000, sortBy: 'tenNganh', sortDir: 'asc' })
    nganhs.value = result.items
  } catch (error) {
    canhBao.value = layThongBaoLoi(error, 'Không tải được danh sách ngành.')
  } finally {
    dangTai.value = false
  }
}

async function xuLyDoiBoLoc({ key }) {
  canhBao.value = ''

  if (key === 'nganhId') {
    resetSauNganh()
    if (boLoc.nganhId) await taiNganhHeDaoTao()
    return
  }

  if (key === 'nganhHeDaoTaoId') {
    resetSauHe()
    if (boLoc.nganhHeDaoTaoId) await taiChuongTrinh()
    return
  }

  if (key === 'chuongTrinhId') {
    resetSauChuongTrinh()
    if (boLoc.chuongTrinhId) await taiVersionHienHanh()
    return
  }

  if (key === 'versionId') {
    resetSauVersion()
    if (boLoc.versionId) await taiKhungKy()
    return
  }

  if (key === 'khungKyId') {
    resetSauKy()
    if (boLoc.khungKyId) await taiLopHocPhanTheoKy()
  }
}

function resetSauNganh() {
  boLoc.nganhHeDaoTaoId = ''
  nganhHeDaoTaos.value = []
  resetSauHe()
}

function resetSauHe() {
  boLoc.chuongTrinhId = ''
  chuongTrinhs.value = []
  resetSauChuongTrinh()
}

function resetSauChuongTrinh() {
  boLoc.versionId = ''
  versions.value = []
  resetSauVersion()
}

function resetSauVersion() {
  boLoc.khungKyId = ''
  khungKys.value = []
  resetSauKy()
}

function resetSauKy() {
  chuongTrinhMons.value = []
  lopHocPhans.value = []
  dongDrawer()
}

function dongDrawer() {
  drawerOpen.value = false
  lopDangChon.value = null
  sinhViens.value = []
  giangViens.value = []
  lichHocs.value = []
  diems.value = []
}

async function taiNganhHeDaoTao() {
  dangTaiHe.value = true
  try {
    const result = await trienKhaiService.nganhHeDaoTao.getAll({ nganhId: boLoc.nganhId, page: 0, size: 1000 })
    nganhHeDaoTaos.value = result.items
  } catch (error) {
    canhBao.value = layThongBaoLoi(error, 'Không tải được ngành hệ đào tạo.')
  } finally {
    dangTaiHe.value = false
  }
}

async function taiChuongTrinh() {
  dangTaiChuongTrinh.value = true
  try {
    const result = await trienKhaiService.chuongTrinh.getAll({ nganhHeDaoTaoId: boLoc.nganhHeDaoTaoId, page: 0, size: 1000 })
    chuongTrinhs.value = result.items
  } catch (error) {
    canhBao.value = layThongBaoLoi(error, 'Không tải được chương trình đào tạo.')
  } finally {
    dangTaiChuongTrinh.value = false
  }
}

async function taiVersionHienHanh() {
  dangTaiVersion.value = true
  try {
    const hienHanh = await trienKhaiService.getVersionHienHanh(boLoc.chuongTrinhId)
    const result = await trienKhaiService.chuongTrinhVersion.getAll({ chuongTrinhId: boLoc.chuongTrinhId, page: 0, size: 100, sortBy: 'id', sortDir: 'desc' })
    versions.value = result.items
    if (hienHanh?.id) {
      boLoc.versionId = String(hienHanh.id)
      await taiKhungKy()
    } else {
      canhBao.value = 'Chương trình này chưa có version hiện hành. Bạn có thể chọn version thủ công nếu có dữ liệu.'
    }
  } catch (error) {
    canhBao.value = layThongBaoLoi(error, 'Không tải được version chương trình.')
  } finally {
    dangTaiVersion.value = false
  }
}

async function taiKhungKy() {
  dangTaiKy.value = true
  try {
    const result = await trienKhaiService.getKhungKyTheoVersion(boLoc.versionId, { page: 0, size: 1000, sortBy: 'soThuTu', sortDir: 'asc' })
    khungKys.value = result.items
  } catch (error) {
    canhBao.value = layThongBaoLoi(error, 'Không tải được khung kỳ của version.')
  } finally {
    dangTaiKy.value = false
  }
}

async function taiLopHocPhanTheoKy() {
  dangTaiLop.value = true
  try {
    const monResult = await trienKhaiService.chuongTrinhMon.getAll({
      chuongTrinhVersionId: boLoc.versionId,
      khungKyId: boLoc.khungKyId,
      page: 0,
      size: 1000,
      sortBy: 'id',
      sortDir: 'asc'
    })
    chuongTrinhMons.value = monResult.items

    if (!chuongTrinhMons.value.length) {
      lopHocPhans.value = []
      canhBao.value = 'Kỳ này chưa có môn trong chương trình, nên chưa thể hiển thị lớp học phần.'
      return
    }

    lopHocPhans.value = await trienKhaiService.getLopHocPhanTheoChuongTrinhMon(chuongTrinhMons.value)
  } catch (error) {
    canhBao.value = layThongBaoLoi(error, 'Không tải được lớp học phần theo kỳ.')
  } finally {
    dangTaiLop.value = false
  }
}

async function moChiTietLop(lop) {
  lopDangChon.value = lop
  drawerOpen.value = true
  sinhViens.value = []
  giangViens.value = []
  lichHocs.value = []
  diems.value = []
  await Promise.all([
    taiSinhVienTrongLop(lop),
    taiGiangVienTrongLop(lop),
    taiLichHocTrongLop(lop),
    taiDiemTrongLop(lop)
  ])
}

async function taiSinhVienTrongLop(lop) {
  dangTaiSinhVien.value = true
  try {
    const result = await trienKhaiService.sinhVienLopHocPhan.getAll({ lopHocPhanId: lop.id, page: 0, size: 1000 })
    sinhViens.value = result.items
  } catch (error) {
    sinhViens.value = []
  } finally {
    dangTaiSinhVien.value = false
  }
}

async function taiGiangVienTrongLop(lop) {
  dangTaiGiangVien.value = true
  try {
    const result = await trienKhaiService.phanCongGiangDay.getAll({ keywordLop: lop.maLop || lop.tenLop, page: 0, size: 100 })
    giangViens.value = result.items.filter(item => String(item.lopHocPhanId) === String(lop.id) || item.maLop === lop.maLop || item.tenLop === lop.tenLop)
  } catch (error) {
    giangViens.value = []
  } finally {
    dangTaiGiangVien.value = false
  }
}

async function taiLichHocTrongLop(lop) {
  dangTaiLichHoc.value = true
  try {
    const result = await trienKhaiService.lichHoc.getAll({ lopHocPhanId: lop.id, page: 0, size: 100, sortBy: 'ngayHoc', sortDir: 'asc' })
    lichHocs.value = result.items
  } catch (error) {
    lichHocs.value = []
  } finally {
    dangTaiLichHoc.value = false
  }
}

async function taiDiemTrongLop(lop) {
  dangTaiDiem.value = true
  try {
    const result = await trienKhaiService.ketQuaLopHocPhan.getAll({ lopHocPhanId: lop.id, page: 0, size: 1000 })
    diems.value = result.items
  } catch (error) {
    diems.value = []
  } finally {
    dangTaiDiem.value = false
  }
}

async function taiLaiTheoNguCanh() {
  if (boLoc.khungKyId) return taiLopHocPhanTheoKy()
  if (boLoc.versionId) return taiKhungKy()
  if (boLoc.chuongTrinhId) return taiVersionHienHanh()
  if (boLoc.nganhHeDaoTaoId) return taiChuongTrinh()
  if (boLoc.nganhId) return taiNganhHeDaoTao()
  return taiNganh()
}

function layThongBaoLoi(error, fallback) {
  return error?.response?.data?.message || error?.response?.data?.error || error?.message || fallback
}
</script>

<style scoped>
.tk-page {
  min-height: 100vh;
  padding: 28px;
  display: grid;
  gap: 22px;
  background:
    radial-gradient(circle at 8% 0%, rgba(37, 99, 235, .12), transparent 28%),
    radial-gradient(circle at 100% 18%, rgba(124, 58, 237, .12), transparent 26%),
    linear-gradient(180deg, #f8fafc 0%, #eef4ff 100%);
}
.page-hero {
  display: flex;
  align-items: stretch;
  justify-content: space-between;
  gap: 20px;
  padding: 30px;
  border-radius: 32px;
  color: white;
  background:
    linear-gradient(135deg, rgba(15, 23, 42, .94), rgba(30, 64, 175, .92)),
    radial-gradient(circle at top right, rgba(96, 165, 250, .45), transparent 40%);
  box-shadow: 0 30px 80px rgba(15, 23, 42, .22);
  overflow: hidden;
}
.hero-kicker { margin: 0 0 8px; color: #93c5fd; text-transform: uppercase; letter-spacing: .14em; font-size: 12px; font-weight: 900; }
h1 { margin: 0; font-size: clamp(32px, 4vw, 54px); line-height: 1.02; letter-spacing: -.04em; }
.hero-desc { max-width: 760px; margin: 14px 0 0; color: #dbeafe; font-size: 16px; line-height: 1.7; font-weight: 650; }
.hero-badge { min-width: 160px; align-self: stretch; display: grid; place-items: center; align-content: center; border: 1px solid rgba(255,255,255,.18); border-radius: 28px; background: rgba(255,255,255,.12); backdrop-filter: blur(10px); }
.hero-badge strong { font-size: 44px; line-height: 1; }
.hero-badge span { margin-top: 8px; color: #dbeafe; font-weight: 800; }
.alert-card { display: flex; gap: 10px; align-items: center; padding: 14px 18px; border-radius: 18px; background: #fffbeb; border: 1px solid #fde68a; color: #92400e; box-shadow: 0 12px 32px rgba(245,158,11,.08); }
.alert-card strong { font-weight: 900; }
@media (max-width: 820px) {
  .tk-page { padding: 16px; }
  .page-hero { flex-direction: column; padding: 22px; }
  .hero-badge { min-height: 120px; }
}
</style>
