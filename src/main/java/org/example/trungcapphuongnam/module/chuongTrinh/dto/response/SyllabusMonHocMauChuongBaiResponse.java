package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocGocChuongBaiResponse {

    private Long id;

    private Long syllabusMonHocGocId;

    private String maChuong;

    private String ten;

    private String mucTieu;

    private String noiDung;

    private Integer thuTu;

    private BigDecimal tongGio;

    private BigDecimal gioLyThuyet;

    private BigDecimal gioThucHanh;

    private BigDecimal gioKiemTra;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}