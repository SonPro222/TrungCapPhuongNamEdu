package org.example.trungcapphuongnam.module.giangDay.mapper;

import org.example.trungcapphuongnam.module.giangDay.entity.CaHoc;
import org.example.trungcapphuongnam.module.giangDay.dto.request.CaHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.CaHocResponse;
import org.springframework.stereotype.Component;

@Component
public class CaHocMapper {

    public CaHoc toEntity(CaHocRequest request) {
        if (request == null) return null;
        return CaHoc.builder()
                .maCa(request.getMaCa())
                .tenCa(request.getTenCa())
                .gioBatDau(request.getGioBatDau())
                .gioKetThuc(request.getGioKetThuc())
                .moTa(request.getMoTa())
                .build();
    }

    public CaHocResponse toResponse(CaHoc entity) {
        if (entity == null) return null;
        return CaHocResponse.builder()
                .id(entity.getId())
                .maCa(entity.getMaCa())
                .tenCa(entity.getTenCa())
                .gioBatDau(entity.getGioBatDau())
                .gioKetThuc(entity.getGioKetThuc())
                .moTa(entity.getMoTa())
                .build();
    }

    public void updateEntity(CaHoc entity, CaHocRequest request) {
        if (entity == null || request == null) return;
        entity.setMaCa(request.getMaCa());
        entity.setTenCa(request.getTenCa());
        entity.setGioBatDau(request.getGioBatDau());
        entity.setGioKetThuc(request.getGioKetThuc());
        entity.setMoTa(request.getMoTa());
    }
}
