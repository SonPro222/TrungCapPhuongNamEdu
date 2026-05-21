package org.example.trungcapphuongnam.module.chuongTrinh.mapper;


import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienMonHocGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienMonHocGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.DieuKienMonHocGoc;
import org.springframework.stereotype.Component;

@Component
public class DieuKienMonHocGocMapper {

    public DieuKienMonHocGoc toEntity(DieuKienMonHocGocRequest request) {
        if (request == null) {
            return null;
        }

        return DieuKienMonHocGoc.builder()
                .ma(request.getMa())
                .loai(request.getLoai())
                .noiDung(request.getNoiDung())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public void updateEntity(DieuKienMonHocGoc entity, DieuKienMonHocGocRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setMa(request.getMa());
        entity.setLoai(request.getLoai());
        entity.setNoiDung(request.getNoiDung());
        entity.setGhiChu(request.getGhiChu());
    }

    public DieuKienMonHocGocResponse toResponse(DieuKienMonHocGoc entity) {
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