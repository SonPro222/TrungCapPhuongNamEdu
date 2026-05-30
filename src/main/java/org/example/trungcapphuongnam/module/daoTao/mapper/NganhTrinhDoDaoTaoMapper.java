package org.example.trungcapphuongnam.module.daoTao.mapper;

import org.example.trungcapphuongnam.module.daoTao.dto.NganhTrinhDoDaoTaoRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhTrinhDoDaoTaoResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.NganhTrinhDoDaoTao;
import org.springframework.stereotype.Component;

@Component
public class NganhTrinhDoDaoTaoMapper {

    public NganhTrinhDoDaoTao toEntity(NganhTrinhDoDaoTaoRequest request) {
        if (request == null) return null;

        return NganhTrinhDoDaoTao.builder()
                .nganhId(request.getNganhId())
                .trinhDoId(request.getTrinhDoId())
                .trangThai(request.getTrangThai())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public NganhTrinhDoDaoTaoResponse toResponse(NganhTrinhDoDaoTao entity) {
        if (entity == null) return null;

        return NganhTrinhDoDaoTaoResponse.builder()
                .id(entity.getId())
                .nganhId(entity.getNganhId())
                .trinhDoId(entity.getTrinhDoId())
                .trangThai(entity.getTrangThai())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(NganhTrinhDoDaoTao entity, NganhTrinhDoDaoTaoRequest request) {
        if (entity == null || request == null) return;

        entity.setNganhId(request.getNganhId());
        entity.setTrinhDoId(request.getTrinhDoId());
        entity.setTrangThai(request.getTrangThai());
        entity.setGhiChu(request.getGhiChu());
    }
}
