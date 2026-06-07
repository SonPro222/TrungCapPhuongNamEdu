package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.springframework.stereotype.Component;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionViTriViecLamRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionViTriViecLamResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersionViTriViecLam;

@Component
public class ChuongTrinhVersionViTriViecLamMapper {

    public ChuongTrinhVersionViTriViecLam toEntity(ChuongTrinhVersionViTriViecLamRequest request) {
        if (request == null) {
            return null;
        }
        ChuongTrinhVersionViTriViecLam entity = new ChuongTrinhVersionViTriViecLam();
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setViTriGocId(request.getViTriGocId());
        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setMoTa(request.getMoTa());
        entity.setThuTu(request.getThuTu());
        entity.setGhiChu(request.getGhiChu());
        return entity;
    }

    public void updateEntity(ChuongTrinhVersionViTriViecLam entity, ChuongTrinhVersionViTriViecLamRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setViTriGocId(request.getViTriGocId());
        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setMoTa(request.getMoTa());
        entity.setThuTu(request.getThuTu());
        entity.setGhiChu(request.getGhiChu());
    }

    public ChuongTrinhVersionViTriViecLamResponse toResponse(ChuongTrinhVersionViTriViecLam entity) {
        if (entity == null) {
            return null;
        }
        return ChuongTrinhVersionViTriViecLamResponse.builder()
                .id(entity.getId())
                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
                .viTriGocId(entity.getViTriGocId())
                .ma(entity.getMa())
                .ten(entity.getTen())
                .moTa(entity.getMoTa())
                .thuTu(entity.getThuTu())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
