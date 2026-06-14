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
.tab-card { padding: 18px; }
.tab-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
p { margin: 0 0 4px; color: #2563eb; font-size: 12px; font-weight: 900; text-transform: uppercase; letter-spacing: .1em; }
h4 { margin: 0; color: #0f172a; font-size: 20px; }
.student-grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 12px; }
.student-card { display: flex; gap: 12px; align-items: center; padding: 14px; border: 1px solid #e2e8f0; border-radius: 18px; background: linear-gradient(180deg, #fff, #f8fafc); }
.avatar { flex: 0 0 auto; width: 44px; height: 44px; border-radius: 16px; display: grid; place-items: center; background: linear-gradient(135deg, #2563eb, #7c3aed); color: #fff; font-weight: 900; }
strong { display: block; color: #0f172a; font-size: 14px; }
span, small { display: block; margin-top: 3px; color: #64748b; font-size: 12px; font-weight: 700; }
.loading-box, .empty { padding: 28px; text-align: center; border-radius: 18px; background: #f8fafc; color: #64748b; font-weight: 700; }
@media (max-width: 820px) { .student-grid { grid-template-columns: 1fr; } }
</style>
