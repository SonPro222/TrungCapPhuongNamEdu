package org.example.trungcapphuongnam.module.sinhVien.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.trungcapphuongnam.module.sinhVien.enums.GioiTinh;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienTrangThai;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class SinhVienExportExcelResponse {

    private String maSinhVien;
    private String hoTen;
    private LocalDate ngaySinh;
    private GioiTinh gioiTinh;
    private String email;
    private String soDienThoai;
    private String soCccd;
    private LocalDate ngayCapCccd;
    private String noiCapCccd;
    private String diaChi;
    private String diaChiThuongTru;
    private String bangCap;
    private Integer namTotNghiep;
    private String truongTotNghiep;
    private SinhVienTrangThai trangThai;
    private LocalDateTime createdAt;
}