package org.example.trungcapphuongnam.module.daoTao.mapper;

import org.example.trungcapphuongnam.module.daoTao.dto.NganhLoaiChuongTrinhRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhLoaiChuongTrinhResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.NganhLoaiChuongTrinh;
import org.springframework.stereotype.Component;

@Component
public class NganhLoaiChuongTrinhMapper {

    public NganhLoaiChuongTrinh toEntity(NganhLoaiChuongTrinhRequest request) {
        if (request == null) return null;

        return NganhLoaiChuongTrinh.builder()
                .nganhId(request.getNganhId())
                .loaiChuongTrinhId(request.getLoaiChuongTrinhId())
                .trangThai(request.getTrangThai())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public NganhLoaiChuongTrinhResponse toResponse(NganhLoaiChuongTrinh entity) {
        if (entity == null) return null;

        return NganhLoaiChuongTrinhResponse.builder()
                .id(entity.getId())
                .nganhId(entity.getNganhId())
                .loaiChuongTrinhId(entity.getLoaiChuongTrinhId())
                .trangThai(entity.getTrangThai())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(NganhLoaiChuongTrinh entity, NganhLoaiChuongTrinhRequest request) {
        if (entity == null || request == null) return;

        entity.setNganhId(request.getNganhId());
        entity.setLoaiChuongTrinhId(request.getLoaiChuongTrinhId());
        entity.setTrangThai(request.getTrangThai());
        entity.setGhiChu(request.getGhiChu());
    }
}
