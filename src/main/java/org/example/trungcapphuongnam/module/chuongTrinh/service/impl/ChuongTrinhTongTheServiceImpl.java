package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.cauTruc.ChuongTrinhCauTrucResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.tongThe.ChuongTrinhTongTheResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersion;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ChuongTrinhMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ChuongTrinhVersionMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhCauTrucService;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhTongTheService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChuongTrinhTongTheServiceImpl implements ChuongTrinhTongTheService {

    private static final String MESSAGE_KHONG_CO_VERSION =
            "Chương trình đào tạo đã tồn tại nhưng chưa có version để xem tổng thể.";

    private static final String LY_DO_VERSION_TRUYEN_VAO =
            "Sử dụng versionId được truyền từ request.";

    private static final String LY_DO_VERSION_HIEN_HANH =
            "Tự động chọn version hiện hành của chương trình.";

    private static final String LY_DO_VERSION_MOI_NHAT =
            "Không có version hiện hành, tự động chọn version mới nhất theo ngày áp dụng/cập nhật/tạo.";

    private final ChuongTrinhRepository chuongTrinhRepository;
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final SyllabusChuongTrinhRepository syllabusChuongTrinhRepository;
    private final ChuongTrinhMapper chuongTrinhMapper;
    private final ChuongTrinhVersionMapper chuongTrinhVersionMapper;
    private final ChuongTrinhCauTrucService chuongTrinhCauTrucService;

    @Override
    public ChuongTrinhTongTheResponse findTongTheByChuongTrinhId(Long chuongTrinhId, Long versionId) {
        return findTongTheByChuongTrinhId(chuongTrinhId, versionId, null, false);
    }

    @Override
    public ChuongTrinhTongTheResponse findTongTheByChuongTrinhId(
            Long chuongTrinhId,
            Long versionId,
            Long khungKyId,
            Boolean includeSyllabusDetail
    ) {
        ChuongTrinh chuongTrinh = chuongTrinhRepository.findById(chuongTrinhId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Chương trình đào tạo không tồn tại: " + chuongTrinhId
                ));

        List<ChuongTrinhVersion> danhSachVersionEntity = chuongTrinhVersionRepository
                .findByChuongTrinhId(chuongTrinhId, Pageable.unpaged())
                .getContent();

        List<ChuongTrinhVersionResponse> danhSachVersion = danhSachVersionEntity.stream()
                .map(chuongTrinhVersionMapper::toResponse)
                .toList();

        ChuongTrinhResponse chuongTrinhResponse = chuongTrinhMapper.toResponse(chuongTrinh);

        if (danhSachVersionEntity.isEmpty()) {
            return ChuongTrinhTongTheResponse.builder()
                    .chuongTrinh(chuongTrinhResponse)
                    .versionDangXem(null)
                    .cauTruc(null)
                    .danhSachVersion(danhSachVersion)
                    .coVersion(false)
                    .chuongTrinhId(chuongTrinh.getId())
                    .versionDangXemId(null)
                    .syllabusChuongTrinhId(null)
                    .syllabusChuongTrinhGocId(null)
                    .tongSoKy(0)
                    .tongSoMon(0)
                    .tongSoTinChi(null)
                    .tongSoGio(null)
                    .lyDoChonVersion(null)
                    .message(MESSAGE_KHONG_CO_VERSION)
                    .build();
        }

        VersionDaChon versionDaChon = versionId == null
                ? chonVersionMacDinh(danhSachVersionEntity)
                : layVersionTheoRequest(chuongTrinhId, versionId);

        ChuongTrinhVersion version = versionDaChon.version();
        ChuongTrinhVersionResponse versionResponse = chuongTrinhVersionMapper.toResponse(version);

        ChuongTrinhCauTrucResponse cauTruc = chuongTrinhCauTrucService.findCauTrucByVersionId(
                version.getId(),
                khungKyId,
                Boolean.TRUE.equals(includeSyllabusDetail)
        );

        SyllabusChuongTrinh syllabusChuongTrinh = laySyllabusChuongTrinhTheoVersion(version.getId());

        return ChuongTrinhTongTheResponse.builder()
                .chuongTrinh(chuongTrinhResponse)
                .versionDangXem(versionResponse)
                .cauTruc(cauTruc)
                .danhSachVersion(danhSachVersion)
                .coVersion(true)
                .chuongTrinhId(chuongTrinh.getId())
                .versionDangXemId(version.getId())
                .syllabusChuongTrinhId(laySyllabusChuongTrinhId(syllabusChuongTrinh))
                .syllabusChuongTrinhGocId(laySyllabusChuongTrinhGocId(syllabusChuongTrinh))
                .tongSoKy(demTongSoKy(cauTruc))
                .tongSoMon(demTongSoMon(cauTruc))
                .tongSoTinChi(layTongSoTinChi(versionResponse, version))
                .tongSoGio(layTongSoGio(versionResponse, version))
                .lyDoChonVersion(versionDaChon.lyDo())
                .message("Lấy tổng thể chương trình đào tạo thành công.")
                .build();
    }

    private SyllabusChuongTrinh laySyllabusChuongTrinhTheoVersion(Long versionId) {
        if (versionId == null) return null;

        return syllabusChuongTrinhRepository
                .findFirstByChuongTrinhVersionIdOrderByIdDesc(versionId)
                .orElse(null);
    }

    private Long laySyllabusChuongTrinhId(SyllabusChuongTrinh syllabusChuongTrinh) {
        return syllabusChuongTrinh == null ? null : syllabusChuongTrinh.getId();
    }

    private Long laySyllabusChuongTrinhGocId(SyllabusChuongTrinh syllabusChuongTrinh) {
        return syllabusChuongTrinh == null ? null : syllabusChuongTrinh.getSyllabusChuongTrinhMauId();
    }

    private VersionDaChon layVersionTheoRequest(Long chuongTrinhId, Long versionId) {
        ChuongTrinhVersion version = chuongTrinhVersionRepository.findById(versionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Version chương trình không tồn tại: " + versionId
                ));

        if (!String.valueOf(chuongTrinhId).equals(String.valueOf(version.getChuongTrinhId()))) {
            throw new BadRequestException(
                    "Version " + versionId + " không thuộc chương trình đào tạo " + chuongTrinhId + "."
            );
        }

        return new VersionDaChon(version, LY_DO_VERSION_TRUYEN_VAO);
    }

    private VersionDaChon chonVersionMacDinh(List<ChuongTrinhVersion> danhSachVersion) {
        ChuongTrinhVersion versionHienHanh = danhSachVersion.stream()
                .filter(version -> Boolean.TRUE.equals(version.getLaHienHanh()))
                .max(this::soSanhDoUuTienVersion)
                .orElse(null);

        if (versionHienHanh != null) {
            return new VersionDaChon(versionHienHanh, LY_DO_VERSION_HIEN_HANH);
        }

        ChuongTrinhVersion versionMoiNhat = danhSachVersion.stream()
                .max(this::soSanhDoUuTienVersion)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Chương trình đào tạo chưa có version để xem tổng thể."
                ));

        return new VersionDaChon(versionMoiNhat, LY_DO_VERSION_MOI_NHAT);
    }

    private int soSanhDoUuTienVersion(ChuongTrinhVersion a, ChuongTrinhVersion b) {
        int byNgayApDung = soSanhLocalDate(a.getNgayApDung(), b.getNgayApDung());
        if (byNgayApDung != 0) return byNgayApDung;

        int byUpdatedAt = soSanhLocalDateTime(a.getUpdatedAt(), b.getUpdatedAt());
        if (byUpdatedAt != 0) return byUpdatedAt;

        int byCreatedAt = soSanhLocalDateTime(a.getCreatedAt(), b.getCreatedAt());
        if (byCreatedAt != 0) return byCreatedAt;

        return soSanhLong(a.getId(), b.getId());
    }

    private int soSanhLocalDate(LocalDate a, LocalDate b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareTo(b);
    }

    private int soSanhLocalDateTime(LocalDateTime a, LocalDateTime b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareTo(b);
    }

    private int soSanhLong(Long a, Long b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareTo(b);
    }

    private Integer demTongSoKy(ChuongTrinhCauTrucResponse cauTruc) {
        if (cauTruc == null || cauTruc.getKhungKy() == null) return 0;
        return cauTruc.getKhungKy().size();
    }

    private Integer demTongSoMon(ChuongTrinhCauTrucResponse cauTruc) {
        if (cauTruc == null || cauTruc.getMonTrongChuongTrinh() == null) return 0;
        return cauTruc.getMonTrongChuongTrinh().size();
    }

    private BigDecimal layTongSoTinChi(
            ChuongTrinhVersionResponse versionResponse,
            ChuongTrinhVersion version
    ) {
        if (versionResponse != null && versionResponse.getTongTinChi() != null) {
            return versionResponse.getTongTinChi();
        }

        return version == null ? null : version.getTongTinChi();
    }

    private BigDecimal layTongSoGio(
            ChuongTrinhVersionResponse versionResponse,
            ChuongTrinhVersion version
    ) {
        if (versionResponse != null && versionResponse.getTongSoGio() != null) {
            return versionResponse.getTongSoGio();
        }

        return version == null ? null : version.getTongSoGio();
    }

    private record VersionDaChon(
            ChuongTrinhVersion version,
            String lyDo
    ) {
    }
}