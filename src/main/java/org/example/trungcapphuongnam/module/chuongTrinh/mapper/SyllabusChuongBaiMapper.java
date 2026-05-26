package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusChuongBaiRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusChuongBaiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongBai;
import org.springframework.stereotype.Component;

@Component
public class SyllabusChuongBaiMapper {

    public SyllabusChuongBai toEntity(SyllabusChuongBaiRequest request) {
        if (request == null) return null;
        return SyllabusChuongBai.builder()
                .syllabusMonId(request.getSyllabusMonId())
                .ten(request.getTen())
                .tongGio(request.getTongGio())
                .gioLyThuyet(request.getGioLyThuyet())
                .gioThucHanh(request.getGioThucHanh())
                .gioKiemTra(request.getGioKiemTra())
                .noiDung(request.getNoiDung())
                .mucTieu(request.getMucTieu())
                .thuTu(request.getThuTu())
                .syllabusMonHocGocId(request.getSyllabusMonHocGocId())
                .ghiChu(request.getGhiChu())

                .build();
    }

    public SyllabusChuongBaiResponse toResponse(SyllabusChuongBai entity) {
        if (entity == null) return null;
        return SyllabusChuongBaiResponse.builder()
                .id(entity.getId())
                .syllabusMonId(entity.getSyllabusMonId())
                .ten(entity.getTen())
                .tongGio(entity.getTongGio())
                .gioLyThuyet(entity.getGioLyThuyet())
                .gioThucHanh(entity.getGioThucHanh())
                .gioKiemTra(entity.getGioKiemTra())
                .noiDung(entity.getNoiDung())
                .mucTieu(entity.getMucTieu())
                .thuTu(entity.getThuTu())
                .syllabusMonHocGocId(entity.getSyllabusMonHocGocId())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(SyllabusChuongBai entity, SyllabusChuongBaiRequest request) {
        if (entity == null || request == null) return;
        entity.setSyllabusMonId(request.getSyllabusMonId());
        entity.setTen(request.getTen());
        entity.setTongGio(request.getTongGio());
        entity.setGioLyThuyet(request.getGioLyThuyet());
        entity.setGioThucHanh(request.getGioThucHanh());
        entity.setGioKiemTra(request.getGioKiemTra());
        entity.setNoiDung(request.getNoiDung());
        entity.setMucTieu(request.getMucTieu());
        entity.setThuTu(request.getThuTu());
        entity.setSyllabusMonHocGocId(request.getSyllabusMonHocGocId());
        entity.setGhiChu(request.getGhiChu());


    }
}
