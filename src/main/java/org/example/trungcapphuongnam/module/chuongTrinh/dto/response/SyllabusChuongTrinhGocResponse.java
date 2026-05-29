package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusChuongTrinhGocResponse {

    private Long id;

    private String ma;

    private String ten;
    private String duongDan;
    private String mucTieu;


    private String doiTuongTuyenSinh;

    private String thoiGianDaoTao;

    private String khoiLuongKienThuc;

    private String dieuKienTotNghiep;

    private String phuongPhapDaoTao;

    private String phuongPhapDanhGia;

    private String huongDanThucHien;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}