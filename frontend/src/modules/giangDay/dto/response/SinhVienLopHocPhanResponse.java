package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiSinhVienLopHocPhan;

import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SinhVienLopHocPhanResponse {
    private Long id;
    private Long sinhVienId;
    private Long lopHocPhanId;
    private LocalDateTime ngayDangKy;
    private TrangThaiSinhVienLopHocPhan trangThai;
    private Boolean laHocLai;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
