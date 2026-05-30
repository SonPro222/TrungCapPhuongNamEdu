<template>
  <div class="tang-page">
    <XemTomTatHuongDi tieu-de="Tầng 8 - Syllabus môn" :danh-sach="summary" />

    <h2 class="tang-title">Syllabus của môn học</h2>

    <XemTrangDangTai v-if="loading" />
    <p v-else-if="loi" class="loi-text">{{ loi }}</p>
    <XemTrangRong v-else-if="!danhSach.length" thong-bao="Môn này chưa có syllabus đã gán." />
    <div v-else class="syllabus-grid">
      <div v-for="(item, index) in danhSach" :key="item.id || index" class="syllabus-card">
        <div class="syllabus-card__ten">{{ tenSyllabus(item) }}</div>
        <div class="syllabus-card__actions">
          <button type="button" class="btn btn-chi-tiet" @click="moChiTiet(item)">Xem chi tiết môn/điểm</button>
          <button type="button" class="btn btn-tai-lieu" @click="moTaiLieu(item)">Xem tài liệu</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { xemChuongTrinhService, layTenHienThi } from '../services/xemChuongTrinhService'
import XemTomTatHuongDi from '../components/XemTomTatHuongDi.vue'
import XemTrangRong from '../components/XemTrangRong.vue'
import XemTrangDangTai from '../components/XemTrangDangTai.vue'

const route = useRoute()
const router = useRouter()

const khungKyId = computed(() => route.params.khungKyId)
const nhomKienThucId = computed(() => route.params.nhomKienThucId)
const chuongTrinhMonId = computed(() => route.params.chuongTrinhMonId)

const loading = ref(false)
const loi = ref('')
const danhSach = ref([])
const tenKy = ref('')
const tenNhom = ref('')
const tenMonTrongCt = ref('')
const tenMonHoc = ref('')

const summary = computed(() => [
  { nhan: 'Kỳ', giaTri: tenKy.value || khungKyId.value },
  { nhan: 'Nhóm kiến thức', giaTri: tenNhom.value || nhomKienThucId.value },
  { nhan: 'Môn trong chương trình', giaTri: tenMonTrongCt.value || chuongTrinhMonId.value },
  { nhan: 'Môn học', giaTri: tenMonHoc.value || '-' }
])

function tenSyllabus(item) {
  return layTenHienThi(item, ['ten', 'tenSyllabusMonHocGoc', 'maSyllabusMonHocGoc', 'mucTieu'])
}

async function taiDuLieu() {
  loading.value = true
  loi.value = ''
  try {
    const [ky, nhom, monData, items] = await Promise.all([
      xemChuongTrinhService.layKyById(khungKyId.value),
      xemChuongTrinhService.layNhomKienThucById(nhomKienThucId.value),
      xemChuongTrinhService.layMonHocTheoChuongTrinhMon(chuongTrinhMonId.value),
      xemChuongTrinhService.laySyllabusTheoChuongTrinhMon(chuongTrinhMonId.value)
    ])
    tenKy.value = ky ? layTenHienThi(ky, ['tenKy', 'maKy', 'tenKhungKyGoc']) : ''
    tenNhom.value = nhom ? layTenHienThi(nhom, ['ten', 'tenNhomKienThuc', 'tenNhomKienThucGoc', 'ma']) : ''
    tenMonTrongCt.value = monData?.chuongTrinhMon
      ? layTenHienThi(monData.chuongTrinhMon, ['tenMon', 'tenChuongTrinhMon', 'maMonTrongCt'])
      : ''
    tenMonHoc.value = monData?.monHoc ? layTenHienThi(monData.monHoc, ['tenMon', 'maMon']) : ''
    danhSach.value = items
  } catch (error) {
    loi.value = error?.message || 'Không tải được syllabus môn.'
  } finally {
    loading.value = false
  }
}

function moChiTiet(item) {
  router.push({
    name: 'XemChuongTrinh.Tang9ChiTietMonDiem',
    params: { ...route.params, syllabusMonId: item.id }
  })
}

function moTaiLieu(item) {
  router.push({
    name: 'XemChuongTrinh.Tang10TaiLieuSyllabusMon',
    params: { ...route.params, syllabusMonId: item.id }
  })
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
.syllabus-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 12px;
}
.syllabus-card {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.syllabus-card__ten {
  font-size: 15px;
  font-weight: 600;
  color: #1e293b;
}
.syllabus-card__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.btn {
  border: none;
  border-radius: 8px;
  padding: 8px 12px;
  font-size: 13px;
  cursor: pointer;
}
.btn-chi-tiet {
  background: #e0e7ff;
  color: #4338ca;
}
.btn-tai-lieu {
  background: #dcfce7;
  color: #15803d;
}
</style>
