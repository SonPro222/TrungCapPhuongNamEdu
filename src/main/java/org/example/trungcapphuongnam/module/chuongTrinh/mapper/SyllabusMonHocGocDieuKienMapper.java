package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocGocDieuKienRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocGocDieuKienResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocGocDieuKien;
import org.springframework.stereotype.Component;

@Component
public class SyllabusMonHocGocDieuKienMapper {

    public SyllabusMonHocGocDieuKien toEntity(SyllabusMonHocGocDieuKienRequest request) {
        if (request == null) {
            return null;
        }

        return SyllabusMonHocGocDieuKien.builder()
                .syllabusMonHocGocId(request.getSyllabusMonHocGocId())
                .dieuKienGocId(request.getDieuKienGocId())
                .thuTu(request.getThuTu())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public void updateEntity(SyllabusMonHocGocDieuKien entity, SyllabusMonHocGocDieuKienRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setSyllabusMonHocGocId(request.getSyllabusMonHocGocId());
        entity.setDieuKienGocId(request.getDieuKienGocId());
        entity.setThuTu(request.getThuTu());
        entity.setGhiChu(request.getGhiChu());
    }

    public SyllabusMonHocGocDieuKienResponse toResponse(SyllabusMonHocGocDieuKien entity) {
        if (entity == null) {
            return null;
        }

        return SyllabusMonHocGocDieuKienResponse.builder()
                .id(entity.getId())
                .syllabusMonHocGocId(entity.getSyllabusMonHocGocId())
                .dieuKienGocId(entity.getDieuKienGocId())
                .thuTu(entity.getThuTu())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}