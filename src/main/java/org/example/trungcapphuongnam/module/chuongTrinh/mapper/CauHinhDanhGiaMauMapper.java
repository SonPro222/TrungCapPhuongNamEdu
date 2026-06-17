package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.CauHinhDanhGiaMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.CauHinhDanhGiaMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.CauHinhDanhGiaMau;
import org.springframework.stereotype.Component;

@Component
public class CauHinhDanhGiaMauMapper {

    public CauHinhDanhGiaMau toEntity(CauHinhDanhGiaMauRequest request) {
        if (request == null) return null;
        return CauHinhDanhGiaMau.builder()
                .syllabusMonHocMauId(request.getSyllabusMonHocMauId())
                .tenCotDiem(request.getTenCotDiem())
                .loaiDiem(request.getLoaiDiem())
                .tyLe(request.getTyLe())
                .diemToiDa(request.getDiemToiDa())
                .thuTu(request.getThuTu())
                .batBuoc(request.getBatBuoc())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public CauHinhDanhGiaMauResponse toResponse(CauHinhDanhGiaMau entity) {
        if (entity == null) return null;
        return CauHinhDanhGiaMauResponse.builder()
                .id(entity.getId())
                .syllabusMonHocMauId(entity.getSyllabusMonHocMauId())
                .chuongTrinhMonId(null)
                .syllabusMonHocId(null)
                .tenCotDiem(entity.getTenCotDiem())
                .loaiDiem(entity.getLoaiDiem())
                .tyLe(entity.getTyLe())
                .diemToiDa(entity.getDiemToiDa())
                .thuTu(entity.getThuTu())
                .batBuoc(entity.getBatBuoc())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(CauHinhDanhGiaMau entity, CauHinhDanhGiaMauRequest request) {
        if (entity == null || request == null) return;
        entity.setSyllabusMonHocMauId(request.getSyllabusMonHocMauId());
        entity.setTenCotDiem(request.getTenCotDiem());
        entity.setLoaiDiem(request.getLoaiDiem());
        entity.setTyLe(request.getTyLe());
        entity.setDiemToiDa(request.getDiemToiDa());
        entity.setThuTu(request.getThuTu());
        entity.setBatBuoc(request.getBatBuoc());
        entity.setGhiChu(request.getGhiChu());
    }
}
