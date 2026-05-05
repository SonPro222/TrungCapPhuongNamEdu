package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.LichSuDayDiemLmsRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LichSuDayDiemLmsResponse;
import org.example.trungcapphuongnam.module.lms.entity.LichSuDayDiemLms;

public final class LichSuDayDiemLmsMapper {
    private LichSuDayDiemLmsMapper() {}

    public static LichSuDayDiemLmsResponse toResponse(LichSuDayDiemLms entity) {
        if (entity == null) return null;
        return LichSuDayDiemLmsResponse.builder()
            .id(entity.getId())
            .ketQuaBaiTapLopId(entity.getKetQuaBaiTapLopId())
            .diemChiTietId(entity.getDiemChiTietId())
            .nguoiThucHienId(entity.getNguoiThucHienId())
            .diemCu(entity.getDiemCu())
            .diemMoi(entity.getDiemMoi())
            .hanhDong(entity.getHanhDong())
            .ghiChu(entity.getGhiChu())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    public static LichSuDayDiemLms toEntity(LichSuDayDiemLmsRequest request) {
        if (request == null) return null;
        return LichSuDayDiemLms.builder()
            .ketQuaBaiTapLopId(request.getKetQuaBaiTapLopId())
            .diemChiTietId(request.getDiemChiTietId())
            .nguoiThucHienId(request.getNguoiThucHienId())
            .diemCu(request.getDiemCu())
            .diemMoi(request.getDiemMoi())
            .hanhDong(request.getHanhDong())
            .ghiChu(request.getGhiChu())
            .build();
    }

    public static void updateEntity(LichSuDayDiemLms entity, LichSuDayDiemLmsRequest request) {
        if (entity == null || request == null) return;
        entity.setKetQuaBaiTapLopId(request.getKetQuaBaiTapLopId());
        entity.setDiemChiTietId(request.getDiemChiTietId());
        entity.setNguoiThucHienId(request.getNguoiThucHienId());
        entity.setDiemCu(request.getDiemCu());
        entity.setDiemMoi(request.getDiemMoi());
        entity.setHanhDong(request.getHanhDong());
        entity.setGhiChu(request.getGhiChu());
    }
}
