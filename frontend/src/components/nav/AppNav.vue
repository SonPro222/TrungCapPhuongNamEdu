<!-- src/components/nav/AppNav.vue -->
<template>
  <header class="nav-wrapper">
    <div class="nav-left">
      <router-link :to="homePath" class="brand">
        <span class="logo">PN</span>
        <span class="brand-text">Phương Nam</span>
      </router-link>

      <nav class="nav-menu">
        <router-link
          v-for="item in menus"
          :key="item.path"
          :to="item.path"
          class="nav-item"
        >
          {{ item.label }}
        </router-link>
      </nav>
    </div>

    <div class="nav-right">
      <div class="user-box">
        <span class="user-email">{{ userEmail }}</span>
        <span class="user-role">{{ roleLabel }}</span>
      </div>

      <button class="logout-btn" @click="handleLogout">Đăng xuất</button>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { logout } from '@/api/auth/logoutApi.js';
import { getHomeByRole, normalizeRole } from '@/utils/permission.js';

const router = useRouter();
const role = normalizeRole(localStorage.getItem('role'));
const user = JSON.parse(localStorage.getItem('user') || '{}');

const userEmail = computed(() => user?.email || user?.taiKhoan?.email || 'Tài khoản');

const roleLabel = computed(() => {
  if (role === 'admin') return 'Quản trị';
  if (role === 'dao_tao') return 'Đào tạo';
  if (role === 'nhan_vien') return 'Nhân viên';
  if (role === 'giao_vien') return 'Giáo viên';
  if (role === 'sinh_vien') return 'Sinh viên';
  return 'Người dùng';
});

const homePath = computed(() => getHomeByRole(role));

const menuByRole = {
  admin: [
    { label: 'Hệ thống', path: '/he-thong' },
    { label: 'Đào tạo', path: '/dao-tao' },
    // { label: 'Chương trình', path: '/chuong-trinh' },
    { label: 'Giảng dạy', path: '/giang-day' },
    { label: 'Điểm', path: '/diem' },
    { label: 'Sinh viên', path: '/sinh-vien' },
    { label: 'Thi', path: '/thi' },
    { label: 'Học phí', path: '/hoc-phi' },
    { label: 'LMS', path: '/lms' },
  ],
  dao_tao: [
    { label: 'Đào tạo', path: '/dao-tao' },
    // { label: 'Chương trình', path: '/chuong-trinh' },
    { label: 'Sinh viên', path: '/sinh-vien' },
    { label: 'Giảng dạy', path: '/giang-day' },
    { label: 'Điểm', path: '/diem' },
    { label: 'Thi', path: '/thi' },
    { label: 'LMS', path: '/lms' },
  ],
  nhan_vien: [
    { label: 'Đào tạo', path: '/dao-tao' },
    // { label: 'Chương trình', path: '/chuong-trinh' },
    { label: 'Sinh viên', path: '/sinh-vien' },
    { label: 'Học phí', path: '/hoc-phi' },
    { label: 'Điểm', path: '/diem' },
  ],
  giao_vien: [
    { label: 'Giảng dạy', path: '/giang-day' },
    { label: 'Điểm', path: '/diem' },
    { label: 'Thi', path: '/thi' },
    { label: 'LMS', path: '/lms' },
  ],
  sinh_vien: [
    { label: 'Sinh viên', path: '/sinh-vien' },
    { label: 'Điểm', path: '/diem' },
    { label: 'Thi', path: '/thi' },
    { label: 'Học phí', path: '/hoc-phi' },
    { label: 'LMS', path: '/lms' },
  ],
};

const menus = computed(() => menuByRole[role] || []);

const handleLogout = () => {
  logout();
  router.replace('/');
};
</script>

<style scoped>
.nav-wrapper {
  width: 100%;
  height: 52px;
  min-height: 52px;
  background: #0f172a;
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 14px;
  gap: 12px;
  flex-shrink: 0;
}

.nav-left {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 0;
  flex: 1;
}

.brand {
  display: flex;
  align-items: center;
  gap: 8px;
  color: white;
  flex-shrink: 0;
}

.logo {
  width: 30px;
  height: 30px;
  border-radius: 9px;
  background: linear-gradient(135deg, #2563eb, #06b6d4);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 900;
}

.brand-text {
  font-size: 13px;
  font-weight: 900;
  white-space: nowrap;
}

.nav-menu {
  display: flex;
  align-items: center;
  gap: 4px;
  min-width: 0;
  overflow-x: auto;
  scrollbar-width: none;
}

.nav-menu::-webkit-scrollbar {
  display: none;
}

.nav-item {
  flex-shrink: 0;
  color: #e2e8f0;
  padding: 7px 9px;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 700;
  white-space: nowrap;
}

.nav-item:hover,
.nav-item.router-link-active {
  background: #1e293b;
  color: #38bdf8;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.user-box {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  max-width: 190px;
}

.user-email {
  max-width: 190px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 12px;
  font-weight: 800;
}

.user-role {
  color: #94a3b8;
  font-size: 11px;
}

.logout-btn {
  height: 32px;
  background: #ef4444;
  border: none;
  border-radius: 8px;
  padding: 0 11px;
  color: white;
  font-size: 12px;
  font-weight: 800;
  cursor: pointer;
  white-space: nowrap;
}

.logout-btn:hover {
  background: #dc2626;
}
</style>
