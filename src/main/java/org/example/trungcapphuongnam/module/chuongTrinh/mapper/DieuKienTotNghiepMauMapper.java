package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.springframework.stereotype.Component;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienTotNghiepMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienTotNghiepMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.DieuKienTotNghiepMau;

@Component
public class DieuKienTotNghiepMauMapper {

    public DieuKienTotNghiepMau toEntity(DieuKienTotNghiepMauRequest request) {
        if (request == null) {
            return null;
        }
        DieuKienTotNghiepMau entity = new DieuKienTotNghiepMau();
        entity.setSyllabusChuongTrinhMauId(request.getSyllabusChuongTrinhMauId());
        entity.setSyllabusChuongTrinhMauId(request.getSyllabusChuongTrinhMauId());
        entity.setMa(request.getMa());
        entity.setNoiDung(request.getNoiDung());
        entity.setGhiChu(request.getGhiChu());
        return entity;
    }

    public void updateEntity(DieuKienTotNghiepMau entity, DieuKienTotNghiepMauRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setMa(request.getMa());
        entity.setNoiDung(request.getNoiDung());
        entity.setGhiChu(request.getGhiChu());
    }

    public DieuKienTotNghiepMauResponse toResponse(DieuKienTotNghiepMau entity) {
        if (entity == null) {
            return null;
        }
        return DieuKienTotNghiepMauResponse.builder()
                .id(entity.getId())
                .syllabusChuongTrinhMauId(entity.getSyllabusChuongTrinhMauId())
                .ma(entity.getMa())
                .noiDung(entity.getNoiDung())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
