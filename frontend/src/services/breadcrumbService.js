import { CHUONG_TRINH_RESOURCE_MAP } from '@/modules/chuongTrinh/constants/trangThaiChuongTrinh.js';
import { getChuongTrinhFlowBreadcrumb } from '@/modules/chuongTrinh/services/chuongTrinhBreadcrumbService.js';

const MODULE_LABEL_MAP = {
  'chuong-trinh': 'Chương trình',
  'dao-tao': 'Đào tạo',
  diem: 'Điểm',
  'giang-day': 'Giảng dạy',
  'he-thong': 'Hệ thống',
  'hoc-phi': 'Học phí',
  lms: 'LMS',
  'sinh-vien': 'Sinh viên',
  thi: 'Thi',
};

const buildStaticItems = (route) => {
  const parts = route.path.split('/').filter(Boolean);
  const items = [{ label: 'Trang chủ', to: '/' }];

  let path = '';
  parts.forEach((part) => {
    path += `/${part}`;
    const resource = CHUONG_TRINH_RESOURCE_MAP[part];
    items.push({ label: resource?.title || MODULE_LABEL_MAP[part] || part, to: path });
  });

  return items;
};

export const buildBreadcrumbItems = async (route) => {
  if (route.path.startsWith('/login')) return [];

  if (route.path.startsWith('/chuong-trinh')) {
    const items = await getChuongTrinhFlowBreadcrumb(route);
    if (items.length) return items;
  }

  return buildStaticItems(route);
};
