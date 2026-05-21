import { daoTaoService } from '@/modules/daoTao/services/daoTaoService'
import { chuongTrinhService } from './chuongTrinhService'

export const xayDungChuongTrinhService = {
    nganh: daoTaoService.nganh,
    trinhDoDaoTao: daoTaoService.trinhDoDaoTao,
    loaiChuongTrinh: daoTaoService.loaiChuongTrinh,
    khungKy: daoTaoService.khungKy,

    chuongTrinh: chuongTrinhService.chuongTrinh,
    chuongTrinhVersion: chuongTrinhService.chuongTrinhVersion,
    monHoc: chuongTrinhService.monHoc,
    chuongTrinhMon: chuongTrinhService.chuongTrinhMon,
    nhomKienThuc: chuongTrinhService.nhomKienThuc,
    nhomTuChon: chuongTrinhService.nhomTuChon,
    monTuChon: chuongTrinhService.monTuChon,
    // 4 bảng này trong trang xây dựng dùng kho gốc để quản lý chung,
    // còn khi chọn vào Version sẽ lưu qua bảng nối riêng.
    mucTieuChuongTrinh: chuongTrinhService.mucTieuChuongTrinhGoc,
    nangLucDauRa: chuongTrinhService.nangLucDauRaGoc,
    viTriViecLam: chuongTrinhService.viTriViecLamGoc,
    dieuKienTotNghiep: chuongTrinhService.dieuKienTotNghiepGoc,

    mucTieuChuongTrinhGoc: chuongTrinhService.mucTieuChuongTrinhGoc,
    nangLucDauRaGoc: chuongTrinhService.nangLucDauRaGoc,
    viTriViecLamGoc: chuongTrinhService.viTriViecLamGoc,
    dieuKienTotNghiepGoc: chuongTrinhService.dieuKienTotNghiepGoc,

    chuongTrinhVersionMucTieu: chuongTrinhService.chuongTrinhVersionMucTieu,
    chuongTrinhVersionNangLuc: chuongTrinhService.chuongTrinhVersionNangLuc,
    chuongTrinhVersionViTriViecLam: chuongTrinhService.chuongTrinhVersionViTriViecLam,
    chuongTrinhVersionDieuKienTotNghiep: chuongTrinhService.chuongTrinhVersionDieuKienTotNghiep,

    syllabusChuongTrinh: chuongTrinhService.syllabusChuongTrinh,
    monTienQuyet: chuongTrinhService.monTienQuyet,
    quyDoiDiem: chuongTrinhService.quyDoiDiem,
    quyDoiDiemMau: chuongTrinhService.quyDoiDiemMau,
    chuongTrinhMonQuyDoiDiemMau: chuongTrinhService.chuongTrinhMonQuyDoiDiemMau,
    syllabusMonHoc: chuongTrinhService.syllabusMonHoc,
    dieuKienMonHoc: chuongTrinhService.dieuKienMonHoc,
    syllabusChuongBai: chuongTrinhService.syllabusChuongBai,
    syllabusTaiLieu: chuongTrinhService.syllabusTaiLieu
}
