import { ROLES } from '@/core/constants/roles'
import { requireAuth } from '@/core/guards/authGuard'
import { requireAdmin } from '@/core/guards/adminGuard'
import DaoTaoLayout from '@/modules/daoTao/layouts/DaoTaoLayout.vue'
import DaoTaoLopHocPhanTheoChuongTrinhPage from './pages/DaoTaoLopHocPhanTheoChuongTrinhPage.vue'

export const trienKhaiRoutes = [
    {
        path: 'dao-tao',
        component: DaoTaoLayout,
        beforeEnter: [requireAuth, requireAdmin],
        meta: {
            module: 'trienKhai',
            title: 'Triển khai đào tạo',
            roles: [ROLES.ADMIN, ROLES.DAO_TAO]
        },
        children: [
            {
                path: 'lop-hoc-phan-theo-chuong-trinh',
                name: 'TrienKhai.LopHocPhanTheoChuongTrinh',
                component: DaoTaoLopHocPhanTheoChuongTrinhPage,
                meta: {
                    title: 'Lớp học phần theo chương trình',
                    roles: [ROLES.ADMIN, ROLES.DAO_TAO]
                }
            }
        ]
    }
]
