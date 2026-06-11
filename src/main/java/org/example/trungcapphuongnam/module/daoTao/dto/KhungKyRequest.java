package org.example.trungcapphuongnam.module.daoTao.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhungKyRequest {

    @NotNull(message = "chuongTrinhVersionId không được để trống")
    private Long chuongTrinhVersionId;

    private String maKy;

    private String tenKy;

    private Integer thuTu;

    private String moTa;

    private LocalDate ngayBatDau;

    private LocalDate ngayKetThuc;
}