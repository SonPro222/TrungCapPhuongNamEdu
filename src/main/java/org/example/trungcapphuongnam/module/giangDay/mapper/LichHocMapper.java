package org.example.trungcapphuongnam.module.giangDay.mapper;

import org.example.trungcapphuongnam.module.giangDay.entity.LichHoc;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LichHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LichHocResponse;
import org.springframework.stereotype.Component;

@Component
public class LichHocMapper {

    public LichHoc toEntity(LichHocRequest request) {
        if (request == null) return null;
        return LichHoc.builder()
                .lopHocPhanId(request.getLopHocPhanId())
                .giaoVienId(request.getGiaoVienId())
                .phongHocId(request.getPhongHocId())
                .caHocId(request.getCaHocId())
                .ngayHoc(request.getNgayHoc())
                .noiDungBuoiHoc(request.getNoiDungBuoiHoc())
                .trangThai(request.getTrangThai())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public LichHocResponse toResponse(LichHoc entity) {
        if (entity == null) return null;
        return LichHocResponse.builder()
                .id(entity.getId())
                .lopHocPhanId(entity.getLopHocPhanId())
                .giaoVienId(entity.getGiaoVienId())
                .phongHocId(entity.getPhongHocId())
                .caHocId(entity.getCaHocId())
                .ngayHoc(entity.getNgayHoc())
                .noiDungBuoiHoc(entity.getNoiDungBuoiHoc())
                .trangThai(entity.getTrangThai())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(LichHoc entity, LichHocRequest request) {
        if (entity == null || request == null) return;
        entity.setLopHocPhanId(request.getLopHocPhanId());
        entity.setGiaoVienId(request.getGiaoVienId());
        entity.setPhongHocId(request.getPhongHocId());
        entity.setCaHocId(request.getCaHocId());
        entity.setNgayHoc(request.getNgayHoc());
        entity.setNoiDungBuoiHoc(request.getNoiDungBuoiHoc());
        entity.setTrangThai(request.getTrangThai());
        entity.setGhiChu(request.getGhiChu());
    }
}
