package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauHinhDanhGiaMauResponse {

    private Long id;
    private Long chuongTrinhMonId;
    private String tenCotDiem;
    private String loaiDiem;
    private BigDecimal tyLe;
    private BigDecimal diemToiDa;
    private Integer thuTu;
    private Boolean batBuoc;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}