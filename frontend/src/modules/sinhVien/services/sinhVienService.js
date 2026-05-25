import { sinhVienApi } from '../api/sinhVienApi'

function layNoiDungPage(response) {
    if (Array.isArray(response)) return response
    if (Array.isArray(response?.content)) return response.content
    if (Array.isArray(response?.data?.content)) return response.data.content
    if (Array.isArray(response?.data)) return response.data
    return []
}

const PAGE_KHUNG = { page: 0, size: 200 }
const PAGE_DU_LIEU_LON = { page: 0, size: 100 }

export const sinhVienService = {
    async layNganh() {
        return layNoiDungPage(await sinhVienApi.nganh.getAll(PAGE_KHUNG))
    },

    async layLopHanhChinh() {
        return layNoiDungPage(await sinhVienApi.lopHanhChinh.getAll(PAGE_KHUNG))
    },

    async layLopHanhChinhTheoVersion(chuongTrinhVersionId) {
        if (!chuongTrinhVersionId) return []
        return layNoiDungPage(await sinhVienApi.lopHanhChinh.getTheoVersion(chuongTrinhVersionId, PAGE_KHUNG))
    },

    async layChuongTrinh() {
        return layNoiDungPage(await sinhVienApi.chuongTrinh.getAll(PAGE_KHUNG))
    },

    async layVersion() {
        return layNoiDungPage(await sinhVienApi.chuongTrinhVersion.getAll(PAGE_KHUNG))
    },

    async laySinhVienChuongTrinh(params = {}) {
        return layNoiDungPage(await sinhVienApi.sinhVienChuongTrinh.getAll({ ...PAGE_DU_LIEU_LON, ...params }))
    },

    async laySinhVienTheoNganhChuongTrinhVersion(nganhId, chuongTrinhId, chuongTrinhVersionId, page = 0, size = 100) {
        const params = { page, size }
        if (nganhId) params.nganhId = nganhId
        if (chuongTrinhId) params.chuongTrinhId = chuongTrinhId
        if (chuongTrinhVersionId) params.chuongTrinhVersionId = chuongTrinhVersionId
        return layNoiDungPage(await sinhVienApi.sinhVien.getTheoNganhChuongTrinhVersion(params))
    },

    async tiepNhanSinhVien(payload) {
        return sinhVienApi.sinhVien.tiepNhan(payload)
    },

    async tiepNhanSinhVienCoFile(payload, files) {
        return sinhVienApi.sinhVien.tiepNhanCoFile(payload, files)
    },

    async capNhatSinhVien(id, payload) {
        return sinhVienApi.sinhVien.update(id, payload)
    },

    async xoaSinhVien(id) {
        return sinhVienApi.sinhVien.delete(id)
    },

    layUrlTaiTep(id) {
        if (!id) return ''
        return sinhVienApi.tepDinhKem.downloadUrl(id)
    },

    async uploadTepSinhVien(file, sinhVienId, nghiepVu, moTa, nguoiGuiTen) {
        if (!file || !sinhVienId) return null
        return sinhVienApi.tepDinhKem.upload(file, {
            module: 'sinh_vien',
            nghiepVu,
            doiTuongId: sinhVienId,
            nguoiGuiLoai: 'NHAN_VIEN',
            nguoiGuiId: 0,
            nguoiGuiTen: nguoiGuiTen || 'Nhân viên tiếp nhận',
            moTa
        })
    },

    async layLopHocPhan(params = {}) {
        return layNoiDungPage(await sinhVienApi.lopHocPhan.getAll({ ...PAGE_DU_LIEU_LON, ...params }))
    },

    async layLopHocPhanChuongTrinhMon(params = {}) {
        return layNoiDungPage(await sinhVienApi.lopHocPhanChuongTrinhMon.getAll({ ...PAGE_DU_LIEU_LON, ...params }))
    },

    async taoLopHocPhan(payload) {
        return sinhVienApi.lopHocPhan.create(payload)
    },

    async taoLopHocPhanChuongTrinhMon(payload) {
        return sinhVienApi.lopHocPhanChuongTrinhMon.create(payload)
    },

    async laySinhVienLopHocPhan(params = {}) {
        return layNoiDungPage(await sinhVienApi.sinhVienLopHocPhan.getAll({ ...PAGE_DU_LIEU_LON, ...params }))
    },

    async layChuongTrinhMon() {
        return layNoiDungPage(await sinhVienApi.chuongTrinhMon.getAll(PAGE_KHUNG))
    },

    async dangKyLopHocPhan(payload) {
        return sinhVienApi.sinhVienLopHocPhan.create(payload)
    },

    async layMienTruBaoLuu(params = {}) {
        return layNoiDungPage(await sinhVienApi.mienTruBaoLuu.getAll({ ...PAGE_DU_LIEU_LON, ...params }))
    },

    async taoMienTruBaoLuu(payload) {
        return sinhVienApi.mienTruBaoLuu.create(payload)
    },

    async taoLopHanhChinh(payload) {
        return sinhVienApi.lopHanhChinh.create(payload)
    },

    async capNhatLopHanhChinh(id, payload) {
        return sinhVienApi.lopHanhChinh.update(id, payload)
    },

    async xoaLopHanhChinh(id) {
        return sinhVienApi.lopHanhChinh.delete(id)
    },

    async capNhatLopHocPhan(id, payload) {
        return sinhVienApi.lopHocPhan.update(id, payload)
    },

    async xoaLopHocPhan(id) {
        return sinhVienApi.lopHocPhan.delete(id)
    },

    async layKhungKy() {
        return layNoiDungPage(await sinhVienApi.khungKy.getAll(PAGE_KHUNG))
    },

    async layBaoLuu(params = {}) {
        return layNoiDungPage(await sinhVienApi.baoLuu.getAll({ ...PAGE_DU_LIEU_LON, ...params }))
    },

    async taoBaoLuu(payload) {
        return sinhVienApi.baoLuu.create(payload)
    },

    async choSinhVienDiHocLai(id, payload) {
        return sinhVienApi.baoLuu.diHocLai(id, payload)
    },

    async huyBaoLuu(id) {
        return sinhVienApi.baoLuu.huy(id)
    },
}