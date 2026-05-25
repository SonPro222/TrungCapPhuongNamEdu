import apiClient from '@/core/api/apiClient'

const SINH_VIEN_URL = '/sinh-vien'
const DAO_TAO_URL = '/dao-tao'
const CHUONG_TRINH_URL = '/chuongTrinh'
const GIANG_DAY_URL = '/giang-day'
const HE_THONG_URL = '/he-thong'

function createCrudApi(baseUrl, path) {
    return {
        getAll(params = {}) {
            return apiClient.get(`${baseUrl}/${path}`, { params })
        },
        getById(id) {
            return apiClient.get(`${baseUrl}/${path}/${id}`)
        },
        create(payload) {
            return apiClient.post(`${baseUrl}/${path}`, payload)
        },
        update(id, payload) {
            return apiClient.put(`${baseUrl}/${path}/${id}`, payload)
        },
        delete(id) {
            return apiClient.delete(`${baseUrl}/${path}/${id}`)
        }
    }
}

export const sinhVienApi = {
    nganh: createCrudApi(DAO_TAO_URL, 'nganh'),

    lopHanhChinh: {
        ...createCrudApi(DAO_TAO_URL, 'lop-hanh-chinh'),
        getTheoVersion(chuongTrinhVersionId, params = {}) {
            return apiClient.get(`${DAO_TAO_URL}/lop-hanh-chinh/theo-version/${chuongTrinhVersionId}`, { params })
        }
    },

    chuongTrinh: createCrudApi(CHUONG_TRINH_URL, 'chuong-trinh'),
    chuongTrinhVersion: createCrudApi(CHUONG_TRINH_URL, 'chuong-trinh-version'),
    chuongTrinhMon: createCrudApi(CHUONG_TRINH_URL, 'chuong-trinh-mon'),
    khungKy: createCrudApi(DAO_TAO_URL, 'khung-ky'),
    lopHocPhan: createCrudApi(GIANG_DAY_URL, 'lop-hoc-phan'),

    lopHocPhanChuongTrinhMon: {
        ...createCrudApi(GIANG_DAY_URL, 'lop-hoc-phan-chuong-trinh-mon'),

        getTheoLopHocPhan(lopHocPhanId) {
            return apiClient.get(`${GIANG_DAY_URL}/lop-hoc-phan-chuong-trinh-mon/theo-lop-hoc-phan/${lopHocPhanId}`)
        }
    },

    sinhVienLopHocPhan: {
        ...createCrudApi(GIANG_DAY_URL, 'sinh-vien-lop-hoc-phan'),

        getTheoLopHocPhan(lopHocPhanId) {
            return apiClient.get(`${GIANG_DAY_URL}/sinh-vien-lop-hoc-phan/theo-lop-hoc-phan/${lopHocPhanId}`)
        },

        getSinhVienTrongLop(lopHocPhanId) {
            return apiClient.get(`${GIANG_DAY_URL}/sinh-vien-lop-hoc-phan/lop-hoc-phan/${lopHocPhanId}/sinh-vien`)
        },

        getSinhVienChuaVaoLop(lopHocPhanId, chuongTrinhVersionId) {
            return apiClient.get(`${GIANG_DAY_URL}/sinh-vien-lop-hoc-phan/lop-hoc-phan/${lopHocPhanId}/sinh-vien-chua-vao-lop`, {
                params: { chuongTrinhVersionId }
            })
        }
    },
    sinhVienChuongTrinh: createCrudApi(SINH_VIEN_URL, 'chuong-trinh'),
    mienTruBaoLuu: createCrudApi(SINH_VIEN_URL, 'mien-tru-bao-luu'),

    sinhVien: {
        getAll(params = {}) {
            return apiClient.get(SINH_VIEN_URL, { params })
        },
        getTheoNganhChuongTrinhVersion(params = {}) {
            return apiClient.get(`${SINH_VIEN_URL}/loc/theo-nganh-chuong-trinh-version`, { params })
        },
        getById(id) {
            return apiClient.get(`${SINH_VIEN_URL}/${id}`)
        },
        tiepNhan(payload) {
            return apiClient.post(`${SINH_VIEN_URL}/tiep-nhan`, payload)
        },
        tiepNhanCoFile(payload, files = {}) {
            const formData = new FormData()
            formData.append('data', new Blob([JSON.stringify(payload)], { type: 'application/json' }))

            if (files.anhChanDung) formData.append('anhChanDung', files.anhChanDung)
            if (files.cccdTruoc) formData.append('cccdTruoc', files.cccdTruoc)
            if (files.cccdSau) formData.append('cccdSau', files.cccdSau)
            if (files.bangCap) formData.append('bangCap', files.bangCap)

            ;(files.giayToKhac || []).forEach(file => {
                if (file) formData.append('giayToKhac', file)
            })

            return apiClient.post(`${SINH_VIEN_URL}/tiep-nhan-co-file`, formData, {
                headers: { 'Content-Type': 'multipart/form-data' }
            })
        },
        update(id, payload) {
            return apiClient.put(`${SINH_VIEN_URL}/${id}`, payload)
        },
        delete(id) {
            return apiClient.delete(`${SINH_VIEN_URL}/${id}`)
        }
    },

    tepDinhKem: {
        getByDoiTuong(params = {}) {
            return apiClient.get(`${HE_THONG_URL}/tep-dinh-kem/doi-tuong`, { params })
        },
        upload(file, data) {
            const formData = new FormData()
            formData.append('file', file)
            formData.append('data', new Blob([JSON.stringify(data)], { type: 'application/json' }))

            return apiClient.post(`${HE_THONG_URL}/tep-dinh-kem/upload`, formData, {
                headers: { 'Content-Type': 'multipart/form-data' }
            })
        },
        downloadUrl(id) {
            return `${apiClient.defaults.baseURL}${HE_THONG_URL}/tep-dinh-kem/${id}/download`
        },
        delete(id) {
            return apiClient.delete(`${HE_THONG_URL}/tep-dinh-kem/${id}`)
        }
    },
    baoLuu: {
        ...createCrudApi(SINH_VIEN_URL, 'bao-luu'),

        diHocLai(id, payload) {
            return apiClient.put(`${SINH_VIEN_URL}/bao-luu/${id}/di-hoc-lai`, payload)
        },

        huy(id) {
            return apiClient.put(`${SINH_VIEN_URL}/bao-luu/${id}/huy`)
        }
    },
}