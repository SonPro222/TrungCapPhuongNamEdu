package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocMauDieuKienRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauDieuKienResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocMauDieuKien;
import org.springframework.stereotype.Component;

@Component
public class SyllabusMonHocMauDieuKienMapper {

    public SyllabusMonHocMauDieuKien toEntity(SyllabusMonHocMauDieuKienRequest request) {
        if (request == null) {
            return null;
        }

        return SyllabusMonHocMauDieuKien.builder()
                .syllabusMonHocMauId(request.getSyllabusMonHocMauId())
                .dieuKienMauId(request.getDieuKienMauId())
                .thuTu(request.getThuTu())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public void updateEntity(SyllabusMonHocMauDieuKien entity, SyllabusMonHocMauDieuKienRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setSyllabusMonHocMauId(request.getSyllabusMonHocMauId());
        entity.setDieuKienMauId(request.getDieuKienMauId());
        entity.setThuTu(request.getThuTu());
        entity.setGhiChu(request.getGhiChu());
    }

    public SyllabusMonHocMauDieuKienResponse toResponse(SyllabusMonHocMauDieuKien entity) {
        if (entity == null) {
            return null;
        }

        return SyllabusMonHocMauDieuKienResponse.builder()
                .id(entity.getId())
                .syllabusMonHocMauId(entity.getSyllabusMonHocMauId())
                .dieuKienMauId(entity.getDieuKienMauId())
                .thuTu(entity.getThuTu())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}