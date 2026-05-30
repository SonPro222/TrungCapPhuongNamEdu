package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuyDoiDiemRequest {

    @NotNull(message = "chuong_trinh_mon_id không được để trống")
    private Long chuongTrinhMonId;

    private BigDecimal nguongTu;

    private BigDecimal nguongDen;

    private BigDecimal diemQuyDoi;

    private String ketQua;

    private String congThuc;

    private String ghiChu;

    private String ma;

    private String ten;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String loaiMau;

    private BigDecimal tyLe;

    private BigDecimal diemToiDa;

    private Integer thuTu;

    private Boolean batBuoc;

}
