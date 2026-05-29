import { ROLES } from '@/core/constants/roles'
import { requireAuth } from '@/core/guards/authGuard'
import { requireAdmin } from '@/core/guards/adminGuard'

import GiangDayLayout from './layouts/GiangDayLayout.vue'
import AdminGiangDayChonNganh from './pages/AdminGiangDayChonNganh.vue'
import AdminGiangDayChonChuongTrinh from './pages/AdminGiangDayChonChuongTrinh.vue'
import AdminGiangDayChonVersion from './pages/AdminGiangDayChonVersion.vue'
import AdminGiangDayChonKy from './pages/AdminGiangDayChonKy.vue'
import AdminGiangDayLopHocPhanTheoKy from './pages/AdminGiangDayLopHocPhanTheoKy.vue'
import AdminChiTietGiangDayLopHocPhan from './pages/AdminChiTietGiangDayLopHocPhan.vue'
import AdminChiTietBuoiHoc from './pages/AdminChiTietBuoiHoc.vue'
import AdminGiaoVien from './pages/AdminGiaoVien.vue'
import AdminChiTietGiaoVien from './pages/AdminChiTietGiaoVien.vue'
import AdminPhongHoc from './pages/AdminPhongHoc.vue'
import AdminCaHoc from './pages/AdminCaHoc.vue'
import AdminPhanCongGiangDay from './pages/AdminPhanCongGiangDay.vue'
import AdminLichHoc from './pages/AdminLichHoc.vue'

export const giangDayRoutes = [
    {
        path: 'giang-day',
        component: GiangDayLayout,
        beforeEnter: [requireAuth, requireAdmin],
        meta: {
            module: 'giangDay',
            title: 'Giảng dạy',
            roles: [ROLES.ADMIN, ROLES.DAO_TAO, ROLES.GIAO_VIEN]
        },
        children: [
            {
                path: '',
                redirect: {
                    name: 'GiangDay.LopHocPhan'
                }
            },
            {
                path: 'lop-hoc-phan',
                name: 'GiangDay.LopHocPhan',
                component: AdminGiangDayChonNganh,
                meta: {
                    title: 'Chọn ngành',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO, ROLES.GIAO_VIEN]
                }
            },
            {
                path: 'lop-hoc-phan/nganh/:nganhId/chuong-trinh',
                name: 'GiangDay.ChonChuongTrinh',
                component: AdminGiangDayChonChuongTrinh,
                meta: {
                    title: 'Chọn chương trình',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO, ROLES.GIAO_VIEN]
                }
            },
            {
                path: 'lop-hoc-phan/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version',
                name: 'GiangDay.ChonVersion',
                component: AdminGiangDayChonVersion,
                meta: {
                    title: 'Chọn version chương trình',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO, ROLES.GIAO_VIEN]
                }
            },
            {
                path: 'lop-hoc-phan/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/ky',
                name: 'GiangDay.ChonKy',
                component: AdminGiangDayChonKy,
                meta: {
                    title: 'Chọn kỳ',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO, ROLES.GIAO_VIEN]
                }
            },
            {
                path: 'lop-hoc-phan/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/ky/:khungKyId',
                name: 'GiangDay.LopHocPhanTheoKy',
                component: AdminGiangDayLopHocPhanTheoKy,
                meta: {
                    title: 'Lớp học phần theo kỳ',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO, ROLES.GIAO_VIEN]
                }
            },
            {
                path: 'lop-hoc-phan/:id/buoi-hoc',
                name: 'GiangDay.ChiTietLopHocPhan',
                component: AdminChiTietGiangDayLopHocPhan,
                meta: {
                    title: 'Danh sách buổi học',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO, ROLES.GIAO_VIEN]
                }
            },
            {
                path: 'lop-hoc-phan/:lopHocPhanId/buoi-hoc/:lichHocId',
                name: 'GiangDay.ChiTietBuoiHoc',
                component: AdminChiTietBuoiHoc,
                meta: {
                    title: 'Chi tiết buổi học',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO, ROLES.GIAO_VIEN]
                }
            },
            {
                path: 'giao-vien',
                name: 'GiangDay.GiaoVien',
                component: AdminGiaoVien,
                meta: {
                    title: 'Danh sách giáo viên',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO, ROLES.GIAO_VIEN]
                }
            },
            {
                path: 'giao-vien/:id',
                name: 'GiangDay.ChiTietGiaoVien',
                component: AdminChiTietGiaoVien,
                meta: {
                    title: 'Hồ sơ giáo viên',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO, ROLES.GIAO_VIEN]
                }
            },
            {
                path: 'phong-hoc',
                name: 'GiangDay.PhongHoc',
                component: AdminPhongHoc,
                meta: {
                    title: 'Quản lý phòng học',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO, ROLES.GIAO_VIEN]
                }
            },
            {
                path: 'ca-hoc',
                name: 'GiangDay.CaHoc',
                component: AdminCaHoc,
                meta: {
                    title: 'Quản lý ca học',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO, ROLES.GIAO_VIEN]
                }
            },
            {
                path: 'phan-cong',
                name: 'GiangDay.PhanCong',
                component: AdminPhanCongGiangDay,
                meta: {
                    title: 'Phân công giảng dạy',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO, ROLES.GIAO_VIEN]
                }
            },
            {
                path: 'lich-hoc',
                name: 'GiangDay.LichHoc',
                component: AdminLichHoc,
                meta: {
                    title: 'Quản lý lịch học',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO, ROLES.GIAO_VIEN]
                }
            }
        ]
    }
]
