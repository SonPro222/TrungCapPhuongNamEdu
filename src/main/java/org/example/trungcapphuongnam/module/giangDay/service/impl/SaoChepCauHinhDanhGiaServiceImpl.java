package org.example.trungcapphuongnam.module.giangDay.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.CauHinhDanhGiaMau;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.CauHinhDanhGiaMauRepository;
import org.example.trungcapphuongnam.module.diem.entity.CauHinhDanhGia;
import org.example.trungcapphuongnam.module.diem.repository.CauHinhDanhGiaRepository;
import org.example.trungcapphuongnam.module.giangDay.service.SaoChepCauHinhDanhGiaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SaoChepCauHinhDanhGiaServiceImpl implements SaoChepCauHinhDanhGiaService {

    private final CauHinhDanhGiaMauRepository cauHinhDanhGiaMauRepository;
    private final CauHinhDanhGiaRepository cauHinhDanhGiaRepository;

    @Override
    public void saoChepTuChuongTrinhMonSangLopHocPhan(Long lopHocPhanId, Long chuongTrinhMonId) {
        if (lopHocPhanId == null) {
            throw new BadRequestException("Lớp học phần không hợp lệ");
        }

        if (chuongTrinhMonId == null) {
            throw new BadRequestException("Chương trình môn không hợp lệ");
        }

        List<CauHinhDanhGiaMau> danhSachMau = cauHinhDanhGiaMauRepository
                .findByChuongTrinhMonIdOrderByThuTuAscIdAsc(chuongTrinhMonId);

        if (danhSachMau.isEmpty()) {
            throw new BadRequestException("Chương trình môn chưa có cấu hình cột điểm mẫu");
        }

        BigDecimal tongTyLe = danhSachMau.stream()
                .map(CauHinhDanhGiaMau::getTyLe)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (tongTyLe.compareTo(new BigDecimal("100")) != 0) {
            throw new BadRequestException("Tổng tỷ lệ cột điểm mẫu của chương trình môn phải bằng 100%");
        }

        for (CauHinhDanhGiaMau mau : danhSachMau) {
            if (cauHinhDanhGiaRepository.existsByLopHocPhanIdAndTenCotDiem(lopHocPhanId, mau.getTenCotDiem())) {
                continue;
            }

            CauHinhDanhGia cauHinhDanhGia = CauHinhDanhGia.builder()
                    .lopHocPhanId(lopHocPhanId)
                    .tenCotDiem(mau.getTenCotDiem())
                    .loaiDiem(mau.getLoaiDiem())
                    .tyLe(mau.getTyLe())
                    .diemToiDa(mau.getDiemToiDa())
                    .thuTu(mau.getThuTu())
                    .build();

            cauHinhDanhGiaRepository.save(cauHinhDanhGia);
        }
    }
}