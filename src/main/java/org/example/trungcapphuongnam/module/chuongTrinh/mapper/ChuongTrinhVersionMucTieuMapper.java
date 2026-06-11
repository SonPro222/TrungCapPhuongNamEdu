package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.springframework.stereotype.Component;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionMucTieuRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionMucTieuResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersionMucTieu;

@Component
public class ChuongTrinhVersionMucTieuMapper {

    public ChuongTrinhVersionMucTieu toEntity(ChuongTrinhVersionMucTieuRequest request) {
        if (request == null) {
            return null;
        }
        ChuongTrinhVersionMucTieu entity = new ChuongTrinhVersionMucTieu();
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setMucTieuMauId(request.getMucTieuMauId());
        entity.setThuTu(request.getThuTu());
        entity.setGhiChu(request.getGhiChu());
        return entity;
    }

    public void updateEntity(ChuongTrinhVersionMucTieu entity, ChuongTrinhVersionMucTieuRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setMucTieuMauId(request.getMucTieuMauId());
        entity.setThuTu(request.getThuTu());
        entity.setGhiChu(request.getGhiChu());
    }

    public ChuongTrinhVersionMucTieuResponse toResponse(ChuongTrinhVersionMucTieu entity) {
        if (entity == null) {
            return null;
        }
        return ChuongTrinhVersionMucTieuResponse.builder()
                .id(entity.getId())
                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
                .mucTieuMauId(entity.getMucTieuMauId())
                .thuTu(entity.getThuTu())
                .ghiChu(entity.getGhiChu())
                .build();
    }
}
