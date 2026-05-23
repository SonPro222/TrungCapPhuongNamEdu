import ChuongTrinhLayout from './layouts/ChuongTrinhLayout.vue'

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
        redirect: { name: 'ChuongTrinh.XayDung' }
      },
      {
        path: 'xay-dung',
        name: 'ChuongTrinh.XayDung',
        component: AdminXayDungChuongTrinhPage,
        meta: { title: 'Xây dựng chương trình' }
      },


    ]
  }
]
