package org.example.trungcapphuongnam.module.chuongTrinh.mapper;


import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocTaiLieuRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocTaiLieuResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocTaiLieu;
import org.springframework.stereotype.Component;

@Component
public class SyllabusMonHocTaiLieuMapper {

    public SyllabusMonHocTaiLieu toEntity(SyllabusMonHocTaiLieuRequest request) {
        if (request == null) {
            return null;
        }

        return SyllabusMonHocTaiLieu.builder()
                .syllabusMonId(request.getSyllabusMonId())
                .taiLieuGocId(request.getTaiLieuGocId())
                .thuTu(request.getThuTu())
                .batBuoc(request.getBatBuoc())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public void updateEntity(SyllabusMonHocTaiLieu entity, SyllabusMonHocTaiLieuRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setSyllabusMonId(request.getSyllabusMonId());
        entity.setTaiLieuGocId(request.getTaiLieuGocId());
        entity.setThuTu(request.getThuTu());
        entity.setBatBuoc(request.getBatBuoc());
        entity.setGhiChu(request.getGhiChu());
    }

    public SyllabusMonHocTaiLieuResponse toResponse(SyllabusMonHocTaiLieu entity) {
        if (entity == null) {
            return null;
        }

        return SyllabusMonHocTaiLieuResponse.builder()
                .id(entity.getId())
                .syllabusMonId(entity.getSyllabusMonId())
                .taiLieuGocId(entity.getTaiLieuGocId())
                .thuTu(entity.getThuTu())
                .batBuoc(entity.getBatBuoc())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}