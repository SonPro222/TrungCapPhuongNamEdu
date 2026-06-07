package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocTaiLieuResponse {

    private Long id;

    private Long syllabusMonId;

    private Long taiLieuGocId;
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

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}