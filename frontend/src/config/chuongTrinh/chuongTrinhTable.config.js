import { CHUONG_TRINH_COLUMNS } from './chuongTrinhColumns.config';

const field = (key, label, type = 'text', extra = {}) => ({ key, label, type, ...extra });

export const SELECT_OPTIONS = {
  mucTieuLoai: [
    { value: 'chung', label: 'Chung' },
    { value: 'kien_thuc', label: 'Kiến thức' },
    { value: 'ky_nang', label: 'Kỹ năng' },
    { value: 'nang_luc_tu_chu_trach_nhiem', label: 'Năng lực tự chủ/trách nhiệm' },
  ],
  nangLucLoai: [
    { value: 'co_ban', label: 'Cơ bản' },
    { value: 'cot_loi', label: 'Cốt lõi' },
    { value: 'nang_cao', label: 'Nâng cao' },
  ],
  loaiNhom: [
    { value: 'chung', label: 'Chung' },
    { value: 'co_so', label: 'Cơ sở' },
    { value: 'chuyen_mon', label: 'Chuyên môn' },
    { value: 'tu_chon', label: 'Tự chọn' },
  ],
  loaiMonCt: [
    { value: 'bat_buoc', label: 'Bắt buộc' },
    { value: 'tu_chon', label: 'Tự chọn' },
  ],
  loaiHocPhan: [
    { value: 'mon_hoc', label: 'Môn học' },
    { value: 'mo_dun', label: 'Mô-đun' },
  ],
  loaiTienQuyet: [
    { value: 'tien_quyet', label: 'Tiên quyết' },
    { value: 'song_hanh', label: 'Song hành' },
    { value: 'hoc_truoc', label: 'Học trước' },
  ],
  donViDiem: [
    { value: 'thang_10', label: 'Thang 10' },
    { value: 'phan_tram', label: 'Phần trăm' },
  ],
  loaiDieuKienMon: [
    { value: 'phong_hoc', label: 'Phòng học' },
    { value: 'thiet_bi', label: 'Thiết bị' },
    { value: 'hoc_lieu', label: 'Học liệu' },
    { value: 'dung_cu', label: 'Dụng cụ' },
    { value: 'nguyen_vat_lieu', label: 'Nguyên vật liệu' },
    { value: 'khac', label: 'Khác' },
  ],
  ketQua: [
    { value: 'dat', label: 'Đạt' },
    { value: 'khong_dat', label: 'Không đạt' },
  ],
};

export const CHUONG_TRINH_TABLE_CONFIG = {
  nganh: {
    key: 'nganh', title: 'Ngành', apiKey: 'nganh', idKey: 'id', childRoute: 'trinh-do-dao-tao', columns: CHUONG_TRINH_COLUMNS.nganh,
    searchFields: ['maNganh', 'tenNganh', 'moTa'],
    formFields: [field('maNganh', 'Mã ngành', 'text', { required: true }), field('tenNganh', 'Tên ngành', 'text', { required: true }), field('moTa', 'Mô tả', 'textarea')],
    permissions: { create: ['ADMIN', 'DAO_TAO'], update: ['ADMIN', 'DAO_TAO'], delete: ['ADMIN'] },
  },
  'trinh-do-dao-tao': {
    key: 'trinh-do-dao-tao', title: 'Trình độ đào tạo', apiKey: 'trinhDoDaoTao', idKey: 'id', childRoute: 'loai-chuong-trinh', columns: CHUONG_TRINH_COLUMNS['trinh-do-dao-tao'],
    searchFields: ['maTrinhDo', 'tenTrinhDo', 'moTa'],
    formFields: [field('maTrinhDo', 'Mã trình độ', 'text', { required: true }), field('tenTrinhDo', 'Tên trình độ', 'text', { required: true }), field('moTa', 'Mô tả', 'textarea')],
    permissions: { create: ['ADMIN', 'DAO_TAO'], update: ['ADMIN', 'DAO_TAO'], delete: ['ADMIN'] },
  },
  'loai-chuong-trinh': {
    key: 'loai-chuong-trinh', title: 'Loại chương trình', apiKey: 'loaiChuongTrinh', idKey: 'id', childRoute: 'chuong-trinh', columns: CHUONG_TRINH_COLUMNS['loai-chuong-trinh'],
    searchFields: ['maLoai', 'tenLoai', 'moTa'],
    formFields: [field('maLoai', 'Mã loại', 'text', { required: true }), field('tenLoai', 'Tên loại', 'text', { required: true }), field('soThang', 'Số tháng', 'number', { required: true }), field('soKy', 'Số kỳ', 'number', { required: true }), field('moTa', 'Mô tả', 'textarea')],
    permissions: { create: ['ADMIN', 'DAO_TAO'], update: ['ADMIN', 'DAO_TAO'], delete: ['ADMIN'] },
  },
  'chuong-trinh': {
    key: 'chuong-trinh', title: 'Chương trình đào tạo', apiKey: 'chuongTrinh', idKey: 'id', childRoute: 'chuong-trinh-version', columns: CHUONG_TRINH_COLUMNS['chuong-trinh'],
    searchFields: ['maChuongTrinh', 'tenChuongTrinh', 'doiTuongTuyenSinh', 'thoiGianDaoTao'],
    formFields: [field('nganhId', 'Ngành', 'select', { optionResource: 'nganh', required: true }), field('trinhDoId', 'Trình độ', 'select', { optionResource: 'trinh-do-dao-tao', required: true }), field('loaiChuongTrinhId', 'Loại chương trình', 'select', { optionResource: 'loai-chuong-trinh', required: true }), field('maChuongTrinh', 'Mã chương trình', 'text', { required: true }), field('tenChuongTrinh', 'Tên chương trình', 'text', { required: true }), field('doiTuongTuyenSinh', 'Đối tượng tuyển sinh', 'textarea'), field('thoiGianDaoTao', 'Thời gian đào tạo')],
    permissions: { create: ['ADMIN', 'DAO_TAO'], update: ['ADMIN', 'DAO_TAO'], delete: ['ADMIN'] },
  },
  'chuong-trinh-version': {
    key: 'chuong-trinh-version', title: 'Version chương trình', apiKey: 'chuongTrinhVersion', parentKey: 'chuongTrinhId', idKey: 'id', childRoute: 'syllabus-chuong-trinh', columns: CHUONG_TRINH_COLUMNS['chuong-trinh-version'],
    searchFields: ['maVersion', 'tenVersion', 'soQuyetDinh', 'nguoiKy', 'coQuanBanHanh'],
    formFields: [field('chuongTrinhId', 'Chương trình', 'select', { optionResource: 'chuong-trinh', required: true }), field('maVersion', 'Mã version', 'text', { required: true }), field('tenVersion', 'Tên version'), field('ngayApDung', 'Ngày áp dụng', 'date'), field('ngayHetHieuLuc', 'Ngày hết hiệu lực', 'date'), field('soQuyetDinh', 'Số quyết định'), field('ngayQuyetDinh', 'Ngày quyết định', 'date'), field('nguoiKy', 'Người ký'), field('coQuanBanHanh', 'Cơ quan ban hành'), field('fileQuyetDinh', 'File quyết định'), field('tongTinChi', 'Tổng tín chỉ', 'number'), field('tongSoGio', 'Tổng số giờ', 'number'), field('tongGioLyThuyet', 'Tổng giờ lý thuyết', 'number'), field('tongGioThucHanh', 'Tổng giờ thực hành', 'number'), field('tongGioKiemTra', 'Tổng giờ kiểm tra', 'number'), field('laHienHanh', 'Là hiện hành', 'checkbox')],
    permissions: { create: ['ADMIN', 'DAO_TAO'], update: ['ADMIN', 'DAO_TAO'], delete: ['ADMIN'] },
  },
  'syllabus-chuong-trinh': {
    key: 'syllabus-chuong-trinh', title: 'Syllabus chương trình', apiKey: 'syllabusChuongTrinh', parentKey: 'chuongTrinhVersionId', idKey: 'id', childRoute: 'muc-tieu-chuong-trinh', columns: CHUONG_TRINH_COLUMNS['syllabus-chuong-trinh'],
    searchFields: ['moTaTongQuan', 'mucDich', 'yeuCauDaoTao', 'phuongPhapDaoTao'],
    formFields: [field('chuongTrinhVersionId', 'Version chương trình', 'select', { optionResource: 'chuong-trinh-version', required: true }), field('moTaTongQuan', 'Mô tả tổng quan', 'textarea'), field('mucDich', 'Mục đích', 'textarea'), field('yeuCauDaoTao', 'Yêu cầu đào tạo', 'textarea'), field('phuongPhapDaoTao', 'Phương pháp đào tạo', 'textarea'), field('ghiChu', 'Ghi chú', 'textarea')],
  },
  'muc-tieu-chuong-trinh': {
    key: 'muc-tieu-chuong-trinh', title: 'Mục tiêu chương trình', apiKey: 'mucTieuChuongTrinh', parentKey: 'chuongTrinhVersionId', idKey: 'id', childRoute: 'nang-luc-dau-ra', columns: CHUONG_TRINH_COLUMNS['muc-tieu-chuong-trinh'],
    searchFields: ['loai', 'noiDung'],
    formFields: [field('chuongTrinhVersionId', 'Version chương trình', 'select', { optionResource: 'chuong-trinh-version', required: true }), field('loai', 'Loại', 'select', { optionKey: 'mucTieuLoai', required: true }), field('noiDung', 'Nội dung', 'textarea', { required: true }), field('thuTu', 'Thứ tự', 'number')],
  },
  'nang-luc-dau-ra': {
    key: 'nang-luc-dau-ra', title: 'Năng lực đầu ra', apiKey: 'nangLucDauRa', parentKey: 'chuongTrinhVersionId', idKey: 'id', childRoute: 'vi-tri-viec-lam', columns: CHUONG_TRINH_COLUMNS['nang-luc-dau-ra'],
    searchFields: ['ma', 'noiDung', 'loai'],
    formFields: [field('chuongTrinhVersionId', 'Version chương trình', 'select', { optionResource: 'chuong-trinh-version', required: true }), field('ma', 'Mã'), field('noiDung', 'Nội dung', 'textarea', { required: true }), field('loai', 'Loại', 'select', { optionKey: 'nangLucLoai', required: true }), field('thuTu', 'Thứ tự', 'number')],
  },
  'vi-tri-viec-lam': {
    key: 'vi-tri-viec-lam', title: 'Vị trí việc làm', apiKey: 'viTriViecLam', parentKey: 'chuongTrinhVersionId', idKey: 'id', childRoute: 'khung-ky', columns: CHUONG_TRINH_COLUMNS['vi-tri-viec-lam'],
    searchFields: ['ten', 'moTa'],
    formFields: [field('chuongTrinhVersionId', 'Version chương trình', 'select', { optionResource: 'chuong-trinh-version', required: true }), field('ten', 'Tên vị trí', 'text', { required: true }), field('moTa', 'Mô tả', 'textarea'), field('thuTu', 'Thứ tự', 'number')],
  },
  'khung-ky': {
    key: 'khung-ky', title: 'Khung kỳ', apiKey: 'khungKy', parentKey: 'loaiChuongTrinhId', idKey: 'id', childRoute: 'nhom-kien-thuc', columns: CHUONG_TRINH_COLUMNS['khung-ky'],
    searchFields: ['maKy', 'tenKy'],
    formFields: [field('loaiChuongTrinhId', 'Loại chương trình', 'select', { optionResource: 'loai-chuong-trinh', required: true }), field('maKy', 'Mã kỳ', 'text', { required: true }), field('tenKy', 'Tên kỳ', 'text', { required: true }), field('thuTu', 'Thứ tự', 'number', { required: true })],
  },
  'nhom-kien-thuc': {
    key: 'nhom-kien-thuc', title: 'Nhóm kiến thức', apiKey: 'nhomKienThuc', parentKey: 'chuongTrinhVersionId', idKey: 'id', childRoute: 'mon-hoc', columns: CHUONG_TRINH_COLUMNS['nhom-kien-thuc'],
    searchFields: ['ma', 'ten', 'loaiNhom'],
    formFields: [field('chuongTrinhVersionId', 'Version chương trình', 'select', { optionResource: 'chuong-trinh-version', required: true }), field('ma', 'Mã nhóm'), field('ten', 'Tên nhóm', 'text', { required: true }), field('thuTu', 'Thứ tự', 'number'), field('loaiNhom', 'Loại nhóm', 'select', { optionKey: 'loaiNhom', required: true }), field('tongTinChi', 'Tổng tín chỉ', 'number'), field('tongSoGio', 'Tổng số giờ', 'number'), field('tongGioLyThuyet', 'Giờ lý thuyết', 'number'), field('tongGioThucHanh', 'Giờ thực hành', 'number'), field('tongGioKiemTra', 'Giờ kiểm tra', 'number')],
  },
  'mon-hoc': {
    key: 'mon-hoc', title: 'Môn học', apiKey: 'monHoc', idKey: 'id', childRoute: 'chuong-trinh-mon', columns: CHUONG_TRINH_COLUMNS['mon-hoc'],
    searchFields: ['maMon', 'tenMon', 'moTa'],
    formFields: [field('maMon', 'Mã môn'), field('tenMon', 'Tên môn', 'text', { required: true }), field('moTa', 'Mô tả', 'textarea')],
  },
  'chuong-trinh-mon': {
    key: 'chuong-trinh-mon', title: 'Môn trong chương trình', apiKey: 'chuongTrinhMon', parentKey: 'chuongTrinhVersionId', idKey: 'id', childRoute: 'mon-tien-quyet', columns: CHUONG_TRINH_COLUMNS['chuong-trinh-mon'],
    searchFields: ['maMonTrongCt', 'loai', 'loaiHocPhan', 'ghiChu'],
    formFields: [field('chuongTrinhVersionId', 'Version chương trình', 'select', { optionResource: 'chuong-trinh-version', required: true }), field('monHocId', 'Môn học', 'select', { optionResource: 'mon-hoc', required: true }), field('maMonTrongCt', 'Mã môn trong CT', 'text', { required: true }), field('khungKyId', 'Khung kỳ', 'select', { optionResource: 'khung-ky' }), field('nhomKienThucId', 'Nhóm kiến thức', 'select', { optionResource: 'nhom-kien-thuc' }), field('loai', 'Loại', 'select', { optionKey: 'loaiMonCt' }), field('loaiHocPhan', 'Loại học phần', 'select', { optionKey: 'loaiHocPhan' }), field('batBuoc', 'Bắt buộc', 'checkbox'), field('laMonDieuKien', 'Là môn điều kiện', 'checkbox'), field('thuTu', 'Thứ tự', 'number'), field('soTinChi', 'Số tín chỉ', 'number'), field('tongGio', 'Tổng giờ', 'number'), field('gioLyThuyet', 'Giờ lý thuyết', 'number'), field('gioThucHanh', 'Giờ thực hành', 'number'), field('gioKiemTra', 'Giờ kiểm tra', 'number'), field('ghiChu', 'Ghi chú', 'textarea')],
  },
  'mon-tien-quyet': {
    key: 'mon-tien-quyet', title: 'Môn tiên quyết', apiKey: 'monTienQuyet', parentKey: 'monId', idKey: 'id', childRoute: 'nhom-tu-chon', columns: CHUONG_TRINH_COLUMNS['mon-tien-quyet'],
    searchFields: ['loai', 'ghiChu'],
    formFields: [field('monId', 'Môn chính', 'select', { optionResource: 'chuong-trinh-mon', required: true }), field('monDieuKienId', 'Môn điều kiện', 'select', { optionResource: 'chuong-trinh-mon', required: true }), field('loai', 'Loại', 'select', { optionKey: 'loaiTienQuyet', required: true }), field('ghiChu', 'Ghi chú', 'textarea')],
  },
  'nhom-tu-chon': {
    key: 'nhom-tu-chon', title: 'Nhóm tự chọn', apiKey: 'nhomTuChon', parentKey: 'chuongTrinhVersionId', idKey: 'id', childRoute: 'syllabus-mon-hoc', columns: CHUONG_TRINH_COLUMNS['nhom-tu-chon'],
    searchFields: ['ten', 'ghiChu'],
    formFields: [field('chuongTrinhVersionId', 'Version chương trình', 'select', { optionResource: 'chuong-trinh-version', required: true }), field('ten', 'Tên nhóm', 'text', { required: true }), field('soMonChon', 'Số môn chọn', 'number'), field('soTinChiCanDat', 'Số tín chỉ cần đạt', 'number'), field('ghiChu', 'Ghi chú', 'textarea')],
  },
  'syllabus-mon-hoc': {
    key: 'syllabus-mon-hoc', title: 'Syllabus môn học', apiKey: 'syllabusMonHoc', parentKey: 'chuongTrinhMonId', idKey: 'id', childRoute: 'syllabus-chuong-bai', columns: CHUONG_TRINH_COLUMNS['syllabus-mon-hoc'],
    searchFields: ['viTri', 'tinhChat', 'mucTieu', 'phuongPhapDanhGia', 'dieuKienHoanThanh'],
    formFields: [field('chuongTrinhMonId', 'Môn trong chương trình', 'select', { optionResource: 'chuong-trinh-mon', required: true }), field('viTri', 'Vị trí'), field('tinhChat', 'Tính chất'), field('mucTieu', 'Mục tiêu', 'textarea'), field('phuongPhapDanhGia', 'Phương pháp đánh giá', 'textarea'), field('dieuKienHoanThanh', 'Điều kiện hoàn thành', 'textarea'), field('huongDan', 'Hướng dẫn', 'textarea'), field('diemDatToiThieu', 'Điểm đạt tối thiểu', 'number'), field('donViDiem', 'Đơn vị điểm', 'select', { optionKey: 'donViDiem' }), field('tyLeChuyenCanToiThieu', 'Tỷ lệ chuyên cần tối thiểu', 'number'), field('batBuocDuThi', 'Bắt buộc dự thi', 'checkbox'), field('congThucQuyDoi', 'Công thức quy đổi', 'textarea')],
  },
  'syllabus-chuong-bai': {
    key: 'syllabus-chuong-bai', title: 'Chương/bài syllabus', apiKey: 'syllabusChuongBai', parentKey: 'syllabusMonId', idKey: 'id', childRoute: 'syllabus-tai-lieu', columns: CHUONG_TRINH_COLUMNS['syllabus-chuong-bai'],
    searchFields: ['ten', 'noiDung', 'mucTieu'],
    formFields: [field('syllabusMonId', 'Syllabus môn học', 'select', { optionResource: 'syllabus-mon-hoc', required: true }), field('ten', 'Tên chương/bài', 'text', { required: true }), field('tongGio', 'Tổng giờ', 'number'), field('gioLyThuyet', 'Giờ lý thuyết', 'number'), field('gioThucHanh', 'Giờ thực hành', 'number'), field('gioKiemTra', 'Giờ kiểm tra', 'number'), field('noiDung', 'Nội dung', 'textarea'), field('mucTieu', 'Mục tiêu', 'textarea'), field('thuTu', 'Thứ tự', 'number')],
  },
  'syllabus-tai-lieu': {
    key: 'syllabus-tai-lieu', title: 'Tài liệu môn học', apiKey: 'syllabusTaiLieu', parentKey: 'syllabusMonId', idKey: 'id', childRoute: 'dieu-kien-mon-hoc', columns: CHUONG_TRINH_COLUMNS['syllabus-tai-lieu'],
    searchFields: ['ten', 'tacGia', 'nhaXuatBan', 'loai', 'ghiChu'],
    formFields: [field('syllabusMonId', 'Syllabus môn học', 'select', { optionResource: 'syllabus-mon-hoc', required: true }), field('ten', 'Tên tài liệu', 'text', { required: true }), field('tacGia', 'Tác giả'), field('namXuatBan', 'Năm xuất bản', 'number'), field('nhaXuatBan', 'Nhà xuất bản'), field('loai', 'Loại'), field('ghiChu', 'Ghi chú', 'textarea')],
  },
  'dieu-kien-mon-hoc': {
    key: 'dieu-kien-mon-hoc', title: 'Điều kiện môn học', apiKey: 'dieuKienMonHoc', parentKey: 'syllabusMonId', idKey: 'id', childRoute: 'quy-doi-diem', columns: CHUONG_TRINH_COLUMNS['dieu-kien-mon-hoc'],
    searchFields: ['loai', 'noiDung'],
    formFields: [field('syllabusMonId', 'Syllabus môn học', 'select', { optionResource: 'syllabus-mon-hoc', required: true }), field('loai', 'Loại', 'select', { optionKey: 'loaiDieuKienMon', required: true }), field('noiDung', 'Nội dung', 'textarea', { required: true }), field('thuTu', 'Thứ tự', 'number')],
  },
  'quy-doi-diem': {
    key: 'quy-doi-diem', title: 'Quy đổi điểm', apiKey: 'quyDoiDiem', parentKey: 'chuongTrinhMonId', idKey: 'id', childRoute: 'dieu-kien-tot-nghiep', columns: CHUONG_TRINH_COLUMNS['quy-doi-diem'],
    searchFields: ['ketQua', 'congThuc', 'ghiChu'],
    formFields: [field('chuongTrinhMonId', 'Môn trong chương trình', 'select', { optionResource: 'chuong-trinh-mon', required: true }), field('nguongTu', 'Ngưỡng từ', 'number'), field('nguongDen', 'Ngưỡng đến', 'number'), field('diemQuyDoi', 'Điểm quy đổi', 'number'), field('ketQua', 'Kết quả', 'select', { optionKey: 'ketQua' }), field('congThuc', 'Công thức', 'textarea'), field('ghiChu', 'Ghi chú', 'textarea')],
  },
  'dieu-kien-tot-nghiep': {
    key: 'dieu-kien-tot-nghiep', title: 'Điều kiện tốt nghiệp', apiKey: 'dieuKienTotNghiep', parentKey: 'chuongTrinhVersionId', idKey: 'id', columns: CHUONG_TRINH_COLUMNS['dieu-kien-tot-nghiep'],
    searchFields: ['noiDung'],
    formFields: [field('chuongTrinhVersionId', 'Version chương trình', 'select', { optionResource: 'chuong-trinh-version', required: true }), field('noiDung', 'Nội dung', 'textarea', { required: true }), field('thuTu', 'Thứ tự', 'number')],
  },
};

Object.keys(CHUONG_TRINH_TABLE_CONFIG).forEach((key) => {
  if (!CHUONG_TRINH_TABLE_CONFIG[key].permissions) {
    CHUONG_TRINH_TABLE_CONFIG[key].permissions = { create: ['ADMIN', 'DAO_TAO'], update: ['ADMIN', 'DAO_TAO'], delete: ['ADMIN'] };
  }
});
