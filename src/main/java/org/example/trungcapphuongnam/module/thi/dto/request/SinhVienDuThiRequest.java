package org.example.trungcapphuongnam.module.thi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.trungcapphuongnam.module.thi.enums.SinhVienDuThiTrangThai;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SinhVienDuThiRequest {
    @NotNull
    private Long lichThiId;
    @NotNull
    private Long sinhVienId;
    private Boolean duDieuKienThi;
    private String lyDoKhongDuDieuKien;
    private SinhVienDuThiTrangThai trangThai;
}
