package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaiLieuGocResponse {

    private Long id;

    private String ma;

    private String ten;

    private String loai;

    private String tacGia;

    private String nhaXuatBan;

    private Integer namXuatBan;

    private String duongDan;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}