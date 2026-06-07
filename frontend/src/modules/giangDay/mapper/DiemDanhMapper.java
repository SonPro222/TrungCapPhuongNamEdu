package org.example.trungcapphuongnam.module.giangDay.mapper;

import org.example.trungcapphuongnam.module.giangDay.entity.DiemDanh;
import org.example.trungcapphuongnam.module.giangDay.dto.request.DiemDanhRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.DiemDanhResponse;
import org.springframework.stereotype.Component;

@Component
public class DiemDanhMapper {

    public DiemDanh toEntity(DiemDanhRequest request) {
        if (request == null) return null;
        return DiemDanh.builder()
                .lichHocId(request.getLichHocId())
                .sinhVienId(request.getSinhVienId())
                .trangThai(request.getTrangThai())
                .ghiChu(request.getGhiChu())
                .thoiGianDiemDanh(request.getThoiGianDiemDanh())
                .build();
    }

    public DiemDanhResponse toResponse(DiemDanh entity) {
        if (entity == null) return null;
        return DiemDanhResponse.builder()
                .id(entity.getId())
                .lichHocId(entity.getLichHocId())
                .sinhVienId(entity.getSinhVienId())
                .trangThai(entity.getTrangThai())
                .ghiChu(entity.getGhiChu())
                .thoiGianDiemDanh(entity.getThoiGianDiemDanh())
                .build();
    }

    public void updateEntity(DiemDanh entity, DiemDanhRequest request) {
        if (entity == null || request == null) return;
        entity.setLichHocId(request.getLichHocId());
        entity.setSinhVienId(request.getSinhVienId());
        entity.setTrangThai(request.getTrangThai());
        entity.setGhiChu(request.getGhiChu());
        entity.setThoiGianDiemDanh(request.getThoiGianDiemDanh());
    }
}
