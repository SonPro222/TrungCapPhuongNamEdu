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
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
    }

    public SyllabusMonHocResponse toResponse(SyllabusMonHoc entity) {
        if (entity == null) return null;
        return SyllabusMonHocResponse.builder()
                .id(entity.getId())
                .chuongTrinhMonId(entity.getChuongTrinhMonId())
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
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(SyllabusMonHoc entity, SyllabusMonHocRequest request) {
        if (entity == null || request == null) return;
        entity.setChuongTrinhMonId(request.getChuongTrinhMonId());
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
        entity.setCreatedAt(request.getCreatedAt());
        entity.setUpdatedAt(request.getUpdatedAt());
    }
}