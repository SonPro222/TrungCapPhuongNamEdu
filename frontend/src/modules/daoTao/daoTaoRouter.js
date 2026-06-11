import {ROLES} from '@/core/constants/roles'
import {requireAuth} from '@/core/guards/authGuard'
import {requireAdmin} from '@/core/guards/adminGuard'
import DaoTaoLayout from './layouts/DaoTaoLayout.vue'
import DaoTaoXemChuongTrinhPage from './pages/DaoTaoXemChuongTrinhPage.vue'
import DaoTaoXemTepPage from './pages/DaoTaoXemTepPage.vue'
import DaoTaoNhomKienThucMon from '@/modules/daoTao/pages/DaoTaoNhomKienThucMon.vue'
import DaoTaoMonHocmau from './pages/DaoTaoMonHocMau.vue'
import DaoTaoSyllabusMonHocmau from './pages/DaoTaoSyllabusMonHocMau.vue'
import DaoTaoSyllabusChuongTrinhmau from './pages/DaoTaoSyllabusChuongTrinhMau.vue'
import DaoTaoMucTieuChuongTrinhmau from './pages/DaoTaoMucTieuChuongTrinhMau.vue'
import DaoTaoNangLucDauRamau from './pages/DaoTaoNangLucDauRaMau.vue'
import DaoTaoViTriViecLammau from './pages/DaoTaoViTriViecLamMau.vue'
import DaoTaoDieuKienTotNghiepmau from './pages/DaoTaoDieuKienTotNghiepMau.vue'
import DaoTaoCauHinhMauDaoTao from './pages/DaoTaoCauHinhMauDaoTao.vue'
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
                path: 'xem-chuong-trinh/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/syllabus-mau/:monHocId?',
                name: 'DaoTao.XemChuongTrinh.Syllabusmau',
                component: DaoTaoXemChuongTrinhPage,
                meta: {title: 'Đào tạo xem - Syllabus môn học gốc', roles: [ROLES.ADMIN, ROLES.DAO_TAO]}
            },
            {
                path: 'xem-chuong-trinh/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/syllabus-ap-dung/:chuongTrinhMonId/tep',
                name: 'DaoTao.XemChuongTrinh.SyllabusTep',
                component: DaoTaoXemTepPage,
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
                path: 'syllabus-chuong-trinh-mau',
                name: 'DaoTao.SyllabusChuongTrinhmau',
                component: DaoTaoSyllabusChuongTrinhmau,
                meta: {
                    title: 'Đào tạo - Syllabus chương trình gốc',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },

            {
                path: 'muc-tieu-chuong-trinh-mau',
                name: 'DaoTao.MucTieuChuongTrinhmau',
                component: DaoTaoMucTieuChuongTrinhmau,
                meta: {
                    title: 'Đào tạo - Mục tiêu chương trình gốc',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },
            {
                path: 'nang-luc-dau-ra-mau',
                name: 'DaoTao.NangLucDauRamau',
                component: DaoTaoNangLucDauRamau,
                meta: {
                    title: 'Đào tạo - Năng lực đầu ra gốc',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },
            {
                path: 'vi-tri-viec-lam-mau',
                name: 'DaoTao.ViTriViecLammau',
                component: DaoTaoViTriViecLammau,
                meta: {
                    title: 'Đào tạo - Vị trí việc làm gốc',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },
            {
                path: 'dieu-kien-tot-nghiep-mau',
                name: 'DaoTao.DieuKienTotNghiepmau',
                component: DaoTaoDieuKienTotNghiepmau,
                meta: {
                    title: 'Đào tạo - Điều kiện tốt nghiệp gốc',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },
            {
                path: 'syllabus-mon-hoc-mau',
                name: 'DaoTao.SyllabusMonHocmau',
                component: DaoTaoSyllabusMonHocmau,
                meta: {
                    title: 'Đào tạo - Syllabus môn học gốc',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
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
                path: 'mon-hoc-mau',
                name: 'DaoTao.MonHocmau',
                component: DaoTaoMonHocmau,
                meta: {
                    title: 'Đào tạo - Môn gốc',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },
            {
                path: 'cau-hinh-mau-dao-tao',
                name: 'DaoTao.CauHinhMauDaoTao',
                component: DaoTaoCauHinhMauDaoTao,
                meta: {
                    title: 'Đào tạo - Cấu hình mẫu đào tạo',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            },
        ]

    }
]