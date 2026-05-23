import { ROLES } from '@/core/constants/roles'
import { requireAuth } from '@/core/guards/authGuard'
import { requireAdmin } from '@/core/guards/adminGuard'
import DaoTaoXemChuongTrinhPage from './pages/DaoTaoXemChuongTrinhPage.vue'

export const daoTaoRoutes = [
  {
    path: 'dao-tao',
    beforeEnter: [requireAuth, requireAdmin],
    meta: {
      module: 'daoTao',
      title: 'Đào tạo',
      roles: [ROLES.ADMIN, ROLES.DAO_TAO]
    },
    children: [
      { path: '', redirect: { name: 'DaoTao.XemChuongTrinh' } },
      { path: 'xem-chuong-trinh', name: 'DaoTao.XemChuongTrinh', component: DaoTaoXemChuongTrinhPage, meta: { title: 'Đào tạo xem - Ngành' } },
      { path: 'xem-chuong-trinh/nganh/:nganhId/cau-hinh-chuong-trinh', name: 'DaoTao.XemChuongTrinh.CauHinh', component: DaoTaoXemChuongTrinhPage, meta: { title: 'Đào tạo xem - Cấu hình chương trình' } },
      { path: 'xem-chuong-trinh/nganh/:nganhId/chuong-trinh', name: 'DaoTao.XemChuongTrinh.ChuongTrinh', component: DaoTaoXemChuongTrinhPage, meta: { title: 'Đào tạo xem - Chương trình' } },
      { path: 'xem-chuong-trinh/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version', name: 'DaoTao.XemChuongTrinh.Version', component: DaoTaoXemChuongTrinhPage, meta: { title: 'Đào tạo xem - Version' } },
      { path: 'xem-chuong-trinh/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/tong-quan', name: 'DaoTao.XemChuongTrinh.TongQuan', component: DaoTaoXemChuongTrinhPage, meta: { title: 'Đào tạo xem - Tổng quan version' } },
      { path: 'xem-chuong-trinh/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/khung-cau-truc', name: 'DaoTao.XemChuongTrinh.KhungCauTruc', component: DaoTaoXemChuongTrinhPage, meta: { title: 'Đào tạo xem - Khung cấu trúc' } },
      { path: 'xem-chuong-trinh/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/mon', name: 'DaoTao.XemChuongTrinh.Mon', component: DaoTaoXemChuongTrinhPage, meta: { title: 'Đào tạo xem - Môn trong chương trình' } },
      { path: 'xem-chuong-trinh/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/syllabus-goc/:monHocId?', name: 'DaoTao.XemChuongTrinh.SyllabusGoc', component: DaoTaoXemChuongTrinhPage, meta: { title: 'Đào tạo xem - Syllabus môn học gốc' } },
      { path: 'xem-chuong-trinh/nganh/:nganhId/chuong-trinh/:chuongTrinhId/version/:versionId/syllabus-ap-dung/:chuongTrinhMonId?', name: 'DaoTao.XemChuongTrinh.SyllabusApDung', component: DaoTaoXemChuongTrinhPage, meta: { title: 'Đào tạo xem - Syllabus môn áp dụng' } }
    ]
  }
]
