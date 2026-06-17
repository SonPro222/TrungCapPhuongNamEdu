<template>
  <section class="lms-page">
    <div class="lp-head">
      <button class="lms-btn ghost" @click="$router.back()">← Quay lại</button>
      <div>
        <h1 class="lp-h1">Đẩy điểm sang bảng điểm</h1>
        <p class="lp-sub" v-if="baiTapLop">{{ baiTapLop.tenBaiTap }} – {{ baiTapLop.maLop }}</p>
      </div>
    </div>

    <div v-if="dangTai" class="loading-state">⏳ Đang tải...</div>
    <template v-else-if="baiTapLop">
      <!-- Kiểm tra điều kiện -->
      <div v-if="baiTapLop.trangThai !== 'da_chot'" class="alert-error">
        ⚠️ Bài tập chưa được chốt điểm. Vui lòng <RouterLink :to="{ name: 'Lms.KetQua', params: { id: $route.params.id } }" class="link">chốt điểm</RouterLink> trước khi đẩy.
      </div>

      <!-- Thông tin đẩy điểm -->
      <div class="form-section" v-if="baiTapLop.trangThai === 'da_chot'">
        <div class="form-section-title">📊 Thông tin đẩy điểm</div>
        <div class="info-grid">
          <div class="info-block">
            <div class="ib-label">Bài tập</div>
            <div class="ib-value">{{ baiTapLop.tenBaiTap }}</div>
          </div>
          <div class="info-block">
            <div class="ib-label">Môn học</div>
            <div class="ib-value">{{ baiTapLop.tenMonHoc }}</div>
          </div>
          <div class="info-block">
            <div class="ib-label">Lớp học phần</div>
            <div class="ib-value">{{ baiTapLop.maLop }}</div>
          </div>
          <div class="info-block">
            <div class="ib-label">Tổng kết quả</div>
            <div class="ib-value">{{ danhSach.length }} sinh viên đã chốt</div>
          </div>
        </div>

        <div class="form-grid mt-16">
          <label class="fl required">
            <span>Cấu hình đánh giá mục tiêu</span>
            <select v-model="form.cauHinhDanhGiaId" class="fi" :class="{ error: errs.cauHinhDanhGiaId }">
              <option value="">-- Chọn cấu hình --</option>
              <option v-for="ch in cauHinhDanhGias" :key="ch.id" :value="ch.id">{{ ch.tenCotDiem || ch.tenCauHinh || `Cột #${ch.id}` }}</option>
            </select>
            <span class="err-msg" v-if="errs.cauHinhDanhGiaId">Bắt buộc chọn cấu hình</span>
          </label>
          <label class="fl required">
            <span>Cột điểm nhận kết quả</span>
            <input v-model="form.cotDiem" class="fi" :class="{ error: errs.cotDiem }" placeholder="VD: Kiểm tra giữa kỳ" />
            <span class="err-msg" v-if="errs.cotDiem">Bắt buộc nhập cột điểm</span>
          </label>
          <label class="fl col-2">
            <span>Ghi chú</span>
            <input v-model="form.ghiChu" class="fi" placeholder="Ghi chú cho lần đẩy điểm này..." />
          </label>
        </div>
      </div>

      <!-- Xem trước dữ liệu sẽ đẩy -->
      <div class="form-section" v-if="baiTapLop.trangThai === 'da_chot'">
        <div class="form-section-title">👁️ Xem trước dữ liệu đẩy điểm</div>
        <div class="table-wrap">
          <table class="lms-table">
            <thead>
              <tr>
                <th>STT</th>
                <th>Mã SV</th>
                <th>Họ tên</th>
                <th class="center">Điểm cuối cùng</th>
                <th>Kết quả</th>
                <th>Ghi chú</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="danhSach.length === 0">
                <td colspan="6" class="empty-cell">Chưa có kết quả nào</td>
              </tr>
              <tr v-for="(item, idx) in danhSach" :key="item.id">
                <td class="center small-text">{{ idx + 1 }}</td>
                <td class="small-text">{{ item.maSV }}</td>
                <td>{{ item.hoTen }}</td>
                <td class="center">
                  <b :class="['diem-final', item.diemCuoiCung >= 5 ? 'text-green' : 'text-red']">{{ fmtD(item.diemCuoiCung) }}</b>
                </td>
                <td>
                  <span :class="['lms-badge', item.ketQua === 'dat' ? 'badge-green' : 'badge-red']">
                    {{ item.ketQua === 'dat' ? '✓ Đạt' : '✗ Không đạt' }}
                  </span>
                </td>
                <td class="small-text">{{ item.diemGiaoVienChinhSua !== null ? 'GV đã chỉnh điểm' : '' }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Actions -->
      <div v-if="baiTapLop.trangThai === 'da_chot'" class="form-actions">
        <button class="lms-btn ghost" @click="$router.back()">Hủy</button>
        <button class="lms-btn primary" @click="xacNhanDay" :disabled="dangXuLy">
          {{ dangXuLy ? 'Đang đẩy...' : '🚀 Đẩy sang bảng điểm' }}
        </button>
      </div>
    </template>

    <!-- Confirm modal -->
    <div v-if="showConfirm" class="modal-overlay" @click.self="showConfirm = false">
      <div class="modal-box">
        <h3>🚀 Xác nhận đẩy điểm</h3>
        <p>Bạn sắp đẩy điểm của <b>{{ danhSach.length }}</b> sinh viên sang bảng điểm môn <b>{{ baiTapLop?.tenMonHoc }}</b>.</p>
        <p>Thao tác này <b>không thể hoàn tác</b> sau khi thực hiện. Tiếp tục?</p>
        <div class="modal-actions">
          <button class="lms-btn ghost" @click="showConfirm = false">Hủy</button>
          <button class="lms-btn primary" @click="thucHienDay" :disabled="dangXuLy">
            {{ dangXuLy ? 'Đang xử lý...' : 'Xác nhận đẩy' }}
          </button>
        </div>
      </div>
    </div>

    <!-- Kết quả thành công -->
    <div v-if="success" class="success-banner">
      <div class="sb-icon">🎉</div>
      <div>
        <div class="sb-title">Đẩy điểm thành công!</div>
        <div class="sb-sub">Tạo mới {{ pushResult?.soDiemTaoMoi ?? 0 }} điểm, cập nhật {{ pushResult?.soDiemCapNhat ?? 0 }} điểm, bỏ qua {{ pushResult?.soBoQua ?? 0 }} dòng.</div>
      </div>
      <RouterLink :to="{ name: 'Lms.BaiTapLop' }" class="lms-btn primary">Về danh sách bài</RouterLink>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { baiTapLopService, ketQuaService, lmsDanhMucService } from '../services/lmsService'
import { formatDiem } from '../services/lmsEnum'

const route = useRoute(), router = useRouter()
const dangTai = ref(true), dangXuLy = ref(false)
const baiTapLop = ref(null), danhSach = ref([]), cauHinhDanhGias = ref([])
const showConfirm = ref(false), success = ref(false), pushResult = ref(null)
const form = ref({ cauHinhDanhGiaId: '', cotDiem: '', ghiChu: '' })
const errs = ref({})

function fmtD(d) { return formatDiem(d) }

function validate() {
  const e = {}
  if (!form.value.cauHinhDanhGiaId) e.cauHinhDanhGiaId = true
  if (!form.value.cotDiem?.trim()) e.cotDiem = true
  errs.value = e
  return !Object.keys(e).length
}

function xacNhanDay() {
  if (!validate()) return
  showConfirm.value = true
}

async function thucHienDay() {
  dangXuLy.value = true
  try {
    pushResult.value = await baiTapLopService.daySangBangDiem(route.params.id, form.value)
    showConfirm.value = false
    success.value = true
  } finally { dangXuLy.value = false }
}

onMounted(async () => {
  dangTai.value = true
  try {
    const [btl, res, cacCauHinh] = await Promise.all([
      baiTapLopService.layTheoId(route.params.id),
      ketQuaService.layTheoLop(route.params.id),
      lmsDanhMucService.layCauHinhDanhGia()
    ])
    baiTapLop.value = btl
    danhSach.value = res.content || res
    cauHinhDanhGias.value = cacCauHinh.filter(ch => !btl?.lopHocPhanId || String(ch.lopHocPhanId) === String(btl.lopHocPhanId))
    if (btl.cotDiem) form.value.cotDiem = btl.cotDiem
    if (btl.cauHinhDanhGiaId) form.value.cauHinhDanhGiaId = btl.cauHinhDanhGiaId
  } finally { dangTai.value = false }
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
.loading-state { text-align: center; padding: 48px; color: #64748b; }
.alert-error { background: #fee2e2; border: 1px solid #fca5a5; border-radius: 10px; padding: 14px 16px; font-size: 13px; color: #b91c1c; font-weight: 600; }
.link { color: #1d4ed8; text-decoration: underline; }
.form-section { background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; padding: 18px; }
.form-section-title { font-size: 14px; font-weight: 800; color: #1e293b; margin-bottom: 14px; padding-bottom: 10px; border-bottom: 1px solid #f1f5f9; }
.info-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 12px; }
.info-block { background: #f8fafc; border-radius: 8px; padding: 12px; }
.ib-label { font-size: 11px; font-weight: 700; color: #64748b; text-transform: uppercase; margin-bottom: 4px; }
.ib-value { font-size: 14px; font-weight: 700; color: #1e293b; }
.form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
.col-2 { grid-column: 1 / -1; }
.mt-16 { margin-top: 16px; }
.fl { display: flex; flex-direction: column; gap: 4px; font-size: 12px; font-weight: 700; color: #475569; }
.fl.required span:first-child::after { content: ' *'; color: #dc2626; }
.fi { border: 1px solid #e2e8f0; border-radius: 8px; padding: 8px 10px; font-size: 13px; color: #1e293b; outline: none; font-family: inherit; }
.fi:focus { border-color: #1d4ed8; }
.fi.error { border-color: #ef4444; }
.err-msg { color: #dc2626; font-size: 11px; }
.table-wrap { overflow-x: auto; }
.lms-table { width: 100%; border-collapse: collapse; font-size: 13px; }
.lms-table th { background: #f8fafc; padding: 9px 12px; text-align: left; font-weight: 700; color: #475569; font-size: 12px; border-bottom: 2px solid #e2e8f0; }
.lms-table td { padding: 9px 12px; border-bottom: 1px solid #f1f5f9; vertical-align: middle; }
.lms-table tr:last-child td { border-bottom: none; }
.empty-cell { text-align: center; color: #94a3b8; padding: 24px !important; }
.center { text-align: center; }
.small-text { font-size: 12px; color: #64748b; }
.diem-final { font-size: 14px; font-weight: 800; }
.text-green { color: #15803d; }
.text-red { color: #b91c1c; }
.lms-badge { display: inline-flex; align-items: center; padding: 3px 10px; border-radius: 20px; font-size: 11px; font-weight: 700; }
.badge-green { background: #dcfce7; color: #15803d; }
.badge-red { background: #fee2e2; color: #b91c1c; }
.form-actions { display: flex; gap: 10px; justify-content: flex-end; }
.modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.45); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal-box { background: #fff; border-radius: 14px; padding: 24px; max-width: 440px; width: 90%; box-shadow: 0 20px 60px rgba(0,0,0,0.2); }
.modal-box h3 { margin: 0 0 10px; font-size: 16px; font-weight: 800; }
.modal-box p { margin: 0 0 12px; color: #475569; font-size: 13px; line-height: 1.6; }
.modal-actions { display: flex; gap: 10px; justify-content: flex-end; }
.success-banner { background: #f0fdf4; border: 2px solid #86efac; border-radius: 12px; padding: 24px; display: flex; align-items: center; gap: 16px; }
.sb-icon { font-size: 36px; }
.sb-title { font-size: 18px; font-weight: 800; color: #15803d; }
.sb-sub { font-size: 13px; color: #16a34a; margin-top: 4px; }
.lms-btn { display: inline-flex; align-items: center; gap: 6px; padding: 8px 16px; border: 1px solid #e2e8f0; border-radius: 8px; background: #fff; color: #475569; font-size: 13px; font-weight: 600; cursor: pointer; text-decoration: none; transition: all 0.15s; }
.lms-btn:disabled { opacity: 0.6; cursor: not-allowed; }
.lms-btn.primary { background: #1d4ed8; color: #fff; border-color: #1d4ed8; }
.lms-btn.ghost { background: transparent; border-color: transparent; color: #64748b; }
.lms-btn.ghost:hover { background: #f1f5f9; }
@media (max-width: 900px) { .info-grid { grid-template-columns: 1fr 1fr; } .form-grid { grid-template-columns: 1fr; } }
</style>
