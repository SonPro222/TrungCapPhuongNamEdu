package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocMauTaiLieuRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauTaiLieuResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocMauTaiLieu;
import org.springframework.stereotype.Component;

@Component
public class SyllabusMonHocMauTaiLieuMapper {

    public SyllabusMonHocMauTaiLieu toEntity(SyllabusMonHocMauTaiLieuRequest request) {
        if (request == null) {
            return null;
        }

        return SyllabusMonHocMauTaiLieu.builder()
                .syllabusMonHocMauId(request.getSyllabusMonHocMauId())
                .taiLieuMauId(request.getTaiLieuMauId())
                .ma(request.getMa())
                .ten(request.getTen())
                .loai(request.getLoai())
                .tacGia(request.getTacGia())
                .nhaXuatBan(request.getNhaXuatBan())
                .namXuatBan(request.getNamXuatBan())
                .duongDan(request.getDuongDan())
                .thuTu(request.getThuTu())
                .batBuoc(request.getBatBuoc())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public void updateEntity(SyllabusMonHocMauTaiLieu entity, SyllabusMonHocMauTaiLieuRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setSyllabusMonHocMauId(request.getSyllabusMonHocMauId());
        entity.setTaiLieuMauId(request.getTaiLieuMauId());
        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setLoai(request.getLoai());
        entity.setTacGia(request.getTacGia());
        entity.setNhaXuatBan(request.getNhaXuatBan());
        entity.setNamXuatBan(request.getNamXuatBan());
        entity.setDuongDan(request.getDuongDan());
        entity.setThuTu(request.getThuTu());
        entity.setBatBuoc(request.getBatBuoc());
        entity.setGhiChu(request.getGhiChu());
    }

    public SyllabusMonHocMauTaiLieuResponse toResponse(SyllabusMonHocMauTaiLieu entity) {
        if (entity == null) {
            return null;
        }

        return SyllabusMonHocMauTaiLieuResponse.builder()
                .id(entity.getId())
                .syllabusMonHocMauId(entity.getSyllabusMonHocMauId())
                .taiLieuMauId(entity.getTaiLieuMauId())
                .ma(entity.getMa())
                .ten(entity.getTen())
                .loai(entity.getLoai())
                .tacGia(entity.getTacGia())
                .nhaXuatBan(entity.getNhaXuatBan())
                .namXuatBan(entity.getNamXuatBan())
                .duongDan(entity.getDuongDan())
                .thuTu(entity.getThuTu())
                .batBuoc(entity.getBatBuoc())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}