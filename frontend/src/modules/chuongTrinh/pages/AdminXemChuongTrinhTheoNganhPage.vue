<template>
  <main class="dao-tao-view-page">
    <header class="mini-head"><button class="btn" @click="$router.back()">← Quay lại</button><div><h2>Chương trình theo ngành</h2><p>{{ nganh?.maNganh || '-' }} - {{ nganh?.tenNganh || route.query.nganhTen || '-' }}</p></div></header>

    <BangQuanLyDaoTao title="2.3. Chương trình đào tạo" description="Chương trình thuộc ngành đang xem. Có thể tạo/sửa ngay tại tầng này." :service="crudServices.chuongTrinh" :fields="configs.chuongTrinh.fields" :columns="configs.chuongTrinh.columns" :default-form="configs.chuongTrinh.defaultForm" :parent-values="{ nganhId }" :parent-text="`Đang xem ngành: ${nganh?.tenNganh || route.query.nganhTen || nganhId}`" :rows="chuongTrinhTheoNganh" :lookups="lookups" @saved="load" @deleted="load" @view="xemVersion" />
    <BangQuanLyDaoTao title="2.1. Trình độ đào tạo" :service="crudServices.trinhDoDaoTao" :fields="configs.trinhDoDaoTao.fields" :columns="configs.trinhDoDaoTao.columns" :default-form="configs.trinhDoDaoTao.defaultForm" :rows="trinhDoDaoTao" :lookups="lookups" :show-view="false" @saved="load" @deleted="load" />
    <BangQuanLyDaoTao title="2.2. Loại chương trình" :service="crudServices.loaiChuongTrinh" :fields="configs.loaiChuongTrinh.fields" :columns="configs.loaiChuongTrinh.columns" :default-form="configs.loaiChuongTrinh.defaultForm" :rows="loaiChuongTrinh" :lookups="lookups" :show-view="false" @saved="load" @deleted="load" />
  </main>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import BangQuanLyDaoTao from '../components/BangQuanLyDaoTao.vue'
import { configs, crudServices, luaChonDaoTao, taiBang, taoMap, withNames } from '../services/daoTaoXemChuongTrinhService'

const route = useRoute(); const router = useRouter(); const nganhId = Number(route.params.nganhId)
const nganh = ref(null); const trinhDoDaoTao = ref([]); const loaiChuongTrinh = ref([]); const chuongTrinh = ref([])
const chuongTrinhTheoNganh = computed(() => {
  const trinhMap = taoMap(trinhDoDaoTao.value); const loaiMap = taoMap(loaiChuongTrinh.value)
  return withNames(chuongTrinh.value.filter((item) => String(item.nganhId) === String(nganhId)), { trinh: trinhMap, loai: loaiMap }, [
    { map: 'trinh', idKey: 'trinhDoId', nameKey: 'tenTrinhDo', keys: ['tenTrinhDo'] },
    { map: 'loai', idKey: 'loaiChuongTrinhId', nameKey: 'tenLoaiChuongTrinh', keys: ['tenLoai'] }
  ])
})
const lookups = computed(() => ({ nganh: nganh.value ? [nganh.value] : [], trinhDoDaoTao: trinhDoDaoTao.value, loaiChuongTrinh: loaiChuongTrinh.value, chuongTrinh: chuongTrinhTheoNganh.value, ...luaChonDaoTao }))
async function load() {
  const [nganhList, trinh, loai, ct] = await Promise.all([taiBang('nganh'), taiBang('trinhDoDaoTao'), taiBang('loaiChuongTrinh'), taiBang('chuongTrinh')])
  nganh.value = nganhList.find((item) => String(item.id) === String(nganhId)) || null
  trinhDoDaoTao.value = trinh; loaiChuongTrinh.value = loai; chuongTrinh.value = ct
}
function xemVersion(item) { router.push({ name: 'ChuongTrinh.DaoTaoVersionTheoChuongTrinh', params: { chuongTrinhId: item.id }, query: { ...route.query, nganhId, chuongTrinhTen: item.tenChuongTrinh } }) }
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
