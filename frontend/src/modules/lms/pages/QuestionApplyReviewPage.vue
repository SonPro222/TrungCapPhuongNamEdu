<template>
  <section class="lms-page">
    <div class="page-head">
      <div><h1>Rà soát áp dụng câu hỏi</h1><p>Duyệt mapping câu hỏi version vào chương trình môn/version chương trình.</p></div>
      <RouterLink :to="{ name:'Lms.NganHangCauHoiThem' }" class="lms-btn primary">+ Soạn câu hỏi</RouterLink>
    </div>
    <div class="filter-card">
      <select v-model="filters.chuongTrinhVersionId" class="fi" @change="loadCtm"><option value="">Tất cả version CTĐT</option><option v-for="v in versions" :key="v.id" :value="v.id">Version #{{ v.id }} {{ v.tenVersion || v.maVersion || '' }}</option></select>
      <select v-model="filters.chuongTrinhMonId" class="fi"><option value="">Tất cả chương trình môn</option><option v-for="m in chuongTrinhMons" :key="m.id" :value="m.id">{{ m.maMonHoc }} - {{ m.tenMonHoc }}</option></select>
      <select v-model="filters.trangThai" class="fi"><option value="">Tất cả trạng thái</option><option v-for="(label,key) in TRANG_THAI_CAU_HOI_AP_DUNG" :key="key" :value="key">{{ label }}</option></select>
      <button class="lms-btn" @click="taiDuLieu">Tải lại</button>
    </div>
    <div class="table-card">
      <table><thead><tr><th>Câu hỏi</th><th>Môn / CTM</th><th>Chủ đề</th><th>Trạng thái</th><th>Ghi chú</th><th>Thao tác</th></tr></thead><tbody>
        <tr v-if="dangTai"><td colspan="6" class="empty">Đang tải...</td></tr>
        <tr v-else-if="!dsLoc.length"><td colspan="6" class="empty">Không có mapping áp dụng.</td></tr>
        <tr v-for="a in dsLoc" :key="a.id"><td><b>{{ a.maCauHoi }}</b><br/><span class="line">{{ a.noiDung }}</span></td><td><b>{{ a.tenMonHoc }}</b><br/><small>CTM #{{ a.chuongTrinhMonId }}</small></td><td>{{ a.tenChuDe || '—' }}</td><td><LmsBadge :value="a.trangThai" :map="TRANG_THAI_CAU_HOI_AP_DUNG" :color-fn="badgeClass" /></td><td>{{ a.ghiChu || '—' }}</td><td class="actions"><button class="act ok" @click="doiTrangThai(a,'ap_dung')">Duyệt</button><button class="act" @click="doiTrangThai(a,'can_ra_soat')">Rà soát</button><button class="act danger" @click="doiTrangThai(a,'khong_ap_dung')">Loại</button></td></tr>
      </tbody></table>
    </div>
  </section>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import LmsBadge from '../components/LmsBadge.vue'
import { cauHoiApDungService, lmsDanhMucService } from '../services/lmsService'
import { TRANG_THAI_CAU_HOI_AP_DUNG, badgeClass } from '../services/lmsEnum'
const dangTai=ref(false), items=ref([]), versions=ref([]), chuongTrinhMons=ref([])
const filters=ref({chuongTrinhVersionId:'', chuongTrinhMonId:'', trangThai:''})
const dsLoc=computed(()=>items.value.filter(x=>(!filters.value.chuongTrinhMonId||String(x.chuongTrinhMonId)===String(filters.value.chuongTrinhMonId))&&(!filters.value.trangThai||x.trangThai===filters.value.trangThai)))
async function loadCtm(){ chuongTrinhMons.value=await lmsDanhMucService.layMonHoc(filters.value.chuongTrinhVersionId); filters.value.chuongTrinhMonId='' }
async function taiDuLieu(){ dangTai.value=true; try{ items.value=(await cauHoiApDungService.layDanhSach()).content } finally{ dangTai.value=false } }
async function doiTrangThai(a,trangThai){ await cauHoiApDungService.capNhat(a.id,{ cauHoiVersionId:a.cauHoiVersionId, chuongTrinhMonId:a.chuongTrinhMonId, syllabusChuongBaiId:a.syllabusChuongBaiId||null, trangThai, ghiChu:a.ghiChu||''}); await taiDuLieu() }
onMounted(async()=>{ versions.value=await lmsDanhMucService.layVersion(); chuongTrinhMons.value=await lmsDanhMucService.layMonHoc(); await taiDuLieu() })
</script>
<style scoped>.lms-page{font-family:Roboto,Arial,sans-serif}.page-head{display:flex;justify-content:space-between;gap:16px;align-items:flex-start;margin-bottom:16px}.page-head h1{margin:0;font-size:24px;font-weight:800}.page-head p{margin:4px 0;color:#64748b}.filter-card,.table-card{background:#fff;border:1px solid #e2e8f0;border-radius:14px}.filter-card{display:grid;grid-template-columns:1fr 1fr 1fr auto;gap:10px;padding:14px;margin-bottom:14px}.fi{height:38px;border:1px solid #dbe3ef;border-radius:10px;padding:0 12px}.table-card{overflow:auto}table{width:100%;border-collapse:collapse}th{background:#f8fafc;text-align:left;font-size:12px;color:#475569;text-transform:uppercase;padding:12px}td{border-top:1px solid #eef2f7;padding:12px;font-size:13px;vertical-align:top}.line{display:-webkit-box;-webkit-line-clamp:2;-webkit-box-orient:vertical;overflow:hidden;max-width:420px}.empty{text-align:center;color:#94a3b8;padding:32px!important}.lms-btn,.act{border:1px solid #dbe3ef;border-radius:10px;background:#fff;padding:8px 12px;font-weight:700;color:#334155;text-decoration:none;cursor:pointer}.lms-btn.primary{background:#1d4ed8;color:#fff;border-color:#1d4ed8}.actions{white-space:nowrap}.act{padding:6px 9px;margin-right:4px}.act.ok{color:#15803d}.act.danger{color:#b91c1c}.badge-gray{background:#f1f5f9;color:#64748b}.badge-green{background:#dcfce7;color:#15803d}.badge-orange{background:#fff7ed;color:#c2410c}.badge-red{background:#fee2e2;color:#b91c1c}@media(max-width:900px){.filter-card{grid-template-columns:1fr}.page-head{flex-direction:column}}</style>
