package org.example.trungcapphuongnam.module.daoTao.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class KhungKyResponse {

    private Long id;

    private Long chuongTrinhVersionId;

    private Long loaiChuongTrinhId;

    private Long khungKyGocId;

    private String maKy;

    private String tenKy;

    private Integer thuTu;
}