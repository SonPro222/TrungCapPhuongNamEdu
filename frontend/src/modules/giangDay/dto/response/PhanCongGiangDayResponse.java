package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.VaiTroGiangDay;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhanCongGiangDayResponse {
    private Long id;

    private Long lopHocPhanId;
    private String maLop;
    private String tenLop;

    private Long giaoVienId;
    private String maGiaoVien;
    private String tenGiaoVien;

    private VaiTroGiangDay vaiTro;
    private LocalDateTime createdAt;
}