import { ROLES } from '@/core/constants/roles'
import { requireAuth } from '@/core/guards/authGuard'
import { requireAdmin } from '@/core/guards/adminGuard'

import SinhVienLayout from './layouts/SinhVienLayout.vue'
import AdminSinhVienFlowPage from './pages/AdminSinhVienFlowPage.vue'
import AdminSinhVienBaoLuuPage from './pages/AdminSinhVienBaoLuuPage.vue'
import AdminSinhVienDanhSachPage from './pages/AdminSinhVienDanhSachPage.vue'
import AdminSinhVienChiTietPage from './pages/AdminSinhVienChiTietPage.vue'

export const sinhVienRoutes = [
    {
        path: 'sinh-vien',
        component: SinhVienLayout,
        beforeEnter: [requireAuth, requireAdmin],
        meta: {
            module: 'sinhVien',
            title: 'Sinh viên',
            roles: [ROLES.ADMIN]
        },
        children: [
            {
                path: '',
                name: 'AdminSinhVienFlow',
                component: AdminSinhVienFlowPage,
                meta: {
                    title: 'Quản lý sinh viên',
                    roles: [ROLES.ADMIN]
                }
            },
            {
                path: 'bao-luu',
                name: 'AdminSinhVienBaoLuu',
                component: AdminSinhVienBaoLuuPage,
                meta: {
                    title: 'Danh sách sinh viên bảo lưu',
                    roles: [ROLES.ADMIN]
                }
            },
            {
                path: 'danh-sach',
                name: 'AdminSinhVienDanhSach',
                component: AdminSinhVienDanhSachPage,
                meta: {
                    title: 'Danh sách sinh viên toàn trường',
                    roles: [ROLES.ADMIN]
                }
            },
            {
                path: 'danh-sach/:id',
                name: 'AdminSinhVienChiTiet',
                component: AdminSinhVienChiTietPage,
                meta: {
                    title: 'Chi tiết hồ sơ sinh viên',
                    roles: [ROLES.ADMIN]
                }
            }
        ]
    }
]