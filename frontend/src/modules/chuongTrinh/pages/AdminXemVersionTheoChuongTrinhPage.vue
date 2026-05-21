<template>
  <main class="dao-tao-view-page">
    <header class="mini-head"><button class="btn" @click="$router.back()">← Quay lại</button><div><h2>Version theo chương trình</h2><p>{{ chuongTrinh?.maChuongTrinh || '-' }} - {{ chuongTrinh?.tenChuongTrinh || route.query.chuongTrinhTen || '-' }}</p></div></header>
    <BangQuanLyDaoTao title="3. Chương trình version" description="Tạo/sửa version thuộc chương trình đang xem." :service="crudServices.chuongTrinhVersion" :fields="configs.chuongTrinhVersion.fields" :columns="configs.chuongTrinhVersion.columns" :default-form="configs.chuongTrinhVersion.defaultForm" :parent-values="{ chuongTrinhId }" :parent-text="`Đang xem chương trình: ${chuongTrinh?.tenChuongTrinh || chuongTrinhId}`" :rows="versions" :lookups="lookups" @saved="load" @deleted="load" @view="xemChiTietVersion" />
  </main>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import BangQuanLyDaoTao from '../components/BangQuanLyDaoTao.vue'
import { configs, crudServices, luaChonDaoTao, taiBang } from '../services/daoTaoXemChuongTrinhService'
const route = useRoute(); const router = useRouter(); const chuongTrinhId = Number(route.params.chuongTrinhId)
const chuongTrinh = ref(null); const allChuongTrinh = ref([]); const allVersions = ref([])
const versions = computed(() => allVersions.value.filter((item) => String(item.chuongTrinhId) === String(chuongTrinhId)))
const lookups = computed(() => ({ chuongTrinh: allChuongTrinh.value, chuongTrinhVersion: versions.value, ...luaChonDaoTao }))
async function load() {
  const [ct, ver] = await Promise.all([taiBang('chuongTrinh'), taiBang('chuongTrinhVersion')])
  allChuongTrinh.value = ct; allVersions.value = ver; chuongTrinh.value = ct.find((item) => String(item.id) === String(chuongTrinhId)) || null
}
function xemChiTietVersion(item) { router.push({ name: 'ChuongTrinh.DaoTaoChiTietVersion', params: { versionId: item.id }, query: { ...route.query, chuongTrinhId, versionTen: item.tenVersion || item.maVersion } }) }
onMounted(load)
</script>

<style scoped>
.dao-tao-view-page {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.mini-head {
  display: flex;
  gap: 10px;
  align-items: center;
  background: #eff6ff;
  border: 1px solid #bfdbfe;
  border-radius: 4px;
  padding: 10px;
  color: #1e3a8a;
}

.mini-head h2 {
  margin: 0;
  font-size: 18px;
  color: #1e3a8a;
}

.mini-head p {
  margin: 4px 0 0;
  color: #1d4ed8;
  font-size: 13px;
}

.btn {
  border: 1px solid #93c5fd;
  background: #ffffff;
  color: #1e40af;
  border-radius: 3px;
  padding: 6px 10px;
  cursor: pointer;
  font-weight: 700;
}

.btn:hover {
  background: #dbeafe;
}
</style>
