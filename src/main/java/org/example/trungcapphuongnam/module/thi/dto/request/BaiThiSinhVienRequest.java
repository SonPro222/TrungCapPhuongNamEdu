package org.example.trungcapphuongnam.module.thi.dto.request;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.Thi.BaiThiSinhVienTrangThai;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiThiSinhVienRequest {
    @NotNull
    private Long deThiId;
    @NotNull
    private Long sinhVienId;
    private String noiDungBaiLam;
    private String fileBaiLam;
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianNop;
    private BaiThiSinhVienTrangThai trangThai;
}
