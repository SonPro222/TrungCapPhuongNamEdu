package org.example.trungcapphuongnam.module.giangDay.mapper;

import org.example.trungcapphuongnam.module.giangDay.entity.SinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.dto.request.SinhVienLopHocPhanRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.SinhVienLopHocPhanResponse;
import org.springframework.stereotype.Component;

@Component
public class SinhVienLopHocPhanMapper {

    public SinhVienLopHocPhan toEntity(SinhVienLopHocPhanRequest request) {
        if (request == null) return null;
        return SinhVienLopHocPhan.builder()
                .sinhVienId(request.getSinhVienId())
                .lopHocPhanId(request.getLopHocPhanId())
                .ngayDangKy(request.getNgayDangKy())
                .trangThai(request.getTrangThai())
                .laHocLai(request.getLaHocLai())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public SinhVienLopHocPhanResponse toResponse(SinhVienLopHocPhan entity) {
        if (entity == null) return null;
        return SinhVienLopHocPhanResponse.builder()
                .id(entity.getId())
                .sinhVienId(entity.getSinhVienId())
                .lopHocPhanId(entity.getLopHocPhanId())
                .ngayDangKy(entity.getNgayDangKy())
                .trangThai(entity.getTrangThai())
                .laHocLai(entity.getLaHocLai())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(SinhVienLopHocPhan entity, SinhVienLopHocPhanRequest request) {
        if (entity == null || request == null) return;
        entity.setSinhVienId(request.getSinhVienId());
        entity.setLopHocPhanId(request.getLopHocPhanId());
        entity.setNgayDangKy(request.getNgayDangKy());
        entity.setTrangThai(request.getTrangThai());
        entity.setLaHocLai(request.getLaHocLai());
        entity.setGhiChu(request.getGhiChu());
    }
}
