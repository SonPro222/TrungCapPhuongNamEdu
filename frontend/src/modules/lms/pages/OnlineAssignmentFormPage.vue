<template>
  <section class="lms-page">
    <div class="page-head"><button class="lms-btn" @click="$router.back()">← Quay lại</button><div><h1>{{ isEdit ? 'Sửa bài tập online' : 'Tạo bài tập online' }}</h1><p>Chỉ chọn câu hỏi version đã được áp dụng trạng thái “Áp dụng” cho chương trình môn.</p></div></div>
    <div class="grid">
      <div class="main">
        <div class="card"><h3>1. Thông tin bài tập</h3><div class="form-grid"><label><span>Chương trình môn *</span><select v-model="form.chuongTrinhMonId" class="fi" @change="loadNguon"><option value="">-- Chọn môn --</option><option v-for="m in chuongTrinhMons" :key="m.id" :value="m.id">{{ m.maMonHoc }} - {{ m.tenMonHoc }}</option></select></label><label><span>Loại bài</span><select v-model="form.loaiBai" class="fi"><option v-for="(label,key) in LOAI_BAI" :key="key" :value="key">{{ label }}</option></select></label><label><span>Trạng thái</span><select v-model="form.trangThai" class="fi"><option v-for="(label,key) in TRANG_THAI_BAI_TAP_ONLINE" :key="key" :value="key">{{ label }}</option></select></label></div><label><span>Tiêu đề *</span><input v-model.trim="form.tieuDe" class="fi" placeholder="VD: Quiz chương 1" /></label><label><span>Mô tả</span><textarea v-model.trim="form.moTa" class="fi area" rows="3"></textarea></label><div class="form-grid"><label><span>Tổng điểm</span><input v-model.number="form.tongDiem" type="number" class="fi" /></label><label class="chk"><input type="checkbox" v-model="form.tronCauHoi"/> Trộn câu hỏi</label><label class="chk"><input type="checkbox" v-model="form.tronDapAn"/> Trộn đáp án</label><label class="chk"><input type="checkbox" v-model="form.hienDapAnSauKhiNop"/> Hiện đáp án sau nộp</label><label class="chk"><input type="checkbox" v-model="form.hienGiaiThichSauKhiNop"/> Hiện giải thích</label></div></div>
        <div class="card"><h3>2. Nguồn câu hỏi</h3><label class="chk"><input type="checkbox" v-model="form.sinhDeTuDong"/> Sinh đề tự động bằng ma trận</label><div v-if="form.sinhDeTuDong" class="mt"><label><span>Ma trận đề</span><select v-model="form.maTranDeId" class="fi"><option value="">-- Chọn ma trận --</option><option v-for="m in maTrans" :key="m.id" :value="m.id">{{ m.tenMaTran }} - {{ m.tenMonHoc }}</option></select></label></div><div v-else><div class="question-toolbar"><input v-model.trim="kwCauHoi" class="fi" placeholder="Tìm câu hỏi đã duyệt áp dụng..."/><span>{{ cauHoiDaChon.length }} câu đã chọn</span></div><div class="question-list"><label v-for="q in cauHoiLoc" :key="q.cauHoiVersionId" class="q-row"><input type="checkbox" :checked="daChon(q)" @change="toggleCauHoi(q)"/><div><b>{{ q.maCauHoi }}</b> · {{ q.noiDung }}<br/><small>{{ q.tenMonHoc }} · {{ q.mucDo }} · {{ q.loaiCauHoi }}</small></div><input v-if="daChon(q)" v-model.number="rowDaChon(q).diem" type="number" class="fi diem" step="0.25" /></label></div></div></div>
        <div class="actions"><button class="lms-btn" @click="$router.back()">Hủy</button><button class="lms-btn primary" :disabled="dangLuu" @click="luu">{{ dangLuu?'Đang lưu...':'Lưu bài tập' }}</button></div>
      </div>
      <aside class="card side"><h3>Tóm tắt</h3><p><b>{{ form.tieuDe || 'Chưa có tiêu đề' }}</b></p><p>{{ form.sinhDeTuDong ? 'Sinh đề tự động' : `${cauHoiDaChon.length} câu hỏi thủ công` }}</p><p>Tổng điểm: {{ form.tongDiem }}</p><p>Trạng thái: {{ TRANG_THAI_BAI_TAP_ONLINE[form.trangThai] }}</p></aside>
    </div>
  </section>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { baiTapOnlineService, cauHoiApDungService, maTranDeService, lmsDanhMucService } from '../services/lmsService'
import { LOAI_BAI, TRANG_THAI_BAI_TAP_ONLINE } from '../services/lmsEnum'
const route=useRoute(), router=useRouter(); const isEdit=computed(()=>!!route.params.id)
const dangLuu=ref(false), chuongTrinhMons=ref([]), cauHois=ref([]), maTrans=ref([]), kwCauHoi=ref('')
const form=ref({ chuongTrinhMonId:'', tieuDe:'', moTa:'', loaiBai:'quiz', tongDiem:10, trangThai:'nhap', tronCauHoi:true, tronDapAn:true, sinhDeTuDong:false, hienDapAnSauKhiNop:false, hienGiaiThichSauKhiNop:false, maTranDeId:'', nguoiTaoTaiKhoanId:1 })
const cauHoiDaChon=ref([])
const cauHoiLoc=computed(()=>{ const kw=kwCauHoi.value.toLowerCase(); return cauHois.value.filter(q=>(!kw||`${q.maCauHoi} ${q.noiDung} ${q.tenMonHoc}`.toLowerCase().includes(kw))) })
function daChon(q){ return cauHoiDaChon.value.some(x=>String(x.cauHoiVersionId)===String(q.cauHoiVersionId)) }
function rowDaChon(q){ return cauHoiDaChon.value.find(x=>String(x.cauHoiVersionId)===String(q.cauHoiVersionId)) || {} }
function toggleCauHoi(q){ if(daChon(q)) cauHoiDaChon.value=cauHoiDaChon.value.filter(x=>String(x.cauHoiVersionId)!==String(q.cauHoiVersionId)); else cauHoiDaChon.value.push({ cauHoiVersionId:q.cauHoiVersionId, id:q.cauHoiVersionId, diem:q.cauHoi?.diemMacDinh||1, thuTu:cauHoiDaChon.value.length+1, batBuoc:true }) }
async function loadNguon(){ cauHois.value=(await cauHoiApDungService.layDanhSach({chuongTrinhMonId:form.value.chuongTrinhMonId,trangThai:'ap_dung'})).content.filter(x=>x.trangThai==='ap_dung'); maTrans.value=(await maTranDeService.layDanhSach()).content.filter(x=>!form.value.chuongTrinhMonId||String(x.chuongTrinhMonId)===String(form.value.chuongTrinhMonId)) }
function sinhCauHoiTheoMaTran(){
  const mt = maTrans.value.find(x => String(x.id) === String(form.value.maTranDeId))
  if (!mt) return []
  const selected = []
  ;(mt.chiTiets || []).forEach(d => {
    const pool = cauHois.value.filter(q => {
      const cq = q.cauHoi || q
      return (!d.mucDo || cq.mucDo === d.mucDo)
        && (!d.loaiCauHoi || cq.loaiCauHoi === d.loaiCauHoi)
        && (!d.syllabusChuongBaiId || String(q.syllabusChuongBaiId) === String(d.syllabusChuongBaiId))
        && !selected.some(s => String(s.cauHoiVersionId) === String(q.cauHoiVersionId))
    })
    pool.slice(0, Number(d.soCau || 0)).forEach(q => selected.push({ cauHoiVersionId: q.cauHoiVersionId, id: q.cauHoiVersionId, diem: Number(d.diemMoiCau || q.cauHoi?.diemMacDinh || 1), batBuoc: true }))
  })
  return selected
}
async function luu(){
  if(!form.value.chuongTrinhMonId||!form.value.tieuDe){alert('Nhập đủ chương trình môn và tiêu đề');return}
  let rows = [...cauHoiDaChon.value]
  if(form.value.sinhDeTuDong){
    if(!form.value.maTranDeId){alert('Chọn ma trận đề');return}
    rows = sinhCauHoiTheoMaTran()
    if(!rows.length){alert('Ma trận chưa sinh được câu hỏi. Kiểm tra câu hỏi áp dụng, mức độ, loại câu hỏi và chủ đề.');return}
  }
  if(!rows.length){alert('Chọn ít nhất 1 câu hỏi đã áp dụng');return}
  dangLuu.value=true
  try{
    const payload={...form.value,cauHoiDaChon:rows.map((x,i)=>({...x,thuTu:i+1}))}
    if(isEdit.value) await baiTapOnlineService.capNhat(route.params.id,payload); else await baiTapOnlineService.tao(payload)
    router.push({name:'Lms.BaiTapOnline'})
  } finally{ dangLuu.value=false }
}
onMounted(async()=>{ chuongTrinhMons.value=await lmsDanhMucService.layMonHoc(); if(isEdit.value){ const b=await baiTapOnlineService.layTheoId(route.params.id); if(b){ form.value={...form.value,...b}; cauHoiDaChon.value=(b.cauHoiRows||[]).map(x=>({cauHoiVersionId:x.cauHoiVersionId,id:x.cauHoiVersionId,diem:x.diem,thuTu:x.thuTu,batBuoc:x.batBuoc})); await loadNguon() } } })
</script>
<style scoped>.lms-page{font-family:Roboto,Arial,sans-serif}.page-head{display:flex;gap:12px;align-items:flex-start;margin-bottom:16px}.page-head h1{margin:0;font-size:24px;font-weight:800}.page-head p{margin:4px 0;color:#64748b}.grid{display:grid;grid-template-columns:minmax(0,1fr) 280px;gap:16px}.card{background:#fff;border:1px solid #e2e8f0;border-radius:14px;padding:16px;margin-bottom:14px}.form-grid{display:grid;grid-template-columns:repeat(3,1fr);gap:12px}.fi{width:100%;min-height:38px;border:1px solid #dbe3ef;border-radius:10px;padding:8px 12px;margin-bottom:10px}.area{min-height:80px}.chk{display:flex;align-items:center;gap:8px;font-weight:700;color:#475569}.mt{margin-top:10px}.question-toolbar{display:flex;justify-content:space-between;gap:10px;align-items:center;margin-top:10px}.question-list{max-height:420px;overflow:auto;border:1px solid #e2e8f0;border-radius:12px}.q-row{display:grid;grid-template-columns:auto 1fr 90px;gap:10px;padding:10px;border-bottom:1px solid #eef2f7;align-items:center}.q-row:last-child{border-bottom:0}.diem{margin:0}.actions{display:flex;justify-content:flex-end;gap:8px}.lms-btn{border:1px solid #dbe3ef;border-radius:10px;background:#fff;padding:9px 14px;font-weight:700;color:#334155;cursor:pointer;text-decoration:none}.lms-btn.primary{background:#1d4ed8;color:#fff;border-color:#1d4ed8}.side{height:max-content;position:sticky;top:16px}@media(max-width:1000px){.grid,.form-grid{grid-template-columns:1fr}.side{position:static}.q-row{grid-template-columns:auto 1fr}}</style>
