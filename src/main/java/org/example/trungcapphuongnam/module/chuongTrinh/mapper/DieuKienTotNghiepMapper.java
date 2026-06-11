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
                .syllabusChuongTrinhId(request.getSyllabusChuongTrinhId())
                .noiDung(request.getNoiDung())
                .thuTu(request.getThuTu())
                .ma(request.getMa())
                .ghiChu(request.getGhiChu())

                .build();
    }

    public DieuKienTotNghiepResponse toResponse(DieuKienTotNghiep entity) {
        if (entity == null) return null;
        return DieuKienTotNghiepResponse.builder()
                .id(entity.getId())
                .syllabusChuongTrinhId(entity.getSyllabusChuongTrinhId())
                .noiDung(entity.getNoiDung())
                .thuTu(entity.getThuTu())
                .ma(entity.getMa())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(DieuKienTotNghiep entity, DieuKienTotNghiepRequest request) {
        if (entity == null || request == null) return;
        entity.setSyllabusChuongTrinhId(request.getSyllabusChuongTrinhId());
        entity.setNoiDung(request.getNoiDung());
        entity.setThuTu(request.getThuTu());
        entity.setMa(request.getMa());
        entity.setGhiChu(request.getGhiChu());


    }
}
