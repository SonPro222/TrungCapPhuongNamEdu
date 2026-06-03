package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.CauHoiGocRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauHoiGocResponse;
import org.example.trungcapphuongnam.module.lms.entity.CauHoiGoc;

public final class CauHoiGocMapper {
    private CauHoiGocMapper() {}

    public static CauHoiGocResponse toResponse(CauHoiGoc entity) {
        if (entity == null) return null;
        return CauHoiGocResponse.builder()
            .id(entity.getId())
            .monHocId(entity.getMonHocId())
            .maCauHoi(entity.getMaCauHoi())
            .nguoiTaoTaiKhoanId(entity.getNguoiTaoTaiKhoanId())
            .trangThai(entity.getTrangThai())
            .donViSoHuuId(entity.getDonViSoHuuId())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    public static CauHoiGoc toEntity(CauHoiGocRequest request) {
        if (request == null) return null;
        return CauHoiGoc.builder()
            .monHocId(request.getMonHocId())
            .maCauHoi(request.getMaCauHoi())
            .nguoiTaoTaiKhoanId(request.getNguoiTaoTaiKhoanId())
            .trangThai(request.getTrangThai())
            .donViSoHuuId(request.getDonViSoHuuId())
            .build();
    }

    public static void updateEntity(CauHoiGoc entity, CauHoiGocRequest request) {
        if (entity == null || request == null) return;
        entity.setMonHocId(request.getMonHocId());
        entity.setMaCauHoi(request.getMaCauHoi());
        entity.setNguoiTaoTaiKhoanId(request.getNguoiTaoTaiKhoanId());
        entity.setTrangThai(request.getTrangThai());
        entity.setDonViSoHuuId(request.getDonViSoHuuId());
    }
}
