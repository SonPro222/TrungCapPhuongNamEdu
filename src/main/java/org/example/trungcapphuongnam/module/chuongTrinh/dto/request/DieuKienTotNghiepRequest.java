package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DieuKienTotNghiepRequest {

    @NotNull(message = "chuong_trinh_version_id không được để trống")
    private Long chuongTrinhVersionId;

    @NotBlank(message = "noi_dung không được để trống")
    private String noiDung;

    private Integer thuTu;

}
