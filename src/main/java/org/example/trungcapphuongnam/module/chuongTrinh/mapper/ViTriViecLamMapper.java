package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ViTriViecLamRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ViTriViecLamResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ViTriViecLam;
import org.springframework.stereotype.Component;

@Component
public class ViTriViecLamMapper {

    public ViTriViecLam toEntity(ViTriViecLamRequest request) {
        if (request == null) return null;
        return ViTriViecLam.builder()
                .chuongTrinhVersionId(request.getChuongTrinhVersionId())
                .ten(request.getTen())
                .moTa(request.getMoTa())
                .thuTu(request.getThuTu())
                .build();
    }

    public ViTriViecLamResponse toResponse(ViTriViecLam entity) {
        if (entity == null) return null;
        return ViTriViecLamResponse.builder()
                .id(entity.getId())
                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
                .ten(entity.getTen())
                .moTa(entity.getMoTa())
                .thuTu(entity.getThuTu())
                .build();
    }

    public void updateEntity(ViTriViecLam entity, ViTriViecLamRequest request) {
        if (entity == null || request == null) return;
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setTen(request.getTen());
        entity.setMoTa(request.getMoTa());
        entity.setThuTu(request.getThuTu());
    }
}
