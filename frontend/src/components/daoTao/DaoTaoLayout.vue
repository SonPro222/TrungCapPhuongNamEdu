<template>
  <div class="module-layout">
    <aside class="module-menu">
      <h2>Đào tạo</h2>

      <RouterLink :to="{ name: 'dao-tao-nganh' }">Ngành đào tạo</RouterLink>
      <RouterLink :to="{ name: 'dao-tao-trinh-do' }">Trình độ đào tạo</RouterLink>
      <RouterLink :to="{ name: 'dao-tao-loai-chuong-trinh' }">Loại chương trình</RouterLink>
      <RouterLink :to="{ name: 'dao-tao-khung-ky' }">Khung kỳ</RouterLink>
      <RouterLink :to="{ name: 'dao-tao-khoa-dao-tao' }">Khóa đào tạo</RouterLink>
      <RouterLink :to="{ name: 'dao-tao-lop-hanh-chinh' }">Lớp hành chính</RouterLink>
    </aside>

    <main class="module-content">
      <RouterView />
    </main>
  </div>
</template>

<script setup>
</script>

<style scoped>
.module-layout {
  width: 100%;
  height: 100%;
  min-height: 0;
  display: flex;
  background: #f4f6f9;
  overflow: hidden;
}

.module-menu {
  width: 250px;
  min-width: 250px;
  height: 100%;
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
  flex: 1;
  min-width: 0;
  height: 100%;
  overflow: auto;
  padding: 12px;
  background: #f4f6f9;
}
</style>
