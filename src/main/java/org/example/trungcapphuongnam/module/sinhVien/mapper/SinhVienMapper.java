package org.example.trungcapphuongnam.module.sinhVien.mapper;

import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienResponse;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.springframework.stereotype.Component;

@Component
public class SinhVienMapper {

    public SinhVien toEntity(SinhVienRequest request) {
        if (request == null) return null;
        return SinhVien.builder()
                .maSinhVien(request.getMaSinhVien())
                .hoTen(request.getHoTen())
                .ngaySinh(request.getNgaySinh())
                .gioiTinh(request.getGioiTinh())
                .email(request.getEmail())
                .soDienThoai(request.getSoDienThoai())
                .diaChi(request.getDiaChi())
                .trangThai(request.getTrangThai())
                .taiKhoanId(request.getTaiKhoanId())
                .build();
    }

    public void updateEntity(SinhVien entity, SinhVienRequest request) {
        entity.setMaSinhVien(request.getMaSinhVien());
        entity.setHoTen(request.getHoTen());
        entity.setNgaySinh(request.getNgaySinh());
        entity.setGioiTinh(request.getGioiTinh());
        entity.setEmail(request.getEmail());
        entity.setSoDienThoai(request.getSoDienThoai());
        entity.setDiaChi(request.getDiaChi());
        entity.setTrangThai(request.getTrangThai());
        entity.setTaiKhoanId(request.getTaiKhoanId());
    }

    public SinhVienResponse toResponse(SinhVien entity) {
        if (entity == null) return null;
        return SinhVienResponse.builder()
                .id(entity.getId())
                .maSinhVien(entity.getMaSinhVien())
                .hoTen(entity.getHoTen())
                .ngaySinh(entity.getNgaySinh())
                .gioiTinh(entity.getGioiTinh())
                .email(entity.getEmail())
                .soDienThoai(entity.getSoDienThoai())
                .diaChi(entity.getDiaChi())
                .trangThai(entity.getTrangThai())
                .taiKhoanId(entity.getTaiKhoanId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
