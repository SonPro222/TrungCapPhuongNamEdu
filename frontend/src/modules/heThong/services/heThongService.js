import { heThongApi } from '../api/heThongApi'

function layDanhSachTuKetQua(result) {
    if (Array.isArray(result)) {
        return result
    }

    if (Array.isArray(result?.content)) {
        return result.content
    }

    if (Array.isArray(result?.data?.content)) {
        return result.data.content
    }

    if (Array.isArray(result?.data?.data?.content)) {
        return result.data.data.content
    }

    if (Array.isArray(result?.data)) {
        return result.data
    }

    return []
}
export const heThongService = {
    layDanhSachTaiKhoan(params = {}) {
        return heThongApi.taiKhoan.getAll(params).then(layDanhSachTuKetQua)
    },

    layDanhSachVaiTro(params = {}) {
        return heThongApi.vaiTro.getAll(params).then(layDanhSachTuKetQua)
    },

    layDanhSachQuyen(params = {}) {
        return heThongApi.quyen.getAll(params).then(layDanhSachTuKetQua)
    },

    layDanhSachTaiKhoanVaiTro(params = {}) {
        return heThongApi.taiKhoanVaiTro.getAll(params).then(layDanhSachTuKetQua)
    },

    layDanhSachVaiTroQuyen(params = {}) {
        return heThongApi.vaiTroQuyen.getAll(params).then(layDanhSachTuKetQua)
    },

    layDanhSachNhanVien(params = {}) {
        return heThongApi.nhanVien.getAll(params).then(layDanhSachTuKetQua)
    },

    layDanhSachNhatKyDangNhap(params = {}) {
        return heThongApi.nhatKyDangNhap.getAll({
            page: 0,
            size: 100,
            sort: 'id,desc',
            ...params
        }).then(layDanhSachTuKetQua)
    },

    layDanhSachNhatKyHeThong(params = {}) {
        return heThongApi.nhatKyHeThong.getAll({
            page: 0,
            size: 100,
            sort: 'id,desc',
            ...params
        }).then(layDanhSachTuKetQua)
    },

    layDanhSachTepDinhKem(params = {}) {
        return heThongApi.tepDinhKem.getAll({
            page: 0,
            size: 100,
            ...params
        }).then(layDanhSachTuKetQua)
    }
}