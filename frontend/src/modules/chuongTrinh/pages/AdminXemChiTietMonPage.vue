<template>
  <main class="dao-tao-view-page">
    <header class="mini-head"><button class="btn" @click="$router.back()">← Quay lại</button><div><h2>Chi tiết môn trong chương trình</h2><p>{{ chuongTrinhMon?.maMonTrongCt || '-' }} - {{ chuongTrinhMon?.tenMonHoc || route.query.monTen || '-' }}</p></div></header>

    <BangQuanLyDaoTao title="Chương trình môn hiện tại" :service="crudServices.chuongTrinhMon" :fields="configs.chuongTrinhMon.fields" :columns="configs.chuongTrinhMon.columns" :default-form="configs.chuongTrinhMon.defaultForm" :rows="chuongTrinhMonDangXem" :lookups="lookups" :show-view="false" @saved="load" @deleted="load" />
    <BangQuanLyDaoTao title="Syllabus môn học" :service="crudServices.syllabusMonHoc" :fields="configs.syllabusMonHoc.fields" :columns="configs.syllabusMonHoc.columns" :default-form="configs.syllabusMonHoc.defaultForm" :parent-values="{ chuongTrinhMonId }" :parent-text="monText" :rows="syllabusTheoMon" :lookups="lookups" @saved="load" @deleted="load" @view="chonSyllabus" />

    <BangQuanLyDaoTao title="Môn học gốc" :service="crudServices.monHoc" :fields="configs.monHoc.fields" :columns="configs.monHoc.columns" :default-form="configs.monHoc.defaultForm" :rows="monHocDangXem" :lookups="lookups" :show-view="false" @saved="load" @deleted="load" />
    <BangQuanLyDaoTao title="Môn tiên quyết" :service="crudServices.monTienQuyet" :fields="configs.monTienQuyet.fields" :columns="configs.monTienQuyet.columns" :default-form="configs.monTienQuyet.defaultForm" :parent-values="{ monId: chuongTrinhMonId }" :parent-text="monText" :rows="monTienQuyetTheoMon" :lookups="lookups" :show-view="false" @saved="load" @deleted="load" />
    <BangQuanLyDaoTao title="Quy đổi điểm" :service="crudServices.quyDoiDiem" :fields="configs.quyDoiDiem.fields" :columns="configs.quyDoiDiem.columns" :default-form="configs.quyDoiDiem.defaultForm" :parent-values="{ chuongTrinhMonId }" :parent-text="monText" :rows="quyDoiDiemTheoMon" :lookups="lookups" :show-view="false" @saved="load" @deleted="load" />
    <BangQuanLyDaoTao title="Quy đổi điểm mẫu" :service="crudServices.quyDoiDiemMau" :fields="configs.quyDoiDiemMau.fields" :columns="configs.quyDoiDiemMau.columns" :default-form="configs.quyDoiDiemMau.defaultForm" :rows="quyDoiDiemMau" :lookups="lookups" :show-view="false" @saved="load" @deleted="load" />
    <BangQuanLyDaoTao title="Gán quy đổi điểm mẫu vào môn" :service="crudServices.chuongTrinhMonQuyDoiDiemMau" :fields="configs.chuongTrinhMonQuyDoiDiemMau.fields" :columns="configs.chuongTrinhMonQuyDoiDiemMau.columns" :default-form="configs.chuongTrinhMonQuyDoiDiemMau.defaultForm" :parent-values="{ chuongTrinhMonId }" :parent-text="monText" :rows="quyDoiDiemMauNoiTheoMon" :lookups="lookups" :show-view="false" @saved="load" @deleted="load" />

    <BangQuanLyDaoTao title="Điều kiện môn học" :service="crudServices.dieuKienMonHoc" :fields="configs.dieuKienMonHoc.fields" :columns="configs.dieuKienMonHoc.columns" :default-form="configs.dieuKienMonHoc.defaultForm" :parent-values="{ syllabusMonId: selectedSyllabus?.id || syllabusTheoMon[0]?.id || null }" :parent-text="syllabusText" :rows="dieuKienTheoSyllabus" :lookups="lookups" :disabled="!selectedSyllabus && !syllabusTheoMon[0]" disabled-text="Cần có Syllabus môn học trước." :show-view="false" @saved="load" @deleted="load" />
    <BangQuanLyDaoTao title="Syllabus chương bài" :service="crudServices.syllabusChuongBai" :fields="configs.syllabusChuongBai.fields" :columns="configs.syllabusChuongBai.columns" :default-form="configs.syllabusChuongBai.defaultForm" :parent-values="{ syllabusMonId: selectedSyllabus?.id || syllabusTheoMon[0]?.id || null }" :parent-text="syllabusText" :rows="chuongBaiTheoSyllabus" :lookups="lookups" :disabled="!selectedSyllabus && !syllabusTheoMon[0]" disabled-text="Cần có Syllabus môn học trước." :show-view="false" @saved="load" @deleted="load" />
    <BangQuanLyDaoTao title="Syllabus tài liệu" :service="crudServices.syllabusTaiLieu" :fields="configs.syllabusTaiLieu.fields" :columns="configs.syllabusTaiLieu.columns" :default-form="configs.syllabusTaiLieu.defaultForm" :parent-values="{ syllabusMonId: selectedSyllabus?.id || syllabusTheoMon[0]?.id || null }" :parent-text="syllabusText" :rows="taiLieuTheoSyllabus" :lookups="lookups" :disabled="!selectedSyllabus && !syllabusTheoMon[0]" disabled-text="Cần có Syllabus môn học trước." :show-view="false" @saved="load" @deleted="load" />
  </main>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import BangQuanLyDaoTao from '../components/BangQuanLyDaoTao.vue'
import { configs, crudServices, luaChonDaoTao, taiNhieuBang, taoMap, tenCua, withNames } from '../services/daoTaoXemChuongTrinhService'
const route = useRoute(); const chuongTrinhMonId = Number(route.params.chuongTrinhMonId)
const data = ref({}); const selectedSyllabus = ref(null)
const chuongTrinhMon = computed(() => {
  const monMap = taoMap(data.value.monHoc || []); const kyMap = taoMap(data.value.khungKy || []); const nhomMap = taoMap(data.value.nhomKienThuc || [])
  return withNames((data.value.chuongTrinhMon || []).filter((i) => String(i.id) === String(chuongTrinhMonId)), { mon: monMap, ky: kyMap, nhom: nhomMap }, [
    { map: 'mon', idKey: 'monHocId', nameKey: 'tenMonHoc', keys: ['tenMon', 'maMon'] },
    { map: 'ky', idKey: 'khungKyId', nameKey: 'tenKhungKy', keys: ['tenKy', 'maKy'] },
    { map: 'nhom', idKey: 'nhomKienThucId', nameKey: 'tenNhomKienThuc', keys: ['ten', 'ma'] }
  ])[0]
})
const monText = computed(() => `Đang xem môn CT: ${chuongTrinhMon.value?.maMonTrongCt || chuongTrinhMonId}`)
const chuongTrinhMonDangXem = computed(() => chuongTrinhMon.value ? [chuongTrinhMon.value] : [])
const monHocDangXem = computed(() => (data.value.monHoc || []).filter((i) => String(i.id) === String(chuongTrinhMon.value?.monHocId)))
const syllabusTheoMon = computed(() => (data.value.syllabusMonHoc || []).filter((i) => String(i.chuongTrinhMonId) === String(chuongTrinhMonId)))
const syllabusId = computed(() => selectedSyllabus.value?.id || syllabusTheoMon.value[0]?.id || null)
const syllabusText = computed(() => `Đang xem syllabus môn ID: ${syllabusId.value || '-'}`)
const monTienQuyetTheoMon = computed(() => {
  const ctmMap = taoMap(data.value.chuongTrinhMon || [])
  return withNames((data.value.monTienQuyet || []).filter((i) => String(i.monId) === String(chuongTrinhMonId)), { current: ctmMap, dk: ctmMap }, [
    { map: 'current', idKey: 'monId', nameKey: 'tenMonHienTai', keys: ['maMonTrongCt'] },
    { map: 'dk', idKey: 'monDieuKienId', nameKey: 'tenMonDieuKien', keys: ['maMonTrongCt'] }
  ])
})
const quyDoiDiemTheoMon = computed(() => (data.value.quyDoiDiem || []).filter((i) => String(i.chuongTrinhMonId) === String(chuongTrinhMonId)))
const quyDoiDiemMau = computed(() => data.value.quyDoiDiemMau || [])
const quyDoiDiemMauNoiTheoMon = computed(() => {
  const mauMap = taoMap(data.value.quyDoiDiemMau || [])
  return (data.value.chuongTrinhMonQuyDoiDiemMau || []).filter((i) => String(i.chuongTrinhMonId) === String(chuongTrinhMonId)).map((i) => ({ ...i, tenQuyDoiDiemMau: tenCua(mauMap.get(i.quyDoiDiemMauId), ['ma', 'ten']) }))
})
const dieuKienTheoSyllabus = computed(() => (data.value.dieuKienMonHoc || []).filter((i) => String(i.syllabusMonId) === String(syllabusId.value)))
const chuongBaiTheoSyllabus = computed(() => (data.value.syllabusChuongBai || []).filter((i) => String(i.syllabusMonId) === String(syllabusId.value)))
const taiLieuTheoSyllabus = computed(() => (data.value.syllabusTaiLieu || []).filter((i) => String(i.syllabusMonId) === String(syllabusId.value)))
const lookups = computed(() => ({ ...data.value, chuongTrinhMon: data.value.chuongTrinhMon || [], syllabusMonHoc: syllabusTheoMon.value, ...luaChonDaoTao }))
async function load() { data.value = await taiNhieuBang(['chuongTrinhMon','monHoc','khungKy','nhomKienThuc','monTienQuyet','quyDoiDiem','quyDoiDiemMau','chuongTrinhMonQuyDoiDiemMau','syllabusMonHoc','dieuKienMonHoc','syllabusChuongBai','syllabusTaiLieu']) }
function chonSyllabus(item) { selectedSyllabus.value = item }
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
