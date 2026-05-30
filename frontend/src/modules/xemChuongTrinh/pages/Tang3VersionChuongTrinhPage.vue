<template>
  <div class="tang-page">
    <XemTomTatHuongDi tieu-de="Tầng 3 - Version chương trình" :danh-sach="summary" />

    <h2 class="tang-title">Version của chương trình</h2>

    <XemTrangDangTai v-if="loading" />
    <p v-else-if="loi" class="loi-text">{{ loi }}</p>
    <XemTrangRong v-else-if="!danhSach.length" thong-bao="Chương trình này chưa có version đã gán." />
    <XemDanhSachNut
      v-else
      :danh-sach="danhSach"
      :ten-keys="['tenVersion', 'maVersion', 'namBanHanh', 'id']"
      :mo-ta-keys="['maVersion', 'namBanHanh', 'trangThai']"
      @chon="moKy"
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

const loading = ref(false)
const loi = ref('')
const danhSach = ref([])
const tenNganh = ref('')
const tenChuongTrinh = ref('')

const summary = computed(() => [
  { nhan: 'Ngành', giaTri: tenNganh.value || nganhId.value },
  { nhan: 'Chương trình', giaTri: tenChuongTrinh.value || chuongTrinhId.value }
])

async function taiDuLieu() {
  loading.value = true
  loi.value = ''
  try {
    const [nganh, chuongTrinh, items] = await Promise.all([
      xemChuongTrinhService.layNganhById(nganhId.value),
      xemChuongTrinhService.layChuongTrinhById(chuongTrinhId.value),
      xemChuongTrinhService.layVersionTheoChuongTrinh(chuongTrinhId.value)
    ])
    tenNganh.value = nganh ? layTenHienThi(nganh, ['tenNganh', 'ten', 'maNganh']) : ''
    tenChuongTrinh.value = chuongTrinh ? layTenHienThi(chuongTrinh, ['tenChuongTrinh', 'ten', 'maChuongTrinh']) : ''
    danhSach.value = items
  } catch (error) {
    loi.value = error?.message || 'Không tải được version chương trình.'
  } finally {
    loading.value = false
  }
}

function moKy(item) {
  router.push({
    name: 'XemChuongTrinh.Tang4Ky',
    params: { ...route.params, versionId: item.id }
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
