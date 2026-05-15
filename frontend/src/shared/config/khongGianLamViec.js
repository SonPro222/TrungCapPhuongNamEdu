export const CHUONG_TRINH_NHOM = [
  {
    key: 'danh-muc',
    ten: 'Danh mục',
    moTa: 'Dữ liệu nền để lập chương trình.',
    muc: [
      { ten: 'Ngành', segment: 'nganh' },
      { ten: 'Trình độ', segment: 'trinh-do-dao-tao' },
      { ten: 'Loại chương trình', segment: 'loai-chuong-trinh' },
      { ten: 'Khung kỳ', segment: 'khung-ky' },
      { ten: 'Môn học', segment: 'mon-hoc' },
    ],
  },
  {
    key: 'khung-dao-tao',
    ten: 'Khung đào tạo',
    moTa: 'Chương trình, phiên bản, nhóm và môn.',
    muc: [
      { ten: 'Chương trình đào tạo', segment: 'chuong-trinh' },
      { ten: 'Phiên bản', segment: 'chuong-trinh-version' },
      { ten: 'Môn trong chương trình', segment: 'chuong-trinh-mon' },
      { ten: 'Nhóm kiến thức', segment: 'nhom-kien-thuc' },
    ],
  },
  {
    key: 'noi-dung-hoc',
    ten: 'Nội dung học',
    moTa: 'Syllabus, chương bài và tài liệu.',
    muc: [
      { ten: 'Syllabus chương trình', segment: 'syllabus-chuong-trinh' },
      { ten: 'Syllabus môn học', segment: 'syllabus-mon-hoc' },
      { ten: 'Chương bài', segment: 'syllabus-chuong-bai' },
      { ten: 'Tài liệu', segment: 'syllabus-tai-lieu' },
    ],
  },
  {
    key: 'chuan-rang-buoc',
    ten: 'Chuẩn & ràng buộc',
    moTa: 'Đầu ra, tự chọn, điều kiện và quy đổi.',
    muc: [
      { ten: 'Mục tiêu', segment: 'muc-tieu-chuong-trinh' },
      { ten: 'Năng lực', segment: 'nang-luc-dau-ra' },
      { ten: 'Việc làm', segment: 'vi-tri-viec-lam' },
      { ten: 'Điều kiện tốt nghiệp', segment: 'dieu-kien-tot-nghiep' },
      { ten: 'Môn tiên quyết', segment: 'mon-tien-quyet' },
      { ten: 'Nhóm tự chọn', segment: 'nhom-tu-chon' },
      { ten: 'Môn tự chọn', segment: 'mon-tu-chon' },
      { ten: 'Điều kiện môn học', segment: 'dieu-kien-mon-hoc' },
      { ten: 'Quy đổi điểm', segment: 'quy-doi-diem' },
    ],
  },
].map((group) => ({
  ...group,
  muc: group.muc.map((item) => {
    const quyTrinhMap = {
      'chuong-trinh': '/quy-trinh/chuong-trinh',
      'chuong-trinh-version': '/quy-trinh/chuong-trinh/phien-ban',
      'chuong-trinh-mon': '/quy-trinh/chuong-trinh/mon-trong-chuong-trinh',
    };

    return {
      ...item,
      quyTrinh: quyTrinhMap[item.segment] || `/quy-trinh/chuong-trinh/${item.segment}`,
      duLieu: `/du-lieu/chuong-trinh/${item.segment}`,
    };
  }),
}));

export const PHAN_HE_HE_THONG = [
  {
    key: 'chuong-trinh',
    ten: 'Chương trình',
    moTa: 'Khung đào tạo, môn học, syllabus và điều kiện.',
    quyTrinh: '/quy-trinh/chuong-trinh',
    duLieu: '/du-lieu/chuong-trinh',
    nhom: CHUONG_TRINH_NHOM,
  },
  { key: 'dao-tao', ten: 'Đào tạo', moTa: 'Lớp, khóa, lịch học và vận hành đào tạo.', quyTrinh: '/dao-tao', duLieu: '/du-lieu' },
  { key: 'sinh-vien', ten: 'Sinh viên', moTa: 'Hồ sơ, chương trình học và tiến độ học tập.', quyTrinh: '/sinh-vien', duLieu: '/du-lieu' },
  { key: 'giang-day', ten: 'Giảng dạy', moTa: 'Giảng viên, lớp học phần, lịch dạy và điểm danh.', quyTrinh: '/giang-day', duLieu: '/du-lieu' },
  { key: 'diem', ten: 'Điểm', moTa: 'Cấu hình điểm, bài kiểm tra và kết quả học tập.', quyTrinh: '/diem', duLieu: '/du-lieu' },
  { key: 'thi', ten: 'Thi', moTa: 'Lịch thi, đề thi, dự thi và bài thi.', quyTrinh: '/thi', duLieu: '/du-lieu' },
  { key: 'hoc-phi', ten: 'Học phí', moTa: 'Khoản thu, giao dịch và trạng thái thanh toán.', quyTrinh: '/hoc-phi', duLieu: '/du-lieu' },
  { key: 'lms', ten: 'LMS', moTa: 'Bài tập online, câu hỏi, làm bài và kết quả.', quyTrinh: '/lms', duLieu: '/du-lieu' },
  { key: 'he-thong', ten: 'Tài khoản', moTa: 'Người dùng, vai trò, quyền và nhật ký.', quyTrinh: '/he-thong', duLieu: '/du-lieu' },
];

export const DIEU_HUONG_CHINH = [
  { key: 'he-thong', ten: 'Hệ thống', to: '/he-thong', moTa: 'Cấu trúc', icon: '▦' },
  { key: 'quy-trinh', ten: 'Quy trình', to: '/quy-trinh', moTa: 'Làm việc', icon: '↳' },
  { key: 'du-lieu', ten: 'Dữ liệu', to: '/du-lieu', moTa: 'Quản lý', icon: '▤' },
];
