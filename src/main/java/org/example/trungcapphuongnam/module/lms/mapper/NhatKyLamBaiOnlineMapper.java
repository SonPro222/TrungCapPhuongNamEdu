package org.example.trungcapphuongnam.module.lms.mapper;

import org.example.trungcapphuongnam.module.lms.dto.request.NhatKyLamBaiOnlineRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.NhatKyLamBaiOnlineResponse;
import org.example.trungcapphuongnam.module.lms.entity.NhatKyLamBaiOnline;

public final class NhatKyLamBaiOnlineMapper {
    private NhatKyLamBaiOnlineMapper() {}

    public static NhatKyLamBaiOnlineResponse toResponse(NhatKyLamBaiOnline entity) {
        if (entity == null) return null;
        return NhatKyLamBaiOnlineResponse.builder()
            .id(entity.getId())
            .lanLamBaiId(entity.getLanLamBaiId())
            .hanhDong(entity.getHanhDong())
            .duLieu(entity.getDuLieu())
            .ipAddress(entity.getIpAddress())
            .userAgent(entity.getUserAgent())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    public static NhatKyLamBaiOnline toEntity(NhatKyLamBaiOnlineRequest request) {
        if (request == null) return null;
        return NhatKyLamBaiOnline.builder()
            .lanLamBaiId(request.getLanLamBaiId())
            .hanhDong(request.getHanhDong())
            .duLieu(request.getDuLieu())
            .ipAddress(request.getIpAddress())
            .userAgent(request.getUserAgent())
            .build();
    }

    public static void updateEntity(NhatKyLamBaiOnline entity, NhatKyLamBaiOnlineRequest request) {
        if (entity == null || request == null) return;
        entity.setLanLamBaiId(request.getLanLamBaiId());
        entity.setHanhDong(request.getHanhDong());
        entity.setDuLieu(request.getDuLieu());
        entity.setIpAddress(request.getIpAddress());
        entity.setUserAgent(request.getUserAgent());
    }
}
