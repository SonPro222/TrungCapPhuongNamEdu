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
    dieuKienMonHoc: createCrudApi('dieu-kien-mon-hoc'),
    quyDoiDiem: createCrudApi('quy-doi-diem'),

    syllabusChuongTrinh: createCrudApi('syllabus-chuong-trinh'),
    syllabusMonHoc: createCrudApi('syllabus-mon-hoc'),
    syllabusChuongBai: createCrudApi('syllabus-chuong-bai'),
    syllabusTaiLieu: createCrudApi('syllabus-tai-lieu'),

    nhomKienThuc: createCrudApi('nhom-kien-thuc'),
    mucTieuChuongTrinh: createCrudApi('muc-tieu-chuong-trinh'),
    nangLucDauRa: createCrudApi('nang-luc-dau-ra'),
    viTriViecLam: createCrudApi('vi-tri-viec-lam'),
    dieuKienTotNghiep: createCrudApi('dieu-kien-tot-nghiep'),
    nhomTuChon: createCrudApi('nhom-tu-chon'),
    monTuChon: createCrudApi('mon-tu-chon'),
    monTienQuyet: createCrudApi('mon-tien-quyet')
}