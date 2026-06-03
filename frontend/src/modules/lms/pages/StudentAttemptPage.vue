<template>
  <section class="lms-page">
    <div class="page-head"><button class="lms-btn" @click="$router.back()">← Quay lại</button><div><h1>Làm bài online</h1><p>Trả lời theo snapshot câu hỏi/đáp án của lần làm bài.</p></div></div>
    <div v-if="dangTai" class="card empty">Đang tải bài làm...</div>
    <div v-else-if="!lanLam" class="card empty">Không tìm thấy lần làm bài.</div>
    <div v-else class="grid"><main><div v-for="q in lanLam.cauHois" :key="q.id" class="card question"><h3>Câu {{ q.thuTu }} <small>({{ LOAI_CAU_HOI[q.loaiCauHoiSnapshot] || q.loaiCauHoiSnapshot }} · {{ q.diemSnapshot }} điểm)</small></h3><p class="noi-dung">{{ q.noiDungSnapshot }}</p><div v-if="q.loaiCauHoiSnapshot==='tu_luan' || q.loaiCauHoiSnapshot==='dien_khuyet'"><textarea v-model="answers[q.id].noiDungTraLoi" class="fi area" rows="4" placeholder="Nhập câu trả lời"></textarea></div><div v-else class="options"><label v-for="a in q.dapAns" :key="a.id" class="opt"><input :type="q.loaiCauHoiSnapshot==='trac_nghiem_nhieu_dap_an'?'checkbox':'radio'" :name="`q_${q.id}`" :checked="answers[q.id].lanLamBaiDapAnIds.includes(a.id)" @change="chon(q,a,$event)"/> {{ a.noiDungSnapshot }}</label></div></div></main><aside class="card side"><h3>Tóm tắt</h3><p>Lần làm: {{ lanLam.lanThu }}</p><p>Số câu: {{ lanLam.cauHois.length }}</p><button class="lms-btn primary block" @click="nopBai">Nộp bài</button></aside></div>
  </section>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { sinhVienLmsService } from '../services/lmsService'
import { LOAI_CAU_HOI } from '../services/lmsEnum'
const route=useRoute(),router=useRouter(); const dangTai=ref(false), lanLam=ref(null), answers=ref({})
function initAnswers(){ const obj={}; (lanLam.value?.cauHois||[]).forEach(q=>{ obj[q.id]={lanLamBaiCauHoiId:q.id, noiDungTraLoi:q.cauTraLoi?.noiDungTraLoi||'', lanLamBaiDapAnIds:[...(q.dapAnDaChonIds||[])]} }); answers.value=obj }
function chon(q,a,e){ const arr=answers.value[q.id].lanLamBaiDapAnIds; if(q.loaiCauHoiSnapshot==='trac_nghiem_nhieu_dap_an'){ if(e.target.checked&&!arr.includes(a.id)) arr.push(a.id); if(!e.target.checked) answers.value[q.id].lanLamBaiDapAnIds=arr.filter(x=>x!==a.id) } else answers.value[q.id].lanLamBaiDapAnIds=[a.id] }
async function nopBai(){ if(!confirm('Nộp bài?')) return; const cauTraLois=Object.values(answers.value); const res=await sinhVienLmsService.nopBai(route.params.id,{cauTraLois}); router.push({name:'Lms.KetQuaSinhVien',params:{id:res.baiTapLopId}}) }
onMounted(async()=>{ dangTai.value=true; try{ lanLam.value=await sinhVienLmsService.getLanLamBai(route.params.id); initAnswers() } finally{ dangTai.value=false } })
</script>
<style scoped>.lms-page{font-family:Roboto,Arial,sans-serif}.page-head{display:flex;gap:12px;align-items:flex-start;margin-bottom:16px}.page-head h1{margin:0;font-size:24px;font-weight:800}.page-head p{margin:4px 0;color:#64748b}.grid{display:grid;grid-template-columns:minmax(0,1fr) 260px;gap:16px}.card{background:#fff;border:1px solid #e2e8f0;border-radius:14px;padding:16px;margin-bottom:14px}.question h3{margin:0 0 8px}.noi-dung{white-space:pre-wrap}.fi{width:100%;border:1px solid #dbe3ef;border-radius:10px;padding:10px}.area{min-height:120px}.opt{display:block;border:1px solid #e2e8f0;border-radius:10px;padding:10px;margin-bottom:8px}.side{height:max-content;position:sticky;top:16px}.lms-btn{border:1px solid #dbe3ef;border-radius:10px;background:#fff;padding:9px 14px;font-weight:700;color:#334155;cursor:pointer;text-decoration:none}.lms-btn.primary{background:#1d4ed8;color:#fff;border-color:#1d4ed8}.block{width:100%;justify-content:center}.empty{text-align:center;color:#94a3b8}@media(max-width:900px){.grid{grid-template-columns:1fr}.side{position:static}}</style>
