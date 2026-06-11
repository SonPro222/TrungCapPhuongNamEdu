package org.example.trungcapphuongnam.module.daoTao.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhungKyGoiYItemResponse {

    private Long khungKyId;

    private Integer thuTu;

    private String maKy;

    private String tenKy;

    private LocalDate ngayBatDauGoiY;

    private LocalDate ngayKetThucGoiY;

    private LocalDate ngayBatDauHienTai;

    private LocalDate ngayKetThucHienTai;

    private Boolean daTonTai;
}
