package org.example.trungcapphuongnam.module.daoTao.mapper;


import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyMauRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyMauResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.KhungKyMau;
import org.springframework.stereotype.Component;

@Component
public class KhungKyMauMapper {

    public KhungKyMau toEntity(KhungKyMauRequest request) {
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

    public KhungKyMauResponse toResponse(KhungKyMau entity) {
        if (entity == null) {
            return null;
        }

        return KhungKyMauResponse.builder()
                .id(entity.getId())
                .maKy(entity.getMaKy())
                .tenKy(entity.getTenKy())
                .thuTu(entity.getThuTu())
                .moTa(entity.getMoTa())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(KhungKyMau entity, KhungKyMauRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setMaKy(request.getMaKy());
        entity.setTenKy(request.getTenKy());
        entity.setThuTu(request.getThuTu());
        entity.setMoTa(request.getMoTa());
    }
}