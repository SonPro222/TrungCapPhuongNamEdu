package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomKienThucGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomKienThucGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomKienThucGoc;
import org.springframework.stereotype.Component;

@Component
public class NhomKienThucGocMapper {

    public NhomKienThucGoc toEntity(NhomKienThucGocRequest request) {
        if (request == null) {
            return null;
        }

        return NhomKienThucGoc.builder()
                .ma(request.getMa())
                .ten(request.getTen())
                .loaiNhom(request.getLoaiNhom())
                .moTa(request.getMoTa())
                .build();
    }

    public void updateEntity(NhomKienThucGoc entity, NhomKienThucGocRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setLoaiNhom(request.getLoaiNhom());
        entity.setMoTa(request.getMoTa());
    }

    public NhomKienThucGocResponse toResponse(NhomKienThucGoc entity) {
        if (entity == null) {
            return null;
        }

        return NhomKienThucGocResponse.builder()
                .id(entity.getId())
                .ma(entity.getMa())
                .ten(entity.getTen())
                .loaiNhom(entity.getLoaiNhom())
                .moTa(entity.getMoTa())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}