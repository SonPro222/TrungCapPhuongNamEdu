<template>
  <teleport to="body">
    <transition name="drawer-fade">
      <div v-if="open" class="drawer-mask" @click.self="$emit('close')">
        <aside class="drawer-panel">
          <header class="drawer-header">
            <div>
              <p class="eyebrow">Chi tiết lớp học phần</p>
              <h3>{{ lopHocPhan?.tenLop || 'Lớp học phần' }}</h3>
              <span>{{ lopHocPhan?.maLop || '—' }} • {{ lopHocPhan?.tenMonHoc || lopHocPhan?.chuongTrinhMon?.tenMonHoc || 'Môn học' }}</span>
            </div>
            <button class="close-btn" type="button" @click="$emit('close')">×</button>
          </header>

          <section class="hero-info">
            <div class="hero-stat">
              <span>Sĩ số</span>
              <strong>{{ lopHocPhan?.soLuongHienTai ?? 0 }}/{{ lopHocPhan?.soLuongToiDa ?? '—' }}</strong>
            </div>
            <div class="hero-stat">
              <span>Bắt đầu</span>
              <strong>{{ dinhDangNgay(lopHocPhan?.ngayBatDau) }}</strong>
            </div>
            <div class="hero-stat">
              <span>Kết thúc</span>
              <strong>{{ dinhDangNgay(lopHocPhan?.ngayKetThuc) }}</strong>
            </div>
            <div class="hero-stat">
              <span>Trạng thái</span>
              <strong>{{ hienThiTrangThai(lopHocPhan?.trangThai) }}</strong>
            </div>
          </section>

          <nav class="drawer-tabs">
            <button v-for="tab in tabs" :key="tab.key" type="button" :class="{ active: activeTab === tab.key }" @click="activeTab = tab.key">
              {{ tab.label }}
              <span v-if="tab.count !== undefined">{{ tab.count }}</span>
            </button>
          </nav>

          <main class="drawer-body">
            <div v-if="activeTab === 'tongQuan'" class="overview-grid">
              <article>
                <span>Môn học</span>
                <strong>{{ lopHocPhan?.tenMonHoc || lopHocPhan?.chuongTrinhMon?.tenMonHoc || '—' }}</strong>
              </article>
              <article>
                <span>Mã môn</span>
                <strong>{{ lopHocPhan?.maMonHoc || lopHocPhan?.chuongTrinhMon?.maMonHoc || '—' }}</strong>
              </article>
              <article>
                <span>Số tín chỉ</span>
                <strong>{{ lopHocPhan?.soTinChi || lopHocPhan?.chuongTrinhMon?.soTinChi || '—' }}</strong>
              </article>
              <article>
                <span>Số buổi học</span>
                <strong>{{ lopHocPhan?.soBuoiHoc || '—' }}</strong>
              </article>
              <article>
                <span>Kỳ học</span>
                <strong>{{ lopHocPhan?.tenKhungKy || '—' }}</strong>
              </article>
              <article>
                <span>Loại lớp</span>
                <strong>{{ lopHocPhan?.loaiLopHocPhan || '—' }}</strong>
              </article>
            </div>

            <div v-else-if="activeTab === 'giangVien'" class="teacher-list">
              <div v-if="loadingGiangVien" class="loading-box">Đang tải giảng viên...</div>
              <div v-else-if="!giangViens.length" class="empty-box">Chưa có phân công giảng viên.</div>
              <article v-for="item in giangViens" :key="item.id" class="teacher-card">
                <div class="avatar">GV</div>
                <div>
                  <strong>{{ item.tenGiaoVien || 'Giảng viên #' + item.giaoVienId }}</strong>
                  <span>{{ item.maGiaoVien || '—' }} • {{ item.vaiTro || 'Phụ trách' }}</span>
                </div>
              </article>
            </div>

            <LopHocPhanSinhVienTab
              v-else-if="activeTab === 'sinhVien'"
              :items="sinhViens"
              :loading="loadingSinhVien"
            />

            <LopHocPhanLichHocTab
              v-else-if="activeTab === 'lichHoc'"
              :items="lichHocs"
              :loading="loadingLichHoc"
            />

            <div v-else-if="activeTab === 'diem'" class="score-list">
              <div v-if="loadingDiem" class="loading-box">Đang tải điểm...</div>
              <div v-else-if="!diems.length" class="empty-box">Chưa có dữ liệu điểm lớp học phần.</div>
              <article v-for="item in diems" :key="item.id" class="score-card">
                <div>
                  <strong>Sinh viên #{{ item.sinhVienId }}</strong>
                  <span>{{ item.ketQua || item.trangThai || 'Chưa chốt' }}</span>
                </div>
                <div class="score-number">{{ item.diemTongKet ?? item.diemQuyDoi ?? '—' }}</div>
              </article>
            </div>
          </main>
        </aside>
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
  const map = { MO_DANG_KY: 'Mở đăng ký', DANG_HOC: 'Đang học', DA_KET_THUC: 'Đã kết thúc', DA_HUY: 'Đã hủy' }
  return map[value] || value || 'Chưa rõ'
}
</script>

<style scoped>
.drawer-mask { position: fixed; inset: 0; z-index: 1000; display: flex; justify-content: flex-end; background: rgba(15, 23, 42, .42); backdrop-filter: blur(8px); }
.drawer-panel { width: min(980px, 94vw); height: 100%; overflow: auto; background: #f8fafc; box-shadow: -30px 0 80px rgba(15,23,42,.24); }
.drawer-header { position: sticky; top: 0; z-index: 2; display: flex; justify-content: space-between; gap: 18px; padding: 24px; color: white; background: linear-gradient(135deg, #0f172a, #1d4ed8); }
.eyebrow { margin: 0 0 6px; color: #bfdbfe; font-size: 12px; font-weight: 900; letter-spacing: .12em; text-transform: uppercase; }
h3 { margin: 0; font-size: 26px; }
.drawer-header span { display: block; margin-top: 7px; color: #dbeafe; font-weight: 700; }
.close-btn { width: 42px; height: 42px; border: 1px solid rgba(255,255,255,.2); border-radius: 16px; background: rgba(255,255,255,.12); color: white; font-size: 28px; line-height: 1; cursor: pointer; }
.hero-info { display: grid; grid-template-columns: repeat(4, minmax(0, 1fr)); gap: 12px; padding: 18px 24px; background: white; border-bottom: 1px solid #e2e8f0; }
.hero-stat { padding: 15px; border-radius: 18px; background: #f8fafc; border: 1px solid #e2e8f0; }
.hero-stat span { display: block; color: #64748b; font-size: 12px; font-weight: 900; text-transform: uppercase; margin-bottom: 6px; }
.hero-stat strong { color: #0f172a; }
.drawer-tabs { position: sticky; top: 90px; z-index: 1; display: flex; gap: 8px; padding: 14px 24px; background: rgba(248,250,252,.88); backdrop-filter: blur(10px); border-bottom: 1px solid #e2e8f0; overflow-x: auto; }
.drawer-tabs button { white-space: nowrap; border: 1px solid #e2e8f0; border-radius: 999px; padding: 10px 14px; background: white; color: #475569; font-weight: 900; cursor: pointer; }
.drawer-tabs button.active { background: #0f172a; color: white; border-color: #0f172a; }
.drawer-tabs span { margin-left: 6px; opacity: .75; }
.drawer-body { padding: 18px 24px 28px; }
.overview-grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 12px; }
.overview-grid article, .teacher-card, .score-card { padding: 16px; border: 1px solid #e2e8f0; border-radius: 20px; background: white; box-shadow: 0 12px 34px rgba(15,23,42,.04); }
.overview-grid span { display: block; color: #64748b; font-size: 12px; font-weight: 900; text-transform: uppercase; margin-bottom: 7px; }
.overview-grid strong { color: #0f172a; }
.teacher-list, .score-list { display: grid; gap: 12px; }
.teacher-card { display: flex; gap: 12px; align-items: center; }
.avatar { width: 44px; height: 44px; border-radius: 16px; display: grid; place-items: center; background: linear-gradient(135deg, #2563eb, #7c3aed); color: white; font-weight: 900; }
.teacher-card strong, .score-card strong { display: block; color: #0f172a; }
.teacher-card span, .score-card span { display: block; margin-top: 4px; color: #64748b; font-weight: 700; }
.score-card { display: flex; align-items: center; justify-content: space-between; }
.score-number { min-width: 62px; height: 62px; border-radius: 22px; display: grid; place-items: center; background: #eff6ff; color: #1d4ed8; font-size: 22px; font-weight: 900; }
.loading-box, .empty-box { padding: 30px; text-align: center; border-radius: 20px; background: white; color: #64748b; font-weight: 800; border: 1px dashed #cbd5e1; }
.drawer-fade-enter-active, .drawer-fade-leave-active { transition: opacity .2s ease; }
.drawer-fade-enter-from, .drawer-fade-leave-to { opacity: 0; }
.drawer-fade-enter-active .drawer-panel, .drawer-fade-leave-active .drawer-panel { transition: transform .24s ease; }
.drawer-fade-enter-from .drawer-panel, .drawer-fade-leave-to .drawer-panel { transform: translateX(40px); }
@media (max-width: 780px) { .hero-info, .overview-grid { grid-template-columns: 1fr; } .drawer-tabs { top: 118px; } }
</style>
