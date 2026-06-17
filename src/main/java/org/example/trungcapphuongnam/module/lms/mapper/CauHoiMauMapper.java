package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.CauHoiMauRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauHoiMauResponse;
import org.example.trungcapphuongnam.module.lms.entity.CauHoiMau;

public final class CauHoiMauMapper {
    private CauHoiMauMapper() {}

    public static CauHoiMauResponse toResponse(CauHoiMau entity) {
        if (entity == null) return null;
        return CauHoiMauResponse.builder()
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

    public static CauHoiMau toEntity(CauHoiMauRequest request) {
        if (request == null) return null;
        return CauHoiMau.builder()
            .monHocId(request.getMonHocId())
            .maCauHoi(request.getMaCauHoi())
            .nguoiTaoTaiKhoanId(request.getNguoiTaoTaiKhoanId())
            .trangThai(request.getTrangThai())
            .donViSoHuuId(request.getDonViSoHuuId())
            .build();
    }

    public static void updateEntity(CauHoiMau entity, CauHoiMauRequest request) {
        if (entity == null || request == null) return;
        entity.setMonHocId(request.getMonHocId());
        entity.setMaCauHoi(request.getMaCauHoi());
        entity.setNguoiTaoTaiKhoanId(request.getNguoiTaoTaiKhoanId());
        entity.setTrangThai(request.getTrangThai());
        entity.setDonViSoHuuId(request.getDonViSoHuuId());
    }
}
