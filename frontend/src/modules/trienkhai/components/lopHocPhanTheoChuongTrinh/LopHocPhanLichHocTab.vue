<template>
  <div class="tab-card">
    <div class="timeline-head">
      <div>
        <p>Lịch học</p>
        <h4>{{ items.length }} buổi học</h4>
      </div>
    </div>

    <div v-if="loading" class="loading-box">Đang tải lịch học...</div>
    <div v-else-if="!items.length" class="empty">Chưa có lịch học cho lớp học phần này.</div>
    <div v-else class="timeline">
      <article v-for="item in items" :key="item.id" class="timeline-item">
        <div class="date-box">
          <strong>{{ ngay(item.ngayHoc) }}</strong>
          <span>{{ thang(item.ngayHoc) }}</span>
        </div>
        <div class="line-body">
          <div class="line-top">
            <strong>{{ item.tenCa || item.maCa || 'Ca học' }}</strong>
            <span>{{ gio(item.gioBatDau) }} - {{ gio(item.gioKetThuc) }}</span>
          </div>
          <p>{{ item.noiDungBuoiHoc || 'Nội dung buổi học chưa cập nhật' }}</p>
          <div class="meta">
            <span>Phòng: {{ item.tenPhong || item.maPhong || '—' }}</span>
            <span>GV: {{ item.tenGiaoVien || item.maGiaoVien || '—' }}</span>
            <span class="status">{{ hienThiTrangThai(item.trangThai) }}</span>
          </div>
        </div>
      </article>
    </div>
  </div>
</template>

<script setup>
defineProps({
  items: { type: Array, default: () => [] },
  loading: Boolean
})

function toDate(value) {
  const date = new Date(value)
  return Number.isNaN(date.getTime()) ? null : date
}
function ngay(value) { const date = toDate(value); return date ? String(date.getDate()).padStart(2, '0') : '--' }
function thang(value) { const date = toDate(value); return date ? `Tháng ${date.getMonth() + 1}` : 'Chưa xếp' }
function gio(value) { return value ? String(value).slice(0, 5) : '--:--' }
function hienThiTrangThai(value) {
  const map = { DA_XEP: 'Đã xếp', DA_DAY: 'Đã dạy', NGHI: 'Nghỉ', BU: 'Học bù', HUY: 'Hủy' }
  return map[value] || value || 'Chưa rõ'
}
</script>

<style scoped>
.tab-card { padding: 18px; }
.timeline-head { margin-bottom: 16px; }
p { margin: 0; color: #64748b; }
.timeline-head p { margin-bottom: 4px; color: #2563eb; font-size: 12px; font-weight: 900; text-transform: uppercase; letter-spacing: .1em; }
h4 { margin: 0; color: #0f172a; font-size: 20px; }
.timeline { display: grid; gap: 12px; }
.timeline-item { display: flex; gap: 14px; padding: 14px; border: 1px solid #e2e8f0; border-radius: 20px; background: #fff; box-shadow: 0 12px 34px rgba(15,23,42,.04); }
.date-box { width: 72px; min-width: 72px; height: 72px; border-radius: 20px; display: grid; place-items: center; align-content: center; background: linear-gradient(135deg, #eff6ff, #eef2ff); color: #1d4ed8; }
.date-box strong { display: block; font-size: 22px; line-height: 1; }
.date-box span { margin-top: 5px; font-size: 11px; font-weight: 900; }
.line-body { flex: 1; min-width: 0; }
.line-top { display: flex; justify-content: space-between; gap: 12px; color: #0f172a; font-weight: 900; }
.line-top span { color: #2563eb; }
.line-body p { margin: 8px 0 10px; }
.meta { display: flex; flex-wrap: wrap; gap: 8px; }
.meta span { border-radius: 999px; padding: 6px 9px; background: #f1f5f9; color: #475569; font-size: 12px; font-weight: 800; }
.meta .status { background: #dcfce7; color: #047857; }
.loading-box, .empty { padding: 28px; text-align: center; border-radius: 18px; background: #f8fafc; color: #64748b; font-weight: 700; }
@media (max-width: 620px) { .timeline-item { flex-direction: column; } .date-box { width: 100%; } .line-top { flex-direction: column; } }
</style>
