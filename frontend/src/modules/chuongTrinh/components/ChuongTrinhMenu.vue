<template>
  <nav class="chuong-trinh-menu">
    <RouterLink to="/admin/chuong-trinh/xay-dung">Xây dựng chương trình</RouterLink>
    <RouterLink to="/admin/chuong-trinh/dao-tao-xem">Đào tạo xem</RouterLink>
  </nav>
</template>

<style scoped>
.chuong-trinh-menu {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 8px 12px;
  border-bottom: 1px solid #e5e7eb;
  background: #ffffff;
}
.chuong-trinh-menu a {
  padding: 6px 10px;
  border: 1px solid #cbd5e1;
  border-radius: 4px;
  color: #334155;
  text-decoration: none;
  font-size: 13px;
}
.chuong-trinh-menu a.router-link-active {
  background: #2563eb;
  color: #ffffff;
  border-color: #2563eb;
}
</style>
