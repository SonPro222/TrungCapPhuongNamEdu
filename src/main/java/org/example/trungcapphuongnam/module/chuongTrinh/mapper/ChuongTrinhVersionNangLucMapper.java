package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.springframework.stereotype.Component;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionNangLucRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionNangLucResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersionNangLuc;

@Component
public class ChuongTrinhVersionNangLucMapper {

    public ChuongTrinhVersionNangLuc toEntity(ChuongTrinhVersionNangLucRequest request) {
        if (request == null) {
            return null;
        }
        ChuongTrinhVersionNangLuc entity = new ChuongTrinhVersionNangLuc();
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setNangLucGocId(request.getNangLucGocId());
        entity.setThuTu(request.getThuTu());
        entity.setGhiChu(request.getGhiChu());
        return entity;
    }

    public void updateEntity(ChuongTrinhVersionNangLuc entity, ChuongTrinhVersionNangLucRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setNangLucGocId(request.getNangLucGocId());
        entity.setThuTu(request.getThuTu());
        entity.setGhiChu(request.getGhiChu());
    }

    public ChuongTrinhVersionNangLucResponse toResponse(ChuongTrinhVersionNangLuc entity) {
        if (entity == null) {
            return null;
        }
        return ChuongTrinhVersionNangLucResponse.builder()
                .id(entity.getId())
                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
                .nangLucGocId(entity.getNangLucGocId())
                .thuTu(entity.getThuTu())
                .ghiChu(entity.getGhiChu())
                .build();
    }
}
