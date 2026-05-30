package org.example.trungcapphuongnam.module.heThong.service;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVien;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.example.trungcapphuongnam.module.heThong.entity.NhanVien;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.repository.NhanVienRepository;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ThongTinTaiKhoanLogResolver {

    private final NhanVienRepository nhanVienRepository;
    private final SinhVienRepository sinhVienRepository;
    private final GiaoVienRepository giaoVienRepository;

    public ThongTinTaiKhoanLog resolve(TaiKhoan taiKhoan) {
        if (taiKhoan == null || taiKhoan.getId() == null) {
            return ThongTinTaiKhoanLog.builder().build();
        }

        NhanVien nhanVien = nhanVienRepository.findByTaiKhoan_Id(taiKhoan.getId()).orElse(null);
        if (nhanVien != null) {
            return ThongTinTaiKhoanLog.builder()
                    .tenTaiKhoan(nhanVien.getHoTen())
                    .doiTuongLoai("NHAN_VIEN")
                    .doiTuongId(nhanVien.getId())
                    .doiTuongTen(nhanVien.getHoTen())
                    .build();
        }

        SinhVien sinhVien = sinhVienRepository.findByTaiKhoanId(taiKhoan.getId()).orElse(null);
        if (sinhVien != null) {
            return ThongTinTaiKhoanLog.builder()
                    .tenTaiKhoan(sinhVien.getHoTen())
                    .doiTuongLoai("SINH_VIEN")
                    .doiTuongId(sinhVien.getId())
                    .doiTuongTen(sinhVien.getHoTen())
                    .build();
        }

        GiaoVien giaoVien = giaoVienRepository.findByTaiKhoanId(taiKhoan.getId()).orElse(null);
        if (giaoVien != null) {
            return ThongTinTaiKhoanLog.builder()
                    .tenTaiKhoan(giaoVien.getHoTen())
                    .doiTuongLoai("GIAO_VIEN")
                    .doiTuongId(giaoVien.getId())
                    .doiTuongTen(giaoVien.getHoTen())
                    .build();
        }

        return ThongTinTaiKhoanLog.builder()
                .tenTaiKhoan(taiKhoan.getEmail())
                .doiTuongLoai("TAI_KHOAN")
                .doiTuongId(taiKhoan.getId())
                .doiTuongTen(taiKhoan.getEmail())
                .build();
    }

    @Getter
    @Builder
    public static class ThongTinTaiKhoanLog {
        private String tenTaiKhoan;
        private String doiTuongLoai;
        private Long doiTuongId;
        private String doiTuongTen;
    }
}