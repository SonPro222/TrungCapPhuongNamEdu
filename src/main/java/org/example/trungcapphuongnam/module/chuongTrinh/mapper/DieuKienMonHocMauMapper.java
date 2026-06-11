package org.example.trungcapphuongnam.module.chuongTrinh.mapper;


import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienMonHocGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienMonHocGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.DieuKienMonHocMau;
import org.springframework.stereotype.Component;

@Component
public class DieuKienMonHocGocMapper {

    public DieuKienMonHocMau toEntity(DieuKienMonHocGocRequest request) {
        if (request == null) {
            return null;
        }

        return DieuKienMonHocMau.builder()
                .ma(request.getMa())
                .loai(request.getLoai())
                .noiDung(request.getNoiDung())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public void updateEntity(DieuKienMonHocMau entity, DieuKienMonHocGocRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setMa(request.getMa());
        entity.setLoai(request.getLoai());
        entity.setNoiDung(request.getNoiDung());
        entity.setGhiChu(request.getGhiChu());
    }

    public DieuKienMonHocGocResponse toResponse(DieuKienMonHocMau entity) {
        if (entity == null) {
            return null;
        }

        return DieuKienMonHocGocResponse.builder()
                .id(entity.getId())
                .ma(entity.getMa())
                .loai(entity.getLoai())
                .noiDung(entity.getNoiDung())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}