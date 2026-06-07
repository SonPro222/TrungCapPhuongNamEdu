package org.example.trungcapphuongnam.module.diem.mapper;

import org.example.trungcapphuongnam.module.diem.dto.request.CauHinhDanhGiaRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.CauHinhDanhGiaResponse;
import org.example.trungcapphuongnam.module.diem.entity.CauHinhDanhGia;
import org.springframework.stereotype.Component;

@Component
public class CauHinhDanhGiaMapper {

    public CauHinhDanhGia toEntity(CauHinhDanhGiaRequest request) {
        if (request == null) return null;
        return CauHinhDanhGia.builder()
                .syllabusMonHocId(request.getSyllabusMonHocId())
                .tenCotDiem(request.getTenCotDiem())
                .loaiDiem(request.getLoaiDiem())
                .tyLe(request.getTyLe())
                .diemToiDa(request.getDiemToiDa())
                .thuTu(request.getThuTu())
                .batBuoc(request.getBatBuoc())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public CauHinhDanhGiaResponse toResponse(CauHinhDanhGia entity) {
        return toResponse(entity, null);
    }

    public CauHinhDanhGiaResponse toResponse(CauHinhDanhGia entity, Long lopHocPhanId) {
        if (entity == null) return null;
        return CauHinhDanhGiaResponse.builder()
                .id(entity.getId())
                .syllabusMonHocId(entity.getSyllabusMonHocId())
                .lopHocPhanId(lopHocPhanId)
                .tenCotDiem(entity.getTenCotDiem())
                .loaiDiem(entity.getLoaiDiem())
                .nguonDiem(null)
                .tyLe(entity.getTyLe())
                .diemToiDa(entity.getDiemToiDa())
                .thuTu(entity.getThuTu())
                .batBuoc(entity.getBatBuoc())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(CauHinhDanhGia entity, CauHinhDanhGiaRequest request) {
        if (entity == null || request == null) return;
        entity.setSyllabusMonHocId(request.getSyllabusMonHocId());
        entity.setTenCotDiem(request.getTenCotDiem());
        entity.setLoaiDiem(request.getLoaiDiem());
        entity.setTyLe(request.getTyLe());
        entity.setDiemToiDa(request.getDiemToiDa());
        entity.setThuTu(request.getThuTu());
        entity.setBatBuoc(request.getBatBuoc());
        entity.setGhiChu(request.getGhiChu());
    }
}
