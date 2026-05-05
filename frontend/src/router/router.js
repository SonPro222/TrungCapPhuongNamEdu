// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';

import diemRoutes from '@/router/module/diemRoutes.js';
import giangDayRoutes from '@/router/module/giangDayRoutes.js';
import sinhVienRoutes from '@/router/module/sinhVienRoutes.js';
import thiRoutes from '@/router/module/thiRoutes.js';
import hocPhiRoutes from '@/router/module/hocPhiRoutes.js';
import lmsRoutes from '@/router/module/lmsRoutes.js';
import heThongRoutes from '@/router/module/heThongRoutes.js';
import chuongTrinhRoutes from '@/router/module/chuongTrinhRoutes.js';
import daoTaoRoutes from '@/router/module/daoTaoRoutes.js'
import PublicLayout from '@/components/layout/PublicLayout.vue';
import MainLayout from '@/components/layout/MainLayout.vue';
import HomePage from '@/components/home/Home.vue';

import {
    canAccessPath,
    clearAuthStorage,
    getHomeByRole,
    normalizeRole,
} from '@/utils/permission.js';

const routes = [
    {
        path: '/',
        component: PublicLayout,
        meta: {
            public: true,
        },
        children: [
            {
                path: '',
                name: 'home',
                component: HomePage,
                meta: {
                    public: true,
                },
            },
        ],
    },
    {
        path: '/',
        component: MainLayout,
        children: [
            ...diemRoutes,
            ...giangDayRoutes,
            ...sinhVienRoutes,
            ...thiRoutes,
            ...hocPhiRoutes,
            ...lmsRoutes,
            ...heThongRoutes,
            ...chuongTrinhRoutes,
            ...daoTaoRoutes,
        ],
    },
    {
        path: '/login',
        redirect: '/',
        meta: {
            public: true,
        },
    },
];

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes,
});

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('accessToken') || localStorage.getItem('token');
    const role = normalizeRole(localStorage.getItem('role'));

    if (to.meta.public) {
        if (token && to.path === '/') {
            return next(getHomeByRole(role));
        }

        return next();
    }

    if (!token) {
        clearAuthStorage();
        return next('/');
    }

    if (!role) {
        clearAuthStorage();
        return next('/');
    }

    if (!canAccessPath(to.path, role)) {
        return next(getHomeByRole(role));
    }

    next();
});

export default router;