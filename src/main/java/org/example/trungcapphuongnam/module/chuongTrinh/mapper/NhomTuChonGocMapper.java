package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomTuChonGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomTuChonGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomTuChonGoc;
import org.springframework.stereotype.Component;

@Component
public class NhomTuChonGocMapper {

    public NhomTuChonGoc toEntity(NhomTuChonGocRequest request) {
        if (request == null) {
            return null;
        }

        return NhomTuChonGoc.builder()
                .ma(request.getMa())
                .ten(request.getTen())
                .moTa(request.getMoTa())
                .build();
    }

    public void updateEntity(NhomTuChonGoc entity, NhomTuChonGocRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setMoTa(request.getMoTa());
    }

    public NhomTuChonGocResponse toResponse(NhomTuChonGoc entity) {
        if (entity == null) {
            return null;
        }

        return NhomTuChonGocResponse.builder()
                .id(entity.getId())
                .ma(entity.getMa())
                .ten(entity.getTen())
                .moTa(entity.getMoTa())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}