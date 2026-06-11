package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NangLucDauRaRequest {

    private Long syllabusChuongTrinhId;
    private Long chuongTrinhVersionId;

    private String ma;

    @NotBlank(message = "noi_dung không được để trống")
    private String noiDung;

    @NotBlank(message = "loai không được để trống")
    private String loai;

    private Integer thuTu;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
