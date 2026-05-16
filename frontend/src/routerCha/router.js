import { createRouter, createWebHistory } from 'vue-router'

import AdminLayout from '../layouts/AdminLayout.vue'
import AuthLayout from '../layouts/AuthLayout.vue'
import PublicLayout from '../layouts/PublicLayout.vue'

import AdminDashboard from '../shared/components/AdminDashboard.vue'

import { requireAuth } from '../core/guards/authGuard'
import { requireAdmin } from '../core/guards/adminGuard'

import { moduleRoutes } from '@/routerCha/moduleRotuer.js'
import {
    authRoutes,
    oauthRoutes
} from '@/modules/auth/authRouter.js'
const routes = [
    ...oauthRoutes,

    {
        path: '/',
        component: PublicLayout,
        children: [
            {
                path: '',
                redirect: '/admin'
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

    {
        path: '/admin',
        component: AdminLayout,
        beforeEnter: [
            requireAuth,
            requireAdmin
        ],
        children: [
            {
                path: '',
                name: 'Admin.Dashboard',
                component: AdminDashboard,
                meta: {
                    title: 'Dashboard'
                }
            },

            ...moduleRoutes
        ]
    },

    {
        path: '/:pathMatch(.*)*',
        redirect: '/admin'
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router