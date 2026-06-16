import { ROLES } from '@/core/constants/roles'
import { requireAuth } from '@/core/guards/authGuard'
import { requireAdmin } from '@/core/guards/adminGuard'

import GiangDayLayout from './layouts/GiangDayLayout.vue'
import AdminGiangDayQuanLyLopHocPhan from './pages/AdminGiangDayQuanLyLopHocPhan.vue'
import AdminChiTietGiangDayLopHocPhan from './pages/AdminChiTietGiangDayLopHocPhan.vue'
import AdminChiTietBuoiHoc from './pages/AdminChiTietBuoiHoc.vue'
import AdminGiaoVien from './pages/AdminGiaoVien.vue'
import AdminChiTietGiaoVien from './pages/AdminChiTietGiaoVien.vue'
import AdminPhongHoc from './pages/AdminPhongHoc.vue'
import AdminCaHoc from './pages/AdminCaHoc.vue'
import AdminPhanCongGiangDay from './pages/AdminPhanCongGiangDay.vue'
import AdminLichHoc from './pages/AdminLichHoc.vue'
import SinhVienXemLichHoc from './pages/SinhVienXemLichHoc.vue'
import GiangVienXemLichDay from './pages/GiangVienXemLichDay.vue'
import AdminRangBuocLichHoc from './pages/AdminRangBuocLichHoc.vue'
import NgayNghi from './pages/AdminNgayNghi.vue'
import AdminGiaoVienMonHoc from './pages/AdminGiaoVienMonHoc.vue'
import AdminGiangVienDangKyGiangDay from './pages/AdminGiangVienDangKyGiangDay.vue'
function taoQueryFlow(params = {}) {
    const query = {}

    if (params.nganhId) query.nganhId = params.nganhId
    if (params.chuongTrinhId) query.chuongTrinhId = params.chuongTrinhId
    if (params.versionId) query.versionId = params.versionId
    if (params.khungKyId) query.khungKyId = params.khungKyId

    return query
}

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
                component: AdminGiangDayQuanLyLopHocPhan,
                meta: {
                    title: 'Quản lý lớp học phần',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO, ROLES.GIAO_VIEN]
                }
            },
            {
                path: 'lop-hoc-phan/nganh/:nganhId/chuong-trinh',
                name: 'GiangDay.ChonChuongTrinh',
                redirect: (to) => ({
                    name: 'GiangDay.LopHocPhan',
                    query: taoQueryFlow(to.params)
                })
            },
            {
                path: 'lop-hoc-phan/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version',
                name: 'GiangDay.ChonVersion',
                redirect: (to) => ({
                    name: 'GiangDay.LopHocPhan',
                    query: taoQueryFlow(to.params)
                })
            },
            {
                path: 'lop-hoc-phan/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/ky',
                name: 'GiangDay.ChonKy',
                redirect: (to) => ({
                    name: 'GiangDay.LopHocPhan',
                    query: taoQueryFlow(to.params)
                })
            },
            {
                path: 'lop-hoc-phan/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/ky/:khungKyId',
                name: 'GiangDay.LopHocPhanTheoKy',
                redirect: (to) => ({
                    name: 'GiangDay.LopHocPhan',
                    query: taoQueryFlow(to.params)
                })
            },
            {
                path: 'lop-hoc-phan/:id/buoi-hoc',
                name: 'GiangDay.ChiTietLopHocPhan',
                component: AdminChiTietGiangDayLopHocPhan,
                meta: {
                    title: 'Điều phối giảng dạy',
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
            },

            {
                path: 'sinh-vien/xem-lich-hoc',
                name: 'GiangDay.SinhVienXemLichHoc',
                component: SinhVienXemLichHoc,
                meta: {
                    title: 'Sinh viên xem lịch học',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO, ROLES.SINH_VIEN]
                }
            },
            {
                path: 'giang-vien/xem-lich-day',
                name: 'GiangDay.GiangVienXemLichDay',
                component: GiangVienXemLichDay,
                meta: {
                    title: 'Giảng viên xem lịch dạy',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO, ROLES.GIAO_VIEN]
                }
            },
            {
                path: 'rang-buoc-lich-hoc',
                name: 'GiangDay.RangBuocLichHoc',
                component: AdminRangBuocLichHoc,
                meta: {
                    title: 'Ràng buộc xếp lịch',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO, ROLES.GIAO_VIEN]
                }
            }
            ,
            {
                path: 'ngay-nghi',
                name: 'GiangDay.NgayNghi',
                component: NgayNghi,
                meta: {
                    title: 'Quản lý lịch nghỉ',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },
            {
                path: 'giao-vien-mon-hoc',
                name: 'GiangDay.GiaoVienMonHoc',
                component: AdminGiaoVienMonHoc,
                meta: { title: 'Giảng viên dạy môn', roles: [ROLES.ADMIN] }
            },
            {
                path: 'giang-vien-dang-ky-giang-day',
                name: 'GiangDay.GiangVienDangKyGiangDay',
                component: AdminGiangVienDangKyGiangDay,
                meta: { title: 'Đăng ký số tiết', roles: [ROLES.ADMIN] }
            },
        ]
    }
]
