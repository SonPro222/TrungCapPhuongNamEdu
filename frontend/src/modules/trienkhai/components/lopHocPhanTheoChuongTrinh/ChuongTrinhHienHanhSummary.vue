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
  border-radius: 28px;
  padding: 22px;
  background: linear-gradient(135deg, #0f172a 0%, #1e3a8a 52%, #2563eb 100%);
  color: white;
  box-shadow: 0 28px 70px rgba(37,99,235,.24);
}
.summary-main { display: flex; gap: 16px; align-items: center; margin-bottom: 20px; }
.icon-orb { width: 58px; height: 58px; border-radius: 20px; display: grid; place-items: center; background: rgba(255,255,255,.16); border: 1px solid rgba(255,255,255,.22); font-weight: 900; box-shadow: inset 0 1px 0 rgba(255,255,255,.22); }
.eyebrow { margin: 0 0 5px; color: #bfdbfe; font-size: 12px; font-weight: 900; letter-spacing: .12em; text-transform: uppercase; }
h3 { margin: 0; font-size: 24px; line-height: 1.18; }
.subline { margin: 7px 0 0; color: #dbeafe; font-weight: 650; }
.summary-stats { display: grid; grid-template-columns: repeat(6, minmax(0, 1fr)); gap: 10px; }
.stat { padding: 14px; border-radius: 18px; background: rgba(255,255,255,.12); border: 1px solid rgba(255,255,255,.16); backdrop-filter: blur(8px); }
.stat span { display: block; color: #bfdbfe; font-size: 12px; font-weight: 800; margin-bottom: 7px; }
.stat strong { display: block; color: #fff; font-size: 15px; line-height: 1.2; }
.stat strong.ok { color: #bbf7d0; }
@media (max-width: 1100px) { .summary-stats { grid-template-columns: repeat(3, minmax(0, 1fr)); } }
@media (max-width: 720px) { .summary-stats { grid-template-columns: 1fr; } .summary-main { align-items: flex-start; } }
</style>
