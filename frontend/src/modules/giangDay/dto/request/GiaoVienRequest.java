package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiGiaoVien;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiaoVienRequest {
    private String maGiaoVien;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String chuyenMon;
    private TrangThaiGiaoVien trangThai;
    private Long taiKhoanId;
}
