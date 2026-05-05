package org.example.trungcapphuongnam.module.thi.dto.response;

import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.Thi.BaiThiSinhVienTrangThai;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiThiSinhVienResponse {
    private Long id;
    private Long deThiId;
    private Long sinhVienId;
    private String noiDungBaiLam;
    private String fileBaiLam;
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianNop;
    private BaiThiSinhVienTrangThai trangThai;
}
