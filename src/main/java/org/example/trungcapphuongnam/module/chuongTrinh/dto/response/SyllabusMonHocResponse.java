package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocResponse {

    private Long id;

    private Long chuongTrinhMonId;
    private Long syllabusMonHocGocId;
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

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long monHocId;

    private String ma;

    private String ten;

    private String ghiChu;

}
