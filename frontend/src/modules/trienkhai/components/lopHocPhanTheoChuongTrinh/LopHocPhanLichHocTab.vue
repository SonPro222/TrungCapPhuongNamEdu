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
.tab-card { font-family: 'Roboto', Arial, sans-serif; }
.timeline-head { margin-bottom: 14px; }
p { margin: 0; color: #7a9a82; font-size: 12px; }
.timeline-head p { margin-bottom: 2px; color: #1a5c36; font-size: 11px; font-weight: 500; text-transform: uppercase; letter-spacing: .1em; }
h4 { margin: 0; color: #1a2e1f; font-size: 15px; font-weight: 600; }

.timeline { display: grid; gap: 8px; }

.timeline-item {
  display: flex;
  gap: 12px;
  padding: 12px 14px;
  border: 1px solid #d1e7d9;
  border-radius: 8px;
  background: #fff;
}

.date-box {
  width: 56px; min-width: 56px; height: 56px;
  border-radius: 7px;
  display: grid; place-items: center; align-content: center;
  background: #e8f5ec;
  color: #1a5c36;
}
.date-box strong { display: block; font-size: 18px; font-weight: 700; line-height: 1; }
.date-box span { margin-top: 3px; font-size: 10px; font-weight: 500; color: #5a7a63; }

.line-body { flex: 1; min-width: 0; }
.line-top {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  color: #1a2e1f;
  font-weight: 500;
  font-size: 13px;
}
.line-top span { color: #1a5c36; font-size: 12px; }
.line-body p { margin: 5px 0 8px; font-size: 12px; color: #7a9a82; }

.meta { display: flex; flex-wrap: wrap; gap: 6px; }
.meta span {
  border-radius: 4px;
  padding: 3px 8px;
  background: #f0f4f1;
  color: #5a7a63;
  font-size: 11px;
  font-weight: 400;
}
.meta .status { background: #d6f0e0; color: #1a5c36; }

.loading-box, .empty {
  padding: 28px;
  text-align: center;
  border: 1px dashed #d1e7d9;
  border-radius: 8px;
  color: #7a9a82;
  font-size: 13px;
}

@media (max-width: 620px) {
  .timeline-item { flex-direction: column; }
  .date-box { width: 100%; height: 44px; flex-direction: row; gap: 8px; }
  .line-top { flex-direction: column; }
}
</style>
