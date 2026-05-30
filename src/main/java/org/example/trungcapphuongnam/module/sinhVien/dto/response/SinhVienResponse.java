package org.example.trungcapphuongnam.module.sinhVien.dto.response;

import lombok.*;
import org.example.trungcapphuongnam.module.sinhVien.enums.GioiTinh;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienTrangThai;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SinhVienResponse {
    private Long id;
    private String maSinhVien;
    private String hoTen;
    private LocalDate ngaySinh;
    private GioiTinh gioiTinh;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
