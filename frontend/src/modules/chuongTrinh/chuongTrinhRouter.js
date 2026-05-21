import ChuongTrinhLayout from './layouts/ChuongTrinhLayout.vue'
import SinhVienChuongTrinhLayout from './layouts/SinhVienChuongTrinhLayout.vue'

import { ROLES } from '@/core/constants/roles'
import { requireAuth } from '@/core/guards/authGuard'
import { requireAdmin } from '@/core/guards/adminGuard'
import AdminXayDungChuongTrinhPage from './pages/AdminXayDungChuongTrinhPage.vue'
import AdminXemNganhDaoTaoPage from './pages/AdminXemNganhDaoTaoPage.vue'
import AdminXemChuongTrinhTheoNganhPage from './pages/AdminXemChuongTrinhTheoNganhPage.vue'
import AdminXemVersionTheoChuongTrinhPage from './pages/AdminXemVersionTheoChuongTrinhPage.vue'
import AdminXemChiTietVersionPage from './pages/AdminXemChiTietVersionPage.vue'
import AdminXemMonTheoVersionPage from './pages/AdminXemMonTheoVersionPage.vue'
import AdminXemChiTietMonPage from './pages/AdminXemChiTietMonPage.vue'

const chuyenRouteCuSangDaoTaoXem = (tenRouteMoi) => (to) => ({
  name: tenRouteMoi,
  params: to.params,
  query: to.query
})

const daoTaoXemChildren = [
  {
    path: '',
    name: 'ChuongTrinh.DaoTaoNganhDaoTao',
    component: AdminXemNganhDaoTaoPage,
    meta: { title: 'Đào tạo xem - Danh sách ngành' }
  },
  {
    path: 'nganh/:nganhId',
    name: 'ChuongTrinh.DaoTaoChuongTrinhTheoNganh',
    component: AdminXemChuongTrinhTheoNganhPage,
    meta: { title: 'Đào tạo xem - Chương trình theo ngành' }
  },
  {
    path: 'chuong-trinh/:chuongTrinhId',
    name: 'ChuongTrinh.DaoTaoVersionTheoChuongTrinh',
    component: AdminXemVersionTheoChuongTrinhPage,
    meta: { title: 'Đào tạo xem - Version theo chương trình' }
  },
  {
    path: 'version/:versionId',
    name: 'ChuongTrinh.DaoTaoChiTietVersion',
    component: AdminXemChiTietVersionPage,
    meta: { title: 'Đào tạo xem - Chi tiết version' }
  },
  {
    path: 'version/:versionId/mon',
    name: 'ChuongTrinh.DaoTaoMonTheoVersion',
    component: AdminXemMonTheoVersionPage,
    meta: { title: 'Đào tạo xem - Môn theo version' }
  },
  {
    path: 'mon/:chuongTrinhMonId',
    name: 'ChuongTrinh.DaoTaoChiTietMon',
    component: AdminXemChiTietMonPage,
    meta: { title: 'Đào tạo xem - Chi tiết môn' }
  }
]

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
      {
        path: 'dao-tao-xem',
        component: SinhVienChuongTrinhLayout,
        meta: { title: 'Đào tạo xem chương trình' },
        children: daoTaoXemChildren
      },
      {
        path: 'sinh-vien',
        redirect: { name: 'ChuongTrinh.DaoTaoNganhDaoTao' }
      },
      {
        path: 'sinh-vien/nganh/:nganhId',
        redirect: chuyenRouteCuSangDaoTaoXem('ChuongTrinh.DaoTaoChuongTrinhTheoNganh')
      },
      {
        path: 'sinh-vien/chuong-trinh/:chuongTrinhId',
        redirect: chuyenRouteCuSangDaoTaoXem('ChuongTrinh.DaoTaoVersionTheoChuongTrinh')
      },
      {
        path: 'sinh-vien/version/:versionId',
        redirect: chuyenRouteCuSangDaoTaoXem('ChuongTrinh.DaoTaoChiTietVersion')
      },
      {
        path: 'sinh-vien/version/:versionId/mon',
        redirect: chuyenRouteCuSangDaoTaoXem('ChuongTrinh.DaoTaoMonTheoVersion')
      },
      {
        path: 'sinh-vien/mon/:chuongTrinhMonId',
        redirect: chuyenRouteCuSangDaoTaoXem('ChuongTrinh.DaoTaoChiTietMon')
      },
      {
        path: 'xem-dao-tao',
        redirect: { name: 'ChuongTrinh.DaoTaoNganhDaoTao' }
      },
      {
        path: 'xem-dao-tao/nganh/:nganhId',
        redirect: chuyenRouteCuSangDaoTaoXem('ChuongTrinh.DaoTaoChuongTrinhTheoNganh')
      },
      {
        path: 'xem-dao-tao/chuong-trinh/:chuongTrinhId',
        redirect: chuyenRouteCuSangDaoTaoXem('ChuongTrinh.DaoTaoVersionTheoChuongTrinh')
      },
      {
        path: 'xem-dao-tao/version/:versionId',
        redirect: chuyenRouteCuSangDaoTaoXem('ChuongTrinh.DaoTaoChiTietVersion')
      },
      {
        path: 'xem-dao-tao/version/:versionId/mon',
        redirect: chuyenRouteCuSangDaoTaoXem('ChuongTrinh.DaoTaoMonTheoVersion')
      },
      {
        path: 'xem-dao-tao/mon/:chuongTrinhMonId',
        redirect: chuyenRouteCuSangDaoTaoXem('ChuongTrinh.DaoTaoChiTietMon')
      }
    ]
  }
]
