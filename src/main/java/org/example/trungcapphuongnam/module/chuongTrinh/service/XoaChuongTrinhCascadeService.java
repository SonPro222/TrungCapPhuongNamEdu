package org.example.trungcapphuongnam.module.chuongTrinh.service;

public interface XoaChuongTrinhCascadeService {

    void xoaTheoNganhId(Long nganhId);

    void xoaTheoChuongTrinhId(Long chuongTrinhId);

    void xoaTheoChuongTrinhVersionId(Long chuongTrinhVersionId);

    void xoaTheoChuongTrinhMonId(Long chuongTrinhMonId);

    void xoaTheoSyllabusMonHocId(Long syllabusMonHocId);

    void xoaTheoKhungKyId(Long khungKyId);

    void xoaTheoNhomTuChonId(Long nhomTuChonId);


    //Mới thêm
    void xoaTheoSyllabusMonHocMauId(Long syllabusMonHocMauId);


    void xoaTheoNhomKienThucMauId(Long nhomKienThucMauId);

    void xoaTheoNhomTuChonMauId(Long nhomTuChonMauId);

    void xoaTheoMucTieuMauId(Long mucTieuMauId);

    void xoaTheoNangLucMauId(Long nangLucMauId);

    void xoaTheoViTriMauId(Long viTriMauId);

    void xoaTheoDieuKienTotNghiepMauId(Long dieuKienMauId);

    void xoaTheoQuyDoiDiemMauId(Long quyDoiDiemMauId);

    void xoaTheoDieuKienMonHocMauId(Long dieuKienMauId);

    void xoaTheoTaiLieuMauId(Long taiLieuMauId);
}