package org.example.trungcapphuongnam.module.donTu.mapper;

import org.example.trungcapphuongnam.module.donTu.dto.response.DonTuDetailResponse;
import org.example.trungcapphuongnam.module.donTu.dto.response.DonTuLichSuResponse;
import org.example.trungcapphuongnam.module.donTu.dto.response.DonTuResponse;
import org.example.trungcapphuongnam.module.donTu.entity.DonTu;
import org.example.trungcapphuongnam.module.donTu.entity.DonTuLichSu;
import org.example.trungcapphuongnam.module.donTu.entity.LoaiDonTu;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DonTuMapper {

    public DonTuResponse toResponse(DonTu entity) {
        if (entity == null) return null;

        LoaiDonTu loaiDonTu = entity.getLoaiDonTu();
        SinhVien sinhVien = entity.getSinhVien();

        return DonTuResponse.builder()
                .id(entity.getId())
                .maDon(entity.getMaDon())
                .loaiDonTuId(loaiDonTu != null ? loaiDonTu.getId() : null)
                .loaiDonTuMa(loaiDonTu != null ? loaiDonTu.getMa() : null)
                .loaiDonTuTen(loaiDonTu != null ? loaiDonTu.getTen() : null)
                .sinhVienId(sinhVien != null ? sinhVien.getId() : null)
                .maSinhVien(sinhVien != null ? sinhVien.getMaSinhVien() : null)
                .hoTenSinhVien(sinhVien != null ? sinhVien.getHoTen() : null)
                .tieuDe(entity.getTieuDe())
                .noiDung(entity.getNoiDung())
                .lyDo(entity.getLyDo())
                .duLieu(entity.getDuLieu())
                .trangThai(entity.getTrangThai())
                .ngayGui(entity.getNgayGui())
                .ngayTiepNhan(entity.getNgayTiepNhan())
                .ngayDuyet(entity.getNgayDuyet())
                .ngayTuChoi(entity.getNgayTuChoi())
                .ngayHoanTat(entity.getNgayHoanTat())
                .nguoiTiepNhanId(entity.getNguoiTiepNhanId())
                .nguoiDuyetId(entity.getNguoiDuyetId())
                .yKienXuLy(entity.getYKienXuLy())
                .lyDoTuChoi(entity.getLyDoTuChoi())
                .doiTuongLienQuanType(entity.getDoiTuongLienQuanType())
                .doiTuongLienQuanId(entity.getDoiTuongLienQuanId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public DonTuDetailResponse toDetailResponse(DonTu entity, List<DonTuLichSu> lichSu) {
        return DonTuDetailResponse.builder()
                .donTu(toResponse(entity))
                .lichSu(lichSu != null
                        ? lichSu.stream().map(this::toLichSuResponse).collect(Collectors.toList())
                        : List.of())
                .build();
    }

    public DonTuLichSuResponse toLichSuResponse(DonTuLichSu entity) {
        if (entity == null) return null;

        return DonTuLichSuResponse.builder()
                .id(entity.getId())
                .donTuId(entity.getDonTu() != null ? entity.getDonTu().getId() : null)
                .hanhDong(entity.getHanhDong())
                .trangThaiCu(entity.getTrangThaiCu())
                .trangThaiMoi(entity.getTrangThaiMoi())
                .nguoiThucHienId(entity.getNguoiThucHienId())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
