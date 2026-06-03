package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.LichSuCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LichSuCauHoiResponse;
import org.example.trungcapphuongnam.module.lms.entity.LichSuCauHoi;

public final class LichSuCauHoiMapper {
    private LichSuCauHoiMapper() {}

    public static LichSuCauHoiResponse toResponse(LichSuCauHoi entity) {
        if (entity == null) return null;
        return LichSuCauHoiResponse.builder()
            .id(entity.getId())
            .cauHoiGocId(entity.getCauHoiGocId())
            .cauHoiVersionId(entity.getCauHoiVersionId())
            .hanhDong(entity.getHanhDong())
            .noiDungCu(entity.getNoiDungCu())
            .noiDungMoi(entity.getNoiDungMoi())
            .nguoiThucHienId(entity.getNguoiThucHienId())
            .thoiGian(entity.getThoiGian())
            .ghiChu(entity.getGhiChu())
            .build();
    }

    public static LichSuCauHoi toEntity(LichSuCauHoiRequest request) {
        if (request == null) return null;
        return LichSuCauHoi.builder()
            .cauHoiGocId(request.getCauHoiGocId())
            .cauHoiVersionId(request.getCauHoiVersionId())
            .hanhDong(request.getHanhDong())
            .noiDungCu(request.getNoiDungCu())
            .noiDungMoi(request.getNoiDungMoi())
            .nguoiThucHienId(request.getNguoiThucHienId())
            .ghiChu(request.getGhiChu())
            .build();
    }

    public static void updateEntity(LichSuCauHoi entity, LichSuCauHoiRequest request) {
        if (entity == null || request == null) return;
        entity.setCauHoiGocId(request.getCauHoiGocId());
        entity.setCauHoiVersionId(request.getCauHoiVersionId());
        entity.setHanhDong(request.getHanhDong());
        entity.setNoiDungCu(request.getNoiDungCu());
        entity.setNoiDungMoi(request.getNoiDungMoi());
        entity.setNguoiThucHienId(request.getNguoiThucHienId());
        entity.setGhiChu(request.getGhiChu());
    }
}
