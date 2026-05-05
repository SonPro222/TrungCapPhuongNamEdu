package org.example.trungcapphuongnam.module.heThong.mapper;

import org.example.trungcapphuongnam.module.heThong.dto.request.VaiTroRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.VaiTroResponse;
import org.example.trungcapphuongnam.module.heThong.entity.VaiTro;
import org.springframework.stereotype.Component;

@Component
public class VaiTroMapper {

    public VaiTroResponse toResponse(VaiTro entity) {
        if (entity == null) {
            return null;
        }
        return VaiTroResponse.builder()
                .id(entity.getId())
                .maVaiTro(entity.getMaVaiTro())
                .tenVaiTro(entity.getTenVaiTro())
                .moTa(entity.getMoTa())
                .build();
    }

    public VaiTro toEntity(VaiTroRequest request) {
        if (request == null) {
            return null;
        }
        VaiTro entity = new VaiTro();
        entity.setMaVaiTro(request.getMaVaiTro());
        entity.setTenVaiTro(request.getTenVaiTro());
        entity.setMoTa(request.getMoTa());
        return entity;
    }

    public void updateEntity(VaiTro entity, VaiTroRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setMaVaiTro(request.getMaVaiTro());
        entity.setTenVaiTro(request.getTenVaiTro());
        entity.setMoTa(request.getMoTa());
    }
}
