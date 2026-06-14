package org.example.trungcapphuongnam.module.daoTao.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhVersionNghiChuyenKyRequest {

    private Integer tuKyThu;

    private Integer denKyThu;

    private Integer soNgayNghi;

    private String ghiChu;
}
