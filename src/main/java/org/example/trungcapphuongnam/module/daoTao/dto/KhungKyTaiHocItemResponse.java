package org.example.trungcapphuongnam.module.daoTao.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhungKyTaiHocItemResponse {

    private Long khungKyId;

    private Integer thuTu;

    private String maKy;

    private String tenKy;

    private Boolean daCoKhungKy;

    private Boolean chuaXepKy;

    private Integer soMon;

    private BigDecimal tongTinChi;

    private BigDecimal tongGio;

    private BigDecimal gioLyThuyet;

    private BigDecimal gioThucHanh;

    private BigDecimal gioKiemTra;

    private Integer soBuoiHoc;

    private BigDecimal tyLeTaiTheoGio;

    private String mucDoTai;

    private List<String> canhBao;
}
