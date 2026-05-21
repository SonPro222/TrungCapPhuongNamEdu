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
    private final SyllabusMonHocRepository syllabusMonHocRepository;
    private final DieuKienMonHocRepository dieuKienMonHocRepository;
    private final SyllabusChuongBaiRepository syllabusChuongBaiRepository;
    private final SyllabusTaiLieuRepository syllabusTaiLieuRepository;

    private final KhungKyRepository khungKyRepository;
    private final LopHanhChinhRepository lopHanhChinhRepository;

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

        mucTieuChuongTrinhRepository.deleteByChuongTrinhVersionId(chuongTrinhVersionId);
        nangLucDauRaRepository.deleteByChuongTrinhVersionId(chuongTrinhVersionId);
        viTriViecLamRepository.deleteByChuongTrinhVersionId(chuongTrinhVersionId);
        dieuKienTotNghiepRepository.deleteByChuongTrinhVersionId(chuongTrinhVersionId);
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
        quyDoiDiemRepository.deleteByChuongTrinhMonId(chuongTrinhMonId);
        lopHocPhanRepository.deleteByChuongTrinhMonId(chuongTrinhMonId);
        chuongTrinhMonRepository.deleteById(chuongTrinhMonId);
    }

    @Override
    @Transactional
    public void xoaTheoSyllabusMonHocId(Long syllabusMonHocId) {
        dieuKienMonHocRepository.deleteBySyllabusMonId(syllabusMonHocId);
        syllabusChuongBaiRepository.deleteBySyllabusMonId(syllabusMonHocId);
        syllabusTaiLieuRepository.deleteBySyllabusMonId(syllabusMonHocId);

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
}