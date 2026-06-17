<template>
  <section class="lms-page">
    <div class="lp-head">
      <button class="lms-btn ghost" @click="$router.back()">← Quay lại</button>
      <h1 class="lp-h1">Giao bài cho lớp học phần</h1>
    </div>

    <div class="two-col">
      <div class="main-col">
        <!-- THÔNG TIN BÀI TẬP -->
        <div class="form-section">
          <div class="form-section-title">📄 Chọn bài tập online</div>
          <label class="fl required">
            <span>Bài tập online</span>
            <select v-model="form.baiTapOnlineId" class="fi" :class="{ error: errs.baiTapOnlineId }" @change="khiChonBaiTap">
              <option value="">-- Chọn bài tập --</option>
              <option v-for="bt in baiTapList" :key="bt.id" :value="bt.id">{{ bt.tenBaiTap }} ({{ bt.tenMonHoc }})</option>
            </select>
            <span class="err-msg" v-if="errs.baiTapOnlineId">Bắt buộc chọn bài tập</span>
          </label>
          <div v-if="baiTapChon" class="info-card mt-10">
            <div class="info-row"><span>Môn học:</span><b>{{ baiTapChon.tenMonHoc }}</b></div>
            <div class="info-row"><span>Loại bài:</span><b>{{ LOAI_BAI[baiTapChon.loaiBai] }}</b></div>
            <div class="info-row"><span>Số câu hỏi:</span><b>{{ baiTapChon.soCauHoi }} câu</b></div>
            <div class="info-row"><span>Tổng điểm:</span><b>{{ baiTapChon.tongDiem }} điểm</b></div>
          </div>
        </div>

        <!-- THÔNG TIN LỚP -->
        <div class="form-section">
          <div class="form-section-title">🏫 Lớp học phần</div>
          <label class="fl required">
            <span>Lớp học phần</span>
            <select v-model="form.lopHocPhanId" class="fi" :class="{ error: errs.lopHocPhanId }" @change="khiChonLop">
              <option value="">-- Chọn lớp học phần --</option>
              <option v-for="lhp in lopList" :key="lhp.id" :value="lhp.id">{{ lhp.maLop }} – {{ lhp.tenLop }}</option>
            </select>
            <span class="err-msg" v-if="errs.lopHocPhanId">Bắt buộc chọn lớp</span>
          </label>
          <div v-if="lopChon" class="info-card mt-10">
            <div class="info-row"><span>Giảng viên:</span><b>{{ lopChon.giaoVien }}</b></div>
            <div class="info-row"><span>Học kỳ:</span><b>{{ lopChon.hocKy }}</b></div>
            <div class="info-row"><span>Số sinh viên:</span><b>{{ lopChon.soSinhVien }} SV</b></div>
          </div>
        </div>

        <!-- CẤU HÌNH LÀM BÀI -->
        <div class="form-section">
          <div class="form-section-title">⚙️ Cấu hình làm bài</div>
          <div class="form-grid">
            <label class="fl required">
              <span>Thời gian mở</span>
              <input v-model="form.thoiGianMo" type="datetime-local" class="fi" :class="{ error: errs.thoiGianMo }" />
              <span class="err-msg" v-if="errs.thoiGianMo">Bắt buộc</span>
            </label>
            <label class="fl required">
              <span>Thời gian đóng</span>
              <input v-model="form.thoiGianDong" type="datetime-local" class="fi" :class="{ error: errs.thoiGianDong }" />
              <span class="err-msg" v-if="errs.thoiGianDong">{{ errs.thoiGianDong === true ? 'Bắt buộc' : errs.thoiGianDong }}</span>
            </label>
            <label class="fl required">
              <span>Thời lượng làm bài (phút)</span>
              <input v-model.number="form.thoiLuongLamBaiPhut" type="number" min="5" class="fi" />
            </label>
            <label class="fl required">
              <span>Số lần làm tối đa</span>
              <input v-model.number="form.soLanLamToiDa" type="number" min="1" class="fi" :class="{ error: errs.soLanLamToiDa }" />
              <span class="err-msg" v-if="errs.soLanLamToiDa">Phải ≥ 1</span>
            </label>
            <label class="fl">
              <span>Cách tính điểm</span>
              <select v-model="form.cachTinhDiem" class="fi">
                <option v-for="(label, val) in CACH_TINH_DIEM" :key="val" :value="val">{{ label }}</option>
              </select>
            </label>
            <label class="fl required">
              <span>Điểm tối đa</span>
              <input v-model.number="form.diemToiDa" type="number" min="0.5" step="0.5" class="fi" :class="{ error: errs.diemToiDa }" />
              <span class="err-msg" v-if="errs.diemToiDa">Điểm phải > 0</span>
            </label>
          </div>
          <div class="checkbox-group">
            <label class="chk"><input type="checkbox" v-model="form.choPhepLamQuaHan" /> Cho phép làm quá hạn</label>
            <label class="chk"><input type="checkbox" v-model="form.choPhepXemDiem" /> Cho phép xem điểm sau khi nộp</label>
            <label class="chk"><input type="checkbox" v-model="form.choPhepXemDapAn" /> Cho phép xem đáp án sau khi đóng bài</label>
            <label class="chk"><input type="checkbox" v-model="form.tronCauHoi" /> Trộn câu hỏi</label>
            <label class="chk"><input type="checkbox" v-model="form.tronDapAn" /> Trộn đáp án</label>
            <label class="chk"><input type="checkbox" v-model="form.yeuCauToanManHinh" /> Yêu cầu toàn màn hình</label>
            <label class="chk"><input type="checkbox" v-model="form.tuDongNopKhiViPham" /> Tự động nộp khi vi phạm</label>
          </div>
          <div class="form-grid mt-10">
            <label class="fl">
              <span>Giới hạn rời tab</span>
              <input v-model.number="form.gioiHanRoiTab" type="number" min="0" class="fi" placeholder="VD: 3" />
            </label>
            <label class="fl">
              <span>Mật khẩu làm bài</span>
              <input v-model="form.matKhauLamBai" class="fi" placeholder="Tùy chọn" />
            </label>
          </div>
        </div>

        <!-- CẤU HÌNH ĐIỂM -->
        <div class="form-section">
          <div class="form-section-title">📊 Cấu hình liên kết điểm (tùy chọn)</div>
          <div class="form-grid">
            <label class="fl">
              <span>Cấu hình đánh giá</span>
              <select v-model="form.cauHinhDanhGiaId" class="fi">
                <option value="">-- Không liên kết --</option>
                <option v-for="ch in cauHinhDanhGias" :key="ch.id" :value="ch.id">{{ ch.tenCotDiem || ch.tenCauHinh || `Cột #${ch.id}` }}</option>
              </select>
            </label>
            <label class="fl" v-if="form.cauHinhDanhGiaId">
              <span>Cột điểm nhận kết quả</span>
              <input v-model="form.cotDiem" class="fi" placeholder="VD: Kiểm tra giữa kỳ" />
            </label>
          </div>
          <div class="alert-info mt-10" v-if="!form.cauHinhDanhGiaId">
            ⚠️ Không chọn cấu hình đánh giá thì sau này không thể đẩy điểm sang bảng điểm tự động.
          </div>
        </div>

        <div class="form-actions">
          <button class="lms-btn ghost" @click="$router.back()">Hủy</button>
          <button class="lms-btn" @click="luuNhap" :disabled="dangLuu">Lưu nháp</button>
          <button class="lms-btn primary" @click="giaoBai" :disabled="dangLuu">
            {{ dangLuu ? 'Đang giao...' : '📨 Giao bài' }}
          </button>
        </div>
      </div>

      <!-- Tóm tắt -->
      <div class="summary-col">
        <div class="summary-card">
          <div class="summary-title">📋 Tóm tắt</div>
          <div class="summary-row"><span>Bài tập:</span><b>{{ baiTapChon?.tenBaiTap || '—' }}</b></div>
          <div class="summary-row"><span>Lớp:</span><b>{{ lopChon?.maLop || '—' }}</b></div>
          <div class="summary-row"><span>Mở lúc:</span><b>{{ fmtDT(form.thoiGianMo) }}</b></div>
          <div class="summary-row"><span>Đóng lúc:</span><b>{{ fmtDT(form.thoiGianDong) }}</b></div>
          <div class="summary-row"><span>Thời lượng:</span><b>{{ form.thoiLuongLamBaiPhut }} phút</b></div>
          <div class="summary-row"><span>Số lần làm:</span><b>{{ form.soLanLamToiDa }} lần</b></div>
          <div class="summary-row"><span>Cách tính:</span><b>{{ CACH_TINH_DIEM[form.cachTinhDiem] }}</b></div>
          <div class="summary-row"><span>Điểm tối đa:</span><b>{{ form.diemToiDa }}</b></div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { baiTapOnlineService, baiTapLopService, lmsDanhMucService } from '../services/lmsService'
import { LOAI_BAI, CACH_TINH_DIEM } from '../services/lmsEnum'
import { formatDateTime } from '../services/lmsEnum'

const route = useRoute(), router = useRouter()
const dangLuu = ref(false)
const baiTapList = ref([]), lopList = ref([]), cauHinhDanhGias = ref([])
const errs = ref({})

const form = ref({
  baiTapOnlineId: route.query.baiTapOnlineId || '',
  lopHocPhanId: '',
  thoiGianMo: '', thoiGianDong: '',
  thoiLuongLamBaiPhut: 45, soLanLamToiDa: 1,
  cachTinhDiem: 'lay_diem_cao_nhat', diemToiDa: 10,
  choPhepLamQuaHan: false, choPhepXemDiem: true,
  choPhepXemDapAn: false, tronCauHoi: true, tronDapAn: true,
  yeuCauToanManHinh: false, gioiHanRoiTab: null, tuDongNopKhiViPham: false, matKhauLamBai: '',
  cauHinhDanhGiaId: '', cotDiem: '', ghiChu: '', trangThai: 'chua_mo'
})

const baiTapChon = computed(() => baiTapList.value.find(b => b.id == form.value.baiTapOnlineId))
const lopChon = computed(() => lopList.value.find(l => l.id == form.value.lopHocPhanId))
const fmtDT = (dt) => dt ? formatDateTime(dt) : '—'

function khiChonBaiTap() {
  if (baiTapChon.value) {
    form.value.thoiLuongLamBaiPhut = baiTapChon.value.thoiLuongPhut || 45
    form.value.soLanLamToiDa = baiTapChon.value.soLanLamMacDinh || 1
    form.value.cachTinhDiem = baiTapChon.value.cachTinhDiem || 'lay_diem_cao_nhat'
  }
}

async function khiChonLop() {
  if (form.value.lopHocPhanId) {
    cauHinhDanhGias.value = await lmsDanhMucService.layCauHinhDanhGia(form.value.lopHocPhanId)
  }
}

function validate() {
  const e = {}
  if (!form.value.baiTapOnlineId) e.baiTapOnlineId = true
  if (!form.value.lopHocPhanId) e.lopHocPhanId = true
  if (!form.value.thoiGianMo) e.thoiGianMo = true
  if (!form.value.thoiGianDong) e.thoiGianDong = true
  else if (form.value.thoiGianDong <= form.value.thoiGianMo) e.thoiGianDong = 'Thời gian đóng phải sau thời gian mở'
  if (!form.value.soLanLamToiDa || form.value.soLanLamToiDa < 1) e.soLanLamToiDa = true
  if (!form.value.diemToiDa || form.value.diemToiDa <= 0) e.diemToiDa = true
  errs.value = e
  return !Object.keys(e).length
}

async function luu(trangThai) {
  if (!validate()) { window.scrollTo(0, 0); return }
  dangLuu.value = true
  try {
    const payload = { ...form.value, trangThai }
    await baiTapLopService.giaoBai(payload)
    router.push({ name: 'Lms.BaiTapLop' })
  } finally { dangLuu.value = false }
}
const luuNhap = () => luu('chua_mo')
const giaoBai = () => luu('chua_mo')

onMounted(async () => {
  const [bts, lhps] = await Promise.all([
    baiTapOnlineService.layDanhSach(),
    lmsDanhMucService.layLopHocPhan()
  ])
  baiTapList.value = bts.content || []
  lopList.value = lhps

  if (form.value.baiTapOnlineId) khiChonBaiTap()
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;600;700;800&display=swap');
*, *::before, *::after { box-sizing: border-box; font-family: 'Roboto', sans-serif; }
.lms-page { display: flex; flex-direction: column; gap: 18px; }
.lp-head { display: flex; align-items: center; gap: 12px; }
.lp-h1 { margin: 0; font-size: 22px; font-weight: 800; color: #1e293b; }
.two-col { display: grid; grid-template-columns: 1fr 260px; gap: 18px; align-items: start; }
.main-col, .summary-col { display: flex; flex-direction: column; gap: 16px; }
.form-section { background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; padding: 18px; }
.form-section-title { font-size: 14px; font-weight: 800; color: #1e293b; margin-bottom: 14px; padding-bottom: 10px; border-bottom: 1px solid #f1f5f9; }
.form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
.fl { display: flex; flex-direction: column; gap: 4px; font-size: 12px; font-weight: 700; color: #475569; }
.fl.required span:first-child::after { content: ' *'; color: #dc2626; }
.fi { border: 1px solid #e2e8f0; border-radius: 8px; padding: 8px 10px; font-size: 13px; color: #1e293b; outline: none; font-family: inherit; }
.fi:focus { border-color: #1d4ed8; }
.fi.error { border-color: #ef4444; }
.err-msg { color: #dc2626; font-size: 11px; }
.mt-10 { margin-top: 10px; }
.info-card { background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 8px; padding: 12px; display: flex; flex-direction: column; gap: 6px; }
.info-row { display: flex; gap: 8px; font-size: 13px; }
.info-row span { color: #64748b; width: 100px; flex-shrink: 0; }
.checkbox-group { display: flex; flex-wrap: wrap; gap: 14px; margin-top: 12px; }
.chk { display: flex; align-items: center; gap: 6px; font-size: 13px; font-weight: 500; color: #475569; cursor: pointer; }
.alert-info { background: #fffbeb; border: 1px solid #fde68a; border-radius: 8px; padding: 10px 12px; font-size: 12px; color: #b45309; font-weight: 600; }
.form-actions { display: flex; gap: 10px; justify-content: flex-end; }
.summary-card { background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; padding: 18px; position: sticky; top: 20px; }
.summary-title { font-size: 14px; font-weight: 800; color: #1e293b; margin-bottom: 14px; padding-bottom: 10px; border-bottom: 1px solid #f1f5f9; }
.summary-row { display: flex; justify-content: space-between; gap: 8px; padding: 7px 0; border-bottom: 1px solid #f8fafc; font-size: 13px; }
.summary-row span { color: #64748b; }
.summary-row b { color: #1e293b; text-align: right; font-size: 12px; max-width: 140px; }
.lms-btn { display: inline-flex; align-items: center; gap: 6px; padding: 8px 16px; border: 1px solid #e2e8f0; border-radius: 8px; background: #fff; color: #475569; font-size: 13px; font-weight: 600; cursor: pointer; text-decoration: none; transition: all 0.15s; }
.lms-btn:disabled { opacity: 0.6; cursor: not-allowed; }
.lms-btn.primary { background: #1d4ed8; color: #fff; border-color: #1d4ed8; }
.lms-btn.primary:hover:not(:disabled) { background: #1e40af; }
.lms-btn.ghost { background: transparent; border-color: transparent; color: #64748b; }
.lms-btn.ghost:hover { background: #f1f5f9; }
@media (max-width: 900px) { .two-col { grid-template-columns: 1fr; } .summary-card { position: static; } .form-grid { grid-template-columns: 1fr; } }
</style>
