package org.example.trungcapphuongnam.module.heThong.dto.request;

import lombok.*;
import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhatKyHeThongRequest {
    private Long taiKhoanId;
    private String hanhDong;
    private String bangTacDong;
    private Long banGhiId;
    private String noiDungCu;
    private String noiDungMoi;
    private String ipAddress;
    private String userAgent;
}
