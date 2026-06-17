package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuyDoiDiemResponse {

    private Long id;

    private Long syllabusMonHocId;

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


    private BigDecimal diemToiDa;

    private Integer thuTu;

    private Boolean batBuoc;
}