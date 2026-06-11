package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.springframework.stereotype.Component;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MucTieuChuongTrinhMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MucTieuChuongTrinhMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.MucTieuChuongTrinhMau;

@Component
public class MucTieuChuongTrinhMauMapper {

    public MucTieuChuongTrinhMau toEntity(MucTieuChuongTrinhMauRequest request) {
        if (request == null) {
            return null;
        }
        MucTieuChuongTrinhMau entity = new MucTieuChuongTrinhMau();
        entity.setSyllabusChuongTrinhMauId(request.getSyllabusChuongTrinhMauId());
        entity.setSyllabusChuongTrinhMauId(request.getSyllabusChuongTrinhMauId());
        entity.setMa(request.getMa());
        entity.setLoai(request.getLoai());
        entity.setNoiDung(request.getNoiDung());
        entity.setGhiChu(request.getGhiChu());
        return entity;
    }

    public void updateEntity(MucTieuChuongTrinhMau entity, MucTieuChuongTrinhMauRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setSyllabusChuongTrinhMauId(request.getSyllabusChuongTrinhMauId());
        entity.setMa(request.getMa());
        entity.setLoai(request.getLoai());
        entity.setNoiDung(request.getNoiDung());
        entity.setGhiChu(request.getGhiChu());
    }

    public MucTieuChuongTrinhMauResponse toResponse(MucTieuChuongTrinhMau entity) {
        if (entity == null) {
            return null;
        }
        return MucTieuChuongTrinhMauResponse.builder()
                .id(entity.getId())
                .syllabusChuongTrinhMauId(entity.getSyllabusChuongTrinhMauId())
                .ma(entity.getMa())
                .loai(entity.getLoai())
                .noiDung(entity.getNoiDung())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
