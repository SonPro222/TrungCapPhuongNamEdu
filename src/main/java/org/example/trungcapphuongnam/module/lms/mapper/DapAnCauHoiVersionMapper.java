package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.DapAnCauHoiVersionRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.DapAnCauHoiVersionResponse;
import org.example.trungcapphuongnam.module.lms.entity.DapAnCauHoiVersion;

public final class DapAnCauHoiVersionMapper {
    private DapAnCauHoiVersionMapper() {}

    public static DapAnCauHoiVersionResponse toResponse(DapAnCauHoiVersion entity) {
        if (entity == null) return null;
        return DapAnCauHoiVersionResponse.builder()
            .id(entity.getId())
            .cauHoiVersionId(entity.getCauHoiVersionId())
            .noiDung(entity.getNoiDung())
            .laDapAnDung(entity.getLaDapAnDung())
            .thuTu(entity.getThuTu())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    public static DapAnCauHoiVersion toEntity(DapAnCauHoiVersionRequest request) {
        if (request == null) return null;
        return DapAnCauHoiVersion.builder()
            .cauHoiVersionId(request.getCauHoiVersionId())
            .noiDung(request.getNoiDung())
            .laDapAnDung(request.getLaDapAnDung())
            .thuTu(request.getThuTu())
            .build();
    }

    public static void updateEntity(DapAnCauHoiVersion entity, DapAnCauHoiVersionRequest request) {
        if (entity == null || request == null) return;
        entity.setCauHoiVersionId(request.getCauHoiVersionId());
        entity.setNoiDung(request.getNoiDung());
        entity.setLaDapAnDung(request.getLaDapAnDung());
        entity.setThuTu(request.getThuTu());
    }
}
