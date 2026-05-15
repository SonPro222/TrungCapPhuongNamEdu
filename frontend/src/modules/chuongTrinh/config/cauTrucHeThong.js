import { CHUONG_TRINH_RESOURCES } from '@/modules/chuongTrinh/constants/trangThaiChuongTrinh.js';

const duLieuChuongTrinh = CHUONG_TRINH_RESOURCES.map((item) => ({
  label: item.title,
  hint: item.description,
  to: `/chuong-trinh/du-lieu/${item.segment}`,
}));

export const PHAN_HE_HE_THONG = [
  {
    key: 'chuong-trinh',
    label: 'Chương trình',
    hint: 'Chương trình, phiên bản, môn học, chuẩn đầu ra.',
    to: '/chuong-trinh/quy-trinh',
    children: [
      { label: 'Chương trình đào tạo', to: '/chuong-trinh/du-lieu/chuong-trinh' },
      { label: 'Phiên bản', to: '/chuong-trinh/du-lieu/chuong-trinh-version' },
      { label: 'Môn học', to: '/chuong-trinh/du-lieu/mon-hoc' },
      { label: 'Khung chương trình', to: '/chuong-trinh/du-lieu/chuong-trinh-mon' },
      { label: 'Syllabus', to: '/chuong-trinh/du-lieu/syllabus-mon-hoc' },
      { label: 'Điều kiện', to: '/chuong-trinh/du-lieu/dieu-kien-tot-nghiep' },
    ],
  },
  {
    key: 'dao-tao',
    label: 'Đào tạo',
    hint: 'Khóa, lớp, học phần, kế hoạch mở lớp.',
    to: '/dao-tao',
    children: [
      { label: 'Khóa đào tạo', to: '/dao-tao' },
      { label: 'Lớp hành chính', to: '/dao-tao' },
      { label: 'Kế hoạch học', to: '/dao-tao' },
    ],
  },
  {
    key: 'sinh-vien',
    label: 'Sinh viên',
    hint: 'Hồ sơ, chương trình học, lớp học phần.',
    to: '/sinh-vien',
    children: [
      { label: 'Hồ sơ', to: '/sinh-vien' },
      { label: 'Chương trình học', to: '/sinh-vien' },
      { label: 'Miễn trừ', to: '/sinh-vien' },
    ],
  },
  {
    key: 'giang-day',
    label: 'Giảng dạy',
    hint: 'Lớp học phần, lịch học, điểm danh, phân công.',
    to: '/giang-day',
    children: [
      { label: 'Lớp học phần', to: '/giang-day' },
      { label: 'Lịch học', to: '/giang-day' },
      { label: 'Giảng viên', to: '/giang-day' },
    ],
  },
  {
    key: 'diem',
    label: 'Điểm',
    hint: 'Cấu hình điểm, điểm chi tiết, kết quả môn.',
    to: '/diem',
    children: [
      { label: 'Cột điểm', to: '/diem' },
      { label: 'Nhập điểm', to: '/diem' },
      { label: 'Kết quả', to: '/diem' },
    ],
  },
  {
    key: 'thi',
    label: 'Thi',
    hint: 'Lịch thi, đề thi, sinh viên dự thi.',
    to: '/thi',
    children: [
      { label: 'Lịch thi', to: '/thi' },
      { label: 'Đề thi', to: '/thi' },
      { label: 'Dự thi', to: '/thi' },
    ],
  },
  {
    key: 'hoc-phi',
    label: 'Học phí',
    hint: 'Khoản thu, giao dịch, trạng thái đóng tiền.',
    to: '/hoc-phi',
    children: [
      { label: 'Khoản thu', to: '/hoc-phi' },
      { label: 'Giao dịch', to: '/hoc-phi' },
    ],
  },
  {
    key: 'lms',
    label: 'LMS',
    hint: 'Bài tập online, câu hỏi, kết quả làm bài.',
    to: '/lms',
    children: [
      { label: 'Bài tập', to: '/lms' },
      { label: 'Câu hỏi', to: '/lms' },
      { label: 'Kết quả', to: '/lms' },
    ],
  },
  {
    key: 'he-thong',
    label: 'Hệ thống',
    hint: 'Tài khoản, vai trò, quyền, nhật ký.',
    to: '/he-thong',
    children: [
      { label: 'Tài khoản', to: '/he-thong' },
      { label: 'Vai trò', to: '/he-thong' },
      { label: 'Nhật ký', to: '/he-thong' },
    ],
  },
];

export const QUY_TRINH_CHUONG_TRINH = [
  {
    label: 'Chương trình',
    hint: 'Tạo hoặc chọn chương trình đào tạo.',
    to: '/chuong-trinh/quy-trinh',
  },
  {
    label: 'Phiên bản',
    hint: 'Quản lý bản áp dụng, quyết định, tín chỉ.',
    to: '/chuong-trinh/du-lieu/chuong-trinh-version',
  },
  {
    label: 'Khung học',
    hint: 'Sắp xếp môn học theo kỳ và nhóm kiến thức.',
    to: '/chuong-trinh/du-lieu/chuong-trinh-mon',
  },
  {
    label: 'Mục tiêu',
    hint: 'Thiết lập mục tiêu và năng lực đầu ra.',
    to: '/chuong-trinh/du-lieu/muc-tieu-chuong-trinh',
  },
  {
    label: 'Syllabus',
    hint: 'Nội dung môn, tài liệu, điều kiện học.',
    to: '/chuong-trinh/du-lieu/syllabus-mon-hoc',
  },
  {
    label: 'Tốt nghiệp',
    hint: 'Điều kiện tốt nghiệp và quy đổi điểm.',
    to: '/chuong-trinh/du-lieu/dieu-kien-tot-nghiep',
  },
];

export const DU_LIEU_CHUONG_TRINH = duLieuChuongTrinh;
