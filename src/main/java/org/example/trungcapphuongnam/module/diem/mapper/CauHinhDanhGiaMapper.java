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
                .lopHocPhanId(request.getLopHocPhanId())
                .tenCotDiem(request.getTenCotDiem())
                .loaiDiem(request.getLoaiDiem())
                .tyLe(request.getTyLe())
                .diemToiDa(request.getDiemToiDa())
                .thuTu(request.getThuTu())
                .build();
    }

    public CauHinhDanhGiaResponse toResponse(CauHinhDanhGia entity) {
        if (entity == null) return null;
        return CauHinhDanhGiaResponse.builder()
                .id(entity.getId())
                .lopHocPhanId(entity.getLopHocPhanId())
                .tenCotDiem(entity.getTenCotDiem())
                .loaiDiem(entity.getLoaiDiem())
                .tyLe(entity.getTyLe())
                .diemToiDa(entity.getDiemToiDa())
                .thuTu(entity.getThuTu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(CauHinhDanhGia entity, CauHinhDanhGiaRequest request) {
        if (entity == null || request == null) return;
        entity.setLopHocPhanId(request.getLopHocPhanId());
        entity.setTenCotDiem(request.getTenCotDiem());
        entity.setLoaiDiem(request.getLoaiDiem());
        entity.setTyLe(request.getTyLe());
        entity.setDiemToiDa(request.getDiemToiDa());
        entity.setThuTu(request.getThuTu());
    }
}
