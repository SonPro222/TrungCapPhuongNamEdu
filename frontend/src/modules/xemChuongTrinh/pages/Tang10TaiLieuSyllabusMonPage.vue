<template>
  <div class="tang-page">
    <XemTomTatHuongDi tieu-de="Tầng 10 - Tài liệu syllabus môn" :danh-sach="summary" />

    <h2 class="tang-title">Tài liệu syllabus môn</h2>

    <XemTrangDangTai v-if="loading" />
    <p v-else-if="loi" class="loi-text">{{ loi }}</p>
    <XemTaiLieuSyllabusMon
      v-else
      :syllabus-mon-id="syllabusMonId"
      :che-do-xem="cheDoXem"
      :tai-lieu-data="taiLieuData"
      @reload="taiTaiLieu"
    />
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { xemChuongTrinhService, layTenHienThi } from '../services/xemChuongTrinhService'
import XemTomTatHuongDi from '../components/XemTomTatHuongDi.vue'
import XemTrangDangTai from '../components/XemTrangDangTai.vue'
import XemTaiLieuSyllabusMon from '../components/XemTaiLieuSyllabusMon.vue'

const route = useRoute()

const khungKyId = computed(() => route.params.khungKyId)
const nhomKienThucId = computed(() => route.params.nhomKienThucId)
const chuongTrinhMonId = computed(() => route.params.chuongTrinhMonId)
const syllabusMonId = computed(() => route.params.syllabusMonId)

// Quyền lấy từ meta route cha (sinhVien | giangVien | daoTao | admin), mặc định sinhVien
const cheDoXem = computed(() => route.meta?.cheDoXem || 'sinhVien')

const loading = ref(false)
const loi = ref('')
const taiLieuData = ref({ syllabusTaiLieu: [], syllabusMonHocTaiLieu: [], taiLieuGoc: [] })
const tenKy = ref('')
const tenNhom = ref('')
const tenMonTrongCt = ref('')
const tenMonHoc = ref('')
const tenSyllabus = ref('')

const summary = computed(() => [
  { nhan: 'Kỳ', giaTri: tenKy.value || khungKyId.value },
  { nhan: 'Nhóm kiến thức', giaTri: tenNhom.value || nhomKienThucId.value },
  { nhan: 'Môn trong chương trình', giaTri: tenMonTrongCt.value || chuongTrinhMonId.value },
  { nhan: 'Môn học', giaTri: tenMonHoc.value || '-' },
  { nhan: 'Syllabus môn', giaTri: tenSyllabus.value || syllabusMonId.value },
  { nhan: 'Tài liệu syllabus', giaTri: 'Danh sách tài liệu' }
])

async function taiTaiLieu() {
  taiLieuData.value = await xemChuongTrinhService.layTaiLieuTheoSyllabusMon(syllabusMonId.value)
}

async function taiDuLieu() {
  loading.value = true
  loi.value = ''
  try {
    const [ky, nhom, monData, syllabus] = await Promise.all([
      xemChuongTrinhService.layKyById(khungKyId.value),
      xemChuongTrinhService.layNhomKienThucById(nhomKienThucId.value),
      xemChuongTrinhService.layMonHocTheoChuongTrinhMon(chuongTrinhMonId.value),
      xemChuongTrinhService.laySyllabusMonById(syllabusMonId.value)
    ])
    tenKy.value = ky ? layTenHienThi(ky, ['tenKy', 'maKy', 'tenKhungKyGoc']) : ''
    tenNhom.value = nhom ? layTenHienThi(nhom, ['ten', 'tenNhomKienThuc', 'tenNhomKienThucGoc', 'ma']) : ''
    tenMonTrongCt.value = monData?.chuongTrinhMon
      ? layTenHienThi(monData.chuongTrinhMon, ['tenMon', 'tenChuongTrinhMon', 'maMonTrongCt'])
      : ''
    tenMonHoc.value = monData?.monHoc ? layTenHienThi(monData.monHoc, ['tenMon', 'maMon']) : ''
    tenSyllabus.value = syllabus
      ? layTenHienThi(syllabus, ['ten', 'tenSyllabusMonHocGoc', 'maSyllabusMonHocGoc', 'mucTieu'])
      : ''
    await taiTaiLieu()
  } catch (error) {
    loi.value = error?.message || 'Không tải được tài liệu syllabus môn.'
  } finally {
    loading.value = false
  }
}

onMounted(taiDuLieu)
</script>

<style scoped>
.tang-page {
  padding: 24px;
  min-height: calc(100vh - 80px);
  background: #f8fafc;
}
.tang-title {
  font-size: 18px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 16px;
}
.loi-text {
  color: #dc2626;
  font-size: 14px;
}
</style>
