<template>
  <section class="dao-tao-layout">
    <nav class="dao-tao-tabs">

      <RouterLink to="/admin/dao-tao/xem-chuong-trinh">
        Ngành / Chương trình
      </RouterLink>
      <RouterLink to="/admin/dao-tao/mon-hoc-goc">
        Môn gốc
      </RouterLink>
      <RouterLink to="/admin/dao-tao/syllabus-chuong-trinh-goc">
        Syllabus chương trình gốc
      </RouterLink>
      <RouterLink to="/admin/dao-tao/syllabus-mon-hoc-goc">
        Syllabus môn học gốc
      </RouterLink>
      <RouterLink to="/admin/dao-tao/muc-tieu-chuong-trinh-goc">
        Mục tiêu chương trình gốc
      </RouterLink>
      <RouterLink to="/admin/dao-tao/nang-luc-dau-ra-goc">
        Năng lực đầu ra gốc
      </RouterLink>
      <RouterLink to="/admin/dao-tao/vi-tri-viec-lam-goc">
        Vị trí việc làm gốc
      </RouterLink>
      <RouterLink to="/admin/dao-tao/nhom-kien-thuc-mon">
        Nhóm kiến thức - Môn học
      </RouterLink>
    </nav>

    <RouterView :key="$route.fullPath" />
  </section>
</template>

<style scoped>
.dao-tao-layout {
  display: grid;
  gap: 2px;
  margin-top: -20px;
}
.dao-tao-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  padding: 4px 8px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  background: #ffffff;
}

.dao-tao-tabs a {
  padding: 4px 12px;
  border: 1px solid #d1d5db;
  border-radius: 5px;
  color: #374151;
  text-decoration: none;
  font-size: 13px;
  font-weight: 600;
  background: #f9fafb;
}

.dao-tao-tabs a.router-link-active {
  border-color: #2563eb;
  background: #eff6ff;
  color: #1d4ed8;
}
</style>