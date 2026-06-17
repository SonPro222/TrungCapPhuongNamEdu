package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhMonQuyDoiDiemMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhMonQuyDoiDiemMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMonQuyDoiDiemMau;
import org.springframework.stereotype.Component;

@Component
public class ChuongTrinhMonQuyDoiDiemMauMapper {

    public ChuongTrinhMonQuyDoiDiemMau toEntity(ChuongTrinhMonQuyDoiDiemMauRequest request) {
        if (request == null) {
            return null;
        }

        return ChuongTrinhMonQuyDoiDiemMau.builder()
                .chuongTrinhMonId(request.getChuongTrinhMonId())
                .syllabusMonHocId(request.getSyllabusMonHocId())
                .quyDoiDiemMauId(request.getQuyDoiDiemMauId())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public void updateEntity(ChuongTrinhMonQuyDoiDiemMau entity, ChuongTrinhMonQuyDoiDiemMauRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setChuongTrinhMonId(request.getChuongTrinhMonId());
        entity.setSyllabusMonHocId(request.getSyllabusMonHocId());
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
                .syllabusMonHocId(entity.getSyllabusMonHocId())
                .quyDoiDiemMauId(entity.getQuyDoiDiemMauId())
                .ghiChu(entity.getGhiChu())
                .build();
    }
}