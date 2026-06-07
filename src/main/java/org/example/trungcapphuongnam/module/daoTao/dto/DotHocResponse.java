package org.example.trungcapphuongnam.module.daoTao.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DotHocResponse {
    private Long id;
    private String maDot;
    private String tenDot;
    private Long khoaDaoTaoId;
    private Long lopHanhChinhId;
    private Long chuongTrinhVersionId;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private String trangThai;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
