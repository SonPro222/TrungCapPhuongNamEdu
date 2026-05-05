package org.example.trungcapphuongnam.module.daoTao.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LopHanhChinhRequest {
    @NotBlank(message = "maLop không được để trống")
    private String maLop;

    @NotBlank(message = "tenLop không được để trống")
    private String tenLop;

    @NotNull(message = "chuongTrinhVersionId không được để trống")
    private Long chuongTrinhVersionId;

    private Long khoaDaoTaoId;

    @PositiveOrZero(message = "siSo không được âm")
    private Integer siSo;

    private String trangThai;

    private String ghiChu;


}
