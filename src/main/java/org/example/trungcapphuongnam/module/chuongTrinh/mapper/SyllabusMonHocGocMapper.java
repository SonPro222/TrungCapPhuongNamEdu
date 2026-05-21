package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocGoc;
import org.springframework.stereotype.Component;

@Component
public class SyllabusMonHocGocMapper {

    public SyllabusMonHocGoc toEntity(SyllabusMonHocGocRequest request) {
        if (request == null) {
            return null;
        }

        return SyllabusMonHocGoc.builder()
                .monHocId(request.getMonHocId())
                .ma(request.getMa())
                .ten(request.getTen())
                .viTri(request.getViTri())
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

    public void updateEntity(SyllabusMonHocGoc entity, SyllabusMonHocGocRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setMonHocId(request.getMonHocId());
        entity.setMa(request.getMa());
        entity.setTen(request.getTen());
        entity.setViTri(request.getViTri());
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

    public SyllabusMonHocGocResponse toResponse(SyllabusMonHocGoc entity) {
        if (entity == null) {
            return null;
        }

        return SyllabusMonHocGocResponse.builder()
                .id(entity.getId())
                .monHocId(entity.getMonHocId())
                .ma(entity.getMa())
                .ten(entity.getTen())
                .viTri(entity.getViTri())
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