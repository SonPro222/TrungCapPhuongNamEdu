package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.*;
import org.example.trungcapphuongnam.common.enums.TrangThaiLichHoc;

import java.time.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichHocResponse {
    private Long id;
    private Long lopHocPhanId;
    private Long giaoVienId;
    private Long phongHocId;
    private Long caHocId;
    private LocalDate ngayHoc;
    private String noiDungBuoiHoc;
    private TrangThaiLichHoc trangThai;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
