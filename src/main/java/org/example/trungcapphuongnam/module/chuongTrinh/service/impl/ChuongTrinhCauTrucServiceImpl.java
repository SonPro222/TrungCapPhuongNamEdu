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
import org.example.trungcapphuongnam.module.diem.mapper.CauHinhDanhGiaMapper;
import org.example.trungcapphuongnam.module.diem.repository.CauHinhDanhGiaRepository;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.KhungKy;
import org.example.trungcapphuongnam.module.daoTao.mapper.KhungKyMapper;
import org.example.trungcapphuongnam.module.daoTao.repository.KhungKyRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiemMau;

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
    private final ChuongTrinhMonQuyDoiDiemMauRepository chuongTrinhMonQuyDoiDiemMauRepository;
    private final SyllabusMonHocRepository syllabusMonHocRepository;
    private final CauHinhDanhGiaRepository cauHinhDanhGiaRepository;
    private final DieuKienMonHocRepository dieuKienMonHocRepository;
    private final SyllabusChuongBaiRepository syllabusChuongBaiRepository;
    private final SyllabusTaiLieuRepository syllabusTaiLieuRepository;
    private final SyllabusMonHocFileRepository syllabusMonHocFileRepository;
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
    private final ChuongTrinhMonQuyDoiDiemMauMapper chuongTrinhMonQuyDoiDiemMauMapper;
    private final SyllabusMonHocMapper syllabusMonHocMapper;
    private final CauHinhDanhGiaMapper cauHinhDanhGiaMapper;
    private final DieuKienMonHocMapper dieuKienMonHocMapper;
    private final SyllabusChuongBaiMapper syllabusChuongBaiMapper;
    private final SyllabusTaiLieuMapper syllabusTaiLieuMapper;
    private final QuyDoiDiemMauRepository quyDoiDiemMauRepository;
    @Override
    public ChuongTrinhCauTrucResponse findCauTrucByVersionId(Long chuongTrinhVersionId) {
        return findCauTrucByVersionId(chuongTrinhVersionId, null, true);
    }

    @Override
    public ChuongTrinhCauTrucResponse findCauTrucByVersionId(
            Long chuongTrinhVersionId,
            Long khungKyId,
            boolean includeSyllabusDetail
    ) {
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

        List<ChuongTrinhMon> monEntities = khungKyId == null
                ? chuongTrinhMonRepository
                .findByChuongTrinhVersionId(chuongTrinhVersionId, pageable)
                .getContent()
                : chuongTrinhMonRepository
                .findByChuongTrinhVersionIdAndKhungKyId(chuongTrinhVersionId, khungKyId, pageable)
                .getContent();

        List<ChuongTrinhMonCauTrucResponse> monTrongChuongTrinh = monEntities.stream()
                .map(mon -> buildMonTrongChuongTrinh(mon, includeSyllabusDetail))
                .toList();

        List<KhungKy> khungKyEntities = khungKyRepository
                .findByChuongTrinhVersionId(chuongTrinhVersionId, pageable)
                .getContent();

        if (khungKyId != null) {
            khungKyEntities = khungKyEntities.stream()
                    .filter(ky -> ky.getId() != null && ky.getId().equals(khungKyId))
                    .toList();
        }

        List<KhungKyCauTrucResponse> khungKy = khungKyEntities.stream()
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

        List<SyllabusChuongTrinhResponse> syllabusChuongTrinh = syllabusChuongTrinhRepository
                .findByChuongTrinhVersionId(chuongTrinhVersionId, pageable)
                .map(syllabusChuongTrinhMapper::toResponse)
                .getContent();

        Long syllabusChuongTrinhId = syllabusChuongTrinh.isEmpty() ? null : syllabusChuongTrinh.get(0).getId();

        return ChuongTrinhCauTrucResponse.builder()
                .chuongTrinh(chuongTrinhMapper.toResponse(chuongTrinh))
                .version(chuongTrinhVersionMapper.toResponse(version))
                .mucTieuChuongTrinh(
                        syllabusChuongTrinhId == null
                                ? List.of()
                                : mucTieuChuongTrinhRepository
                                .findBySyllabusChuongTrinhId(syllabusChuongTrinhId, pageable)
                                .map(mucTieuChuongTrinhMapper::toResponse)
                                .getContent()
                )
                .nangLucDauRa(
                        syllabusChuongTrinhId == null
                                ? List.of()
                                : nangLucDauRaRepository
                                .findBySyllabusChuongTrinhId(syllabusChuongTrinhId, pageable)
                                .map(nangLucDauRaMapper::toResponse)
                                .getContent()
                )
                .viTriViecLam(
                        syllabusChuongTrinhId == null
                                ? List.of()
                                : viTriViecLamRepository
                                .findBySyllabusChuongTrinhId(syllabusChuongTrinhId, pageable)
                                .map(viTriViecLamMapper::toResponse)
                                .getContent()
                )
                .dieuKienTotNghiep(
                        syllabusChuongTrinhId == null
                                ? List.of()
                                : dieuKienTotNghiepRepository
                                .findBySyllabusChuongTrinhId(syllabusChuongTrinhId, pageable)
                                .map(dieuKienTotNghiepMapper::toResponse)
                                .getContent()
                )
                .syllabusChuongTrinh(syllabusChuongTrinh)
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
        return buildMonTrongChuongTrinh(entity, true);
    }

    private ChuongTrinhMonCauTrucResponse buildMonTrongChuongTrinh(ChuongTrinhMon entity, boolean includeSyllabusDetail) {
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

        List<MonTienQuyetResponse> monTienQuyet = includeSyllabusDetail
                ? monTienQuyetRepository
                .findByMonId(entity.getId(), pageable)
                .map(monTienQuyetMapper::toResponse)
                .getContent()
                : List.of();

        List<QuyDoiDiemResponse> quyDoiDiem = includeSyllabusDetail
                ? quyDoiDiemRepository
                .findByChuongTrinhMonId(entity.getId(), pageable)
                .map(quyDoiDiemMapper::toResponse)
                .getContent()
                : List.of();

        List<SyllabusMonHocCauTrucResponse> syllabusMonHoc = syllabusMonHocRepository
                .findByChuongTrinhMonId(entity.getId(), pageable)
                .getContent()
                .stream()
                .map(syllabus -> includeSyllabusDetail
                        ? buildSyllabusMonHoc(syllabus)
                        : buildSyllabusMonHocTomTat(syllabus)
                )
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


    @Override
    public SyllabusMonHocCauTrucResponse findSyllabusChiTietById(Long syllabusMonHocId) {
        SyllabusMonHoc entity = syllabusMonHocRepository.findById(syllabusMonHocId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Syllabus môn học không tồn tại: " + syllabusMonHocId
                ));

        return buildSyllabusMonHoc(entity);
    }

    private SyllabusMonHocCauTrucResponse buildSyllabusMonHocTomTat(SyllabusMonHoc entity) {
        return SyllabusMonHocCauTrucResponse.builder()
                .syllabusMonHoc(syllabusMonHocMapper.toResponse(entity))
                .dieuKienMonHoc(List.of())
                .chuongBai(List.of())
                .taiLieu(List.of())
                .cauHinhDanhGia(List.of())
                .quyDoiDiem(List.of())
                .quyDoiDiemTheoChuongTrinh(List.of())
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
                        syllabusChuongBaiRepository.findBySyllabusMonIdOrderByThuTuAsc(entity.getId())
                                .stream()
                                .map(syllabusChuongBaiMapper::toResponse)
                                .toList()
                )
                .taiLieu(
                        syllabusTaiLieuRepository.findBySyllabusMonId(entity.getId(), pageable)
                                .map(syllabusTaiLieuMapper::toResponse)
                                .getContent()
                )
                .cauHinhDanhGia(
                        cauHinhDanhGiaRepository.findBySyllabusMonHocIdOrderByThuTuAscIdAsc(entity.getId())
                                .stream()
                                .map(cauHinhDanhGiaMapper::toResponse)
                                .toList()
                )
                .quyDoiDiem(
                        quyDoiDiemRepository.findBySyllabusMonHocIdOrderByThuTuAscIdAsc(entity.getId())
                                .stream()
                                .map(quyDoiDiemMapper::toResponse)
                                .toList()
                )
                .quyDoiDiemTheoChuongTrinh(buildQuyDoiDiemTheoChuongTrinh(entity.getId()))
                .fileSyllabus(
                        syllabusMonHocFileRepository.findBySyllabusMonHocIdOrderByCreatedAtDesc(entity.getId())
                                .stream()
                                .map(f -> SyllabusMonHocFileResponse.builder()
                                        .id(f.getId())
                                        .syllabusMonHocId(f.getSyllabusMonHocId())
                                        .tenFile(f.getTenFile())
                                        .loaiFile(f.getLoaiFile())
                                        .kichThuoc(f.getKichThuoc())
                                        .loaiTaiLieu(f.getLoaiTaiLieu())
                                        .laFileNguon(f.getLaFileNguon())
                                        .trangThaiDoc(f.getTrangThaiDoc())
                                        .loiDoc(f.getLoiDoc())
                                        .checksum(f.getChecksum())
                                        .ghiChu(f.getGhiChu())
                                        .createdAt(f.getCreatedAt())
                                        .updatedAt(f.getUpdatedAt())
                                        .build())
                                .toList()
                )
                .build();
    }

    private List<ChuongTrinhMonQuyDoiDiemMauResponse> buildQuyDoiDiemTheoChuongTrinh(Long syllabusMonHocId) {
        if (syllabusMonHocId == null) {
            return List.of();
        }

        return chuongTrinhMonQuyDoiDiemMauRepository.findBySyllabusMonHocId(syllabusMonHocId)
                .stream()
                .map(row -> {
                    QuyDoiDiemMau Mau = row.getQuyDoiDiemMauId() == null
                            ? null
                            : quyDoiDiemMauRepository.findById(row.getQuyDoiDiemMauId()).orElse(null);

                    return ChuongTrinhMonQuyDoiDiemMauResponse.builder()
                            .id(row.getId())
                            .chuongTrinhMonId(row.getChuongTrinhMonId())
                            .syllabusMonHocId(row.getSyllabusMonHocId())
                            .quyDoiDiemMauId(row.getQuyDoiDiemMauId())
                            .ghiChu(row.getGhiChu())
                            .ma(Mau != null ? Mau.getMa() : null)
                            .ten(Mau != null ? Mau.getTen() : null)
                            .nguongTu(Mau != null ? Mau.getNguongTu() : null)
                            .nguongDen(Mau != null ? Mau.getNguongDen() : null)
                            .diemQuyDoi(Mau != null ? Mau.getDiemQuyDoi() : null)
                            .ketQua(Mau != null && Mau.getKetQua() != null ? Mau.getKetQua().getValue() : null)
                            .congThuc(Mau != null ? Mau.getCongThuc() : null)
                            .loaiMau(Mau != null ? Mau.getLoaiMau() : null)
                            .tyLe(Mau != null ? Mau.getTyLe() : null)
                            .diemToiDa(Mau != null ? Mau.getDiemToiDa() : null)
                            .thuTu(Mau != null ? Mau.getThuTu() : null)
                            .batBuoc(Mau != null ? Mau.getBatBuoc() : null)
                            .build();
                })
                .toList();
    }
}