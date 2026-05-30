package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;

        import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocGocChuongBaiRequest {

    private Long syllabusMonHocGocId;

    private String ten;

    private String noiDung;

    private Integer thuTu;

    private BigDecimal tongGio;

    private BigDecimal gioLyThuyet;

    private BigDecimal gioThucHanh;

    private BigDecimal gioKiemTra;

    private String ghiChu;
}