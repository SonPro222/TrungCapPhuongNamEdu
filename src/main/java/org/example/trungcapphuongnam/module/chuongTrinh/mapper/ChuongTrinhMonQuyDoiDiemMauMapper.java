package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.springframework.stereotype.Component;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhMonQuyDoiDiemMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhMonQuyDoiDiemMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMonQuyDoiDiemMau;

@Component
public class ChuongTrinhMonQuyDoiDiemMauMapper {

    public ChuongTrinhMonQuyDoiDiemMau toEntity(ChuongTrinhMonQuyDoiDiemMauRequest request) {
        if (request == null) {
            return null;
        }
        ChuongTrinhMonQuyDoiDiemMau entity = new ChuongTrinhMonQuyDoiDiemMau();
        entity.setChuongTrinhMonId(request.getChuongTrinhMonId());
        entity.setQuyDoiDiemMauId(request.getQuyDoiDiemMauId());
        entity.setGhiChu(request.getGhiChu());
        return entity;
    }

    public void updateEntity(ChuongTrinhMonQuyDoiDiemMau entity, ChuongTrinhMonQuyDoiDiemMauRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setChuongTrinhMonId(request.getChuongTrinhMonId());
        entity.setQuyDoiDiemMauId(request.getQuyDoiDiemMauId());
        entity.setGhiChu(request.getGhiChu());
    }

    public ChuongTrinhMonQuyDoiDiemMauResponse toResponse(ChuongTrinhMonQuyDoiDiemMau entity) {
        if (entity == null) {
            return null;
        }
        return ChuongTrinhMonQuyDoiDiemMauResponse.builder()
                .id(entity.getId())
                .chuongTrinhMonId(entity.getChuongTrinhMonId())
                .quyDoiDiemMauId(entity.getQuyDoiDiemMauId())
                .ghiChu(entity.getGhiChu())
                .build();
    }
}
