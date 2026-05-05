package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.DapAnCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.DapAnCauHoiResponse;
import org.example.trungcapphuongnam.module.lms.entity.DapAnCauHoi;

public final class DapAnCauHoiMapper {
    private DapAnCauHoiMapper() {}

    public static DapAnCauHoiResponse toResponse(DapAnCauHoi entity) {
        if (entity == null) return null;
        return DapAnCauHoiResponse.builder()
            .id(entity.getId())
            .cauHoiId(entity.getCauHoiId())
            .noiDung(entity.getNoiDung())
            .laDapAnDung(entity.getLaDapAnDung())
            .thuTu(entity.getThuTu())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    public static DapAnCauHoi toEntity(DapAnCauHoiRequest request) {
        if (request == null) return null;
        return DapAnCauHoi.builder()
            .cauHoiId(request.getCauHoiId())
            .noiDung(request.getNoiDung())
            .laDapAnDung(request.getLaDapAnDung())
            .thuTu(request.getThuTu())
            .build();
    }

    public static void updateEntity(DapAnCauHoi entity, DapAnCauHoiRequest request) {
        if (entity == null || request == null) return;
        entity.setCauHoiId(request.getCauHoiId());
        entity.setNoiDung(request.getNoiDung());
        entity.setLaDapAnDung(request.getLaDapAnDung());
        entity.setThuTu(request.getThuTu());
    }
}
