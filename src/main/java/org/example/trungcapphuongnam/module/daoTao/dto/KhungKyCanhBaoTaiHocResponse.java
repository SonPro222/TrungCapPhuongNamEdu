package org.example.trungcapphuongnam.module.daoTao.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhungKyCanhBaoTaiHocResponse {

    private Long chuongTrinhVersionId;

    private Integer soKy;

    private Integer tongSoMon;

    private Integer soMonDaXepKy;

    private Integer soMonChuaXepKy;

    private BigDecimal tongTinChi;

    private BigDecimal tongGio;

    private BigDecimal gioLyThuyet;

    private BigDecimal gioThucHanh;

    private BigDecimal gioKiemTra;

    private Integer tongSoBuoiHoc;

    private BigDecimal soMonTrungBinhMoiKy;

    private BigDecimal tinChiTrungBinhMoiKy;

    private BigDecimal gioTrungBinhMoiKy;

    private BigDecimal buoiHocTrungBinhMoiKy;

    private BigDecimal nguongQuaTaiTheoGio;

    private BigDecimal nguongQuaTaiTheoTinChi;

    private BigDecimal nguongNheTaiTheoGio;

    private List<String> canhBaoChung;

    private List<KhungKyTaiHocItemResponse> danhSachKy;
}
