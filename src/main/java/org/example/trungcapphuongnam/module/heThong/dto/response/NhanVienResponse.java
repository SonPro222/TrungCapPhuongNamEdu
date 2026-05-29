package org.example.trungcapphuongnam.module.heThong.dto.response;

import lombok.*;
import org.example.trungcapphuongnam.module.heThong.enums.TrangThaiNhanVien;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhanVienResponse {
    private Long id;
    private Long taiKhoanId;
    private String maNhanVien;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String phongBan;
    private String chucVu;
    private TrangThaiNhanVien trangThai;

    // Chỉ trả về khi tạo mới/cấp lại tài khoản, không lưu DB
    private String matKhauTam;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}