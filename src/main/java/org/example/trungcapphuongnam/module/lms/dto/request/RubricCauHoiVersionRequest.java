package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RubricCauHoiVersionRequest {
    private Long cauHoiVersionId;

    private String tieuChi;

    private String moTa;

    private BigDecimal diemToiDa;

    private Integer thuTu;

}
