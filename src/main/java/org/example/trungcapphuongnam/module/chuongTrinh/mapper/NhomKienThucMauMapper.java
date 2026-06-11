package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomKienThucMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomKienThucMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomKienThucMau;
import org.springframework.stereotype.Component;

@Component
public class NhomKienThucMauMapper {

    public NhomKienThucMau toEntity(NhomKienThucMauRequest request) {
        if (request == null) {
            return null;
        }

        return NhomKienThucMau.builder()
                .ma(request.getMa())
                .ten(request.getTen())
                .loaiNhom(request.getLoaiNhom())
                .moTa(request.getMoTa())
                .build();
    }

    public void updateEntity(NhomKienThucMau entity, NhomKienThucMauRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setLoaiNhom(request.getLoaiNhom());
        entity.setMoTa(request.getMoTa());
    }

    public NhomKienThucMauResponse toResponse(NhomKienThucMau entity) {
        if (entity == null) {
            return null;
        }

        return NhomKienThucMauResponse.builder()
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