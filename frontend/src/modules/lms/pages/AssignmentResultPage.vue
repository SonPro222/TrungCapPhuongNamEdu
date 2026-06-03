<template>
  <section class="lms-page">
    <div class="lp-head">
      <button class="lms-btn ghost" @click="$router.back()">← Quay lại</button>
      <div>
        <h1 class="lp-h1">Kết quả bài tập lớp</h1>
        <p class="lp-sub" v-if="baiTapLop">{{ baiTapLop.tenBaiTap }} – {{ baiTapLop.maLop }}</p>
      </div>
      <div class="btn-group">
        <button class="lms-btn" @click="tinhLaiKetQua" :disabled="dangXuLy">↻ Tính lại</button>
        <button class="lms-btn" @click="xuatExcel">↓ Xuất Excel</button>
        <button v-if="baiTapLop?.trangThai !== 'da_chot' && baiTapLop?.trangThai !== 'da_day_sang_diem'" class="lms-btn primary" @click="xacNhanChot">✓ Chốt điểm</button>
        <button v-else-if="baiTapLop?.trangThai === 'da_chot'" class="lms-btn purple" @click="dayDiem">📤 Đẩy sang bảng điểm</button>
      </div>
    </div>

    <!-- Thống kê -->
    <div class="stat-row" v-if="thongKe">
      <div class="mini-stat blue"><div class="ms-num">{{ thongKe.tongSV }}</div><div class="ms-label">Tổng SV</div></div>
      <div class="mini-stat green"><div class="ms-num">{{ thongKe.soDat }}</div><div class="ms-label">Đạt</div></div>
      <div class="mini-stat red"><div class="ms-num">{{ thongKe.soKhongDat }}</div><div class="ms-label">Không đạt</div></div>
      <div class="mini-stat indigo"><div class="ms-num">{{ thongKe.diemTB }}</div><div class="ms-label">Điểm TB</div></div>
      <div class="mini-stat orange"><div class="ms-num">{{ thongKe.diemMax }}</div><div class="ms-label">Điểm cao nhất</div></div>
      <div class="mini-stat gray"><div class="ms-num">{{ thongKe.diemMin }}</div><div class="ms-label">Điểm thấp nhất</div></div>
    </div>

    <!-- Trạng thái chốt -->
    <div v-if="baiTapLop" class="status-banner" :class="baiTapLop.trangThai">
      <span v-if="baiTapLop.trangThai === 'da_chot'">✅ Điểm đã chốt lúc {{ fmt(baiTapLop.ngayChot) }}</span>
      <span v-else-if="baiTapLop.trangThai === 'da_day_sang_diem'">🚀 Điểm đã đẩy sang bảng điểm</span>
      <span v-else>⚠️ Điểm chưa chốt – Có thể chỉnh sửa</span>
    </div>

    <div class="filter-card">
      <div class="filter-row">
        <label class="fl">
          <span>Kết quả</span>
          <select v-model="loc.ketQua" class="fi">
            <option value="">Tất cả</option>
            <option value="dat">Đạt</option>
            <option value="khong_dat">Không đạt</option>
          </select>
        </label>
        <label class="fl flex-1">
          <span>Tìm sinh viên</span>
          <input v-model.trim="loc.keyword" class="fi" placeholder="Tên hoặc mã sinh viên..." />
        </label>
      </div>
    </div>

    <div class="table-card">
      <div class="table-wrap">
        <table class="lms-table">
          <thead>
            <tr>
              <th>STT</th>
              <th>Sinh viên</th>
              <th>Mã SV</th>
              <th class="center">Số lần làm</th>
              <th class="center">Lần được chọn</th>
              <th class="center">Điểm hệ thống</th>
              <th class="center">Điểm GV chỉnh</th>
              <th class="center">Điểm cuối cùng</th>
              <th>Kết quả</th>
              <th>Trạng thái</th>
              <th v-if="coTheChinhDiem" style="width:80px">Chỉnh điểm</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="dangTai"><td :colspan="coTheChinhDiem ? 11 : 10" class="empty-cell">⏳ Đang tải...</td></tr>
            <tr v-else-if="danhSachHienThi.length === 0"><td :colspan="coTheChinhDiem ? 11 : 10" class="empty-cell">📭 Chưa có kết quả</td></tr>
            <tr v-for="(item, idx) in danhSachHienThi" :key="item.id">
              <td class="center small-text">{{ idx + 1 }}</td>
              <td><b>{{ item.hoTen }}</b></td>
              <td class="small-text">{{ item.maSV }}</td>
              <td class="center">{{ item.soLanLam }}</td>
              <td class="center small-text">Lần {{ item.lanDuocChon }}</td>
              <td class="center">
                <span :class="['diem-badge', diemClass(item.diemHeThong)]">{{ fmtD(item.diemHeThong) }}</span>
              </td>
              <td class="center">
                <span v-if="item.diemGiaoVienChinhSua !== null" class="diem-badge diem-gv">{{ fmtD(item.diemGiaoVienChinhSua) }}</span>
                <span v-else class="text-gray">—</span>
              </td>
              <td class="center">
                <b :class="['diem-final', diemClass(item.diemCuoiCung)]">{{ fmtD(item.diemCuoiCung) }}</b>
              </td>
              <td>
                <span :class="['lms-badge', item.ketQua === 'dat' ? 'badge-green' : 'badge-red']">
                  {{ item.ketQua === 'dat' ? '✓ Đạt' : '✗ Không đạt' }}
                </span>
              </td>
              <td><span :class="['lms-badge', item.trangThai === 'da_chot' ? 'badge-indigo' : 'badge-gray']">{{ item.trangThai === 'da_chot' ? 'Đã chốt' : 'Tạm tính' }}</span></td>
              <td v-if="coTheChinhDiem">
                <button class="act-btn edit" @click="moChinhDiem(item)">Chỉnh</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Modal chỉnh điểm -->
    <div v-if="chinhDiemItem" class="modal-overlay" @click.self="chinhDiemItem = null">
      <div class="modal-box">
        <h3>📝 Chỉnh điểm: {{ chinhDiemItem.hoTen }}</h3>
        <p>Điểm hệ thống: <b>{{ fmtD(chinhDiemItem.diemHeThong) }}</b> / Điểm cuối: <b>{{ fmtD(chinhDiemItem.diemCuoiCung) }}</b></p>
        <label class="fl mb-10">
          <span>Điểm mới</span>
          <input v-model.number="diemMoi" type="number" min="0" max="10" step="0.5" class="fi" />
        </label>
        <label class="fl mb-14">
          <span>Lý do chỉnh điểm</span>
          <textarea v-model="lyDoChinhDiem" class="fi ta" rows="2" placeholder="Lý do..."></textarea>
        </label>
        <div class="modal-actions">
          <button class="lms-btn ghost" @click="chinhDiemItem = null">Hủy</button>
          <button class="lms-btn primary" @click="luuChinhDiem" :disabled="dangXuLy">
            {{ dangXuLy ? '...' : 'Lưu' }}
          </button>
        </div>
      </div>
    </div>

    <!-- Confirm chốt -->
    <div v-if="showConfirmChot" class="modal-overlay" @click.self="showConfirmChot = false">
      <div class="modal-box">
        <h3>✓ Xác nhận chốt điểm</h3>
        <p>Sau khi chốt, điểm sẽ được khóa. Bạn vẫn có thể mở chốt lại nếu cần. Tiếp tục?</p>
        <div class="modal-actions">
          <button class="lms-btn ghost" @click="showConfirmChot = false">Hủy</button>
          <button class="lms-btn primary" @click="thucHienChot" :disabled="dangXuLy">Chốt điểm</button>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ketQuaService, baiTapLopService } from '../services/lmsService'
import { formatDateTime, formatDiem } from '../services/lmsEnum'

const route = useRoute(), router = useRouter()
const dangTai = ref(true), dangXuLy = ref(false)
const danhSach = ref([]), baiTapLop = ref(null)
const chinhDiemItem = ref(null), diemMoi = ref(null), lyDoChinhDiem = ref('')
const showConfirmChot = ref(false)
const loc = ref({ ketQua: '', keyword: '' })

const coTheChinhDiem = computed(() => baiTapLop.value?.trangThai !== 'da_day_sang_diem')
const danhSachHienThi = computed(() => danhSach.value.filter(item => {
  if (loc.value.ketQua && item.ketQua !== loc.value.ketQua) return false
  if (loc.value.keyword) {
    const kw = loc.value.keyword.toLowerCase()
    if (!item.hoTen?.toLowerCase().includes(kw) && !item.maSV?.toLowerCase().includes(kw)) return false
  }
  return true
}))
const thongKe = computed(() => {
  if (!danhSach.value.length) return null
  const diems = danhSach.value.filter(i => i.diemCuoiCung !== null).map(i => i.diemCuoiCung)
  return {
    tongSV: danhSach.value.length,
    soDat: danhSach.value.filter(i => i.ketQua === 'dat').length,
    soKhongDat: danhSach.value.filter(i => i.ketQua === 'khong_dat').length,
    diemTB: diems.length ? (diems.reduce((a, b) => a + b, 0) / diems.length).toFixed(1) : '—',
    diemMax: diems.length ? Math.max(...diems).toFixed(1) : '—',
    diemMin: diems.length ? Math.min(...diems).toFixed(1) : '—',
  }
})

function fmt(dt) { return formatDateTime(dt) }
function fmtD(d) { return formatDiem(d) }
function diemClass(d) { return d !== null ? (d >= 5 ? 'diem-dat' : 'diem-khong-dat') : '' }
function xuatExcel() { alert('Chức năng xuất Excel đang phát triển') }
function xacNhanChot() { showConfirmChot.value = true }
function moChinhDiem(item) { chinhDiemItem.value = item; diemMoi.value = item.diemCuoiCung; lyDoChinhDiem.value = '' }
function dayDiem() { router.push({ name: 'Lms.DayDiem', params: { id: route.params.id } }) }

async function tinhLaiKetQua() {
  dangXuLy.value = true
  try { await ketQuaService.tinhLai(route.params.id); await taiDuLieu() }
  finally { dangXuLy.value = false }
}

async function luuChinhDiem() {
  dangXuLy.value = true
  try {
    await ketQuaService.chinhDiem(chinhDiemItem.value.id, { diemMoi: diemMoi.value, lyDo: lyDoChinhDiem.value })
    chinhDiemItem.value = null
    await taiDuLieu()
  } finally { dangXuLy.value = false }
}

async function thucHienChot() {
  dangXuLy.value = true
  try {
    await baiTapLopService.chotDiem(route.params.id)
    showConfirmChot.value = false
    baiTapLop.value = await baiTapLopService.layTheoId(route.params.id)
  } finally { dangXuLy.value = false }
}

async function taiDuLieu() {
  dangTai.value = true
  try {
    const [btl, res] = await Promise.all([
      baiTapLopService.layTheoId(route.params.id),
      ketQuaService.layTheoLop(route.params.id)
    ])
    baiTapLop.value = btl
    danhSach.value = res.content || res
  } finally { dangTai.value = false }
}
onMounted(taiDuLieu)
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;600;700;800&display=swap');
*, *::before, *::after { box-sizing: border-box; font-family: 'Roboto', sans-serif; }
.lms-page { display: flex; flex-direction: column; gap: 18px; }
.lp-head { display: flex; align-items: flex-start; gap: 12px; }
.lp-head > div { flex: 1; }
.lp-h1 { margin: 0 0 4px; font-size: 22px; font-weight: 800; color: #1e293b; }
.lp-sub { margin: 0; color: #64748b; font-size: 13px; }
.btn-group { display: flex; gap: 8px; flex-wrap: wrap; }
.stat-row { display: flex; gap: 10px; }
.mini-stat { flex: 1; background: #fff; border: 1px solid #e2e8f0; border-radius: 10px; padding: 12px; text-align: center; }
.ms-num { font-size: 24px; font-weight: 800; }
.ms-label { font-size: 11px; font-weight: 600; color: #64748b; margin-top: 2px; }
.mini-stat.blue .ms-num { color: #1d4ed8; }
.mini-stat.green .ms-num { color: #15803d; }
.mini-stat.red .ms-num { color: #b91c1c; }
.mini-stat.indigo .ms-num { color: #4338ca; }
.mini-stat.orange .ms-num { color: #c2410c; }
.mini-stat.gray .ms-num { color: #64748b; }
.status-banner { padding: 10px 16px; border-radius: 10px; font-size: 13px; font-weight: 600; }
.status-banner.da_chot { background: #dcfce7; color: #15803d; border: 1px solid #bbf7d0; }
.status-banner.da_day_sang_diem { background: #f3e8ff; color: #7c3aed; border: 1px solid #e9d5ff; }
.status-banner.dang_mo, .status-banner.da_dong { background: #fff7ed; color: #c2410c; border: 1px solid #fed7aa; }
.filter-card { background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; padding: 14px 16px; }
.filter-row { display: flex; gap: 12px; }
.fl { display: flex; flex-direction: column; gap: 4px; font-size: 12px; font-weight: 600; color: #475569; min-width: 140px; }
.fl.flex-1 { flex: 1; min-width: 0; }
.fl.mb-10 { margin-bottom: 10px; }
.fl.mb-14 { margin-bottom: 14px; }
.fi { border: 1px solid #e2e8f0; border-radius: 7px; padding: 7px 10px; font-size: 13px; color: #1e293b; outline: none; font-family: inherit; }
.fi:focus { border-color: #1d4ed8; }
.ta { resize: vertical; min-height: 60px; }
.table-card { background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; overflow: hidden; }
.table-wrap { overflow-x: auto; }
.lms-table { width: 100%; border-collapse: collapse; font-size: 13px; }
.lms-table th { background: #f8fafc; padding: 9px 12px; text-align: left; font-weight: 700; color: #475569; font-size: 12px; border-bottom: 2px solid #e2e8f0; }
.lms-table td { padding: 9px 12px; border-bottom: 1px solid #f1f5f9; vertical-align: middle; }
.lms-table tr:last-child td { border-bottom: none; }
.lms-table tr:hover td { background: #f8fafc; }
.empty-cell { text-align: center; color: #94a3b8; padding: 32px !important; }
.center { text-align: center; }
.small-text { font-size: 12px; color: #64748b; }
.text-gray { color: #94a3b8; }
.diem-badge { display: inline-block; padding: 2px 8px; border-radius: 6px; font-size: 12px; font-weight: 700; }
.diem-final { font-size: 14px; font-weight: 800; }
.diem-dat { color: #15803d; background: #dcfce7; }
.diem-khong-dat { color: #b91c1c; background: #fee2e2; }
.diem-gv { color: #4338ca; background: #e0e7ff; }
.lms-badge { display: inline-flex; align-items: center; padding: 3px 10px; border-radius: 20px; font-size: 11px; font-weight: 700; }
.badge-green { background: #dcfce7; color: #15803d; }
.badge-red { background: #fee2e2; color: #b91c1c; }
.badge-indigo { background: #e0e7ff; color: #4338ca; }
.badge-gray { background: #f1f5f9; color: #64748b; }
.act-btn { padding: 4px 10px; border-radius: 6px; font-size: 12px; font-weight: 600; cursor: pointer; border: 1px solid; }
.act-btn.edit { background: #eff6ff; color: #1d4ed8; border-color: #bfdbfe; }
.modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.45); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal-box { background: #fff; border-radius: 14px; padding: 24px; max-width: 420px; width: 90%; box-shadow: 0 20px 60px rgba(0,0,0,0.2); }
.modal-box h3 { margin: 0 0 10px; font-size: 16px; font-weight: 800; }
.modal-box p { margin: 0 0 16px; color: #475569; font-size: 13px; line-height: 1.6; }
.modal-actions { display: flex; gap: 10px; justify-content: flex-end; }
.lms-btn { display: inline-flex; align-items: center; gap: 6px; padding: 8px 16px; border: 1px solid #e2e8f0; border-radius: 8px; background: #fff; color: #475569; font-size: 13px; font-weight: 600; cursor: pointer; text-decoration: none; transition: all 0.15s; }
.lms-btn:disabled { opacity: 0.6; cursor: not-allowed; }
.lms-btn.primary { background: #1d4ed8; color: #fff; border-color: #1d4ed8; }
.lms-btn.purple { background: #7c3aed; color: #fff; border-color: #7c3aed; }
.lms-btn.ghost { background: transparent; border-color: transparent; color: #64748b; }
.lms-btn.ghost:hover { background: #f1f5f9; }
@media (max-width: 900px) { .stat-row { flex-wrap: wrap; } }
</style>
