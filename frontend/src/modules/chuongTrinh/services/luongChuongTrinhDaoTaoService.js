import pageURL from '@/core/config/pageURL';
import { chuongTrinhApi } from '@/modules/chuongTrinh/api/chuongTrinhApi.js';

export const API_LUONG_CHUONG_TRINH = {
  CHUONG_TRINH: pageURL.chuongTrinh.CHUONG_TRINH,
  PHIEN_BAN: pageURL.chuongTrinh.CHUONG_TRINH_VERSION,
  NHOM_KIEN_THUC: pageURL.chuongTrinh.NHOM_KIEN_THUC,
  MON_TRONG_CHUONG_TRINH: pageURL.chuongTrinh.CHUONG_TRINH_MON,
  MON_HOC: pageURL.chuongTrinh.MON_HOC,
  SYLLABUS_MON_HOC: pageURL.chuongTrinh.SYLLABUS_MON_HOC,
  SYLLABUS_CHUONG_BAI: pageURL.chuongTrinh.SYLLABUS_CHUONG_BAI,
  SYLLABUS_TAI_LIEU: pageURL.chuongTrinh.SYLLABUS_TAI_LIEU,
  MUC_TIEU: pageURL.chuongTrinh.MUC_TIEU_CHUONG_TRINH,
  NANG_LUC_DAU_RA: pageURL.chuongTrinh.NANG_LUC_DAU_RA,
  DIEU_KIEN_TOT_NGHIEP: pageURL.chuongTrinh.DIEU_KIEN_TOT_NGHIEP,
  VI_TRI_VIEC_LAM: pageURL.chuongTrinh.VI_TRI_VIEC_LAM,
  MON_TIEN_QUYET: pageURL.chuongTrinh.MON_TIEN_QUYET,
  MON_TU_CHON: pageURL.chuongTrinh.MON_TU_CHON,
  QUY_DOI_DIEM: pageURL.chuongTrinh.QUY_DOI_DIEM,
  DIEU_KIEN_MON_HOC: pageURL.chuongTrinh.DIEU_KIEN_MON_HOC,
  NHOM_TU_CHON: pageURL.chuongTrinh.NHOM_TU_CHON,
  SYLLABUS_CHUONG_TRINH: pageURL.chuongTrinh.SYLLABUS_CHUONG_TRINH,
};

export const layIdBanGhi = (record = {}) => record.id ?? record.ID ?? record._id;

export const layGiaTriHienThi = (record = {}, keys = [], fallback = '—') => {
  for (const key of keys) {
    const value = record?.[key];
    if (value !== undefined && value !== null && value !== '') return value;
  }
  return fallback;
};

export const taoNhanChuongTrinh = (record = {}) => {
  const ma = layGiaTriHienThi(record, ['maChuongTrinh', 'ma', 'code'], 'CT');
  const ten = layGiaTriHienThi(record, ['tenChuongTrinh', 'ten', 'name'], 'Chương trình đào tạo');
  return `${ma} - ${ten}`;
};

export const taoNhanPhienBan = (record = {}) => {
  const ma = layGiaTriHienThi(record, ['maVersion', 'ma', 'code'], 'Version');
  const ten = layGiaTriHienThi(record, ['tenVersion', 'ten', 'name'], 'Phiên bản chương trình');
  return `${ma} - ${ten}`;
};

export const taoNhanMonHoc = (record = {}) => {
  const ma = layGiaTriHienThi(record, ['maMonTrongCt', 'maMon', 'ma', 'code'], 'MH');
  const ten = layGiaTriHienThi(record, ['tenMon', 'tenMonHoc', 'ten', 'name'], 'Môn học');
  return `${ma} - ${ten}`;
};

export const locTheoGiaTri = (rows = [], key, value) => {
  if (value === undefined || value === null || value === '') return rows;
  return rows.filter((row) => String(row?.[key]) === String(value));
};

export const layDanhSachChuongTrinh = (params = {}) => chuongTrinhApi.getList(API_LUONG_CHUONG_TRINH.CHUONG_TRINH, params);
export const layChiTietChuongTrinh = (id) => chuongTrinhApi.getDetail(API_LUONG_CHUONG_TRINH.CHUONG_TRINH, id);
export const layDanhSachPhienBan = (params = {}) => chuongTrinhApi.getList(API_LUONG_CHUONG_TRINH.PHIEN_BAN, params);
export const layChiTietPhienBan = (id) => chuongTrinhApi.getDetail(API_LUONG_CHUONG_TRINH.PHIEN_BAN, id);
export const layDanhSachNhomKienThuc = (params = {}) => chuongTrinhApi.getList(API_LUONG_CHUONG_TRINH.NHOM_KIEN_THUC, params);
export const layDanhSachMonTrongChuongTrinh = (params = {}) => chuongTrinhApi.getList(API_LUONG_CHUONG_TRINH.MON_TRONG_CHUONG_TRINH, params);
export const layChiTietMonTrongChuongTrinh = (id) => chuongTrinhApi.getDetail(API_LUONG_CHUONG_TRINH.MON_TRONG_CHUONG_TRINH, id);
export const layDanhSachMonHoc = (params = {}) => chuongTrinhApi.getList(API_LUONG_CHUONG_TRINH.MON_HOC, params);
export const layDanhSachSyllabusChuongTrinh = (params = {}) => chuongTrinhApi.getList(API_LUONG_CHUONG_TRINH.SYLLABUS_CHUONG_TRINH, params);
export const layDanhSachSyllabusMonHoc = (params = {}) => chuongTrinhApi.getList(API_LUONG_CHUONG_TRINH.SYLLABUS_MON_HOC, params);
export const layDanhSachSyllabusChuongBai = (params = {}) => chuongTrinhApi.getList(API_LUONG_CHUONG_TRINH.SYLLABUS_CHUONG_BAI, params);
export const layDanhSachSyllabusTaiLieu = (params = {}) => chuongTrinhApi.getList(API_LUONG_CHUONG_TRINH.SYLLABUS_TAI_LIEU, params);
export const layDanhSachMucTieu = (params = {}) => chuongTrinhApi.getList(API_LUONG_CHUONG_TRINH.MUC_TIEU, params);
export const layDanhSachNangLucDauRa = (params = {}) => chuongTrinhApi.getList(API_LUONG_CHUONG_TRINH.NANG_LUC_DAU_RA, params);
export const layDanhSachDieuKienTotNghiep = (params = {}) => chuongTrinhApi.getList(API_LUONG_CHUONG_TRINH.DIEU_KIEN_TOT_NGHIEP, params);
export const layDanhSachViTriViecLam = (params = {}) => chuongTrinhApi.getList(API_LUONG_CHUONG_TRINH.VI_TRI_VIEC_LAM, params);
export const layDanhSachMonTienQuyet = (params = {}) => chuongTrinhApi.getList(API_LUONG_CHUONG_TRINH.MON_TIEN_QUYET, params);
export const layDanhSachMonTuChon = (params = {}) => chuongTrinhApi.getList(API_LUONG_CHUONG_TRINH.MON_TU_CHON, params);
export const layDanhSachNhomTuChon = (params = {}) => chuongTrinhApi.getList(API_LUONG_CHUONG_TRINH.NHOM_TU_CHON, params);
export const layDanhSachQuyDoiDiem = (params = {}) => chuongTrinhApi.getList(API_LUONG_CHUONG_TRINH.QUY_DOI_DIEM, params);
export const layDanhSachDieuKienMonHoc = (params = {}) => chuongTrinhApi.getList(API_LUONG_CHUONG_TRINH.DIEU_KIEN_MON_HOC, params);

export const taoBanGhi = (endpoint, payload) => chuongTrinhApi.create(endpoint, payload);
export const capNhatBanGhi = (endpoint, id, payload) => chuongTrinhApi.update(endpoint, id, payload);
export const xoaBanGhi = (endpoint, id) => chuongTrinhApi.remove(endpoint, id);
