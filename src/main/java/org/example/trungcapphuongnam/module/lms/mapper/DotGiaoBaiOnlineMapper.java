package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.DotGiaoBaiOnlineRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.DotGiaoBaiOnlineResponse;
import org.example.trungcapphuongnam.module.lms.entity.DotGiaoBaiOnline;

public final class DotGiaoBaiOnlineMapper {
    private DotGiaoBaiOnlineMapper() {}

    public static DotGiaoBaiOnlineResponse toResponse(DotGiaoBaiOnline entity) {
        if (entity == null) return null;
        return DotGiaoBaiOnlineResponse.builder()
            .id(entity.getId())
            .baiTapOnlineId(entity.getBaiTapOnlineId())
            .nguoiGiaoTaiKhoanId(entity.getNguoiGiaoTaiKhoanId())
            .nguonGiao(entity.getNguonGiao())
            .phamVi(entity.getPhamVi())
            .nganhId(entity.getNganhId())
            .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
            .ghiChu(entity.getGhiChu())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    public static DotGiaoBaiOnline toEntity(DotGiaoBaiOnlineRequest request) {
        if (request == null) return null;
        return DotGiaoBaiOnline.builder()
            .baiTapOnlineId(request.getBaiTapOnlineId())
            .nguoiGiaoTaiKhoanId(request.getNguoiGiaoTaiKhoanId())
            .nguonGiao(request.getNguonGiao())
            .phamVi(request.getPhamVi())
            .nganhId(request.getNganhId())
            .chuongTrinhVersionId(request.getChuongTrinhVersionId())
            .ghiChu(request.getGhiChu())
            .build();
    }

    public static void updateEntity(DotGiaoBaiOnline entity, DotGiaoBaiOnlineRequest request) {
        if (entity == null || request == null) return;
        entity.setBaiTapOnlineId(request.getBaiTapOnlineId());
        entity.setNguoiGiaoTaiKhoanId(request.getNguoiGiaoTaiKhoanId());
        entity.setNguonGiao(request.getNguonGiao());
        entity.setPhamVi(request.getPhamVi());
        entity.setNganhId(request.getNganhId());
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setGhiChu(request.getGhiChu());
    }
}
