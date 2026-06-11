package org.example.trungcapphuongnam.module.giangDay.mapper;

import org.example.trungcapphuongnam.module.giangDay.dto.request.GiaoVienRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiaoVienResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVien;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.enums.LoaiTaiKhoan;
import org.example.trungcapphuongnam.module.heThong.enums.TrangThaiTaiKhoan;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;

@Component
public class GiaoVienMapper {

    public GiaoVien toEntity(GiaoVienRequest request) {
        if (request == null) return null;

        return GiaoVien.builder()
                .maGiaoVien(chuanHoa(request.getMaGiaoVien()))
                .hoTen(chuanHoa(request.getHoTen()))
                .email(chuanHoaEmail(request.getEmail()))
                .soDienThoai(chuanHoa(request.getSoDienThoai()))
                .chuyenMon(chuanHoa(request.getChuyenMon()))
                .trangThai(request.getTrangThai())
                .taiKhoanId(request.getTaiKhoanId())
                .build();
    }


    public TaiKhoan toTaiKhoanGiaoVien(String email, String matKhauHash) {
        return TaiKhoan.builder()
                .email(chuanHoaEmail(email))
                .matKhauHash(matKhauHash)
                .loaiTaiKhoan(LoaiTaiKhoan.giao_vien)
                .trangThai(TrangThaiTaiKhoan.da_kich_hoat)
                .taiKhoanVaiTros(new LinkedHashSet<>())
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

        entity.setMaGiaoVien(chuanHoa(request.getMaGiaoVien()));
        entity.setHoTen(chuanHoa(request.getHoTen()));
        entity.setEmail(chuanHoaEmail(request.getEmail()));
        entity.setSoDienThoai(chuanHoa(request.getSoDienThoai()));
        entity.setChuyenMon(chuanHoa(request.getChuyenMon()));
        entity.setTrangThai(request.getTrangThai());
    }

    private String chuanHoa(String value) {
        return value == null ? null : value.trim();
    }

    private String chuanHoaEmail(String value) {
        return value == null ? null : value.trim().toLowerCase();
    }
}