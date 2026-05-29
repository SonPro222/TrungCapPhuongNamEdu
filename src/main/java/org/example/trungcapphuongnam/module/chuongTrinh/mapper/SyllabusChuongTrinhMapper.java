package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusChuongTrinhRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusChuongTrinhResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongTrinh;
import org.springframework.stereotype.Component;

@Component
public class SyllabusChuongTrinhMapper {

    public SyllabusChuongTrinh toEntity(SyllabusChuongTrinhRequest request) {
        if (request == null) return null;
        return SyllabusChuongTrinh.builder()
                .chuongTrinhVersionId(request.getChuongTrinhVersionId())
                .moTaTongQuan(request.getMoTaTongQuan())
                .syllabusChuongTrinhGocId(request.getSyllabusChuongTrinhGocId())
                .mucDich(request.getMucDich())
                .yeuCauDaoTao(request.getYeuCauDaoTao())
                .phuongPhapDaoTao(request.getPhuongPhapDaoTao())
                .ghiChu(request.getGhiChu())
                .ma(request.getMa())
                .ten(request.getTen())
                .mucTieu(request.getMucTieu())
                .doiTuongTuyenSinh(request.getDoiTuongTuyenSinh())
                .thoiGianDaoTao(request.getThoiGianDaoTao())
                .khoiLuongKienThuc(request.getKhoiLuongKienThuc())
                .dieuKienTotNghiep(request.getDieuKienTotNghiep())
                .phuongPhapDanhGia(request.getPhuongPhapDanhGia())
                .huongDanThucHien(request.getHuongDanThucHien())
                .duongDan(request.getDuongDan())
                .build();
    }

    public SyllabusChuongTrinhResponse toResponse(SyllabusChuongTrinh entity) {
        if (entity == null) return null;
        return SyllabusChuongTrinhResponse.builder()
                .id(entity.getId())
                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
                .syllabusChuongTrinhGocId(entity.getSyllabusChuongTrinhGocId())
                .moTaTongQuan(entity.getMoTaTongQuan())
                .mucDich(entity.getMucDich())
                .yeuCauDaoTao(entity.getYeuCauDaoTao())
                .phuongPhapDaoTao(entity.getPhuongPhapDaoTao())
                .ghiChu(entity.getGhiChu())
                .ma(entity.getMa())
                .ten(entity.getTen())
                .mucTieu(entity.getMucTieu())
                .doiTuongTuyenSinh(entity.getDoiTuongTuyenSinh())
                .thoiGianDaoTao(entity.getThoiGianDaoTao())
                .khoiLuongKienThuc(entity.getKhoiLuongKienThuc())
                .dieuKienTotNghiep(entity.getDieuKienTotNghiep())
                .phuongPhapDanhGia(entity.getPhuongPhapDanhGia())
                .huongDanThucHien(entity.getHuongDanThucHien())
                .duongDan(entity.getDuongDan())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(SyllabusChuongTrinh entity, SyllabusChuongTrinhRequest request) {
        if (entity == null || request == null) return;
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setSyllabusChuongTrinhGocId(request.getSyllabusChuongTrinhGocId());
        entity.setMoTaTongQuan(request.getMoTaTongQuan());
        entity.setMucDich(request.getMucDich());
        entity.setYeuCauDaoTao(request.getYeuCauDaoTao());
        entity.setPhuongPhapDaoTao(request.getPhuongPhapDaoTao());
        entity.setGhiChu(request.getGhiChu());
        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setMucTieu(request.getMucTieu());
        entity.setDoiTuongTuyenSinh(request.getDoiTuongTuyenSinh());
        entity.setThoiGianDaoTao(request.getThoiGianDaoTao());
        entity.setKhoiLuongKienThuc(request.getKhoiLuongKienThuc());
        entity.setDieuKienTotNghiep(request.getDieuKienTotNghiep());
        entity.setPhuongPhapDanhGia(request.getPhuongPhapDanhGia());
        entity.setHuongDanThucHien(request.getHuongDanThucHien());
        entity.setDuongDan(request.getDuongDan());

    }
}
