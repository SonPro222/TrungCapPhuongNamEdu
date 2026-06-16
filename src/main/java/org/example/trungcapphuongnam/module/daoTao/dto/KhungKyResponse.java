package org.example.trungcapphuongnam.module.daoTao.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhungKyResponse {

    private Long id;

    private Long chuongTrinhVersionId;

    private String maKy;

    private String tenKy;

    private Integer thuTu;

    private String moTa;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate ngayBatDau;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate ngayKetThuc;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Enriched fields — ngữ cảnh đầy đủ cho dropdown giảng viên
    private String maVersion;
    private String tenVersion;
    private Long chuongTrinhId;
    private String tenChuongTrinh;
    private Long nganhId;
    private String tenNganh;
    private String labelDayDu;
}