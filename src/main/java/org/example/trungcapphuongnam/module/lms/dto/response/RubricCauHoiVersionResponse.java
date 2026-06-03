package org.example.trungcapphuongnam.module.lms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RubricCauHoiVersionResponse {
    private Long id;

    private Long cauHoiVersionId;

    private String tieuChi;

    private String moTa;

    private BigDecimal diemToiDa;

    private Integer thuTu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
