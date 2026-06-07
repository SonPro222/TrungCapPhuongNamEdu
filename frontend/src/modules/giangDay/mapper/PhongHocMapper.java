package org.example.trungcapphuongnam.module.giangDay.mapper;

import org.example.trungcapphuongnam.module.giangDay.entity.PhongHoc;
import org.example.trungcapphuongnam.module.giangDay.dto.request.PhongHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.PhongHocResponse;
import org.springframework.stereotype.Component;

@Component
public class PhongHocMapper {

    public PhongHoc toEntity(PhongHocRequest request) {
        if (request == null) return null;
        return PhongHoc.builder()
                .maPhong(request.getMaPhong())
                .tenPhong(request.getTenPhong())
                .loaiPhong(request.getLoaiPhong())
                .sucChua(request.getSucChua())
                .diaDiem(request.getDiaDiem())
                .trangThai(request.getTrangThai())
                .build();
    }

    public PhongHocResponse toResponse(PhongHoc entity) {
        if (entity == null) return null;
        return PhongHocResponse.builder()
                .id(entity.getId())
                .maPhong(entity.getMaPhong())
                .tenPhong(entity.getTenPhong())
                .loaiPhong(entity.getLoaiPhong())
                .sucChua(entity.getSucChua())
                .diaDiem(entity.getDiaDiem())
                .trangThai(entity.getTrangThai())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(PhongHoc entity, PhongHocRequest request) {
        if (entity == null || request == null) return;
        entity.setMaPhong(request.getMaPhong());
        entity.setTenPhong(request.getTenPhong());
        entity.setLoaiPhong(request.getLoaiPhong());
        entity.setSucChua(request.getSucChua());
        entity.setDiaDiem(request.getDiaDiem());
        entity.setTrangThai(request.getTrangThai());
    }
}
