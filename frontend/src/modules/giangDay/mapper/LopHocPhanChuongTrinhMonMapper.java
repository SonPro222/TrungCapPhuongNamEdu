package org.example.trungcapphuongnam.module.giangDay.mapper;

import org.example.trungcapphuongnam.module.giangDay.dto.request.LopHocPhanChuongTrinhMonRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LopHocPhanChuongTrinhMonResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhanChuongTrinhMon;
import org.springframework.stereotype.Component;

@Component
public class LopHocPhanChuongTrinhMonMapper {

    public LopHocPhanChuongTrinhMon toEntity(LopHocPhanChuongTrinhMonRequest request) {
        if (request == null) {
            return null;
        }

        return LopHocPhanChuongTrinhMon.builder()
                .lopHocPhanId(request.getLopHocPhanId())
                .chuongTrinhMonId(request.getChuongTrinhMonId())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public LopHocPhanChuongTrinhMonResponse toResponse(LopHocPhanChuongTrinhMon entity) {
        if (entity == null) {
            return null;
        }

        return LopHocPhanChuongTrinhMonResponse.builder()
                .id(entity.getId())
                .lopHocPhanId(entity.getLopHocPhanId())
                .chuongTrinhMonId(entity.getChuongTrinhMonId())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(LopHocPhanChuongTrinhMon entity, LopHocPhanChuongTrinhMonRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setLopHocPhanId(request.getLopHocPhanId());
        entity.setChuongTrinhMonId(request.getChuongTrinhMonId());
        entity.setGhiChu(request.getGhiChu());
    }
}