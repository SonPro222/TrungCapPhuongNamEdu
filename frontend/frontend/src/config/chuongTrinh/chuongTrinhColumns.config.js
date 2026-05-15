import { CHUONG_TRINH_TABLE_CONFIG } from './chuongTrinhTable.config.js';

export const getChuongTrinhColumns = (resourceKey) => CHUONG_TRINH_TABLE_CONFIG[resourceKey]?.columns || [];
