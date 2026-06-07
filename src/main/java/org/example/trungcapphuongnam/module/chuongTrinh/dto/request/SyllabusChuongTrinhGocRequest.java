package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusChuongTrinhGocRequest {

    private Long chuongTrinhVersionId;

    private String ma;

    private String ten;

    private String mucTieu;

    private String duongDan;

    private String doiTuongTuyenSinh;

    private String thoiGianDaoTao;

    private String khoiLuongKienThuc;

    private String dieuKienTotNghiep;

    private String phuongPhapDaoTao;

    private String phuongPhapDanhGia;

    private String huongDanThucHien;

    private String ghiChu;
}