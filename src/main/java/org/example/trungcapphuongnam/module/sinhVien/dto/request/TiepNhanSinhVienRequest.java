package org.example.trungcapphuongnam.module.sinhVien.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.trungcapphuongnam.module.sinhVien.enums.GioiTinh;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TiepNhanSinhVienRequest {
    @NotNull(message = "Ngành không được để trống")
    private Long nganhId;

    @NotNull(message = "Chương trình không được để trống")
    private Long chuongTrinhId;

    @NotNull(message = "Version chương trình không được để trống")
    private Long chuongTrinhVersionId;

    private Long lopHanhChinhId;

    @NotBlank(message = "Họ tên không được để trống")
    private String hoTen;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate ngaySinh;

    @NotNull(message = "Giới tính không được để trống")
    private GioiTinh gioiTinh;

    @NotBlank(message = "Gmail sinh viên không được để trống")
    @Email(message = "Gmail sinh viên không đúng định dạng")
    private String email;

    @NotBlank(message = "Số điện thoại sinh viên không được để trống")
    private String soDienThoai;

    @NotBlank(message = "Địa chỉ liên hệ không được để trống")
    private String diaChi;

    @NotBlank(message = "Địa chỉ thường trú không được để trống")
    private String diaChiThuongTru;

    @NotBlank(message = "Số CCCD không được để trống")
    private String soCccd;

    @NotNull(message = "Ngày cấp CCCD không được để trống")
    private LocalDate ngayCapCccd;

    @NotBlank(message = "Nơi cấp CCCD không được để trống")
    private String noiCapCccd;

    private Long anhChanDungTepId;
    private Long cccdTruocTepId;
    private Long cccdSauTepId;
    private Long bangCapTepId;

    @NotBlank(message = "Bằng cấp không được để trống")
    private String bangCap;

    @NotNull(message = "Năm tốt nghiệp không được để trống")
    private Integer namTotNghiep;

    @NotBlank(message = "Trường tốt nghiệp không được để trống")
    private String truongTotNghiep;

    @NotBlank(message = "Họ tên cha không được để trống")
    private String hoTenCha;

    private String ngheNghiepCha;

    @NotBlank(message = "Số điện thoại cha không được để trống")
    private String sdtCha;

    @Email(message = "Gmail cha không đúng định dạng")
    private String emailCha;

    @NotBlank(message = "Họ tên mẹ không được để trống")
    private String hoTenMe;

    private String ngheNghiepMe;

    @NotBlank(message = "Số điện thoại mẹ không được để trống")
    private String sdtMe;

    @Email(message = "Gmail mẹ không đúng định dạng")
    private String emailMe;

    @NotBlank(message = "Họ tên người thân không được để trống")
    private String hoTenNguoiThan;

    @NotBlank(message = "Quan hệ người thân không được để trống")
    private String quanHeNguoiThan;

    @NotBlank(message = "Số điện thoại người thân không được để trống")
    private String sdtNguoiThan;

    @NotBlank(message = "Gmail người thân không được để trống")
    @Email(message = "Gmail người thân không đúng định dạng")
    private String emailNguoiThan;

    private String ghiChuHoSo;
    private LocalDate ngayNhapHoc;
    private String ghiChuChuongTrinh;
}
