package org.example.trungcapphuongnam.module.daoTao.mapper;

import org.example.trungcapphuongnam.module.daoTao.dto.LoaiChuongTrinhRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.LoaiChuongTrinhResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.LoaiChuongTrinh;
import org.springframework.stereotype.Component;

@Component
public class LoaiChuongTrinhMapper {

    public LoaiChuongTrinh toEntity(LoaiChuongTrinhRequest request) {
        if (request == null) return null;
        return LoaiChuongTrinh.builder()
                .maLoai(request.getMaLoai())
                .tenLoai(request.getTenLoai())
                .soThang(request.getSoThang())
                .soKy(request.getSoKy())
                .moTa(request.getMoTa())
                .build();
    }

    public LoaiChuongTrinhResponse toResponse(LoaiChuongTrinh entity) {
        if (entity == null) return null;
        return LoaiChuongTrinhResponse.builder()
                .id(entity.getId())
                .maLoai(entity.getMaLoai())
                .tenLoai(entity.getTenLoai())
                .soThang(entity.getSoThang())
                .soKy(entity.getSoKy())
                .moTa(entity.getMoTa())
                .build();
    }

    public void updateEntity(LoaiChuongTrinh entity, LoaiChuongTrinhRequest request) {
        if (entity == null || request == null) return;
        entity.setMaLoai(request.getMaLoai());
        entity.setTenLoai(request.getTenLoai());
        entity.setSoThang(request.getSoThang());
        entity.setSoKy(request.getSoKy());
        entity.setMoTa(request.getMoTa());
    }
}
