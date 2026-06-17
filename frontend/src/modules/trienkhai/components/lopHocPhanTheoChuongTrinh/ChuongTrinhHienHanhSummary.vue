<template>
  <section class="summary-card">
    <div class="summary-main">
      <div class="icon-orb">CT</div>
      <div>
        <p class="eyebrow">Chương trình đang triển khai</p>
        <h3>{{ chuongTrinh?.tenChuongTrinh || chuongTrinh?.ten || 'Chưa chọn chương trình' }}</h3>
        <p class="subline">
          {{ hienThiNganh }}
          <span v-if="hienThiHe">• {{ hienThiHe }}</span>
        </p>
      </div>
    </div>

    <div class="summary-stats">
      <div class="stat">
        <span>Version</span>
        <strong>{{ tenVersion }}</strong>
      </div>
      <div class="stat">
        <span>Trạng thái</span>
        <strong :class="version?.laHienHanh ? 'ok' : ''">{{ trangThaiVersion }}</strong>
      </div>
      <div class="stat">
        <span>Ngày bắt đầu</span>
        <strong>{{ dinhDangNgay(version?.ngayBatDau || version?.ngayApDungTu || chuongTrinh?.ngayBatDau) }}</strong>
      </div>
      <div class="stat">
        <span>Ngày kết thúc</span>
        <strong>{{ dinhDangNgay(version?.ngayKetThuc || version?.ngayApDungDen || chuongTrinh?.ngayKetThuc) }}</strong>
      </div>
      <div class="stat">
        <span>Số kỳ</span>
        <strong>{{ soKy }}</strong>
      </div>
      <div class="stat">
        <span>Lớp học phần</span>
        <strong>{{ tongLop }}</strong>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  nganh: { type: Object, default: null },
  nganhHeDaoTao: { type: Object, default: null },
  chuongTrinh: { type: Object, default: null },
  version: { type: Object, default: null },
  khungKys: { type: Array, default: () => [] },
  tongLop: { type: Number, default: 0 }
})

const hienThiNganh = computed(() => props.nganh?.tenNganh || props.nganh?.ten || 'Chưa chọn ngành')
const tenVersion = computed(() => props.version?.maVersion || props.version?.tenVersion || props.version?.version || (props.version?.id ? `Version ${props.version.id}` : 'Chưa có'))
const trangThaiVersion = computed(() => props.version?.laHienHanh ? 'Hiện hành' : (props.version?.trangThai || 'Chưa xác định'))
const soKy = computed(() => props.nganhHeDaoTao?.soKy || props.nganhHeDaoTao?.soHocKy || props.khungKys.length || '—')
const hienThiHe = computed(() => {
  const item = props.nganhHeDaoTao || {}
  const trinhDo = item.tenTrinhDo || item.trinhDoDaoTaoTen || item.trinhDoDaoTao?.ten || item.trinhDo
  const loai = item.tenLoaiChuongTrinh || item.loaiChuongTrinhTen || item.loaiChuongTrinh?.ten || item.loaiChuongTrinh
  const thang = item.soThangDaoTao || item.soThang || item.thoiGianDaoTao
  return [trinhDo, loai, thang ? `${thang} tháng` : ''].filter(Boolean).join(' - ')
})

function dinhDangNgay(value) {
  if (!value) return '—'
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return value
  return new Intl.DateTimeFormat('vi-VN').format(date)
}
</script>

<style scoped>
.summary-card {
  border-radius: 10px;
  padding: 16px 20px;
  background: linear-gradient(135deg, #1a5c36 0%, #2d8a55 100%);
  color: #fff;
  box-shadow: 0 2px 10px rgba(26,92,54,0.18);
  font-family: 'Roboto', Arial, sans-serif;
}

.summary-main {
  display: flex;
  gap: 14px;
  align-items: center;
  margin-bottom: 14px;
}

.icon-orb {
  width: 44px;
  height: 44px;
  border-radius: 8px;
  display: grid;
  place-items: center;
  background: rgba(255,255,255,0.15);
  border: 1px solid rgba(255,255,255,0.2);
  font-weight: 700;
  font-size: 13px;
  flex-shrink: 0;
}

.eyebrow {
  margin: 0 0 3px;
  color: rgba(255,255,255,0.65);
  font-size: 11px;
  font-weight: 400;
  letter-spacing: 0.1em;
  text-transform: uppercase;
}

h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  line-height: 1.3;
}

.subline {
  margin: 4px 0 0;
  color: rgba(255,255,255,0.75);
  font-size: 13px;
  font-weight: 400;
}

.summary-stats {
  display: grid;
  grid-template-columns: repeat(6, minmax(0, 1fr));
  gap: 8px;
}

.stat {
  padding: 10px 12px;
  border-radius: 7px;
  background: rgba(255,255,255,0.12);
  border: 1px solid rgba(255,255,255,0.15);
}

.stat span {
  display: block;
  color: rgba(255,255,255,0.65);
  font-size: 11px;
  font-weight: 400;
  margin-bottom: 5px;
}

.stat strong {
  display: block;
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  line-height: 1.2;
}

.stat strong.ok { color: #bbf7d0; }

@media (max-width: 1100px) { .summary-stats { grid-template-columns: repeat(3, minmax(0, 1fr)); } }
@media (max-width: 720px) {
  .summary-stats { grid-template-columns: repeat(2, 1fr); }
  .summary-main { align-items: flex-start; }
}
</style>
