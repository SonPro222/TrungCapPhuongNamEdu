package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MonTuChonRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MonTuChonResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.MonTuChon;
import org.springframework.stereotype.Component;

@Component
public class MonTuChonMapper {

    public MonTuChon toEntity(MonTuChonRequest request) {
        if (request == null) return null;
        return MonTuChon.builder()
                .nhomId(request.getNhomId())
                .chuongTrinhMonId(request.getChuongTrinhMonId())
                .build();
    }

    public MonTuChonResponse toResponse(MonTuChon entity) {
        if (entity == null) return null;
        return MonTuChonResponse.builder()
                .id(entity.getId())
                .nhomId(entity.getNhomId())
                .chuongTrinhMonId(entity.getChuongTrinhMonId())
                .build();
    }

    public void updateEntity(MonTuChon entity, MonTuChonRequest request) {
        if (entity == null || request == null) return;
        entity.setNhomId(request.getNhomId());
        entity.setChuongTrinhMonId(request.getChuongTrinhMonId());
    }
}
