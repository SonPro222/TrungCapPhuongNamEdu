package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusTaiLieuRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusTaiLieuResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusTaiLieu;
import org.springframework.stereotype.Component;

@Component
public class SyllabusTaiLieuMapper {

    public SyllabusTaiLieu toEntity(SyllabusTaiLieuRequest request) {
        if (request == null) return null;
        return SyllabusTaiLieu.builder()
                .syllabusMonId(request.getSyllabusMonId())
                .ten(request.getTen())
                .tacGia(request.getTacGia())
                .namXuatBan(request.getNamXuatBan())
                .nhaXuatBan(request.getNhaXuatBan())
                .loai(request.getLoai())
                .ghiChu(request.getGhiChu())
                .ma(request.getMa())
                .duongDan(request.getDuongDan())

                .build();
    }

    public SyllabusTaiLieuResponse toResponse(SyllabusTaiLieu entity) {
        if (entity == null) return null;
        return SyllabusTaiLieuResponse.builder()
                .id(entity.getId())
                .syllabusMonId(entity.getSyllabusMonId())
                .ten(entity.getTen())
                .tacGia(entity.getTacGia())
                .namXuatBan(entity.getNamXuatBan())
                .nhaXuatBan(entity.getNhaXuatBan())
                .loai(entity.getLoai())
                .ghiChu(entity.getGhiChu())
                .ma(entity.getMa())
                .duongDan(entity.getDuongDan())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(SyllabusTaiLieu entity, SyllabusTaiLieuRequest request) {
        if (entity == null || request == null) return;
        entity.setSyllabusMonId(request.getSyllabusMonId());
        entity.setTen(request.getTen());
        entity.setTacGia(request.getTacGia());
        entity.setNamXuatBan(request.getNamXuatBan());
        entity.setNhaXuatBan(request.getNhaXuatBan());
        entity.setLoai(request.getLoai());
        entity.setGhiChu(request.getGhiChu());
        entity.setMa(request.getMa());
        entity.setDuongDan(request.getDuongDan());


    }
}
