import { ROLES } from '@/core/constants/roles'

// Màn xem chương trình đào tạo dùng chung cho Sinh viên / Giảng viên (chỉ xem theo luồng tên).
// Được mount dưới /admin (xem moduleRotuer.js) nên URL đầy đủ là /admin/xem-chuong-trinh-dao-tao.
export const xemChuongTrinhRoutes = [
  {
    path: 'xem-chuong-trinh-dao-tao',
    name: 'XemChuongTrinhDaoTao',
    component: () => import('../pages/XemChuongTrinhDaoTaoPage.vue'),
    meta: {
      title: 'Xem chương trình đào tạo',
      roles: [ROLES.ADMIN, ROLES.DAO_TAO, ROLES.GIAO_VIEN, ROLES.SINH_VIEN]
    }
  }
]
