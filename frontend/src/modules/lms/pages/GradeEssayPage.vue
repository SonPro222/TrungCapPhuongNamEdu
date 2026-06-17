<template>
  <section class="lms-page">
    <div class="lp-head">
      <button class="lms-btn ghost" @click="$router.back()">← Quay lại</button>
      <div>
        <h1 class="lp-h1">Chấm bài tự luận & Chỉnh điểm</h1>
        <p class="lp-sub" v-if="chiTiet">{{ chiTiet.hoTen }} – Lần {{ chiTiet.lanThu }} · Nộp: {{ fmt(chiTiet.thoiGianNop) }}</p>
      </div>
    </div>

    <div v-if="dangTai" class="loading-state">⏳ Đang tải bài làm...</div>
    <template v-else-if="chiTiet">
      <div class="two-col">
        <!-- CỘT TRÁI: Câu hỏi -->
        <div class="questions-col">
          <div class="form-section" v-for="(cau, idx) in chiTiet.cauTraLois" :key="cau.cauHoiId">
            <div class="cau-head">
              <div class="cau-so">Câu {{ idx + 1 }}</div>
              <div class="cau-info">
                <span class="lms-badge badge-blue">{{ loaiCauHoiLabel(cau.loaiCauHoi) }}</span>
                <span class="text-gray">{{ cau.diemToiDa }} điểm</span>
              </div>
            </div>

            <div class="cau-noi-dung">{{ cau.noiDung }}</div>

            <!-- Trắc nghiệm / Đúng Sai -->
            <template v-if="['trac_nghiem_1_dap_an', 'trac_nghiem_nhieu_dap_an', 'dung_sai'].includes(cau.loaiCauHoi)">
              <div class="dap-an-sv">
                <div class="da-label">Đáp án sinh viên chọn:</div>
                <div v-for="da in cau.dapAnChon" :key="da" class="da-item" :class="{ correct: cau.dapAnDung?.includes(da), wrong: !cau.dapAnDung?.includes(da) }">
                  <span>{{ cau.dapAnDung?.includes(da) ? '✓' : '✗' }}</span> {{ da }}
                </div>
              </div>
              <div class="dap-an-dung">
                <div class="da-label">Đáp án đúng:</div>
                <div v-for="da in cau.dapAnDung" :key="da" class="da-item correct">✓ {{ da }}</div>
              </div>
              <div class="cau-diem-auto">
                Điểm tự động: <b :class="cau.diem >= cau.diemToiDa ? 'text-green' : (cau.diem > 0 ? 'text-blue' : 'text-red')">{{ cau.diem }}/{{ cau.diemToiDa }}</b>
              </div>
            </template>

            <!-- Điền khuyết -->
            <template v-else-if="cau.loaiCauHoi === 'dien_khuyet'">
              <div class="dap-an-sv">
                <div class="da-label">Câu trả lời của sinh viên:</div>
                <div class="da-text">{{ cau.noiDungTraLoi || '(Để trống)' }}</div>
              </div>
              <div class="cau-diem-auto">
                Điểm tự động: <b>{{ cau.diem }}/{{ cau.diemToiDa }}</b>
              </div>
            </template>

            <!-- Tự luận -->
            <template v-else-if="cau.loaiCauHoi === 'tu_luan'">
              <div class="tra-loi-box">
                <div class="tl-label">Bài làm của sinh viên:</div>
                <div class="tl-content">{{ cau.noiDungTraLoi || '(Sinh viên không trả lời)' }}</div>
              </div>

              <div v-if="cau.huongDanCham" class="huong-dan-box">
                <div class="hd-label">📋 Hướng dẫn chấm:</div>
                <div class="hd-content">{{ cau.huongDanCham }}</div>
              </div>

              <div class="cham-diem-row">
                <label class="fl">
                  <span>Điểm chấm (0 – {{ cau.diemToiDa }})</span>
                  <input
                    v-model.number="diemTuLuan[cau.cauHoiId]"
                    type="number" :min="0" :max="cau.diemToiDa" step="0.5"
                    class="fi diem-input"
                    :class="{ error: diemErrors[cau.cauHoiId] }"
                    @input="validateDiem(cau)"
                  />
                  <span v-if="diemErrors[cau.cauHoiId]" class="err-msg">Điểm từ 0 đến {{ cau.diemToiDa }}</span>
                </label>
                <label class="fl flex-1">
                  <span>Nhận xét (tùy chọn)</span>
                  <input v-model="nhanXet[cau.cauHoiId]" class="fi" placeholder="Nhận xét cho sinh viên..." />
                </label>
              </div>

              <div v-if="cau.diemGiaoVien !== null" class="da-cham-truoc">
                ℹ️ Đã chấm trước: <b>{{ cau.diemGiaoVien }}</b> điểm
                <span v-if="cau.trangThaiCham === 'da_cham'" class="lms-badge badge-green ml-6">Đã chấm</span>
              </div>
            </template>
          </div>
        </div>

        <!-- CỘT PHẢI: Thông tin & Lưu -->
        <div class="info-col">
          <div class="info-card sticky-card">
            <div class="ic-title">📊 Tóm tắt bài làm</div>
            <div class="ic-row"><span>Họ tên:</span><b>{{ chiTiet.hoTen }}</b></div>
            <div class="ic-row"><span>Mã SV:</span><b>{{ chiTiet.maSV }}</b></div>
            <div class="ic-row"><span>Lần làm:</span><b>{{ chiTiet.lanThu }}</b></div>
            <div class="ic-row"><span>Thời gian nộp:</span><b>{{ fmt(chiTiet.thoiGianNop) }}</b></div>
            <div class="ic-row"><span>Điểm hệ thống:</span><b class="text-blue">{{ chiTiet.diemTuDong }}</b></div>
            <div class="ic-row"><span>Câu đúng:</span><b>{{ chiTiet.soCauDung }}/{{ chiTiet.tongSoCau }}</b></div>

            <div class="ic-divider"></div>

            <div class="ic-title mt-8">✍️ Chỉnh điểm toàn bài</div>
            <label class="fl mb-8">
              <span>Điểm điều chỉnh</span>
              <input v-model.number="diemChinhSua" type="number" min="0" max="10" step="0.5" class="fi" placeholder="Nhập điểm mới..." />
            </label>
            <label class="fl mb-8">
              <span>Lý do chỉnh điểm</span>
              <textarea v-model="lyDoChinhSua" class="fi ta" rows="2" placeholder="Ghi rõ lý do..."></textarea>
            </label>

            <div class="btn-stack">
              <button class="lms-btn primary w-full" @click="luuChamTuLuan" :disabled="dangLuu">
                {{ dangLuu ? 'Đang lưu...' : '💾 Lưu chấm tự luận' }}
              </button>
              <button class="lms-btn w-full" @click="luuChinhDiem" :disabled="dangLuu || !diemChinhSua">
                {{ dangLuu ? 'Đang lưu...' : '📝 Lưu chỉnh điểm' }}
              </button>
            </div>

            <div v-if="thongBao" class="alert mt-8" :class="thongBao.type">{{ thongBao.msg }}</div>
          </div>
        </div>
      </div>
    </template>
    <div v-else class="loading-state text-gray">Không tìm thấy dữ liệu bài làm.</div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { giaoVienLmsService } from '../services/lmsService'
import { LOAI_CAU_HOI, formatDateTime } from '../services/lmsEnum'

const route = useRoute()
const dangTai = ref(true), dangLuu = ref(false)
const chiTiet = ref(null)
const diemTuLuan = ref({})
const nhanXet = ref({})
const diemErrors = ref({})
const diemChinhSua = ref(null)
const lyDoChinhSua = ref('')
const thongBao = ref(null)

function loaiCauHoiLabel(val) { return LOAI_CAU_HOI[val] || val }
function fmt(dt) { return formatDateTime(dt) }
function showMsg(type, msg) { thongBao.value = { type, msg }; setTimeout(() => { thongBao.value = null }, 3000) }

function validateDiem(cau) {
  const val = diemTuLuan.value[cau.cauHoiId]
  if (val < 0 || val > cau.diemToiDa) {
    diemErrors.value[cau.cauHoiId] = true
  } else {
    delete diemErrors.value[cau.cauHoiId]
  }
}

async function luuChamTuLuan() {
  const cauTuLuan = chiTiet.value.cauTraLois.filter(c => c.loaiCauHoi === 'tu_luan')
  if (Object.keys(diemErrors.value).length) return

  dangLuu.value = true
  try {
    const payload = {
      danhSachChamTuLuan: cauTuLuan.map(cau => ({
        cauHoiId: cau.cauHoiId,
        diem: diemTuLuan.value[cau.cauHoiId] ?? 0,
        nhanXet: nhanXet.value[cau.cauHoiId] || ''
      }))
    }
    await giaoVienLmsService.chamTuLuan(route.params.id, payload)
    showMsg('success', '✓ Đã lưu điểm tự luận thành công!')
  } catch (e) {
    showMsg('error', '✗ Lỗi khi lưu điểm, vui lòng thử lại.')
  } finally {
    dangLuu.value = false
  }
}

async function luuChinhDiem() {
  if (!diemChinhSua.value) return
  dangLuu.value = true
  try {
    await giaoVienLmsService.chamTuLuan(route.params.id, {
      diemChinhSua: diemChinhSua.value,
      lyDo: lyDoChinhSua.value
    })
    showMsg('success', '✓ Đã chỉnh điểm thành công!')
  } catch {
    showMsg('error', '✗ Lỗi khi chỉnh điểm.')
  } finally {
    dangLuu.value = false
  }
}

onMounted(async () => {
  dangTai.value = true
  try {
    const data = await giaoVienLmsService.getChiTietLanLam(route.params.id)
    chiTiet.value = data
    // Init điểm tự luận từ dữ liệu cũ
    data.cauTraLois?.forEach(cau => {
      if (cau.loaiCauHoi === 'tu_luan') {
        diemTuLuan.value[cau.cauHoiId] = cau.diemGiaoVien ?? ''
        nhanXet.value[cau.cauHoiId] = cau.nhanXet || ''
      }
    })
  } finally {
    dangTai.value = false
  }
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;600;700;800&display=swap');
*, *::before, *::after { box-sizing: border-box; font-family: 'Roboto', sans-serif; }
.lms-page { display: flex; flex-direction: column; gap: 18px; }
.lp-head { display: flex; align-items: flex-start; gap: 12px; }
.lp-head > div { flex: 1; }
.lp-h1 { margin: 0 0 4px; font-size: 22px; font-weight: 800; color: #1e293b; }
.lp-sub { margin: 0; color: #64748b; font-size: 13px; }
.loading-state { text-align: center; padding: 48px; color: #64748b; font-size: 15px; }
.two-col { display: grid; grid-template-columns: 1fr 300px; gap: 18px; align-items: start; }
.questions-col { display: flex; flex-direction: column; gap: 16px; }
.form-section { background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; padding: 18px; }
.cau-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.cau-so { font-size: 14px; font-weight: 800; color: #1e293b; }
.cau-info { display: flex; align-items: center; gap: 8px; }
.cau-noi-dung { font-size: 14px; color: #1e293b; font-weight: 500; margin-bottom: 14px; line-height: 1.6; background: #f8fafc; padding: 12px; border-radius: 8px; border-left: 3px solid #1d4ed8; }
.dap-an-sv, .dap-an-dung { margin-bottom: 10px; }
.da-label { font-size: 11px; font-weight: 700; color: #64748b; margin-bottom: 6px; text-transform: uppercase; letter-spacing: 0.05em; }
.da-item { padding: 6px 10px; border-radius: 6px; font-size: 13px; font-weight: 500; display: flex; align-items: center; gap: 6px; margin-bottom: 4px; }
.da-item.correct { background: #dcfce7; color: #15803d; }
.da-item.wrong { background: #fee2e2; color: #b91c1c; }
.da-text { font-size: 13px; color: #334155; background: #f8fafc; padding: 8px 10px; border-radius: 6px; }
.cau-diem-auto { font-size: 13px; color: #475569; margin-top: 8px; }
.tra-loi-box { background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 8px; padding: 12px; margin-bottom: 12px; }
.tl-label { font-size: 11px; font-weight: 700; color: #64748b; text-transform: uppercase; margin-bottom: 8px; }
.tl-content { font-size: 13px; color: #334155; line-height: 1.7; white-space: pre-wrap; }
.huong-dan-box { background: #fffbeb; border: 1px solid #fde68a; border-radius: 8px; padding: 12px; margin-bottom: 12px; }
.hd-label { font-size: 11px; font-weight: 700; color: #b45309; text-transform: uppercase; margin-bottom: 6px; }
.hd-content { font-size: 12px; color: #92400e; line-height: 1.6; }
.cham-diem-row { display: flex; gap: 12px; align-items: flex-start; margin-top: 12px; }
.da-cham-truoc { font-size: 12px; color: #4338ca; background: #eef2ff; padding: 6px 10px; border-radius: 6px; margin-top: 8px; display: flex; align-items: center; gap: 6px; }
.fl { display: flex; flex-direction: column; gap: 4px; font-size: 12px; font-weight: 700; color: #475569; }
.fl.flex-1 { flex: 1; }
.fl.mb-8 { margin-bottom: 8px; }
.fi { border: 1px solid #e2e8f0; border-radius: 8px; padding: 7px 10px; font-size: 13px; color: #1e293b; outline: none; font-family: inherit; }
.fi:focus { border-color: #1d4ed8; }
.fi.error { border-color: #ef4444; }
.fi.diem-input { width: 100px; }
.ta { resize: vertical; min-height: 60px; }
.err-msg { color: #dc2626; font-size: 11px; }
.info-col { display: flex; flex-direction: column; }
.info-card { background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; padding: 18px; }
.sticky-card { position: sticky; top: 20px; }
.ic-title { font-size: 14px; font-weight: 800; color: #1e293b; margin-bottom: 12px; }
.mt-8 { margin-top: 8px; }
.ic-row { display: flex; justify-content: space-between; align-items: center; padding: 6px 0; border-bottom: 1px solid #f8fafc; font-size: 12px; }
.ic-row span { color: #64748b; }
.ic-row b { color: #1e293b; text-align: right; max-width: 140px; font-size: 12px; }
.ic-divider { height: 1px; background: #e2e8f0; margin: 12px 0; }
.btn-stack { display: flex; flex-direction: column; gap: 8px; }
.lms-badge { display: inline-flex; align-items: center; padding: 2px 8px; border-radius: 20px; font-size: 10px; font-weight: 700; }
.ml-6 { margin-left: 6px; }
.badge-blue { background: #dbeafe; color: #1d4ed8; }
.badge-green { background: #dcfce7; color: #15803d; }
.text-gray { color: #94a3b8; }
.text-green { color: #15803d; }
.text-blue { color: #1d4ed8; }
.text-red { color: #b91c1c; }
.alert { padding: 8px 12px; border-radius: 8px; font-size: 12px; font-weight: 600; }
.alert.success { background: #dcfce7; color: #15803d; }
.alert.error { background: #fee2e2; color: #b91c1c; }
.lms-btn { display: inline-flex; align-items: center; justify-content: center; gap: 6px; padding: 9px 16px; border: 1px solid #e2e8f0; border-radius: 8px; background: #fff; color: #475569; font-size: 13px; font-weight: 600; cursor: pointer; text-decoration: none; transition: all 0.15s; }
.lms-btn:disabled { opacity: 0.6; cursor: not-allowed; }
.lms-btn.primary { background: #1d4ed8; color: #fff; border-color: #1d4ed8; }
.lms-btn.ghost { background: transparent; border-color: transparent; color: #64748b; }
.lms-btn.ghost:hover { background: #f1f5f9; }
.w-full { width: 100%; }
@media (max-width: 900px) { .two-col { grid-template-columns: 1fr; } .sticky-card { position: static; } }
</style>
