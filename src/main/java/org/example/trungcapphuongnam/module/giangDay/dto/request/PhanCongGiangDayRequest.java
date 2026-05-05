package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.*;
import org.example.trungcapphuongnam.common.enums.VaiTroGiangDay;

import java.time.*;
import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhanCongGiangDayRequest {
    private Long lopHocPhanId;
    private Long giaoVienId;
    private VaiTroGiangDay vaiTro;
}
