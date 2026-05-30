package org.example.trungcapphuongnam.module.daoTao.mapper;

import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.KhungKy;
import org.springframework.stereotype.Component;

@Component
public class KhungKyMapper {

    public KhungKy toEntity(KhungKyRequest request) {
        if (request == null) {
            return null;
        }

        return KhungKy.builder()
                .chuongTrinhVersionId(request.getChuongTrinhVersionId())
                .loaiChuongTrinhId(request.getLoaiChuongTrinhId())
                .khungKyGocId(request.getKhungKyGocId())
                .maKy(request.getMaKy())
                .tenKy(request.getTenKy())
                .thuTu(request.getThuTu())
                .moTa(request.getMoTa())
                .build();
    }

    public KhungKyResponse toResponse(KhungKy entity) {
        if (entity == null) {
            return null;
        }

        return KhungKyResponse.builder()
                .id(entity.getId())
                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
                .loaiChuongTrinhId(entity.getLoaiChuongTrinhId())
                .khungKyGocId(entity.getKhungKyGocId())
                .maKy(entity.getMaKy())
                .tenKy(entity.getTenKy())
                .thuTu(entity.getThuTu())
                .moTa(entity.getMoTa())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(KhungKy entity, KhungKyRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setLoaiChuongTrinhId(request.getLoaiChuongTrinhId());
        entity.setKhungKyGocId(request.getKhungKyGocId());
        entity.setMaKy(request.getMaKy());
        entity.setTenKy(request.getTenKy());
        entity.setThuTu(request.getThuTu());
        entity.setMoTa(request.getMoTa());
    }
}