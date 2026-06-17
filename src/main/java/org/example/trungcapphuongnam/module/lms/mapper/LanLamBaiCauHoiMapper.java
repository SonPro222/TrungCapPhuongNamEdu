package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.LanLamBaiCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LanLamBaiCauHoiResponse;
import org.example.trungcapphuongnam.module.lms.entity.LanLamBaiCauHoi;

public final class LanLamBaiCauHoiMapper {
    private LanLamBaiCauHoiMapper() {}

    public static LanLamBaiCauHoiResponse toResponse(LanLamBaiCauHoi entity) {
        if (entity == null) return null;
        return LanLamBaiCauHoiResponse.builder()
            .id(entity.getId())
            .lanLamBaiId(entity.getLanLamBaiId())
            .cauHoiVersionId(entity.getCauHoiVersionId())
            .noiDungSnapshot(entity.getNoiDungSnapshot())
            .loaiCauHoiSnapshot(entity.getLoaiCauHoiSnapshot())
            .diemSnapshot(entity.getDiemSnapshot())
            .thuTu(entity.getThuTu())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    public static LanLamBaiCauHoi toEntity(LanLamBaiCauHoiRequest request) {
        if (request == null) return null;
        return LanLamBaiCauHoi.builder()
            .lanLamBaiId(request.getLanLamBaiId())
            .cauHoiVersionId(request.getCauHoiVersionId())
            .noiDungSnapshot(request.getNoiDungSnapshot())
            .loaiCauHoiSnapshot(request.getLoaiCauHoiSnapshot())
            .diemSnapshot(request.getDiemSnapshot())
            .thuTu(request.getThuTu())
            .build();
    }

    public static void updateEntity(LanLamBaiCauHoi entity, LanLamBaiCauHoiRequest request) {
        if (entity == null || request == null) return;
        entity.setLanLamBaiId(request.getLanLamBaiId());
        entity.setCauHoiVersionId(request.getCauHoiVersionId());
        entity.setNoiDungSnapshot(request.getNoiDungSnapshot());
        entity.setLoaiCauHoiSnapshot(request.getLoaiCauHoiSnapshot());
        entity.setDiemSnapshot(request.getDiemSnapshot());
        entity.setThuTu(request.getThuTu());
    }
}
