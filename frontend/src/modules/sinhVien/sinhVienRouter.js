import SinhVienLayout from './layouts/SinhVienLayout.vue'
import AdminSinhVienPage from './pages/AdminSinhVienPage.vue'
import AdminSinhVienTheoLopHocPhan from './pages/AdminSinhVienTheoLopHocPhan.vue'
import AdminLopHanhChinhPage from './pages/AdminLopHanhChinhPage.vue'
import AdminTaoLopHocPhanPage from './pages/AdminTaoLopHocPhanPage.vue'
import AdminSinhVienBaoLuuPage from './pages/AdminSinhVienBaoLuuPage.vue'
export const sinhVienRoutes = [
    {
        path: '/admin/sinh-vien',
        component: SinhVienLayout,
        meta: { requiresAuth: true },
        children: [
            {
                path: 'lop-hanh-chinh',
                name: 'AdminLopHanhChinh',
                component: AdminLopHanhChinhPage,
                meta: { requiresAuth: true }
            },
            {
                path: 'tao-lop-hoc-phan',
                name: 'AdminTaoLopHocPhan',
                component: AdminTaoLopHocPhanPage,
                meta: { requiresAuth: true }
            },
            {
                path: '',
                name: 'AdminSinhVien',
                component: AdminSinhVienPage,
                meta: { requiresAuth: true }
            },
            {
                path: 'lop-hoc-phan',
                name: 'AdminSinhVienTheoLopHocPhan',
                component: AdminSinhVienTheoLopHocPhan,
                meta: { requiresAuth: true }
            },
            {
                path: 'bao-luu',
                name: 'AdminSinhVienBaoLuu',
                component: AdminSinhVienBaoLuuPage,
                meta: { requiresAuth: true }
            },
        ]
    }
]