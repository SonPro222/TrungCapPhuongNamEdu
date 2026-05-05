package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.CauTraLoiChonDapAnRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauTraLoiChonDapAnResponse;
import org.example.trungcapphuongnam.module.lms.entity.CauTraLoiChonDapAn;

public final class CauTraLoiChonDapAnMapper {
    private CauTraLoiChonDapAnMapper() {}

    public static CauTraLoiChonDapAnResponse toResponse(CauTraLoiChonDapAn entity) {
        if (entity == null) return null;
        return CauTraLoiChonDapAnResponse.builder()
            .id(entity.getId())
            .cauTraLoiId(entity.getCauTraLoiId())
            .dapAnId(entity.getDapAnId())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    public static CauTraLoiChonDapAn toEntity(CauTraLoiChonDapAnRequest request) {
        if (request == null) return null;
        return CauTraLoiChonDapAn.builder()
            .cauTraLoiId(request.getCauTraLoiId())
            .dapAnId(request.getDapAnId())
            .build();
    }

    public static void updateEntity(CauTraLoiChonDapAn entity, CauTraLoiChonDapAnRequest request) {
        if (entity == null || request == null) return;
        entity.setCauTraLoiId(request.getCauTraLoiId());
        entity.setDapAnId(request.getDapAnId());
    }
}
