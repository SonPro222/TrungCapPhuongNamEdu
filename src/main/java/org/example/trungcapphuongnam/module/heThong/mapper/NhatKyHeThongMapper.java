package org.example.trungcapphuongnam.module.heThong.mapper;

import org.example.trungcapphuongnam.module.heThong.dto.request.NhatKyHeThongRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.NhatKyHeThongResponse;
import org.example.trungcapphuongnam.module.heThong.entity.NhatKyHeThong;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.springframework.stereotype.Component;

@Component
public class NhatKyHeThongMapper {

    public NhatKyHeThongResponse toResponse(NhatKyHeThong entity) {
        if (entity == null) {
            return null;
        }

        return NhatKyHeThongResponse.builder()
                .id(entity.getId())
                .taiKhoanId(entity.getTaiKhoan() == null ? null : entity.getTaiKhoan().getId())
                .hanhDong(entity.getHanhDong())
                .bangTacDong(entity.getBangTacDong())
                .banGhiId(entity.getBanGhiId())
                .noiDungCu(entity.getNoiDungCu())
                .noiDungMoi(entity.getNoiDungMoi())
                .ipAddress(entity.getIpAddress())
                .userAgent(entity.getUserAgent())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public NhatKyHeThong toEntity(NhatKyHeThongRequest request) {
        if (request == null) {
            return null;
        }

        NhatKyHeThong entity = new NhatKyHeThong();

        if (request.getTaiKhoanId() != null) {
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setId(request.getTaiKhoanId());
            entity.setTaiKhoan(taiKhoan);
        }

        entity.setHanhDong(request.getHanhDong());
        entity.setBangTacDong(request.getBangTacDong());
        entity.setBanGhiId(request.getBanGhiId());
        entity.setNoiDungCu(request.getNoiDungCu());
        entity.setNoiDungMoi(request.getNoiDungMoi());
        entity.setIpAddress(request.getIpAddress());
        entity.setUserAgent(request.getUserAgent());

        return entity;
    }

    public void updateEntity(NhatKyHeThong entity, NhatKyHeThongRequest request) {
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

        entity.setHanhDong(request.getHanhDong());
        entity.setBangTacDong(request.getBangTacDong());
        entity.setBanGhiId(request.getBanGhiId());
        entity.setNoiDungCu(request.getNoiDungCu());
        entity.setNoiDungMoi(request.getNoiDungMoi());
        entity.setIpAddress(request.getIpAddress());
        entity.setUserAgent(request.getUserAgent());
    }
}