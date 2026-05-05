<template>
  <div class="module-layout">
    <aside class="module-menu">
      <h2>Chương trình</h2>
      <RouterLink to="/chuong-trinh/chuong-trinh">Chương trình đào tạo</RouterLink>
      <RouterLink to="/chuong-trinh/chuong-trinh-version">Version chương trình</RouterLink>
      <RouterLink to="/chuong-trinh/syllabus-chuong-trinh">Syllabus chương trình</RouterLink>
      <RouterLink to="/chuong-trinh/muc-tieu-chuong-trinh">Mục tiêu chương trình</RouterLink>
      <RouterLink to="/chuong-trinh/nang-luc-dau-ra">Năng lực đầu ra</RouterLink>
      <RouterLink to="/chuong-trinh/vi-tri-viec-lam">Vị trí việc làm</RouterLink>
      <RouterLink to="/chuong-trinh/mon-hoc">Môn học</RouterLink>
      <RouterLink to="/chuong-trinh/nhom-kien-thuc">Nhóm kiến thức</RouterLink>
      <RouterLink to="/chuong-trinh/chuong-trinh-mon">Môn trong chương trình</RouterLink>
      <RouterLink to="/chuong-trinh/mon-tien-quyet">Môn tiên quyết</RouterLink>
      <RouterLink to="/chuong-trinh/nhom-tu-chon">Nhóm tự chọn</RouterLink>
      <RouterLink to="/chuong-trinh/mon-tu-chon">Môn tự chọn</RouterLink>
      <RouterLink to="/chuong-trinh/syllabus-mon-hoc">Syllabus môn học</RouterLink>
      <RouterLink to="/chuong-trinh/syllabus-chuong-bai">Syllabus chương/bài</RouterLink>
      <RouterLink to="/chuong-trinh/syllabus-tai-lieu">Tài liệu syllabus</RouterLink>
      <RouterLink to="/chuong-trinh/dieu-kien-mon-hoc">Điều kiện môn học</RouterLink>
      <RouterLink to="/chuong-trinh/quy-doi-diem">Quy đổi điểm</RouterLink>
      <RouterLink to="/chuong-trinh/dieu-kien-tot-nghiep">Điều kiện tốt nghiệp</RouterLink>
    </aside>
    <main class="module-content">
      <RouterView />
    </main>
  </div>
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
