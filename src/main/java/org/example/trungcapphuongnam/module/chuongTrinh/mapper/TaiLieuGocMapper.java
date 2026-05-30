package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.TaiLieuGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.TaiLieuGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.TaiLieuGoc;
import org.springframework.stereotype.Component;

@Component
public class TaiLieuGocMapper {

    public TaiLieuGoc toEntity(TaiLieuGocRequest request) {
        if (request == null) {
            return null;
        }

        return TaiLieuGoc.builder()
                .ma(request.getMa())
                .ten(request.getTen())
                .loai(request.getLoai())
                .tacGia(request.getTacGia())
                .nhaXuatBan(request.getNhaXuatBan())
                .namXuatBan(request.getNamXuatBan())
                .duongDan(request.getDuongDan())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public void updateEntity(TaiLieuGoc entity, TaiLieuGocRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setLoai(request.getLoai());
        entity.setTacGia(request.getTacGia());
        entity.setNhaXuatBan(request.getNhaXuatBan());
        entity.setNamXuatBan(request.getNamXuatBan());
        entity.setDuongDan(request.getDuongDan());
        entity.setGhiChu(request.getGhiChu());
    }

    public TaiLieuGocResponse toResponse(TaiLieuGoc entity) {
        if (entity == null) {
            return null;
        }

        return TaiLieuGocResponse.builder()
                .id(entity.getId())
                .ma(entity.getMa())
                .ten(entity.getTen())
                .loai(entity.getLoai())
                .tacGia(entity.getTacGia())
                .nhaXuatBan(entity.getNhaXuatBan())
                .namXuatBan(entity.getNamXuatBan())
                .duongDan(entity.getDuongDan())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}