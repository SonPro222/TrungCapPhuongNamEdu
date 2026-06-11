package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusChuongTrinhMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusChuongTrinhMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongTrinhMau;
import org.springframework.stereotype.Component;

@Component
public class SyllabusChuongTrinhMauMapper {

    public SyllabusChuongTrinhMau toEntity(SyllabusChuongTrinhMauRequest request) {
        if (request == null) {
            return null;
        }

        return SyllabusChuongTrinhMau.builder()
                .chuongTrinhId(request.getChuongTrinhId())
                .ma(request.getMa())
                .ten(request.getTen())

                .mucTieu(request.getMucTieu())
                .doiTuongTuyenSinh(request.getDoiTuongTuyenSinh())
                .thoiGianDaoTao(request.getThoiGianDaoTao())
                .khoiLuongKienThuc(request.getKhoiLuongKienThuc())
                .dieuKienTotNghiep(request.getDieuKienTotNghiep())
                .phuongPhapDaoTao(request.getPhuongPhapDaoTao())
                .phuongPhapDanhGia(request.getPhuongPhapDanhGia())
                .huongDanThucHien(request.getHuongDanThucHien())
                .ghiChu(request.getGhiChu())
                .duongDan(request.getDuongDan())
                .build();
    }

    public void updateEntity(SyllabusChuongTrinhMau entity, SyllabusChuongTrinhMauRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setChuongTrinhId(request.getChuongTrinhId());
        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setMucTieu(request.getMucTieu());
        entity.setDoiTuongTuyenSinh(request.getDoiTuongTuyenSinh());
        entity.setThoiGianDaoTao(request.getThoiGianDaoTao());
        entity.setKhoiLuongKienThuc(request.getKhoiLuongKienThuc());
        entity.setDieuKienTotNghiep(request.getDieuKienTotNghiep());
        entity.setPhuongPhapDaoTao(request.getPhuongPhapDaoTao());
        entity.setPhuongPhapDanhGia(request.getPhuongPhapDanhGia());
        entity.setHuongDanThucHien(request.getHuongDanThucHien());
        entity.setGhiChu(request.getGhiChu());
        entity.setDuongDan(request.getDuongDan());
    }

    public SyllabusChuongTrinhMauResponse toResponse(SyllabusChuongTrinhMau entity) {
        if (entity == null) {
            return null;
        }

        return SyllabusChuongTrinhMauResponse.builder()
                .id(entity.getId())
                .chuongTrinhId(entity.getChuongTrinhId())
                .ma(entity.getMa())
                .ten(entity.getTen())
                .mucTieu(entity.getMucTieu())
                .doiTuongTuyenSinh(entity.getDoiTuongTuyenSinh())
                .thoiGianDaoTao(entity.getThoiGianDaoTao())
                .khoiLuongKienThuc(entity.getKhoiLuongKienThuc())
                .dieuKienTotNghiep(entity.getDieuKienTotNghiep())
                .phuongPhapDaoTao(entity.getPhuongPhapDaoTao())
                .phuongPhapDanhGia(entity.getPhuongPhapDanhGia())
                .huongDanThucHien(entity.getHuongDanThucHien())
                .ghiChu(entity.getGhiChu())
                .duongDan(entity.getDuongDan())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}