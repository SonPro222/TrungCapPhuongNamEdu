package org.example.trungcapphuongnam.module.diem.mapper;

import org.example.trungcapphuongnam.module.diem.dto.request.DiemChiTietRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.DiemChiTietResponse;
import org.example.trungcapphuongnam.module.diem.entity.DiemChiTiet;
import org.springframework.stereotype.Component;

@Component
public class DiemChiTietMapper {

    public DiemChiTiet toEntity(DiemChiTietRequest request) {
        if (request == null) return null;
        return DiemChiTiet.builder()
                .sinhVienId(request.getSinhVienId())
                .lopHocPhanId(request.getLopHocPhanId())
                .cauHinhDanhGiaId(request.getCauHinhDanhGiaId())
                .baiKiemTraId(request.getBaiKiemTraId())
                .diem(request.getDiem())
                .nhanXet(request.getNhanXet())
                .giaoVienChamId(request.getGiaoVienChamId())
                .ngayCham(request.getNgayCham())
                .trangThai(request.getTrangThai())
                .build();
    }

    public DiemChiTietResponse toResponse(DiemChiTiet entity) {
        if (entity == null) return null;
        return DiemChiTietResponse.builder()
                .id(entity.getId())
                .sinhVienId(entity.getSinhVienId())
                .lopHocPhanId(entity.getLopHocPhanId())
                .cauHinhDanhGiaId(entity.getCauHinhDanhGiaId())
                .baiKiemTraId(entity.getBaiKiemTraId())
                .diem(entity.getDiem())
                .nhanXet(entity.getNhanXet())
                .giaoVienChamId(entity.getGiaoVienChamId())
                .ngayCham(entity.getNgayCham())
                .trangThai(entity.getTrangThai())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(DiemChiTiet entity, DiemChiTietRequest request) {
        if (entity == null || request == null) return;
        entity.setSinhVienId(request.getSinhVienId());
        entity.setLopHocPhanId(request.getLopHocPhanId());
        entity.setCauHinhDanhGiaId(request.getCauHinhDanhGiaId());
        entity.setBaiKiemTraId(request.getBaiKiemTraId());
        entity.setDiem(request.getDiem());
        entity.setNhanXet(request.getNhanXet());
        entity.setGiaoVienChamId(request.getGiaoVienChamId());
        entity.setNgayCham(request.getNgayCham());
        entity.setTrangThai(request.getTrangThai());
    }
}
