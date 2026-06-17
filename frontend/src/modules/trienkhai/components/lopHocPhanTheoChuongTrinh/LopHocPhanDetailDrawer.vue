<template>
  <teleport to="body">
    <transition name="page-slide">
      <div v-if="open" class="detail-page">

        <!-- Header của trang chi tiết -->
        <div class="detail-topbar">
          <button class="back-btn" type="button" @click="$emit('close')">
            ← Quay lại
          </button>
          <div class="topbar-info">
            <span class="topbar-kicker">Chi tiết lớp học phần</span>
            <strong class="topbar-title">{{ lopHocPhan?.tenLop || 'Lớp học phần' }}</strong>
            <span class="topbar-sub">{{ lopHocPhan?.maLop || '—' }} • {{ lopHocPhan?.tenMonHoc || lopHocPhan?.chuongTrinhMon?.tenMonHoc || 'Môn học' }}</span>
          </div>
        </div>

        <!-- Thông tin nhanh -->
        <div class="detail-meta">
          <div class="meta-item">
            <span>Sĩ số</span>
            <strong>{{ lopHocPhan?.soLuongHienTai ?? 0 }}/{{ lopHocPhan?.soLuongToiDa ?? '—' }}</strong>
          </div>
          <div class="meta-item">
            <span>Bắt đầu</span>
            <strong>{{ dinhDangNgay(lopHocPhan?.ngayBatDau) }}</strong>
          </div>
          <div class="meta-item">
            <span>Kết thúc</span>
            <strong>{{ dinhDangNgay(lopHocPhan?.ngayKetThuc) }}</strong>
          </div>
          <div class="meta-item">
            <span>Trạng thái</span>
            <strong :class="'status-' + lopHocPhan?.trangThai">{{ hienThiTrangThai(lopHocPhan?.trangThai) }}</strong>
          </div>
        </div>

        <!-- Tabs -->
        <div class="detail-tabs">
          <button
            v-for="tab in tabs" :key="tab.key"
            type="button"
            :class="['tab-btn', { active: activeTab === tab.key }]"
            @click="activeTab = tab.key"
          >
            {{ tab.label }}
            <span v-if="tab.count !== undefined" class="tab-count">{{ tab.count }}</span>
          </button>
        </div>

        <!-- Nội dung tab -->
        <div class="detail-body">

          <!-- Tổng quan -->
          <div v-if="activeTab === 'tongQuan'" class="overview-grid">
            <div class="info-card">
              <span>Môn học</span>
              <strong>{{ lopHocPhan?.tenMonHoc || lopHocPhan?.chuongTrinhMon?.tenMonHoc || '—' }}</strong>
            </div>
            <div class="info-card">
              <span>Mã môn</span>
              <strong>{{ lopHocPhan?.maMonHoc || lopHocPhan?.chuongTrinhMon?.maMonHoc || '—' }}</strong>
            </div>
            <div class="info-card">
              <span>Số tín chỉ</span>
              <strong>{{ lopHocPhan?.soTinChi || lopHocPhan?.chuongTrinhMon?.soTinChi || '—' }}</strong>
            </div>
            <div class="info-card">
              <span>Số buổi học</span>
              <strong>{{ lopHocPhan?.soBuoiHoc || '—' }}</strong>
            </div>
            <div class="info-card">
              <span>Kỳ học</span>
              <strong>{{ lopHocPhan?.tenKhungKy || '—' }}</strong>
            </div>
            <div class="info-card">
              <span>Loại lớp</span>
              <strong>{{ lopHocPhan?.loaiLopHocPhan || '—' }}</strong>
            </div>
          </div>

          <!-- Giảng viên -->
          <div v-else-if="activeTab === 'giangVien'">
            <div v-if="loadingGiangVien" class="state-box">Đang tải giảng viên...</div>
            <div v-else-if="!giangViens.length" class="state-box empty">Chưa có phân công giảng viên.</div>
            <div v-else class="person-list">
              <div v-for="item in giangViens" :key="item.id" class="person-card">
                <div class="person-avatar gv">GV</div>
                <div>
                  <strong>{{ item.tenGiaoVien || 'Giảng viên #' + item.giaoVienId }}</strong>
                  <span>{{ item.maGiaoVien || '—' }} • {{ item.vaiTro || 'Phụ trách' }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- Sinh viên -->
          <LopHocPhanSinhVienTab
            v-else-if="activeTab === 'sinhVien'"
            :items="sinhViens"
            :loading="loadingSinhVien"
          />

          <!-- Lịch học -->
          <LopHocPhanLichHocTab
            v-else-if="activeTab === 'lichHoc'"
            :items="lichHocs"
            :loading="loadingLichHoc"
          />

          <!-- Điểm -->
          <div v-else-if="activeTab === 'diem'">
            <div v-if="loadingDiem" class="state-box">Đang tải điểm...</div>
            <div v-else-if="!diems.length" class="state-box empty">Chưa có dữ liệu điểm.</div>
            <div v-else class="score-list">
              <div v-for="item in diems" :key="item.id" class="score-card">
                <div>
                  <strong>Sinh viên #{{ item.sinhVienId }}</strong>
                  <span>{{ item.ketQua || item.trangThai || 'Chưa chốt' }}</span>
                </div>
                <div class="score-badge">{{ item.diemTongKet ?? item.diemQuyDoi ?? '—' }}</div>
              </div>
            </div>
          </div>

        </div>
      </div>
    </transition>
  </teleport>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import LopHocPhanSinhVienTab from './LopHocPhanSinhVienTab.vue'
import LopHocPhanLichHocTab from './LopHocPhanLichHocTab.vue'

const props = defineProps({
  open: Boolean,
  lopHocPhan: { type: Object, default: null },
  sinhViens: { type: Array, default: () => [] },
  giangViens: { type: Array, default: () => [] },
  lichHocs: { type: Array, default: () => [] },
  diems: { type: Array, default: () => [] },
  loadingSinhVien: Boolean,
  loadingGiangVien: Boolean,
  loadingLichHoc: Boolean,
  loadingDiem: Boolean
})

defineEmits(['close'])

const activeTab = ref('tongQuan')

const tabs = computed(() => [
  { key: 'tongQuan', label: 'Tổng quan' },
  { key: 'giangVien', label: 'Giảng viên', count: props.giangViens.length },
  { key: 'sinhVien', label: 'Sinh viên', count: props.sinhViens.length },
  { key: 'lichHoc', label: 'Lịch học', count: props.lichHocs.length },
  { key: 'diem', label: 'Điểm', count: props.diems.length }
])

watch(() => props.lopHocPhan?.id, () => { activeTab.value = 'tongQuan' })

function dinhDangNgay(value) {
  if (!value) return '—'
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return value
  return new Intl.DateTimeFormat('vi-VN').format(date)
}

function hienThiTrangThai(value) {
  const map = { MO_DANG_KY: 'Mở đăng ký', DANG_HOC: 'Đang học', DA_KET_THUC: 'Đã kết thúc', DA_HUY: 'Đã hủy', TAM_DUNG: 'Tạm dừng' }
  return map[value] || value || 'Chưa rõ'
}
</script>

<style scoped>
/* ============ FULL PAGE OVERLAY ============ */
.detail-page {
  position: fixed;
  inset: 0;
  top: 60px; /* bên dưới header UserLayout */
  z-index: 2000;
  background: #f4f7f4;
  overflow-y: auto;
  font-family: 'Roboto', Arial, sans-serif;
  display: flex;
  flex-direction: column;
}

/* ============ TOPBAR ============ */
.detail-topbar {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 14px 24px;
  background: #1a5c36;
  color: #fff;
  flex-shrink: 0;
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  border: 1px solid rgba(255,255,255,0.3);
  border-radius: 6px;
  background: transparent;
  color: rgba(255,255,255,0.9);
  font-size: 13px;
  font-weight: 500;
  padding: 6px 14px;
  cursor: pointer;
  white-space: nowrap;
  font-family: 'Roboto', Arial, sans-serif;
  transition: background 0.15s;
}
.back-btn:hover { background: rgba(255,255,255,0.12); }

.topbar-info { display: flex; flex-direction: column; gap: 2px; min-width: 0; }
.topbar-kicker {
  font-size: 11px;
  font-weight: 400;
  color: rgba(255,255,255,0.65);
  text-transform: uppercase;
  letter-spacing: 0.1em;
}
.topbar-title {
  font-size: 16px;
  font-weight: 600;
  line-height: 1.3;
  color: #fff;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.topbar-sub {
  font-size: 12px;
  color: rgba(255,255,255,0.7);
  font-weight: 400;
}

/* ============ META STATS ============ */
.detail-meta {
  display: grid;
  grid-template-columns: repeat(4, minmax(0,1fr));
  gap: 1px;
  background: #d1e7d9;
  border-bottom: 1px solid #d1e7d9;
  flex-shrink: 0;
}
.meta-item {
  background: #fff;
  padding: 12px 20px;
  display: flex;
  flex-direction: column;
  gap: 3px;
}
.meta-item span {
  font-size: 11px;
  font-weight: 500;
  color: #7a9a82;
  text-transform: uppercase;
  letter-spacing: 0.07em;
}
.meta-item strong {
  font-size: 14px;
  font-weight: 600;
  color: #1a2e1f;
}
.meta-item strong.status-DANG_HOC { color: #1a5c36; }
.meta-item strong.status-DA_KET_THUC { color: #1e40af; }
.meta-item strong.status-DA_HUY { color: #b91c1c; }

/* ============ TABS ============ */
.detail-tabs {
  display: flex;
  gap: 2px;
  padding: 0 24px;
  background: #fff;
  border-bottom: 1px solid #d1e7d9;
  flex-shrink: 0;
  overflow-x: auto;
}
.tab-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 10px 16px;
  border: none;
  border-bottom: 2px solid transparent;
  background: transparent;
  color: #7a9a82;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  white-space: nowrap;
  font-family: 'Roboto', Arial, sans-serif;
  transition: color 0.15s;
  margin-bottom: -1px;
}
.tab-btn:hover { color: #1a5c36; }
.tab-btn.active {
  color: #1a5c36;
  border-bottom-color: #1a5c36;
  font-weight: 600;
}
.tab-count {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 18px;
  height: 18px;
  padding: 0 5px;
  border-radius: 9px;
  background: #e8f5ec;
  color: #1a5c36;
  font-size: 11px;
  font-weight: 600;
}
.tab-btn.active .tab-count { background: #1a5c36; color: #fff; }

/* ============ BODY ============ */
.detail-body {
  flex: 1;
  padding: 20px 24px;
  max-width: 1100px;
  width: 100%;
  margin: 0 auto;
}

/* Tổng quan */
.overview-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0,1fr));
  gap: 10px;
}
.info-card {
  background: #fff;
  border: 1px solid #d1e7d9;
  border-radius: 8px;
  padding: 14px 16px;
}
.info-card span {
  display: block;
  font-size: 11px;
  font-weight: 500;
  color: #7a9a82;
  text-transform: uppercase;
  letter-spacing: 0.07em;
  margin-bottom: 5px;
}
.info-card strong {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #1a2e1f;
}

/* Người (GV/SV) */
.person-list { display: grid; gap: 8px; }
.person-card {
  display: flex;
  gap: 12px;
  align-items: center;
  background: #fff;
  border: 1px solid #d1e7d9;
  border-radius: 8px;
  padding: 12px 16px;
}
.person-avatar {
  width: 38px; height: 38px;
  border-radius: 7px;
  display: grid; place-items: center;
  font-size: 12px; font-weight: 600;
  flex-shrink: 0;
}
.person-avatar.gv { background: #e8f5ec; color: #1a5c36; }
.person-card strong { display: block; font-size: 13px; font-weight: 500; color: #1a2e1f; }
.person-card span { display: block; font-size: 12px; color: #7a9a82; margin-top: 2px; }

/* Điểm */
.score-list { display: grid; gap: 8px; }
.score-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  border: 1px solid #d1e7d9;
  border-radius: 8px;
  padding: 12px 16px;
}
.score-card strong { display: block; font-size: 13px; font-weight: 500; color: #1a2e1f; }
.score-card span { display: block; font-size: 12px; color: #7a9a82; margin-top: 2px; }
.score-badge {
  min-width: 48px; height: 48px;
  border-radius: 8px;
  display: grid; place-items: center;
  background: #e8f5ec;
  color: #1a5c36;
  font-size: 18px;
  font-weight: 600;
}

/* States */
.state-box {
  background: #fff;
  border: 1px dashed #d1e7d9;
  border-radius: 8px;
  padding: 32px;
  text-align: center;
  color: #7a9a82;
  font-size: 13px;
}
.state-box.empty { color: #aab8ae; }

/* ============ TRANSITION ============ */
.page-slide-enter-active { transition: opacity 0.18s ease, transform 0.18s ease; }
.page-slide-leave-active { transition: opacity 0.15s ease, transform 0.15s ease; }
.page-slide-enter-from { opacity: 0; transform: translateY(12px); }
.page-slide-leave-to { opacity: 0; transform: translateY(8px); }

/* ============ RESPONSIVE ============ */
@media (max-width: 900px) {
  .overview-grid { grid-template-columns: repeat(2, minmax(0,1fr)); }
  .detail-meta { grid-template-columns: repeat(2, minmax(0,1fr)); }
  .detail-body { padding: 16px; }
}
@media (max-width: 560px) {
  .overview-grid { grid-template-columns: 1fr; }
  .detail-meta { grid-template-columns: repeat(2, minmax(0,1fr)); }
  .detail-topbar { padding: 12px 16px; }
}
</style>
