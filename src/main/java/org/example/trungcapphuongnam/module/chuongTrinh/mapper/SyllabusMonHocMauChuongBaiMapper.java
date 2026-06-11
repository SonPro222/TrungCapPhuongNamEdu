package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocMauChuongBaiRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauChuongBaiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocMauChuongBai;
import org.springframework.stereotype.Component;

@Component
public class SyllabusMonHocMauChuongBaiMapper {

    public SyllabusMonHocMauChuongBai toEntity(SyllabusMonHocMauChuongBaiRequest request) {
        if (request == null) {
            return null;
        }

        return SyllabusMonHocMauChuongBai.builder()
                .syllabusMonHocMauId(request.getSyllabusMonHocMauId())
                .maChuong(request.getMaChuong())
                .ten(request.getTen())
                .mucTieu(request.getMucTieu())
                .noiDung(request.getNoiDung())
                .thuTu(request.getThuTu())
                .tongGio(request.getTongGio())
                .gioLyThuyet(request.getGioLyThuyet())
                .gioThucHanh(request.getGioThucHanh())
                .gioKiemTra(request.getGioKiemTra())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public void updateEntity(SyllabusMonHocMauChuongBai entity, SyllabusMonHocMauChuongBaiRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setSyllabusMonHocMauId(request.getSyllabusMonHocMauId());
        entity.setMaChuong(request.getMaChuong());
        entity.setTen(request.getTen());
        entity.setMucTieu(request.getMucTieu());
        entity.setNoiDung(request.getNoiDung());
        entity.setThuTu(request.getThuTu());
        entity.setTongGio(request.getTongGio());
        entity.setGioLyThuyet(request.getGioLyThuyet());
        entity.setGioThucHanh(request.getGioThucHanh());
        entity.setGioKiemTra(request.getGioKiemTra());
        entity.setGhiChu(request.getGhiChu());
    }

    public SyllabusMonHocMauChuongBaiResponse toResponse(SyllabusMonHocMauChuongBai entity) {
        if (entity == null) {
            return null;
        }

        return SyllabusMonHocMauChuongBaiResponse.builder()
                .id(entity.getId())
                .syllabusMonHocMauId(entity.getSyllabusMonHocMauId())
                .maChuong(entity.getMaChuong())
                .ten(entity.getTen())
                .mucTieu(entity.getMucTieu())
                .noiDung(entity.getNoiDung())
                .thuTu(entity.getThuTu())
                .tongGio(entity.getTongGio())
                .gioLyThuyet(entity.getGioLyThuyet())
                .gioThucHanh(entity.getGioThucHanh())
                .gioKiemTra(entity.getGioKiemTra())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}