package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinh;
import org.springframework.stereotype.Component;

@Component
public class ChuongTrinhMapper {

    public ChuongTrinh toEntity(ChuongTrinhRequest request) {
        if (request == null) return null;
        return ChuongTrinh.builder()
                .nganhLoaiChuongTrinhId(request.getNganhLoaiChuongTrinhId())
                .trinhDoId(request.getTrinhDoId())
                .maChuongTrinh(request.getMaChuongTrinh())
                .tenChuongTrinh(request.getTenChuongTrinh())
                .doiTuongTuyenSinh(request.getDoiTuongTuyenSinh())
                .thoiGianDaoTao(request.getThoiGianDaoTao())
                .build();
    }

    public ChuongTrinhResponse toResponse(ChuongTrinh entity) {
        if (entity == null) return null;
        return ChuongTrinhResponse.builder()
                .id(entity.getId())
                .nganhLoaiChuongTrinhId(entity.getNganhLoaiChuongTrinhId())
                .nganhId(entity.getNganhId())
                .trinhDoId(entity.getTrinhDoId())
                .loaiChuongTrinhId(entity.getLoaiChuongTrinhId())
                .maChuongTrinh(entity.getMaChuongTrinh())
                .tenChuongTrinh(entity.getTenChuongTrinh())
                .doiTuongTuyenSinh(entity.getDoiTuongTuyenSinh())
                .thoiGianDaoTao(entity.getThoiGianDaoTao())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(ChuongTrinh entity, ChuongTrinhRequest request) {
        if (entity == null || request == null) return;
        entity.setNganhLoaiChuongTrinhId(request.getNganhLoaiChuongTrinhId());
        entity.setTrinhDoId(request.getTrinhDoId());
        entity.setMaChuongTrinh(request.getMaChuongTrinh());
        entity.setTenChuongTrinh(request.getTenChuongTrinh());
        entity.setDoiTuongTuyenSinh(request.getDoiTuongTuyenSinh());
        entity.setThoiGianDaoTao(request.getThoiGianDaoTao());
    }
}
