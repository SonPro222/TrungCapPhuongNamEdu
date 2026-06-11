package org.example.trungcapphuongnam.module.daoTao.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhungKyGoiYResponse {

    private Long chuongTrinhVersionId;

    private Long chuongTrinhId;

    private Long nganhHeDaoTaoId;

    private Integer soThang;

    private Integer soKy;

    private LocalDate ngayApDung;

    private LocalDate ngayHetHieuLucGoiY;

    private Integer soKyDaTao;

    private Integer soKyConThieu;

    private Boolean daTaoDuKy;

    private KhungKyGoiYItemResponse kyTiepTheoGoiY;

    private List<KhungKyGoiYItemResponse> danhSachKy;

    private String message;
}
