package org.example.trungcapphuongnam.module.heThong.mapper;

import org.example.trungcapphuongnam.module.heThong.dto.request.VaiTroQuyenRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.VaiTroQuyenResponse;
import org.example.trungcapphuongnam.module.heThong.entity.Quyen;
import org.example.trungcapphuongnam.module.heThong.entity.VaiTro;
import org.example.trungcapphuongnam.module.heThong.entity.VaiTroQuyen;
import org.springframework.stereotype.Component;

@Component
public class VaiTroQuyenMapper {

    public VaiTroQuyenResponse toResponse(VaiTroQuyen entity) {
        if (entity == null) {
            return null;
        }

        return VaiTroQuyenResponse.builder()
                .id(entity.getId())
                .vaiTroId(entity.getVaiTro() == null ? null : entity.getVaiTro().getId())
                .quyenId(entity.getQuyen() == null ? null : entity.getQuyen().getId())
                .build();
    }

    public VaiTroQuyen toEntity(VaiTroQuyenRequest request) {
        if (request == null) {
            return null;
        }

        VaiTroQuyen entity = new VaiTroQuyen();

        if (request.getVaiTroId() != null) {
            VaiTro vaiTro = new VaiTro();
            vaiTro.setId(request.getVaiTroId());
            entity.setVaiTro(vaiTro);
        }

        if (request.getQuyenId() != null) {
            Quyen quyen = new Quyen();
            quyen.setId(request.getQuyenId());
            entity.setQuyen(quyen);
        }

        return entity;
    }

    public void updateEntity(VaiTroQuyen entity, VaiTroQuyenRequest request) {
        if (entity == null || request == null) {
            return;
        }

        if (request.getVaiTroId() != null) {
            VaiTro vaiTro = new VaiTro();
            vaiTro.setId(request.getVaiTroId());
            entity.setVaiTro(vaiTro);
        } else {
            entity.setVaiTro(null);
        }

        if (request.getQuyenId() != null) {
            Quyen quyen = new Quyen();
            quyen.setId(request.getQuyenId());
            entity.setQuyen(quyen);
        } else {
            entity.setQuyen(null);
        }
    }
}