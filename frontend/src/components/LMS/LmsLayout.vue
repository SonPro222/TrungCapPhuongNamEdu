<template>
  <section class="module-layout">
    <aside class="module-menu">
      <h2>LMS</h2>
        <RouterLink to="/lms/ngan-hang-cau-hoi">Ngân hàng câu hỏi</RouterLink>
        <RouterLink to="/lms/dap-an-cau-hoi">Đáp án câu hỏi</RouterLink>
        <RouterLink to="/lms/bai-tap-online">Bài tập online</RouterLink>
        <RouterLink to="/lms/bai-tap-online-cau-hoi">Bài tập online câu hỏi</RouterLink>
        <RouterLink to="/lms/dot-giao-bai-online">Đợt giao bài online</RouterLink>
        <RouterLink to="/lms/bai-tap-lop">Bài tập lớp</RouterLink>
        <RouterLink to="/lms/lich-su-bai-tap-lop">Lịch sử bài tập lớp</RouterLink>
        <RouterLink to="/lms/lan-lam-bai-online">Lần làm bài online</RouterLink>
        <RouterLink to="/lms/cau-tra-loi-sinh-vien">Câu trả lời sinh viên</RouterLink>
        <RouterLink to="/lms/cau-tra-loi-chon-dap-an">Câu trả lời chọn đáp án</RouterLink>
        <RouterLink to="/lms/ket-qua-bai-tap-lop">Kết quả bài tập lớp</RouterLink>
        <RouterLink to="/lms/lich-su-chinh-diem-bai-tap">Lịch sử chỉnh điểm bài tập</RouterLink>
        <RouterLink to="/lms/lich-su-day-diem-lms">Lịch sử đẩy điểm LMS</RouterLink>
    </aside>
    <main class="module-content">
      <RouterView />
    </main>
  </section>
</template>

<script setup>
import { RouterLink, RouterView } from 'vue-router';
</script>

<style scoped>
.module-layout {
  width: 100%;
  height: 100%;
  min-height: 0;
  display: grid;
  grid-template-columns: 250px minmax(0, 1fr);
  background: #f4f6f9;
  overflow: hidden;
}

.module-menu {
  width: 250px;
  height: 100%;
  min-height: 0;
  background: #0f172a;
  color: #fff;
  padding: 14px 12px;
  display: flex;
  flex-direction: column;
  gap: 6px;
  overflow-y: auto;
  overflow-x: hidden;
}

.module-menu h2 {
  margin: 0 6px 12px;
  font-size: 18px;
  line-height: 1.2;
}

.module-menu a {
  display: block;
  color: #dbeafe;
  text-decoration: none;
  padding: 9px 10px;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 700;
  line-height: 1.25;
}

.module-menu a.router-link-active {
  background: #2563eb;
  color: #fff;
}

.module-content {
  min-width: 0;
  height: 100%;
  min-height: 0;
  overflow: auto;
  padding: 14px;
  background: #f4f6f9;
}

@media (max-width: 900px) {
  .module-layout {
    grid-template-columns: 1fr;
    grid-template-rows: auto minmax(0, 1fr);
  }

  .module-menu {
    width: 100%;
    height: auto;
    max-height: 220px;
  }
}
</style>
