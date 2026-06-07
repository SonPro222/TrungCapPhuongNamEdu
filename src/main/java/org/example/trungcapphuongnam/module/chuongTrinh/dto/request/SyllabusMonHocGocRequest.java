package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;


import lombok.*;
import jakarta.validation.constraints.*;

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

    @NotNull(message = "Số buổi học không được để trống")
    @Min(value = 1, message = "Số buổi học phải lớn hơn 0")
    private Integer soBuoiHoc;

    @NotNull(message = "Số tiết mỗi buổi không được để trống")
    @DecimalMin(value = "0.1", message = "Số tiết mỗi buổi phải lớn hơn 0")
    private BigDecimal soTietMoiBuoi;

    @NotNull(message = "Số phút một tiết không được để trống")
    @Min(value = 1, message = "Số phút một tiết phải lớn hơn 0")
    private Integer soPhutMotTiet;


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