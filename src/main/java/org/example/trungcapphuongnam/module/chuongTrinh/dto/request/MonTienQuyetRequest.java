package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonTienQuyetRequest {

    @NotNull(message = "mon_id không được để trống")
    private Long monId;

    @NotNull(message = "mon_dieu_kien_id không được để trống")
    private Long monDieuKienId;

    @NotBlank(message = "loai không được để trống")
    private String loai;

    private String ghiChu;

}
