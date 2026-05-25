package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.VaiTroGiangDay;


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
