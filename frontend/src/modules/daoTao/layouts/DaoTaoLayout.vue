<template>
  <section class="dao-tao-layout">
    <nav class="dao-tao-tabs">
      <RouterLink to="/admin/dao-tao/xem-chuong-trinh">
        Ngành / Chương trình
      </RouterLink>
      <RouterLink to="/admin/dao-tao/mon-hoc-goc">
        Môn gốc
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
  gap: 14px;
}

.dao-tao-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 10px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  background: #ffffff;
}

.dao-tao-tabs a {
  padding: 8px 12px;
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