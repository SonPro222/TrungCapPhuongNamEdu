import { daoTaoService } from '@/modules/daoTao/services/daoTaoService'
import { chuongTrinhService } from '@/modules/chuongTrinh/services/chuongTrinhService'

export const daoTaoXemChuongTrinhService = {
    nganh: daoTaoService.nganh,
    trinhDoDaoTao: daoTaoService.trinhDoDaoTao,
    loaiChuongTrinh: daoTaoService.loaiChuongTrinh,
    khungKy: daoTaoService.khungKy,
    khungKyGoc: daoTaoService.khungKyGoc,

    chuongTrinh: chuongTrinhService.chuongTrinh,
    chuongTrinhVersion: chuongTrinhService.chuongTrinhVersion,
    monHoc: chuongTrinhService.monHoc,
    chuongTrinhMon: chuongTrinhService.chuongTrinhMon,
    nhomKienThuc: chuongTrinhService.nhomKienThuc,
    nhomKienThucGoc: chuongTrinhService.nhomKienThucGoc,
    nhomTuChon: chuongTrinhService.nhomTuChon,
    nhomTuChonGoc: chuongTrinhService.nhomTuChonGoc,
    monTuChon: chuongTrinhService.monTuChon,
    mucTieuChuongTrinh: chuongTrinhService.mucTieuChuongTrinh,
    nangLucDauRa: chuongTrinhService.nangLucDauRa,
    viTriViecLam: chuongTrinhService.viTriViecLam,
    dieuKienTotNghiep: chuongTrinhService.dieuKienTotNghiep,

    mucTieuChuongTrinhGoc: chuongTrinhService.mucTieuChuongTrinhGoc,
    nangLucDauRaGoc: chuongTrinhService.nangLucDauRaGoc,
    viTriViecLamGoc: chuongTrinhService.viTriViecLamGoc,
    dieuKienTotNghiepGoc: chuongTrinhService.dieuKienTotNghiepGoc,

    chuongTrinhVersionMucTieu: chuongTrinhService.chuongTrinhVersionMucTieu,
    chuongTrinhVersionNangLuc: chuongTrinhService.chuongTrinhVersionNangLuc,
    chuongTrinhVersionViTriViecLam: chuongTrinhService.chuongTrinhVersionViTriViecLam,
    chuongTrinhVersionDieuKienTotNghiep: chuongTrinhService.chuongTrinhVersionDieuKienTotNghiep,

    syllabusChuongTrinh: chuongTrinhService.syllabusChuongTrinh,
    syllabusChuongTrinhGoc: chuongTrinhService.syllabusChuongTrinhGoc,
    monTienQuyet: chuongTrinhService.monTienQuyet,
    quyDoiDiem: chuongTrinhService.quyDoiDiem,
    quyDoiDiemMau: chuongTrinhService.quyDoiDiemMau,
    chuongTrinhMonQuyDoiDiemMau: chuongTrinhService.chuongTrinhMonQuyDoiDiemMau,
    syllabusMonHoc: chuongTrinhService.syllabusMonHoc,
    syllabusMonHocGoc: chuongTrinhService.syllabusMonHocGoc,
    syllabusMonHocGocChuongBai: chuongTrinhService.syllabusMonHocGocChuongBai,
    syllabusMonHocGocDieuKien: chuongTrinhService.syllabusMonHocGocDieuKien,
    syllabusMonHocGocTaiLieu: chuongTrinhService.syllabusMonHocGocTaiLieu,
    dieuKienMonHoc: chuongTrinhService.dieuKienMonHoc,
    // Điều kiện môn học mới dùng kho gốc + bảng nối vào syllabus môn học
    dieuKienMonHocGoc: chuongTrinhService.dieuKienMonHocGoc,
    syllabusMonHocDieuKien: chuongTrinhService.syllabusMonHocDieuKien,
    taiLieuGoc: chuongTrinhService.taiLieuGoc,
    syllabusMonHocTaiLieu: chuongTrinhService.syllabusMonHocTaiLieu,
    syllabusChuongBai: chuongTrinhService.syllabusChuongBai,
    syllabusTaiLieu: chuongTrinhService.syllabusTaiLieu
}
