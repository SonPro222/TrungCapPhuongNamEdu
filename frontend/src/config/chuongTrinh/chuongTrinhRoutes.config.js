import { CHUONG_TRINH_RESOURCES, DEFAULT_CHUONG_TRINH_RESOURCE } from '@/modules/chuongTrinh/constants/trangThaiChuongTrinh.js';

export const CHUONG_TRINH_ROUTE_BASE = '/chuong-trinh';
export const CHUONG_TRINH_DEFAULT_PATH = `${CHUONG_TRINH_ROUTE_BASE}/${DEFAULT_CHUONG_TRINH_RESOURCE.segment}`;
export const CHUONG_TRINH_ROUTE_ITEMS = CHUONG_TRINH_RESOURCES.map((resource) => ({
  key: resource.key,
  label: resource.title,
  path: `${CHUONG_TRINH_ROUTE_BASE}/${resource.segment}`,
  endpoint: resource.endpoint,
}));
