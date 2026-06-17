package org.example.trungcapphuongnam.module.heThong.dto.request;

import lombok.*;
import org.example.trungcapphuongnam.module.heThong.enums.TrangThaiNhanVien;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhanVienRequest {
    private Long taiKhoanId;
    private String maNhanVien;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String phongBan;
    private String chucVu;
    private TrangThaiNhanVien trangThai;
}
