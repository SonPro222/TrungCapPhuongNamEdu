package org.example.trungcapphuongnam.module.thi.dto.response;

import lombok.*;
import org.example.trungcapphuongnam.module.thi.enums.SinhVienDuThiTrangThai;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SinhVienDuThiResponse {
    private Long id;
    private Long lichThiId;
    private Long sinhVienId;
    private Boolean duDieuKienThi;
    private String lyDoKhongDuDieuKien;
    private SinhVienDuThiTrangThai trangThai;
}
