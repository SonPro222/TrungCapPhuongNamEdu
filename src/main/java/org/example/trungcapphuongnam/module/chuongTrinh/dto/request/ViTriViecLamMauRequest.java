package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViTriViecLamGocRequest {

    private Long chuongTrinhId;
    private String ma;
    private String ten;
    private String moTa;
    private String ghiChu;
}
