package org.example.trungcapphuongnam.module.daoTao.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrinhDoDaoTaoRequest {
    @NotBlank(message = "maTrinhDo không được để trống")
    private String maTrinhDo;

    @NotBlank(message = "tenTrinhDo không được để trống")
    private String tenTrinhDo;

    private String moTa;


}
