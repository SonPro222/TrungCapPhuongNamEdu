package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusChuongBaiResponse {

    private Long id;

    private Long syllabusMonId;

    private String ten;

    private BigDecimal tongGio;

    private BigDecimal gioLyThuyet;

    private BigDecimal gioThucHanh;

    private BigDecimal gioKiemTra;

    private String noiDung;

    private String mucTieu;

    private Integer thuTu;

    private Long syllabusMonHocGocId;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
