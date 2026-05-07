<template>
  <header class="app-nav">
    <RouterLink class="brand" :to="homeUrl">
      <span class="brand-mark">PN</span>
      <span>Đào tạo Phương Nam</span>
    </RouterLink>

    <nav class="nav-links">
      <RouterLink v-for="item in visibleNavItems" :key="item.to" :to="item.to">
        {{ item.label }}
      </RouterLink>
    </nav>

    <button class="logout-btn" type="button" @click="handleLogout">Đăng xuất</button>
  </header>
</template>

<script setup>
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { canAccessPath, clearAuthStorage, getHomeByRole, normalizeRole } from '@/utils/permission.js';

const router = useRouter();
const role = computed(() => normalizeRole(localStorage.getItem('role')));
const homeUrl = computed(() => getHomeByRole(role.value));

const navItems = [
  { label: 'Chương trình', to: '/chuong-trinh/nganh', guard: '/chuong-trinh' },
  { label: 'Đào tạo', to: '/dao-tao', guard: '/dao-tao' },
  { label: 'Điểm', to: '/diem', guard: '/diem' },
  { label: 'Giảng dạy', to: '/giang-day', guard: '/giang-day' },
  { label: 'Học phí', to: '/hoc-phi', guard: '/hoc-phi' },
  { label: 'LMS', to: '/lms', guard: '/lms' },
  { label: 'Sinh viên', to: '/sinh-vien', guard: '/sinh-vien' },
  { label: 'Thi', to: '/thi', guard: '/thi' },
  { label: 'Hệ thống', to: '/he-thong', guard: '/he-thong' },
];

const visibleNavItems = computed(() => navItems.filter((item) => canAccessPath(item.guard, role.value)));

const handleLogout = () => {
  clearAuthStorage();
  router.replace('/');
};
</script>

<style scoped>
.app-nav {
  min-height: 58px;
  flex: 0 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 0 18px;
  border-bottom: 1px solid #e2e8f0;
  background: #ffffff;
  box-shadow: 0 8px 26px rgba(15, 23, 42, 0.06);
}
.brand { display: inline-flex; align-items: center; gap: 10px; color: #0f172a; font-weight: 950; text-decoration: none; white-space: nowrap; }
.brand-mark { width: 34px; height: 34px; display: inline-flex; align-items: center; justify-content: center; border-radius: 12px; background: #2563eb; color: #ffffff; font-size: 13px; letter-spacing: -0.4px; }
.nav-links { display: flex; align-items: center; gap: 4px; overflow-x: auto; flex: 1; }
.nav-links a { padding: 8px 11px; border-radius: 999px; color: #475569; font-size: 13px; font-weight: 800; text-decoration: none; white-space: nowrap; }
.nav-links a:hover, .nav-links a.router-link-active { background: #eff6ff; color: #1d4ed8; }
.logout-btn { border: 1px solid #fecaca; background: #fff1f2; color: #be123c; border-radius: 999px; padding: 8px 12px; font-size: 13px; font-weight: 900; cursor: pointer; white-space: nowrap; }
.logout-btn:hover { background: #ffe4e6; }
@media (max-width: 768px) { .app-nav { align-items: flex-start; height: auto; flex-direction: column; padding: 10px 12px; } .nav-links { width: 100%; } .logout-btn { align-self: flex-end; } }
</style>
