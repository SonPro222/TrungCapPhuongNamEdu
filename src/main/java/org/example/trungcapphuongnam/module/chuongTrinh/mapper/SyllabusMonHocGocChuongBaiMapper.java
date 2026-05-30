package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocGocChuongBaiRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocGocChuongBaiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocGocChuongBai;
import org.springframework.stereotype.Component;

@Component
public class SyllabusMonHocGocChuongBaiMapper {

    public SyllabusMonHocGocChuongBai toEntity(SyllabusMonHocGocChuongBaiRequest request) {
        if (request == null) {
            return null;
        }

        return SyllabusMonHocGocChuongBai.builder()
                .syllabusMonHocGocId(request.getSyllabusMonHocGocId())
                .ten(request.getTen())
                .noiDung(request.getNoiDung())
                .thuTu(request.getThuTu())
                .tongGio(request.getTongGio())
                .gioLyThuyet(request.getGioLyThuyet())
                .gioThucHanh(request.getGioThucHanh())
                .gioKiemTra(request.getGioKiemTra())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public void updateEntity(SyllabusMonHocGocChuongBai entity, SyllabusMonHocGocChuongBaiRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setSyllabusMonHocGocId(request.getSyllabusMonHocGocId());
        entity.setTen(request.getTen());
        entity.setNoiDung(request.getNoiDung());
        entity.setThuTu(request.getThuTu());
        entity.setTongGio(request.getTongGio());
        entity.setGioLyThuyet(request.getGioLyThuyet());
        entity.setGioThucHanh(request.getGioThucHanh());
        entity.setGioKiemTra(request.getGioKiemTra());
        entity.setGhiChu(request.getGhiChu());
    }

    public SyllabusMonHocGocChuongBaiResponse toResponse(SyllabusMonHocGocChuongBai entity) {
        if (entity == null) {
            return null;
        }

        return SyllabusMonHocGocChuongBaiResponse.builder()
                .id(entity.getId())
                .syllabusMonHocGocId(entity.getSyllabusMonHocGocId())
                .ten(entity.getTen())
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