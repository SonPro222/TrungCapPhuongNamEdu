package org.example.trungcapphuongnam.module.sinhVien.mapper;

import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienBaoLuuRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienBaoLuuResponse;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVienBaoLuu;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVienChuongTrinh;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SinhVienBaoLuuMapper {

    public SinhVienBaoLuu toEntity(
            SinhVienBaoLuuRequest request,
            SinhVien sinhVien,
            SinhVienChuongTrinh sinhVienChuongTrinh
    ) {
        LocalDate ngayBatDau = request.getNgayBatDau() != null
                ? request.getNgayBatDau()
                : LocalDate.now();

        Integer soKyBaoLuu = request.getSoKyBaoLuu() != null
                ? request.getSoKyBaoLuu()
                : 1;

        LocalDate ngayKetThucDuKien = request.getNgayKetThucDuKien() != null
                ? request.getNgayKetThucDuKien()
                : ngayBatDau.plusMonths(soKyBaoLuu * 6L);

        return SinhVienBaoLuu.builder()
                .sinhVien(sinhVien)
                .sinhVienChuongTrinh(sinhVienChuongTrinh)
                .chuongTrinhVersionIdCu(sinhVienChuongTrinh.getChuongTrinhVersionId())
                .lopHanhChinhIdCu(sinhVienChuongTrinh.getLopHanhChinhId())
                .ngayBatDau(ngayBatDau)
                .ngayKetThucDuKien(ngayKetThucDuKien)
                .soKyBaoLuu(soKyBaoLuu)
                .lyDo(request.getLyDo())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public SinhVienBaoLuuResponse toResponse(SinhVienBaoLuu entity) {
        return toResponse(entity, 0);
    }

    public SinhVienBaoLuuResponse toResponse(SinhVienBaoLuu entity, Integer soMonDuocCongNhan) {
        if (entity == null) return null;

        SinhVien sinhVien = entity.getSinhVien();
        SinhVienChuongTrinh sinhVienChuongTrinh = entity.getSinhVienChuongTrinh();

        return SinhVienBaoLuuResponse.builder()
                .id(entity.getId())
                .sinhVienId(sinhVien != null ? sinhVien.getId() : null)
                .maSinhVien(sinhVien != null ? sinhVien.getMaSinhVien() : null)
                .hoTen(sinhVien != null ? sinhVien.getHoTen() : null)
                .sinhVienChuongTrinhId(sinhVienChuongTrinh != null ? sinhVienChuongTrinh.getId() : null)
                .chuongTrinhVersionIdCu(entity.getChuongTrinhVersionIdCu())
                .lopHanhChinhIdCu(entity.getLopHanhChinhIdCu())
                .ngayBatDau(entity.getNgayBatDau())
                .ngayKetThucDuKien(entity.getNgayKetThucDuKien())
                .soKyBaoLuu(entity.getSoKyBaoLuu())
                .ngayDiHocLai(entity.getNgayDiHocLai())
                .chuongTrinhVersionIdHocLai(entity.getChuongTrinhVersionIdHocLai())
                .khungKyIdHocLai(entity.getKhungKyIdHocLai())
                .lopHanhChinhIdHocLai(entity.getLopHanhChinhIdHocLai())
                .lyDo(entity.getLyDo())
                .trangThai(entity.getTrangThai())
                .ghiChu(entity.getGhiChu())
                .soMonDuocCongNhan(soMonDuocCongNhan)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}