package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DieuKienTotNghiepGocRequest {

    private String ma;
    private String noiDung;
    private String ghiChu;
}
