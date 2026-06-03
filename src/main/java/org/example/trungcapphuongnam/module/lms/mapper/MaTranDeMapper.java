package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.MaTranDeRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.MaTranDeResponse;
import org.example.trungcapphuongnam.module.lms.entity.MaTranDe;

public final class MaTranDeMapper {
    private MaTranDeMapper() {}

    public static MaTranDeResponse toResponse(MaTranDe entity) {
        if (entity == null) return null;
        return MaTranDeResponse.builder()
            .id(entity.getId())
            .tenMaTran(entity.getTenMaTran())
            .chuongTrinhMonId(entity.getChuongTrinhMonId())
            .tongDiem(entity.getTongDiem())
            .thoiGianLamBai(entity.getThoiGianLamBai())
            .trangThai(entity.getTrangThai())
            .build();
    }

    public static MaTranDe toEntity(MaTranDeRequest request) {
        if (request == null) return null;
        return MaTranDe.builder()
            .tenMaTran(request.getTenMaTran())
            .chuongTrinhMonId(request.getChuongTrinhMonId())
            .tongDiem(request.getTongDiem())
            .thoiGianLamBai(request.getThoiGianLamBai())
            .trangThai(request.getTrangThai())
            .build();
    }

    public static void updateEntity(MaTranDe entity, MaTranDeRequest request) {
        if (entity == null || request == null) return;
        entity.setTenMaTran(request.getTenMaTran());
        entity.setChuongTrinhMonId(request.getChuongTrinhMonId());
        entity.setTongDiem(request.getTongDiem());
        entity.setThoiGianLamBai(request.getThoiGianLamBai());
        entity.setTrangThai(request.getTrangThai());
    }
}
