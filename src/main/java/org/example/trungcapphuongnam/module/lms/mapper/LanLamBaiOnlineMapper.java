package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.LanLamBaiOnlineRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LanLamBaiOnlineResponse;
import org.example.trungcapphuongnam.module.lms.entity.LanLamBaiOnline;

public final class LanLamBaiOnlineMapper {
    private LanLamBaiOnlineMapper() {}

    public static LanLamBaiOnlineResponse toResponse(LanLamBaiOnline entity) {
        if (entity == null) return null;
        return LanLamBaiOnlineResponse.builder()
            .id(entity.getId())
            .baiTapLopId(entity.getBaiTapLopId())
            .sinhVienId(entity.getSinhVienId())
            .lanThu(entity.getLanThu())
            .thoiGianBatDau(entity.getThoiGianBatDau())
            .thoiGianNop(entity.getThoiGianNop())
            .diemTuDong(entity.getDiemTuDong())
            .diemGiaoVienCham(entity.getDiemGiaoVienCham())
            .diemCuoiCung(entity.getDiemCuoiCung())
            .soCauDung(entity.getSoCauDung())
            .soCauSai(entity.getSoCauSai())
            .tongSoCau(entity.getTongSoCau())
            .trangThai(entity.getTrangThai())
            .ghiChu(entity.getGhiChu())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    public static LanLamBaiOnline toEntity(LanLamBaiOnlineRequest request) {
        if (request == null) return null;
        return LanLamBaiOnline.builder()
            .baiTapLopId(request.getBaiTapLopId())
            .sinhVienId(request.getSinhVienId())
            .lanThu(request.getLanThu())
            .thoiGianBatDau(request.getThoiGianBatDau())
            .thoiGianNop(request.getThoiGianNop())
            .diemTuDong(request.getDiemTuDong())
            .diemGiaoVienCham(request.getDiemGiaoVienCham())
            .diemCuoiCung(request.getDiemCuoiCung())
            .soCauDung(request.getSoCauDung())
            .soCauSai(request.getSoCauSai())
            .tongSoCau(request.getTongSoCau())
            .trangThai(request.getTrangThai())
            .ghiChu(request.getGhiChu())
            .build();
    }

    public static void updateEntity(LanLamBaiOnline entity, LanLamBaiOnlineRequest request) {
        if (entity == null || request == null) return;
        entity.setBaiTapLopId(request.getBaiTapLopId());
        entity.setSinhVienId(request.getSinhVienId());
        entity.setLanThu(request.getLanThu());
        entity.setThoiGianBatDau(request.getThoiGianBatDau());
        entity.setThoiGianNop(request.getThoiGianNop());
        entity.setDiemTuDong(request.getDiemTuDong());
        entity.setDiemGiaoVienCham(request.getDiemGiaoVienCham());
        entity.setDiemCuoiCung(request.getDiemCuoiCung());
        entity.setSoCauDung(request.getSoCauDung());
        entity.setSoCauSai(request.getSoCauSai());
        entity.setTongSoCau(request.getTongSoCau());
        entity.setTrangThai(request.getTrangThai());
        entity.setGhiChu(request.getGhiChu());
    }
}
