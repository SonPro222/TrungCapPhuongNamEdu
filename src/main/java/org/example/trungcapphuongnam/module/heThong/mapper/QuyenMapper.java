package org.example.trungcapphuongnam.module.heThong.mapper;

import org.example.trungcapphuongnam.module.heThong.dto.request.QuyenRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.QuyenResponse;
import org.example.trungcapphuongnam.module.heThong.entity.Quyen;
import org.springframework.stereotype.Component;

@Component
public class QuyenMapper {

    public QuyenResponse toResponse(Quyen entity) {
        if (entity == null) {
            return null;
        }
        return QuyenResponse.builder()
                .id(entity.getId())
                .maQuyen(entity.getMaQuyen())
                .tenQuyen(entity.getTenQuyen())
                .moTa(entity.getMoTa())
                .build();
    }

    public Quyen toEntity(QuyenRequest request) {
        if (request == null) {
            return null;
        }
        Quyen entity = new Quyen();
        entity.setMaQuyen(request.getMaQuyen());
        entity.setTenQuyen(request.getTenQuyen());
        entity.setMoTa(request.getMoTa());
        return entity;
    }

    public void updateEntity(Quyen entity, QuyenRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setMaQuyen(request.getMaQuyen());
        entity.setTenQuyen(request.getTenQuyen());
        entity.setMoTa(request.getMoTa());
    }
}
