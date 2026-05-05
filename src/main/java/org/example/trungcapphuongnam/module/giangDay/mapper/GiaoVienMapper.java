package org.example.trungcapphuongnam.module.giangDay.mapper;

import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVien;
import org.example.trungcapphuongnam.module.giangDay.dto.request.GiaoVienRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiaoVienResponse;
import org.springframework.stereotype.Component;

@Component
public class GiaoVienMapper {

    public GiaoVien toEntity(GiaoVienRequest request) {
        if (request == null) return null;
        return GiaoVien.builder()
                .maGiaoVien(request.getMaGiaoVien())
                .hoTen(request.getHoTen())
                .email(request.getEmail())
                .soDienThoai(request.getSoDienThoai())
                .chuyenMon(request.getChuyenMon())
                .trangThai(request.getTrangThai())
                .taiKhoanId(request.getTaiKhoanId())
                .build();
    }

    public GiaoVienResponse toResponse(GiaoVien entity) {
        if (entity == null) return null;
        return GiaoVienResponse.builder()
                .id(entity.getId())
                .maGiaoVien(entity.getMaGiaoVien())
                .hoTen(entity.getHoTen())
                .email(entity.getEmail())
                .soDienThoai(entity.getSoDienThoai())
                .chuyenMon(entity.getChuyenMon())
                .trangThai(entity.getTrangThai())
                .taiKhoanId(entity.getTaiKhoanId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(GiaoVien entity, GiaoVienRequest request) {
        if (entity == null || request == null) return;
        entity.setMaGiaoVien(request.getMaGiaoVien());
        entity.setHoTen(request.getHoTen());
        entity.setEmail(request.getEmail());
        entity.setSoDienThoai(request.getSoDienThoai());
        entity.setChuyenMon(request.getChuyenMon());
        entity.setTrangThai(request.getTrangThai());
        entity.setTaiKhoanId(request.getTaiKhoanId());
    }
}
