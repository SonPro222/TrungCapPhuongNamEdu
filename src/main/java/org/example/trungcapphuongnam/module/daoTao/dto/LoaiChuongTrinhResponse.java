package org.example.trungcapphuongnam.module.daoTao.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoaiChuongTrinhResponse {
    private Long id;

    private String maLoai;

    private String tenLoai;

    private Integer soThang;

    private Integer soKy;

    private String moTa;


}
