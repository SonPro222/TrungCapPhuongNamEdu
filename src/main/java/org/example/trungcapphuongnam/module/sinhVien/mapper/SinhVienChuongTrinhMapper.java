package org.example.trungcapphuongnam.module.sinhVien.mapper;

import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienChuongTrinhRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienChuongTrinhResponse;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVienChuongTrinh;
import org.springframework.stereotype.Component;

@Component
public class SinhVienChuongTrinhMapper {

    public SinhVienChuongTrinh toEntity(SinhVienChuongTrinhRequest request, SinhVien sinhVien) {
        if (request == null) return null;
        return SinhVienChuongTrinh.builder()
                .sinhVien(sinhVien)
                .chuongTrinhVersionId(request.getChuongTrinhVersionId())
                .lopHanhChinhId(request.getLopHanhChinhId())
                .ngayDangKy(request.getNgayDangKy())
                .ngayNhapHoc(request.getNgayNhapHoc())
                .trangThai(request.getTrangThai())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public void updateEntity(SinhVienChuongTrinh entity, SinhVienChuongTrinhRequest request, SinhVien sinhVien) {
        entity.setSinhVien(sinhVien);
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setLopHanhChinhId(request.getLopHanhChinhId());
        entity.setNgayDangKy(request.getNgayDangKy());
        entity.setNgayNhapHoc(request.getNgayNhapHoc());
        entity.setTrangThai(request.getTrangThai());
        entity.setGhiChu(request.getGhiChu());
    }

    public SinhVienChuongTrinhResponse toResponse(SinhVienChuongTrinh entity) {
        if (entity == null) return null;
        SinhVien sv = entity.getSinhVien();
        return SinhVienChuongTrinhResponse.builder()
                .id(entity.getId())
                .sinhVienId(sv != null ? sv.getId() : null)
                .maSinhVien(sv != null ? sv.getMaSinhVien() : null)
                .hoTen(sv != null ? sv.getHoTen() : null)
                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
                .lopHanhChinhId(entity.getLopHanhChinhId())
                .ngayDangKy(entity.getNgayDangKy())
                .ngayNhapHoc(entity.getNgayNhapHoc())
                .trangThai(entity.getTrangThai())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
