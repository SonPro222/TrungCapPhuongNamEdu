package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.springframework.stereotype.Component;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MucTieuChuongTrinhGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MucTieuChuongTrinhGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.MucTieuChuongTrinhMau;

@Component
public class MucTieuChuongTrinhGocMapper {

    public MucTieuChuongTrinhMau toEntity(MucTieuChuongTrinhGocRequest request) {
        if (request == null) {
            return null;
        }
        MucTieuChuongTrinhMau entity = new MucTieuChuongTrinhMau();
        entity.setMa(request.getMa());
        entity.setLoai(request.getLoai());
        entity.setNoiDung(request.getNoiDung());
        entity.setGhiChu(request.getGhiChu());
        return entity;
    }

    public void updateEntity(MucTieuChuongTrinhMau entity, MucTieuChuongTrinhGocRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setMa(request.getMa());
        entity.setLoai(request.getLoai());
        entity.setNoiDung(request.getNoiDung());
        entity.setGhiChu(request.getGhiChu());
    }

    public MucTieuChuongTrinhGocResponse toResponse(MucTieuChuongTrinhMau entity) {
        if (entity == null) {
            return null;
        }
        return MucTieuChuongTrinhGocResponse.builder()
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
