package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.*;
import java.time.*;
import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaHocRequest {
    private String maCa;
    private String tenCa;
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;
    private String moTa;
}
