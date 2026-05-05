package org.example.trungcapphuongnam.module.daoTao.mapper;

import org.example.trungcapphuongnam.module.daoTao.dto.NganhRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.Nganh;
import org.springframework.stereotype.Component;

@Component
public class NganhMapper {

    public Nganh toEntity(NganhRequest request) {
        if (request == null) return null;
        return Nganh.builder()
                .maNganh(request.getMaNganh())
                .tenNganh(request.getTenNganh())
                .moTa(request.getMoTa())
                .build();
    }

    public NganhResponse toResponse(Nganh entity) {
        if (entity == null) return null;
        return NganhResponse.builder()
                .id(entity.getId())
                .maNganh(entity.getMaNganh())
                .tenNganh(entity.getTenNganh())
                .moTa(entity.getMoTa())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(Nganh entity, NganhRequest request) {
        if (entity == null || request == null) return;
        entity.setMaNganh(request.getMaNganh());
        entity.setTenNganh(request.getTenNganh());
        entity.setMoTa(request.getMoTa());
    }
}
