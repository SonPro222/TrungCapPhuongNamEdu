import { CHUONG_TRINH_FLOW } from './chuongTrinhRelation.config.js';
import { CHUONG_TRINH_TABLE_CONFIG } from './chuongTrinhTable.config.js';

export const CHUONG_TRINH_MODULE_BASE = '/chuong-trinh';

export const CHUONG_TRINH_ROUTE_ITEMS = CHUONG_TRINH_FLOW.map((key) => ({
  key,
  title: CHUONG_TRINH_TABLE_CONFIG[key]?.title || key,
}));
