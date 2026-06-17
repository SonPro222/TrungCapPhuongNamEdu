package org.example.trungcapphuongnam.module.chuongTrinh.mapper;


import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienMonHocMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienMonHocMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.DieuKienMonHocMau;
import org.springframework.stereotype.Component;

@Component
public class DieuKienMonHocMauMapper {

    public DieuKienMonHocMau toEntity(DieuKienMonHocMauRequest request) {
        if (request == null) {
            return null;
        }

        return DieuKienMonHocMau.builder()
                .ma(request.getMa())
                .loai(request.getLoai())
                .noiDung(request.getNoiDung())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public void updateEntity(DieuKienMonHocMau entity, DieuKienMonHocMauRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setMa(request.getMa());
        entity.setLoai(request.getLoai());
        entity.setNoiDung(request.getNoiDung());
        entity.setGhiChu(request.getGhiChu());
    }

    public DieuKienMonHocMauResponse toResponse(DieuKienMonHocMau entity) {
        if (entity == null) {
            return null;
        }

        return DieuKienMonHocMauResponse.builder()
                .id(entity.getId())
                .ma(entity.getMa())
                .loai(entity.getLoai())
                .noiDung(entity.getNoiDung())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}