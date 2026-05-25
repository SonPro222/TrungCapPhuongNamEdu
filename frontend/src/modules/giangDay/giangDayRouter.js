import { ROLES } from '@/core/constants/roles'
import { requireAuth } from '@/core/guards/authGuard'
import { requireAdmin } from '@/core/guards/adminGuard'
import AdminQuanLyGiangDay from './pages/AdminQuanLyGiangDay.vue'
import GiangDayLayout from './layouts/GiangDayLayout.vue'
import AdminGiaoVien from './pages/AdminGiaoVien.vue'
import AdminPhongHoc from './pages/AdminPhongHoc.vue'
import AdminCaHoc from './pages/AdminCaHoc.vue'
import AdminLopHocPhan from './pages/AdminLopHocPhan.vue'
import AdminPhanCongGiangDay from './pages/AdminPhanCongGiangDay.vue'
import AdminLichHoc from './pages/AdminLichHoc.vue'
import AdminDiemDanh from './pages/AdminDiemDanh.vue'

export const giangDayRoutes = [
    {
        path: 'giang-day',
        component: GiangDayLayout,
        beforeEnter: [requireAuth, requireAdmin],
        meta: {
            module: 'giangDay',
            title: 'Giảng dạy',
            roles: [ROLES.ADMIN, ROLES.DAO_TAO]
        },
        children: [
            {
                path: 'quan-ly',
                name: 'GiangDay.QuanLy',
                component: AdminQuanLyGiangDay,
                meta: {
                    title: 'Quản lý giảng dạy',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },
            {
                path: 'giao-vien',
                name: 'GiangDay.GiaoVien',
                component: AdminGiaoVien,
                meta: {
                    title: 'Quản lý giáo viên',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },
            {
                path: 'phong-hoc',
                name: 'GiangDay.PhongHoc',
                component: AdminPhongHoc,
                meta: {
                    title: 'Quản lý phòng học',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },
            {
                path: 'ca-hoc',
                name: 'GiangDay.CaHoc',
                component: AdminCaHoc,
                meta: {
                    title: 'Quản lý ca học',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },
            {
                path: 'lop-hoc-phan',
                name: 'GiangDay.LopHocPhan',
                component: AdminLopHocPhan,
                meta: {
                    title: 'Quản lý lớp học phần',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },
            {
                path: 'phan-cong',
                name: 'GiangDay.PhanCong',
                component: AdminPhanCongGiangDay,
                meta: {
                    title: 'Phân công giảng dạy',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },
            {
                path: 'lich-hoc',
                name: 'GiangDay.LichHoc',
                component: AdminLichHoc,
                meta: {
                    title: 'Quản lý lịch học',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },
            {
                path: 'diem-danh',
                name: 'GiangDay.DiemDanh',
                component: AdminDiemDanh,
                meta: {
                    title: 'Quản lý điểm danh',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            }
        ]
    }
]