package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.springframework.stereotype.Component;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienTotNghiepGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienTotNghiepGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.DieuKienTotNghiepMau;

@Component
public class DieuKienTotNghiepGocMapper {

    public DieuKienTotNghiepMau toEntity(DieuKienTotNghiepGocRequest request) {
        if (request == null) {
            return null;
        }
        DieuKienTotNghiepMau entity = new DieuKienTotNghiepMau();
        entity.setMa(request.getMa());
        entity.setNoiDung(request.getNoiDung());
        entity.setGhiChu(request.getGhiChu());
        return entity;
    }

    public void updateEntity(DieuKienTotNghiepMau entity, DieuKienTotNghiepGocRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setMa(request.getMa());
        entity.setNoiDung(request.getNoiDung());
        entity.setGhiChu(request.getGhiChu());
    }

    public DieuKienTotNghiepGocResponse toResponse(DieuKienTotNghiepMau entity) {
        if (entity == null) {
            return null;
        }
        return DieuKienTotNghiepGocResponse.builder()
                .id(entity.getId())
                .ma(entity.getMa())
                .noiDung(entity.getNoiDung())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
