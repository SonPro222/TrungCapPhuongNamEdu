package org.example.trungcapphuongnam.module.daoTao.mapper;

import org.example.trungcapphuongnam.module.daoTao.dto.DotHocRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.DotHocResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.DotHoc;
import org.springframework.stereotype.Component;

@Component
public class DotHocMapper {

    public DotHoc toEntity(DotHocRequest request) {
        if (request == null) return null;
        return DotHoc.builder()
                .maDot(request.getMaDot())
                .tenDot(request.getTenDot())
                .khoaDaoTaoId(request.getKhoaDaoTaoId())
                .lopHanhChinhId(request.getLopHanhChinhId())
                .chuongTrinhVersionId(request.getChuongTrinhVersionId())
                .tuNgay(request.getTuNgay())
                .denNgay(request.getDenNgay())
                .trangThai(request.getTrangThai())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public DotHocResponse toResponse(DotHoc entity) {
        if (entity == null) return null;
        return DotHocResponse.builder()
                .id(entity.getId())
                .maDot(entity.getMaDot())
                .tenDot(entity.getTenDot())
                .khoaDaoTaoId(entity.getKhoaDaoTaoId())
                .lopHanhChinhId(entity.getLopHanhChinhId())
                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
                .tuNgay(entity.getTuNgay())
                .denNgay(entity.getDenNgay())
                .trangThai(entity.getTrangThai())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(DotHoc entity, DotHocRequest request) {
        if (entity == null || request == null) return;
        entity.setMaDot(request.getMaDot());
        entity.setTenDot(request.getTenDot());
        entity.setKhoaDaoTaoId(request.getKhoaDaoTaoId());
        entity.setLopHanhChinhId(request.getLopHanhChinhId());
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setTuNgay(request.getTuNgay());
        entity.setDenNgay(request.getDenNgay());
        entity.setTrangThai(request.getTrangThai());
        entity.setGhiChu(request.getGhiChu());
    }
}
