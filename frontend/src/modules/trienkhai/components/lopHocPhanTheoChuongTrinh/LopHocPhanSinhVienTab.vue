<template>
  <div class="tab-card">
    <div class="tab-head">
      <div>
        <p>Sinh viên trong lớp</p>
        <h4>{{ items.length }} sinh viên</h4>
      </div>
    </div>

    <div v-if="loading" class="loading-box">Đang tải danh sách sinh viên...</div>
    <div v-else-if="!items.length" class="empty">Chưa có sinh viên trong lớp học phần này.</div>
    <div v-else class="student-grid">
      <article v-for="item in items" :key="item.id" class="student-card">
        <div class="avatar">{{ layChuCai(item) }}</div>
        <div>
          <strong>{{ item.tenSinhVien || item.hoTenSinhVien || item.sinhVien?.hoTen || ('Sinh viên #' + item.sinhVienId) }}</strong>
          <span>{{ item.maSinhVien || item.sinhVien?.maSinhVien || 'Mã SV chưa có' }}</span>
          <small>{{ item.laHocLai ? 'Học lại' : 'Học lần đầu' }} • {{ hienThiTrangThai(item.trangThai) }}</small>
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

function layChuCai(item) {
  const ten = item.tenSinhVien || item.hoTenSinhVien || item.sinhVien?.hoTen || 'SV'
  return ten.split(' ').filter(Boolean).slice(-2).map(part => part[0]).join('').toUpperCase()
}

function hienThiTrangThai(value) {
  const map = {
    DANG_HOC: 'Đang học',
    DA_RUT: 'Đã rút',
    DA_HOAN_THANH: 'Đã hoàn thành',
    BAO_LUU: 'Bảo lưu'
  }
  return map[value] || value || 'Chưa rõ'
}
</script>

<style scoped>
.tab-card { font-family: 'Roboto', Arial, sans-serif; }
.tab-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px; }
p { margin: 0 0 2px; color: #1a5c36; font-size: 11px; font-weight: 500; text-transform: uppercase; letter-spacing: .1em; }
h4 { margin: 0; color: #1a2e1f; font-size: 15px; font-weight: 600; }

.student-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 8px;
}

.student-card {
  display: flex;
  gap: 10px;
  align-items: center;
  padding: 12px 14px;
  border: 1px solid #d1e7d9;
  border-radius: 8px;
  background: #fff;
}

.avatar {
  flex: 0 0 auto;
  width: 36px; height: 36px;
  border-radius: 7px;
  display: grid; place-items: center;
  background: #e8f5ec;
  color: #1a5c36;
  font-size: 12px; font-weight: 600;
}

strong { display: block; color: #1a2e1f; font-size: 13px; font-weight: 500; }
span { display: block; margin-top: 2px; color: #7a9a82; font-size: 12px; font-weight: 400; }
small { display: block; margin-top: 2px; color: #aab8ae; font-size: 11px; font-weight: 400; }

.loading-box, .empty {
  padding: 28px;
  text-align: center;
  border: 1px dashed #d1e7d9;
  border-radius: 8px;
  color: #7a9a82;
  font-size: 13px;
}

@media (max-width: 820px) { .student-grid { grid-template-columns: 1fr; } }
</style>
