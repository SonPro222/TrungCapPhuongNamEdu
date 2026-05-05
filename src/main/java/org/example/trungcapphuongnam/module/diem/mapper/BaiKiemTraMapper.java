package org.example.trungcapphuongnam.module.diem.mapper;

import org.example.trungcapphuongnam.module.diem.dto.request.BaiKiemTraRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.BaiKiemTraResponse;
import org.example.trungcapphuongnam.module.diem.entity.BaiKiemTra;
import org.springframework.stereotype.Component;

@Component
public class BaiKiemTraMapper {

    public BaiKiemTra toEntity(BaiKiemTraRequest request) {
        if (request == null) return null;
        return BaiKiemTra.builder()
                .lopHocPhanId(request.getLopHocPhanId())
                .cauHinhDanhGiaId(request.getCauHinhDanhGiaId())
                .giaoVienId(request.getGiaoVienId())
                .tieuDe(request.getTieuDe())
                .loaiBai(request.getLoaiBai())
                .moTa(request.getMoTa())
                .noiDungDe(request.getNoiDungDe())
                .thoiGianBatDau(request.getThoiGianBatDau())
                .thoiGianKetThuc(request.getThoiGianKetThuc())
                .hinhThuc(request.getHinhThuc())
                .trangThai(request.getTrangThai())
                .build();
    }

    public BaiKiemTraResponse toResponse(BaiKiemTra entity) {
        if (entity == null) return null;
        return BaiKiemTraResponse.builder()
                .id(entity.getId())
                .lopHocPhanId(entity.getLopHocPhanId())
                .cauHinhDanhGiaId(entity.getCauHinhDanhGiaId())
                .giaoVienId(entity.getGiaoVienId())
                .tieuDe(entity.getTieuDe())
                .loaiBai(entity.getLoaiBai())
                .moTa(entity.getMoTa())
                .noiDungDe(entity.getNoiDungDe())
                .thoiGianBatDau(entity.getThoiGianBatDau())
                .thoiGianKetThuc(entity.getThoiGianKetThuc())
                .hinhThuc(entity.getHinhThuc())
                .trangThai(entity.getTrangThai())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(BaiKiemTra entity, BaiKiemTraRequest request) {
        if (entity == null || request == null) return;
        entity.setLopHocPhanId(request.getLopHocPhanId());
        entity.setCauHinhDanhGiaId(request.getCauHinhDanhGiaId());
        entity.setGiaoVienId(request.getGiaoVienId());
        entity.setTieuDe(request.getTieuDe());
        entity.setLoaiBai(request.getLoaiBai());
        entity.setMoTa(request.getMoTa());
        entity.setNoiDungDe(request.getNoiDungDe());
        entity.setThoiGianBatDau(request.getThoiGianBatDau());
        entity.setThoiGianKetThuc(request.getThoiGianKetThuc());
        entity.setHinhThuc(request.getHinhThuc());
        entity.setTrangThai(request.getTrangThai());
    }
}
