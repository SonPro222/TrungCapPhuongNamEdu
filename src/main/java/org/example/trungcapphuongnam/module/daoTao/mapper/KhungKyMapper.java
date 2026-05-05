package org.example.trungcapphuongnam.module.daoTao.mapper;

import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.KhungKy;
import org.springframework.stereotype.Component;

@Component
public class KhungKyMapper {

    public KhungKy toEntity(KhungKyRequest request) {
        if (request == null) return null;
        return KhungKy.builder()
                .loaiChuongTrinhId(request.getLoaiChuongTrinhId())
                .maKy(request.getMaKy())
                .tenKy(request.getTenKy())
                .thuTu(request.getThuTu())
                .build();
    }

    public KhungKyResponse toResponse(KhungKy entity) {
        if (entity == null) return null;
        return KhungKyResponse.builder()
                .id(entity.getId())
                .loaiChuongTrinhId(entity.getLoaiChuongTrinhId())
                .maKy(entity.getMaKy())
                .tenKy(entity.getTenKy())
                .thuTu(entity.getThuTu())
                .build();
    }

    public void updateEntity(KhungKy entity, KhungKyRequest request) {
        if (entity == null || request == null) return;
        entity.setLoaiChuongTrinhId(request.getLoaiChuongTrinhId());
        entity.setMaKy(request.getMaKy());
        entity.setTenKy(request.getTenKy());
        entity.setThuTu(request.getThuTu());
    }
}
