import { createRouter, createWebHistory } from 'vue-router';

import diemRoutes from '@/modules/diem/diemRouter.js';
import giangDayRoutes from '@/modules/giangDay/giangDayRouter.js';
import sinhVienRoutes from '@/modules/sinhVien/sinhVienRouter.js';
import thiRoutes from '@/modules/thi/thiRouter.js';
import hocPhiRoutes from '@/modules/hocPhi/hocPhiRouter.js';
import lmsRoutes from '@/modules/LMS/LmsRouter.js';
import heThongRoutes from '@/modules/heThong/giangDayRouter.js';
import chuongTrinhRoutes from '@/modules/chuongTrinh/chuongTrinhRouter.js';
import daoTaoRoutes from '@/modules/daoTao/daoTaoRouter.js';
import PublicLayout from '@/components/layout/PublicLayout.vue';
import MainLayout from '@/components/layout/MainLayout.vue';
import HomePage from '@/components/home/Home.vue';
import ModuleDashboard from '@/components/module/ModuleDashboard.vue';
import HeThongPage from '@/shared/pages/heThongPage.vue';
import QuyTrinhPage from '@/shared/pages/quyTrinhPage.vue';
import DuLieuPage from '@/shared/pages/duLieuPage.vue';

import { canAccessPath, clearAuthStorage, getHomeByRole, normalizeRole } from '@/utils/permission.js';

const moduleRoutes = [
  ...chuongTrinhRoutes,
  ...daoTaoRoutes,
  ...diemRoutes,
  ...giangDayRoutes,
  ...heThongRoutes,
  ...hocPhiRoutes,
  ...lmsRoutes,
  ...sinhVienRoutes,
  ...thiRoutes,
];

const routes = [
  {
    path: '/',
    component: PublicLayout,
    meta: { public: true },
    children: [
      { path: '', name: 'home', component: HomePage, meta: { public: true } },
      { path: 'login', name: 'login', component: HomePage, meta: { public: true } },
    ],
  },
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: 'dashboard', redirect: '/he-thong' },
      { path: 'he-thong', name: 'he-thong', component: HeThongPage, meta: { title: 'Hệ thống' } },
      { path: 'quy-trinh', name: 'quy-trinh', component: QuyTrinhPage, meta: { title: 'Quy trình' } },
      { path: 'du-lieu', name: 'du-lieu', component: DuLieuPage, meta: { title: 'Dữ liệu' } },
      ...moduleRoutes,
    ],
  },
  { path: '/:pathMatch(.*)*', redirect: '/' },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('accessToken') || localStorage.getItem('token');
  const role = normalizeRole(localStorage.getItem('role'));

  if (to.meta.public) {
    if (token && to.path === '/') return next(getHomeByRole(role));
    return next();
  }

  if (!token) {
    clearAuthStorage();
    return next('/login');
  }

  if (!canAccessPath(to.path, role)) {
    return next(getHomeByRole(role));
  }

  return next();
});

export default router;
