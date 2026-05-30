package org.example.trungcapphuongnam.module.heThong.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaiKhoanSinhVienResponse {

    private Long sinhVienId;
    private String maSinhVien;
    private String hoTen;
    private String emailSinhVien;
    private String soDienThoai;
    private String trangThaiSinhVien;

    private Long taiKhoanId;
    private String emailTaiKhoan;
    private String trangThaiTaiKhoan;
    private String loaiTaiKhoan;

    private String matKhauTam;

    private Long sinhVienChuongTrinhId;

    private Long nganhId;
    private String maNganh;
    private String tenNganh;

    private Long chuongTrinhId;
    private String maChuongTrinh;
    private String tenChuongTrinh;

    private Long chuongTrinhVersionId;
    private String maVersion;
    private String tenVersion;

}