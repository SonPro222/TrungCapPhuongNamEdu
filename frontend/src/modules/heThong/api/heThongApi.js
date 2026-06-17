import apiClient from '@/core/api/apiClient'

const BASE_URL = '/he-thong'

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

export const heThongApi = {
    taiKhoan: {
        ...createCrudApi('tai-khoan'),

        getPage(params = {}) {
            return apiClient.get(`${BASE_URL}/tai-khoan/page`, { params })
        },

        doiMatKhauQuanTri(id, payload) {
            return apiClient.put(`${BASE_URL}/tai-khoan/${id}/doi-mat-khau`, payload)
        },
        getByEmail(email) {
            return apiClient.get(`${BASE_URL}/tai-khoan/theo-gmail`, {
                params: { email }
            })
        },


    },

    vaiTro: createCrudApi('vai-tro'),

    quyen: createCrudApi('quyen'),

    taiKhoanVaiTro: createCrudApi('tai-khoan-vai-tro'),

    vaiTroQuyen: createCrudApi('vai-tro-quyen'),

    nhanVien: createCrudApi('nhan-vien'),

    nhatKyDangNhap: createCrudApi('nhat-ky-dang-nhap'),

    nhatKyHeThong: createCrudApi('nhat-ky-he-thong'),

    cauHinhMaSinhVien: {
        getAll(params = {}) {
            return apiClient.get(`${BASE_URL}/cau-hinh-ma-sinh-vien`, { params })
        },

        getById(id) {
            return apiClient.get(`${BASE_URL}/cau-hinh-ma-sinh-vien/${id}`)
        },

        create(payload) {
            return apiClient.post(`${BASE_URL}/cau-hinh-ma-sinh-vien`, payload)
        },

        update(id, payload) {
            return apiClient.put(`${BASE_URL}/cau-hinh-ma-sinh-vien/${id}`, payload)
        },

        delete(id) {
            return apiClient.delete(`${BASE_URL}/cau-hinh-ma-sinh-vien/${id}`)
        }
    },

    taiKhoanSinhVien: {
        getAll(params = {}) {
            return apiClient.get(`${BASE_URL}/tai-khoan-sinh-vien`, { params })
        },

        capTaiKhoan(sinhVienId, payload) {
            return apiClient.post(`${BASE_URL}/tai-khoan-sinh-vien/${sinhVienId}/cap-tai-khoan`, payload)
        },

        capNhatGmail(sinhVienId, payload) {
            return apiClient.put(`${BASE_URL}/tai-khoan-sinh-vien/${sinhVienId}/cap-nhat-gmail`, payload)
        },

        datLaiMatKhau(sinhVienId) {
            return apiClient.put(`${BASE_URL}/tai-khoan-sinh-vien/${sinhVienId}/dat-lai-mat-khau`)
        },

        khoaTaiKhoan(sinhVienId) {
            return apiClient.put(`${BASE_URL}/tai-khoan-sinh-vien/${sinhVienId}/khoa-tai-khoan`)
        },

        tuDongKhoa() {
            return apiClient.put(`${BASE_URL}/tai-khoan-sinh-vien/tu-dong-khoa`)
        },

        moKhoaTaiKhoan(sinhVienId) {
            return apiClient.put(`${BASE_URL}/tai-khoan-sinh-vien/${sinhVienId}/mo-khoa-tai-khoan`)
        },

        khoaNhieu(payload) {
            return apiClient.put(`${BASE_URL}/tai-khoan-sinh-vien/khoa-nhieu`, payload)
        },

        moKhoaNhieu(payload) {
            return apiClient.put(`${BASE_URL}/tai-khoan-sinh-vien/mo-khoa-nhieu`, payload)
        },

        guiOtpDoiMatKhau(payload) {
            return apiClient.post(`${BASE_URL}/tai-khoan-sinh-vien/doi-mat-khau/gui-otp`, payload)
        },

        xacNhanDoiMatKhau(payload) {
            return apiClient.post(`${BASE_URL}/tai-khoan-sinh-vien/doi-mat-khau/xac-nhan`, payload)
        }
    },

    tepDinhKem: {
        getAll(params = {}) {
            return apiClient.get(`${BASE_URL}/tep-dinh-kem`, { params })
        },

        getById(id) {
            return apiClient.get(`${BASE_URL}/tep-dinh-kem/${id}`)
        },

        getByDoiTuong(params = {}) {
            return apiClient.get(`${BASE_URL}/tep-dinh-kem/doi-tuong`, { params })
        },

        getByNguoiGui(params = {}) {
            return apiClient.get(`${BASE_URL}/tep-dinh-kem/nguoi-gui`, { params })
        },

        upload(file, data) {
            const formData = new FormData()

            formData.append('file', file)
            formData.append(
                'data',
                new Blob([JSON.stringify(data)], {
                    type: 'application/json'
                })
            )

            return apiClient.post(`${BASE_URL}/tep-dinh-kem/upload`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            })
        },

        downloadUrl(id) {
            return `${apiClient.defaults.baseURL}${BASE_URL}/tep-dinh-kem/${id}/download`
        },

        delete(id) {
            return apiClient.delete(`${BASE_URL}/tep-dinh-kem/${id}`)
        }
    }
}