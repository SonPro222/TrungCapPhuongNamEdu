package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.springframework.stereotype.Component;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NangLucDauRaMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NangLucDauRaMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NangLucDauRaMau;

@Component
public class NangLucDauRaMauMapper {

    public NangLucDauRaMau toEntity(NangLucDauRaMauRequest request) {
        if (request == null) {
            return null;
        }
        NangLucDauRaMau entity = new NangLucDauRaMau();
        entity.setSyllabusChuongTrinhMauId(request.getSyllabusChuongTrinhMauId());
        entity.setMa(request.getMa());
        entity.setLoai(request.getLoai());
        entity.setNoiDung(request.getNoiDung());
        entity.setGhiChu(request.getGhiChu());
        return entity;
    }

    public void updateEntity(NangLucDauRaMau entity, NangLucDauRaMauRequest request) {
        if (entity == null || request == null) {
            return;
        }
        entity.setSyllabusChuongTrinhMauId(request.getSyllabusChuongTrinhMauId());
        entity.setMa(request.getMa());
        entity.setLoai(request.getLoai());
        entity.setNoiDung(request.getNoiDung());
        entity.setGhiChu(request.getGhiChu());
    }

    public NangLucDauRaMauResponse toResponse(NangLucDauRaMau entity) {
        if (entity == null) {
            return null;
        }
        return NangLucDauRaMauResponse.builder()
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
