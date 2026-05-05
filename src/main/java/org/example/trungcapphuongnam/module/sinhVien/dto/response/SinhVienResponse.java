package org.example.trungcapphuongnam.module.sinhVien.dto.response;


import lombok.*;
import org.example.trungcapphuongnam.common.enums.SinhVien.GioiTinh;
import org.example.trungcapphuongnam.common.enums.SinhVien.SinhVienTrangThai;

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
    private SinhVienTrangThai trangThai;
    private Long taiKhoanId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
