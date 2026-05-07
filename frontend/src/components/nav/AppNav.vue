<template>
  <header class="app-nav">
    <RouterLink class="brand" to="/">
      <span class="brand-mark">PN</span>
      <span>Đào tạo Phương Nam</span>
    </RouterLink>

    <nav class="nav-links">
      <RouterLink to="/chuong-trinh/nganh">Chương trình</RouterLink>
      <RouterLink to="/dao-tao">Đào tạo</RouterLink>
      <RouterLink to="/sinh-vien">Sinh viên</RouterLink>
      <RouterLink to="/giang-day">Giảng dạy</RouterLink>
      <RouterLink to="/diem">Điểm</RouterLink>
    </nav>
  </header>
</template>

<script setup>
</script>

<style scoped>
.app-nav {
  height: 58px;
  flex: 0 0 58px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 0 18px;
  border-bottom: 1px solid #e2e8f0;
  background: #ffffff;
  box-shadow: 0 8px 26px rgba(15, 23, 42, 0.06);
}

.brand {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  color: #0f172a;
  font-weight: 950;
  text-decoration: none;
  white-space: nowrap;
}

.brand-mark {
  width: 34px;
  height: 34px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  background: #2563eb;
  color: #ffffff;
  font-size: 13px;
  letter-spacing: -0.4px;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 4px;
  overflow-x: auto;
}

.nav-links a {
  padding: 8px 11px;
  border-radius: 999px;
  color: #475569;
  font-size: 13px;
  font-weight: 800;
  text-decoration: none;
  white-space: nowrap;
}

.nav-links a:hover,
.nav-links a.router-link-active {
  background: #eff6ff;
  color: #1d4ed8;
}

@media (max-width: 768px) {
  .app-nav {
    align-items: flex-start;
    height: auto;
    flex-direction: column;
    padding: 10px 12px;
  }

  .nav-links {
    width: 100%;
  }
}
</style>
