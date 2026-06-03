package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.BaiNopLmsRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.BaiNopLmsResponse;
import org.example.trungcapphuongnam.module.lms.entity.BaiNopLms;

public final class BaiNopLmsMapper {
    private BaiNopLmsMapper() {}

    public static BaiNopLmsResponse toResponse(BaiNopLms entity) {
        if (entity == null) return null;
        return BaiNopLmsResponse.builder()
            .id(entity.getId())
            .baiTapLopId(entity.getBaiTapLopId())
            .sinhVienId(entity.getSinhVienId())
            .lanNop(entity.getLanNop())
            .noiDungBaiLam(entity.getNoiDungBaiLam())
            .thoiGianNop(entity.getThoiGianNop())
            .trangThai(entity.getTrangThai())
            .diem(entity.getDiem())
            .nhanXet(entity.getNhanXet())
            .nguoiChamId(entity.getNguoiChamId())
            .thoiGianCham(entity.getThoiGianCham())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    public static BaiNopLms toEntity(BaiNopLmsRequest request) {
        if (request == null) return null;
        return BaiNopLms.builder()
            .baiTapLopId(request.getBaiTapLopId())
            .sinhVienId(request.getSinhVienId())
            .lanNop(request.getLanNop())
            .noiDungBaiLam(request.getNoiDungBaiLam())
            .thoiGianNop(request.getThoiGianNop())
            .trangThai(request.getTrangThai())
            .diem(request.getDiem())
            .nhanXet(request.getNhanXet())
            .nguoiChamId(request.getNguoiChamId())
            .thoiGianCham(request.getThoiGianCham())
            .build();
    }

    public static void updateEntity(BaiNopLms entity, BaiNopLmsRequest request) {
        if (entity == null || request == null) return;
        entity.setBaiTapLopId(request.getBaiTapLopId());
        entity.setSinhVienId(request.getSinhVienId());
        entity.setLanNop(request.getLanNop());
        entity.setNoiDungBaiLam(request.getNoiDungBaiLam());
        entity.setThoiGianNop(request.getThoiGianNop());
        entity.setTrangThai(request.getTrangThai());
        entity.setDiem(request.getDiem());
        entity.setNhanXet(request.getNhanXet());
        entity.setNguoiChamId(request.getNguoiChamId());
        entity.setThoiGianCham(request.getThoiGianCham());
    }
}
