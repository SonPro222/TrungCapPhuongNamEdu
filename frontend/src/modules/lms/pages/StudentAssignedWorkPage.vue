<template>
  <section class="lms-page">
    <div class="lp-head">
      <div>
        <h1 class="lp-h1">Bài được giao</h1>
        <p class="lp-sub">Danh sách bài tập online được giao cho bạn</p>
      </div>
    </div>

    <div class="filter-card">
      <div class="filter-row">
        <label class="fl">
          <span>Trạng thái</span>
          <select v-model="loc.trangThai" class="fi">
            <option value="">Tất cả</option>
            <option value="chua_lam">Chưa làm</option>
            <option value="dang_mo">Đang mở</option>
            <option value="da_nop">Đã nộp</option>
            <option value="da_dong">Đã đóng</option>
          </select>
        </label>
        <label class="fl flex-1">
          <span>Tìm kiếm</span>
          <input v-model.trim="loc.keyword" class="fi" placeholder="Tên bài tập, môn học..." />
        </label>
      </div>
    </div>

    <div v-if="dangTai" class="loading-state">⏳ Đang tải bài tập...</div>
    <div v-else-if="danhSachHienThi.length === 0" class="empty-full">
      <div class="ef-icon">📚</div>
      <div class="ef-title">Chưa có bài tập nào</div>
      <div class="ef-sub">Khi giáo viên giao bài, chúng sẽ xuất hiện ở đây.</div>
    </div>

    <div v-else class="card-grid">
      <div v-for="item in danhSachHienThi" :key="item.id" class="bai-card" :class="cardClass(item)">
        <div class="bc-header">
          <div class="bc-header-left">
            <span class="bc-loai">{{ LOAI_BAI[item.loaiBai] || item.loaiBai }}</span>
            <span :class="['lms-badge', mauTT(item.trangThai)]">{{ tenTT(item.trangThai) }}</span>
          </div>
          <div class="bc-diem" v-if="item.diemCuoiCung !== null && item.diemCuoiCung !== undefined">
            <span :class="['diem-tag', item.diemCuoiCung >= 5 ? 'diem-dat' : 'diem-khong-dat']">
              {{ fmtD(item.diemCuoiCung) }} điểm
            </span>
          </div>
        </div>

        <div class="bc-title">{{ item.tenBaiTap }}</div>
        <div class="bc-mon">{{ item.tenMonHoc }}</div>

        <div class="bc-info-row">
          <div class="bc-info-item">
            <span class="bi-icon">⏱</span>
            <span>{{ item.thoiLuongPhut }} phút</span>
          </div>
          <div class="bc-info-item">
            <span class="bi-icon">🔁</span>
            <span>{{ item.soLanLamToiDa }} lần</span>
          </div>
          <div class="bc-info-item">
            <span class="bi-icon">✍️</span>
            <span>Đã làm: {{ item.soLanDaLam || 0 }}</span>
          </div>
        </div>

        <div class="bc-time">
          <div><b>Mở:</b> {{ fmt(item.thoiGianMo) }}</div>
          <div><b>Đóng:</b> {{ fmt(item.thoiGianDong) }}</div>
        </div>

        <div class="countdown" v-if="item.trangThai === 'dang_mo' && thoiGianConLai(item) !== null">
          <span class="cd-icon">⏰</span>
          <span>Còn lại: <b>{{ thoiGianConLai(item) }}</b></span>
        </div>

        <div class="bc-actions">
          <template v-if="item.trangThai === 'dang_mo' && coTheLam(item)">
            <button class="lms-btn primary full-btn" @click="batDauLamBai(item)">
              {{ item.soLanDaLam > 0 ? '🔄 Làm lại' : '▶ Bắt đầu làm bài' }}
            </button>
          </template>
          <template v-else-if="item.soLanDaLam > 0">
            <button class="lms-btn full-btn" @click="xemKetQua(item)">📊 Xem kết quả</button>
          </template>
          <template v-else>
            <button class="lms-btn full-btn disabled-btn" disabled>
              {{ item.trangThai === 'chua_mo' ? '🔒 Chưa mở' : '⛔ Đã đóng' }}
            </button>
          </template>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { sinhVienLmsService } from '../services/lmsService'
import { LOAI_BAI, TRANG_THAI_BAI_TAP_LOP, mauTrangThaiBaiTapLop, formatDateTime, formatDiem } from '../services/lmsEnum'

const router = useRouter()
const dangTai = ref(true)
const danhSach = ref([])
const loc = ref({ trangThai: '', keyword: '' })

const danhSachHienThi = computed(() => danhSach.value.filter(item => {
  if (loc.value.trangThai) {
    if (loc.value.trangThai === 'chua_lam' && item.soLanDaLam > 0) return false
    if (loc.value.trangThai === 'da_nop' && !(item.soLanDaLam > 0)) return false
    if (['dang_mo', 'da_dong'].includes(loc.value.trangThai) && item.trangThai !== loc.value.trangThai) return false
  }
  if (loc.value.keyword) {
    const kw = loc.value.keyword.toLowerCase()
    if (!item.tenBaiTap?.toLowerCase().includes(kw) && !item.tenMonHoc?.toLowerCase().includes(kw)) return false
  }
  return true
}))

function fmt(dt) { return formatDateTime(dt) }
function fmtD(d) { return formatDiem(d) }
function mauTT(tt) { return mauTrangThaiBaiTapLop(tt) }
function tenTT(tt) { return TRANG_THAI_BAI_TAP_LOP[tt] || tt }
function coTheLam(item) { return item.soLanDaLam < item.soLanLamToiDa }
function cardClass(item) {
  if (item.trangThai === 'dang_mo' && coTheLam(item)) return 'card-active'
  if (item.soLanDaLam > 0) return 'card-done'
  return 'card-closed'
}
function thoiGianConLai(item) {
  const dong = new Date(item.thoiGianDong)
  const ms = dong - Date.now()
  if (ms <= 0) return null
  const h = Math.floor(ms / 3600000)
  const m = Math.floor((ms % 3600000) / 60000)
  if (h > 24) return Math.floor(h / 24) + ' ngày'
  return `${h}h ${m}m`
}

async function batDauLamBai(item) {
  try {
    const lanLam = await sinhVienLmsService.batDauLamBai(item.id)
    router.push({ name: 'Lms.LamBai', params: { id: lanLam.id } })
  } catch {
    alert('Không thể bắt đầu làm bài. Vui lòng thử lại.')
  }
}

function xemKetQua(item) {
  router.push({ name: 'Lms.KetQuaSinhVien', params: { id: item.id } })
}

onMounted(async () => {
  dangTai.value = true
  try {
    const res = await sinhVienLmsService.layBaiDuocGiao()
    danhSach.value = res.content || []
  } finally { dangTai.value = false }
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;600;700;800&display=swap');
*, *::before, *::after { box-sizing: border-box; font-family: 'Roboto', sans-serif; }
.lms-page { display: flex; flex-direction: column; gap: 18px; }
.lp-head { display: flex; justify-content: space-between; align-items: flex-start; }
.lp-h1 { margin: 0 0 4px; font-size: 22px; font-weight: 800; color: #1e293b; }
.lp-sub { margin: 0; color: #64748b; font-size: 13px; }
.filter-card { background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; padding: 14px 16px; }
.filter-row { display: flex; gap: 12px; }
.fl { display: flex; flex-direction: column; gap: 4px; font-size: 12px; font-weight: 600; color: #475569; min-width: 140px; }
.fl.flex-1 { flex: 1; min-width: 0; }
.fi { border: 1px solid #e2e8f0; border-radius: 7px; padding: 7px 10px; font-size: 13px; color: #1e293b; outline: none; font-family: inherit; }
.loading-state { text-align: center; padding: 48px; color: #64748b; }
.empty-full { text-align: center; padding: 60px 20px; }
.ef-icon { font-size: 48px; margin-bottom: 12px; }
.ef-title { font-size: 18px; font-weight: 700; color: #1e293b; }
.ef-sub { font-size: 13px; color: #64748b; margin-top: 6px; }
.card-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; }
.bai-card { background: #fff; border: 1px solid #e2e8f0; border-radius: 14px; padding: 18px; display: flex; flex-direction: column; gap: 10px; transition: box-shadow 0.15s; }
.bai-card:hover { box-shadow: 0 4px 20px rgba(0,0,0,0.08); }
.bai-card.card-active { border-color: #1d4ed8; border-left: 4px solid #1d4ed8; }
.bai-card.card-done { border-color: #bbf7d0; border-left: 4px solid #16a34a; }
.bai-card.card-closed { opacity: 0.75; }
.bc-header { display: flex; justify-content: space-between; align-items: center; }
.bc-header-left { display: flex; align-items: center; gap: 6px; }
.bc-loai { font-size: 11px; font-weight: 700; color: #64748b; background: #f1f5f9; padding: 2px 8px; border-radius: 4px; }
.bc-title { font-size: 15px; font-weight: 800; color: #1e293b; line-height: 1.3; }
.bc-mon { font-size: 12px; color: #64748b; }
.bc-info-row { display: flex; gap: 14px; }
.bc-info-item { display: flex; align-items: center; gap: 5px; font-size: 12px; color: #475569; }
.bi-icon { font-size: 14px; }
.bc-time { background: #f8fafc; border-radius: 8px; padding: 8px 10px; font-size: 12px; color: #475569; display: flex; flex-direction: column; gap: 3px; }
.countdown { display: flex; align-items: center; gap: 6px; font-size: 12px; color: #c2410c; background: #fff7ed; border-radius: 6px; padding: 6px 10px; border: 1px solid #fed7aa; }
.cd-icon { font-size: 14px; }
.diem-tag { font-size: 13px; font-weight: 800; padding: 3px 10px; border-radius: 20px; }
.diem-dat { color: #15803d; background: #dcfce7; }
.diem-khong-dat { color: #b91c1c; background: #fee2e2; }
.lms-badge { display: inline-flex; align-items: center; padding: 2px 8px; border-radius: 20px; font-size: 10px; font-weight: 700; }
.badge-gray { background: #f1f5f9; color: #64748b; }
.badge-green { background: #dcfce7; color: #15803d; }
.badge-blue { background: #dbeafe; color: #1d4ed8; }
.badge-orange { background: #fff7ed; color: #c2410c; }
.badge-indigo { background: #e0e7ff; color: #4338ca; }
.badge-purple { background: #f3e8ff; color: #7c3aed; }
.badge-red { background: #fee2e2; color: #b91c1c; }
.bc-actions { margin-top: 4px; }
.full-btn { width: 100%; justify-content: center; padding: 10px; }
.disabled-btn { opacity: 0.6; cursor: not-allowed; }
.lms-btn { display: inline-flex; align-items: center; gap: 6px; padding: 8px 16px; border: 1px solid #e2e8f0; border-radius: 8px; background: #fff; color: #475569; font-size: 13px; font-weight: 600; cursor: pointer; transition: all 0.15s; }
.lms-btn.primary { background: #1d4ed8; color: #fff; border-color: #1d4ed8; }
.lms-btn.primary:hover { background: #1e40af; }
@media (max-width: 1100px) { .card-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 700px) { .card-grid { grid-template-columns: 1fr; } }
</style>
