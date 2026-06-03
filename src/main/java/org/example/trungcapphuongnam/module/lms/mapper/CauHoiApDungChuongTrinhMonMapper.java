package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.CauHoiApDungChuongTrinhMonRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauHoiApDungChuongTrinhMonResponse;
import org.example.trungcapphuongnam.module.lms.entity.CauHoiApDungChuongTrinhMon;

public final class CauHoiApDungChuongTrinhMonMapper {
    private CauHoiApDungChuongTrinhMonMapper() {}

    public static CauHoiApDungChuongTrinhMonResponse toResponse(CauHoiApDungChuongTrinhMon entity) {
        if (entity == null) return null;
        return CauHoiApDungChuongTrinhMonResponse.builder()
            .id(entity.getId())
            .cauHoiVersionId(entity.getCauHoiVersionId())
            .chuongTrinhMonId(entity.getChuongTrinhMonId())
            .syllabusChuongBaiId(entity.getSyllabusChuongBaiId())
            .trangThai(entity.getTrangThai())
            .ghiChu(entity.getGhiChu())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    public static CauHoiApDungChuongTrinhMon toEntity(CauHoiApDungChuongTrinhMonRequest request) {
        if (request == null) return null;
        return CauHoiApDungChuongTrinhMon.builder()
            .cauHoiVersionId(request.getCauHoiVersionId())
            .chuongTrinhMonId(request.getChuongTrinhMonId())
            .syllabusChuongBaiId(request.getSyllabusChuongBaiId())
            .trangThai(request.getTrangThai())
            .ghiChu(request.getGhiChu())
            .build();
    }

    public static void updateEntity(CauHoiApDungChuongTrinhMon entity, CauHoiApDungChuongTrinhMonRequest request) {
        if (entity == null || request == null) return;
        entity.setCauHoiVersionId(request.getCauHoiVersionId());
        entity.setChuongTrinhMonId(request.getChuongTrinhMonId());
        entity.setSyllabusChuongBaiId(request.getSyllabusChuongBaiId());
        entity.setTrangThai(request.getTrangThai());
        entity.setGhiChu(request.getGhiChu());
    }
}
