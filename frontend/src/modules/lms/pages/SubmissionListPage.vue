<template>
  <section class="lms-page">
    <div class="lp-head">
      <button class="lms-btn ghost" @click="$router.back()">← Quay lại</button>
      <div>
        <h1 class="lp-h1">Danh sách nộp bài</h1>
        <p class="lp-sub" v-if="baiTapLop">{{ baiTapLop.tenBaiTap }} – {{ baiTapLop.maLop }}</p>
      </div>
      <div class="btn-group">
        <button class="lms-btn" @click="xuatExcel">↓ Xuất Excel</button>
        <RouterLink v-if="baiTapLop" :to="{ name: 'Lms.KetQua', params: { id: $route.params.id } }" class="lms-btn primary">📊 Xem kết quả</RouterLink>
      </div>
    </div>

    <!-- Thống kê nhanh -->
    <div class="stat-row" v-if="baiTapLop">
      <div class="mini-stat blue">
        <div class="ms-num">{{ baiTapLop.soSinhVienNop || danhSach.filter(d => d.soLanLam > 0).length }}</div>
        <div class="ms-label">Đã nộp</div>
      </div>
      <div class="mini-stat gray">
        <div class="ms-num">{{ danhSach.filter(d => !d.soLanLam).length }}</div>
        <div class="ms-label">Chưa nộp</div>
      </div>
      <div class="mini-stat orange">
        <div class="ms-num">{{ danhSach.filter(d => d.trangThai === 'chua_cham').length }}</div>
        <div class="ms-label">Chưa chấm TL</div>
      </div>
      <div class="mini-stat green">
        <div class="ms-num">{{ danhSach.filter(d => d.trangThai === 'da_cham').length }}</div>
        <div class="ms-label">Đã chấm</div>
      </div>
    </div>

    <div class="filter-card">
      <div class="filter-row">
        <label class="fl">
          <span>Trạng thái</span>
          <select v-model="loc.trangThai" class="fi">
            <option value="">Tất cả</option>
            <option value="chua_nop">Chưa nộp</option>
            <option value="chua_cham">Chưa chấm TL</option>
            <option value="da_cham">Đã chấm</option>
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
              <th>Sinh viên</th>
              <th>Mã SV</th>
              <th>Lớp</th>
              <th class="center">Số lần làm</th>
              <th>Lần được chọn</th>
              <th>Thời gian nộp</th>
              <th class="center">Điểm TĐ</th>
              <th class="center">Điểm GV</th>
              <th class="center">Điểm CC</th>
              <th>Trạng thái</th>
              <th style="width:120px">Thao tác</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="dangTai"><td colspan="11" class="empty-cell">⏳ Đang tải...</td></tr>
            <tr v-else-if="danhSachHienThi.length === 0"><td colspan="11" class="empty-cell">📭 Không có dữ liệu</td></tr>
            <tr v-for="item in danhSachHienThi" :key="item.sinhVienId" :class="{ 'row-chua-nop': !item.soLanLam }">
              <td><b>{{ item.hoTen }}</b></td>
              <td class="small-text">{{ item.maSV }}</td>
              <td class="small-text">{{ item.lop }}</td>
              <td class="center">{{ item.soLanLam || 0 }}</td>
              <td class="center small-text">{{ item.lanDuocChon || '—' }}</td>
              <td class="nowrap small-text">{{ item.thoiGianNopGanNhat ? fmt(item.thoiGianNopGanNhat) : '—' }}</td>
              <td class="center">
                <span v-if="item.diemTuDong !== null" :class="['diem-badge', diemClass(item.diemTuDong)]">{{ fmtD(item.diemTuDong) }}</span>
                <span v-else class="text-gray">—</span>
              </td>
              <td class="center">
                <span v-if="item.diemGiaoVien !== null" class="diem-badge diem-gv">{{ fmtD(item.diemGiaoVien) }}</span>
                <span v-else class="text-gray">—</span>
              </td>
              <td class="center">
                <b v-if="item.diemCuoiCung !== null" :class="['diem-final', diemClass(item.diemCuoiCung)]">{{ fmtD(item.diemCuoiCung) }}</b>
                <span v-else class="text-gray">—</span>
              </td>
              <td>
                <span v-if="!item.soLanLam" class="lms-badge badge-gray">Chưa nộp</span>
                <span v-else-if="item.trangThai === 'chua_cham'" class="lms-badge badge-orange">Chưa chấm TL</span>
                <span v-else-if="item.trangThai === 'da_cham'" class="lms-badge badge-green">Đã chấm</span>
                <span v-else class="lms-badge badge-blue">{{ item.trangThai }}</span>
              </td>
              <td>
                <div v-if="item.soLanLam" class="row-actions">
                  <button class="act-btn view" @click="xemChiTiet(item)">Xem</button>
                  <button v-if="item.trangThai === 'chua_cham'" class="act-btn grade" @click="cham(item)">Chấm</button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Chi tiết lần làm -->
    <div v-if="chiTiet" class="modal-overlay" @click.self="chiTiet = null">
      <div class="detail-modal">
        <div class="dm-head">
          <div>
            <h3>Chi tiết bài làm: {{ chiTiet.hoTen }}</h3>
            <p>Điểm: <b>{{ fmtD(chiTiet.diemCuoiCung) }}</b> · Nộp lúc: {{ fmt(chiTiet.thoiGianNopGanNhat) }}</p>
          </div>
          <button class="lms-btn ghost" @click="chiTiet = null">✕ Đóng</button>
        </div>
        <div class="dm-body">
          <p class="hint">Xem chi tiết từng câu trả lời của sinh viên trong trang chấm bài.</p>
          <RouterLink :to="{ name: 'Lms.ChamBai', params: { id: chiTiet.lanLamBaiId } }" class="lms-btn primary">✍️ Mở trang chấm bài</RouterLink>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { giaoVienLmsService, baiTapLopService } from '../services/lmsService'
import { formatDateTime, formatDiem } from '../services/lmsEnum'

const route = useRoute()
const dangTai = ref(true)
const danhSach = ref([])
const baiTapLop = ref(null)
const chiTiet = ref(null)
const loc = ref({ trangThai: '', keyword: '' })

const danhSachHienThi = computed(() => danhSach.value.filter(item => {
  if (loc.value.trangThai) {
    if (loc.value.trangThai === 'chua_nop' && item.soLanLam) return false
    if (loc.value.trangThai !== 'chua_nop' && (!item.soLanLam || item.trangThai !== loc.value.trangThai)) return false
  }
  if (loc.value.keyword) {
    const kw = loc.value.keyword.toLowerCase()
    if (!item.hoTen?.toLowerCase().includes(kw) && !item.maSV?.toLowerCase().includes(kw)) return false
  }
  return true
}))

function fmt(dt) { return formatDateTime(dt) }
function fmtD(d) { return formatDiem(d) }
function diemClass(d) { return d >= 5 ? 'diem-dat' : 'diem-khong-dat' }
function xuatExcel() { alert('Chức năng xuất Excel đang phát triển') }
function xemChiTiet(item) { chiTiet.value = { ...item, lanLamBaiId: item.lanLamBaiId || item.sinhVienId } }
function cham(item) {
  chiTiet.value = { ...item, lanLamBaiId: item.lanLamBaiId || item.sinhVienId }
}

onMounted(async () => {
  dangTai.value = true
  try {
    const [btl, ds] = await Promise.all([
      baiTapLopService.layTheoId(route.params.id),
      giaoVienLmsService.layDanhSachNop(route.params.id)
    ])
    baiTapLop.value = btl
    danhSach.value = ds.content || ds
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
.btn-group { display: flex; gap: 8px; }
.stat-row { display: flex; gap: 12px; }
.mini-stat { flex: 1; background: #fff; border: 1px solid #e2e8f0; border-radius: 10px; padding: 12px 16px; text-align: center; }
.ms-num { font-size: 26px; font-weight: 800; }
.ms-label { font-size: 11px; font-weight: 600; margin-top: 3px; color: #64748b; }
.mini-stat.blue { border-color: #bfdbfe; }
.mini-stat.blue .ms-num { color: #1d4ed8; }
.mini-stat.gray .ms-num { color: #64748b; }
.mini-stat.orange .ms-num { color: #c2410c; }
.mini-stat.green .ms-num { color: #15803d; }
.filter-card { background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; padding: 14px 16px; }
.filter-row { display: flex; gap: 12px; }
.fl { display: flex; flex-direction: column; gap: 4px; font-size: 12px; font-weight: 600; color: #475569; min-width: 160px; }
.fl.flex-1 { flex: 1; min-width: 0; }
.fi { border: 1px solid #e2e8f0; border-radius: 7px; padding: 7px 10px; font-size: 13px; color: #1e293b; outline: none; font-family: inherit; }
.fi:focus { border-color: #1d4ed8; }
.table-card { background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; overflow: hidden; }
.table-wrap { overflow-x: auto; }
.lms-table { width: 100%; border-collapse: collapse; font-size: 13px; }
.lms-table th { background: #f8fafc; padding: 9px 12px; text-align: left; font-weight: 700; color: #475569; font-size: 12px; border-bottom: 2px solid #e2e8f0; }
.lms-table td { padding: 9px 12px; border-bottom: 1px solid #f1f5f9; vertical-align: middle; }
.lms-table tr:last-child td { border-bottom: none; }
.lms-table tr:hover td { background: #f8fafc; }
.lms-table tr.row-chua-nop td { background: #fafafa; color: #94a3b8; }
.empty-cell { text-align: center; color: #94a3b8; padding: 32px !important; }
.center { text-align: center; }
.nowrap { white-space: nowrap; }
.small-text { font-size: 12px; color: #64748b; }
.text-gray { color: #94a3b8; }
.diem-badge { display: inline-block; padding: 2px 8px; border-radius: 6px; font-size: 12px; font-weight: 700; }
.diem-final { font-size: 14px; font-weight: 800; }
.diem-dat { color: #15803d; background: #dcfce7; }
.diem-khong-dat { color: #b91c1c; background: #fee2e2; }
.diem-gv { color: #4338ca; background: #e0e7ff; }
.lms-badge { display: inline-flex; align-items: center; padding: 3px 10px; border-radius: 20px; font-size: 11px; font-weight: 700; }
.badge-gray { background: #f1f5f9; color: #64748b; }
.badge-green { background: #dcfce7; color: #15803d; }
.badge-blue { background: #dbeafe; color: #1d4ed8; }
.badge-orange { background: #fff7ed; color: #c2410c; }
.row-actions { display: flex; gap: 5px; }
.act-btn { padding: 4px 10px; border-radius: 6px; font-size: 12px; font-weight: 600; cursor: pointer; border: 1px solid; }
.act-btn.view { background: #eff6ff; color: #1d4ed8; border-color: #bfdbfe; }
.act-btn.grade { background: #f0fdf4; color: #15803d; border-color: #bbf7d0; }
.modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.45); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.detail-modal { background: #fff; border-radius: 14px; padding: 24px; max-width: 500px; width: 90%; box-shadow: 0 20px 60px rgba(0,0,0,0.2); }
.dm-head { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 16px; gap: 12px; }
.dm-head h3 { margin: 0 0 4px; font-size: 16px; font-weight: 800; }
.dm-head p { margin: 0; color: #64748b; font-size: 13px; }
.dm-body { padding-top: 12px; border-top: 1px solid #f1f5f9; display: flex; flex-direction: column; gap: 10px; align-items: flex-start; }
.hint { font-size: 13px; color: #64748b; margin: 0; }
.lms-btn { display: inline-flex; align-items: center; gap: 6px; padding: 8px 16px; border: 1px solid #e2e8f0; border-radius: 8px; background: #fff; color: #475569; font-size: 13px; font-weight: 600; cursor: pointer; text-decoration: none; transition: all 0.15s; }
.lms-btn.primary { background: #1d4ed8; color: #fff; border-color: #1d4ed8; }
.lms-btn.ghost { background: transparent; border-color: transparent; color: #64748b; }
.lms-btn.ghost:hover { background: #f1f5f9; }
@media (max-width: 768px) { .stat-row { flex-wrap: wrap; } .mini-stat { min-width: 120px; } }
</style>
