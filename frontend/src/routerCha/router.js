import { createRouter, createWebHistory } from 'vue-router'

import AdminLayout from '../layouts/AdminLayout.vue'
import AuthLayout from '../layouts/AuthLayout.vue'
import PublicLayout from '../layouts/PublicLayout.vue'
import UserLayout from '../layouts/UserLayout.vue'

import AdminDashboard from '../shared/components/AdminDashboard.vue'

import { requireAuth } from '../core/guards/authGuard'
import { requireAdmin } from '../core/guards/adminGuard'
import { requireUser } from '../core/guards/userGuard'

import { moduleRoutes } from '@/routerCha/moduleRotuer.js'
import { tongTheRoutes } from '@/modules/daoTao/tongTheRouter.js'
import {
    authRoutes,
    oauthRoutes
} from '@/modules/auth/authRouter.js'
import { trienKhaiRoutes } from '@/modules/trienkhai/trienKhaiRouter'

const routes = [
    ...oauthRoutes,

    {
        path: '/',
        component: PublicLayout,
        children: [
            {
                path: '',
                redirect: '/dashboard'
            }
        ]
    },

    {
        path: '/auth',
        component: AuthLayout,
        children: [
            ...authRoutes
        ]
    },

    ...tongTheRoutes,

    {
        path: '/dashboard',
        name: 'Dashboard',
        component: AdminDashboard,
        beforeEnter: [requireAuth],
        meta: {
            title: 'Dashboard'
        }
    },

    {
        path: '/admin',
        component: AdminLayout,
        beforeEnter: [
            requireAuth,
            requireAdmin
        ],
        children: [
            ...moduleRoutes
        ]
    },

    {
        path: '/user',
        component: UserLayout,
        beforeEnter: [
            requireAuth,
            requireUser
        ],
        children: [
            ...trienKhaiRoutes
        ]
    },

    {
        path: '/:pathMatch(.*)*',
        redirect: '/dashboard'
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
