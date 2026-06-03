import apiClient from '@/core/api/apiClient'

const BASE_URL = '/giang-day'
const DIEM_URL = '/diem'

function createCrudApi(path, baseUrl = BASE_URL) {
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

export const giangDayApi = {
    giaoVien: createCrudApi('giao-vien'),
    phongHoc: createCrudApi('phong-hoc'),
    caHoc: createCrudApi('ca-hoc'),
    lopHocPhan: createCrudApi('lop-hoc-phan'),
    lopHocPhanChuongTrinhMon: createCrudApi('lop-hoc-phan-chuong-trinh-mon'),
    phanCongGiangDay: createCrudApi('phan-cong-giang-day'),
    giaoVienKhaDung: createCrudApi('giao-vien-kha-dung'),
    ngayNghi: createCrudApi('ngay-nghi'),

    dieuPhoi: {
        previewSinhLich(lopHocPhanId, payload) {
            return apiClient.post(`${BASE_URL}/lop-hoc-phan/${lopHocPhanId}/dieu-phoi/lich-hoc/preview`, payload)
        },

        taoLichTuDong(lopHocPhanId, payload) {
            return apiClient.post(`${BASE_URL}/lop-hoc-phan/${lopHocPhanId}/dieu-phoi/lich-hoc/tao-tu-dong`, payload)
        },

        goiYLichHoc(lopHocPhanId, payload) {
            return apiClient.post(`${BASE_URL}/lop-hoc-phan/${lopHocPhanId}/dieu-phoi/lich-hoc/goi-y`, payload)
        },

        xepLichHangLoat(lopHocPhanId, payload) {
            return apiClient.post(`${BASE_URL}/lop-hoc-phan/${lopHocPhanId}/dieu-phoi/lich-hoc/xep-hang-loat`, payload)
        }
    },

    sinhVienLopHocPhan: {
        ...createCrudApi('sinh-vien-lop-hoc-phan'),

        getTheoLopHocPhan(lopHocPhanId, params = {}) {
            return apiClient.get(`${BASE_URL}/sinh-vien-lop-hoc-phan/theo-lop-hoc-phan/${lopHocPhanId}`, { params })
        },

        getSinhVienTrongLop(lopHocPhanId, params = {}) {
            return apiClient.get(`${BASE_URL}/sinh-vien-lop-hoc-phan/lop-hoc-phan/${lopHocPhanId}/sinh-vien`, { params })
        }
    },

    lichHoc: createCrudApi('lich-hoc'),
    yeuCauDoiLich: createCrudApi('yeu-cau-doi-lich'),
    diemDanh: createCrudApi('diem-danh'),

    diemChiTiet: createCrudApi('diem-chi-tiet', DIEM_URL),
    ketQuaLopHocPhan: createCrudApi('ket-qua-lop-hoc-phan', DIEM_URL),
    cauHinhDanhGia: createCrudApi('cau-hinh-danh-gia', DIEM_URL),
    baiKiemTra: createCrudApi('bai-kiem-tra', DIEM_URL),
}