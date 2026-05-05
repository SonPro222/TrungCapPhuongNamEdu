package org.example.trungcapphuongnam.module.diem.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiemChiTietRequest {
    @NotNull(message = "sinhVienId không được để trống")
    private Long sinhVienId;

    @NotNull(message = "lopHocPhanId không được để trống")
    private Long lopHocPhanId;

    private Long cauHinhDanhGiaId;

    private Long baiKiemTraId;

    @DecimalMin(value = "0.0", message = "diem không được âm")
    private BigDecimal diem;

    private String nhanXet;

    private Long giaoVienChamId;

    private LocalDateTime ngayCham;

    private String trangThai;


}
