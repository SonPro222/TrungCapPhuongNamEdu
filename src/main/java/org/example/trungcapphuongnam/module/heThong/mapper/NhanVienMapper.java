package org.example.trungcapphuongnam.module.heThong.mapper;

import org.example.trungcapphuongnam.module.heThong.dto.request.NhanVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.NhanVienResponse;
import org.example.trungcapphuongnam.module.heThong.entity.NhanVien;
import org.springframework.stereotype.Component;

@Component
public class NhanVienMapper {

    public NhanVienResponse toResponse(NhanVien entity) {
        if (entity == null) {
            return null;
        }

        return NhanVienResponse.builder()
                .id(entity.getId())
                .taiKhoanId(entity.getTaiKhoan() == null ? null : entity.getTaiKhoan().getId())
                .maNhanVien(entity.getMaNhanVien())
                .hoTen(entity.getHoTen())
                .email(entity.getEmail())
                .soDienThoai(entity.getSoDienThoai())
                .phongBan(entity.getPhongBan())
                .chucVu(entity.getChucVu())
                .trangThai(entity.getTrangThai())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public NhanVien toEntity(NhanVienRequest request) {
        if (request == null) {
            return null;
        }

        NhanVien entity = new NhanVien();
        entity.setMaNhanVien(chuanHoa(request.getMaNhanVien()));
        entity.setHoTen(chuanHoa(request.getHoTen()));
        entity.setEmail(chuanHoaEmail(request.getEmail()));
        entity.setSoDienThoai(chuanHoa(request.getSoDienThoai()));
        entity.setPhongBan(chuanHoa(request.getPhongBan()));
        entity.setChucVu(chuanHoa(request.getChucVu()));
        entity.setTrangThai(request.getTrangThai());

        return entity;
    }

    public void updateEntity(NhanVien entity, NhanVienRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setMaNhanVien(chuanHoa(request.getMaNhanVien()));
        entity.setHoTen(chuanHoa(request.getHoTen()));
        entity.setEmail(chuanHoaEmail(request.getEmail()));
        entity.setSoDienThoai(chuanHoa(request.getSoDienThoai()));
        entity.setPhongBan(chuanHoa(request.getPhongBan()));
        entity.setChucVu(chuanHoa(request.getChucVu()));
        entity.setTrangThai(request.getTrangThai());
    }

    private String chuanHoa(String value) {
        return value == null ? null : value.trim();
    }

    private String chuanHoaEmail(String value) {
        return value == null ? null : value.trim().toLowerCase();
    }
}