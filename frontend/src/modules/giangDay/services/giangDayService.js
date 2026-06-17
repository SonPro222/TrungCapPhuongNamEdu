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

    async layGiaoVienTheoId(id) {
        const result = await giangDayApi.giaoVien.getById(id)
        return result?.data?.data || result?.data || result
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

    async layTrangGiaoVienKhaDung(params = {}) {
        return layPage(await giangDayApi.giaoVienKhaDung.getAll({
            page: 0,
            size: 500,
            sortBy: 'id',
            sortDir: 'desc',
            ...params
        }))
    },

    async taoGiaoVienKhaDung(payload) {
        return giangDayApi.giaoVienKhaDung.create(payload)
    },

    async capNhatGiaoVienKhaDung(id, payload) {
        return giangDayApi.giaoVienKhaDung.update(id, payload)
    },

    async xoaGiaoVienKhaDung(id) {
        return giangDayApi.giaoVienKhaDung.delete(id)
    },

    async layTrangNgayNghi(params = {}) {
        return layPage(await giangDayApi.ngayNghi.getAll({
            page: 0,
            size: 500,
            sortBy: 'ngay',
            sortDir: 'asc',
            ...params
        }))
    },

    async taoNgayNghi(payload) {
        return giangDayApi.ngayNghi.create(payload)
    },

    async capNhatNgayNghi(id, payload) {
        return giangDayApi.ngayNghi.update(id, payload)
    },

    async xoaNgayNghi(id) {
        return giangDayApi.ngayNghi.delete(id)
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

    async layTrangLichHocAdmin(params = {}) {
        return layPage(await giangDayApi.lichHoc.getAdmin({
            page: 0,
            size: 100,
            sortBy: 'ngayHoc',
            sortDir: 'desc',
            ...params
        }))
    },

    async layLichHocSinhVien(params = {}) {
        return layPage(await giangDayApi.lichHoc.getSinhVienMe({
            page: 0,
            size: 100,
            sortBy: 'ngayHoc',
            sortDir: 'asc',
            ...params
        }))
    },

    async layLichDayGiangVien(params = {}) {
        return layPage(await giangDayApi.lichHoc.getGiangVienMe({
            page: 0,
            size: 100,
            sortBy: 'ngayHoc',
            sortDir: 'asc',
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

    async previewSinhLich(lopHocPhanId, payload) {
        const result = await giangDayApi.dieuPhoi.previewSinhLich(lopHocPhanId, payload)
        return result?.data?.data || result?.data || result
    },

    async taoLichTuDong(lopHocPhanId, payload) {
        const result = await giangDayApi.dieuPhoi.taoLichTuDong(lopHocPhanId, payload)
        return result?.data?.data || result?.data || result
    },

    async goiYLichHoc(lopHocPhanId, payload) {
        const result = await giangDayApi.dieuPhoi.goiYLichHoc(lopHocPhanId, payload)
        return result?.data?.data || result?.data || result
    },

    async xepLichHangLoat(lopHocPhanId, payload) {
        const result = await giangDayApi.dieuPhoi.xepLichHangLoat(lopHocPhanId, payload)
        return result?.data?.data || result?.data || result
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
    async layDanhSachLopHocPhan(params = {}) {
        return layPage(await giangDayApi.lopHocPhan.getAll({
            page: 0,
            size: 500,
            sortBy: 'id',
            sortDir: 'desc',
            ...params
        })).content
    },

    async layDanhSachGiaoVien(params = {}) {
        return layPage(await giangDayApi.giaoVien.getAll({
            page: 0,
            size: 500,
            sortBy: 'id',
            sortDir: 'desc',
            ...params
        })).content
    },

    async layDanhSachPhongHoc(params = {}) {
        return layPage(await giangDayApi.phongHoc.getAll({
            page: 0,
            size: 500,
            sortBy: 'id',
            sortDir: 'desc',
            ...params
        })).content
    },

    async layDanhSachCaHoc(params = {}) {
        return layPage(await giangDayApi.caHoc.getAll({
            page: 0,
            size: 500,
            sortBy: 'id',
            sortDir: 'asc',
            ...params
        })).content
    },

    // GiaoVienMonHoc
    async layTrangGiaoVienMonHoc(params = {}) {
        return giangDayApi.giaoVienMonHoc.getAll({ page: 0, size: 200, ...params })
    },
    async layDanhSachGiaoVienMonHoc(params = {}) {
        const res = await giangDayApi.giaoVienMonHoc.getAll({ page: 0, size: 500, ...params })
        const data = res?.data?.data ?? res?.data ?? res
        if (Array.isArray(data?.content)) return data.content
        if (Array.isArray(data?.data?.content)) return data.data.content
        if (Array.isArray(data)) return data
        return []
    },
    async taoGiaoVienMonHoc(payload) {
        return giangDayApi.giaoVienMonHoc.create(payload)
    },
    async capNhatGiaoVienMonHoc(id, payload) {
        return giangDayApi.giaoVienMonHoc.update(id, payload)
    },
    async xoaGiaoVienMonHoc(id) {
        return giangDayApi.giaoVienMonHoc.delete(id)
    },
    // GiangVienDangKyGiangDay
    async layTrangGiangVienDangKyGiangDay(params = {}) {
        return giangDayApi.giangVienDangKyGiangDay.getAll({ page: 0, size: 200, ...params })
    },
    async layDanhSachGiangVienDangKyGiangDay(params = {}) {
        const res = await giangDayApi.giangVienDangKyGiangDay.getAll({ page: 0, size: 500, ...params })
        const data = res?.data?.data ?? res?.data ?? res
        if (Array.isArray(data?.content)) return data.content
        if (Array.isArray(data?.data?.content)) return data.data.content
        if (Array.isArray(data)) return data
        return []
    },
    async taoGiangVienDangKyGiangDay(payload) {
        return giangDayApi.giangVienDangKyGiangDay.create(payload)
    },
    async capNhatGiangVienDangKyGiangDay(id, payload) {
        return giangDayApi.giangVienDangKyGiangDay.update(id, payload)
    },
    async xoaGiangVienDangKyGiangDay(id) {
        return giangDayApi.giangVienDangKyGiangDay.delete(id)
    },

    // GiaoVienDangKyNhomCa
    async layDanhSachNhomCaTheoGiaoVien(giaoVienId) {
        const res = await giangDayApi.giaoVienDangKyNhomCa.getByGiaoVien(giaoVienId)
        return res?.data?.data || res?.data || []
    },
    async taoNhomCa(payload) {
        return giangDayApi.giaoVienDangKyNhomCa.create(payload)
    },
    async capNhatNhomCa(id, payload) {
        return giangDayApi.giaoVienDangKyNhomCa.update(id, payload)
    },
    async xoaNhomCa(id) {
        return giangDayApi.giaoVienDangKyNhomCa.delete(id)
    },
    

}

export default giangDayService