import { ROLES } from '@/core/constants/roles'
import DaoTaoLopHocPhanTheoChuongTrinhPage from './pages/DaoTaoLopHocPhanTheoChuongTrinhPage.vue'

export const trienKhaiRoutes = [
    {
        path: 'trien-khai',
        meta: {
            module: 'trienKhai',
            title: 'Triển khai đào tạo',
            roles: [ROLES.SINH_VIEN, ROLES.GIAO_VIEN, 'GIANG_VIEN']
        },
        children: [
            {
                path: '',
                redirect: '/user/trien-khai/chuong-trinh-dao-tao'
            },
            {
                path: 'chuong-trinh-dao-tao',
                name: 'User.TrienKhai.ChuongTrinhDaoTao',
                component: DaoTaoLopHocPhanTheoChuongTrinhPage,
                meta: {
                    title: 'Chương trình đào tạo',
                    roles: [ROLES.SINH_VIEN, ROLES.GIAO_VIEN, 'GIANG_VIEN']
                }
            }
        ]
    }
]
