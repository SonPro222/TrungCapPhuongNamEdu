package org.example.trungcapphuongnam.module.daoTao.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhoaDaoTaoRequest {
    @NotBlank(message = "maKhoa không được để trống")
    private String maKhoa;

    private String tenKhoa;

    private Integer namBatDau;

    private Integer namKetThuc;

    private String ghiChu;


}
