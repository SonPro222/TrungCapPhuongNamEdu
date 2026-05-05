package org.example.trungcapphuongnam.module.daoTao.mapper;

import org.example.trungcapphuongnam.module.daoTao.dto.KhoaDaoTaoRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhoaDaoTaoResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.KhoaDaoTao;
import org.springframework.stereotype.Component;

@Component
public class KhoaDaoTaoMapper {

    public KhoaDaoTao toEntity(KhoaDaoTaoRequest request) {
        if (request == null) return null;
        return KhoaDaoTao.builder()
                .maKhoa(request.getMaKhoa())
                .tenKhoa(request.getTenKhoa())
                .namBatDau(request.getNamBatDau())
                .namKetThuc(request.getNamKetThuc())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public KhoaDaoTaoResponse toResponse(KhoaDaoTao entity) {
        if (entity == null) return null;
        return KhoaDaoTaoResponse.builder()
                .id(entity.getId())
                .maKhoa(entity.getMaKhoa())
                .tenKhoa(entity.getTenKhoa())
                .namBatDau(entity.getNamBatDau())
                .namKetThuc(entity.getNamKetThuc())
                .ghiChu(entity.getGhiChu())
                .build();
    }

    public void updateEntity(KhoaDaoTao entity, KhoaDaoTaoRequest request) {
        if (entity == null || request == null) return;
        entity.setMaKhoa(request.getMaKhoa());
        entity.setTenKhoa(request.getTenKhoa());
        entity.setNamBatDau(request.getNamBatDau());
        entity.setNamKetThuc(request.getNamKetThuc());
        entity.setGhiChu(request.getGhiChu());
    }
}
