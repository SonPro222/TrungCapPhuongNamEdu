package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaiLieuMauRequest {

    private String ma;

    private String ten;

    private String loai;

    private String tacGia;

    private String nhaXuatBan;

    private Integer namXuatBan;

    private String duongDan;

    private String ghiChu;
}