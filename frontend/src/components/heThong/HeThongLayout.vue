<template>
  <div class="module-layout">
    <aside class="module-menu">
      <h2>Hệ thống</h2>
      <RouterLink to="/he-thong/tai-khoan-sinh-vien">Tài khoản sinh viên</RouterLink>
      <RouterLink to="/he-thong/tai-khoan-giang-vien">Tài khoản giảng viên</RouterLink>
      <RouterLink to="/he-thong/nhan-vien">Nhân viên</RouterLink>
      <RouterLink to="/he-thong/dao-tao-admin">Đào tạo / Admin</RouterLink>
      <RouterLink to="/he-thong/vai-tro-quyen">Vai trò / Quyền</RouterLink>
      <RouterLink to="/he-thong/lien-ket-phan-quyen">Liên kết phân quyền</RouterLink>
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
