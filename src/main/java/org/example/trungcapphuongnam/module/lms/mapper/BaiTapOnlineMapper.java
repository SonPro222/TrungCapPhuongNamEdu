package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.BaiTapOnlineRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.BaiTapOnlineResponse;
import org.example.trungcapphuongnam.module.lms.entity.BaiTapOnline;

public final class BaiTapOnlineMapper {
    private BaiTapOnlineMapper() {}

    public static BaiTapOnlineResponse toResponse(BaiTapOnline entity) {
        if (entity == null) return null;
        return BaiTapOnlineResponse.builder()
            .id(entity.getId())
            .chuongTrinhMonId(entity.getChuongTrinhMonId())
            .nguoiTaoTaiKhoanId(entity.getNguoiTaoTaiKhoanId())
            .nguonTao(entity.getNguonTao())
            .tieuDe(entity.getTieuDe())
            .moTa(entity.getMoTa())
            .loaiBai(entity.getLoaiBai())
            .tongDiem(entity.getTongDiem())
            .trangThai(entity.getTrangThai())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    public static BaiTapOnline toEntity(BaiTapOnlineRequest request) {
        if (request == null) return null;
        return BaiTapOnline.builder()
            .chuongTrinhMonId(request.getChuongTrinhMonId())
            .nguoiTaoTaiKhoanId(request.getNguoiTaoTaiKhoanId())
            .nguonTao(request.getNguonTao())
            .tieuDe(request.getTieuDe())
            .moTa(request.getMoTa())
            .loaiBai(request.getLoaiBai())
            .tongDiem(request.getTongDiem())
            .trangThai(request.getTrangThai())
            .build();
    }

    public static void updateEntity(BaiTapOnline entity, BaiTapOnlineRequest request) {
        if (entity == null || request == null) return;
        entity.setChuongTrinhMonId(request.getChuongTrinhMonId());
        entity.setNguoiTaoTaiKhoanId(request.getNguoiTaoTaiKhoanId());
        entity.setNguonTao(request.getNguonTao());
        entity.setTieuDe(request.getTieuDe());
        entity.setMoTa(request.getMoTa());
        entity.setLoaiBai(request.getLoaiBai());
        entity.setTongDiem(request.getTongDiem());
        entity.setTrangThai(request.getTrangThai());
    }
}
