package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiSinhVienLopHocPhan;

import java.time.*;


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
