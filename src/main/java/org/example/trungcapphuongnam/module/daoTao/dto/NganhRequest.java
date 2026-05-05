package org.example.trungcapphuongnam.module.daoTao.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NganhRequest {
    @NotBlank(message = "maNganh không được để trống")
    private String maNganh;

    @NotBlank(message = "tenNganh không được để trống")
    private String tenNganh;

    private String moTa;


}
