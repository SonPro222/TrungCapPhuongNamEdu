const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';
const API_PREFIX = import.meta.env.VITE_API_PREFIX || '/api';

const joinUrl = (...parts) => parts
  .filter((part) => part !== undefined && part !== null && String(part).trim() !== '')
  .map((part, index) => {
    const value = String(part).trim();
    if (index === 0) return value.replace(/\/+$/, '');
    return value.replace(/^\/+|\/+$/g, '');
  })
  .join('/');

const pageURL = {
  API_BASE_URL,
  API_PREFIX,

  AUTH: joinUrl(API_PREFIX, 'auth'),
  CHUONG_TRINH: joinUrl(API_PREFIX, 'chuongTrinh'),
  DAO_TAO: joinUrl(API_PREFIX, 'dao-tao'),
  DIEM: joinUrl(API_PREFIX, 'diem'),
  GIANG_DAY: joinUrl(API_PREFIX, 'giang-day'),
  HE_THONG: joinUrl(API_PREFIX, 'he-thong'),
  HOC_PHI: joinUrl(API_PREFIX, 'hoc-phi'),
  LMS: joinUrl(API_PREFIX, 'lms'),
  SINH_VIEN: joinUrl(API_PREFIX, 'sinh-vien'),
  THI: joinUrl(API_PREFIX, 'thi'),

  daoTao: {
    NGANH: joinUrl(API_PREFIX, 'dao-tao', 'nganh'),
    TRINH_DO_DAO_TAO: joinUrl(API_PREFIX, 'dao-tao', 'trinh-do-dao-tao'),
    LOAI_CHUONG_TRINH: joinUrl(API_PREFIX, 'dao-tao', 'loai-chuong-trinh'),
    KHUNG_KY: joinUrl(API_PREFIX, 'dao-tao', 'khung-ky'),
    KHOA_DAO_TAO: joinUrl(API_PREFIX, 'dao-tao', 'khoa-dao-tao'),
    LOP_HANH_CHINH: joinUrl(API_PREFIX, 'dao-tao', 'lop-hanh-chinh'),
  },

  chuongTrinh: {
    CHUONG_TRINH: joinUrl(API_PREFIX, 'chuongTrinh', 'chuong-trinh'),
    CHUONG_TRINH_VERSION: joinUrl(API_PREFIX, 'chuongTrinh', 'chuong-trinh-version'),
    CHUONG_TRINH_MON: joinUrl(API_PREFIX, 'chuongTrinh', 'chuong-trinh-mon'),
    MON_HOC: joinUrl(API_PREFIX, 'chuongTrinh', 'mon-hoc'),
    DIEU_KIEN_MON_HOC: joinUrl(API_PREFIX, 'chuongTrinh', 'dieu-kien-mon-hoc'),
    DIEU_KIEN_TOT_NGHIEP: joinUrl(API_PREFIX, 'chuongTrinh', 'dieu-kien-tot-nghiep'),
    MON_TIEN_QUYET: joinUrl(API_PREFIX, 'chuongTrinh', 'mon-tien-quyet'),
    MON_TU_CHON: joinUrl(API_PREFIX, 'chuongTrinh', 'mon-tu-chon'),
    MUC_TIEU_CHUONG_TRINH: joinUrl(API_PREFIX, 'chuongTrinh', 'muc-tieu-chuong-trinh'),
    NANG_LUC_DAU_RA: joinUrl(API_PREFIX, 'chuongTrinh', 'nang-luc-dau-ra'),
    NHOM_KIEN_THUC: joinUrl(API_PREFIX, 'chuongTrinh', 'nhom-kien-thuc'),
    NHOM_TU_CHON: joinUrl(API_PREFIX, 'chuongTrinh', 'nhom-tu-chon'),
    QUY_DOI_DIEM: joinUrl(API_PREFIX, 'chuongTrinh', 'quy-doi-diem'),
    SYLLABUS_CHUONG_BAI: joinUrl(API_PREFIX, 'chuongTrinh', 'syllabus-chuong-bai'),
    SYLLABUS_CHUONG_TRINH: joinUrl(API_PREFIX, 'chuongTrinh', 'syllabus-chuong-trinh'),
    SYLLABUS_MON_HOC: joinUrl(API_PREFIX, 'chuongTrinh', 'syllabus-mon-hoc'),
    SYLLABUS_TAI_LIEU: joinUrl(API_PREFIX, 'chuongTrinh', 'syllabus-tai-lieu'),
    VI_TRI_VIEC_LAM: joinUrl(API_PREFIX, 'chuongTrinh', 'vi-tri-viec-lam'),
  },
};

export { joinUrl };
export default pageURL;
