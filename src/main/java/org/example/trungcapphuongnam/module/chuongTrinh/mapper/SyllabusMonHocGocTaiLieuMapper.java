package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocGocTaiLieuRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocGocTaiLieuResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocGocTaiLieu;
import org.springframework.stereotype.Component;

@Component
public class SyllabusMonHocGocTaiLieuMapper {

    public SyllabusMonHocGocTaiLieu toEntity(SyllabusMonHocGocTaiLieuRequest request) {
        if (request == null) {
            return null;
        }

        return SyllabusMonHocGocTaiLieu.builder()
                .syllabusMonHocGocId(request.getSyllabusMonHocGocId())
                .taiLieuGocId(request.getTaiLieuGocId())
                .thuTu(request.getThuTu())
                .batBuoc(request.getBatBuoc())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public void updateEntity(SyllabusMonHocGocTaiLieu entity, SyllabusMonHocGocTaiLieuRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setSyllabusMonHocGocId(request.getSyllabusMonHocGocId());
        entity.setTaiLieuGocId(request.getTaiLieuGocId());
        entity.setThuTu(request.getThuTu());
        entity.setBatBuoc(request.getBatBuoc());
        entity.setGhiChu(request.getGhiChu());
    }

    public SyllabusMonHocGocTaiLieuResponse toResponse(SyllabusMonHocGocTaiLieu entity) {
        if (entity == null) {
            return null;
        }

        return SyllabusMonHocGocTaiLieuResponse.builder()
                .id(entity.getId())
                .syllabusMonHocGocId(entity.getSyllabusMonHocGocId())
                .taiLieuGocId(entity.getTaiLieuGocId())
                .thuTu(entity.getThuTu())
                .batBuoc(entity.getBatBuoc())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}