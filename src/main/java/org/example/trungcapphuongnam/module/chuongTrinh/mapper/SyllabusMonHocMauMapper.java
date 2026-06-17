package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocMau;
import org.springframework.stereotype.Component;

@Component
public class SyllabusMonHocMauMapper {

    public SyllabusMonHocMau toEntity(SyllabusMonHocMauRequest request) {
        if (request == null) {
            return null;
        }

        return SyllabusMonHocMau.builder()
                .monHocId(request.getMonHocId())
                .ma(request.getMa())
                .ten(request.getTen())
                .viTri(request.getViTri())
                .soTinChi(request.getSoTinChi())
                .soBuoiHoc(request.getSoBuoiHoc())
                .soTietMoiBuoi(request.getSoTietMoiBuoi())
                .soPhutMotTiet(request.getSoPhutMotTiet())
                .tinhChat(request.getTinhChat())
                .mucTieu(request.getMucTieu())
                .phuongPhapDanhGia(request.getPhuongPhapDanhGia())
                .dieuKienHoanThanh(request.getDieuKienHoanThanh())
                .huongDan(request.getHuongDan())
                .diemDatToiThieu(request.getDiemDatToiThieu())
                .donViDiem(request.getDonViDiem())
                .tyLeChuyenCanToiThieu(request.getTyLeChuyenCanToiThieu())
                .batBuocDuThi(request.getBatBuocDuThi())
                .congThucQuyDoi(request.getCongThucQuyDoi())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public void updateEntity(SyllabusMonHocMau entity, SyllabusMonHocMauRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setMonHocId(request.getMonHocId());
        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setViTri(request.getViTri());
        entity.setSoTinChi(request.getSoTinChi());
        entity.setSoBuoiHoc(request.getSoBuoiHoc());
        entity.setSoTietMoiBuoi(request.getSoTietMoiBuoi());
        entity.setSoPhutMotTiet(request.getSoPhutMotTiet());
        entity.setTinhChat(request.getTinhChat());
        entity.setMucTieu(request.getMucTieu());
        entity.setPhuongPhapDanhGia(request.getPhuongPhapDanhGia());
        entity.setDieuKienHoanThanh(request.getDieuKienHoanThanh());
        entity.setHuongDan(request.getHuongDan());
        entity.setDiemDatToiThieu(request.getDiemDatToiThieu());
        entity.setDonViDiem(request.getDonViDiem());
        entity.setTyLeChuyenCanToiThieu(request.getTyLeChuyenCanToiThieu());
        entity.setBatBuocDuThi(request.getBatBuocDuThi());
        entity.setCongThucQuyDoi(request.getCongThucQuyDoi());
        entity.setGhiChu(request.getGhiChu());
    }

    public SyllabusMonHocMauResponse toResponse(SyllabusMonHocMau entity) {
        if (entity == null) {
            return null;
        }

        return SyllabusMonHocMauResponse.builder()
                .id(entity.getId())
                .monHocId(entity.getMonHocId())
                .ma(entity.getMa())
                .ten(entity.getTen())
                .viTri(entity.getViTri())
                .soTinChi(entity.getSoTinChi())
                .soBuoiHoc(entity.getSoBuoiHoc())
                .soTietMoiBuoi(entity.getSoTietMoiBuoi())
                .soPhutMotTiet(entity.getSoPhutMotTiet())
                .tinhChat(entity.getTinhChat())
                .mucTieu(entity.getMucTieu())
                .phuongPhapDanhGia(entity.getPhuongPhapDanhGia())
                .dieuKienHoanThanh(entity.getDieuKienHoanThanh())
                .huongDan(entity.getHuongDan())
                .diemDatToiThieu(entity.getDiemDatToiThieu())
                .donViDiem(entity.getDonViDiem())
                .tyLeChuyenCanToiThieu(entity.getTyLeChuyenCanToiThieu())
                .batBuocDuThi(entity.getBatBuocDuThi())
                .congThucQuyDoi(entity.getCongThucQuyDoi())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}