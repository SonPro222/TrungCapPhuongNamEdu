package org.example.trungcapphuongnam.module.daoTao.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LopHanhChinhResponse {
    private Long id;

    private String maLop;

    private String tenLop;

    private Long chuongTrinhVersionId;

    private Long khoaDaoTaoId;

    private Integer siSo;

    private String trangThai;

    private String ghiChu;

    private LocalDate ngayBatDauNhanSinhVien;

    private LocalDate ngayKetThucNhanSinhVien;

    private Boolean daChotTuyenSinh;

    private LocalDateTime ngayChotTuyenSinh;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
