package org.example.trungcapphuongnam.module.diem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHoc;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocRepository;
import org.example.trungcapphuongnam.module.diem.entity.CauHinhDanhGia;
import org.example.trungcapphuongnam.module.diem.entity.DiemChiTiet;
import org.example.trungcapphuongnam.module.diem.entity.KetQuaLopHocPhan;
import org.example.trungcapphuongnam.module.diem.repository.CauHinhDanhGiaRepository;
import org.example.trungcapphuongnam.module.diem.repository.DiemChiTietRepository;
import org.example.trungcapphuongnam.module.diem.repository.KetQuaLopHocPhanRepository;
import org.example.trungcapphuongnam.module.diem.service.TinhDiemLopHocPhanService;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TinhDiemLopHocPhanServiceImpl implements TinhDiemLopHocPhanService {

    private static final BigDecimal MOT_TRAM = new BigDecimal("100");
    private static final BigDecimal DIEM_DAT_MAC_DINH = new BigDecimal("5.00");

    private final DiemChiTietRepository diemChiTietRepository;
    private final CauHinhDanhGiaRepository cauHinhDanhGiaRepository;
    private final KetQuaLopHocPhanRepository ketQuaLopHocPhanRepository;
    private final LopHocPhanRepository lopHocPhanRepository;
    private final SyllabusMonHocRepository syllabusMonHocRepository;

    @Override
    public void tinhLaiKetQuaLopHocPhan(Long sinhVienId, Long lopHocPhanId) {
        if (sinhVienId == null || lopHocPhanId == null) {
            throw new BadRequestException("sinhVienId và lopHocPhanId không được để trống");
        }

        List<DiemChiTiet> diemChiTiets = diemChiTietRepository.findBySinhVienIdAndLopHocPhanId(sinhVienId, lopHocPhanId);
        Long syllabusMonHocId = resolveSyllabusMonHocIdTuLopHocPhan(lopHocPhanId);
        List<CauHinhDanhGia> cauHinhs = cauHinhDanhGiaRepository.findBySyllabusMonHocIdOrderByThuTuAscIdAsc(syllabusMonHocId);
        Map<Long, CauHinhDanhGia> cauHinhMap = cauHinhs.stream()
                .filter(c -> c.getId() != null)
                .collect(Collectors.toMap(CauHinhDanhGia::getId, Function.identity(), (a, b) -> a));

        BigDecimal diemTongKet = BigDecimal.ZERO;
        BigDecimal diemChuyenCan = null;
        BigDecimal diemQuaTrinh = null;
        BigDecimal diemThi = null;

        for (DiemChiTiet diemChiTiet : diemChiTiets) {
            if (diemChiTiet.getDiem() == null || diemChiTiet.getCauHinhDanhGiaId() == null) {
                continue;
            }
            CauHinhDanhGia cauHinh = cauHinhMap.get(diemChiTiet.getCauHinhDanhGiaId());
            if (cauHinh == null || cauHinh.getTyLe() == null) {
                continue;
            }

            BigDecimal diemQuyVeThang10 = quyVeThang10(diemChiTiet.getDiem(), cauHinh.getDiemToiDa());
            diemTongKet = diemTongKet.add(diemQuyVeThang10.multiply(cauHinh.getTyLe()).divide(MOT_TRAM, 4, RoundingMode.HALF_UP));

            String loaiDiem = Objects.toString(cauHinh.getLoaiDiem(), "").toLowerCase();
            if (loaiDiem.contains("chuyen_can")) {
                diemChuyenCan = diemQuyVeThang10;
            } else if (loaiDiem.contains("cuoi_ky") || loaiDiem.contains("thi")) {
                diemThi = diemQuyVeThang10;
            } else {
                diemQuaTrinh = diemQuaTrinh == null ? diemQuyVeThang10 : diemQuaTrinh.add(diemQuyVeThang10).divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
            }
        }

        diemTongKet = diemTongKet.setScale(2, RoundingMode.HALF_UP);
        KetQuaLopHocPhan ketQua = ketQuaLopHocPhanRepository
                .findFirstBySinhVienIdAndLopHocPhanId(sinhVienId, lopHocPhanId)
                .orElseGet(() -> KetQuaLopHocPhan.builder()
                        .sinhVienId(sinhVienId)
                        .lopHocPhanId(lopHocPhanId)
                        .trangThai("nhap")
                        .build());

        ketQua.setDiemChuyenCan(diemChuyenCan);
        ketQua.setDiemQuaTrinh(diemQuaTrinh);
        ketQua.setDiemThi(diemThi);
        ketQua.setDiemTongKet(diemTongKet);
        ketQua.setDiemPhanTram(diemTongKet.multiply(BigDecimal.TEN).setScale(2, RoundingMode.HALF_UP));
        ketQua.setDiemQuyDoi(diemTongKet);
        ketQua.setKetQua(diemTongKet.compareTo(DIEM_DAT_MAC_DINH) >= 0 ? "dat" : "khong_dat");
        if (ketQua.getTrangThai() == null || ketQua.getTrangThai().isBlank()) {
            ketQua.setTrangThai("nhap");
        }

        ketQuaLopHocPhanRepository.save(ketQua);
    }

    private Long resolveSyllabusMonHocIdTuLopHocPhan(Long lopHocPhanId) {
        LopHocPhan lopHocPhan = lopHocPhanRepository.findById(lopHocPhanId)
                .orElseThrow(() -> new BadRequestException("Lớp học phần không tồn tại: " + lopHocPhanId));

        if (lopHocPhan.getChuongTrinhMonId() == null) {
            throw new BadRequestException("Lớp học phần chưa tham chiếu môn trong chương trình nên không xác định được syllabus");
        }

        SyllabusMonHoc syllabusMonHoc = syllabusMonHocRepository
                .findFirstByChuongTrinhMonIdOrderByIdAsc(lopHocPhan.getChuongTrinhMonId())
                .orElseThrow(() -> new BadRequestException("Môn trong chương trình chưa có syllabus áp dụng: " + lopHocPhan.getChuongTrinhMonId()));

        return syllabusMonHoc.getId();
    }

    private BigDecimal quyVeThang10(BigDecimal diem, BigDecimal diemToiDa) {
        if (diemToiDa == null || diemToiDa.compareTo(BigDecimal.ZERO) <= 0 || diemToiDa.compareTo(BigDecimal.TEN) == 0) {
            return diem.setScale(2, RoundingMode.HALF_UP);
        }
        return diem.multiply(BigDecimal.TEN).divide(diemToiDa, 4, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
    }
}
