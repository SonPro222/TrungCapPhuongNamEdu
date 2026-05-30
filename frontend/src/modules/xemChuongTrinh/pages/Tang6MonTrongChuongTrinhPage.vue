<template>
  <div class="tang-page">
    <XemTomTatHuongDi tieu-de="Tầng 6 - Môn trong chương trình" :danh-sach="summary" />

    <h2 class="tang-title">Môn trong chương trình</h2>

    <XemTrangDangTai v-if="loading" />
    <p v-else-if="loi" class="loi-text">{{ loi }}</p>
    <XemTrangRong v-else-if="!danhSach.length" thong-bao="Kỳ / nhóm kiến thức này chưa có môn đã gán." />
    <XemDanhSachNut
      v-else
      :danh-sach="danhSach"
      :ten-keys="['tenMon', 'tenChuongTrinhMon', 'maMonTrongCt']"
      :mo-ta-keys="['maMonTrongCt']"
      @chon="moMonHoc"
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

const nganhId = computed(() => route.params.nganhId)
const chuongTrinhId = computed(() => route.params.chuongTrinhId)
const versionId = computed(() => route.params.versionId)
const khungKyId = computed(() => route.params.khungKyId)
const nhomKienThucId = computed(() => route.params.nhomKienThucId)

const loading = ref(false)
const loi = ref('')
const danhSach = ref([])
const tenNganh = ref('')
const tenChuongTrinh = ref('')
const tenVersion = ref('')
const tenKy = ref('')
const tenNhom = ref('')

const summary = computed(() => [
  { nhan: 'Ngành', giaTri: tenNganh.value || nganhId.value },
  { nhan: 'Chương trình', giaTri: tenChuongTrinh.value || chuongTrinhId.value },
  { nhan: 'Version', giaTri: tenVersion.value || versionId.value },
  { nhan: 'Kỳ', giaTri: tenKy.value || khungKyId.value },
  { nhan: 'Nhóm kiến thức', giaTri: tenNhom.value || nhomKienThucId.value }
])

async function taiDuLieu() {
  loading.value = true
  loi.value = ''
  try {
    const [nganh, chuongTrinh, version, ky, nhom, items] = await Promise.all([
      xemChuongTrinhService.layNganhById(nganhId.value),
      xemChuongTrinhService.layChuongTrinhById(chuongTrinhId.value),
      xemChuongTrinhService.layVersionById(versionId.value),
      xemChuongTrinhService.layKyById(khungKyId.value),
      xemChuongTrinhService.layNhomKienThucById(nhomKienThucId.value),
      xemChuongTrinhService.layMonTheoKyVaNhom(versionId.value, khungKyId.value, nhomKienThucId.value)
    ])
    tenNganh.value = nganh ? layTenHienThi(nganh, ['tenNganh', 'ten', 'maNganh']) : ''
    tenChuongTrinh.value = chuongTrinh ? layTenHienThi(chuongTrinh, ['tenChuongTrinh', 'ten', 'maChuongTrinh']) : ''
    tenVersion.value = version ? layTenHienThi(version, ['tenVersion', 'maVersion', 'namBanHanh', 'trangThai']) : ''
    tenKy.value = ky ? layTenHienThi(ky, ['tenKy', 'maKy', 'tenKhungKyGoc']) : ''
    tenNhom.value = nhom ? layTenHienThi(nhom, ['ten', 'tenNhomKienThuc', 'tenNhomKienThucGoc', 'ma']) : ''
    danhSach.value = items
  } catch (error) {
    loi.value = error?.message || 'Không tải được môn trong chương trình.'
  } finally {
    loading.value = false
  }
}

function moMonHoc(item) {
  router.push({
    name: 'XemChuongTrinh.Tang7MonHoc',
    params: { ...route.params, chuongTrinhMonId: item.id }
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
