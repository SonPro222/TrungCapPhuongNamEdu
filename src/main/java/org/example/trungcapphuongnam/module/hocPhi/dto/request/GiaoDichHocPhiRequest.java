package org.example.trungcapphuongnam.module.hocPhi.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.PhuongThucGiaoDichHocPhi;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiaoDichHocPhiRequest {

    @NotNull(message = "Học phí id không được để trống")
    private Long hocPhiId;

    @NotNull(message = "Số tiền không được để trống")
    @DecimalMin(value = "0.01", message = "Số tiền phải lớn hơn 0")
    private BigDecimal soTien;

    private LocalDateTime ngayGiaoDich;
    private PhuongThucGiaoDichHocPhi phuongThuc;
    private String maGiaoDich;
    private String ghiChu;
}
