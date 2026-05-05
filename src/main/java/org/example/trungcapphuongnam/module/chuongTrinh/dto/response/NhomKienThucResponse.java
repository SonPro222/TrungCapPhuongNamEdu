package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhomKienThucResponse {

    private Long id;

    private Long chuongTrinhVersionId;

    private String ma;

    private String ten;

    private Integer thuTu;

    private String loaiNhom;

    private BigDecimal tongTinChi;

    private BigDecimal tongSoGio;

    private BigDecimal tongGioLyThuyet;

    private BigDecimal tongGioThucHanh;

    private BigDecimal tongGioKiemTra;

}
