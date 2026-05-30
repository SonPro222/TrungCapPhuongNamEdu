package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiGiaoVien;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiaoVienResponse {
    private Long id;
    private String maGiaoVien;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String chuyenMon;
    private TrangThaiGiaoVien trangThai;
    private Long taiKhoanId;

    // Chỉ trả về khi tạo mới/cấp lại tài khoản, không lưu DB
    private String matKhauTam;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}