package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.BaiTapOnlineCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.BaiTapOnlineCauHoiResponse;
import org.example.trungcapphuongnam.module.lms.entity.BaiTapOnlineCauHoi;

public final class BaiTapOnlineCauHoiMapper {
    private BaiTapOnlineCauHoiMapper() {}

    public static BaiTapOnlineCauHoiResponse toResponse(BaiTapOnlineCauHoi entity) {
        if (entity == null) return null;
        return BaiTapOnlineCauHoiResponse.builder()
            .id(entity.getId())
            .baiTapOnlineId(entity.getBaiTapOnlineId())
            .cauHoiId(entity.getCauHoiId())
            .diem(entity.getDiem())
            .thuTu(entity.getThuTu())
            .batBuoc(entity.getBatBuoc())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    public static BaiTapOnlineCauHoi toEntity(BaiTapOnlineCauHoiRequest request) {
        if (request == null) return null;
        return BaiTapOnlineCauHoi.builder()
            .baiTapOnlineId(request.getBaiTapOnlineId())
            .cauHoiId(request.getCauHoiId())
            .diem(request.getDiem())
            .thuTu(request.getThuTu())
            .batBuoc(request.getBatBuoc())
            .build();
    }

    public static void updateEntity(BaiTapOnlineCauHoi entity, BaiTapOnlineCauHoiRequest request) {
        if (entity == null || request == null) return;
        entity.setBaiTapOnlineId(request.getBaiTapOnlineId());
        entity.setCauHoiId(request.getCauHoiId());
        entity.setDiem(request.getDiem());
        entity.setThuTu(request.getThuTu());
        entity.setBatBuoc(request.getBatBuoc());
    }
}
