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
                .syllabusChuongTrinhId(request.getSyllabusChuongTrinhId())
                .ten(request.getTen())
                .moTa(request.getMoTa())
                .thuTu(request.getThuTu())
                .ma(request.getMa())
                .ghiChu(request.getGhiChu())

                .build();
    }

    public ViTriViecLamResponse toResponse(ViTriViecLam entity) {
        if (entity == null) return null;
        return ViTriViecLamResponse.builder()
                .id(entity.getId())
                .syllabusChuongTrinhId(entity.getSyllabusChuongTrinhId())
                .ten(entity.getTen())
                .moTa(entity.getMoTa())
                .thuTu(entity.getThuTu())
                .ma(entity.getMa())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(ViTriViecLam entity, ViTriViecLamRequest request) {
        if (entity == null || request == null) return;
        entity.setSyllabusChuongTrinhId(request.getSyllabusChuongTrinhId());
        entity.setTen(request.getTen());
        entity.setMoTa(request.getMoTa());
        entity.setThuTu(request.getThuTu());
        entity.setMa(request.getMa());
        entity.setGhiChu(request.getGhiChu());

    }
}
