package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHoc;
import org.springframework.stereotype.Component;

@Component
public class SyllabusMonHocMapper {

    public SyllabusMonHoc toEntity(SyllabusMonHocRequest request) {
        if (request == null) return null;
        return SyllabusMonHoc.builder()
                .chuongTrinhMonId(request.getChuongTrinhMonId())
                .syllabusMonHocMauId(request.getSyllabusMonHocMauId())
                .viTri(request.getViTri())
                .soTinChi(request.getSoTinChi())
                .tinhChat(request.getTinhChat())
                .soBuoiHoc(request.getSoBuoiHoc())
                .soTietMoiBuoi(request.getSoTietMoiBuoi())
                .soPhutMotTiet(request.getSoPhutMotTiet())
                .mucTieu(request.getMucTieu())
                .phuongPhapDanhGia(request.getPhuongPhapDanhGia())
                .dieuKienHoanThanh(request.getDieuKienHoanThanh())
                .huongDan(request.getHuongDan())
                .diemDatToiThieu(request.getDiemDatToiThieu())
                .donViDiem(request.getDonViDiem())
                .tyLeChuyenCanToiThieu(request.getTyLeChuyenCanToiThieu())
                .batBuocDuThi(request.getBatBuocDuThi())
                .congThucQuyDoi(request.getCongThucQuyDoi())

                .monHocId(request.getMonHocId())
                .ma(request.getMa())
                .ten(request.getTen())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public SyllabusMonHocResponse toResponse(SyllabusMonHoc entity) {
        if (entity == null) return null;
        return SyllabusMonHocResponse.builder()
                .id(entity.getId())
                .chuongTrinhMonId(entity.getChuongTrinhMonId())
                .syllabusMonHocMauId(entity.getSyllabusMonHocMauId())
                .viTri(entity.getViTri())
                .soTinChi(entity.getSoTinChi())
                .tinhChat(entity.getTinhChat())
                .soBuoiHoc(entity.getSoBuoiHoc())
                .soTietMoiBuoi(entity.getSoTietMoiBuoi())
                .soPhutMotTiet(entity.getSoPhutMotTiet())
                .mucTieu(entity.getMucTieu())
                .phuongPhapDanhGia(entity.getPhuongPhapDanhGia())
                .dieuKienHoanThanh(entity.getDieuKienHoanThanh())
                .huongDan(entity.getHuongDan())
                .diemDatToiThieu(entity.getDiemDatToiThieu())
                .donViDiem(entity.getDonViDiem())
                .tyLeChuyenCanToiThieu(entity.getTyLeChuyenCanToiThieu())
                .batBuocDuThi(entity.getBatBuocDuThi())
                .congThucQuyDoi(entity.getCongThucQuyDoi())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .monHocId(entity.getMonHocId())
                .ma(entity.getMa())
                .ten(entity.getTen())
                .ghiChu(entity.getGhiChu())
                .build();
    }

    public void updateEntity(SyllabusMonHoc entity, SyllabusMonHocRequest request) {
        if (entity == null || request == null) return;
        entity.setChuongTrinhMonId(request.getChuongTrinhMonId());
        entity.setSyllabusMonHocMauId(request.getSyllabusMonHocMauId());
        entity.setViTri(request.getViTri());
        entity.setSoTinChi(request.getSoTinChi());
        entity.setTinhChat(request.getTinhChat());
        entity.setSoBuoiHoc(request.getSoBuoiHoc());
        entity.setSoTietMoiBuoi(request.getSoTietMoiBuoi());
        entity.setSoPhutMotTiet(request.getSoPhutMotTiet());
        entity.setMucTieu(request.getMucTieu());
        entity.setPhuongPhapDanhGia(request.getPhuongPhapDanhGia());
        entity.setDieuKienHoanThanh(request.getDieuKienHoanThanh());
        entity.setHuongDan(request.getHuongDan());
        entity.setDiemDatToiThieu(request.getDiemDatToiThieu());
        entity.setDonViDiem(request.getDonViDiem());
        entity.setTyLeChuyenCanToiThieu(request.getTyLeChuyenCanToiThieu());
        entity.setBatBuocDuThi(request.getBatBuocDuThi());
        entity.setCongThucQuyDoi(request.getCongThucQuyDoi());

        entity.setMonHocId(request.getMonHocId());
        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setGhiChu(request.getGhiChu());

    }
}