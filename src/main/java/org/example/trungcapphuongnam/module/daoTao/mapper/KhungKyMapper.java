package org.example.trungcapphuongnam.module.daoTao.mapper;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionRepository;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.KhungKy;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KhungKyMapper {

    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final ChuongTrinhRepository chuongTrinhRepository;
    private final NganhRepository nganhRepository;

    public KhungKy toEntity(KhungKyRequest request) {
        if (request == null) {
            return null;
        }

        return KhungKy.builder()
                .chuongTrinhVersionId(request.getChuongTrinhVersionId())
                .maKy(request.getMaKy())
                .tenKy(request.getTenKy())
                .thuTu(request.getThuTu())
                .moTa(request.getMoTa())
                .ngayBatDau(request.getNgayBatDau())
                .ngayKetThuc(request.getNgayKetThuc())
                .build();
    }

    public KhungKyResponse toResponse(KhungKy entity) {
        if (entity == null) {
            return null;
        }

        KhungKyResponse response = KhungKyResponse.builder()
                .id(entity.getId())
                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
                .maKy(entity.getMaKy())
                .tenKy(entity.getTenKy())
                .thuTu(entity.getThuTu())
                .moTa(entity.getMoTa())
                .ngayBatDau(entity.getNgayBatDau())
                .ngayKetThuc(entity.getNgayKetThuc())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();

        // Enrich: version -> chương trình -> ngành
        if (entity.getChuongTrinhVersionId() != null) {
            chuongTrinhVersionRepository.findById(entity.getChuongTrinhVersionId()).ifPresent(ver -> {
                response.setMaVersion(ver.getMaVersion());
                response.setTenVersion(ver.getTenVersion());
                response.setChuongTrinhId(ver.getChuongTrinhId());

                if (ver.getChuongTrinhId() != null) {
                    chuongTrinhRepository.findById(ver.getChuongTrinhId()).ifPresent(ct -> {
                        response.setTenChuongTrinh(ct.getTenChuongTrinh());
                        response.setNganhId(ct.getNganhId());

                        if (ct.getNganhId() != null) {
                            nganhRepository.findById(ct.getNganhId()).ifPresent(nganh -> {
                                response.setTenNganh(nganh.getTenNganh());
                            });
                        }
                    });
                }
            });
        }

        // Build label đầy đủ: tenNganh / tenChuongTrinh / tenVersion / tenKy
        String label = buildLabel(response.getTenNganh(), response.getTenChuongTrinh(),
                response.getTenVersion(), entity.getTenKy());
        response.setLabelDayDu(label);

        return response;
    }

    public void updateEntity(KhungKy entity, KhungKyRequest request) {
        if (entity == null || request == null) {
            return;
        }

        // chuongTrinhVersionId không đổi khi update — chỉ ghi nếu không null để tránh NOT NULL violation
        if (request.getChuongTrinhVersionId() != null) {
            entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        }
        entity.setMaKy(request.getMaKy());
        entity.setTenKy(request.getTenKy());
        entity.setThuTu(request.getThuTu());
        entity.setMoTa(request.getMoTa());
        entity.setNgayBatDau(request.getNgayBatDau());
        entity.setNgayKetThuc(request.getNgayKetThuc());
    }

    private String buildLabel(String tenNganh, String tenChuongTrinh, String tenVersion, String tenKy) {
        StringBuilder sb = new StringBuilder();
        if (tenNganh != null && !tenNganh.isBlank()) sb.append(tenNganh);
        if (tenChuongTrinh != null && !tenChuongTrinh.isBlank()) {
            if (sb.length() > 0) sb.append(" / ");
            sb.append(tenChuongTrinh);
        }
        if (tenVersion != null && !tenVersion.isBlank()) {
            if (sb.length() > 0) sb.append(" / ");
            sb.append(tenVersion);
        }
        if (tenKy != null && !tenKy.isBlank()) {
            if (sb.length() > 0) sb.append(" / ");
            sb.append(tenKy);
        }
        return sb.length() > 0 ? sb.toString() : tenKy;
    }
}
