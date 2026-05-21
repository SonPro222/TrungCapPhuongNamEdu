import apiClient from '@/core/api/apiClient'

const BASE_URL = '/dao-tao'

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

export const daoTaoApi = {
    nganh: createCrudApi('nganh'),
    trinhDoDaoTao: createCrudApi('trinh-do-dao-tao'),
    loaiChuongTrinh: createCrudApi('loai-chuong-trinh'),
    khungKy: createCrudApi('khung-ky'),
    khoaDaoTao: createCrudApi('khoa-dao-tao'),
    lopHanhChinh: createCrudApi('lop-hanh-chinh')
}
