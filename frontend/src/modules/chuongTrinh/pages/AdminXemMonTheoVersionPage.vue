<template>
  <main class="dao-tao-view-page">
    <header class="mini-head">
      <button class="btn" @click="$router.back()">← Quay lại</button>
      <div>
        <h2>5. Môn học{{ route.query.boLocTen ? ': ' + route.query.boLocTen : '' }}</h2>
        <p>Hiển thị môn theo version/kỳ/nhóm kiến thức. Bảng chương trình môn là bảng chính, môn học gốc nằm dưới để đối chiếu.</p>
      </div>
    </header>

    <BangQuanLyDaoTao title="5.1. Chương trình môn" :service="crudServices.chuongTrinhMon" :fields="configs.chuongTrinhMon.fields" :columns="configs.chuongTrinhMon.columns" :default-form="configs.chuongTrinhMon.defaultForm" :parent-values="{ chuongTrinhVersionId: versionId, khungKyId: route.query.khungKyId || null, nhomKienThucId: route.query.nhomKienThucId || null }" :parent-text="`Đang xem version ${versionId}${route.query.boLocTen ? ' / ' + route.query.boLocTen : ''}`" :rows="chuongTrinhMonDaLoc" :lookups="lookups" @saved="load" @deleted="load" @view="xemChiTietMon" />
    <BangQuanLyDaoTao title="5.2. Môn học gốc" :service="crudServices.monHoc" :fields="configs.monHoc.fields" :columns="configs.monHoc.columns" :default-form="configs.monHoc.defaultForm" :rows="monHoc" :lookups="lookups" :show-view="false" @saved="load" @deleted="load" />
  </main>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import BangQuanLyDaoTao from '../components/BangQuanLyDaoTao.vue'
import { configs, crudServices, luaChonDaoTao, taiNhieuBang, taoMap, withNames } from '../services/daoTaoXemChuongTrinhService'
const route = useRoute(); const router = useRouter(); const versionId = Number(route.params.versionId)
const data = ref({})
const khungKy = computed(() => (data.value.khungKy || []).filter((i) => String(i.chuongTrinhVersionId) === String(versionId)))
const nhomKienThuc = computed(() => (data.value.nhomKienThuc || []).filter((i) => String(i.chuongTrinhVersionId) === String(versionId)))
const monHoc = computed(() => data.value.monHoc || [])
const chuongTrinhMon = computed(() => {
  const monMap = taoMap(monHoc.value); const kyMap = taoMap(khungKy.value); const nhomMap = taoMap(nhomKienThuc.value)
  return withNames((data.value.chuongTrinhMon || []).filter((i) => String(i.chuongTrinhVersionId) === String(versionId)), { mon: monMap, ky: kyMap, nhom: nhomMap }, [
    { map: 'mon', idKey: 'monHocId', nameKey: 'tenMonHoc', keys: ['tenMon', 'maMon'] },
    { map: 'ky', idKey: 'khungKyId', nameKey: 'tenKhungKy', keys: ['tenKy', 'maKy'] },
    { map: 'nhom', idKey: 'nhomKienThucId', nameKey: 'tenNhomKienThuc', keys: ['ten', 'ma'] }
  ])
})
const chuongTrinhMonDaLoc = computed(() => chuongTrinhMon.value.filter((i) => !route.query.khungKyId || String(i.khungKyId) === String(route.query.khungKyId)).filter((i) => !route.query.nhomKienThucId || String(i.nhomKienThucId) === String(route.query.nhomKienThucId)))
const lookups = computed(() => ({ ...data.value, khungKy: khungKy.value, nhomKienThuc: nhomKienThuc.value, chuongTrinhMon: chuongTrinhMon.value, ...luaChonDaoTao }))
async function load() { data.value = await taiNhieuBang(['monHoc','chuongTrinhMon','khungKy','nhomKienThuc','chuongTrinhVersion']) }
function xemChiTietMon(mon) { router.push({ name: 'ChuongTrinh.DaoTaoChiTietMon', params: { chuongTrinhMonId: mon.id }, query: { ...route.query, versionId, monTen: mon.tenMonHoc || mon.maMonTrongCt } }) }
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
