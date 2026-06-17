import apiClient from '@/core/api/apiClient'

const BASE = '/lms'
const DAO_TAO_BASE = '/dao-tao'
const CHUONG_TRINH_BASE = '/chuongTrinh'
const GIANG_DAY_BASE = '/giang-day'
const DIEM_BASE = '/diem'
const SINH_VIEN_BASE = '/sinh-vien'

function crudApi(path) {
    return {
        getAll(params = {}) { return apiClient.get(`${BASE}/${path}`, { params }) },
        getPage(params = {}) { return apiClient.get(`${BASE}/${path}`, { params }) },
        getById(id) { return apiClient.get(`${BASE}/${path}/${id}`) },
        create(payload) { return apiClient.post(`${BASE}/${path}`, payload) },
        update(id, payload) { return apiClient.put(`${BASE}/${path}/${id}`, payload) },
        delete(id) { return apiClient.delete(`${BASE}/${path}/${id}`) },
    }
}

function danhMucApi(base, path) {
    return {
        getAll(params = {}) { return apiClient.get(`${base}/${path}`, { params }) },
        getById(id) { return apiClient.get(`${base}/${path}/${id}`) },
    }
}

export const lmsApi = {
    // Legacy read-only / doi chieu du lieu cu
    nganHangCauHoi: crudApi('ngan-hang-cau-hoi'),
    dapAnCauHoi: crudApi('dap-an-cau-hoi'),

    // Ngan hang cau hoi version moi
    cauHoimau: crudApi('cau-hoi-mau'),
    cauHoiVersion: crudApi('cau-hoi-version'),
    dapAnCauHoiVersion: crudApi('dap-an-cau-hoi-version'),
    cauHoiApDung: crudApi('cau-hoi-ap-dung'),
    lichSuCauHoi: crudApi('lich-su-cau-hoi'),
    rubricCauHoiVersion: crudApi('rubric-cau-hoi-version'),

    // Ma tran de / bai tap
    maTranDe: crudApi('ma-tran-de'),
    maTranDeChiTiet: crudApi('ma-tran-de-chi-tiet'),
    baiTapOnline: crudApi('bai-tap-online'),
    baiTapOnlineCauHoi: crudApi('bai-tap-online-cau-hoi'),
    dotGiaoBaiOnline: crudApi('dot-giao-bai-online'),
    baiTapLop: {
        ...crudApi('bai-tap-lop'),
        dayDiem(id, payload) { return apiClient.post(`${BASE}/bai-tap-lop/${id}/day-diem`, payload) },
    },

    // Lam bai / snapshot / nop bai
    lanLamBaiOnline: crudApi('lan-lam-bai-online'),
    lanLamBaiCauHoi: crudApi('lan-lam-bai-cau-hoi'),
    lanLamBaiDapAn: crudApi('lan-lam-bai-dap-an'),
    cauTraLoiSinhVien: crudApi('cau-tra-loi-sinh-vien'),
    cauTraLoiChonDapAn: crudApi('cau-tra-loi-chon-dap-an'),
    baiNopLms: crudApi('bai-nop-lms'),
    baiNopLmsTep: crudApi('bai-nop-lms-tep'),
    nhatKyLamBaiOnline: crudApi('nhat-ky-lam-bai-online'),

    // Ket qua / diem / lich su
    ketQuaBaiTapLop: crudApi('ket-qua-bai-tap-lop'),
    lichSuBaiTapLop: crudApi('lich-su-bai-tap-lop'),
    lichSuChinhDiem: crudApi('lich-su-chinh-diem-bai-tap'),
    lichSuDayDiem: crudApi('lich-su-day-diem-lms'),


    diemChiTiet: {
        getAll(params = {}) { return apiClient.get(`${DIEM_BASE}/diem-chi-tiet`, { params }) },
        getById(id) { return apiClient.get(`${DIEM_BASE}/diem-chi-tiet/${id}`) },
        create(payload) { return apiClient.post(`${DIEM_BASE}/diem-chi-tiet`, payload) },
        update(id, payload) { return apiClient.put(`${DIEM_BASE}/diem-chi-tiet/${id}`, payload) },
        delete(id) { return apiClient.delete(`${DIEM_BASE}/diem-chi-tiet/${id}`) },
    },
    ketQuaLopHocPhan: {
        getAll(params = {}) { return apiClient.get(`${DIEM_BASE}/ket-qua-lop-hoc-phan`, { params }) },
        getById(id) { return apiClient.get(`${DIEM_BASE}/ket-qua-lop-hoc-phan/${id}`) },
        update(id, payload) { return apiClient.put(`${DIEM_BASE}/ket-qua-lop-hoc-phan/${id}`, payload) },
    },

    // Nghiep vu sinh vien hien co trong BE
    sinhVien: {
        batDauLamBai(baiTapLopId) { return apiClient.post(`${BASE}/sinh-vien/bai-tap-lop/${baiTapLopId}/bat-dau`) },
        nopBai(lanLamBaiId, payload) { return apiClient.post(`${BASE}/sinh-vien/lan-lam-bai/${lanLamBaiId}/nop-bai`, payload) },
    },

    danhMuc: {
        nganh: danhMucApi(DAO_TAO_BASE, 'nganh'),
        chuongTrinh: danhMucApi(CHUONG_TRINH_BASE, 'chuong-trinh'),
        chuongTrinhVersion: danhMucApi(CHUONG_TRINH_BASE, 'chuong-trinh-version'),
        chuongTrinhMon: danhMucApi(CHUONG_TRINH_BASE, 'chuong-trinh-mon'),
        monHoc: danhMucApi(CHUONG_TRINH_BASE, 'mon-hoc'),
        syllabusChuongBai: danhMucApi(CHUONG_TRINH_BASE, 'syllabus-chuong-bai'),
        lopHocPhan: danhMucApi(GIANG_DAY_BASE, 'lop-hoc-phan'),
        cauHinhDanhGia: danhMucApi(DIEM_BASE, 'cau-hinh-danh-gia'),
        sinhVien: danhMucApi(SINH_VIEN_BASE, 'sinh-vien'),
    },
}

export const lmsCrudRegistry = {
    cauHoimau: { label: 'Câu hỏi gốc', api: lmsApi.cauHoimau },
    cauHoiVersion: { label: 'Version câu hỏi', api: lmsApi.cauHoiVersion },
    dapAnCauHoiVersion: { label: 'Đáp án version', api: lmsApi.dapAnCauHoiVersion },
    cauHoiApDung: { label: 'Áp dụng câu hỏi', api: lmsApi.cauHoiApDung },
    lichSuCauHoi: { label: 'Lịch sử câu hỏi', api: lmsApi.lichSuCauHoi },
    rubricCauHoiVersion: { label: 'Rubric câu hỏi', api: lmsApi.rubricCauHoiVersion },
    maTranDe: { label: 'Ma trận đề', api: lmsApi.maTranDe },
    maTranDeChiTiet: { label: 'Chi tiết ma trận đề', api: lmsApi.maTranDeChiTiet },
    baiTapOnline: { label: 'Bài tập online', api: lmsApi.baiTapOnline },
    baiTapOnlineCauHoi: { label: 'Câu hỏi trong bài tập', api: lmsApi.baiTapOnlineCauHoi },
    dotGiaoBaiOnline: { label: 'Đợt giao bài', api: lmsApi.dotGiaoBaiOnline },
    baiTapLop: { label: 'Bài tập lớp', api: lmsApi.baiTapLop },
    lanLamBaiOnline: { label: 'Lần làm bài', api: lmsApi.lanLamBaiOnline },
    lanLamBaiCauHoi: { label: 'Snapshot câu hỏi', api: lmsApi.lanLamBaiCauHoi },
    lanLamBaiDapAn: { label: 'Snapshot đáp án', api: lmsApi.lanLamBaiDapAn },
    cauTraLoiSinhVien: { label: 'Câu trả lời sinh viên', api: lmsApi.cauTraLoiSinhVien },
    cauTraLoiChonDapAn: { label: 'Đáp án sinh viên chọn', api: lmsApi.cauTraLoiChonDapAn },
    baiNopLms: { label: 'Bài nộp LMS', api: lmsApi.baiNopLms },
    baiNopLmsTep: { label: 'Tệp bài nộp LMS', api: lmsApi.baiNopLmsTep },
    nhatKyLamBaiOnline: { label: 'Nhật ký làm bài', api: lmsApi.nhatKyLamBaiOnline },
    ketQuaBaiTapLop: { label: 'Kết quả bài tập lớp', api: lmsApi.ketQuaBaiTapLop },
    lichSuBaiTapLop: { label: 'Lịch sử bài tập lớp', api: lmsApi.lichSuBaiTapLop },
    lichSuChinhDiem: { label: 'Lịch sử chỉnh điểm', api: lmsApi.lichSuChinhDiem },
    lichSuDayDiem: { label: 'Lịch sử đẩy điểm LMS', api: lmsApi.lichSuDayDiem },
}
