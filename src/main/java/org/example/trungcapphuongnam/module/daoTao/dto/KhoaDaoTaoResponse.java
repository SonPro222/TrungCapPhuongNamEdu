package org.example.trungcapphuongnam.module.daoTao.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhoaDaoTaoResponse {
    private Long id;

    private String maKhoa;

    private String tenKhoa;

    private Integer namBatDau;

    private Integer namKetThuc;

    private String ghiChu;


}
