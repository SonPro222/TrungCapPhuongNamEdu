package org.example.trungcapphuongnam.module.heThong.mapper;

import org.example.trungcapphuongnam.module.heThong.dto.request.NhatKyDangNhapRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.NhatKyDangNhapResponse;
import org.example.trungcapphuongnam.module.heThong.entity.NhatKyDangNhap;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.springframework.stereotype.Component;

@Component
public class NhatKyDangNhapMapper {

    public NhatKyDangNhapResponse toResponse(NhatKyDangNhap entity) {
        if (entity == null) {
            return null;
        }

        return NhatKyDangNhapResponse.builder()
                .id(entity.getId())
                .taiKhoanId(entity.getTaiKhoan() == null ? null : entity.getTaiKhoan().getId())
                .thoiGian(entity.getThoiGian())
                .ipAddress(entity.getIpAddress())
                .userAgent(entity.getUserAgent())
                .thanhCong(entity.getThanhCong())
                .lyDoThatBai(entity.getLyDoThatBai())
                .build();
    }

    public NhatKyDangNhap toEntity(NhatKyDangNhapRequest request) {
        if (request == null) {
            return null;
        }

        NhatKyDangNhap entity = new NhatKyDangNhap();

        if (request.getTaiKhoanId() != null) {
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setId(request.getTaiKhoanId());
            entity.setTaiKhoan(taiKhoan);
        }

        entity.setThoiGian(request.getThoiGian());
        entity.setIpAddress(request.getIpAddress());
        entity.setUserAgent(request.getUserAgent());
        entity.setThanhCong(request.getThanhCong());
        entity.setLyDoThatBai(request.getLyDoThatBai());

        return entity;
    }

    public void updateEntity(NhatKyDangNhap entity, NhatKyDangNhapRequest request) {
        if (entity == null || request == null) {
            return;
        }

        if (request.getTaiKhoanId() != null) {
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setId(request.getTaiKhoanId());
            entity.setTaiKhoan(taiKhoan);
        } else {
            entity.setTaiKhoan(null);
        }

        entity.setThoiGian(request.getThoiGian());
        entity.setIpAddress(request.getIpAddress());
        entity.setUserAgent(request.getUserAgent());
        entity.setThanhCong(request.getThanhCong());
        entity.setLyDoThatBai(request.getLyDoThatBai());
    }
}