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
    void xoaTheoSyllabusMonHocGocId(Long syllabusMonHocGocId);

    void xoaTheoKhungKyGocId(Long khungKyGocId);

    void xoaTheoNhomKienThucGocId(Long nhomKienThucGocId);

    void xoaTheoNhomTuChonGocId(Long nhomTuChonGocId);

    void xoaTheoMucTieuGocId(Long mucTieuGocId);

    void xoaTheoNangLucGocId(Long nangLucGocId);

    void xoaTheoViTriGocId(Long viTriGocId);

    void xoaTheoDieuKienTotNghiepGocId(Long dieuKienGocId);

    void xoaTheoQuyDoiDiemMauId(Long quyDoiDiemMauId);

    void xoaTheoDieuKienMonHocGocId(Long dieuKienGocId);

    void xoaTheoTaiLieuGocId(Long taiLieuGocId);
}