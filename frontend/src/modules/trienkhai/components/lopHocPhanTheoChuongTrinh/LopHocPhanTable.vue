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
.table-card {
  border: 1px solid #d1e7d9;
  border-radius: 10px;
  background: #fff;
  box-shadow: 0 1px 6px rgba(26,92,54,0.07);
  overflow: hidden;
  font-family: 'Roboto', Arial, sans-serif;
}

.table-head {
  padding: 14px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  border-bottom: 1px solid #e8f0ea;
}

.eyebrow {
  margin: 0 0 2px;
  color: #1a5c36;
  text-transform: uppercase;
  font-weight: 500;
  font-size: 11px;
  letter-spacing: 0.1em;
}

h3 {
  margin: 0;
  color: #1a2e1f;
  font-size: 15px;
  font-weight: 600;
}

.counter {
  display: inline-flex;
  align-items: center;
  border-radius: 5px;
  padding: 4px 10px;
  background: #e8f5ec;
  color: #1a5c36;
  font-size: 12px;
  font-weight: 500;
}

.table-wrap { width: 100%; overflow-x: auto; }

table { width: 100%; border-collapse: collapse; min-width: 900px; }

th {
  text-align: left;
  padding: 10px 14px;
  color: #5a7a63;
  font-size: 11px;
  text-transform: uppercase;
  letter-spacing: 0.07em;
  background: #f5faf6;
  font-weight: 500;
}

td {
  padding: 11px 14px;
  border-top: 1px solid #edf3ee;
  color: #1a2e1f;
  vertical-align: middle;
}

tbody tr { cursor: pointer; transition: background 0.12s; }
tbody tr:hover { background: #f5faf6; }

strong { display: block; font-size: 13px; font-weight: 500; }
small { display: block; color: #7a9a82; margin-top: 2px; font-size: 12px; font-weight: 400; }

.code {
  display: inline-flex;
  border-radius: 4px;
  padding: 3px 8px;
  background: #e8f5ec;
  color: #1a5c36;
  font-size: 12px;
  font-weight: 500;
}

.capacity span { font-size: 12px; font-weight: 500; color: #3a5a42; }
.bar { width: 80px; height: 5px; margin-top: 5px; background: #e2ede6; border-radius: 3px; overflow: hidden; }
.bar i { display: block; height: 100%; border-radius: inherit; background: linear-gradient(90deg, #2d8a55, #1a5c36); }

.status {
  display: inline-flex;
  border-radius: 4px;
  padding: 3px 8px;
  font-size: 11px;
  font-weight: 500;
}
.status.active { background: #d6f0e0; color: #1a5c36; }
.status.done { background: #dbeafe; color: #1e40af; }
.status.danger { background: #fee2e2; color: #b91c1c; }
.status.neutral { background: #f0f4f1; color: #5a7a63; }

.detail-btn {
  border: 1px solid #d1e7d9;
  border-radius: 5px;
  padding: 5px 12px;
  background: #fff;
  color: #1a5c36;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.12s;
  font-family: 'Roboto', Arial, sans-serif;
}
.detail-btn:hover { background: #f0f9f3; }

.empty-state { padding: 48px 24px; text-align: center; color: #7a9a82; }
.empty-icon {
  width: 56px; height: 56px;
  display: grid; place-items: center;
  margin: 0 auto 12px;
  border-radius: 10px;
  background: #e8f5ec;
  font-size: 26px;
}
.empty-state h4 { margin: 0 0 6px; color: #1a2e1f; font-size: 16px; font-weight: 600; }
.empty-state p { margin: 0 auto; max-width: 480px; font-size: 13px; line-height: 1.6; }

.skeleton-list { padding: 14px 20px 20px; display: grid; gap: 8px; }
.skeleton-row {
  height: 48px;
  border-radius: 6px;
  background: linear-gradient(90deg, #f0f4f1, #e2ede6, #f0f4f1);
  background-size: 200% 100%;
  animation: shimmer 1.2s infinite;
}
@keyframes shimmer { to { background-position: -200% 0; } }
</style>
