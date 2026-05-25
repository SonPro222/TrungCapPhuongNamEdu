package org.example.trungcapphuongnam.module.sinhVien.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.trungcapphuongnam.module.sinhVien.enums.GioiTinh;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienTrangThai;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SinhVienRequest {
    private String maSinhVien;

    @NotBlank(message = "Họ tên không được để trống")
    private String hoTen;

    private LocalDate ngaySinh;
    private GioiTinh gioiTinh;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    private String soDienThoai;
    private String diaChi;
    private String diaChiThuongTru;
    private String soCccd;
    private LocalDate ngayCapCccd;
    private String noiCapCccd;
    private Long anhChanDungTepId;
    private Long cccdTruocTepId;
    private Long cccdSauTepId;
    private Long bangCapTepId;
    private String bangCap;
    private Integer namTotNghiep;
    private String truongTotNghiep;
    private String hoTenCha;
    private String ngheNghiepCha;
    private String sdtCha;
    private String emailCha;
    private String hoTenMe;
    private String ngheNghiepMe;
    private String sdtMe;
    private String emailMe;
    private String hoTenNguoiThan;
    private String quanHeNguoiThan;
    private String sdtNguoiThan;
    private String emailNguoiThan;
    private String ghiChuHoSo;
    private SinhVienTrangThai trangThai;
    private Long taiKhoanId;
}
