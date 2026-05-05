package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.LichSuBaiTapLopRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LichSuBaiTapLopResponse;
import org.example.trungcapphuongnam.module.lms.entity.LichSuBaiTapLop;

public final class LichSuBaiTapLopMapper {
    private LichSuBaiTapLopMapper() {}

    public static LichSuBaiTapLopResponse toResponse(LichSuBaiTapLop entity) {
        if (entity == null) return null;
        return LichSuBaiTapLopResponse.builder()
            .id(entity.getId())
            .baiTapLopId(entity.getBaiTapLopId())
            .nguoiThucHienId(entity.getNguoiThucHienId())
            .vaiTroThucHien(entity.getVaiTroThucHien())
            .hanhDong(entity.getHanhDong())
            .duLieuCu(entity.getDuLieuCu())
            .duLieuMoi(entity.getDuLieuMoi())
            .lyDo(entity.getLyDo())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    public static LichSuBaiTapLop toEntity(LichSuBaiTapLopRequest request) {
        if (request == null) return null;
        return LichSuBaiTapLop.builder()
            .baiTapLopId(request.getBaiTapLopId())
            .nguoiThucHienId(request.getNguoiThucHienId())
            .vaiTroThucHien(request.getVaiTroThucHien())
            .hanhDong(request.getHanhDong())
            .duLieuCu(request.getDuLieuCu())
            .duLieuMoi(request.getDuLieuMoi())
            .lyDo(request.getLyDo())
            .build();
    }

    public static void updateEntity(LichSuBaiTapLop entity, LichSuBaiTapLopRequest request) {
        if (entity == null || request == null) return;
        entity.setBaiTapLopId(request.getBaiTapLopId());
        entity.setNguoiThucHienId(request.getNguoiThucHienId());
        entity.setVaiTroThucHien(request.getVaiTroThucHien());
        entity.setHanhDong(request.getHanhDong());
        entity.setDuLieuCu(request.getDuLieuCu());
        entity.setDuLieuMoi(request.getDuLieuMoi());
        entity.setLyDo(request.getLyDo());
    }
}
