package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.LichSuChinhDiemBaiTapRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LichSuChinhDiemBaiTapResponse;
import org.example.trungcapphuongnam.module.lms.entity.LichSuChinhDiemBaiTap;

public final class LichSuChinhDiemBaiTapMapper {
    private LichSuChinhDiemBaiTapMapper() {}

    public static LichSuChinhDiemBaiTapResponse toResponse(LichSuChinhDiemBaiTap entity) {
        if (entity == null) return null;
        return LichSuChinhDiemBaiTapResponse.builder()
            .id(entity.getId())
            .ketQuaBaiTapLopId(entity.getKetQuaBaiTapLopId())
            .nguoiChinhId(entity.getNguoiChinhId())
            .vaiTroChinh(entity.getVaiTroChinh())
            .diemCu(entity.getDiemCu())
            .diemMoi(entity.getDiemMoi())
            .lyDo(entity.getLyDo())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    public static LichSuChinhDiemBaiTap toEntity(LichSuChinhDiemBaiTapRequest request) {
        if (request == null) return null;
        return LichSuChinhDiemBaiTap.builder()
            .ketQuaBaiTapLopId(request.getKetQuaBaiTapLopId())
            .nguoiChinhId(request.getNguoiChinhId())
            .vaiTroChinh(request.getVaiTroChinh())
            .diemCu(request.getDiemCu())
            .diemMoi(request.getDiemMoi())
            .lyDo(request.getLyDo())
            .build();
    }

    public static void updateEntity(LichSuChinhDiemBaiTap entity, LichSuChinhDiemBaiTapRequest request) {
        if (entity == null || request == null) return;
        entity.setKetQuaBaiTapLopId(request.getKetQuaBaiTapLopId());
        entity.setNguoiChinhId(request.getNguoiChinhId());
        entity.setVaiTroChinh(request.getVaiTroChinh());
        entity.setDiemCu(request.getDiemCu());
        entity.setDiemMoi(request.getDiemMoi());
        entity.setLyDo(request.getLyDo());
    }
}
