package org.example.trungcapphuongnam.module.diem.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiNopSinhVienRequest {
    @NotNull(message = "baiKiemTraId không được để trống")
    private Long baiKiemTraId;

    @NotNull(message = "sinhVienId không được để trống")
    private Long sinhVienId;

    private String noiDungBaiLam;

    private String fileDinhKem;

    private LocalDateTime thoiGianNop;

    private String trangThai;


}
