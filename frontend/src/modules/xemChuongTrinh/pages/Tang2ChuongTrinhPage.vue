<template>
  <div class="tang-page">
    <XemTomTatHuongDi tieu-de="Tầng 2 - Chương trình" :danh-sach="summary" />

    <h2 class="tang-title">Chương trình thuộc ngành</h2>

    <XemTrangDangTai v-if="loading" />
    <p v-else-if="loi" class="loi-text">{{ loi }}</p>
    <XemTrangRong v-else-if="!danhSach.length" thong-bao="Ngành này chưa có chương trình đã gán." />
    <XemDanhSachNut
      v-else
      :danh-sach="danhSach"
      :ten-keys="['tenChuongTrinh', 'ten', 'maChuongTrinh']"
      @chon="moVersion"
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

const loading = ref(false)
const loi = ref('')
const danhSach = ref([])
const tenNganh = ref('')

const summary = computed(() => [{ nhan: 'Ngành', giaTri: tenNganh.value || nganhId.value }])

async function taiDuLieu() {
  loading.value = true
  loi.value = ''
  try {
    const [nganh, items] = await Promise.all([
      xemChuongTrinhService.layNganhById(nganhId.value),
      xemChuongTrinhService.layChuongTrinhTheoNganh(nganhId.value)
    ])
    tenNganh.value = nganh ? layTenHienThi(nganh, ['tenNganh', 'ten', 'maNganh']) : ''
    danhSach.value = items
  } catch (error) {
    loi.value = error?.message || 'Không tải được chương trình.'
  } finally {
    loading.value = false
  }
}

function moVersion(item) {
  router.push({
    name: 'XemChuongTrinh.Tang3VersionChuongTrinh',
    params: { ...route.params, nganhId: nganhId.value, chuongTrinhId: item.id }
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
