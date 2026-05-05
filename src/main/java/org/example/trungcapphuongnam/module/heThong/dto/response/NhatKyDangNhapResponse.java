package org.example.trungcapphuongnam.module.heThong.dto.response;

import lombok.*;
import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhatKyDangNhapResponse {
    private Long id;
    private Long taiKhoanId;
    private LocalDateTime thoiGian;
    private String ipAddress;
    private String userAgent;
    private Boolean thanhCong;
    private String lyDoThatBai;
}
