package org.example.trungcapphuongnam.module.chuongTrinh.mapper;


import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocTaiLieuRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocTaiLieuResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocTaiLieu;
import org.springframework.stereotype.Component;

@Component
public class SyllabusMonHocTaiLieuMapper {

    public SyllabusMonHocTaiLieu toEntity(SyllabusMonHocTaiLieuRequest request) {
        if (request == null) {
            return null;
        }

        return SyllabusMonHocTaiLieu.builder()
                .syllabusMonId(request.getSyllabusMonId())
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

    public void updateEntity(SyllabusMonHocTaiLieu entity, SyllabusMonHocTaiLieuRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setSyllabusMonId(request.getSyllabusMonId());
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

    public SyllabusMonHocTaiLieuResponse toResponse(SyllabusMonHocTaiLieu entity) {
        if (entity == null) {
            return null;
        }

        return SyllabusMonHocTaiLieuResponse.builder()
                .id(entity.getId())
                .syllabusMonId(entity.getSyllabusMonId())
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