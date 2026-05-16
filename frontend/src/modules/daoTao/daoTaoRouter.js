import DaoTaoLayout from './layouts/DaoTaoLayout.vue'
import AdminDaoTaoHomePage from './pages/AdminDaoTaoHomePage.vue'
import AdminNganhPage from './pages/AdminNganhPage.vue'
import AdminTrinhDoDaoTaoPage from './pages/AdminTrinhDoDaoTaoPage.vue'
import AdminLoaiChuongTrinhPage from './pages/AdminLoaiChuongTrinhPage.vue'
import AdminKhoaDaoTaoPage from './pages/AdminKhoaDaoTaoPage.vue'
import AdminKhungKyPage from './pages/AdminKhungKyPage.vue'
import AdminLopHanhChinhPage from './pages/AdminLopHanhChinhPage.vue'

import { ROLES } from '@/core/constants/roles'
import { requireAuth } from '@/core/guards/authGuard'
import { requireAdmin } from '@/core/guards/adminGuard'

export const daoTaoRoutes = [
    {
        path: 'dao-tao',
        component: DaoTaoLayout,
        beforeEnter: [
            requireAuth,
            requireAdmin
        ],
        meta: {
            module: 'daoTao',
            title: 'Đào tạo',
            roles: [ROLES.ADMIN, ROLES.DAO_TAO]
        },
        children: [
            {
                path: '',
                name: 'DaoTao.Home',
                component: AdminDaoTaoHomePage
            },
            {
                path: 'nganh',
                name: 'DaoTao.Nganh',
                component: AdminNganhPage
            },
            {
                path: 'trinh-do-dao-tao',
                name: 'DaoTao.TrinhDoDaoTao',
                component: AdminTrinhDoDaoTaoPage
            },
            {
                path: 'loai-chuong-trinh',
                name: 'DaoTao.LoaiChuongTrinh',
                component: AdminLoaiChuongTrinhPage
            },
            {
                path: 'khoa-dao-tao',
                name: 'DaoTao.KhoaDaoTao',
                component: AdminKhoaDaoTaoPage
            },
            {
                path: 'khung-ky',
                name: 'DaoTao.KhungKy',
                component: AdminKhungKyPage
            },
            {
                path: 'lop-hanh-chinh',
                name: 'DaoTao.LopHanhChinh',
                component: AdminLopHanhChinhPage
            }
        ]
    }
]