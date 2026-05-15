import { CHUONG_TRINH_RESOURCES } from '@/modules/chuongTrinh/constants/trangThaiChuongTrinh.js';

export const CHUONG_TRINH_TABLE_CONFIG = CHUONG_TRINH_RESOURCES.reduce((config, resource) => {
  config[resource.key] = {
    columns: resource.columns || [],
    fields: resource.fields || [],
    filters: resource.filters || [],
    searchFields: resource.searchFields || [],
  };
  return config;
}, {});
