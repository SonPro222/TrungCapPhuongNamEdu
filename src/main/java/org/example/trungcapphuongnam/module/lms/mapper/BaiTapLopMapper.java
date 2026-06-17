package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.BaiTapLopRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.BaiTapLopResponse;
import org.example.trungcapphuongnam.module.lms.entity.BaiTapLop;

public final class BaiTapLopMapper {
    private BaiTapLopMapper() {}

    public static BaiTapLopResponse toResponse(BaiTapLop entity) {
        if (entity == null) return null;
        return BaiTapLopResponse.builder()
            .id(entity.getId())
            .dotGiaoBaiOnlineId(entity.getDotGiaoBaiOnlineId())
            .baiTapOnlineId(entity.getBaiTapOnlineId())
            .lopHocPhanId(entity.getLopHocPhanId())
            .cauHinhDanhGiaId(entity.getCauHinhDanhGiaId())
            .nguoiGiaoTaiKhoanId(entity.getNguoiGiaoTaiKhoanId())
            .thoiGianMo(entity.getThoiGianMo())
            .thoiGianDong(entity.getThoiGianDong())
            .thoiLuongLamBaiPhut(entity.getThoiLuongLamBaiPhut())
            .soLanLamToiDa(entity.getSoLanLamToiDa())
            .cachTinhDiem(entity.getCachTinhDiem())
            .choPhepXemDiem(entity.getChoPhepXemDiem())
            .choPhepXemDapAn(entity.getChoPhepXemDapAn())
            .tronCauHoi(entity.getTronCauHoi())
            .tronDapAn(entity.getTronDapAn())
            .choPhepLamQuaHan(entity.getChoPhepLamQuaHan())
            .giaoVienDuocChinhSua(entity.getGiaoVienDuocChinhSua())
            .trangThai(entity.getTrangThai())
            .ghiChu(entity.getGhiChu())
            .diemToiDa(entity.getDiemToiDa())
            .yeuCauToanManHinh(entity.getYeuCauToanManHinh())
            .gioiHanRoiTab(entity.getGioiHanRoiTab())
            .tuDongNopKhiViPham(entity.getTuDongNopKhiViPham())
            .matKhauLamBai(entity.getMatKhauLamBai())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    public static BaiTapLop toEntity(BaiTapLopRequest request) {
        if (request == null) return null;
        return BaiTapLop.builder()
            .dotGiaoBaiOnlineId(request.getDotGiaoBaiOnlineId())
            .baiTapOnlineId(request.getBaiTapOnlineId())
            .lopHocPhanId(request.getLopHocPhanId())
            .cauHinhDanhGiaId(request.getCauHinhDanhGiaId())
            .nguoiGiaoTaiKhoanId(request.getNguoiGiaoTaiKhoanId())
            .thoiGianMo(request.getThoiGianMo())
            .thoiGianDong(request.getThoiGianDong())
            .thoiLuongLamBaiPhut(request.getThoiLuongLamBaiPhut())
            .soLanLamToiDa(request.getSoLanLamToiDa())
            .cachTinhDiem(request.getCachTinhDiem())
            .choPhepXemDiem(request.getChoPhepXemDiem())
            .choPhepXemDapAn(request.getChoPhepXemDapAn())
            .tronCauHoi(request.getTronCauHoi())
            .tronDapAn(request.getTronDapAn())
            .choPhepLamQuaHan(request.getChoPhepLamQuaHan())
            .giaoVienDuocChinhSua(request.getGiaoVienDuocChinhSua())
            .trangThai(request.getTrangThai())
            .ghiChu(request.getGhiChu())
            .diemToiDa(request.getDiemToiDa())
            .yeuCauToanManHinh(request.getYeuCauToanManHinh())
            .gioiHanRoiTab(request.getGioiHanRoiTab())
            .tuDongNopKhiViPham(request.getTuDongNopKhiViPham())
            .matKhauLamBai(request.getMatKhauLamBai())
            .build();
    }

    public static void updateEntity(BaiTapLop entity, BaiTapLopRequest request) {
        if (entity == null || request == null) return;
        entity.setDotGiaoBaiOnlineId(request.getDotGiaoBaiOnlineId());
        entity.setBaiTapOnlineId(request.getBaiTapOnlineId());
        entity.setLopHocPhanId(request.getLopHocPhanId());
        entity.setCauHinhDanhGiaId(request.getCauHinhDanhGiaId());
        entity.setNguoiGiaoTaiKhoanId(request.getNguoiGiaoTaiKhoanId());
        entity.setThoiGianMo(request.getThoiGianMo());
        entity.setThoiGianDong(request.getThoiGianDong());
        entity.setThoiLuongLamBaiPhut(request.getThoiLuongLamBaiPhut());
        entity.setSoLanLamToiDa(request.getSoLanLamToiDa());
        entity.setCachTinhDiem(request.getCachTinhDiem());
        entity.setChoPhepXemDiem(request.getChoPhepXemDiem());
        entity.setChoPhepXemDapAn(request.getChoPhepXemDapAn());
        entity.setTronCauHoi(request.getTronCauHoi());
        entity.setTronDapAn(request.getTronDapAn());
        entity.setChoPhepLamQuaHan(request.getChoPhepLamQuaHan());
        entity.setGiaoVienDuocChinhSua(request.getGiaoVienDuocChinhSua());
        entity.setTrangThai(request.getTrangThai());
        entity.setGhiChu(request.getGhiChu());
        entity.setDiemToiDa(request.getDiemToiDa());
        entity.setYeuCauToanManHinh(request.getYeuCauToanManHinh());
        entity.setGioiHanRoiTab(request.getGioiHanRoiTab());
        entity.setTuDongNopKhiViPham(request.getTuDongNopKhiViPham());
        entity.setMatKhauLamBai(request.getMatKhauLamBai());
    }
}
