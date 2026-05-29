package org.example.trungcapphuongnam.module.giangDay.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiem;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.QuyDoiDiemRepository;
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

    private static final BigDecimal MOT_TRAM = new BigDecimal("100.00");

    private final CauHinhDanhGiaRepository cauHinhDanhGiaRepository;
    private final QuyDoiDiemRepository quyDoiDiemRepository;

    @Override
    public void saoChepTuChuongTrinhMonSangLopHocPhan(Long lopHocPhanId, Long chuongTrinhMonId) {
        if (lopHocPhanId == null) {
            throw new BadRequestException("Lớp học phần không hợp lệ");
        }

        if (chuongTrinhMonId == null) {
            throw new BadRequestException("Chương trình môn không hợp lệ");
        }

        List<QuyDoiDiem> danhSachQuyDoiDiem = quyDoiDiemRepository
                .findByChuongTrinhMonIdOrderByThuTuAscIdAsc(chuongTrinhMonId);

        if (danhSachQuyDoiDiem.isEmpty()) {
            throw new BadRequestException("Môn trong chương trình chưa có Quy đổi điểm đã lưu. Cần khai báo bảng Quy đổi điểm đã lưu cho môn trong chương trình trước khi tạo lớp học phần.");
        }

        kiemTraQuyDoiDiemDaLuu(danhSachQuyDoiDiem);

        for (QuyDoiDiem quyDoiDiem : danhSachQuyDoiDiem) {
            String tenCotDiem = layTenCotDiemTuQuyDoiDiem(quyDoiDiem);

            if (cauHinhDanhGiaRepository.existsByLopHocPhanIdAndTenCotDiem(lopHocPhanId, tenCotDiem)) {
                continue;
            }

            CauHinhDanhGia cauHinhDanhGia = CauHinhDanhGia.builder()
                    .lopHocPhanId(lopHocPhanId)
                    .tenCotDiem(tenCotDiem)
                    .loaiDiem(chuanHoaLoaiDiem(quyDoiDiem.getLoaiMau()))
                    .tyLe(quyDoiDiem.getTyLe())
                    .diemToiDa(quyDoiDiem.getDiemToiDa() == null ? BigDecimal.TEN : quyDoiDiem.getDiemToiDa())
                    .thuTu(quyDoiDiem.getThuTu())
                    .build();

            cauHinhDanhGiaRepository.save(cauHinhDanhGia);
        }
    }

    private void kiemTraQuyDoiDiemDaLuu(List<QuyDoiDiem> danhSachQuyDoiDiem) {
        BigDecimal tongTyLe = BigDecimal.ZERO;

        for (QuyDoiDiem quyDoiDiem : danhSachQuyDoiDiem) {
            String tenCotDiem = layTenCotDiemTuQuyDoiDiem(quyDoiDiem);

            if (quyDoiDiem.getTyLe() == null) {
                throw new BadRequestException("Quy đổi điểm đã lưu '" + tenCotDiem + "' chưa có tỷ lệ.");
            }

            if (quyDoiDiem.getTyLe().compareTo(BigDecimal.ZERO) < 0 || quyDoiDiem.getTyLe().compareTo(MOT_TRAM) > 0) {
                throw new BadRequestException("Tỷ lệ Quy đổi điểm đã lưu '" + tenCotDiem + "' phải từ 0 đến 100.");
            }

            if (quyDoiDiem.getDiemToiDa() != null && quyDoiDiem.getDiemToiDa().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestException("Điểm tối đa của Quy đổi điểm đã lưu '" + tenCotDiem + "' phải lớn hơn 0.");
            }

            tongTyLe = tongTyLe.add(quyDoiDiem.getTyLe());
        }

        if (tongTyLe.compareTo(MOT_TRAM) != 0) {
            String chiTiet = danhSachQuyDoiDiem.stream()
                    .map(item -> layTenCotDiemTuQuyDoiDiem(item) + "=" + item.getTyLe() + "%")
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("không có dòng quy đổi điểm");

            throw new BadRequestException(
                    "Tổng tỷ lệ bảng Quy đổi điểm đã lưu cho môn trong chương trình phải bằng 100%. " +
                            "Hiện tại = " + tongTyLe + "%. Chi tiết: " + chiTiet
            );
        }
    }

    private String layTenCotDiemTuQuyDoiDiem(QuyDoiDiem quyDoiDiem) {
        if (quyDoiDiem.getTen() != null && !quyDoiDiem.getTen().isBlank()) {
            return quyDoiDiem.getTen().trim();
        }

        if (quyDoiDiem.getMa() != null && !quyDoiDiem.getMa().isBlank()) {
            return quyDoiDiem.getMa().trim();
        }

        if (quyDoiDiem.getGhiChu() != null && !quyDoiDiem.getGhiChu().isBlank()) {
            return quyDoiDiem.getGhiChu().trim();
        }

        throw new BadRequestException("Quy đổi điểm đã lưu phải có tên cột điểm.");
    }

    private String chuanHoaLoaiDiem(String loaiDiem) {
        if (loaiDiem == null || loaiDiem.isBlank()) {
            return "khac";
        }

        String value = loaiDiem.trim().toLowerCase();

        return switch (value) {
            case "chuyen_can", "bai_tap", "giua_ky", "cuoi_ky", "thuc_hanh", "do_an", "khac" -> value;
            default -> "khac";
        };
    }
}