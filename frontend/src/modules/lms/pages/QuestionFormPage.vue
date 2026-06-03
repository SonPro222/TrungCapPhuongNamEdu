<template>
  <section class="lms-page">
    <div class="page-head">
      <button class="lms-btn" @click="$router.back()">← Quay lại</button>
      <div><h1>{{ isEdit ? 'Cập nhật version câu hỏi' : 'Soạn câu hỏi mới' }}</h1><p>Luồng đúng DB: câu hỏi gốc → version → đáp án/rubric → áp dụng chương trình môn.</p></div>
    </div>

    <div class="grid">
      <div class="main">
        <div class="card">
          <h3>1. Câu hỏi gốc</h3>
          <div class="form-grid">
            <label><span>Môn học gốc *</span><select v-model="form.monHocId" class="fi"><option value="">-- Chọn môn --</option><option v-for="m in monHocs" :key="m.id" :value="m.id">{{ m.maMonHoc }} - {{ m.tenMonHoc }}</option></select></label>
            <label><span>Mã câu hỏi</span><input v-model.trim="form.maCauHoi" class="fi" placeholder="VD: DCB-CH001" /></label>
            <label><span>Trạng thái gốc</span><select v-model="form.trangThaiGoc" class="fi"><option v-for="(label,key) in TRANG_THAI_CAU_HOI_GOC" :key="key" :value="key">{{ label }}</option></select></label>
          </div>
        </div>

        <div class="card">
          <h3>2. Nội dung version</h3>
          <label><span>Nội dung câu hỏi *</span><textarea v-model.trim="form.noiDung" class="fi area" rows="5" placeholder="Nhập nội dung câu hỏi"></textarea></label>
          <div class="form-grid mt">
            <label><span>Loại câu hỏi</span><select v-model="form.loaiCauHoi" class="fi" @change="resetDapAnTheoLoai"><option v-for="(label,key) in LOAI_CAU_HOI" :key="key" :value="key">{{ label }}</option></select></label>
            <label><span>Mức độ</span><select v-model="form.mucDo" class="fi"><option v-for="(label,key) in MUC_DO" :key="key" :value="key">{{ label }}</option></select></label>
            <label><span>Điểm mặc định</span><input v-model.number="form.diemMacDinh" type="number" min="0" step="0.25" class="fi" /></label>
            <label><span>Trạng thái version</span><select v-model="form.trangThai" class="fi"><option v-for="(label,key) in TRANG_THAI_CAU_HOI_VERSION" :key="key" :value="key">{{ label }}</option></select></label>
          </div>
          <label class="mt"><span>Giải thích đáp án / hướng dẫn chấm</span><textarea v-model.trim="form.giaiThichDapAn" class="fi area" rows="3"></textarea></label>
          <label v-if="isEdit" class="chk mt"><input type="checkbox" v-model="form.taoVersionMoi" /> Tạo version mới thay vì sửa đè version hiện tại</label>
        </div>

        <div class="card" v-if="form.loaiCauHoi !== 'tu_luan'">
          <div class="section-head"><h3>3. Đáp án theo version</h3><button class="lms-btn" @click="themDapAn">+ Đáp án</button></div>
          <div v-for="(d,idx) in form.dapAns" :key="idx" class="answer-row">
            <input v-model.trim="d.noiDung" class="fi" :placeholder="`Đáp án ${idx+1}`" />
            <label class="chk"><input type="checkbox" v-model="d.laDapAnDung" @change="chonDung(idx)" /> Đúng</label>
            <button class="lms-btn danger" @click="xoaDapAn(idx)" :disabled="form.dapAns.length<=2">Xóa</button>
          </div>
        </div>

        <div class="card" v-else>
          <div class="section-head"><h3>3. Rubric chấm tự luận</h3><button class="lms-btn" @click="themRubric">+ Tiêu chí</button></div>
          <div v-for="(r,idx) in form.rubrics" :key="idx" class="rubric-row">
            <input v-model.trim="r.tieuChi" class="fi" placeholder="Tiêu chí" />
            <input v-model.number="r.diemToiDa" class="fi small" type="number" min="0" step="0.25" placeholder="Điểm" />
            <input v-model.trim="r.moTa" class="fi" placeholder="Mô tả" />
            <button class="lms-btn danger" @click="form.rubrics.splice(idx,1)">Xóa</button>
          </div>
        </div>

        <div class="card">
          <h3>4. Áp dụng vào chương trình môn</h3>
          <div class="form-grid">
            <label><span>Version chương trình</span><select v-model="form.chuongTrinhVersionId" class="fi" @change="loadCtm"><option value="">-- Không chọn --</option><option v-for="v in versions" :key="v.id" :value="v.id">Version #{{ v.id }} {{ v.tenVersion || v.maVersion || '' }}</option></select></label>
            <label><span>Chương trình môn</span><select v-model="form.chuongTrinhMonId" class="fi" @change="loadChuDe"><option value="">-- Chưa áp dụng --</option><option v-for="m in chuongTrinhMons" :key="m.id" :value="m.id">{{ m.maMonHoc }} - {{ m.tenMonHoc }}</option></select></label>
            <label><span>Chương/bài syllabus</span><select v-model="form.syllabusChuongBaiId" class="fi"><option value="">-- Không gắn --</option><option v-for="c in chuDes" :key="c.id" :value="c.id">{{ c.tenChuDe }}</option></select></label>
            <label><span>Trạng thái áp dụng</span><select v-model="form.trangThaiApDung" class="fi"><option v-for="(label,key) in TRANG_THAI_CAU_HOI_AP_DUNG" :key="key" :value="key">{{ label }}</option></select></label>
          </div>
          <label class="mt"><span>Ghi chú áp dụng</span><input v-model.trim="form.ghiChuApDung" class="fi" /></label>
        </div>

        <div class="actions">
          <button class="lms-btn" @click="$router.back()">Hủy</button>
          <button class="lms-btn primary" :disabled="dangLuu" @click="luu">{{ dangLuu ? 'Đang lưu...' : 'Lưu câu hỏi' }}</button>
        </div>
      </div>

      <aside class="side card">
        <h3>Kiểm tra nghiệp vụ</h3>
        <ul>
          <li :class="{ok: form.monHocId}">Có môn học gốc</li>
          <li :class="{ok: form.noiDung}">Có nội dung version</li>
          <li :class="{ok: form.loaiCauHoi==='tu_luan' || soDapAnDungHopLe}">Đáp án hợp lệ theo loại</li>
          <li :class="{ok: !form.chuongTrinhMonId || form.trangThaiApDung}">Mapping áp dụng rõ trạng thái</li>
        </ul>
        <p v-if="itemCu?.isLocked" class="warn">Version này đã khóa vì đã dùng trong bài tập. Khi lưu nên tạo version mới.</p>
      </aside>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { nganHangCauHoiService, lmsDanhMucService } from '../services/lmsService'
import { LOAI_CAU_HOI, MUC_DO, TRANG_THAI_CAU_HOI_GOC, TRANG_THAI_CAU_HOI_VERSION, TRANG_THAI_CAU_HOI_AP_DUNG } from '../services/lmsEnum'

const route = useRoute(), router = useRouter()
const isEdit = computed(() => !!route.params.id)
const dangLuu = ref(false)
const itemCu = ref(null)
const monHocs = ref([]), versions = ref([]), chuongTrinhMons = ref([]), chuDes = ref([])
const form = ref({
  monHocId: '', maCauHoi: '', trangThaiGoc: 'dang_su_dung',
  noiDung: '', loaiCauHoi: 'trac_nghiem_1_dap_an', mucDo: 'trung_binh', diemMacDinh: 1, trangThai: 'nhap', giaiThichDapAn: '', taoVersionMoi: false,
  dapAns: [{ noiDung: '', laDapAnDung: true }, { noiDung: '', laDapAnDung: false }], rubrics: [{ tieuChi: 'Đúng nội dung', diemToiDa: 10, moTa: '' }],
  chuongTrinhVersionId: '', chuongTrinhMonId: '', syllabusChuongBaiId: '', trangThaiApDung: 'can_ra_soat', ghiChuApDung: '', nguoiTaoTaiKhoanId: 1,
})

const soDapAnDungHopLe = computed(() => {
  const ds = form.value.dapAns.filter(d => d.noiDung?.trim())
  const dung = ds.filter(d => d.laDapAnDung).length
  if (form.value.loaiCauHoi === 'trac_nghiem_1_dap_an' || form.value.loaiCauHoi === 'dung_sai') return ds.length >= 2 && dung === 1
  if (form.value.loaiCauHoi === 'trac_nghiem_nhieu_dap_an') return ds.length >= 2 && dung >= 1
  if (form.value.loaiCauHoi === 'dien_khuyet') return ds.length >= 1
  return true
})
function themDapAn(){ form.value.dapAns.push({ noiDung:'', laDapAnDung:false }) }
function xoaDapAn(i){ form.value.dapAns.splice(i,1) }
function chonDung(i){ if(['trac_nghiem_1_dap_an','dung_sai'].includes(form.value.loaiCauHoi) && form.value.dapAns[i].laDapAnDung) form.value.dapAns.forEach((d,idx)=>{ if(idx!==i) d.laDapAnDung=false }) }
function themRubric(){ form.value.rubrics.push({ tieuChi:'', diemToiDa:1, moTa:'' }) }
function resetDapAnTheoLoai(){ if(form.value.loaiCauHoi==='dung_sai') form.value.dapAns=[{noiDung:'Đúng',laDapAnDung:true},{noiDung:'Sai',laDapAnDung:false}] }
async function loadCtm(){ chuongTrinhMons.value = await lmsDanhMucService.layMonHoc(form.value.chuongTrinhVersionId); await loadChuDe() }
async function loadChuDe(){ chuDes.value = await lmsDanhMucService.layChuDe(form.value.chuongTrinhMonId) }
function validate(){ if(!form.value.monHocId) return 'Chưa chọn môn học gốc'; if(!form.value.noiDung) return 'Chưa nhập nội dung'; if(!soDapAnDungHopLe.value) return 'Đáp án chưa hợp lệ theo loại câu hỏi'; return '' }
async function luu(){ const err=validate(); if(err){alert(err);return} dangLuu.value=true; try{ if(isEdit.value) await nganHangCauHoiService.capNhat(route.params.id, form.value); else await nganHangCauHoiService.tao(form.value); router.push({name:'Lms.NganHangCauHoi'}) } finally{ dangLuu.value=false } }

onMounted(async()=>{
  ;[monHocs.value, versions.value] = await Promise.all([lmsDanhMucService.layMonHocGoc(), lmsDanhMucService.layVersion()])
  if(isEdit.value){
    const item = await nganHangCauHoiService.layTheoId(route.params.id); itemCu.value=item
    if(item){
      const app = item.apDungs?.[0] || {}
      form.value = { ...form.value, ...item, monHocId:item.monHocId, maCauHoi:item.maCauHoi, trangThaiGoc:item.trangThaiGoc||'dang_su_dung', cauHoiGocId:item.cauHoiGocId, dapAns:item.dapAns?.length?item.dapAns:form.value.dapAns, rubrics:item.rubrics?.length?item.rubrics:form.value.rubrics, chuongTrinhMonId:app.chuongTrinhMonId||'', syllabusChuongBaiId:app.syllabusChuongBaiId||'', trangThaiApDung:app.trangThai||'can_ra_soat', ghiChuApDung:app.ghiChu||'', taoVersionMoi:!!item.isLocked }
      await loadCtm(); await loadChuDe()
    }
  }
})
</script>

<style scoped>
.lms-page{font-family:Roboto,Arial,sans-serif}.page-head{display:flex;gap:12px;align-items:flex-start;margin-bottom:16px}.page-head h1{margin:0;font-size:24px;font-weight:800}.page-head p{margin:4px 0;color:#64748b}.grid{display:grid;grid-template-columns:minmax(0,1fr) 300px;gap:16px}.card{background:#fff;border:1px solid #e2e8f0;border-radius:14px;padding:16px;margin-bottom:14px}.card h3{margin:0 0 12px;font-size:16px}.form-grid{display:grid;grid-template-columns:repeat(3,1fr);gap:12px}label span{display:block;font-weight:700;font-size:12px;color:#475569;margin-bottom:6px}.fi{width:100%;min-height:38px;border:1px solid #dbe3ef;border-radius:10px;padding:8px 12px}.area{min-height:90px}.mt{margin-top:12px}.section-head{display:flex;justify-content:space-between;align-items:center}.answer-row,.rubric-row{display:grid;grid-template-columns:1fr auto auto;gap:10px;align-items:center;margin-bottom:8px}.rubric-row{grid-template-columns:1fr 100px 1fr auto}.chk{display:flex;align-items:center;gap:8px;color:#334155}.actions{display:flex;justify-content:flex-end;gap:8px;margin:16px 0}.lms-btn{display:inline-flex;align-items:center;gap:6px;border:1px solid #dbe3ef;border-radius:10px;background:#fff;padding:9px 14px;font-weight:700;color:#334155;cursor:pointer;text-decoration:none}.lms-btn.primary{background:#1d4ed8;color:#fff;border-color:#1d4ed8}.lms-btn.danger{color:#b91c1c}.side{height:max-content;position:sticky;top:16px}.side ul{padding-left:20px}.side li{margin:8px 0;color:#b91c1c}.side li.ok{color:#15803d}.warn{background:#fff7ed;color:#9a3412;padding:10px;border-radius:10px}@media(max-width:900px){.grid,.form-grid{grid-template-columns:1fr}.side{position:static}.answer-row,.rubric-row{grid-template-columns:1fr}}
</style>
