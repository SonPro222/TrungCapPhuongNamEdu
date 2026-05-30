<template>
  <div class="tang-page">
    <XemTomTatHuongDi tieu-de="Tầng 1 - Ngành" :danh-sach="summary" />

    <h2 class="tang-title">Danh sách ngành</h2>

    <XemTrangDangTai v-if="loading" />
    <p v-else-if="loi" class="loi-text">{{ loi }}</p>
    <XemTrangRong v-else-if="!danhSach.length" thong-bao="Chưa có ngành nào để hiển thị." />
    <XemDanhSachNut
      v-else
      :danh-sach="danhSach"
      :ten-keys="['tenNganh', 'ten', 'maNganh']"
      @chon="moChuongTrinh"
    />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { xemChuongTrinhService } from '../services/xemChuongTrinhService'
import XemTomTatHuongDi from '../components/XemTomTatHuongDi.vue'
import XemDanhSachNut from '../components/XemDanhSachNut.vue'
import XemTrangRong from '../components/XemTrangRong.vue'
import XemTrangDangTai from '../components/XemTrangDangTai.vue'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const loi = ref('')
const danhSach = ref([])
const summary = ref([{ nhan: 'Tầng', giaTri: 'Ngành' }])

async function taiDuLieu() {
  loading.value = true
  loi.value = ''
  try {
    danhSach.value = await xemChuongTrinhService.layDanhSachNganh()
  } catch (error) {
    loi.value = error?.message || 'Không tải được danh sách ngành.'
  } finally {
    loading.value = false
  }
}

function moChuongTrinh(item) {
  router.push({
    name: 'XemChuongTrinh.Tang2ChuongTrinh',
    params: { ...route.params, nganhId: item.id }
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
