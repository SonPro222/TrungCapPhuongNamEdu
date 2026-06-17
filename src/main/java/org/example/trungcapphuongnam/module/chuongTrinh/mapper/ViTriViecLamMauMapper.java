package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.springframework.stereotype.Component;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ViTriViecLamMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ViTriViecLamMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ViTriViecLamMau;

@Component
public class ViTriViecLamMauMapper {

    public ViTriViecLamMau toEntity(ViTriViecLamMauRequest request) {
        if (request == null) {
            return null;
        }
        ViTriViecLamMau entity = new ViTriViecLamMau();
        entity.setSyllabusChuongTrinhMauId(request.getSyllabusChuongTrinhMauId());
        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setMoTa(request.getMoTa());
        entity.setGhiChu(request.getGhiChu());
        return entity;
    }

    public void updateEntity(ViTriViecLamMau entity, ViTriViecLamMauRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setSyllabusChuongTrinhMauId(request.getSyllabusChuongTrinhMauId());
        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setMoTa(request.getMoTa());
        entity.setGhiChu(request.getGhiChu());
    }

    public ViTriViecLamMauResponse toResponse(ViTriViecLamMau entity) {
        if (entity == null) {
            return null;
        }
        return ViTriViecLamMauResponse.builder()
                .id(entity.getId())
                .syllabusChuongTrinhMauId(entity.getSyllabusChuongTrinhMauId())
                .ma(entity.getMa())
                .ten(entity.getTen())
                .moTa(entity.getMoTa())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
