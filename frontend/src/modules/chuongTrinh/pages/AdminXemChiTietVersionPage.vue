<template>
  <main class="dao-tao-view-page">
    <header class="mini-head">
      <button class="btn" @click="$router.back()">← Quay lại</button>
      <div>
        <h2>Chi tiết version đào tạo</h2>
        <p>{{ version?.maVersion || '-' }} - {{ version?.tenVersion || route.query.versionTen || '-' }}</p>
      </div>
    </header>

    <BangQuanLyDaoTao title="4.1. Khung kỳ" :service="crudServices.khungKy" :fields="configs.khungKy.fields" :columns="configs.khungKy.columns" :default-form="configs.khungKy.defaultForm" :parent-values="{ chuongTrinhVersionId: versionId, loaiChuongTrinhId: chuongTrinh?.loaiChuongTrinhId || null }" :parent-text="versionText" :rows="khungKyTheoVersion" :lookups="lookups" @saved="load" @deleted="load" @view="xemMonTheoKy" />

    <BangQuanLyDaoTao title="4.10. Chương trình môn" :service="crudServices.chuongTrinhMon" :fields="configs.chuongTrinhMon.fields" :columns="configs.chuongTrinhMon.columns" :default-form="configs.chuongTrinhMon.defaultForm" :parent-values="{ chuongTrinhVersionId: versionId }" :parent-text="versionText" :rows="chuongTrinhMonTheoVersion" :lookups="lookups" @saved="load" @deleted="load" @view="xemChiTietMon" />

    <BangQuanLyDaoTao title="4.8. Nhóm tự chọn" :service="crudServices.nhomTuChon" :fields="configs.nhomTuChon.fields" :columns="configs.nhomTuChon.columns" :default-form="configs.nhomTuChon.defaultForm" :parent-values="{ chuongTrinhVersionId: versionId }" :parent-text="versionText" :rows="nhomTuChonTheoVersion" :lookups="lookups" @saved="load" @deleted="load" @view="chonNhomTuChon" />
    <BangQuanLyDaoTao title="4.8.1. Môn tự chọn" :service="crudServices.monTuChon" :fields="configs.monTuChon.fields" :columns="configs.monTuChon.columns" :default-form="configs.monTuChon.defaultForm" :parent-values="{ nhomId: selectedNhomTuChon?.id || null }" :parent-text="selectedNhomTuChon ? `Đang xem nhóm tự chọn: ${selectedNhomTuChon.ten}` : 'Chọn/Xem nhóm tự chọn để lọc môn tự chọn'" :rows="monTuChonTheoNhom" :lookups="lookups" :disabled="!selectedNhomTuChon" disabled-text="Cần bấm Xem một nhóm tự chọn trước." :show-view="false" @saved="load" @deleted="load" />

    <BangQuanLyDaoTao title="4.2. Nhóm kiến thức" :service="crudServices.nhomKienThuc" :fields="configs.nhomKienThuc.fields" :columns="configs.nhomKienThuc.columns" :default-form="configs.nhomKienThuc.defaultForm" :parent-values="{ chuongTrinhVersionId: versionId }" :parent-text="versionText" :rows="nhomKienThucTheoVersion" :lookups="lookups" @saved="load" @deleted="load" @view="xemMonTheoNhom" />
    <BangQuanLyDaoTao title="4.3. Syllabus chương trình" :service="crudServices.syllabusChuongTrinh" :fields="configs.syllabusChuongTrinh.fields" :columns="configs.syllabusChuongTrinh.columns" :default-form="configs.syllabusChuongTrinh.defaultForm" :parent-values="{ chuongTrinhVersionId: versionId }" :parent-text="versionText" :rows="syllabusChuongTrinhTheoVersion" :lookups="lookups" :show-view="false" @saved="load" @deleted="load" />

    <BangQuanLyDaoTao title="4.4. Mục tiêu chương trình gốc" :service="crudServices.mucTieuChuongTrinhGoc" :fields="configs.mucTieuChuongTrinhGoc.fields" :columns="configs.mucTieuChuongTrinhGoc.columns" :default-form="configs.mucTieuChuongTrinhGoc.defaultForm" :rows="mucTieuChuongTrinhGoc" :lookups="lookups" :show-view="false" @saved="load" @deleted="load" />
    <BangQuanLyDaoTao title="4.4.1. Mục tiêu gán vào version" :service="crudServices.chuongTrinhVersionMucTieu" :fields="configs.chuongTrinhVersionMucTieu.fields" :columns="configs.chuongTrinhVersionMucTieu.columns" :default-form="configs.chuongTrinhVersionMucTieu.defaultForm" :parent-values="{ chuongTrinhVersionId: versionId }" :parent-text="versionText" :rows="mucTieuNoiTheoVersion" :lookups="lookups" :show-view="false" @saved="load" @deleted="load" />

    <BangQuanLyDaoTao title="4.5. Năng lực đầu ra gốc" :service="crudServices.nangLucDauRaGoc" :fields="configs.nangLucDauRaGoc.fields" :columns="configs.nangLucDauRaGoc.columns" :default-form="configs.nangLucDauRaGoc.defaultForm" :rows="nangLucDauRaGoc" :lookups="lookups" :show-view="false" @saved="load" @deleted="load" />
    <BangQuanLyDaoTao title="4.5.1. Năng lực gán vào version" :service="crudServices.chuongTrinhVersionNangLuc" :fields="configs.chuongTrinhVersionNangLuc.fields" :columns="configs.chuongTrinhVersionNangLuc.columns" :default-form="configs.chuongTrinhVersionNangLuc.defaultForm" :parent-values="{ chuongTrinhVersionId: versionId }" :parent-text="versionText" :rows="nangLucNoiTheoVersion" :lookups="lookups" :show-view="false" @saved="load" @deleted="load" />

    <BangQuanLyDaoTao title="4.6. Vị trí việc làm gốc" :service="crudServices.viTriViecLamGoc" :fields="configs.viTriViecLamGoc.fields" :columns="configs.viTriViecLamGoc.columns" :default-form="configs.viTriViecLamGoc.defaultForm" :rows="viTriViecLamGoc" :lookups="lookups" :show-view="false" @saved="load" @deleted="load" />
    <BangQuanLyDaoTao title="4.6.1. Vị trí việc làm gán vào version" :service="crudServices.chuongTrinhVersionViTriViecLam" :fields="configs.chuongTrinhVersionViTriViecLam.fields" :columns="configs.chuongTrinhVersionViTriViecLam.columns" :default-form="configs.chuongTrinhVersionViTriViecLam.defaultForm" :parent-values="{ chuongTrinhVersionId: versionId }" :parent-text="versionText" :rows="viTriNoiTheoVersion" :lookups="lookups" :show-view="false" @saved="load" @deleted="load" />

    <BangQuanLyDaoTao title="4.7. Điều kiện tốt nghiệp gốc" :service="crudServices.dieuKienTotNghiepGoc" :fields="configs.dieuKienTotNghiepGoc.fields" :columns="configs.dieuKienTotNghiepGoc.columns" :default-form="configs.dieuKienTotNghiepGoc.defaultForm" :rows="dieuKienTotNghiepGoc" :lookups="lookups" :show-view="false" @saved="load" @deleted="load" />
    <BangQuanLyDaoTao title="4.7.1. Điều kiện tốt nghiệp gán vào version" :service="crudServices.chuongTrinhVersionDieuKienTotNghiep" :fields="configs.chuongTrinhVersionDieuKienTotNghiep.fields" :columns="configs.chuongTrinhVersionDieuKienTotNghiep.columns" :default-form="configs.chuongTrinhVersionDieuKienTotNghiep.defaultForm" :parent-values="{ chuongTrinhVersionId: versionId }" :parent-text="versionText" :rows="dieuKienNoiTheoVersion" :lookups="lookups" :show-view="false" @saved="load" @deleted="load" />
  </main>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import BangQuanLyDaoTao from '../components/BangQuanLyDaoTao.vue'
import { configs, crudServices, luaChonDaoTao, taiNhieuBang, taoMap, tenCua, withNames } from '../services/daoTaoXemChuongTrinhService'

const route = useRoute(); const router = useRouter(); const versionId = Number(route.params.versionId)
const data = ref({}); const selectedNhomTuChon = ref(null)
const version = computed(() => (data.value.chuongTrinhVersion || []).find((item) => String(item.id) === String(versionId)))
const chuongTrinh = computed(() => (data.value.chuongTrinh || []).find((item) => String(item.id) === String(version.value?.chuongTrinhId)))
const versionText = computed(() => `Đang xem version: ${version.value?.tenVersion || version.value?.maVersion || versionId}`)
const khungKyTheoVersion = computed(() => (data.value.khungKy || []).filter((i) => String(i.chuongTrinhVersionId) === String(versionId)))
const nhomKienThucTheoVersion = computed(() => (data.value.nhomKienThuc || []).filter((i) => String(i.chuongTrinhVersionId) === String(versionId)))
const syllabusChuongTrinhTheoVersion = computed(() => (data.value.syllabusChuongTrinh || []).filter((i) => String(i.chuongTrinhVersionId) === String(versionId)))
const nhomTuChonTheoVersion = computed(() => (data.value.nhomTuChon || []).filter((i) => String(i.chuongTrinhVersionId) === String(versionId)))
const monHoc = computed(() => data.value.monHoc || [])
const chuongTrinhMonTheoVersion = computed(() => {
  const monMap = taoMap(data.value.monHoc || []); const kyMap = taoMap(khungKyTheoVersion.value); const nhomMap = taoMap(nhomKienThucTheoVersion.value)
  return withNames((data.value.chuongTrinhMon || []).filter((i) => String(i.chuongTrinhVersionId) === String(versionId)), { mon: monMap, ky: kyMap, nhom: nhomMap }, [
    { map: 'mon', idKey: 'monHocId', nameKey: 'tenMonHoc', keys: ['tenMon', 'maMon'] },
    { map: 'ky', idKey: 'khungKyId', nameKey: 'tenKhungKy', keys: ['tenKy', 'maKy'] },
    { map: 'nhom', idKey: 'nhomKienThucId', nameKey: 'tenNhomKienThuc', keys: ['ten', 'ma'] }
  ])
})
const monTuChonTheoNhom = computed(() => {
  if (!selectedNhomTuChon.value) return []
  const ctmMap = taoMap(chuongTrinhMonTheoVersion.value)
  return withNames((data.value.monTuChon || []).filter((i) => String(i.nhomId) === String(selectedNhomTuChon.value.id)), { ctm: ctmMap, nhom: taoMap(nhomTuChonTheoVersion.value) }, [
    { map: 'ctm', idKey: 'chuongTrinhMonId', nameKey: 'tenChuongTrinhMon', keys: ['maMonTrongCt', 'tenMonHoc'] },
    { map: 'nhom', idKey: 'nhomId', nameKey: 'tenNhomTuChon', keys: ['ten'] }
  ])
})
const mucTieuChuongTrinhGoc = computed(() => data.value.mucTieuChuongTrinhGoc || [])
const nangLucDauRaGoc = computed(() => data.value.nangLucDauRaGoc || [])
const viTriViecLamGoc = computed(() => data.value.viTriViecLamGoc || [])
const dieuKienTotNghiepGoc = computed(() => data.value.dieuKienTotNghiepGoc || [])
const mucTieuNoiTheoVersion = computed(() => ganTenNoi('chuongTrinhVersionMucTieu', 'mucTieuChuongTrinhGoc', 'mucTieuGocId', 'tenMucTieuGoc'))
const nangLucNoiTheoVersion = computed(() => ganTenNoi('chuongTrinhVersionNangLuc', 'nangLucDauRaGoc', 'nangLucGocId', 'tenNangLucGoc'))
const viTriNoiTheoVersion = computed(() => ganTenNoi('chuongTrinhVersionViTriViecLam', 'viTriViecLamGoc', 'viTriViecLamGocId', 'tenViTriViecLamGoc'))
const dieuKienNoiTheoVersion = computed(() => ganTenNoi('chuongTrinhVersionDieuKienTotNghiep', 'dieuKienTotNghiepGoc', 'dieuKienTotNghiepGocId', 'tenDieuKienTotNghiepGoc'))
const lookups = computed(() => ({ ...data.value, khungKy: khungKyTheoVersion.value, nhomKienThuc: nhomKienThucTheoVersion.value, chuongTrinhMon: chuongTrinhMonTheoVersion.value, monTuChon: monTuChonTheoNhom.value, ...luaChonDaoTao }))
function ganTenNoi(noiKey, gocKey, idKey, nameKey) {
  const gocMap = taoMap(data.value[gocKey] || [])
  return (data.value[noiKey] || []).filter((i) => String(i.chuongTrinhVersionId) === String(versionId)).map((i) => ({ ...i, [nameKey]: tenCua(gocMap.get(i[idKey]), ['ma', 'ten', 'noiDung']) }))
}
async function load() {
  data.value = await taiNhieuBang(['chuongTrinhVersion','chuongTrinh','loaiChuongTrinh','khungKy','nhomKienThuc','syllabusChuongTrinh','nhomTuChon','monTuChon','monHoc','chuongTrinhMon','mucTieuChuongTrinhGoc','nangLucDauRaGoc','viTriViecLamGoc','dieuKienTotNghiepGoc','chuongTrinhVersionMucTieu','chuongTrinhVersionNangLuc','chuongTrinhVersionViTriViecLam','chuongTrinhVersionDieuKienTotNghiep'])
}
function chonNhomTuChon(item) { selectedNhomTuChon.value = item }
function xemMonTheoKy(ky) { router.push({ name: 'ChuongTrinh.DaoTaoMonTheoVersion', params: { versionId }, query: { ...route.query, khungKyId: ky.id, boLocTen: ky.tenKy || ky.maKy } }) }
function xemMonTheoNhom(nhom) { router.push({ name: 'ChuongTrinh.DaoTaoMonTheoVersion', params: { versionId }, query: { ...route.query, nhomKienThucId: nhom.id, boLocTen: nhom.ten || nhom.ma } }) }
function xemChiTietMon(mon) { router.push({ name: 'ChuongTrinh.DaoTaoChiTietMon', params: { chuongTrinhMonId: mon.id }, query: { ...route.query, versionId, versionTen: version.value?.tenVersion, monTen: mon.tenMonHoc || mon.maMonTrongCt } }) }
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
