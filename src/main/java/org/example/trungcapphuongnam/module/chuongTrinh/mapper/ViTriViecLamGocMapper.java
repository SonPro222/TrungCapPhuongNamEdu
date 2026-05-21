package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.springframework.stereotype.Component;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ViTriViecLamGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ViTriViecLamGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ViTriViecLamGoc;

@Component
public class ViTriViecLamGocMapper {

    public ViTriViecLamGoc toEntity(ViTriViecLamGocRequest request) {
        if (request == null) {
            return null;
        }
        ViTriViecLamGoc entity = new ViTriViecLamGoc();
        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setMoTa(request.getMoTa());
        entity.setGhiChu(request.getGhiChu());
        return entity;
    }

    public void updateEntity(ViTriViecLamGoc entity, ViTriViecLamGocRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setMoTa(request.getMoTa());
        entity.setGhiChu(request.getGhiChu());
    }

    public ViTriViecLamGocResponse toResponse(ViTriViecLamGoc entity) {
        if (entity == null) {
            return null;
        }
        return ViTriViecLamGocResponse.builder()
                .id(entity.getId())
                .ma(entity.getMa())
                .ten(entity.getTen())
                .moTa(entity.getMoTa())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
