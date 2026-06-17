package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.LanLamBaiDapAnRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LanLamBaiDapAnResponse;
import org.example.trungcapphuongnam.module.lms.entity.LanLamBaiDapAn;

public final class LanLamBaiDapAnMapper {
    private LanLamBaiDapAnMapper() {}

    public static LanLamBaiDapAnResponse toResponse(LanLamBaiDapAn entity) {
        if (entity == null) return null;
        return LanLamBaiDapAnResponse.builder()
            .id(entity.getId())
            .lanLamBaiCauHoiId(entity.getLanLamBaiCauHoiId())
            .dapAnCauHoiVersionId(entity.getDapAnCauHoiVersionId())
            .noiDungSnapshot(entity.getNoiDungSnapshot())
            .laDapAnDungSnapshot(entity.getLaDapAnDungSnapshot())
            .thuTu(entity.getThuTu())
            .build();
    }

    public static LanLamBaiDapAn toEntity(LanLamBaiDapAnRequest request) {
        if (request == null) return null;
        return LanLamBaiDapAn.builder()
            .lanLamBaiCauHoiId(request.getLanLamBaiCauHoiId())
            .dapAnCauHoiVersionId(request.getDapAnCauHoiVersionId())
            .noiDungSnapshot(request.getNoiDungSnapshot())
            .laDapAnDungSnapshot(request.getLaDapAnDungSnapshot())
            .thuTu(request.getThuTu())
            .build();
    }

    public static void updateEntity(LanLamBaiDapAn entity, LanLamBaiDapAnRequest request) {
        if (entity == null || request == null) return;
        entity.setLanLamBaiCauHoiId(request.getLanLamBaiCauHoiId());
        entity.setDapAnCauHoiVersionId(request.getDapAnCauHoiVersionId());
        entity.setNoiDungSnapshot(request.getNoiDungSnapshot());
        entity.setLaDapAnDungSnapshot(request.getLaDapAnDungSnapshot());
        entity.setThuTu(request.getThuTu());
    }
}
