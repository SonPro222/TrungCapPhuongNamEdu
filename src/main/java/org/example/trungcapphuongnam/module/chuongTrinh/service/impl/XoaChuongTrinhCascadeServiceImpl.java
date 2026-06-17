package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersion;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHoc;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.example.trungcapphuongnam.module.daoTao.entity.KhungKy;
import org.example.trungcapphuongnam.module.daoTao.repository.KhungKyRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.LopHanhChinhRepository;
import org.example.trungcapphuongnam.module.diem.repository.CauHinhDanhGiaRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class XoaChuongTrinhCascadeServiceImpl implements XoaChuongTrinhCascadeService {
    private final LopHocPhanRepository lopHocPhanRepository;
    private final ChuongTrinhRepository chuongTrinhRepository;
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final ChuongTrinhMonRepository chuongTrinhMonRepository;

    private final MucTieuChuongTrinhRepository mucTieuChuongTrinhRepository;
    private final NangLucDauRaRepository nangLucDauRaRepository;
    private final ViTriViecLamRepository viTriViecLamRepository;
    private final DieuKienTotNghiepRepository dieuKienTotNghiepRepository;
    private final SyllabusChuongTrinhRepository syllabusChuongTrinhRepository;

    private final NhomKienThucRepository nhomKienThucRepository;
    private final NhomTuChonRepository nhomTuChonRepository;
    private final MonTuChonRepository monTuChonRepository;

    private final MonTienQuyetRepository monTienQuyetRepository;
    private final QuyDoiDiemRepository quyDoiDiemRepository;
    private final CauHinhDanhGiaRepository cauHinhDanhGiaRepository;
    private final SyllabusMonHocRepository syllabusMonHocRepository;
    private final DieuKienMonHocRepository dieuKienMonHocRepository;
    private final SyllabusChuongBaiRepository syllabusChuongBaiRepository;
    private final SyllabusTaiLieuRepository syllabusTaiLieuRepository;

    private final KhungKyRepository khungKyRepository;
    private final LopHanhChinhRepository lopHanhChinhRepository;


    //Mới thêm
    private final ChuongTrinhVersionMucTieuRepository chuongTrinhVersionMucTieuRepository;
    private final ChuongTrinhVersionNangLucRepository chuongTrinhVersionNangLucRepository;
    private final ChuongTrinhVersionViTriViecLamRepository chuongTrinhVersionViTriViecLamRepository;
    private final ChuongTrinhVersionDieuKienTotNghiepRepository chuongTrinhVersionDieuKienTotNghiepRepository;

    private final ChuongTrinhMonQuyDoiDiemMauRepository chuongTrinhMonQuyDoiDiemMauRepository;

    private final SyllabusMonHocDieuKienRepository syllabusMonHocDieuKienRepository;
    private final SyllabusMonHocTaiLieuRepository syllabusMonHocTaiLieuRepository;

    private final SyllabusMonHocMauChuongBaiRepository syllabusMonHocMauChuongBaiRepository;
    private final SyllabusMonHocMauDieuKienRepository syllabusMonHocMauDieuKienRepository;
    private final SyllabusMonHocMauTaiLieuRepository syllabusMonHocMauTaiLieuRepository;
    private final SyllabusMonHocMauRepository syllabusMonHocMauRepository;

    @Override
    @Transactional
    public void xoaTheoNganhId(Long nganhId) {
        List<ChuongTrinh> chuongTrinhList = chuongTrinhRepository
                .findByNganhId(nganhId, Pageable.unpaged())
                .getContent();

        chuongTrinhList.forEach(item -> xoaTheoChuongTrinhId(item.getId()));
    }

    @Override
    @Transactional
    public void xoaTheoChuongTrinhId(Long chuongTrinhId) {
        List<ChuongTrinhVersion> versionList = chuongTrinhVersionRepository
                .findByChuongTrinhId(chuongTrinhId, Pageable.unpaged())
                .getContent();

        versionList.forEach(item -> xoaTheoChuongTrinhVersionId(item.getId()));

        chuongTrinhRepository.deleteById(chuongTrinhId);
    }

    @Override
    @Transactional
    public void xoaTheoChuongTrinhVersionId(Long chuongTrinhVersionId) {
        List<ChuongTrinhMon> monList = chuongTrinhMonRepository
                .findByChuongTrinhVersionId(chuongTrinhVersionId, Pageable.unpaged())
                .getContent();

        monList.forEach(item -> xoaTheoChuongTrinhMonId(item.getId()));

        List<KhungKy> khungKyList = khungKyRepository
                .findByChuongTrinhVersionId(chuongTrinhVersionId, Pageable.unpaged())
                .getContent();

        khungKyList.forEach(item -> khungKyRepository.deleteById(item.getId()));

        nhomTuChonRepository
                .findByChuongTrinhVersionId(chuongTrinhVersionId, Pageable.unpaged())
                .getContent()
                .forEach(item -> xoaTheoNhomTuChonId(item.getId()));

        lopHanhChinhRepository.deleteByChuongTrinhVersionId(chuongTrinhVersionId);

        //Mới
        chuongTrinhVersionMucTieuRepository.deleteByChuongTrinhVersionId(chuongTrinhVersionId);
        chuongTrinhVersionNangLucRepository.deleteByChuongTrinhVersionId(chuongTrinhVersionId);
        chuongTrinhVersionViTriViecLamRepository.deleteByChuongTrinhVersionId(chuongTrinhVersionId);
        chuongTrinhVersionDieuKienTotNghiepRepository.deleteByChuongTrinhVersionId(chuongTrinhVersionId);

        syllabusChuongTrinhRepository
                .findByChuongTrinhVersionId(chuongTrinhVersionId, Pageable.unpaged())
                .getContent()
                .forEach(syllabus -> {
                    mucTieuChuongTrinhRepository.deleteBySyllabusChuongTrinhId(syllabus.getId());
                    nangLucDauRaRepository.deleteBySyllabusChuongTrinhId(syllabus.getId());
                    viTriViecLamRepository.deleteBySyllabusChuongTrinhId(syllabus.getId());
                    dieuKienTotNghiepRepository.deleteBySyllabusChuongTrinhId(syllabus.getId());
                });
        syllabusChuongTrinhRepository.deleteByChuongTrinhVersionId(chuongTrinhVersionId);
        nhomKienThucRepository.deleteByChuongTrinhVersionId(chuongTrinhVersionId);

        chuongTrinhVersionRepository.deleteById(chuongTrinhVersionId);
    }

    @Override
    @Transactional
    public void xoaTheoChuongTrinhMonId(Long chuongTrinhMonId) {
        List<SyllabusMonHoc> syllabusList = syllabusMonHocRepository
                .findByChuongTrinhMonId(chuongTrinhMonId, Pageable.unpaged())
                .getContent();

        syllabusList.forEach(item -> xoaTheoSyllabusMonHocId(item.getId()));

        monTuChonRepository.deleteByChuongTrinhMonId(chuongTrinhMonId);
        monTienQuyetRepository.deleteByMonIdOrMonDieuKienId(chuongTrinhMonId, chuongTrinhMonId);
        lopHocPhanRepository.deleteByChuongTrinhMonId(chuongTrinhMonId);
        chuongTrinhMonRepository.deleteById(chuongTrinhMonId);
    }

    @Override
    @Transactional
    public void xoaTheoSyllabusMonHocId(Long syllabusMonHocId) {
//        dieuKienMonHocRepository.deleteBySyllabusMonId(syllabusMonHocId);
//        syllabusChuongBaiRepository.deleteBySyllabusMonId(syllabusMonHocId);
//        syllabusTaiLieuRepository.deleteBySyllabusMonId(syllabusMonHocId);
//
//        syllabusMonHocRepository.deleteById(syllabusMonHocId);

        syllabusMonHocDieuKienRepository.deleteBySyllabusMonId(syllabusMonHocId);
        syllabusMonHocTaiLieuRepository.deleteBySyllabusMonId(syllabusMonHocId);

        dieuKienMonHocRepository.deleteBySyllabusMonId(syllabusMonHocId);
        syllabusChuongBaiRepository.deleteBySyllabusMonId(syllabusMonHocId);
        syllabusTaiLieuRepository.deleteBySyllabusMonId(syllabusMonHocId);

        cauHinhDanhGiaRepository.deleteBySyllabusMonHocId(syllabusMonHocId);
        quyDoiDiemRepository.deleteBySyllabusMonHocId(syllabusMonHocId);

        syllabusMonHocRepository.deleteById(syllabusMonHocId);
    }

    @Override
    @Transactional
    public void xoaTheoKhungKyId(Long khungKyId) {
        List<ChuongTrinhMon> monList = chuongTrinhMonRepository
                .findByKhungKyId(khungKyId, Pageable.unpaged())
                .getContent();

        monList.forEach(item -> xoaTheoChuongTrinhMonId(item.getId()));

        khungKyRepository.deleteById(khungKyId);
    }

    @Override
    @Transactional
    public void xoaTheoNhomTuChonId(Long nhomTuChonId) {
        monTuChonRepository.deleteByNhomId(nhomTuChonId);
        nhomTuChonRepository.deleteById(nhomTuChonId);
    }

    //
    @Override
    @Transactional
    public void xoaTheoSyllabusMonHocMauId(Long syllabusMonHocMauId) {
        syllabusMonHocRepository
                .findBySyllabusMonHocMauId(syllabusMonHocMauId, Pageable.unpaged())
                .getContent()
                .forEach(item -> xoaTheoSyllabusMonHocId(item.getId()));

        syllabusMonHocMauChuongBaiRepository.deleteBySyllabusMonHocMauId(syllabusMonHocMauId);
        syllabusMonHocMauDieuKienRepository.deleteBySyllabusMonHocMauId(syllabusMonHocMauId);
        syllabusMonHocMauTaiLieuRepository.deleteBySyllabusMonHocMauId(syllabusMonHocMauId);
    }



    @Override
    @Transactional
    public void xoaTheoNhomKienThucMauId(Long nhomKienThucMauId) {
        nhomKienThucRepository
                .findByNhomKienThucMauId(nhomKienThucMauId, Pageable.unpaged())
                .getContent()
                .forEach(nhom -> {
                    chuongTrinhMonRepository
                            .findByNhomKienThucId(nhom.getId(), Pageable.unpaged())
                            .getContent()
                            .forEach(mon -> xoaTheoChuongTrinhMonId(mon.getId()));

                    nhomKienThucRepository.deleteById(nhom.getId());
                });
    }

    @Override
    @Transactional
    public void xoaTheoNhomTuChonMauId(Long nhomTuChonMauId) {
        nhomTuChonRepository
                .findByNhomTuChonMauId(nhomTuChonMauId, Pageable.unpaged())
                .getContent()
                .forEach(item -> xoaTheoNhomTuChonId(item.getId()));
    }

    @Override
    @Transactional
    public void xoaTheoMucTieuMauId(Long mucTieuMauId) {
        chuongTrinhVersionMucTieuRepository.deleteByMucTieuMauId(mucTieuMauId);
    }

    @Override
    @Transactional
    public void xoaTheoNangLucMauId(Long nangLucMauId) {
        chuongTrinhVersionNangLucRepository.deleteByNangLucMauId(nangLucMauId);
    }

    @Override
    @Transactional
    public void xoaTheoViTriMauId(Long viTriMauId) {
        chuongTrinhVersionViTriViecLamRepository.deleteByViTriMauId(viTriMauId);
    }

    @Override
    @Transactional
    public void xoaTheoDieuKienTotNghiepMauId(Long dieuKienMauId) {
        chuongTrinhVersionDieuKienTotNghiepRepository.deleteByDieuKienMauId(dieuKienMauId);
    }

    @Override
    @Transactional
    public void xoaTheoQuyDoiDiemMauId(Long quyDoiDiemMauId) {
        chuongTrinhMonQuyDoiDiemMauRepository.deleteByQuyDoiDiemMauId(quyDoiDiemMauId);
    }

    @Override
    @Transactional
    public void xoaTheoDieuKienMonHocMauId(Long dieuKienMauId) {
        syllabusMonHocDieuKienRepository.deleteByDieuKienMauId(dieuKienMauId);
        syllabusMonHocMauDieuKienRepository.deleteByDieuKienMauId(dieuKienMauId);
    }

    @Override
    @Transactional
    public void xoaTheoTaiLieuMauId(Long taiLieuMauId) {
        syllabusMonHocTaiLieuRepository.deleteByTaiLieuMauId(taiLieuMauId);
        syllabusMonHocMauTaiLieuRepository.deleteByTaiLieuMauId(taiLieuMauId);
    }
}