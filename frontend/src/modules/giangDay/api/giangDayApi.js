import apiClient from '@/core/api/apiClient'

const BASE_URL = '/giang-day'

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

export const giangDayApi = {
    giaoVien: createCrudApi('giao-vien'),
    phongHoc: createCrudApi('phong-hoc'),
    caHoc: createCrudApi('ca-hoc'),
    lopHocPhan: createCrudApi('lop-hoc-phan'),
    lopHocPhanChuongTrinhMon: createCrudApi('lop-hoc-phan-chuong-trinh-mon'),
    phanCongGiangDay: createCrudApi('phan-cong-giang-day'),
    sinhVienLopHocPhan: createCrudApi('sinh-vien-lop-hoc-phan'),
    lichHoc: createCrudApi('lich-hoc'),
    yeuCauDoiLich: createCrudApi('yeu-cau-doi-lich'),
    diemDanh: createCrudApi('diem-danh')
}