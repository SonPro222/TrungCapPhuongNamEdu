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
public class NhomTuChonRequest {

    @NotNull(message = "chuong_trinh_version_id không được để trống")
    private Long chuongTrinhVersionId;
    private Long nhomTuChonGocId;
    @NotBlank(message = "ten không được để trống")
    private String ten;

    private Integer soMonChon;

    private BigDecimal soTinChiCanDat;

    private String ghiChu;

    private String ma;

    private String moTa;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
