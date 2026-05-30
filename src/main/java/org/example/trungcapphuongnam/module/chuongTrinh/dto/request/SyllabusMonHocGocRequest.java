package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;


import lombok.*;

        import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocGocRequest {

    private Long monHocId;

    private String ma;

    private String ten;

    private String viTri;

    private String tinhChat;

    private String mucTieu;

    private String phuongPhapDanhGia;

    private String dieuKienHoanThanh;

    private String huongDan;

    private BigDecimal diemDatToiThieu;

    private String donViDiem;

    private BigDecimal tyLeChuyenCanToiThieu;

    private Boolean batBuocDuThi;

    private String congThucQuyDoi;

    private String ghiChu;
}