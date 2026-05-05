package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.*;
import java.time.*;
import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaHocResponse {
    private Long id;
    private String maCa;
    private String tenCa;
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;
    private String moTa;
}
