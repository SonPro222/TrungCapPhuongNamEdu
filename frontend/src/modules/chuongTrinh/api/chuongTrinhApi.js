import apiClient from '@/core/api/apiClient'

const BASE_URL = '/chuongTrinh'

function createCrudApi(path) {
    return {
        getAll(params = {}) {
            return apiClient.get(`${BASE_URL}/${path}`, { params })
        },

        getById(id) {
            return apiClient.get(`${BASE_URL}/${path}/${id}`)
        },

        create(payload) {
            return apiClient.post(`${BASE_URL}/${path}`, payload)
        },

        update(id, payload) {
            return apiClient.put(`${BASE_URL}/${path}/${id}`, payload)
        },

        delete(id) {
            return apiClient.delete(`${BASE_URL}/${path}/${id}`)
        }
    }
}

export const chuongTrinhApi = {
    chuongTrinh: createCrudApi('chuong-trinh'),
    chuongTrinhVersion: createCrudApi('chuong-trinh-version'),
    chuongTrinhMon: createCrudApi('chuong-trinh-mon'),

    monHoc: createCrudApi('mon-hoc'),
    nhomKienThuc: createCrudApi('nhom-kien-thuc'),
    nhomTuChon: createCrudApi('nhom-tu-chon'),
    monTuChon: createCrudApi('mon-tu-chon'),
    monTienQuyet: createCrudApi('mon-tien-quyet'),
    quyDoiDiem: createCrudApi('quy-doi-diem'),
    quyDoiDiemMau: createCrudApi('quy-doi-diem-mau'),
    chuongTrinhMonQuyDoiDiemMau: createCrudApi('chuong-trinh-mon-quy-doi-diem-mau'),

    mucTieuChuongTrinh: createCrudApi('muc-tieu-chuong-trinh'),
    nangLucDauRa: createCrudApi('nang-luc-dau-ra'),
    viTriViecLam: createCrudApi('vi-tri-viec-lam'),
    dieuKienTotNghiep: createCrudApi('dieu-kien-tot-nghiep'),

    // Kho danh mục gốc dùng chung toàn hệ thống
    mucTieuChuongTrinhGoc: createCrudApi('muc-tieu-chuong-trinh-goc'),
    nangLucDauRaGoc: createCrudApi('nang-luc-dau-ra-goc'),
    viTriViecLamGoc: createCrudApi('vi-tri-viec-lam-goc'),
    dieuKienTotNghiepGoc: createCrudApi('dieu-kien-tot-nghiep-goc'),

    // Bảng nối gán danh mục gốc vào từng phiên bản chương trình
    chuongTrinhVersionMucTieu: createCrudApi('chuong-trinh-version-muc-tieu'),
    chuongTrinhVersionNangLuc: createCrudApi('chuong-trinh-version-nang-luc'),
    chuongTrinhVersionViTriViecLam: createCrudApi('chuong-trinh-version-vi-tri-viec-lam'),
    chuongTrinhVersionDieuKienTotNghiep: createCrudApi('chuong-trinh-version-dieu-kien-tot-nghiep'),

    syllabusChuongTrinh: createCrudApi('syllabus-chuong-trinh'),
    syllabusChuongTrinhGoc: createCrudApi('syllabus-chuong-trinh-goc'),
    nhomKienThucGoc: createCrudApi('nhom-kien-thuc-goc'),
    nhomTuChonGoc: createCrudApi('nhom-tu-chon-goc'),
    syllabusMonHoc: createCrudApi('syllabus-mon-hoc'),
    syllabusMonHocGoc: createCrudApi('syllabus-mon-hoc-goc'),
    syllabusMonHocGocChuongBai: createCrudApi('syllabus-mon-hoc-goc-chuong-bai'),
    syllabusMonHocGocDieuKien: createCrudApi('syllabus-mon-hoc-goc-dieu-kien'),
    syllabusMonHocGocTaiLieu: createCrudApi('syllabus-mon-hoc-goc-tai-lieu'),
    dieuKienMonHoc: createCrudApi('dieu-kien-mon-hoc'),
    dieuKienMonHocGoc: createCrudApi('dieu-kien-mon-hoc-goc'),
    syllabusMonHocDieuKien: createCrudApi('syllabus-mon-hoc-dieu-kien'),
    taiLieuGoc: createCrudApi('tai-lieu-goc'),
    syllabusMonHocTaiLieu: createCrudApi('syllabus-mon-hoc-tai-lieu'),
    syllabusChuongBai: createCrudApi('syllabus-chuong-bai'),
    syllabusTaiLieu: createCrudApi('syllabus-tai-lieu')
}
