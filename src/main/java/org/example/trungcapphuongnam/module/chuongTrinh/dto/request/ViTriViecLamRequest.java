package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViTriViecLamRequest {

    private Long syllabusChuongTrinhId;
    private Long chuongTrinhVersionId;

    @NotBlank(message = "ten không được để trống")
    private String ten;

    private String moTa;

    private Integer thuTu;

    private String ma;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
