<template>
  <section class="lms-page">
    <div class="page-head"><div><h1>Kho dữ liệu API LMS</h1><p>Màn kỹ thuật nhận hết CRUD API LMS mới: snapshot, rubric, bài nộp, lịch sử, nhật ký, ma trận...</p></div></div>
    <div class="workspace">
      <aside class="resource-list"><button v-for="(r,key) in resources" :key="key" :class="['res-btn',{active:key===resourceKey}]" @click="chonResource(key)">{{ r.label }}</button></aside>
      <main class="main-panel">
        <div class="toolbar"><h2>{{ current?.label }}</h2><div><button class="lms-btn" @click="taiDuLieu">Tải lại</button><button class="lms-btn primary" @click="taoMoi">+ Tạo JSON</button></div></div>
        <div class="table-card"><table><thead><tr><th v-for="h in headers" :key="h">{{ h }}</th><th></th></tr></thead><tbody><tr v-if="dangTai"><td :colspan="headers.length+1" class="empty">Đang tải...</td></tr><tr v-else-if="!items.length"><td :colspan="headers.length+1" class="empty">Không có dữ liệu.</td></tr><tr v-for="row in items" :key="row.id"><td v-for="h in headers" :key="h"><span class="cell-json">{{ hienThi(row[h]) }}</span></td><td class="actions"><button class="act" @click="sua(row)">Sửa</button><button class="act danger" @click="xoa(row)">Xóa</button></td></tr></tbody></table></div>
      </main>
    </div>
    <div v-if="showModal" class="modal"><div class="modal-card"><h3>{{ editId ? 'Sửa' : 'Tạo' }} {{ current?.label }}</h3><p>Nhập payload JSON đúng DTO request của BE.</p><textarea v-model="jsonText" class="json-area"></textarea><div class="modal-actions"><button class="lms-btn" @click="showModal=false">Hủy</button><button class="lms-btn primary" @click="luuJson">Lưu</button></div></div></div>
  </section>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { lmsCrudService } from '../services/lmsService'
const route=useRoute(), router=useRouter()
const resources=lmsCrudService.resources
const resourceKey=ref(route.query.resource || 'cauHoiVersion')
const items=ref([]), dangTai=ref(false), showModal=ref(false), jsonText=ref('{}'), editId=ref(null)
const current=computed(()=>resources[resourceKey.value])
const headers=computed(()=>{ const keys=new Set(['id']); items.value.slice(0,20).forEach(r=>Object.keys(r||{}).forEach(k=>{ if(!['createdAt','updatedAt'].includes(k)) keys.add(k) })); return Array.from(keys).slice(0,10) })
function hienThi(v){ if(v===null||v===undefined) return '—'; if(typeof v==='object') return JSON.stringify(v); return v }
async function taiDuLieu(){ dangTai.value=true; try{ items.value=(await lmsCrudService.list(resourceKey.value)).content } finally{ dangTai.value=false } }
function chonResource(k){ resourceKey.value=k; router.replace({query:{resource:k}}); taiDuLieu() }
function taoMoi(){ editId.value=null; jsonText.value='{}'; showModal.value=true }
function sua(row){ editId.value=row.id; jsonText.value=JSON.stringify(row,null,2); showModal.value=true }
async function xoa(row){ if(confirm('Xóa dòng ID '+row.id+'?')){ await lmsCrudService.delete(resourceKey.value,row.id); await taiDuLieu() } }
async function luuJson(){ try{ const payload=JSON.parse(jsonText.value); if(editId.value) await lmsCrudService.update(resourceKey.value,editId.value,payload); else await lmsCrudService.create(resourceKey.value,payload); showModal.value=false; await taiDuLieu() } catch(e){ alert('JSON/API lỗi: '+(e?.message||e)) } }
onMounted(taiDuLieu)
</script>
<style scoped>.lms-page{font-family:Roboto,Arial,sans-serif}.page-head{margin-bottom:16px}.page-head h1{margin:0;font-size:24px;font-weight:800}.page-head p{margin:4px 0;color:#64748b}.workspace{display:grid;grid-template-columns:260px 1fr;gap:16px}.resource-list{background:#fff;border:1px solid #e2e8f0;border-radius:14px;padding:8px;height:max-content;max-height:75vh;overflow:auto}.res-btn{width:100%;text-align:left;border:0;background:#fff;border-radius:10px;padding:9px 10px;font-weight:700;color:#475569;cursor:pointer}.res-btn.active,.res-btn:hover{background:#eff6ff;color:#1d4ed8}.main-panel,.table-card{min-width:0}.toolbar{display:flex;justify-content:space-between;align-items:center;background:#fff;border:1px solid #e2e8f0;border-radius:14px;padding:12px 14px;margin-bottom:12px}.toolbar h2{margin:0;font-size:18px}.table-card{background:#fff;border:1px solid #e2e8f0;border-radius:14px;overflow:auto}table{width:100%;border-collapse:collapse}th{background:#f8fafc;text-align:left;font-size:12px;text-transform:uppercase;color:#475569;padding:10px}td{border-top:1px solid #eef2f7;padding:10px;font-size:13px;vertical-align:top}.cell-json{display:block;max-width:220px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.empty{text-align:center;color:#94a3b8;padding:32px!important}.lms-btn,.act{border:1px solid #dbe3ef;border-radius:10px;background:#fff;padding:8px 12px;font-weight:700;color:#334155;text-decoration:none;cursor:pointer;margin-left:6px}.lms-btn.primary{background:#1d4ed8;color:#fff;border-color:#1d4ed8}.act{padding:5px 9px}.danger{color:#b91c1c}.modal{position:fixed;inset:0;background:rgba(15,23,42,.45);display:flex;align-items:center;justify-content:center;z-index:50}.modal-card{background:#fff;border-radius:16px;padding:18px;width:min(760px,92vw)}.json-area{width:100%;height:420px;border:1px solid #dbe3ef;border-radius:12px;padding:12px;font-family:ui-monospace,Menlo,monospace}.modal-actions{display:flex;justify-content:flex-end;gap:8px;margin-top:12px}@media(max-width:900px){.workspace{grid-template-columns:1fr}.resource-list{max-height:none}}</style>
