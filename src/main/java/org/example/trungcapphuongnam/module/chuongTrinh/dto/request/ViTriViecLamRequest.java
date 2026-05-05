package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViTriViecLamRequest {

    @NotNull(message = "chuong_trinh_version_id không được để trống")
    private Long chuongTrinhVersionId;

    @NotBlank(message = "ten không được để trống")
    private String ten;

    private String moTa;

    private Integer thuTu;

}
