package org.example.trungcapphuongnam.module.diem.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiNopSinhVienResponse {
    private Long id;

    private Long baiKiemTraId;

    private Long sinhVienId;

    private String noiDungBaiLam;

    private String fileDinhKem;

    private LocalDateTime thoiGianNop;

    private String trangThai;


}
