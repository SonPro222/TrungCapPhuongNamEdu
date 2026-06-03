<template>
  <section class="lms-page">
    <div class="page-head"><div><h1>Lịch sử LMS</h1><p>Gom lịch sử câu hỏi, bài tập lớp, chỉnh điểm, đẩy điểm và nhật ký làm bài.</p></div><button class="lms-btn" @click="taiDuLieu">Tải lại</button></div>
    <div class="filter-card"><input v-model.trim="kw" class="fi" placeholder="Tìm hành động, ghi chú, loại log..."/><select v-model="loai" class="fi"><option value="">Tất cả loại log</option><option v-for="l in loaiLogs" :key="l" :value="l">{{ l }}</option></select></div>
    <div class="table-card"><table><thead><tr><th>Thời gian</th><th>Loại</th><th>Hành động</th><th>Người/Đối tượng</th><th>Nội dung cũ</th><th>Nội dung mới</th><th>Ghi chú</th></tr></thead><tbody><tr v-if="dangTai"><td colspan="7" class="empty">Đang tải...</td></tr><tr v-else-if="!dsLoc.length"><td colspan="7" class="empty">Không có lịch sử.</td></tr><tr v-for="(it,idx) in dsLoc" :key="`${it.loaiLog}-${it.id}-${idx}`"><td>{{ formatDateTime(it.thoiGianHienThi || it.thoiGian || it.createdAt) }}</td><td><span class="pill">{{ it.loaiLog }}</span></td><td><b>{{ it.hanhDong || it.noiDung || '—' }}</b></td><td>{{ it.nguoiThucHienId || it.lanLamBaiId || it.cauHoiVersionId || it.baiTapLopId || '—' }}</td><td class="json">{{ show(it.noiDungCu || it.giaTriCu) }}</td><td class="json">{{ show(it.noiDungMoi || it.giaTriMoi || it.duLieu) }}</td><td>{{ it.ghiChu || it.nhanXet || '—' }}</td></tr></tbody></table></div>
  </section>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import { lichSuLmsService } from '../services/lmsService'
import { formatDateTime } from '../services/lmsEnum'
const dangTai=ref(false), items=ref([]), kw=ref(''), loai=ref('')
const loaiLogs=computed(()=>Array.from(new Set(items.value.map(x=>x.loaiLog).filter(Boolean))))
const dsLoc=computed(()=>{ const k=kw.value.toLowerCase(); return items.value.filter(x=>(!loai.value||x.loaiLog===loai.value)&&(!k||JSON.stringify(x).toLowerCase().includes(k))) })
function show(v){ if(!v) return '—'; if(typeof v==='object') return JSON.stringify(v); return v }
async function taiDuLieu(){ dangTai.value=true; try{ items.value=(await lichSuLmsService.layDanhSach()).content } finally{ dangTai.value=false } }
onMounted(taiDuLieu)
</script>
<style scoped>.lms-page{font-family:Roboto,Arial,sans-serif}.page-head{display:flex;justify-content:space-between;align-items:flex-start;margin-bottom:16px}.page-head h1{margin:0;font-size:24px;font-weight:800}.page-head p{margin:4px 0;color:#64748b}.filter-card,.table-card{background:#fff;border:1px solid #e2e8f0;border-radius:14px}.filter-card{display:grid;grid-template-columns:1fr 240px;gap:10px;padding:14px;margin-bottom:14px}.fi{height:38px;border:1px solid #dbe3ef;border-radius:10px;padding:0 12px}.table-card{overflow:auto}table{width:100%;border-collapse:collapse}th{background:#f8fafc;text-align:left;font-size:12px;text-transform:uppercase;color:#475569;padding:12px}td{border-top:1px solid #eef2f7;padding:10px;font-size:13px;vertical-align:top}.empty{text-align:center;color:#94a3b8;padding:32px!important}.json{max-width:260px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;color:#64748b}.pill{background:#eff6ff;color:#1d4ed8;border-radius:999px;padding:3px 9px;font-size:12px;font-weight:700}.lms-btn{border:1px solid #dbe3ef;border-radius:10px;background:#fff;padding:8px 12px;font-weight:700;color:#334155;cursor:pointer}@media(max-width:800px){.filter-card{grid-template-columns:1fr}.page-head{flex-direction:column}}</style>
