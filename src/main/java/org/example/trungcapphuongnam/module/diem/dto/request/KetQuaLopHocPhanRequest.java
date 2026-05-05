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
public class KetQuaLopHocPhanRequest {
    @NotNull(message = "sinhVienId không được để trống")
    private Long sinhVienId;

    @NotNull(message = "lopHocPhanId không được để trống")
    private Long lopHocPhanId;

    @DecimalMin(value = "0.0", message = "diemChuyenCan không được âm")
    private BigDecimal diemChuyenCan;

    @DecimalMin(value = "0.0", message = "diemQuaTrinh không được âm")
    private BigDecimal diemQuaTrinh;

    @DecimalMin(value = "0.0", message = "diemThi không được âm")
    private BigDecimal diemThi;

    @DecimalMin(value = "0.0", message = "diemTongKet không được âm")
    private BigDecimal diemTongKet;

    @DecimalMin(value = "0.0", message = "diemPhanTram không được âm")
    private BigDecimal diemPhanTram;

    @DecimalMin(value = "0.0", message = "diemQuyDoi không được âm")
    private BigDecimal diemQuyDoi;

    private String ketQua;

    private String trangThai;

    private LocalDateTime ngayChot;

    private LocalDateTime ngayCongBo;


}
