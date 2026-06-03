import {ROLES} from '@/core/constants/roles'
import {requireAuth} from '@/core/guards/authGuard'
import {requireAdmin} from '@/core/guards/adminGuard'
import DaoTaoLayout from './layouts/DaoTaoLayout.vue'
import DaoTaoXemChuongTrinhPage from './pages/DaoTaoXemChuongTrinhPage.vue'
import DaoTaoXemTepSyllabusMonPage from './pages/DaoTaoXemTepSyllabusMonPage.vue'
import DaoTaoNhomKienThucMon from '@/modules/daoTao/pages/DaoTaoNhomKienThucMon.vue'
import DaoTaoMonHocGoc from './pages/DaoTaoMonHocGoc.vue'
export const daoTaoRoutes = [
    {
        path: 'dao-tao',
        component: DaoTaoLayout,
        beforeEnter: [requireAuth, requireAdmin],
        meta: {
            module: 'daoTao',
            title: 'Đào tạo',
            roles: [ROLES.ADMIN, ROLES.DAO_TAO]
        },
        children: [
            {path: '', redirect: {name: 'DaoTao.XemChuongTrinh'}},
            {
                path: 'xem-chuong-trinh',
                name: 'DaoTao.XemChuongTrinh',
                component: DaoTaoXemChuongTrinhPage,
                meta: {title: 'Đào tạo xem - Ngành', roles: [ROLES.ADMIN, ROLES.DAO_TAO]}
            },
            {
                path: 'xem-chuong-trinh/nganh/:nganhId/cau-hinh-chuong-trinh',
                name: 'DaoTao.XemChuongTrinh.CauHinh',
                component: DaoTaoXemChuongTrinhPage,
                meta: {title: 'Đào tạo xem - Cấu hình chương trình', roles: [ROLES.ADMIN, ROLES.DAO_TAO]}
            },
            {
                path: 'xem-chuong-trinh/nganh/:nganhId/chuong-trinh',
                name: 'DaoTao.XemChuongTrinh.ChuongTrinh',
                component: DaoTaoXemChuongTrinhPage,
                meta: {title: 'Đào tạo xem - Chương trình', roles: [ROLES.ADMIN, ROLES.DAO_TAO]}
            },
            {
                path: 'xem-chuong-trinh/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version',
                name: 'DaoTao.XemChuongTrinh.Version',
                component: DaoTaoXemChuongTrinhPage,
                meta: {title: 'Đào tạo xem - Version', roles: [ROLES.ADMIN, ROLES.DAO_TAO]}
            },
            {
                path: 'xem-chuong-trinh/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/tong-quan',
                name: 'DaoTao.XemChuongTrinh.TongQuan',
                component: DaoTaoXemChuongTrinhPage,
                meta: {title: 'Đào tạo xem - Tổng quan version', roles: [ROLES.ADMIN, ROLES.DAO_TAO]}
            },
            {
                path: 'xem-chuong-trinh/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/khung-cau-truc',
                name: 'DaoTao.XemChuongTrinh.KhungCauTruc',
                component: DaoTaoXemChuongTrinhPage,
                meta: {title: 'Đào tạo xem - Khung cấu trúc', roles: [ROLES.ADMIN, ROLES.DAO_TAO]}
            },
            {
                path: 'xem-chuong-trinh/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/mon',
                name: 'DaoTao.XemChuongTrinh.Mon',
                component: DaoTaoXemChuongTrinhPage,
                meta: {title: 'Đào tạo xem - Môn trong chương trình', roles: [ROLES.ADMIN, ROLES.DAO_TAO]}
            },
            {
                path: 'xem-chuong-trinh/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/syllabus-goc/:monHocId?',
                name: 'DaoTao.XemChuongTrinh.SyllabusGoc',
                component: DaoTaoXemChuongTrinhPage,
                meta: {title: 'Đào tạo xem - Syllabus môn học gốc', roles: [ROLES.ADMIN, ROLES.DAO_TAO]}
            },
            {
                path: 'xem-chuong-trinh/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/syllabus-ap-dung/:chuongTrinhMonId/tep',
                name: 'DaoTao.XemChuongTrinh.SyllabusTep',
                component: DaoTaoXemTepSyllabusMonPage,
                meta: {
                    title: 'Đào tạo xem - Tệp syllabus môn',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },
            {
                path: 'xem-chuong-trinh/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/syllabus-ap-dung/:chuongTrinhMonId?',
                name: 'DaoTao.XemChuongTrinh.SyllabusApDung',
                component: DaoTaoXemChuongTrinhPage,
                meta: {title: 'Đào tạo xem - Syllabus môn áp dụng', roles: [ROLES.ADMIN, ROLES.DAO_TAO]}
            },
            {
                path: 'nhom-kien-thuc-mon',
                name: 'DaoTao.NhomKienThucMon',
                component: DaoTaoNhomKienThucMon,
                meta: {
                    title: 'Đào tạo - Nhóm kiến thức và môn',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },
            {
                path: 'mon-hoc-goc',
                name: 'DaoTao.MonHocGoc',
                component: DaoTaoMonHocGoc,
                meta: {
                    title: 'Đào tạo - Môn gốc',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },

        ]

    }
]