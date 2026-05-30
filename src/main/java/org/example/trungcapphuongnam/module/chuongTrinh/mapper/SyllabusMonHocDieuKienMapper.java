package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocDieuKienRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocDieuKienResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocDieuKien;
import org.springframework.stereotype.Component;

@Component
public class SyllabusMonHocDieuKienMapper {

    public SyllabusMonHocDieuKien toEntity(SyllabusMonHocDieuKienRequest request) {
        if (request == null) {
            return null;
        }

        return SyllabusMonHocDieuKien.builder()
                .syllabusMonId(request.getSyllabusMonId())
                .dieuKienGocId(request.getDieuKienGocId())
                .thuTu(request.getThuTu())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public void updateEntity(SyllabusMonHocDieuKien entity, SyllabusMonHocDieuKienRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setSyllabusMonId(request.getSyllabusMonId());
        entity.setDieuKienGocId(request.getDieuKienGocId());
        entity.setThuTu(request.getThuTu());
        entity.setGhiChu(request.getGhiChu());
    }

    public SyllabusMonHocDieuKienResponse toResponse(SyllabusMonHocDieuKien entity) {
        if (entity == null) {
            return null;
        }

        return SyllabusMonHocDieuKienResponse.builder()
                .id(entity.getId())
                .syllabusMonId(entity.getSyllabusMonId())
                .dieuKienGocId(entity.getDieuKienGocId())
                .thuTu(entity.getThuTu())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}