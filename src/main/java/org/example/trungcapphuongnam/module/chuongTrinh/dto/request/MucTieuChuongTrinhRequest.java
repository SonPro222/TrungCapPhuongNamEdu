package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MucTieuChuongTrinhRequest {

    @NotNull(message = "chuong_trinh_version_id không được để trống")
    private Long chuongTrinhVersionId;

    @NotBlank(message = "loai không được để trống")
    private String loai;

    @NotBlank(message = "noi_dung không được để trống")
    private String noiDung;

    private Integer thuTu;

}
