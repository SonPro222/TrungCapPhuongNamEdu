package org.example.trungcapphuongnam.module.giangDay.mapper;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiem;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiemMau;
import org.example.trungcapphuongnam.module.diem.entity.CauHinhDanhGia;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CauHinhDanhGiaGiangDayMapper {

    public CauHinhDanhGia toEntityFromQuyDoiDiem(
            Long syllabusMonHocId,
            QuyDoiDiem quyDoiDiem,
            String tenCotDiem,
            String loaiDiem
    ) {
        if (quyDoiDiem == null) {
            return null;
        }

        return CauHinhDanhGia.builder()
                .syllabusMonHocId(syllabusMonHocId)
                .tenCotDiem(tenCotDiem)
                .loaiDiem(loaiDiem)
                .diemToiDa(quyDoiDiem.getDiemToiDa() == null ? BigDecimal.TEN : quyDoiDiem.getDiemToiDa())
                .thuTu(quyDoiDiem.getThuTu())
                .build();
    }

    public CauHinhDanhGia toEntityFromQuyDoiDiemMau(
            Long syllabusMonHocId,
            QuyDoiDiemMau mau,
            String loaiDiem
    ) {
        if (mau == null) {
            return null;
        }

        return CauHinhDanhGia.builder()
                .syllabusMonHocId(syllabusMonHocId)
                .tenCotDiem(mau.getTen())
                .loaiDiem(loaiDiem)
                .diemToiDa(mau.getDiemToiDa() == null ? BigDecimal.TEN : mau.getDiemToiDa())
                .thuTu(mau.getThuTu())
                .build();
    }
}
