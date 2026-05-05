package org.example.trungcapphuongnam.module.diem.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauHinhDanhGiaResponse {
    private Long id;

    private Long lopHocPhanId;

    private String tenCotDiem;

    private String loaiDiem;

    private BigDecimal tyLe;

    private BigDecimal diemToiDa;

    private Integer thuTu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
