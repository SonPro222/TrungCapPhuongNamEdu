package org.example.trungcapphuongnam.module.heThong.service;

import org.example.trungcapphuongnam.module.heThong.dto.request.CapTaiKhoanSinhVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.request.DoiMatKhauSinhVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.request.GuiOtpDoiMatKhauSinhVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.TaiKhoanSinhVienResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaiKhoanSinhVienService {

    Page<TaiKhoanSinhVienResponse> findAll(
            String keyword,
            Long nganhId,
            Long chuongTrinhVersionId,
            String trangThaiTaiKhoan,
            Pageable pageable
    );

    TaiKhoanSinhVienResponse capTaiKhoan(Long sinhVienId, CapTaiKhoanSinhVienRequest request);

    TaiKhoanSinhVienResponse capNhatGmail(Long sinhVienId, CapTaiKhoanSinhVienRequest request);

    TaiKhoanSinhVienResponse datLaiMatKhau(Long sinhVienId);

    TaiKhoanSinhVienResponse khoaTaiKhoan(Long sinhVienId);

    TaiKhoanSinhVienResponse moKhoaTaiKhoan(Long sinhVienId);

    int khoaNhieuTaiKhoan(List<Long> sinhVienIds);

    int moKhoaNhieuTaiKhoan(List<Long> sinhVienIds);

    int tuDongKhoaTaiKhoanTheoNghiepVu();

    void guiOtpDoiMatKhau(GuiOtpDoiMatKhauSinhVienRequest request);

    void xacNhanDoiMatKhau(DoiMatKhauSinhVienRequest request);
}