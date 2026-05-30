import { ROLES } from '@/core/constants/roles'
import { requireAuth } from '@/core/guards/authGuard'
import { requireAdmin } from '@/core/guards/adminGuard'

import HeThongLayout from './layouts/HeThongLayout.vue'
import AdminQuanLyTaiKhoan from './pages/AdminQuanLyTaiKhoan.vue'
import AdminCapNhatTaiKhoan from './pages/AdminCapNhatTaiKhoan.vue'
import AdminDoiMatKhauTaiKhoan from './pages/AdminDoiMatKhauTaiKhoan.vue'
import AdminCrudGocHeThong from './pages/AdminCrudGocHeThong.vue'
import AdminNhatKyDangNhap from './pages/AdminNhatKyDangNhap.vue'
import AdminNhatKyHeThong from './pages/AdminNhatKyHeThong.vue'
import AdminTepDinhKem from './pages/AdminTepDinhKem.vue'
export const heThongRoutes = [
    {
        path: 'he-thong',
        component: HeThongLayout,
        beforeEnter: [requireAuth, requireAdmin],
        meta: {
            module: 'heThong',
            title: 'Hệ thống',
            roles: [ROLES.ADMIN, ROLES.DAO_TAO]
        },
        children: [
            {
                path: '',
                redirect: { name: 'HeThong.QuanLyTaiKhoan' }
            },
            {
                path: 'tai-khoan',
                name: 'HeThong.QuanLyTaiKhoan',
                component: AdminQuanLyTaiKhoan,
                meta: {
                    title: 'Quản lý tài khoản',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },

            {
                path: 'cap-nhat-tai-khoan',
                name: 'HeThong.CapNhatTaiKhoan',
                component: AdminCapNhatTaiKhoan,
                meta: {
                    title: 'Cập nhật tài khoản',
                    roles: [ROLES.ADMIN]
                }
            },
            {
                path: 'doi-mat-khau-tai-khoan',
                name: 'HeThong.DoiMatKhauTaiKhoan',
                component: AdminDoiMatKhauTaiKhoan,
                meta: {
                    title: 'Đổi mật khẩu tài khoản',
                    roles: [ROLES.ADMIN]
                }
            },
            {
                path: 'crud-goc',
                name: 'HeThong.CrudGoc',
                component: AdminCrudGocHeThong,
                meta: {
                    title: 'Quản lý CRUD gốc',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },
            {
                path: 'nhat-ky-dang-nhap',
                name: 'HeThong.NhatKyDangNhap',
                component: AdminNhatKyDangNhap,
                meta: {
                    title: 'Nhật ký đăng nhập',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },
            {
                path: 'nhat-ky-he-thong',
                name: 'HeThong.NhatKyHeThong',
                component: AdminNhatKyHeThong,
                meta: {
                    title: 'Nhật ký hệ thống',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },
            {
                path: 'tep-dinh-kem',
                name: 'HeThong.TepDinhKem',
                component: AdminTepDinhKem,
                meta: {
                    title: 'Quản lý tệp đính kèm',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            }

        ]
    }
]