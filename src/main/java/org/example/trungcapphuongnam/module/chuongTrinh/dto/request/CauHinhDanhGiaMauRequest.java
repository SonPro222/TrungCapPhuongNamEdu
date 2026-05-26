package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauHinhDanhGiaMauRequest {

    @NotNull(message = "chuongTrinhMonId không được để trống")
    private Long chuongTrinhMonId;

    @NotBlank(message = "tenCotDiem không được để trống")
    private String tenCotDiem;

    private String loaiDiem;

    @NotNull(message = "tyLe không được để trống")
    @DecimalMin(value = "0.0", message = "tyLe không được âm")
    @DecimalMax(value = "100.0", message = "tyLe không được vượt quá 100")
    private BigDecimal tyLe;

    @DecimalMin(value = "0.01", message = "diemToiDa phải lớn hơn 0")
    private BigDecimal diemToiDa;

    private Integer thuTu;

    private Boolean batBuoc;

    private String ghiChu;
}