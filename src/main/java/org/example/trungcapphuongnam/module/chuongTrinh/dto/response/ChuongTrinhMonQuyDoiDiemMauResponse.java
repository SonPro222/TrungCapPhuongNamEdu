package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhMonQuyDoiDiemMauResponse {

    private Long id;

    private Long chuongTrinhMonId;

    private Long syllabusMonHocId;

    private Long quyDoiDiemMauId;

    private String ghiChu;

    private String ma;

    private String ten;

    private BigDecimal nguongTu;

    private BigDecimal nguongDen;

    private BigDecimal diemQuyDoi;

    private String ketQua;

    private String congThuc;

    private String loaiMau;

    private BigDecimal tyLe;

    private BigDecimal diemToiDa;

    private Integer thuTu;

    private Boolean batBuoc;
}