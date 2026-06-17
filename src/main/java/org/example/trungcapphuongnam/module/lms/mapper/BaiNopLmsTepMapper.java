package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.BaiNopLmsTepRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.BaiNopLmsTepResponse;
import org.example.trungcapphuongnam.module.lms.entity.BaiNopLmsTep;

public final class BaiNopLmsTepMapper {
    private BaiNopLmsTepMapper() {}

    public static BaiNopLmsTepResponse toResponse(BaiNopLmsTep entity) {
        if (entity == null) return null;
        return BaiNopLmsTepResponse.builder()
            .id(entity.getId())
            .baiNopLmsId(entity.getBaiNopLmsId())
            .tepDinhKemId(entity.getTepDinhKemId())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    public static BaiNopLmsTep toEntity(BaiNopLmsTepRequest request) {
        if (request == null) return null;
        return BaiNopLmsTep.builder()
            .baiNopLmsId(request.getBaiNopLmsId())
            .tepDinhKemId(request.getTepDinhKemId())
            .build();
    }

    public static void updateEntity(BaiNopLmsTep entity, BaiNopLmsTepRequest request) {
        if (entity == null || request == null) return;
        entity.setBaiNopLmsId(request.getBaiNopLmsId());
        entity.setTepDinhKemId(request.getTepDinhKemId());
    }
}
