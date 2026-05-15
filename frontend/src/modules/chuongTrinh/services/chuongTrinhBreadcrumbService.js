import pageURL from '@/core/config/pageURL';
import { chuongTrinhApi } from '@/modules/chuongTrinh/api/chuongTrinhApi.js';
import { CHUONG_TRINH_RESOURCE_MAP } from '@/modules/chuongTrinh/constants/trangThaiChuongTrinh.js';

const CACHE = new Map();

const firstValue = (record, keys = []) => {
  for (const key of keys) {
    const value = record?.[key];
    if (value !== undefined && value !== null && value !== '') return value;
  }
  return '';
};

const formatWithCode = (record, codeKeys, nameKeys, fallback) => {
  const code = firstValue(record, codeKeys);
  const name = firstValue(record, nameKeys);
  if (code && name) return `${code} - ${name}`;
  return code || name || fallback;
};

const getId = (value) => {
  if (Array.isArray(value)) return value[0];
  return value;
};

const fetchDetail = async (endpoint, id) => {
  if (!id) return null;
  const cacheKey = `${endpoint}/${id}`;
  if (CACHE.has(cacheKey)) return CACHE.get(cacheKey);

  try {
    const record = await chuongTrinhApi.getDetail(endpoint, id);
    CACHE.set(cacheKey, record);
    return record;
  } catch {
    return null;
  }
};

const findSelectedRow = async (endpoint, idKeys, id) => {
  if (!id) return null;
  const cacheKey = `${endpoint}?${idKeys.join('|')}=${id}`;
  if (CACHE.has(cacheKey)) return CACHE.get(cacheKey);

  try {
    const list = await chuongTrinhApi.getList(endpoint, {});
    const record = list.find((item) => idKeys.some((key) => String(item?.[key]) === String(id))) || null;
    CACHE.set(cacheKey, record);
    return record;
  } catch {
    return null;
  }
};

const makeItem = (label, to) => ({ label, to });

export const getChuongTrinhFlowBreadcrumb = async (route) => {
  const query = route.query || {};
  const params = route.params || {};
  const segment = route.path.split('/').filter(Boolean).at(-1);
  const resource = CHUONG_TRINH_RESOURCE_MAP[segment];

  const items = [
    makeItem('Trang chủ', '/'),
    makeItem('Chương trình đào tạo', '/chuong-trinh'),
  ];

  if (route.path === '/chuong-trinh') return items;
  if (route.path === '/chuong-trinh/danh-sach') {
    items.push(makeItem('Danh sách chương trình', route.fullPath));
    return items;
  }

  const chuongTrinhId = getId(params.chuongTrinhId || query.chuongTrinhId || query.chuong_trinh_id);
  const versionId = getId(params.versionId || query.chuongTrinhVersionId || query.versionId || query.chuong_trinh_version_id);
  const nhomKienThucId = getId(query.nhomKienThucId || query.nhom_kien_thuc_id);
  const chuongTrinhMonId = getId(params.chuongTrinhMonId || query.chuongTrinhMonId || query.chuong_trinh_mon_id);
  const monHocId = getId(query.monHocId || query.mon_hoc_id);

  const version = await fetchDetail(pageURL.chuongTrinh.CHUONG_TRINH_VERSION, versionId);
  const realChuongTrinhId = chuongTrinhId || version?.chuongTrinhId;
  const chuongTrinh = await fetchDetail(pageURL.chuongTrinh.CHUONG_TRINH, realChuongTrinhId);

  if (chuongTrinh) {
    items.push(makeItem(formatWithCode(chuongTrinh, ['maChuongTrinh'], ['tenChuongTrinh'], `Chương trình #${realChuongTrinhId}`), `/chuong-trinh/${realChuongTrinhId}`));
  } else if (realChuongTrinhId) {
    items.push(makeItem(`Chương trình #${realChuongTrinhId}`, `/chuong-trinh/${realChuongTrinhId}`));
  }

  if (route.path.endsWith('/phien-ban')) {
    items.push(makeItem('Danh sách phiên bản', route.fullPath));
    return items;
  }

  if (version) {
    items.push(makeItem(formatWithCode(version, ['maVersion'], ['tenVersion'], `Version #${versionId}`), `/chuong-trinh/${version.chuongTrinhId || realChuongTrinhId}/phien-ban/${versionId}`));
  } else if (versionId) {
    items.push(makeItem(`Version #${versionId}`, `/chuong-trinh/${realChuongTrinhId || chuongTrinhId}/phien-ban/${versionId}`));
  }

  if (route.path.endsWith('/khung-chuong-trinh')) {
    items.push(makeItem('Khung chương trình', route.fullPath));
    return items;
  }

  const nhomKienThuc = await fetchDetail(pageURL.chuongTrinh.NHOM_KIEN_THUC, nhomKienThucId);
  if (nhomKienThuc) {
    items.push(makeItem(formatWithCode(nhomKienThuc, ['ma'], ['ten'], `Nhóm kiến thức #${nhomKienThucId}`), route.fullPath));
  }

  let chuongTrinhMon = await fetchDetail(pageURL.chuongTrinh.CHUONG_TRINH_MON, chuongTrinhMonId);
  if (!chuongTrinhMon && monHocId && versionId) {
    chuongTrinhMon = await findSelectedRow(pageURL.chuongTrinh.CHUONG_TRINH_MON, ['monHocId'], monHocId);
  }

  const realMonHocId = monHocId || chuongTrinhMon?.monHocId;
  const monHoc = await fetchDetail(pageURL.chuongTrinh.MON_HOC, realMonHocId);

  if (monHoc || chuongTrinhMon) {
    const monLabel = monHoc
      ? formatWithCode(monHoc, ['maMon'], ['tenMon'], `Môn học #${realMonHocId}`)
      : formatWithCode(chuongTrinhMon, ['maMonTrongCt'], ['loaiHocPhan', 'loai'], `Môn CT #${chuongTrinhMonId}`);
    items.push(makeItem(monLabel, route.fullPath));
  }

  if (route.path.includes('/quan-ly/')) {
    const quanLyResource = CHUONG_TRINH_RESOURCE_MAP[segment];
    items.push(makeItem('CRUD dữ liệu', '/chuong-trinh/quan-ly/chuong-trinh'));
    if (quanLyResource) items.push(makeItem(quanLyResource.title, route.fullPath));
  } else if (resource && !items.some((item) => item.label === resource.title)) {
    items.push(makeItem(resource.title, route.fullPath));
  }

  return items;
};
