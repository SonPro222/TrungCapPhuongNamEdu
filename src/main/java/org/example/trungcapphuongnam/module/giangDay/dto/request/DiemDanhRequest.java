package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiDiemDanh;

import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiemDanhRequest {
    private Long lichHocId;
    private Long sinhVienId;
    private TrangThaiDiemDanh trangThai;
    private String ghiChu;
    private LocalDateTime thoiGianDiemDanh;
}
