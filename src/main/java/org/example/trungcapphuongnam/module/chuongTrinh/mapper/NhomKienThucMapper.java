package org.example.trungcapphuongnam.module.chuongTrinh.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomKienThucRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomKienThucResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomKienThuc;
import org.springframework.stereotype.Component;

@Component
public class NhomKienThucMapper {

    public NhomKienThuc toEntity(NhomKienThucRequest request) {
        if (request == null) return null;
        return NhomKienThuc.builder()
                .chuongTrinhVersionId(request.getChuongTrinhVersionId())
                .ma(request.getMa())
                .nhomKienThucGocId(request.getNhomKienThucGocId())
                .ten(request.getTen())
                .thuTu(request.getThuTu())
                .loaiNhom(request.getLoaiNhom())
                .tongTinChi(request.getTongTinChi())
                .tongSoGio(request.getTongSoGio())
                .tongGioLyThuyet(request.getTongGioLyThuyet())
                .tongGioThucHanh(request.getTongGioThucHanh())
                .tongGioKiemTra(request.getTongGioKiemTra())
                .moTa(request.getMoTa())

                .build();
    }

    public NhomKienThucResponse toResponse(NhomKienThuc entity) {
        if (entity == null) return null;
        return NhomKienThucResponse.builder()
                .id(entity.getId())
                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
                .ma(entity.getMa())
                .nhomKienThucGocId(entity.getNhomKienThucGocId())
                .ten(entity.getTen())
                .thuTu(entity.getThuTu())
                .loaiNhom(entity.getLoaiNhom())
                .tongTinChi(entity.getTongTinChi())
                .tongSoGio(entity.getTongSoGio())
                .tongGioLyThuyet(entity.getTongGioLyThuyet())
                .tongGioThucHanh(entity.getTongGioThucHanh())
                .tongGioKiemTra(entity.getTongGioKiemTra())
                .moTa(entity.getMoTa())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntity(NhomKienThuc entity, NhomKienThucRequest request) {
        if (entity == null || request == null) return;
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setMa(request.getMa());
        entity.setNhomKienThucGocId(request.getNhomKienThucGocId());
        entity.setTen(request.getTen());
        entity.setThuTu(request.getThuTu());
        entity.setLoaiNhom(request.getLoaiNhom());
        entity.setTongTinChi(request.getTongTinChi());
        entity.setTongSoGio(request.getTongSoGio());
        entity.setTongGioLyThuyet(request.getTongGioLyThuyet());
        entity.setTongGioThucHanh(request.getTongGioThucHanh());
        entity.setTongGioKiemTra(request.getTongGioKiemTra());
        entity.setMoTa(request.getMoTa());


    }
}
