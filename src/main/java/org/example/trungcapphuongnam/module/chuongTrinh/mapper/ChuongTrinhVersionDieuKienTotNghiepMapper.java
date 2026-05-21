package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.springframework.stereotype.Component;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionDieuKienTotNghiepRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionDieuKienTotNghiepResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersionDieuKienTotNghiep;

@Component
public class ChuongTrinhVersionDieuKienTotNghiepMapper {

    public ChuongTrinhVersionDieuKienTotNghiep toEntity(ChuongTrinhVersionDieuKienTotNghiepRequest request) {
        if (request == null) {
            return null;
        }
        ChuongTrinhVersionDieuKienTotNghiep entity = new ChuongTrinhVersionDieuKienTotNghiep();
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setDieuKienGocId(request.getDieuKienGocId());
        entity.setThuTu(request.getThuTu());
        entity.setGhiChu(request.getGhiChu());
        return entity;
    }

    public void updateEntity(ChuongTrinhVersionDieuKienTotNghiep entity, ChuongTrinhVersionDieuKienTotNghiepRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setDieuKienGocId(request.getDieuKienGocId());
        entity.setThuTu(request.getThuTu());
        entity.setGhiChu(request.getGhiChu());
    }

    public ChuongTrinhVersionDieuKienTotNghiepResponse toResponse(ChuongTrinhVersionDieuKienTotNghiep entity) {
        if (entity == null) {
            return null;
        }
        return ChuongTrinhVersionDieuKienTotNghiepResponse.builder()
                .id(entity.getId())
                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
                .dieuKienGocId(entity.getDieuKienGocId())
                .thuTu(entity.getThuTu())
                .ghiChu(entity.getGhiChu())
                .build();
    }
}
