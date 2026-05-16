<template>
  <nav class="dao-tao-menu">
    <RouterLink to="/admin/dao-tao">Tổng quan</RouterLink>
    <RouterLink to="/admin/dao-tao/nganh">Ngành</RouterLink>
    <RouterLink to="/admin/dao-tao/trinh-do-dao-tao">Trình độ</RouterLink>
    <RouterLink to="/admin/dao-tao/loai-chuong-trinh">Loại chương trình</RouterLink>
    <RouterLink to="/admin/dao-tao/khoa-dao-tao">Khóa đào tạo</RouterLink>
    <RouterLink to="/admin/dao-tao/khung-ky">Khung kỳ</RouterLink>
    <RouterLink to="/admin/dao-tao/lop-hanh-chinh">Lớp hành chính</RouterLink>
  </nav>
</template>
<style scoped>
.dao-tao-menu {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.dao-tao-menu a {
  padding: 8px 12px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  background: var(--color-white);
  color: var(--color-text);
}

.dao-tao-menu a.router-link-active {
  background: var(--color-primary);
  color: white;
  border-color: var(--color-primary);
}
</style>