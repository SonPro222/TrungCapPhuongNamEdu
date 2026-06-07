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
public class NhomKienThucRequest {

    @NotNull(message = "chuong_trinh_version_id không được để trống")
    private Long chuongTrinhVersionId;

    private String ma;
    private Long nhomKienThucGocId;
    @NotBlank(message = "ten không được để trống")
    private String ten;

    private Integer thuTu;

    @NotBlank(message = "loai_nhom không được để trống")
    private String loaiNhom;

//    private BigDecimal tongTinChi;
//
//    private BigDecimal tongSoGio;
//
//    private BigDecimal tongGioLyThuyet;
//
//    private BigDecimal tongGioThucHanh;
//
//    private BigDecimal tongGioKiemTra;

    private String moTa;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
