package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomTuChonMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomTuChonMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomTuChonMau;
import org.springframework.stereotype.Component;

@Component
public class NhomTuChonMauMapper {

    public NhomTuChonMau toEntity(NhomTuChonMauRequest request) {
        if (request == null) {
            return null;
        }

        return NhomTuChonMau.builder()
                .ma(request.getMa())
                .ten(request.getTen())
                .moTa(request.getMoTa())
                .build();
    }

    public void updateEntity(NhomTuChonMau entity, NhomTuChonMauRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setMoTa(request.getMoTa());
    }

    public NhomTuChonMauResponse toResponse(NhomTuChonMau entity) {
        if (entity == null) {
            return null;
        }

        return NhomTuChonMauResponse.builder()
                .id(entity.getId())
                .ma(entity.getMa())
                .ten(entity.getTen())
                .moTa(entity.getMoTa())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}