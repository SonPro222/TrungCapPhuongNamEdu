package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.RubricCauHoiVersionRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.RubricCauHoiVersionResponse;
import org.example.trungcapphuongnam.module.lms.entity.RubricCauHoiVersion;

public final class RubricCauHoiVersionMapper {
    private RubricCauHoiVersionMapper() {}

    public static RubricCauHoiVersionResponse toResponse(RubricCauHoiVersion entity) {
        if (entity == null) return null;
        return RubricCauHoiVersionResponse.builder()
            .id(entity.getId())
            .cauHoiVersionId(entity.getCauHoiVersionId())
            .tieuChi(entity.getTieuChi())
            .moTa(entity.getMoTa())
            .diemToiDa(entity.getDiemToiDa())
            .thuTu(entity.getThuTu())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    public static RubricCauHoiVersion toEntity(RubricCauHoiVersionRequest request) {
        if (request == null) return null;
        return RubricCauHoiVersion.builder()
            .cauHoiVersionId(request.getCauHoiVersionId())
            .tieuChi(request.getTieuChi())
            .moTa(request.getMoTa())
            .diemToiDa(request.getDiemToiDa())
            .thuTu(request.getThuTu())
            .build();
    }

    public static void updateEntity(RubricCauHoiVersion entity, RubricCauHoiVersionRequest request) {
        if (entity == null || request == null) return;
        entity.setCauHoiVersionId(request.getCauHoiVersionId());
        entity.setTieuChi(request.getTieuChi());
        entity.setMoTa(request.getMoTa());
        entity.setDiemToiDa(request.getDiemToiDa());
        entity.setThuTu(request.getThuTu());
    }
}
