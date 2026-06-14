<template>
  <section class="table-card">
    <div class="table-head">
      <div>
        <p class="eyebrow">Danh sách lớp học phần</p>
        <h3>{{ title }}</h3>
      </div>
      <div class="head-actions">
        <span class="counter">{{ items.length }} lớp</span>
      </div>
    </div>

    <div v-if="loading" class="skeleton-list">
      <div v-for="n in 5" :key="n" class="skeleton-row"></div>
    </div>

    <div v-else-if="!items.length" class="empty-state">
      <div class="empty-icon">📚</div>
      <h4>Chưa có lớp học phần</h4>
      <p>Chọn đủ ngành, hệ đào tạo, chương trình, version và kỳ để tải lớp học phần đang triển khai.</p>
    </div>

    <div v-else class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Mã lớp</th>
            <th>Lớp học phần</th>
            <th>Môn học</th>
            <th>Sĩ số</th>
            <th>Thời gian</th>
            <th>Trạng thái</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in items" :key="item.id" @click="$emit('select', item)">
            <td><span class="code">{{ item.maLop || 'LHP' + item.id }}</span></td>
            <td>
              <strong>{{ item.tenLop || 'Lớp học phần' }}</strong>
              <small>{{ item.loaiLopHocPhan || 'Lớp môn' }} <template v-if="item.tenKhungKy">• {{ item.tenKhungKy }}</template></small>
            </td>
            <td>
              <strong>{{ item.tenMonHoc || item.chuongTrinhMon?.tenMonHoc || 'Môn học' }}</strong>
              <small>{{ item.maMonHoc || item.chuongTrinhMon?.maMonHoc || '—' }} <template v-if="item.soTinChi">• {{ item.soTinChi }} TC</template></small>
            </td>
            <td>
              <div class="capacity">
                <span>{{ item.soLuongHienTai ?? 0 }}/{{ item.soLuongToiDa ?? '—' }}</span>
                <div class="bar"><i :style="{ width: tiLeSiSo(item) + '%' }"></i></div>
              </div>
            </td>
            <td>
              <strong>{{ dinhDangNgay(item.ngayBatDau) }}</strong>
              <small>đến {{ dinhDangNgay(item.ngayKetThuc) }}</small>
            </td>
            <td><span class="status" :class="classTrangThai(item.trangThai)">{{ hienThiTrangThai(item.trangThai) }}</span></td>
            <td><button class="detail-btn" type="button">Xem</button></td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
defineProps({
  items: { type: Array, default: () => [] },
  loading: Boolean,
  title: { type: String, default: 'Lớp học phần theo kỳ' }
})

defineEmits(['select'])

function dinhDangNgay(value) {
  if (!value) return '—'
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return value
  return new Intl.DateTimeFormat('vi-VN').format(date)
}

function tiLeSiSo(item) {
  const max = Number(item.soLuongToiDa || 0)
  const current = Number(item.soLuongHienTai || 0)
  if (!max) return 0
  return Math.min(100, Math.round((current / max) * 100))
}

function hienThiTrangThai(value) {
  const map = {
    MO_DANG_KY: 'Mở đăng ký',
    DANG_HOC: 'Đang học',
    DA_KET_THUC: 'Đã kết thúc',
    DA_HUY: 'Đã hủy',
    TAM_DUNG: 'Tạm dừng'
  }
  return map[value] || value || 'Chưa rõ'
}

function classTrangThai(value) {
  if (value === 'DANG_HOC') return 'active'
  if (value === 'DA_KET_THUC') return 'done'
  if (value === 'DA_HUY') return 'danger'
  return 'neutral'
}
</script>

<style scoped>
.table-card { border: 1px solid rgba(15, 23, 42, .08); border-radius: 28px; background: #fff; box-shadow: 0 22px 60px rgba(15,23,42,.07); overflow: hidden; }
.table-head { padding: 22px 24px; display: flex; align-items: center; justify-content: space-between; gap: 16px; border-bottom: 1px solid #eef2f7; }
.eyebrow { margin: 0 0 5px; color: #2563eb; text-transform: uppercase; font-weight: 900; font-size: 12px; letter-spacing: .12em; }
h3 { margin: 0; color: #0f172a; font-size: 22px; }
.counter { display: inline-flex; align-items: center; border-radius: 999px; padding: 9px 14px; background: #eff6ff; color: #1d4ed8; font-weight: 900; }
.table-wrap { width: 100%; overflow-x: auto; }
table { width: 100%; border-collapse: collapse; min-width: 980px; }
th { text-align: left; padding: 14px 18px; color: #64748b; font-size: 12px; text-transform: uppercase; letter-spacing: .08em; background: #f8fafc; }
td { padding: 16px 18px; border-top: 1px solid #eef2f7; color: #0f172a; vertical-align: middle; }
tbody tr { cursor: pointer; transition: .18s ease; }
tbody tr:hover { background: #f8fbff; transform: translateY(-1px); }
strong { display: block; font-size: 14px; }
small { display: block; color: #64748b; margin-top: 4px; font-weight: 650; }
.code { display: inline-flex; border-radius: 999px; padding: 7px 10px; background: #eef2ff; color: #3730a3; font-weight: 900; }
.capacity span { font-size: 13px; font-weight: 900; color: #334155; }
.bar { width: 92px; height: 7px; margin-top: 7px; background: #e2e8f0; border-radius: 999px; overflow: hidden; }
.bar i { display: block; height: 100%; border-radius: inherit; background: linear-gradient(90deg, #22c55e, #2563eb); }
.status { display: inline-flex; border-radius: 999px; padding: 7px 10px; font-size: 12px; font-weight: 900; }
.status.active { background: #dcfce7; color: #047857; }
.status.done { background: #e0f2fe; color: #0369a1; }
.status.danger { background: #fee2e2; color: #b91c1c; }
.status.neutral { background: #f1f5f9; color: #475569; }
.detail-btn { border: 0; border-radius: 999px; padding: 8px 12px; background: #0f172a; color: #fff; font-weight: 900; cursor: pointer; }
.empty-state { padding: 58px 24px; text-align: center; color: #64748b; }
.empty-icon { width: 64px; height: 64px; display: grid; place-items: center; margin: 0 auto 14px; border-radius: 22px; background: #eff6ff; font-size: 30px; }
.empty-state h4 { margin: 0 0 8px; color: #0f172a; font-size: 20px; }
.empty-state p { margin: 0 auto; max-width: 520px; }
.skeleton-list { padding: 18px 24px 24px; display: grid; gap: 12px; }
.skeleton-row { height: 58px; border-radius: 16px; background: linear-gradient(90deg, #f1f5f9, #e2e8f0, #f1f5f9); background-size: 200% 100%; animation: shimmer 1.2s infinite; }
@keyframes shimmer { to { background-position: -200% 0; } }
</style>
