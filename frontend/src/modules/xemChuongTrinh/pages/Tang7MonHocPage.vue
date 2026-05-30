<template>
  <div class="tang-page">
    <XemTomTatHuongDi tieu-de="Tầng 7 - Môn học" :danh-sach="summary" />

    <h2 class="tang-title">Môn học</h2>

    <XemTrangDangTai v-if="loading" />
    <p v-else-if="loi" class="loi-text">{{ loi }}</p>
    <XemTrangRong v-else-if="!monHoc" thong-bao="Môn trong chương trình này chưa gán môn học." />
    <XemDanhSachNut
      v-else
      :danh-sach="[monHoc]"
      :ten-keys="['tenMon', 'maMon']"
      :mo-ta-keys="['maMon']"
      @chon="moSyllabus"
    />
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { xemChuongTrinhService, layTenHienThi } from '../services/xemChuongTrinhService'
import XemTomTatHuongDi from '../components/XemTomTatHuongDi.vue'
import XemDanhSachNut from '../components/XemDanhSachNut.vue'
import XemTrangRong from '../components/XemTrangRong.vue'
import XemTrangDangTai from '../components/XemTrangDangTai.vue'

const route = useRoute()
const router = useRouter()

const khungKyId = computed(() => route.params.khungKyId)
const nhomKienThucId = computed(() => route.params.nhomKienThucId)
const chuongTrinhMonId = computed(() => route.params.chuongTrinhMonId)

const loading = ref(false)
const loi = ref('')
const monHoc = ref(null)
const tenKy = ref('')
const tenNhom = ref('')
const tenMonTrongCt = ref('')

const summary = computed(() => [
  { nhan: 'Kỳ', giaTri: tenKy.value || khungKyId.value },
  { nhan: 'Nhóm kiến thức', giaTri: tenNhom.value || nhomKienThucId.value },
  { nhan: 'Môn trong chương trình', giaTri: tenMonTrongCt.value || chuongTrinhMonId.value }
])

async function taiDuLieu() {
  loading.value = true
  loi.value = ''
  try {
    const [ky, nhom, monData] = await Promise.all([
      xemChuongTrinhService.layKyById(khungKyId.value),
      xemChuongTrinhService.layNhomKienThucById(nhomKienThucId.value),
      xemChuongTrinhService.layMonHocTheoChuongTrinhMon(chuongTrinhMonId.value)
    ])
    tenKy.value = ky ? layTenHienThi(ky, ['tenKy', 'maKy', 'tenKhungKyGoc']) : ''
    tenNhom.value = nhom ? layTenHienThi(nhom, ['ten', 'tenNhomKienThuc', 'tenNhomKienThucGoc', 'ma']) : ''
    tenMonTrongCt.value = monData?.chuongTrinhMon
      ? layTenHienThi(monData.chuongTrinhMon, ['tenMon', 'tenChuongTrinhMon', 'maMonTrongCt'])
      : ''
    monHoc.value = monData?.monHoc || null
  } catch (error) {
    loi.value = error?.message || 'Không tải được môn học.'
  } finally {
    loading.value = false
  }
}

function moSyllabus() {
  router.push({
    name: 'XemChuongTrinh.Tang8SyllabusMon',
    params: { ...route.params }
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
</style>
