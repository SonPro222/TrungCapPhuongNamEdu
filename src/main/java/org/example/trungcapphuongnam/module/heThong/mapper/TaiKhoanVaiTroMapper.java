package org.example.trungcapphuongnam.module.heThong.mapper;

import org.example.trungcapphuongnam.module.heThong.dto.request.TaiKhoanVaiTroRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.TaiKhoanVaiTroResponse;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoanVaiTro;
import org.example.trungcapphuongnam.module.heThong.entity.VaiTro;
import org.springframework.stereotype.Component;

@Component
public class TaiKhoanVaiTroMapper {

    public TaiKhoanVaiTroResponse toResponse(TaiKhoanVaiTro entity) {
        if (entity == null) {
            return null;
        }

        return TaiKhoanVaiTroResponse.builder()
                .id(entity.getId())
                .taiKhoanId(entity.getTaiKhoan() == null ? null : entity.getTaiKhoan().getId())
                .vaiTroId(entity.getVaiTro() == null ? null : entity.getVaiTro().getId())
                .build();
    }

    public TaiKhoanVaiTro toEntity(TaiKhoanVaiTroRequest request) {
        if (request == null) {
            return null;
        }

        TaiKhoanVaiTro entity = new TaiKhoanVaiTro();

        if (request.getTaiKhoanId() != null) {
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setId(request.getTaiKhoanId());
            entity.setTaiKhoan(taiKhoan);
        }

        if (request.getVaiTroId() != null) {
            VaiTro vaiTro = new VaiTro();
            vaiTro.setId(request.getVaiTroId());
            entity.setVaiTro(vaiTro);
        }

        return entity;
    }

    public void updateEntity(TaiKhoanVaiTro entity, TaiKhoanVaiTroRequest request) {
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

        if (request.getVaiTroId() != null) {
            VaiTro vaiTro = new VaiTro();
            vaiTro.setId(request.getVaiTroId());
            entity.setVaiTro(vaiTro);
        } else {
            entity.setVaiTro(null);
        }
    }
}