package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.*;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.cauTruc.*;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersion;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHoc;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.*;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhCauTrucService;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.KhungKy;
import org.example.trungcapphuongnam.module.daoTao.mapper.KhungKyMapper;
import org.example.trungcapphuongnam.module.daoTao.repository.KhungKyRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChuongTrinhCauTrucServiceImpl implements ChuongTrinhCauTrucService {

    private final ChuongTrinhRepository chuongTrinhRepository;
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final MucTieuChuongTrinhRepository mucTieuChuongTrinhRepository;
    private final NangLucDauRaRepository nangLucDauRaRepository;
    private final ViTriViecLamRepository viTriViecLamRepository;
    private final DieuKienTotNghiepRepository dieuKienTotNghiepRepository;
    private final SyllabusChuongTrinhRepository syllabusChuongTrinhRepository;
    private final NhomKienThucRepository nhomKienThucRepository;
    private final NhomTuChonRepository nhomTuChonRepository;
    private final MonTuChonRepository monTuChonRepository;
    private final KhungKyRepository khungKyRepository;
    private final ChuongTrinhMonRepository chuongTrinhMonRepository;
    private final MonHocRepository monHocRepository;
    private final MonTienQuyetRepository monTienQuyetRepository;
    private final QuyDoiDiemRepository quyDoiDiemRepository;
    private final SyllabusMonHocRepository syllabusMonHocRepository;
    private final DieuKienMonHocRepository dieuKienMonHocRepository;
    private final SyllabusChuongBaiRepository syllabusChuongBaiRepository;
    private final SyllabusTaiLieuRepository syllabusTaiLieuRepository;
    private final ChuongTrinhMapper chuongTrinhMapper;
    private final ChuongTrinhVersionMapper chuongTrinhVersionMapper;
    private final MucTieuChuongTrinhMapper mucTieuChuongTrinhMapper;
    private final NangLucDauRaMapper nangLucDauRaMapper;
    private final ViTriViecLamMapper viTriViecLamMapper;
    private final DieuKienTotNghiepMapper dieuKienTotNghiepMapper;
    private final SyllabusChuongTrinhMapper syllabusChuongTrinhMapper;
    private final NhomKienThucMapper nhomKienThucMapper;
    private final NhomTuChonMapper nhomTuChonMapper;
    private final MonTuChonMapper monTuChonMapper;
    private final KhungKyMapper khungKyMapper;
    private final ChuongTrinhMonMapper chuongTrinhMonMapper;
    private final MonHocMapper monHocMapper;
    private final MonTienQuyetMapper monTienQuyetMapper;
    private final QuyDoiDiemMapper quyDoiDiemMapper;
    private final SyllabusMonHocMapper syllabusMonHocMapper;
    private final DieuKienMonHocMapper dieuKienMonHocMapper;
    private final SyllabusChuongBaiMapper syllabusChuongBaiMapper;
    private final SyllabusTaiLieuMapper syllabusTaiLieuMapper;

    @Override
    public ChuongTrinhCauTrucResponse findCauTrucByVersionId(Long chuongTrinhVersionId) {
        Pageable pageable = Pageable.unpaged();

        ChuongTrinhVersion version = chuongTrinhVersionRepository.findById(chuongTrinhVersionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Version chương trình không tồn tại: " + chuongTrinhVersionId
                ));

        ChuongTrinh chuongTrinh = chuongTrinhRepository.findById(version.getChuongTrinhId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Chương trình không tồn tại: " + version.getChuongTrinhId()
                ));

        List<NhomKienThucResponse> nhomKienThuc = nhomKienThucRepository
                .findByChuongTrinhVersionId(chuongTrinhVersionId, pageable)
                .map(nhomKienThucMapper::toResponse)
                .getContent();

        List<ChuongTrinhMon> monEntities = chuongTrinhMonRepository
                .findByChuongTrinhVersionId(chuongTrinhVersionId, pageable)
                .getContent();

        List<ChuongTrinhMonCauTrucResponse> monTrongChuongTrinh = monEntities.stream()
                .map(this::buildMonTrongChuongTrinh)
                .toList();

        List<KhungKyCauTrucResponse> khungKy = khungKyRepository
                .findByChuongTrinhVersionId(chuongTrinhVersionId, pageable)
                .getContent()
                .stream()
                .map(ky -> buildKhungKy(ky, monTrongChuongTrinh))
                .toList();

        List<NhomTuChonCauTrucResponse> nhomTuChon = nhomTuChonRepository
                .findByChuongTrinhVersionId(chuongTrinhVersionId, pageable)
                .getContent()
                .stream()
                .map(nhom -> NhomTuChonCauTrucResponse.builder()
                        .nhomTuChon(nhomTuChonMapper.toResponse(nhom))
                        .monTuChon(
                                monTuChonRepository.findByNhomId(nhom.getId(), pageable)
                                        .getContent()
                                        .stream()
                                        .map(monTuChon -> MonTuChonCauTrucResponse.builder()
                                                .monTuChon(monTuChonMapper.toResponse(monTuChon))
                                                .monTrongChuongTrinh(
                                                        monTrongChuongTrinh.stream()
                                                                .filter(mon -> mon.getChuongTrinhMon() != null)
                                                                .filter(mon -> mon.getChuongTrinhMon().getId() != null)
                                                                .filter(mon -> mon.getChuongTrinhMon().getId()
                                                                        .equals(monTuChon.getChuongTrinhMonId()))
                                                                .findFirst()
                                                                .orElse(null)
                                                )
                                                .build()
                                        )
                                        .toList()
                        )
                        .build()
                )
                .toList();

        return ChuongTrinhCauTrucResponse.builder()
                .chuongTrinh(chuongTrinhMapper.toResponse(chuongTrinh))
                .version(chuongTrinhVersionMapper.toResponse(version))
                .mucTieuChuongTrinh(
                        mucTieuChuongTrinhRepository
                                .findByChuongTrinhVersionId(chuongTrinhVersionId, pageable)
                                .map(mucTieuChuongTrinhMapper::toResponse)
                                .getContent()
                )
                .nangLucDauRa(
                        nangLucDauRaRepository
                                .findByChuongTrinhVersionId(chuongTrinhVersionId, pageable)
                                .map(nangLucDauRaMapper::toResponse)
                                .getContent()
                )
                .viTriViecLam(
                        viTriViecLamRepository
                                .findByChuongTrinhVersionId(chuongTrinhVersionId, pageable)
                                .map(viTriViecLamMapper::toResponse)
                                .getContent()
                )
                .dieuKienTotNghiep(
                        dieuKienTotNghiepRepository
                                .findByChuongTrinhVersionId(chuongTrinhVersionId, pageable)
                                .map(dieuKienTotNghiepMapper::toResponse)
                                .getContent()
                )
                .syllabusChuongTrinh(
                        syllabusChuongTrinhRepository
                                .findByChuongTrinhVersionId(chuongTrinhVersionId, pageable)
                                .map(syllabusChuongTrinhMapper::toResponse)
                                .getContent()
                )
                .nhomKienThuc(nhomKienThuc)
                .nhomTuChon(nhomTuChon)
                .khungKy(khungKy)
                .monTrongChuongTrinh(monTrongChuongTrinh)
                .build();
    }

    private KhungKyCauTrucResponse buildKhungKy(
            KhungKy khungKy,
            List<ChuongTrinhMonCauTrucResponse> monTrongChuongTrinh
    ) {
        KhungKyResponse khungKyResponse = khungKyMapper.toResponse(khungKy);

        List<ChuongTrinhMonCauTrucResponse> monTrongKy = monTrongChuongTrinh.stream()
                .filter(mon -> mon.getChuongTrinhMon() != null)
                .filter(mon -> mon.getChuongTrinhMon().getKhungKyId() != null)
                .filter(mon -> mon.getChuongTrinhMon().getKhungKyId().equals(khungKy.getId()))
                .toList();

        return KhungKyCauTrucResponse.builder()
                .khungKy(khungKyResponse)
                .monTrongKy(monTrongKy)
                .build();
    }

    private ChuongTrinhMonCauTrucResponse buildMonTrongChuongTrinh(ChuongTrinhMon entity) {
        Pageable pageable = Pageable.unpaged();

        ChuongTrinhMonResponse chuongTrinhMon = chuongTrinhMonMapper.toResponse(entity);

        MonHocResponse monHoc = entity.getMonHocId() == null
                ? null
                : monHocRepository.findById(entity.getMonHocId())
                .map(monHocMapper::toResponse)
                .orElse(null);

        NhomKienThucResponse nhomKienThuc = entity.getNhomKienThucId() == null
                ? null
                : nhomKienThucRepository.findById(entity.getNhomKienThucId())
                .map(nhomKienThucMapper::toResponse)
                .orElse(null);

        List<MonTienQuyetResponse> monTienQuyet = monTienQuyetRepository
                .findByMonId(entity.getId(), pageable)
                .map(monTienQuyetMapper::toResponse)
                .getContent();

        List<QuyDoiDiemResponse> quyDoiDiem = quyDoiDiemRepository
                .findByChuongTrinhMonId(entity.getId(), pageable)
                .map(quyDoiDiemMapper::toResponse)
                .getContent();

        List<SyllabusMonHocCauTrucResponse> syllabusMonHoc = syllabusMonHocRepository
                .findByChuongTrinhMonId(entity.getId(), pageable)
                .getContent()
                .stream()
                .map(this::buildSyllabusMonHoc)
                .toList();

        return ChuongTrinhMonCauTrucResponse.builder()
                .chuongTrinhMon(chuongTrinhMon)
                .monHoc(monHoc)
                .nhomKienThuc(nhomKienThuc)
                .monTienQuyet(monTienQuyet)
                .quyDoiDiem(quyDoiDiem)
                .syllabusMonHoc(syllabusMonHoc)
                .build();
    }

    private SyllabusMonHocCauTrucResponse buildSyllabusMonHoc(SyllabusMonHoc entity) {
        Pageable pageable = Pageable.unpaged();

        return SyllabusMonHocCauTrucResponse.builder()
                .syllabusMonHoc(syllabusMonHocMapper.toResponse(entity))
                .dieuKienMonHoc(
                        dieuKienMonHocRepository.findBySyllabusMonId(entity.getId(), pageable)
                                .map(dieuKienMonHocMapper::toResponse)
                                .getContent()
                )
                .chuongBai(
                        syllabusChuongBaiRepository.findBySyllabusMonId(entity.getId(), pageable)
                                .map(syllabusChuongBaiMapper::toResponse)
                                .getContent()
                )
                .taiLieu(
                        syllabusTaiLieuRepository.findBySyllabusMonId(entity.getId(), pageable)
                                .map(syllabusTaiLieuMapper::toResponse)
                                .getContent()
                )
                .build();
    }
}