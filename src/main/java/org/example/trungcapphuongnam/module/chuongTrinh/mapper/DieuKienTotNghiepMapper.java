package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienTotNghiepRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienTotNghiepResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.DieuKienTotNghiep;
import org.springframework.stereotype.Component;

@Component
public class DieuKienTotNghiepMapper {

    public DieuKienTotNghiep toEntity(DieuKienTotNghiepRequest request) {
        if (request == null) return null;
        return DieuKienTotNghiep.builder()
                .chuongTrinhVersionId(request.getChuongTrinhVersionId())
                .noiDung(request.getNoiDung())
                .thuTu(request.getThuTu())
                .build();
    }

    public DieuKienTotNghiepResponse toResponse(DieuKienTotNghiep entity) {
        if (entity == null) return null;
        return DieuKienTotNghiepResponse.builder()
                .id(entity.getId())
                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
                .noiDung(entity.getNoiDung())
                .thuTu(entity.getThuTu())
                .build();
    }

    public void updateEntity(DieuKienTotNghiep entity, DieuKienTotNghiepRequest request) {
        if (entity == null || request == null) return;
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setNoiDung(request.getNoiDung());
        entity.setThuTu(request.getThuTu());
    }
}
