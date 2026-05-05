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
public class KetQuaMonHocRequest {
    @NotNull(message = "sinhVienId không được để trống")
    private Long sinhVienId;

    @NotNull(message = "chuongTrinhMonId không được để trống")
    private Long chuongTrinhMonId;

    private Long lopHocPhanId;

    @DecimalMin(value = "0.0", message = "diemTongKet không được âm")
    private BigDecimal diemTongKet;

    @DecimalMin(value = "0.0", message = "diemPhanTram không được âm")
    private BigDecimal diemPhanTram;

    @DecimalMin(value = "0.0", message = "diemQuyDoi không được âm")
    private BigDecimal diemQuyDoi;

    private String ketQua;

    @Positive(message = "soLanHoc phải lớn hơn 0")
    private Integer soLanHoc;

    private LocalDateTime ngayCapNhat;


}
