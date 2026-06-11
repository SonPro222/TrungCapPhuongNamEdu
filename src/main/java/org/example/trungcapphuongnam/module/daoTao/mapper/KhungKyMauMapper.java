package org.example.trungcapphuongnam.module.daoTao.mapper;


import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyGocRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyGocResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.KhungKyMau;
import org.springframework.stereotype.Component;

@Component
public class KhungKyGocMapper {

    public KhungKyMau toEntity(KhungKyGocRequest request) {
        if (request == null) {
            return null;
        }

        return KhungKyMau.builder()
                .maKy(request.getMaKy())
                .tenKy(request.getTenKy())
                .thuTu(request.getThuTu())
                .moTa(request.getMoTa())
                .build();
    }

    public KhungKyGocResponse toResponse(KhungKyMau entity) {
        if (entity == null) {
            return null;
        }

        return KhungKyGocResponse.builder()
                .id(entity.getId())
                .maKy(entity.getMaKy())
                .tenKy(entity.getTenKy())
                .thuTu(entity.getThuTu())
                .moTa(entity.getMoTa())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(KhungKyMau entity, KhungKyGocRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setMaKy(request.getMaKy());
        entity.setTenKy(request.getTenKy());
        entity.setThuTu(request.getThuTu());
        entity.setMoTa(request.getMoTa());
    }
}