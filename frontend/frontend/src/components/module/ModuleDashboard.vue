<template>
  <section class="dashboard-page">
    <div class="dashboard-shell">
      <header class="dashboard-head">
        <div>
          <p class="dashboard-kicker">Dashboard quản trị</p>
          <h1>Hệ thống quản lý đào tạo</h1>
          <p>Truy cập nhanh các phân hệ nghiệp vụ chính của trường.</p>
        </div>
      </header>

      <section class="module-grid" aria-label="Danh sách module">
        <RouterLink v-for="module in modules" :key="module.to" class="module-card" :to="module.to">
          <span class="module-icon">{{ module.short }}</span>
          <span>
            <b>{{ module.title }}</b>
            <small>{{ module.desc }}</small>
          </span>
        </RouterLink>
      </section>
    </div>
  </section>
</template>

<script setup>
const modules = [
  { title: 'Chương trình đào tạo', short: 'CT', to: '/chuong-trinh', desc: 'Chương trình, phiên bản, khung đào tạo, môn học.' },
  { title: 'Đào tạo', short: 'ĐT', to: '/dao-tao', desc: 'Ngành, trình độ, khóa đào tạo, lớp hành chính.' },
  { title: 'Sinh viên', short: 'SV', to: '/sinh-vien', desc: 'Hồ sơ sinh viên, chương trình học, trạng thái.' },
  { title: 'Giảng dạy', short: 'GD', to: '/giang-day', desc: 'Lớp học phần, lịch học, giáo viên, điểm danh.' },
  { title: 'Điểm', short: 'ĐI', to: '/diem', desc: 'Bài kiểm tra, kết quả môn học, bảng điểm.' },
  { title: 'Thi', short: 'TH', to: '/thi', desc: 'Đề thi, lịch thi, sinh viên dự thi.' },
  { title: 'Học phí', short: 'HP', to: '/hoc-phi', desc: 'Học phí, giao dịch, công nợ.' },
  { title: 'LMS', short: 'LM', to: '/lms', desc: 'Bài tập online, ngân hàng câu hỏi, đồng bộ điểm.' },
  { title: 'Hệ thống', short: 'HT', to: '/he-thong', desc: 'Tài khoản, vai trò, quyền, nhật ký.' },
];
</script>

<style scoped>
.dashboard-page{min-height:100%;padding:18px;background:#f4f6fa;color:#102033}.dashboard-shell{width:100%;max-width:none;margin:0;display:grid;gap:14px}.dashboard-head{border:1px solid #dce5f2;border-radius:11px;background:#fff;padding:18px 20px;box-shadow:0 8px 22px rgba(15,23,42,.035)}.dashboard-kicker{margin:0 0 4px;color:#2454d8;font-size:11px;font-weight:900;text-transform:uppercase;letter-spacing:.08em}.dashboard-head h1{margin:0;color:#102033;font-size:25px;line-height:1.25;font-weight:900}.dashboard-head p:last-child{margin:5px 0 0;color:#5f6f86;font-size:13.5px}.module-grid{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:12px}.module-card{display:grid;grid-template-columns:40px 1fr;gap:12px;align-items:center;min-height:82px;border:1px solid #dce5f2;border-radius:11px;background:#fff;padding:13px 14px;text-decoration:none;color:#102033;box-shadow:0 8px 22px rgba(15,23,42,.035)}.module-card:hover{border-color:#9fc2ff;background:#fbfdff}.module-icon{width:40px;height:40px;border-radius:9px;background:#edf4ff;color:#1d4ed8;display:flex;align-items:center;justify-content:center;font-size:13px;font-weight:900}.module-card b{display:block;font-size:15px;line-height:1.25}.module-card small{display:block;margin-top:4px;color:#64748b;font-size:12px;line-height:1.35}@media(max-width:1100px){.module-grid{grid-template-columns:repeat(2,minmax(0,1fr))}}@media(max-width:720px){.dashboard-page{padding:12px}.module-grid{grid-template-columns:1fr}.dashboard-head h1{font-size:22px}}
</style>
