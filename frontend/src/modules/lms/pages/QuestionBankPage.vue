<template>
  <section class="lms-page">
    <div class="page-head">
      <div>
        <h1>Ngân hàng câu hỏi version</h1>
        <p>Câu hỏi được quản lý theo câu hỏi gốc → version → áp dụng cho chương trình môn.</p>
      </div>
      <div class="head-actions">
        <RouterLink :to="{ name: 'Lms.RaSoatCauHoi' }" class="lms-btn">Rà soát áp dụng</RouterLink>
        <RouterLink :to="{ name: 'Lms.NganHangCauHoiThem' }" class="lms-btn primary">+ Soạn câu hỏi</RouterLink>
      </div>
    </div>

    <div class="filter-card">
      <input v-model.trim="filters.keyword" class="fi" placeholder="Tìm nội dung, mã câu hỏi, môn học..." />
      <select v-model="filters.monHocId" class="fi"><option value="">Tất cả môn</option><option v-for="m in monHocs" :key="m.id" :value="m.id">{{ m.maMonHoc }} - {{ m.tenMonHoc }}</option></select>
      <select v-model="filters.mucDo" class="fi"><option value="">Tất cả mức độ</option><option v-for="(label,key) in MUC_DO" :key="key" :value="key">{{ label }}</option></select>
      <select v-model="filters.trangThai" class="fi"><option value="">Tất cả trạng thái</option><option v-for="(label,key) in TRANG_THAI_CAU_HOI_VERSION" :key="key" :value="key">{{ label }}</option></select>
      <button class="lms-btn" @click="taiDuLieu">Tải lại</button>
    </div>

    <div class="stat-row">
      <div class="stat"><b>{{ dsLoc.length }}</b><span>Version câu hỏi</span></div>
      <div class="stat"><b>{{ dsLoc.filter(x => x.trangThai === 'dang_su_dung').length }}</b><span>Đang sử dụng</span></div>
      <div class="stat"><b>{{ dsLoc.filter(x => x.isLocked).length }}</b><span>Đã khóa</span></div>
      <div class="stat"><b>{{ dsLoc.reduce((s,x)=>s+(x.soVersionApDung||0),0) }}</b><span>Lượt áp dụng CTM</span></div>
    </div>

    <div class="table-card">
      <table>
        <thead><tr><th>Mã / Version</th><th>Nội dung</th><th>Môn học</th><th>Loại</th><th>Mức độ</th><th>Áp dụng</th><th>Trạng thái</th><th></th></tr></thead>
        <tbody>
          <tr v-if="dangTai"><td colspan="8" class="empty">Đang tải...</td></tr>
          <tr v-else-if="!dsLoc.length"><td colspan="8" class="empty">Chưa có câu hỏi version trong DB mới.</td></tr>
          <tr v-for="q in dsLoc" :key="q.id">
            <td><b>{{ q.maCauHoi }}</b><br/><small>Version {{ q.versionNo || 1 }} · ID {{ q.id }}</small></td>
            <td class="content-cell"><div class="line-clamp">{{ q.noiDung }}</div><small>{{ q.dapAns?.length || 0 }} đáp án · {{ q.rubrics?.length || 0 }} rubric</small></td>
            <td><b>{{ q.tenMonHoc }}</b><br/><small>{{ q.maMonHoc }}</small></td>
            <td>{{ LOAI_CAU_HOI[q.loaiCauHoi] || q.loaiCauHoi }}</td>
            <td><LmsBadge :value="q.mucDo" :map="MUC_DO" :color-fn="mauMucDo" /></td>
            <td><b>{{ q.soVersionApDung || 0 }}</b><br/><small>{{ q.apDungs?.filter(a=>a.trangThai==='ap_dung').length || 0 }} đã duyệt</small></td>
            <td><LmsBadge :value="q.trangThai" :map="TRANG_THAI_CAU_HOI_VERSION" :color-fn="badgeClass" /><br/><small v-if="q.isLocked">🔒 đã dùng</small></td>
            <td class="actions">
              <RouterLink :to="{ name: 'Lms.NganHangCauHoiSua', params: { id: q.id } }" class="act">Sửa</RouterLink>
              <button class="act danger" @click="xoa(q)" :disabled="q.isLocked">Xóa</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import LmsBadge from '../components/LmsBadge.vue'
import { nganHangCauHoiService, lmsDanhMucService } from '../services/lmsService'
import { LOAI_CAU_HOI, MUC_DO, TRANG_THAI_CAU_HOI_VERSION, mauMucDo, badgeClass } from '../services/lmsEnum'

const dangTai = ref(false)
const items = ref([])
const monHocs = ref([])
const filters = ref({ keyword: '', monHocId: '', mucDo: '', trangThai: '' })

const dsLoc = computed(() => {
  const kw = filters.value.keyword.toLowerCase()
  return items.value.filter(q => {
    const text = `${q.maCauHoi} ${q.noiDung} ${q.tenMonHoc} ${q.maMonHoc}`.toLowerCase()
    return (!kw || text.includes(kw))
      && (!filters.value.monHocId || String(q.monHocId) === String(filters.value.monHocId))
      && (!filters.value.mucDo || q.mucDo === filters.value.mucDo)
      && (!filters.value.trangThai || q.trangThai === filters.value.trangThai)
  })
})

async function taiDuLieu() {
  dangTai.value = true
  try { items.value = (await nganHangCauHoiService.layDanhSach()).content }
  finally { dangTai.value = false }
}

async function xoa(q) {
  if (!confirm(`Xóa version câu hỏi ${q.maCauHoi}?`)) return
  await nganHangCauHoiService.xoa(q.id)
  await taiDuLieu()
}

onMounted(async () => {
  monHocs.value = await lmsDanhMucService.layMonHocmau()
  await taiDuLieu()
})
</script>

<style scoped>
.lms-page{font-family:Roboto,Arial,sans-serif}.page-head{display:flex;justify-content:space-between;gap:16px;align-items:flex-start;margin-bottom:16px}.page-head h1{margin:0;font-size:24px;font-weight:800;color:#0f172a}.page-head p{margin:4px 0 0;color:#64748b}.head-actions{display:flex;gap:8px;flex-wrap:wrap}.filter-card,.table-card,.stat{background:white;border:1px solid #e2e8f0;border-radius:14px;box-shadow:0 1px 4px rgba(15,23,42,.04)}.filter-card{display:grid;grid-template-columns:2fr 1fr 1fr 1fr auto;gap:10px;padding:14px;margin-bottom:14px}.fi{height:38px;border:1px solid #dbe3ef;border-radius:10px;padding:0 12px;background:#fff}.stat-row{display:grid;grid-template-columns:repeat(4,1fr);gap:12px;margin-bottom:14px}.stat{padding:16px}.stat b{font-size:24px;color:#1d4ed8}.stat span{display:block;color:#64748b;font-size:13px;margin-top:4px}.table-card{overflow:auto}table{width:100%;border-collapse:collapse}th{background:#f8fafc;color:#475569;text-align:left;font-size:12px;text-transform:uppercase;padding:12px}td{border-top:1px solid #eef2f7;padding:12px;vertical-align:top;color:#334155;font-size:13px}.content-cell{max-width:420px}.line-clamp{display:-webkit-box;-webkit-line-clamp:2;-webkit-box-orient:vertical;overflow:hidden}small{color:#64748b}.empty{text-align:center;padding:32px!important;color:#94a3b8}.actions{white-space:nowrap}.lms-btn,.act{display:inline-flex;align-items:center;gap:6px;border:1px solid #dbe3ef;border-radius:10px;background:#fff;padding:8px 12px;font-weight:700;font-size:13px;text-decoration:none;color:#334155;cursor:pointer}.lms-btn.primary{background:#1d4ed8;color:#fff;border-color:#1d4ed8}.act{padding:6px 10px;margin-right:4px}.act.danger{color:#b91c1c}.act:disabled{opacity:.45;cursor:not-allowed}.badge-gray{background:#f1f5f9;color:#64748b}.badge-green{background:#dcfce7;color:#15803d}.badge-blue{background:#dbeafe;color:#1d4ed8}.badge-indigo{background:#e0e7ff;color:#4338ca}.badge-purple{background:#f3e8ff;color:#7c3aed}.badge-orange{background:#fff7ed;color:#c2410c}.badge-red{background:#fee2e2;color:#b91c1c}@media(max-width:900px){.filter-card,.stat-row{grid-template-columns:1fr}.page-head{flex-direction:column}}
</style>
