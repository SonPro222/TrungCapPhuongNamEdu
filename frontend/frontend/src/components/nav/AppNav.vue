<template>
  <header class="pn-admin-header">
    <RouterLink class="pn-admin-brand" :to="homeUrl">
      <span class="pn-admin-logo">PN</span>
      <span>
        <b>Đào tạo Phương Nam</b>
        <small>Hệ thống quản lý đào tạo</small>
      </span>
    </RouterLink>

    <nav class="pn-admin-menu" aria-label="Module hệ thống">
      <RouterLink v-for="item in visibleNavItems" :key="item.to" :to="item.to">
        {{ item.label }}
      </RouterLink>
    </nav>

    <div class="pn-admin-user">
      <span class="pn-role">{{ roleLabel }}</span>
      <button class="pn-logout" type="button" @click="handleLogout">Đăng xuất</button>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { canAccessPath, clearAuthStorage, getHomeByRole, normalizeRole } from '@/utils/permission.js';

const router = useRouter();
const role = computed(() => normalizeRole(localStorage.getItem('role')));
const homeUrl = computed(() => getHomeByRole(role.value));
const roleLabel = computed(() => (role.value === 'ADMIN' ? 'ROLE_ADMIN' : role.value));

const navItems = [
  { label: 'Chương trình', to: '/chuong-trinh', guard: '/chuong-trinh' },
  { label: 'Đào tạo', to: '/dao-tao', guard: '/dao-tao' },
  { label: 'Sinh viên', to: '/sinh-vien', guard: '/sinh-vien' },
  { label: 'Giảng dạy', to: '/giang-day', guard: '/giang-day' },
  { label: 'Điểm', to: '/diem', guard: '/diem' },
  { label: 'Thi', to: '/thi', guard: '/thi' },
  { label: 'Học phí', to: '/hoc-phi', guard: '/hoc-phi' },
  { label: 'LMS', to: '/lms', guard: '/lms' },
  { label: 'Hệ thống', to: '/he-thong', guard: '/he-thong' },
];

const visibleNavItems = computed(() => navItems.filter((item) => canAccessPath(item.guard, role.value)));

const handleLogout = () => {
  clearAuthStorage();
  router.replace('/login');
};
</script>

<style scoped>
.pn-admin-header{height:56px;flex:0 0 auto;display:grid;grid-template-columns:250px minmax(0,1fr) auto;align-items:center;gap:12px;padding:0 14px;border-bottom:1px solid #dbe3ee;background:#fff;box-shadow:0 2px 10px rgba(15,23,42,.04);z-index:10}.pn-admin-brand{display:inline-flex;align-items:center;gap:10px;color:#111827;text-decoration:none;min-width:0}.pn-admin-logo{width:32px;height:32px;border-radius:8px;background:#2563eb;color:#fff;display:flex;align-items:center;justify-content:center;font-size:12px;font-weight:900}.pn-admin-brand b{display:block;font-size:14px;font-weight:900;line-height:1.15;white-space:nowrap}.pn-admin-brand small{display:block;margin-top:1px;color:#64748b;font-size:11px;font-weight:600;white-space:nowrap}.pn-admin-menu{display:flex;align-items:center;gap:2px;min-width:0;overflow-x:auto;padding-bottom:1px}.pn-admin-menu a{display:inline-flex;align-items:center;height:30px;border-radius:7px;padding:0 8px;color:#334155;font-size:12.5px;font-weight:800;text-decoration:none;white-space:nowrap}.pn-admin-menu a:hover,.pn-admin-menu a.router-link-active{background:#eef5ff;color:#1d4ed8}.pn-admin-user{display:flex;align-items:center;gap:8px}.pn-role{height:28px;display:inline-flex;align-items:center;border:1px solid #dbe4ef;border-radius:999px;background:#f8fafc;padding:0 10px;color:#334155;font-size:11.5px;font-weight:800}.pn-logout{height:30px;border:1px solid #fecaca;border-radius:999px;background:#fff1f2;color:#be123c;padding:0 12px;font-size:12px;font-weight:850;cursor:pointer}.pn-logout:hover{background:#ffe4e6}@media(max-width:1100px){.pn-admin-header{grid-template-columns:220px minmax(0,1fr)}.pn-admin-user{grid-column:2;justify-content:flex-end}.pn-admin-menu{grid-column:1 / -1;order:3}}@media(max-width:720px){.pn-admin-header{height:auto;display:flex;align-items:flex-start;flex-direction:column;padding:10px 12px}.pn-admin-menu{width:100%}.pn-admin-user{width:100%;justify-content:space-between}}
</style>
