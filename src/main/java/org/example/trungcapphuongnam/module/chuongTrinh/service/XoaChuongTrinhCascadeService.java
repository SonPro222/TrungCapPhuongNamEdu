package org.example.trungcapphuongnam.module.chuongTrinh.service;

public interface XoaChuongTrinhCascadeService {

    void xoaTheoNganhId(Long nganhId);

    void xoaTheoChuongTrinhId(Long chuongTrinhId);

    void xoaTheoChuongTrinhVersionId(Long chuongTrinhVersionId);

    void xoaTheoChuongTrinhMonId(Long chuongTrinhMonId);

    void xoaTheoSyllabusMonHocId(Long syllabusMonHocId);

    void xoaTheoKhungKyId(Long khungKyId);

    void xoaTheoNhomTuChonId(Long nhomTuChonId);
}