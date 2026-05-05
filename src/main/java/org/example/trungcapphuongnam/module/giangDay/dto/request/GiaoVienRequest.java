package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.*;
import org.example.trungcapphuongnam.common.enums.TrangThaiGiaoVien;

import java.time.*;
import java.math.BigDecimal;


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
