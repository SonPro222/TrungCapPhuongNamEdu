package org.example.trungcapphuongnam.module.heThong.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhatKyHeThongRequest {
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