package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.springframework.stereotype.Component;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NangLucDauRaGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NangLucDauRaGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NangLucDauRaMau;

@Component
public class NangLucDauRaGocMapper {

    public NangLucDauRaMau toEntity(NangLucDauRaGocRequest request) {
        if (request == null) {
            return null;
        }
        NangLucDauRaMau entity = new NangLucDauRaMau();
        entity.setChuongTrinhId(request.getChuongTrinhId());
        entity.setMa(request.getMa());
        entity.setLoai(request.getLoai());
        entity.setNoiDung(request.getNoiDung());
        entity.setGhiChu(request.getGhiChu());
        return entity;
    }

    public void updateEntity(NangLucDauRaMau entity, NangLucDauRaGocRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setChuongTrinhId(request.getChuongTrinhId());
        entity.setMa(request.getMa());
        entity.setLoai(request.getLoai());
        entity.setNoiDung(request.getNoiDung());
        entity.setGhiChu(request.getGhiChu());
    }

    public NangLucDauRaGocResponse toResponse(NangLucDauRaMau entity) {
        if (entity == null) {
            return null;
        }
        return NangLucDauRaGocResponse.builder()
                .id(entity.getId())
                .chuongTrinhId(entity.getChuongTrinhId())
                .ma(entity.getMa())
                .loai(entity.getLoai())
                .noiDung(entity.getNoiDung())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
