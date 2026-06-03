<template>
  <section class="lms-page">
    <div class="lp-head">
      <div>
        <h1 class="lp-h1">Quản lý bài tập lớp</h1>
        <p class="lp-sub">Danh sách bài đã giao cho lớp học phần</p>
      </div>
      <RouterLink :to="{ name: 'Lms.GiaoBai' }" class="lms-btn primary">+ Giao bài mới</RouterLink>
    </div>

    <div class="filter-card">
      <div class="filter-grid">
        <label class="fl"><span>Môn học</span>
          <input v-model.trim="loc.tenMonHoc" class="fi" placeholder="Tên môn học..." />
        </label>
        <label class="fl"><span>Lớp học phần</span>
          <input v-model.trim="loc.maLop" class="fi" placeholder="Mã lớp..." />
        </label>
        <label class="fl"><span>Trạng thái</span>
          <select v-model="loc.trangThai" class="fi">
            <option value="">Tất cả</option>
            <option v-for="(label, val) in TRANG_THAI_BTL" :key="val" :value="val">{{ label }}</option>
          </select>
        </label>
        <label class="fl"><span>Từ khóa</span>
          <input v-model.trim="loc.keyword" class="fi" placeholder="Tên bài tập..." />
        </label>
      </div>
      <div class="filter-footer">
        <button class="lms-btn ghost" @click="datLaiLoc">↺ Đặt lại</button>
        <span class="result-count">{{ danhSachHienThi.length }} bài</span>
      </div>
    </div>

    <div class="table-card">
      <div class="table-wrap">
        <table class="lms-table">
          <thead>
            <tr>
              <th>Tên bài</th>
              <th>Môn học</th>
              <th>Lớp HP</th>
              <th>Thời gian mở</th>
              <th>Thời gian đóng</th>
              <th>Đã nộp</th>
              <th>Cách tính điểm</th>
              <th>Trạng thái</th>
              <th style="width:200px">Thao tác</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="dangTai"><td colspan="9" class="empty-cell">⏳ Đang tải...</td></tr>
            <tr v-else-if="danhSachHienThi.length === 0"><td colspan="9" class="empty-cell">📭 Chưa có bài tập lớp nào</td></tr>
            <tr v-for="item in danhSachHienThi" :key="item.id">
              <td><div class="q-content">{{ item.tenBaiTap }}</div></td>
              <td>{{ item.tenMonHoc }}</td>
              <td><span class="lms-badge badge-indigo">{{ item.maLop }}</span></td>
              <td class="nowrap small-text">{{ fmt(item.thoiGianMo) }}</td>
              <td class="nowrap small-text">{{ fmt(item.thoiGianDong) }}</td>
              <td>
                <div class="progress-wrap">
                  <span>{{ item.soSinhVienNop }}/{{ item.tongSinhVien }}</span>
                  <div class="prog-bar"><div class="prog-fill" :style="{ width: pct(item.soSinhVienNop, item.tongSinhVien) }"></div></div>
                </div>
              </td>
              <td class="small-text">{{ CACH_TINH_DIEM[item.cachTinhDiem] || '—' }}</td>
              <td><span :class="['lms-badge', mauTT(item.trangThai)]">{{ TRANG_THAI_BTL[item.trangThai] }}</span></td>
              <td>
                <div class="row-actions">
                  <RouterLink :to="{ name: 'Lms.DanhSachNop', params: { id: item.id } }" class="act-btn view">Nộp bài</RouterLink>
                  <RouterLink :to="{ name: 'Lms.KetQua', params: { id: item.id } }" class="act-btn result">Kết quả</RouterLink>
                  <button class="act-btn more" @click="moMenu(item)">▾</button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Dropdown actions menu -->
    <div v-if="menuItem" class="action-modal-overlay" @click.self="menuItem = null">
      <div class="action-modal">
        <div class="action-modal-title">Thao tác: <b>{{ menuItem.tenBaiTap }}</b></div>
        <div class="action-list">
          <button class="action-item" @click="thaoTac('gia-han')">📅 Gia hạn thời gian</button>
          <button class="action-item" @click="thaoTac('doi-lan')">🔄 Đổi số lần làm</button>
          <button v-if="menuItem.trangThai === 'dang_mo'" class="action-item orange" @click="xacNhan('tam_dong')">⏸ Tạm đóng</button>
          <button v-if="menuItem.trangThai === 'tam_dong'" class="action-item green" @click="xacNhan('mo_lai')">▶ Mở lại</button>
          <button v-if="canChot" class="action-item indigo" @click="xacNhan('chot')">✓ Chốt điểm</button>
          <button v-if="menuItem.trangThai === 'da_chot'" class="action-item blue" @click="xacNhan('mo_chot')">🔓 Mở chốt</button>
          <button v-if="menuItem.trangThai === 'da_chot'" class="action-item purple" @click="dayDiem">📤 Đẩy sang bảng điểm</button>
          <RouterLink v-if="menuItem.trangThai === 'da_chot' || menuItem.trangThai === 'da_dong'" :to="{ name: 'Lms.DayDiem', params: { id: menuItem.id } }" class="action-item purple">📊 Đẩy điểm</RouterLink>
          <RouterLink :to="{ name: 'Lms.LichSu' }" class="action-item">🕐 Lịch sử</RouterLink>
        </div>
        <button class="lms-btn ghost w-full mt-8" @click="menuItem = null">Đóng</button>
      </div>
    </div>

    <!-- Confirm dialog -->
    <div v-if="confirmAction" class="modal-overlay" @click.self="confirmAction = null">
      <div class="modal-box">
        <h3>{{ confirmAction.title }}</h3>
        <p>{{ confirmAction.message }}</p>
        <div v-if="confirmAction.type === 'gia-han' || confirmAction.type === 'doi-lan'" class="form-extra">
          <template v-if="confirmAction.type === 'gia-han'">
            <label class="fl">
              <span>Thời gian đóng mới</span>
              <input v-model="confirmAction.value" type="datetime-local" class="fi" />
            </label>
          </template>
          <template v-else-if="confirmAction.type === 'doi-lan'">
            <label class="fl">
              <span>Số lần làm mới</span>
              <input v-model.number="confirmAction.value" type="number" min="1" class="fi" />
            </label>
          </template>
        </div>
        <div class="modal-actions">
          <button class="lms-btn ghost" @click="confirmAction = null">Hủy</button>
          <button class="lms-btn primary" @click="thucHienThaoTac" :disabled="dangXuLy">
            {{ dangXuLy ? 'Đang xử lý...' : 'Xác nhận' }}
          </button>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { baiTapLopService } from '../services/lmsService'
import { TRANG_THAI_BAI_TAP_LOP, CACH_TINH_DIEM, mauTrangThaiBaiTapLop, formatDateTime } from '../services/lmsEnum'

const TRANG_THAI_BTL = TRANG_THAI_BAI_TAP_LOP
const dangTai = ref(true), dangXuLy = ref(false)
const danhSach = ref([]), menuItem = ref(null), confirmAction = ref(null)
const loc = ref({ tenMonHoc: '', maLop: '', trangThai: '', keyword: '' })

const danhSachHienThi = computed(() => danhSach.value.filter(item => {
  if (loc.value.trangThai && item.trangThai !== loc.value.trangThai) return false
  if (loc.value.maLop && !item.maLop?.toLowerCase().includes(loc.value.maLop.toLowerCase())) return false
  if (loc.value.tenMonHoc && !item.tenMonHoc?.toLowerCase().includes(loc.value.tenMonHoc.toLowerCase())) return false
  if (loc.value.keyword && !item.tenBaiTap?.toLowerCase().includes(loc.value.keyword.toLowerCase())) return false
  return true
}))
const canChot = computed(() => menuItem.value && ['da_dong', 'tam_dong'].includes(menuItem.value.trangThai))

function mauTT(tt) { return mauTrangThaiBaiTapLop(tt) }
function fmt(dt) { return formatDateTime(dt) }
function pct(a, b) { return b ? Math.round((a / b) * 100) + '%' : '0%' }
function datLaiLoc() { loc.value = { tenMonHoc: '', maLop: '', trangThai: '', keyword: '' } }
function moMenu(item) { menuItem.value = item }
function dayDiem() { menuItem.value = null }

function xacNhan(type) {
  const item = menuItem.value
  const titles = { tam_dong: '⏸ Tạm đóng bài', mo_lai: '▶ Mở lại bài', chot: '✓ Chốt điểm', mo_chot: '🔓 Mở chốt điểm' }
  const messages = {
    tam_dong: `Tạm đóng bài "${item.tenBaiTap}"? Sinh viên sẽ không thể làm bài.`,
    mo_lai: `Mở lại bài "${item.tenBaiTap}"? Sinh viên sẽ có thể tiếp tục làm bài.`,
    chot: `Chốt điểm bài "${item.tenBaiTap}"? Sau khi chốt có thể đẩy sang bảng điểm.`,
    mo_chot: `Mở chốt bài "${item.tenBaiTap}"? Trạng thái sẽ về đã đóng.`,
  }
  confirmAction.value = { type, item, title: titles[type], message: messages[type], value: null }
  menuItem.value = null
}

function thaoTac(type) {
  const item = menuItem.value
  const titles = { 'gia-han': '📅 Gia hạn thời gian', 'doi-lan': '🔄 Đổi số lần làm' }
  const messages = { 'gia-han': 'Chọn thời gian đóng mới:', 'doi-lan': 'Nhập số lần làm mới:' }
  confirmAction.value = { type, item, title: titles[type], message: messages[type], value: type === 'doi-lan' ? item.soLanLamToiDa : '' }
  menuItem.value = null
}

async function thucHienThaoTac() {
  const { type, item, value } = confirmAction.value
  dangXuLy.value = true
  try {
    if (type === 'tam_dong') await baiTapLopService.tamDong(item.id)
    else if (type === 'mo_lai') await baiTapLopService.moLai(item.id)
    else if (type === 'chot') await baiTapLopService.chotDiem(item.id)
    else if (type === 'mo_chot') await baiTapLopService.moChot(item.id)
    else if (type === 'gia-han') await baiTapLopService.giaHan(item.id, { thoiGianDong: value })
    else if (type === 'doi-lan') await baiTapLopService.doiSoLanLam(item.id, { soLanLamToiDa: value })
    confirmAction.value = null
    await taiDuLieu()
  } finally { dangXuLy.value = false }
}

async function taiDuLieu() {
  dangTai.value = true
  try {
    const res = await baiTapLopService.layDanhSach()
    danhSach.value = res.content || []
  } finally { dangTai.value = false }
}
onMounted(taiDuLieu)
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;600;700;800&display=swap');
*, *::before, *::after { box-sizing: border-box; font-family: 'Roboto', sans-serif; }
.lms-page { display: flex; flex-direction: column; gap: 18px; }
.lp-head { display: flex; justify-content: space-between; align-items: flex-start; }
.lp-h1 { margin: 0 0 4px; font-size: 22px; font-weight: 800; color: #1e293b; }
.lp-sub { margin: 0; color: #64748b; font-size: 13px; }
.filter-card { background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; padding: 16px 18px; }
.filter-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 12px; }
.fl { display: flex; flex-direction: column; gap: 4px; font-size: 12px; font-weight: 600; color: #475569; }
.fi { border: 1px solid #e2e8f0; border-radius: 7px; padding: 7px 10px; font-size: 13px; color: #1e293b; outline: none; font-family: inherit; }
.fi:focus { border-color: #1d4ed8; }
.filter-footer { display: flex; align-items: center; gap: 10px; margin-top: 12px; padding-top: 12px; border-top: 1px solid #f1f5f9; }
.result-count { margin-left: auto; font-size: 12px; color: #64748b; font-weight: 600; }
.table-card { background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; overflow: hidden; }
.table-wrap { overflow-x: auto; }
.lms-table { width: 100%; border-collapse: collapse; font-size: 13px; }
.lms-table th { background: #f8fafc; padding: 10px 12px; text-align: left; font-weight: 700; color: #475569; font-size: 12px; border-bottom: 2px solid #e2e8f0; }
.lms-table td { padding: 10px 12px; border-bottom: 1px solid #f1f5f9; vertical-align: middle; }
.lms-table tr:last-child td { border-bottom: none; }
.lms-table tr:hover td { background: #f8fafc; }
.empty-cell { text-align: center; color: #94a3b8; padding: 32px !important; }
.nowrap { white-space: nowrap; }
.small-text { font-size: 12px; color: #64748b; }
.q-content { font-weight: 600; max-width: 200px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.progress-wrap { display: flex; flex-direction: column; gap: 3px; }
.prog-bar { height: 5px; background: #e2e8f0; border-radius: 3px; width: 70px; }
.prog-fill { height: 100%; background: #1d4ed8; border-radius: 3px; }
.lms-badge { display: inline-flex; align-items: center; padding: 3px 10px; border-radius: 20px; font-size: 11px; font-weight: 700; }
.badge-indigo { background: #e0e7ff; color: #4338ca; }
.badge-gray { background: #f1f5f9; color: #64748b; }
.badge-green { background: #dcfce7; color: #15803d; }
.badge-blue { background: #dbeafe; color: #1d4ed8; }
.badge-orange { background: #fff7ed; color: #c2410c; }
.badge-purple { background: #f3e8ff; color: #7c3aed; }
.badge-red { background: #fee2e2; color: #b91c1c; }
.row-actions { display: flex; gap: 5px; }
.act-btn { padding: 4px 10px; border-radius: 6px; font-size: 12px; font-weight: 600; cursor: pointer; border: 1px solid; text-decoration: none; display: inline-block; }
.act-btn.view { background: #eff6ff; color: #1d4ed8; border-color: #bfdbfe; }
.act-btn.result { background: #f0fdf4; color: #15803d; border-color: #bbf7d0; }
.act-btn.more { background: #f8fafc; color: #475569; border-color: #e2e8f0; }
.action-modal-overlay, .modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.45); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.action-modal, .modal-box { background: #fff; border-radius: 14px; padding: 24px; max-width: 400px; width: 90%; box-shadow: 0 20px 60px rgba(0,0,0,0.2); }
.action-modal-title, .modal-box h3 { font-size: 15px; font-weight: 800; color: #1e293b; margin: 0 0 14px; padding-bottom: 10px; border-bottom: 1px solid #f1f5f9; }
.action-list { display: flex; flex-direction: column; gap: 6px; }
.action-item { padding: 10px 14px; border-radius: 8px; font-size: 13px; font-weight: 600; cursor: pointer; border: 1px solid #e2e8f0; background: #f8fafc; color: #334155; text-align: left; text-decoration: none; display: block; }
.action-item:hover { background: #eff6ff; border-color: #bfdbfe; color: #1d4ed8; }
.action-item.orange { color: #c2410c; background: #fff7ed; border-color: #fed7aa; }
.action-item.green { color: #15803d; background: #f0fdf4; border-color: #bbf7d0; }
.action-item.indigo { color: #4338ca; background: #eef2ff; border-color: #c7d2fe; }
.action-item.blue { color: #1d4ed8; background: #eff6ff; border-color: #bfdbfe; }
.action-item.purple { color: #7c3aed; background: #faf5ff; border-color: #e9d5ff; }
.modal-box p { color: #475569; font-size: 14px; margin: 0 0 16px; line-height: 1.6; }
.form-extra { margin-bottom: 16px; }
.modal-actions { display: flex; gap: 10px; justify-content: flex-end; }
.lms-btn { display: inline-flex; align-items: center; gap: 6px; padding: 8px 16px; border: 1px solid #e2e8f0; border-radius: 8px; background: #fff; color: #475569; font-size: 13px; font-weight: 600; cursor: pointer; text-decoration: none; transition: all 0.15s; }
.lms-btn:disabled { opacity: 0.6; cursor: not-allowed; }
.lms-btn.primary { background: #1d4ed8; color: #fff; border-color: #1d4ed8; }
.lms-btn.ghost { background: transparent; border-color: transparent; color: #64748b; }
.lms-btn.ghost:hover { background: #f1f5f9; }
.w-full { width: 100%; justify-content: center; }
.mt-8 { margin-top: 8px; }
@media (max-width: 900px) { .filter-grid { grid-template-columns: 1fr 1fr; } }
</style>
