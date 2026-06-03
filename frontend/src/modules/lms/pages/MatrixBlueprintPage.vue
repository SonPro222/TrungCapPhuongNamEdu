<template>
  <section class="lms-page">
    <div class="page-head"><div><h1>Ma trận đề</h1><p>Cấu hình sinh đề tự động theo chương/bài, mức độ, loại câu hỏi.</p></div><button class="lms-btn primary" @click="taoMoi">+ Ma trận</button></div>
    <div class="grid">
      <div class="table-card"><table><thead><tr><th>Tên ma trận</th><th>Môn học</th><th>Tổng điểm</th><th>Thời gian</th><th>Trạng thái</th><th>Chi tiết</th><th></th></tr></thead><tbody><tr v-if="!items.length"><td colspan="7" class="empty">Chưa có ma trận đề.</td></tr><tr v-for="m in items" :key="m.id"><td><b>{{ m.tenMaTran }}</b><br/><small>ID {{ m.id }}</small></td><td>{{ m.tenMonHoc }}</td><td>{{ m.tongDiem }}</td><td>{{ m.thoiGianLamBai }} phút</td><td><LmsBadge :value="m.trangThai" :map="TRANG_THAI_MA_TRAN_DE" :color-fn="badgeClass" /></td><td><span v-for="d in m.chiTiets" :key="d.id" class="pill">{{ MUC_DO[d.mucDo] || d.mucDo }}: {{ d.soCau }} câu</span></td><td><button class="act" @click="sua(m)">Sửa</button><button class="act danger" @click="xoa(m)">Xóa</button></td></tr></tbody></table></div>
      <div v-if="showForm" class="card editor"><h3>{{ form.id ? 'Sửa ma trận' : 'Tạo ma trận' }}</h3><label><span>Chương trình môn *</span><select v-model="form.chuongTrinhMonId" class="fi" @change="loadChuDe"><option value="">-- Chọn môn --</option><option v-for="m in chuongTrinhMons" :key="m.id" :value="m.id">{{ m.maMonHoc }} - {{ m.tenMonHoc }}</option></select></label><label><span>Tên ma trận</span><input v-model="form.tenMaTran" class="fi" /></label><div class="form-grid"><label><span>Tổng điểm</span><input v-model.number="form.tongDiem" type="number" class="fi" /></label><label><span>Thời gian phút</span><input v-model.number="form.thoiGianLamBai" type="number" class="fi" /></label><label><span>Trạng thái</span><select v-model="form.trangThai" class="fi"><option v-for="(label,key) in TRANG_THAI_MA_TRAN_DE" :key="key" :value="key">{{ label }}</option></select></label></div><div class="section-head"><div><h4>Dòng ma trận</h4><p>Mỗi dòng là một điều kiện để hệ thống lấy câu hỏi khi sinh đề.</p></div><button class="lms-btn" @click="themDong">+ Dòng</button></div><div class="matrix-detail-table"><div class="detail-header"><span>Chương / bài</span><span>Mức độ</span><span>Loại câu hỏi</span><span>Số câu</span><span>Điểm / câu</span><span>Xóa</span></div><div v-for="(d,i) in form.chiTiets" :key="i" class="detail-row"><select v-model="d.syllabusChuongBaiId" class="fi" title="Chọn chương/bài hoặc Toàn môn"><option value="">Toàn môn</option><option v-for="c in chuDes" :key="c.id" :value="c.id">{{ c.tenChuDe }}</option></select><select v-model="d.mucDo" class="fi" title="Mức độ câu hỏi"><option v-for="(label,key) in MUC_DO" :key="key" :value="key">{{ label }}</option></select><select v-model="d.loaiCauHoi" class="fi" title="Loại câu hỏi"><option value="">Mọi loại</option><option v-for="(label,key) in LOAI_CAU_HOI" :key="key" :value="key">{{ label }}</option></select><input v-model.number="d.soCau" type="number" class="fi tiny" min="1" title="Số câu cần lấy" /><input v-model.number="d.diemMoiCau" type="number" step="0.25" class="fi tiny" title="Điểm của mỗi câu" /><button class="lms-btn danger" title="Xóa dòng" @click="form.chiTiets.splice(i,1)">x</button></div></div><p class="matrix-help">Ví dụ: Toàn môn | Trung bình | Mọi loại | 5 | 1 nghĩa là lấy 5 câu trung bình, mỗi câu 1 điểm.</p><div class="actions"><button class="lms-btn" @click="showForm=false">Hủy</button><button class="lms-btn primary" @click="luu">Lưu</button></div></div>
    </div>
  </section>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import LmsBadge from '../components/LmsBadge.vue'
import { maTranDeService, lmsDanhMucService } from '../services/lmsService'
import { TRANG_THAI_MA_TRAN_DE, MUC_DO, LOAI_CAU_HOI, badgeClass } from '../services/lmsEnum'
const items=ref([]), chuongTrinhMons=ref([]), chuDes=ref([]), showForm=ref(false)
const form=ref({ id:null, tenMaTran:'', chuongTrinhMonId:'', tongDiem:10, thoiGianLamBai:45, trangThai:'nhap', chiTiets:[] })
function taoMoi(){ form.value={ id:null, tenMaTran:'', chuongTrinhMonId:'', tongDiem:10, thoiGianLamBai:45, trangThai:'nhap', chiTiets:[{syllabusChuongBaiId:'',mucDo:'de',loaiCauHoi:'',soCau:5,diemMoiCau:1}]}; showForm.value=true }
async function sua(m){ form.value={...m, chiTiets:(m.chiTiets||[]).map(x=>({...x}))}; showForm.value=true; await loadChuDe() }
function themDong(){ form.value.chiTiets.push({syllabusChuongBaiId:'',mucDo:'trung_binh',loaiCauHoi:'',soCau:1,diemMoiCau:1}) }
async function loadChuDe(){ chuDes.value=await lmsDanhMucService.layChuDe(form.value.chuongTrinhMonId) }
async function tai(){ items.value=(await maTranDeService.layDanhSach()).content }
async function luu(){ if(!form.value.chuongTrinhMonId){alert('Chọn chương trình môn');return} if(form.value.id) await maTranDeService.capNhat(form.value.id,form.value); else await maTranDeService.tao(form.value); showForm.value=false; await tai() }
async function xoa(m){ if(confirm('Xóa ma trận này?')){ await maTranDeService.xoa(m.id); await tai() } }
onMounted(async()=>{ chuongTrinhMons.value=await lmsDanhMucService.layMonHoc(); await tai() })
</script>
<style scoped>
.lms-page {
  width: 100%;
  font-family: Roboto, Arial, sans-serif;
}

.page-head {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.page-head h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 800;
}

.page-head p {
  margin: 4px 0;
  color: #64748b;
}

.grid {
  width: 100%;
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(560px, 38vw);
  gap: 18px;
  align-items: start;
}

.table-card,
.card {
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 14px;
}

.table-card {
  width: 100%;
  overflow-x: auto;
}

.card {
  width: 100%;
  padding: 18px;
}

.editor {
  min-width: 0;
}

table {
  width: 100%;
  min-width: 980px;
  border-collapse: collapse;
}

th {
  background: #f8fafc;
  color: #475569;
  text-align: left;
  font-size: 12px;
  text-transform: uppercase;
  padding: 12px;
}

td {
  border-top: 1px solid #eef2f7;
  padding: 12px;
  font-size: 13px;
  vertical-align: top;
}

.empty {
  text-align: center;
  color: #94a3b8;
  padding: 32px !important;
}

.fi {
  width: 100%;
  min-height: 40px;
  border: 1px solid #dbe3ef;
  border-radius: 10px;
  padding: 8px 12px;
  margin-bottom: 10px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 10px;
}

.matrix-detail-table {
  width: 100%;
  overflow-x: auto;
  padding-bottom: 4px;
}

.detail-header,
.detail-row {
  display: grid;
  grid-template-columns: minmax(180px, 1.4fr) minmax(120px, .8fr) minmax(160px, 1fr) 88px 104px 48px;
  gap: 8px;
  align-items: center;
  min-width: 760px;
}

.detail-header {
  margin-bottom: 6px;
  color: #475569;
  font-size: 12px;
  font-weight: 800;
  text-transform: uppercase;
}

.detail-row .fi {
  margin-bottom: 8px;
}

.detail-row .tiny {
  text-align: center;
}

.matrix-help {
  margin: 6px 0 0;
  color: #64748b;
  font-size: 13px;
}

.section-head {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 12px;
  margin-top: 6px;
}

.section-head h4 {
  margin-bottom: 2px;
}

.section-head p {
  margin: 0;
  color: #64748b;
  font-size: 13px;
}

.lms-btn,
.act {
  border: 1px solid #dbe3ef;
  border-radius: 10px;
  background: #fff;
  padding: 8px 12px;
  font-weight: 700;
  color: #334155;
  text-decoration: none;
  cursor: pointer;
}

.lms-btn.primary {
  background: #1d4ed8;
  color: #fff;
  border-color: #1d4ed8;
}

.danger {
  color: #b91c1c;
}

.act {
  padding: 6px 10px;
  margin-right: 4px;
}

.pill {
  display: inline-block;
  background: #eff6ff;
  color: #1d4ed8;
  padding: 3px 8px;
  border-radius: 999px;
  margin: 2px;
  font-size: 12px;
}

.actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 12px;
}

.badge-gray { background: #f1f5f9; color: #64748b; }
.badge-green { background: #dcfce7; color: #15803d; }
.badge-orange { background: #fff7ed; color: #c2410c; }
.badge-red { background: #fee2e2; color: #b91c1c; }

@media (max-width: 1400px) {
  .grid {
    grid-template-columns: 1fr;
  }

  table {
    min-width: 900px;
  }
}

@media (max-width: 900px) {
  .page-head {
    flex-direction: column;
    gap: 10px;
  }

  .detail-row,
  .form-grid {
    grid-template-columns: 1fr;
  }

  table {
    min-width: 760px;
  }
}
</style>
