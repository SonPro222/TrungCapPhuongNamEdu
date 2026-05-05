package org.example.trungcapphuongnam.module.daoTao.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhungKyRequest {
    @NotNull(message = "loaiChuongTrinhId không được để trống")
    private Long loaiChuongTrinhId;

    @NotBlank(message = "maKy không được để trống")
    private String maKy;

    @NotBlank(message = "tenKy không được để trống")
    private String tenKy;

    @NotNull(message = "thuTu không được để trống")
    @Positive(message = "thuTu phải lớn hơn 0")
    private Integer thuTu;


}
