package org.example.trungcapphuongnam.module.giangDay.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMonQuyDoiDiemMau;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiemMau;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhMonQuyDoiDiemMauRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.QuyDoiDiemMauRepository;
import org.example.trungcapphuongnam.module.diem.entity.CauHinhDanhGia;
import org.example.trungcapphuongnam.module.diem.repository.CauHinhDanhGiaRepository;
import org.example.trungcapphuongnam.module.giangDay.service.SaoChepCotDiemTuChuongTrinhMonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SaoChepCotDiemTuChuongTrinhMonServiceImpl implements SaoChepCotDiemTuChuongTrinhMonService {

    private final ChuongTrinhMonQuyDoiDiemMauRepository chuongTrinhMonQuyDoiDiemMauRepository;
    private final QuyDoiDiemMauRepository quyDoiDiemMauRepository;
    private final CauHinhDanhGiaRepository cauHinhDanhGiaRepository;

    @Override
    public void saoChep(Long lopHocPhanId, Long chuongTrinhMonId) {
        if (lopHocPhanId == null) {
            throw new BadRequestException("Lớp học phần không hợp lệ");
        }

        if (chuongTrinhMonId == null) {
            throw new BadRequestException("Chương trình môn không hợp lệ");
        }

        List<Long> mauIds = chuongTrinhMonQuyDoiDiemMauRepository
                .findByChuongTrinhMonId(chuongTrinhMonId)
                .stream()
                .map(ChuongTrinhMonQuyDoiDiemMau::getQuyDoiDiemMauId)
                .toList();

        List<QuyDoiDiemMau> cotDiemMau = quyDoiDiemMauRepository.findAllById(mauIds)
                .stream()
                .filter(item -> "COT_DIEM".equalsIgnoreCase(item.getLoaiMau()))
                .sorted(Comparator
                        .comparing(QuyDoiDiemMau::getThuTu, Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(QuyDoiDiemMau::getId))
                .toList();

        if (cotDiemMau.isEmpty()) {
            throw new BadRequestException("Chương trình môn chưa gán mẫu cột điểm");
        }

        BigDecimal tongTyLe = cotDiemMau.stream()
                .map(item -> item.getTyLe() == null ? BigDecimal.ZERO : item.getTyLe())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (tongTyLe.compareTo(new BigDecimal("100")) != 0) {
            throw new BadRequestException("Tổng tỷ lệ cột điểm của chương trình môn phải bằng 100%");
        }

        for (QuyDoiDiemMau mau : cotDiemMau) {
            if (cauHinhDanhGiaRepository.existsByLopHocPhanIdAndTenCotDiem(lopHocPhanId, mau.getTen())) {
                continue;
            }

            CauHinhDanhGia cauHinh = CauHinhDanhGia.builder()
                    .lopHocPhanId(lopHocPhanId)
                    .tenCotDiem(mau.getTen())
                    .loaiDiem(mau.getLoaiMau())
                    .tyLe(mau.getTyLe())
                    .diemToiDa(mau.getDiemToiDa() == null ? BigDecimal.TEN : mau.getDiemToiDa())
                    .thuTu(mau.getThuTu())
                    .build();

            cauHinhDanhGiaRepository.save(cauHinh);
        }
    }
}