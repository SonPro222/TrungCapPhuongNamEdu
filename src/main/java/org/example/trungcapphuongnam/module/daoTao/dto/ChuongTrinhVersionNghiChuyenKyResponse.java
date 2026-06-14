package org.example.trungcapphuongnam.module.daoTao.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhVersionNghiChuyenKyResponse {

    private Long id;

    private Long chuongTrinhVersionId;

    private Integer tuKyThu;

    private Integer denKyThu;

    private Integer soNgayNghi;

    private LocalDate ngayBatDauNghiGoiY;

    private LocalDate ngayKetThucNghiGoiY;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
