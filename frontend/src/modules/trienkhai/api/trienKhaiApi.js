import apiClient from '@/core/api/apiClient'

const DAO_TAO_URL = '/dao-tao'
const CHUONG_TRINH_URL = '/chuongTrinh'
const GIANG_DAY_URL = '/giang-day'
const DIEM_URL = '/diem'

function get(url, params = {}) {
    return apiClient.get(url, { params })
}

export const trienKhaiApi = {
    nganh: {
        getAll(params = {}) { return get(`${DAO_TAO_URL}/nganh`, params) }
    },
    nganhHeDaoTao: {
        getAll(params = {}) { return get(`${DAO_TAO_URL}/nganh-he-dao-tao`, params) }
    },
    chuongTrinh: {
        getAll(params = {}) { return get(`${CHUONG_TRINH_URL}/chuong-trinh`, params) }
    },
    chuongTrinhVersion: {
        getAll(params = {}) { return get(`${CHUONG_TRINH_URL}/chuong-trinh-version`, params) }
    },
    khungKy: {
        getByVersion(chuongTrinhVersionId, params = {}) {
            return get(`${DAO_TAO_URL}/khung-ky/theo-version/${chuongTrinhVersionId}`, params)
        }
    },
    chuongTrinhMon: {
        getAll(params = {}) { return get(`${CHUONG_TRINH_URL}/chuong-trinh-mon`, params) }
    },
    lopHocPhan: {
        getAll(params = {}) { return get(`${GIANG_DAY_URL}/lop-hoc-phan`, params) },
        getById(id) { return get(`${GIANG_DAY_URL}/lop-hoc-phan/${id}`) }
    },
    sinhVienLopHocPhan: {
        getAll(params = {}) { return get(`${GIANG_DAY_URL}/sinh-vien-lop-hoc-phan`, params) },
        getTheoLop(lopHocPhanId, params = {}) {
            return get(`${GIANG_DAY_URL}/sinh-vien-lop-hoc-phan/theo-lop-hoc-phan/${lopHocPhanId}`, params)
        }
    },
    phanCongGiangDay: {
        getAll(params = {}) { return get(`${GIANG_DAY_URL}/phan-cong-giang-day`, params) }
    },
    lichHoc: {
        getAll(params = {}) { return get(`${GIANG_DAY_URL}/lich-hoc/admin`, params) }
    },
    ketQuaLopHocPhan: {
        getAll(params = {}) { return get(`${DIEM_URL}/ket-qua-lop-hoc-phan`, params) }
    }
}
