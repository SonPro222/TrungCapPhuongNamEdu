package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.*;
import org.example.trungcapphuongnam.common.enums.TrangThaiDiemDanh;

import java.time.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiemDanhResponse {
    private Long id;
    private Long lichHocId;
    private Long sinhVienId;
    private TrangThaiDiemDanh trangThai;
    private String ghiChu;
    private LocalDateTime thoiGianDiemDanh;
}
