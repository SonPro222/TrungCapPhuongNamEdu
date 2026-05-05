package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MonHocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MonHocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.MonHoc;
import org.springframework.stereotype.Component;

@Component
public class MonHocMapper {

    public MonHoc toEntity(MonHocRequest request) {
        if (request == null) return null;
        return MonHoc.builder()
                .maMon(request.getMaMon())
                .tenMon(request.getTenMon())
                .moTa(request.getMoTa())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
    }

    public MonHocResponse toResponse(MonHoc entity) {
        if (entity == null) return null;
        return MonHocResponse.builder()
                .id(entity.getId())
                .maMon(entity.getMaMon())
                .tenMon(entity.getTenMon())
                .moTa(entity.getMoTa())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(MonHoc entity, MonHocRequest request) {
        if (entity == null || request == null) return;
        entity.setMaMon(request.getMaMon());
        entity.setTenMon(request.getTenMon());
        entity.setMoTa(request.getMoTa());
        entity.setCreatedAt(request.getCreatedAt());
        entity.setUpdatedAt(request.getUpdatedAt());
    }
}
