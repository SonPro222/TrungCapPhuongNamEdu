package org.example.trungcapphuongnam.module.daoTao.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoaiChuongTrinhRequest {
    @NotBlank(message = "maLoai không được để trống")
    private String maLoai;

    @NotBlank(message = "tenLoai không được để trống")
    private String tenLoai;

    @NotNull(message = "soThang không được để trống")
    @Positive(message = "soThang phải lớn hơn 0")
    private Integer soThang;

    @NotNull(message = "soKy không được để trống")
    @Positive(message = "soKy phải lớn hơn 0")
    private Integer soKy;

    private String moTa;


}
