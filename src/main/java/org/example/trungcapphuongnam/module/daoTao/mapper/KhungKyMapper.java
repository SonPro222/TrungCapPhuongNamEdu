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
                .maKy(request.getMaKy())
                .tenKy(request.getTenKy())
                .thuTu(request.getThuTu())
                .moTa(request.getMoTa())
                .ngayBatDau(request.getNgayBatDau())
                .ngayKetThuc(request.getNgayKetThuc())
                .build();
    }

    public KhungKyResponse toResponse(KhungKy entity) {
        if (entity == null) {
            return null;
        }

        return KhungKyResponse.builder()
                .id(entity.getId())
                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
                .maKy(entity.getMaKy())
                .tenKy(entity.getTenKy())
                .thuTu(entity.getThuTu())
                .moTa(entity.getMoTa())
                .ngayBatDau(entity.getNgayBatDau())
                .ngayKetThuc(entity.getNgayKetThuc())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(KhungKy entity, KhungKyRequest request) {
        if (entity == null || request == null) {
            return;
        }

        // chuongTrinhVersionId không đổi khi update — chỉ ghi nếu không null để tránh NOT NULL violation
        if (request.getChuongTrinhVersionId() != null) {
            entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        }
        entity.setMaKy(request.getMaKy());
        entity.setTenKy(request.getTenKy());
        entity.setThuTu(request.getThuTu());
        entity.setMoTa(request.getMoTa());
        entity.setNgayBatDau(request.getNgayBatDau());
        entity.setNgayKetThuc(request.getNgayKetThuc());
    }
}