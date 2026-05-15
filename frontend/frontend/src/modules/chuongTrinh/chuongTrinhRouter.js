import ChuongTrinhDanhSach from '@/modules/chuongTrinh/pages/admin/danhSach/ChuongTrinhDanhSach.vue';
import ChuongTrinhDaoTaoHome from '@/modules/chuongTrinh/pages/luongChuongTrinh/ChuongTrinhDaoTaoHome.vue';
import ChiTietChuongTrinhDaoTao from '@/modules/chuongTrinh/pages/luongChuongTrinh/ChiTietChuongTrinhDaoTao.vue';
import DanhSachPhienBanChuongTrinh from '@/modules/chuongTrinh/pages/luongChuongTrinh/DanhSachPhienBanChuongTrinh.vue';
import ChiTietPhienBanChuongTrinh from '@/modules/chuongTrinh/pages/luongChuongTrinh/ChiTietPhienBanChuongTrinh.vue';
import KhungChuongTrinhDaoTao from '@/modules/chuongTrinh/pages/luongChuongTrinh/KhungChuongTrinhDaoTao.vue';
import ChiTietMonHocChuongTrinh from '@/modules/chuongTrinh/pages/luongChuongTrinh/ChiTietMonHocChuongTrinh.vue';
import { CHUONG_TRINH_RESOURCES, CHUONG_TRINH_RESOURCE_KEYS } from '@/modules/chuongTrinh/constants/trangThaiChuongTrinh.js';

export default [
  { path: '/chuong-trinh', redirect: '/quy-trinh/chuong-trinh' },
  {
    path: '/quy-trinh/chuong-trinh',
    name: 'quy-trinh-chuong-trinh',
    component: ChuongTrinhDaoTaoHome,
    meta: { moduleKey: 'chuongTrinh', title: 'Chương trình' },
  },


  {
    path: '/quy-trinh/chuong-trinh/:chuongTrinhId(\\d+)',
    name: 'chi-tiet-chuong-trinh-dao-tao',
    component: ChiTietChuongTrinhDaoTao,
    meta: { moduleKey: 'chuongTrinh', title: 'Chi tiết chương trình' },
  },
  {
    path: '/quy-trinh/chuong-trinh/:chuongTrinhId(\\d+)/phien-ban',
    name: 'danh-sach-phien-ban-chuong-trinh',
    component: DanhSachPhienBanChuongTrinh,
    meta: { moduleKey: 'chuongTrinh', title: 'Phiên bản' },
  },
  {
    path: '/quy-trinh/chuong-trinh/:chuongTrinhId(\\d+)/phien-ban/:versionId(\\d+)',
    name: 'chi-tiet-phien-ban-chuong-trinh',
    component: ChiTietPhienBanChuongTrinh,
    meta: { moduleKey: 'chuongTrinh', title: 'Chi tiết phiên bản' },
  },
  {
    path: '/quy-trinh/chuong-trinh/:chuongTrinhId(\\d+)/phien-ban/:versionId(\\d+)/khung',
    name: 'khung-chuong-trinh-dao-tao',
    component: KhungChuongTrinhDaoTao,
    meta: { moduleKey: 'chuongTrinh', title: 'Khung chương trình' },
  },
  {
    path: '/quy-trinh/chuong-trinh/:chuongTrinhId(\\d+)/phien-ban/:versionId(\\d+)/mon/:chuongTrinhMonId(\\d+)',
    name: 'chi-tiet-mon-hoc-chuong-trinh',
    component: ChiTietMonHocChuongTrinh,
    meta: { moduleKey: 'chuongTrinh', title: 'Chi tiết môn học' },
  },
  {
    path: '/du-lieu/chuong-trinh',
    redirect: '/du-lieu/chuong-trinh/chuong-trinh',
  },
  ...CHUONG_TRINH_RESOURCES.map((resource) => ({
    path: `/du-lieu/chuong-trinh/${resource.segment}`,
    name: `du-lieu-chuong-trinh-${resource.segment}`,
    component: ChuongTrinhDanhSach,
    meta: { moduleKey: 'chuongTrinh', resourceKey: resource.key, title: resource.title },
  })),
  ...CHUONG_TRINH_RESOURCES.map((resource) => ({
    path: `/chuong-trinh/quan-ly/${resource.segment}`,
    redirect: `/du-lieu/chuong-trinh/${resource.segment}`,
  })),
];
