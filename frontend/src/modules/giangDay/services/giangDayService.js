import { giangDayApi } from '../api/giangDayApi'
import { daoTaoApi } from '@/modules/daoTao/api/daoTaoApi'

function layPage(result) {
    const data = result?.data?.data || result?.data || result

    if (Array.isArray(data)) {
        return {
            content: data,
            number: 0,
            totalPages: 1,
            totalElements: data.length
        }
    }

    if (Array.isArray(data?.content)) {
        return {
            content: data.content,
            number: data.number || 0,
            totalPages: data.totalPages || 1,
            totalElements: data.totalElements || data.content.length
        }
    }

    return {
        content: [],
        number: 0,
        totalPages: 0,
        totalElements: 0
    }
}

export const giangDayService = {
    async layTrangGiaoVien(params = {}) {
        return layPage(await giangDayApi.giaoVien.getAll({
            page: 0,
            size: 100,
            sortBy: 'id',
            sortDir: 'desc',
            ...params
        }))
    },

    async taoGiaoVien(payload) {
        return giangDayApi.giaoVien.create(payload)
    },

    async capNhatGiaoVien(id, payload) {
        return giangDayApi.giaoVien.update(id, payload)
    },

    async xoaGiaoVien(id) {
        return giangDayApi.giaoVien.delete(id)
    },
    async layTrangPhongHoc(params = {}) {
        return layPage(await giangDayApi.phongHoc.getAll({
            page: 0,
            size: 100,
            sortBy: 'id',
            sortDir: 'desc',
            ...params
        }))
    },

    async taoPhongHoc(payload) {
        return giangDayApi.phongHoc.create(payload)
    },

    async capNhatPhongHoc(id, payload) {
        return giangDayApi.phongHoc.update(id, payload)
    },

    async xoaPhongHoc(id) {
        return giangDayApi.phongHoc.delete(id)
    },

    async layTrangCaHoc(params = {}) {
        return layPage(await giangDayApi.caHoc.getAll({
            page: 0,
            size: 100,
            sortBy: 'id',
            sortDir: 'desc',
            ...params
        }))
    },

    async taoCaHoc(payload) {
        return giangDayApi.caHoc.create(payload)
    },

    async capNhatCaHoc(id, payload) {
        return giangDayApi.caHoc.update(id, payload)
    },

    async xoaCaHoc(id) {
        return giangDayApi.caHoc.delete(id)
    },
    async layTrangPhanCongGiangDay(params = {}) {
        return layPage(await giangDayApi.phanCongGiangDay.getAll({
            page: 0,
            size: 100,
            sortBy: 'id',
            sortDir: 'desc',
            ...params
        }))
    },

    async taoPhanCongGiangDay(payload) {
        return giangDayApi.phanCongGiangDay.create(payload)
    },

    async capNhatPhanCongGiangDay(id, payload) {
        return giangDayApi.phanCongGiangDay.update(id, payload)
    },

    async xoaPhanCongGiangDay(id) {
        return giangDayApi.phanCongGiangDay.delete(id)
    },

    async layTrangSinhVienLopHocPhan(params = {}) {
        return layPage(await giangDayApi.sinhVienLopHocPhan.getAll({
            page: 0,
            size: 100,
            sortBy: 'id',
            sortDir: 'desc',
            ...params
        }))
    },

    async taoSinhVienLopHocPhan(payload) {
        return giangDayApi.sinhVienLopHocPhan.create(payload)
    },

    async capNhatSinhVienLopHocPhan(id, payload) {
        return giangDayApi.sinhVienLopHocPhan.update(id, payload)
    },

    async xoaSinhVienLopHocPhan(id) {
        return giangDayApi.sinhVienLopHocPhan.delete(id)
    },
    async layTrangLichHoc(params = {}) {
        return layPage(await giangDayApi.lichHoc.getAll({
            page: 0,
            size: 100,
            sortBy: 'ngayHoc',
            sortDir: 'desc',
            ...params
        }))
    },

    async taoLichHoc(payload) {
        return giangDayApi.lichHoc.create(payload)
    },

    async capNhatLichHoc(id, payload) {
        return giangDayApi.lichHoc.update(id, payload)
    },

    async xoaLichHoc(id) {
        return giangDayApi.lichHoc.delete(id)
    },

    async layTrangDiemDanh(params = {}) {
        return layPage(await giangDayApi.diemDanh.getAll({
            page: 0,
            size: 100,
            sortBy: 'id',
            sortDir: 'desc',
            ...params
        }))
    },

    async taoDiemDanh(payload) {
        return giangDayApi.diemDanh.create(payload)
    },

    async capNhatDiemDanh(id, payload) {
        return giangDayApi.diemDanh.update(id, payload)
    },

    async xoaDiemDanh(id) {
        return giangDayApi.diemDanh.delete(id)
    },

    async layTrangYeuCauDoiLich(params = {}) {
        return layPage(await giangDayApi.yeuCauDoiLich.getAll({
            page: 0,
            size: 100,
            sortBy: 'id',
            sortDir: 'desc',
            ...params
        }))
    },

    async taoYeuCauDoiLich(payload) {
        return giangDayApi.yeuCauDoiLich.create(payload)
    },

    async capNhatYeuCauDoiLich(id, payload) {
        return giangDayApi.yeuCauDoiLich.update(id, payload)
    },

    async xoaYeuCauDoiLich(id) {
        return giangDayApi.yeuCauDoiLich.delete(id)
    },
    async layTrangLopHocPhan(params = {}) {
        return layPage(await giangDayApi.lopHocPhan.getAll({
            page: 0,
            size: 100,
            sortBy: 'id',
            sortDir: 'desc',
            ...params
        }))
    },
    async layLopHocPhanTheoId(id) {
        const result = await giangDayApi.lopHocPhan.getById(id)
        return result?.data?.data || result?.data || result
    },

    async taoLopHocPhan(payload) {
        return giangDayApi.lopHocPhan.create(payload)
    },

    async capNhatLopHocPhan(id, payload) {
        return giangDayApi.lopHocPhan.update(id, payload)
    },

    async xoaLopHocPhan(id) {
        return giangDayApi.lopHocPhan.delete(id)
    },
    async layDanhSachNganh(params = {}) {
        return layPage(await daoTaoApi.nganh.getAll({
            page: 0,
            size: 200,
            sortBy: 'id',
            sortDir: 'desc',
            ...params
        })).content
    },

    async layDanhSachChuongTrinh(params = {}) {
        return layPage(await daoTaoApi.chuongTrinh.getAll({
            page: 0,
            size: 200,
            sortBy: 'id',
            sortDir: 'desc',
            ...params
        })).content
    },

    async layDanhSachChuongTrinhVersion(params = {}) {
        return layPage(await daoTaoApi.chuongTrinhVersion.getAll({
            page: 0,
            size: 200,
            sortBy: 'id',
            sortDir: 'desc',
            ...params
        })).content
    },

    async layDanhSachChuongTrinhMon(params = {}) {
        return layPage(await daoTaoApi.chuongTrinhMon.getAll({
            page: 0,
            size: 200,
            sortBy: 'id',
            sortDir: 'desc',
            ...params
        })).content
    },

    async layDanhSachMonHoc(params = {}) {
        return layPage(await daoTaoApi.monHoc.getAll({
            page: 0,
            size: 200,
            sortBy: 'id',
            sortDir: 'desc',
            ...params
        })).content
    },
    async layTrangLopHocPhanChuongTrinhMon(params = {}) {
        return layPage(await giangDayApi.lopHocPhanChuongTrinhMon.getAll({
            page: 0,
            size: 500,
            sortBy: 'id',
            sortDir: 'desc',
            ...params
        }))
    },

    async layDanhSachKhungKy(params = {}) {
        return layPage(await daoTaoApi.khungKy.getAll({
            page: 0,
            size: 500,
            sortBy: 'thuTu',
            sortDir: 'asc',
            ...params
        })).content
    },


    async layDanhSachSinhVienTrongLopHocPhan(lopHocPhanId, params = {}) {
        return layPage(await giangDayApi.sinhVienLopHocPhan.getSinhVienTrongLop(lopHocPhanId, {
            page: 0,
            size: 500,
            ...params
        })).content
    },



    async layTrangDiemChiTiet(params = {}) {
        return layPage(await giangDayApi.diemChiTiet.getAll({
            page: 0,
            size: 500,
            sortBy: 'id',
            sortDir: 'asc',
            ...params
        }))
    },

    async layTrangKetQuaLopHocPhan(params = {}) {
        return layPage(await giangDayApi.ketQuaLopHocPhan.getAll({
            page: 0,
            size: 500,
            sortBy: 'id',
            sortDir: 'asc',
            ...params
        }))
    },
    async layTrangCauHinhDanhGia(params = {}) {
        return layPage(await giangDayApi.cauHinhDanhGia.getAll({
            page: 0,
            size: 1000,
            sortBy: 'thuTu',
            sortDir: 'asc',
            ...params
        }))
    },

    async layTrangBaiKiemTra(params = {}) {
        return layPage(await giangDayApi.baiKiemTra.getAll({
            page: 0,
            size: 1000,
            sortBy: 'id',
            sortDir: 'asc',
            ...params
        }))
    },

}
