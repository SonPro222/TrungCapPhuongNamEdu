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
public class SyllabusChuongBaiRequest {

    @NotNull(message = "syllabus_mon_id không được để trống")
    private Long syllabusMonId;
    private String maChuong;
    @NotBlank(message = "ten không được để trống")
    private String ten;

    private BigDecimal tongGio;

    private BigDecimal gioLyThuyet;

    private BigDecimal gioThucHanh;

    private BigDecimal gioKiemTra;

    private String noiDung;

    private String mucTieu;

    private Integer thuTu;

    private Long syllabusMonHocMauId;

    private String ghiChu;


}
