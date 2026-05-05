package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.*;
import org.example.trungcapphuongnam.common.enums.TrangThaiSinhVienLopHocPhan;

import java.time.*;
import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SinhVienLopHocPhanRequest {
    private Long sinhVienId;
    private Long lopHocPhanId;
    private LocalDateTime ngayDangKy;
    private TrangThaiSinhVienLopHocPhan trangThai;
    private Boolean laHocLai;
    private String ghiChu;
}
