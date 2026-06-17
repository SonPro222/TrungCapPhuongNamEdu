package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocTaiLieuRequest {

    private Long syllabusMonId;

    private Long taiLieuMauId;
    private String ma;

    private String ten;

    private String loai;

    private String tacGia;

    private String nhaXuatBan;

    private Integer namXuatBan;

    private String duongDan;


    private Integer thuTu;

    private Boolean batBuoc;

    private String ghiChu;
}