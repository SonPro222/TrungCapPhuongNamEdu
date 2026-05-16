import ChuongTrinhLayout from './layouts/ChuongTrinhLayout.vue'

import AdminChuongTrinhPage from './pages/AdminChuongTrinhPage.vue'
import AdminChuongTrinhVersionPage from './pages/AdminChuongTrinhVersionPage.vue'
import AdminChuongTrinhMonPage from './pages/AdminChuongTrinhMonPage.vue'
import AdminNhomKienThucPage from './pages/AdminNhomKienThucPage.vue'
import AdminMucTieuChuongTrinhPage from './pages/AdminMucTieuChuongTrinhPage.vue'
import AdminNangLucDauRaPage from './pages/AdminNangLucDauRaPage.vue'
import AdminViTriViecLamPage from './pages/AdminViTriViecLamPage.vue'
import AdminDieuKienTotNghiepPage from './pages/AdminDieuKienTotNghiepPage.vue'
import AdminNhomTuChonPage from './pages/AdminNhomTuChonPage.vue'
import AdminMonTuChonPage from './pages/AdminMonTuChonPage.vue'
import AdminMonTienQuyetPage from './pages/AdminMonTienQuyetPage.vue'
import AdminMonHocPage from './pages/AdminMonHocPage.vue'
import AdminDieuKienMonHocPage from './pages/AdminDieuKienMonHocPage.vue'
import AdminQuyDoiDiemPage from './pages/AdminQuyDoiDiemPage.vue'
import AdminSyllabusChuongTrinhPage from './pages/AdminSyllabusChuongTrinhPage.vue'
import AdminSyllabusMonHocPage from './pages/AdminSyllabusMonHocPage.vue'
import AdminSyllabusChuongBaiPage from './pages/AdminSyllabusChuongBaiPage.vue'
import AdminSyllabusTaiLieuPage from './pages/AdminSyllabusTaiLieuPage.vue'
import { ROLES } from '@/core/constants/roles'
import { requireAuth } from '@/core/guards/authGuard'
import { requireAdmin } from '@/core/guards/adminGuard'
import AdminXayDungChuongTrinhPage from './pages/AdminXayDungChuongTrinhPage.vue'
export const chuongTrinhRoutes = [
    {
        path: 'chuong-trinh',
        component: ChuongTrinhLayout,
        beforeEnter: [requireAuth, requireAdmin],
        meta: {
            module: 'chuongTrinh',
            title: 'Chương trình',
            roles: [ROLES.ADMIN, ROLES.DAO_TAO]
        },
        children: [
            {
                path: '',
                name: 'ChuongTrinh.ChuongTrinh',
                component: AdminChuongTrinhPage
            },
            {
                path: 'version',
                name: 'ChuongTrinh.Version',
                component: AdminChuongTrinhVersionPage
            },
            {
                path: 'mon',
                name: 'ChuongTrinh.Mon',
                component: AdminChuongTrinhMonPage
            },
            {
                path: 'nhom-kien-thuc',
                name: 'ChuongTrinh.NhomKienThuc',
                component: AdminNhomKienThucPage
            },
            {
                path: 'muc-tieu',
                name: 'ChuongTrinh.MucTieu',
                component: AdminMucTieuChuongTrinhPage
            },
            {
                path: 'nang-luc-dau-ra',
                name: 'ChuongTrinh.NangLucDauRa',
                component: AdminNangLucDauRaPage
            },
            {
                path: 'vi-tri-viec-lam',
                name: 'ChuongTrinh.ViTriViecLam',
                component: AdminViTriViecLamPage
            },
            {
                path: 'dieu-kien-tot-nghiep',
                name: 'ChuongTrinh.DieuKienTotNghiep',
                component: AdminDieuKienTotNghiepPage
            },
            {
                path: 'nhom-tu-chon',
                name: 'ChuongTrinh.NhomTuChon',
                component: AdminNhomTuChonPage
            },
            {
                path: 'mon-tu-chon',
                name: 'ChuongTrinh.MonTuChon',
                component: AdminMonTuChonPage
            },
            {
                path: 'mon-tien-quyet',
                name: 'ChuongTrinh.MonTienQuyet',
                component: AdminMonTienQuyetPage
            },
            {
                path: 'mon-hoc',
                name: 'ChuongTrinh.MonHoc',
                component: AdminMonHocPage
            },
            {
                path: 'dieu-kien-mon-hoc',
                name: 'ChuongTrinh.DieuKienMonHoc',
                component: AdminDieuKienMonHocPage
            },
            {
                path: 'quy-doi-diem',
                name: 'ChuongTrinh.QuyDoiDiem',
                component: AdminQuyDoiDiemPage
            },
            {
                path: 'syllabus-chuong-trinh',
                name: 'ChuongTrinh.SyllabusChuongTrinh',
                component: AdminSyllabusChuongTrinhPage
            },
            {
                path: 'syllabus-mon-hoc',
                name: 'ChuongTrinh.SyllabusMonHoc',
                component: AdminSyllabusMonHocPage
            },
            {
                path: 'syllabus-chuong-bai',
                name: 'ChuongTrinh.SyllabusChuongBai',
                component: AdminSyllabusChuongBaiPage
            },
            {
                path: 'syllabus-tai-lieu',
                name: 'ChuongTrinh.SyllabusTaiLieu',
                component: AdminSyllabusTaiLieuPage
            },
            {
                path: 'xay-dung',
                name: 'ChuongTrinh.XayDung',
                component: AdminXayDungChuongTrinhPage
            }
        ]
    }
]