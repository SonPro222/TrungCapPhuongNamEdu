package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocRequest {

    @NotNull(message = "chuong_trinh_mon_id không được để trống")
    private Long chuongTrinhMonId;
    private Long syllabusMonHocGocId;
    private String viTri;

    private String tinhChat;

    @NotNull(message = "Số buổi học không được để trống")
    @Min(value = 1, message = "Số buổi học phải lớn hơn 0")
    private Integer soBuoiHoc;

    @NotNull(message = "Số tiết mỗi buổi không được để trống")
    @DecimalMin(value = "0.1", message = "Số tiết mỗi buổi phải lớn hơn 0")
    private BigDecimal soTietMoiBuoi;

    @NotNull(message = "Số phút một tiết không được để trống")
    @Min(value = 1, message = "Số phút một tiết phải lớn hơn 0")
    private Integer soPhutMotTiet;


    private String mucTieu;

    private String phuongPhapDanhGia;

    private String dieuKienHoanThanh;

    private String huongDan;

    private BigDecimal diemDatToiThieu;

    private String donViDiem;

    private BigDecimal tyLeChuyenCanToiThieu;

    private Boolean batBuocDuThi;

    private String congThucQuyDoi;

    private Long monHocId;

    private String ma;

    private String ten;

    private String ghiChu;

}
