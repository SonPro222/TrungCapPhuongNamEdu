package org.example.trungcapphuongnam.module.heThong.dto.response;

import lombok.*;
import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhatKyHeThongResponse {
    private Long id;
    private Long taiKhoanId;
    private String hanhDong;
    private String bangTacDong;
    private Long banGhiId;
    private String banGhiMa;
    private String banGhiTen;
    private String noiDungCu;
    private String noiDungMoi;
    private String duLieuTimKiem;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createdAt;
    private String taiKhoanEmail;
    private String tenTaiKhoan;
    private String loaiTaiKhoan;
    private String doiTuongLoai;
    private Long doiTuongId;
    private String doiTuongMa;
    private String doiTuongTen;
    private String doiTuongEmail;
    private String moduleNghiepVu;
    private String chucNang;
    private String moTa;
    private String phuongThuc;
    private String duongDan;
    private String ketQua;
}