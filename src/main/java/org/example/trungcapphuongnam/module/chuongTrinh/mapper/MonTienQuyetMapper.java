package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MonTienQuyetRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MonTienQuyetResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.MonTienQuyet;
import org.springframework.stereotype.Component;

@Component
public class MonTienQuyetMapper {

    public MonTienQuyet toEntity(MonTienQuyetRequest request) {
        if (request == null) return null;
        return MonTienQuyet.builder()
                .monId(request.getMonId())
                .monDieuKienId(request.getMonDieuKienId())
                .loai(request.getLoai())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public MonTienQuyetResponse toResponse(MonTienQuyet entity) {
        if (entity == null) return null;
        return MonTienQuyetResponse.builder()
                .id(entity.getId())
                .monId(entity.getMonId())
                .monDieuKienId(entity.getMonDieuKienId())
                .loai(entity.getLoai())
                .ghiChu(entity.getGhiChu())
                .build();
    }

    public void updateEntity(MonTienQuyet entity, MonTienQuyetRequest request) {
        if (entity == null || request == null) return;
        entity.setMonId(request.getMonId());
        entity.setMonDieuKienId(request.getMonDieuKienId());
        entity.setLoai(request.getLoai());
        entity.setGhiChu(request.getGhiChu());
    }
}
