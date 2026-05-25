package org.example.trungcapphuongnam.module.heThong.mapper;

import org.example.trungcapphuongnam.module.heThong.dto.response.TepDinhKemResponse;
import org.example.trungcapphuongnam.module.heThong.entity.TepDinhKem;
import org.springframework.stereotype.Component;

@Component
public class TepDinhKemMapper {

    public TepDinhKemResponse toResponse(TepDinhKem entity) {
        if (entity == null) {
            return null;
        }

        return TepDinhKemResponse.builder()
                .id(entity.getId())
                .tenGoc(entity.getTenGoc())
                .tenLuu(entity.getTenLuu())
                .duongDanTuongDoi(entity.getDuongDanTuongDoi())
                .duongDanDayDu(entity.getDuongDanDayDu())
                .duongDanTaiVe("/api/he-thong/tep-dinh-kem/" + entity.getId() + "/download")
                .duongDanCongKhai("/public/tep-dinh-kem/" + entity.getDuongDanTuongDoi())
                .loaiNoiDung(entity.getLoaiNoiDung())
                .contentType(entity.getContentType())
                .extension(entity.getExtension())
                .dungLuong(entity.getDungLuong())
                .module(entity.getModule())
                .nghiepVu(entity.getNghiepVu())
                .doiTuongId(entity.getDoiTuongId())
                .nguoiGuiLoai(entity.getNguoiGuiLoai())
                .nguoiGuiId(entity.getNguoiGuiId())
                .nguoiGuiTen(entity.getNguoiGuiTen())
                .moTa(entity.getMoTa())
                .trangThai(entity.getTrangThai())
                .ngayTao(entity.getNgayTao())
                .ngayCapNhat(entity.getNgayCapNhat())
                .build();
    }
}